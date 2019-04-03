package com.marklordan.connect;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Service
public class ConnectionUtil {

    private static final Log log = LogFactory.getLog(ConnectionUtil.class.getSimpleName());

    @Value("${driver.details.url}")
    private String driverBusAssignmentUrl;

    @Value("${driver.details.url.timeout}")
    private int requestTimeout;

    @Value("${driver.details.url.retry.count}")
    private int maxRetryAttempts;

    RestTemplate restTemplate;

    public JsonNode retrieveDriverJson(){
        log.info("Initiating connection to: " + driverBusAssignmentUrl);

        JsonNode jsonResponse = null;
        int retryCount = 0;
        do{
            try {
                log.info("Trying to retrieve JSON, attempt #" + (retryCount+1));
                jsonResponse = getRestTemplate().getForObject(driverBusAssignmentUrl, JsonNode.class);
                jsonResponse = jsonResponse.get("results");
            } catch(RestClientException e){
                retryCount++;
                logExceptionDetails(e, retryCount);
            }
        }while (jsonResponse == null && retryCount < maxRetryAttempts);

        return jsonResponse;
    }

    private ClientHttpRequestFactory getClientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory
                = new HttpComponentsClientHttpRequestFactory();

        log.info("Setting connectionTimeout to: " + requestTimeout + "ms");
        clientHttpRequestFactory.setConnectTimeout(requestTimeout);
        clientHttpRequestFactory.setReadTimeout(requestTimeout);
        return clientHttpRequestFactory;
    }

    private RestTemplate getRestTemplate() {
        if(restTemplate == null){
            restTemplate = new RestTemplate(getClientHttpRequestFactory());
        }
        return restTemplate;
    }

    private void logExceptionDetails(RestClientException e, int retryCount) {
        log.warn("RestClientException encountered during connection attempt");
        if(retryCount == maxRetryAttempts){
            log.error("Failed to retrieve JSON data from endpoint", e);
        }
    }
}
