package org.example;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        final String sensorName = "TestSensor";
        registerNewSensor(sensorName);

        Random random = new Random();

        double minTemperature = -15d;
        double maxTemperature = 15d;
        double diff = maxTemperature - minTemperature;

        for(int i = 0; i <= 500; i++) {
            double value = random.nextDouble() * diff + minTemperature;
            sendNewMeasurement(Math.round(value * 10d)/10d, random.nextBoolean(), sensorName);
        }

        }

        private static void registerNewSensor(String sensorName) {
            final String url = "http://localhost:8080/sensors/register";

            Map<String, Object> sensorJson = new HashMap<>();
            sensorJson.put("name", sensorName);

            makePostRequest(url, sensorJson);
        }

        private static void sendNewMeasurement(double value, boolean raining, String sensorName) {
            final String url = "http://localhost:8080/measurements/add";

            Map<String, Object> measurementJson = new HashMap<>();
            Map<String, Object> sensorJson = new HashMap<>();
            measurementJson.put("value", value);
            measurementJson.put("raining", raining);
            sensorJson.put("name", sensorName);
            measurementJson.put("sensor", sensorJson);


            makePostRequest(url, measurementJson);
        }

        private static void  makePostRequest(String url, Map<String, Object> json) {
        final RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> httpEntity = new HttpEntity<Object>(json, httpHeaders);

        try {
           String result = restTemplate.postForObject(url, json, String.class);
        } catch (HttpClientErrorException e) {
            System.out.println(e.getMessage());
        }

        }


}
