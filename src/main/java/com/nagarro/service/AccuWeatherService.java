package com.nagarro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nagarro.model.AccuWeatherLocationResponse;
import com.nagarro.model.AccuWeatherResponse;

@Service
public class AccuWeatherService {
	@Value("${accuweathermap.api.key}")
	private String accuWeatherApiKey;
	private final RestTemplate restTemplate;
	@Autowired
	public AccuWeatherService(RestTemplate restTemplate){
		this.restTemplate=restTemplate;
	}
	public String getLocationKey(String city) {
		String accuWeatherUrl="https://dataservice.accuweather.com/locations/v1/search?q={city}&apikey={apiKey}";
		
		AccuWeatherLocationResponse[] locationResponse=restTemplate.getForObject(
				accuWeatherUrl, 
				AccuWeatherLocationResponse[].class,
				city,
				accuWeatherApiKey);
		
		if(locationResponse!=null&&locationResponse.length>0) {
			return locationResponse[0].getKey();
		}else {
			throw new RuntimeException("Unable to fetch location key from AccuWeather Api");
		}
	}
	public AccuWeatherResponse getWeatherInfo(String locationKey) {
		String accuWeatherUrl="https://dataservice.accuweather.com/currentconditions/v1/{locationKey}?apikey={apiKey}";
		AccuWeatherResponse[] weatherResponse=restTemplate.getForObject(
				accuWeatherUrl,
				AccuWeatherResponse[].class,
				locationKey,
				accuWeatherApiKey
				);
		if(weatherResponse!=null&&weatherResponse.length>0) {
			return weatherResponse[0];
		}else {
			throw new RuntimeException("Unable to fetch weather information from Accuweather Api");
		}
	}
	public String getWeatherInformation(String accuLocationKey) {
		try {
			String accuWeatherUrl="https://dataservice.accuweather.com/currentconditions/v1/{locationKey}?apikey={apiKey}";
			
			return restTemplate.getForObject(accuWeatherUrl,
					String.class,
					accuLocationKey,
					accuWeatherApiKey
					);
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
}
