package com.marklordan.model;

import lombok.Data;

@Data
public class Location extends BaseObject{

    private long locationId;
    private String locationName;

    public Location(){

    }

    public Location(long locationId, String location_name) {

        this.locationId = locationId;
        setLocationName(location_name);
    }

    public void setLocationName(String locationName){
        this.locationName = trimAndCapitalise(locationName);
    }
}
