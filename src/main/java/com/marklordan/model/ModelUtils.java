package com.marklordan.model;

/**
 * Created by Mark on 03/04/2019.
 */
public class ModelUtils {
    
    public static boolean compareDriverRecords(Driver d, Driver driverFromDb) {
        boolean hasChanged = false;
        if(!d.getFirstName().equals(driverFromDb.getFirstName())){
            hasChanged = true;
        }
        else if(d.getMiddleInit() != driverFromDb.getMiddleInit()){
            hasChanged = true;
        }
        else if(!d.getLastName().equals(driverFromDb.getLastName())){
            hasChanged = true;
        }
        else if(d.getOperatorClass() != driverFromDb.getOperatorClass()){
            hasChanged = true;
        }
        return hasChanged;
    }

    public static boolean compareCarrierRecords(Carrier carrier, Carrier carrierFromDb) {
        boolean hasChanged = false;
        if(!carrier.getCarrierName().equals(carrierFromDb.getCarrierName())){
            hasChanged = true;
        }
        return hasChanged;
    }

    public static boolean compareLocationRecords(Location location, Location locationFromDb) {
        boolean hasChanged = false;
        if(!location.getLocationName().equals(locationFromDb.getLocationName())){
            hasChanged = true;
        }
        else if(location.getLocationId() != locationFromDb.getLocationId()){
            hasChanged = true;
        }
        return hasChanged;
    }
}
