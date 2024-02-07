package com.nagarro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nagarro.model.OpenWeatherGeolocationResponse;
import com.nagarro.model.OpenWeatherResponse;

@Service
public class OpenWeatherService {
	@Value("${openweathermap.api.key}")
	private String openWeatherApiKey;
	private final RestTemplate restTemplate;

	@Autowired
	public OpenWeatherService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public OpenWeatherGeolocationResponse getGeolocationInfo(String zipCode, String countryCode) {
		try {
			String openWeatherGeolocationUrl = "http://api.openweathermap.org/geo/1.0/zip?zip={zipCode},{countryCode}&appid={apiKey}";

			return restTemplate.getForObject(openWeatherGeolocationUrl, OpenWeatherGeolocationResponse.class, zipCode,
					countryCode, openWeatherApiKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public OpenWeatherResponse getWeatherInfo(double lat, double lon) {
		try {
			String openWeatherResponseUrl = "https://api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={apiKey}";
			return restTemplate.getForObject(openWeatherResponseUrl, OpenWeatherResponse.class, lat, lon,
					openWeatherApiKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getGeolocatorInfo(String zip, String country) {
		try {
			String openWeatherUrl = "http://api.openweathermap.org/geo/1.0/zip?zip={zip},{country}&appid={apiKey}";
			return restTemplate.getForObject(openWeatherUrl, String.class, zip, country, openWeatherApiKey);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
