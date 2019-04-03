package com.marklordan.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Driver extends BaseObject{

    private long driverId;
    private String firstName;
    private char middleInit;
    private String lastName;
    private char operatorClass;
    private Carrier carrier;
    private Location location;

    public Driver(){

    }

    public Driver(long driverId, String firstName, char middleInit, String lastName,
                  char operatorClass, Carrier carrier, Location location) {

        this.driverId = driverId;
        setFirstName(firstName);
        this.middleInit = middleInit;
        setLastName(lastName);
        this.operatorClass = operatorClass;
        this.carrier = carrier;
        this.location = location;
    }

    @JsonCreator
    @JsonIgnoreProperties(ignoreUnknown = true)
    public Driver(@JsonProperty("oper_nbr") long driver_id,
                  @JsonProperty("first_name") String first_name,
                  @JsonProperty("middle_init") char middle_init,
                  @JsonProperty("last_name") String last_name,
                  @JsonProperty("oper_class") char oper_class,
                  @JsonProperty("carrier_cd") String carrier_name,
                  @JsonProperty("home_loc_6") long location_id,
                  @JsonProperty("home_loc_3") String locationName){

        this(driver_id, first_name, middle_init, last_name, oper_class,
                new Carrier(carrier_name),
                new Location(location_id, locationName));
    }

    public void setFirstName(String firstName) {
        this.firstName = trimAndCapitalise(firstName);
    }

    public void setLastName(String lastName) {
        this.lastName = trimAndCapitalise(lastName);
    }





}
