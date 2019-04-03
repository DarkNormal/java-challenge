package com.marklordan.model;

import lombok.Data;

@Data
public class Carrier extends BaseObject{

    private String carrierName;

    public Carrier(){

    }

    public Carrier(String carrierName) {
        this.carrierName = trimAndCapitalise(carrierName);
    }

    public void setCarrierName(String carrierName){
        this.carrierName = trimAndCapitalise(carrierName);
    }
}
