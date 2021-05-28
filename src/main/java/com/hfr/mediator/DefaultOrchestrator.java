package com.hfr.mediator;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Collections;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.CoreResponse.Orchestration;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class DefaultOrchestrator extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final MediatorConfig config;


    public DefaultOrchestrator(MediatorConfig config) {
        this.config = config;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            
            String body = ((MediatorHTTPRequest) msg).getBody();
            SourceMsg SrcMessage = new Gson().fromJson(body, SourceMsg.class);
            
            // Desrialize the object
            String convertedMessage = new Gson().toJson(SrcMessage);
            JSONObject connectionProperties = new JSONObject(config.getDynamicConfig()).getJSONObject("hprs");
            String url = "" + connectionProperties.get("scheme") + connectionProperties.get("host") + ":" + connectionProperties.get("port")+connectionProperties.get("path");
            HashMap<String, String> header = new HashMap<>();
            String username = String.valueOf(connectionProperties.get("username"));
            String password = String.valueOf(connectionProperties.get("password"));

            String credentials = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(credentials.getBytes(StandardCharsets.ISO_8859_1));
            String authHeader = "Basic " + new String(encodedAuth);
            header.put("Authorization", authHeader);
            header.put("Content-Type", "application/json");
            MediatorHTTPRequest hprsRequest = new MediatorHTTPRequest(((MediatorHTTPRequest) msg).getRequestHandler(),
             getSelf(),
             "Sending Data from mediator to HPRS", "POST",
             url,
             convertedMessage,
             header,
             null);
             

             ActorSelection httpConnector = getContext().actorSelection(config.userPathFor("http-connector"));
             httpConnector.tell(hprsRequest, getSelf());

            // FinishRequest finishRequest = new FinishRequest(convertedMessage, "application/json", HttpStatus.SC_OK);
            // ((MediatorHTTPRequest) msg).getRequestHandler().tell(finishRequest, getSelf());

        } else if (msg instanceof MediatorHTTPResponse) {
            ((MediatorHTTPResponse) msg)
                    .getOriginalRequest()
                    .getRequestHandler()
                    .tell(((MediatorHTTPResponse) msg).toFinishRequest(), getSelf());
        } else {
            unhandled(msg);
        }
    }
}
