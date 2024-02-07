package com.nagarro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nagarro.model.WeatherAggregateResponse;
import com.nagarro.model.WeatherInfo;
import com.nagarro.service.AccuWeatherService;
import com.nagarro.service.WeatherAggregatorService;

@RestController
@RequestMapping("/weather")
public class WeatherController {
	@Autowired
	private final WeatherAggregatorService weatherAggregatorService;
	@Autowired
	private AccuWeatherService accuWeatherService;
	@Autowired
	public WeatherController(WeatherAggregatorService weatherAggregatorService) {
		this.weatherAggregatorService = weatherAggregatorService;
	}
	@GetMapping
	public ResponseEntity<WeatherAggregateResponse> getWeather(@RequestParam String city, @RequestParam String zip,
			@RequestParam String country) {
		WeatherAggregateResponse response = weatherAggregatorService.getWeatherAggregate(city, zip, country);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	@GetMapping("/locationKey")
	public String getLocationKey(@RequestParam String city) {
		try {
			String locationKey = accuWeatherService.getLocationKey(city);
			System.out.println("Location Key is :" + locationKey);
			return locationKey;
		} catch (Exception e) {
			e.printStackTrace();
			return "Error getting location key";
		}
	}
	@GetMapping("/weatherInfo")
	public ResponseEntity<WeatherInfo> getWeatherInfo(@RequestParam String city, @RequestParam String zip) {
		try {
			WeatherInfo weatherInfo = weatherAggregatorService.getAggregatedWeather(city, zip);
			System.out.println("Weather information :" + weatherInfo);
			return new ResponseEntity<>(weatherInfo,HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
