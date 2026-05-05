package net.abdullahjutt.journalApp.service;

import net.abdullahjutt.journalApp.api.response.WheatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WheatherService {
    @Value("${weather.api.key}")
    private String apikey;
    public static  final String API="https://api.weatherstack.com/current?access_key=API_KEY&query=CITY";

    @Autowired
    private RestTemplate restTemplate;

    public WheatherResponse getWheather(String city){
        String finalAPI=API.replace("CITY",city).replace("API_KEY",apikey);
       ResponseEntity<WheatherResponse> response= restTemplate.exchange(finalAPI,HttpMethod.GET,null, WheatherResponse.class);
      WheatherResponse body= response.getBody();
      return body;
    }
}
