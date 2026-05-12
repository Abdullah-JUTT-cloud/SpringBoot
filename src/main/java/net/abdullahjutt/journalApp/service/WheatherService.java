package net.abdullahjutt.journalApp.service;

import net.abdullahjutt.journalApp.api.response.WheatherResponse;
import net.abdullahjutt.journalApp.cache.AppCache;
import net.abdullahjutt.journalApp.constants.Placeholders;
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
    @Autowired
    private AppCache appCache;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    public WheatherResponse getWheather(String city){
         String cacheKey = "Weather_of_" + city;
         WheatherResponse wheatherResponse = redisService.get(cacheKey, WheatherResponse.class);
         if(wheatherResponse !=null){
             return wheatherResponse;
         }else {
             String finalAPI=appCache.appCache.get(AppCache.keys.WEATHER_API.toString()).replace(Placeholders.CITY,city).replace(Placeholders.API_KEY,apikey);
             ResponseEntity<WheatherResponse> response= restTemplate.exchange(finalAPI,HttpMethod.GET,null, WheatherResponse.class);
             WheatherResponse body= response.getBody();
             if(body!=null){
                 redisService.set(cacheKey,body,300l);
             }
             return body;
         }

    }
}
