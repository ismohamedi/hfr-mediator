package com.hfr.mediator;
import com.google.gson.annotations.SerializedName;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SourceMsg {
    String Fac_IDNumber;

    @SerializedName(value="name", alternate={"Name"})
    String Name;
    String Comm_FacName;

    @SerializedName(value="zone", alternate={"Zone"})
    String Zone;
    String Region_Code;

    @SerializedName(value="region", alternate={"Region"})
    String Region;
    String District_Code;
    
    @SerializedName(value="district", alternate={"District"})
    String District;
    String Council_Code;

    @SerializedName(value="council", alternate={"Council"})
    String Council;

    @SerializedName(value="ward", alternate={"Ward"})
    String Ward;

    @SerializedName(value="village", alternate={"Village"})
    String Village;
    String FacilityTypeGroupCode;
    String FacilityTypeGroup;
    String FacilityTypeCode;
    String FacilityType;
    String OwnershipGroupCode;
    String OwnershipGroup;
    String OwnershipCode;
    String Ownership;
    String OperatingStatus;

    @SerializedName(value="latitude", alternate={"Latitude"})
    String Latitude;

    @SerializedName(value="longitude", alternate={"Longitude"})
    String Longitude;
    String RegistrationStatus;
    String OpenedDate;
    String CreatedAt;
    String UpdatedAt;
}

