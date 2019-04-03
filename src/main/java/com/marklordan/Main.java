package com.marklordan;

import com.fasterxml.jackson.databind.JsonNode;
import com.marklordan.connect.ConnectionUtil;
import com.marklordan.service.DriverService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getSimpleName());
    private static ApplicationContext context;


    public static void main(String[] args) {
        context = new ClassPathXmlApplicationContext("application-context.xml");

        ConnectionUtil connectionUtil = (ConnectionUtil) context.getBean("connectionUtil");

        JsonNode resultSet = connectionUtil.retrieveDriverJson();
        log.info("Number of Driver records to process: " + resultSet.size());

        DriverService driverService = (DriverService) context.getBean("driverService");
        driverService.processJsonResultset(resultSet);

        if(driverService.hasDataBeenLoadedPreviously()){
            log.info("Data has been loaded previously, checking for required updates");
            driverService.updateDbRecords();
        }
        else{
            driverService.persistRecordsToDb();
        }
        HibernateSessionFactory.shutdown();
    }
}
