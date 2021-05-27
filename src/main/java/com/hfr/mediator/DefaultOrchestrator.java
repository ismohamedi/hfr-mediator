package com.hfr.mediator;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.apache.http.HttpStatus;
import org.openhim.mediator.engine.MediatorConfig;
import org.openhim.mediator.engine.messages.FinishRequest;
import org.openhim.mediator.engine.messages.MediatorHTTPRequest;

public class DefaultOrchestrator extends UntypedActor {
    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final MediatorConfig config;


    public DefaultOrchestrator(MediatorConfig config) {
        this.config = config;
    }

    @Override
    public void onReceive(Object msg) throws Exception {
        if (msg instanceof MediatorHTTPRequest) {
            FinishRequest finishRequest = new FinishRequest(((MediatorHTTPRequest) msg).getBody(), "json/json", HttpStatus.SC_OK);
            ((MediatorHTTPRequest) msg).getRequestHandler().tell(finishRequest, getSelf());
        } else {
            unhandled(msg);
        }
    }
}
