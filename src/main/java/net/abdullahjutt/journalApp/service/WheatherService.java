package net.abdullahjutt.journalApp.service;

import net.abdullahjutt.journalApp.api.response.WheatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WheatherService {
    public static  final String apikey="9f2f6e995877bef1598d3ccaddec8f16";
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
