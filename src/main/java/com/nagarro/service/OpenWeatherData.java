package com.nagarro.service;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.model.OpenWeatherResponse;
public class OpenWeatherData {
	public static double getLatitude(String openGeocoderInfo) {
		try {
			ObjectMapper objectMapper=new ObjectMapper();
			JsonNode rootNode=objectMapper.readTree(openGeocoderInfo);
			
			return rootNode.path("lat").asDouble();
		}catch(IOException e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	public static double getLongitude(String openGeoCoderInfo) {
		try {
			ObjectMapper objectMapper=new ObjectMapper();
			JsonNode rootNode=objectMapper.readTree(openGeoCoderInfo);
			
			return rootNode.path("lon").asDouble();
		}catch(IOException e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	public static OpenWeatherResponse getOpenWeatherResponse(String openWeatherDetails) {
		try {
			ObjectMapper objectMappper=new ObjectMapper();
			JsonNode rootNode=objectMappper.readTree(openWeatherDetails);
			 double temperature = rootNode.path("main").path("temp").asDouble();
	            double feelsLike = rootNode.path("main").path("feels_like").asDouble();
	            int pressure = rootNode.path("main").path("pressure").asInt();
	            int humidity = rootNode.path("main").path("humidity").asInt();
	            int visibility = rootNode.path("visibility").asInt();
	            double windSpeed = rootNode.path("wind").path("speed").asDouble();
	            int windDegree = rootNode.path("wind").path("deg").asInt();
	            double windGust = rootNode.path("wind").path("gust").asDouble();
	            long sunrise = rootNode.path("sys").path("sunrise").asLong();
	            long sunset = rootNode.path("sys").path("sunset").asLong();
	            return new OpenWeatherResponse(temperature, feelsLike, pressure, humidity, visibility, windSpeed, windDegree, windGust, sunrise, sunset);
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
