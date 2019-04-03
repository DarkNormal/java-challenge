package com.marklordan.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marklordan.dao.CarrierDaoImpl;
import com.marklordan.dao.DriverDaoImpl;
import com.marklordan.dao.LocationDaoImpl;
import com.marklordan.model.Carrier;
import com.marklordan.model.Driver;
import com.marklordan.model.Location;
import com.marklordan.model.ModelUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DriverService {

    private static final Log log = LogFactory.getLog(DriverService.class.getSimpleName());

    @Autowired
    private DriverDaoImpl driverDao;

    @Autowired
    private CarrierDaoImpl carrierDao;

    @Autowired
    private LocationDaoImpl locationDao;

    private List<Driver> driverRecords = new ArrayList<>();


    public void processJsonResultset(JsonNode resultSet){
        log.info("Beginning processJsonResultSet");
        ObjectMapper mapper = new ObjectMapper();

        for (int i = 0; i < resultSet.size(); i++) {
            Driver driver = mapper.convertValue(resultSet.get(i), Driver.class);
            driverRecords.add(driver);
        }
    }

    public void persistRecordsToDb(){
        log.info("Persisting records to DB");
        for(Driver driver : driverRecords){
            persistRecordToDb(driver);
        }
    }

    private void persistRecordToDb(Driver driver){
        saveOrSetCarrier(driver);
        saveOrSetLocation(driver);
        driverDao.save(driver);
    }

    private void saveOrSetLocation(Driver driver) {
        Location locationFromDb = locationDao.getLocationByLocationId(driver.getLocation().getLocationId());
        if(locationFromDb == null){
            log.info("Persisting location with name: " + driver.getLocation().getLocationName());
            locationDao.save(driver.getLocation());
        }
        else{
            log.info("Location already persisted to DB");
            driver.setLocation(locationFromDb);
        }
    }

    private void saveOrSetCarrier(Driver driver) {
        Carrier carrierFromDb = carrierDao.getCarrierByName(driver.getCarrier().getCarrierName());
        if(carrierFromDb == null){
            log.info("Persisting carrier with name: " + driver.getCarrier().getCarrierName());
            carrierDao.save(driver.getCarrier());
        }
        else{
            log.info("Carrier already persisted to DB");
            driver.setCarrier(carrierFromDb);
        }
    }

    public boolean hasDataBeenLoadedPreviously() {
        Driver driver = driverDao.getSingleRecord();
        if(driver != null){
            log.info("Data present in DB");
            return true;
        }
        return false;
    }

    public void updateDbRecords() {
        log.info("Comparing all JSON objects to persisted objects");
        for(Driver driver: driverRecords){
            Driver driverFromDb = driverDao.getDriverByDriverId(driver.getDriverId());
            if(driverFromDb != null){
                if(ModelUtils.compareCarrierRecords(driver.getCarrier(), driverFromDb.getCarrier())){
                    carrierDao.update(driver.getCarrier());
                    driverFromDb.setCarrier(driver.getCarrier());
                }
                if(ModelUtils.compareLocationRecords(driver.getLocation(), driverFromDb.getLocation())){
                    locationDao.update(driver.getLocation());
                    driverFromDb.setLocation(driver.getLocation());
                }
                if(ModelUtils.compareDriverRecords(driver, driverFromDb)){
                    saveOrSetCarrier(driver);
                    saveOrSetLocation(driver);
                    copyDriverDetails(driver, driverFromDb);
                    driverDao.update(driverFromDb);
                }
            }
            else{
                persistRecordToDb(driver);
            }

        }
    }

    private void copyDriverDetails(Driver driver, Driver driverFromDb) {
        driverFromDb.setFirstName(driver.getFirstName());
        driverFromDb.setLastName(driver.getLastName());
        driverFromDb.setMiddleInit(driver.getMiddleInit());
        driverFromDb.setOperatorClass(driver.getOperatorClass());
    }
}
