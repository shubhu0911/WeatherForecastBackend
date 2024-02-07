package com.nagarro.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nagarro.model.AccuWeatherResponse;
import com.nagarro.model.OpenWeatherGeolocationResponse;
import com.nagarro.model.OpenWeatherResponse;
import com.nagarro.model.OpenWeatherResponse.Clouds;
import com.nagarro.model.OpenWeatherResponse.Coordinates;
import com.nagarro.model.OpenWeatherResponse.Main;
import com.nagarro.model.OpenWeatherResponse.Sys;
import com.nagarro.model.OpenWeatherResponse.Weather;
import com.nagarro.model.OpenWeatherResponse.Wind;
import com.nagarro.model.WeatherAggregateResponse;
import com.nagarro.model.WeatherInfo;

@Service
public class WeatherAggregatorService {
	@Autowired
	private final AccuWeatherService accuWeatherService;
	@Autowired
	private final OpenWeatherService openWeatherService;
	@Autowired
	public WeatherAggregatorService(AccuWeatherService accuWeatherService, OpenWeatherService openWeatherService) {
		this.accuWeatherService = accuWeatherService;
		this.openWeatherService = openWeatherService;
	}
	public WeatherAggregateResponse getWeatherAggregate(String city, String zipCode, String countryCode) {
		String accuWeatherLocationKey = accuWeatherService.getLocationKey(city);
		AccuWeatherResponse accuWeatherResponse = accuWeatherService.getWeatherInfo(accuWeatherLocationKey);
		OpenWeatherGeolocationResponse openWeatherGeolocationResponse = openWeatherService.getGeolocationInfo(zipCode,
				countryCode);
		OpenWeatherResponse openWeatherResponse = openWeatherService
				.getWeatherInfo(openWeatherGeolocationResponse.getLat(), openWeatherGeolocationResponse.getLon());
		WeatherAggregateResponse weatherAggregateResponse = new WeatherAggregateResponse();
		AccuWeatherResponse.Temperature accuTemperature = accuWeatherResponse.getTemperature();
		if (accuTemperature != null) {
			double temperatureValue = accuTemperature.getMetric().getValue();
			String temperatureUnit = accuTemperature.getMetric().getUnit();
			weatherAggregateResponse
					.setTemperature(new WeatherAggregateResponse.Temperature(temperatureValue, temperatureUnit));
		}
		if (accuWeatherResponse.getTemperature() != null
				&& accuWeatherResponse.getTemperature().getImperial() != null) {
			double feelsLikeValue = accuWeatherResponse.getTemperature().getImperial().getValue();
			String feelsLikeUnit = accuWeatherResponse.getTemperature().getImperial().getUnit();
			WeatherAggregateResponse.FeelsLike feelsLike = new WeatherAggregateResponse.FeelsLike();
			feelsLike.setValue(feelsLikeValue);
			feelsLike.setUnit(feelsLikeUnit);
			weatherAggregateResponse.setFeelsLike(feelsLike);
		}
		if (openWeatherResponse != null) {
			OpenWeatherResponse.Main mainInfo = openWeatherResponse.getMain();
			if (mainInfo != null) {
				weatherAggregateResponse.setPressure(mainInfo.getPressure());
				weatherAggregateResponse.setHumidity(mainInfo.getHumidity());
				weatherAggregateResponse.setVisibility(openWeatherResponse.getVisibility());
			}
			OpenWeatherResponse.Wind windInfo = openWeatherResponse.getWind();
			if (windInfo != null) {
				WeatherAggregateResponse.Wind aggregatedWindInfo = new WeatherAggregateResponse.Wind();
				aggregatedWindInfo.setSpeed(windInfo.getSpeed());
				aggregatedWindInfo.setDeg(windInfo.getDeg());
				aggregatedWindInfo.setGust(windInfo.getGust());
				weatherAggregateResponse.setWind(aggregatedWindInfo);
			}
		}
		return weatherAggregateResponse;
	}
	public WeatherInfo getAggregatedWeather(String city, String zip) {
		try {
			CompletableFuture<String> accuWeatherLocationKeyFuture = CompletableFuture
					.supplyAsync(() -> accuWeatherService.getLocationKey(city));

			CompletableFuture<String> openWeatherGeolocatorInfoFuture = CompletableFuture
					.supplyAsync(() -> openWeatherService.getGeolocatorInfo(zip, "IN"));

			CompletableFuture<WeatherInfo> combinedResult = accuWeatherLocationKeyFuture
					.thenCombine(openWeatherGeolocatorInfoFuture, (accuLocationKey, openGeolocatorInfo) -> {
						String accuWeatherInfo = accuWeatherService.getWeatherInformation(accuLocationKey);

						AccuWeatherResponse accuWeatherData = extractAccuWeatherData(accuWeatherInfo);
						double latitude = getLatitude(openGeolocatorInfo);
						double longitude = getLongitude(openGeolocatorInfo);
						OpenWeatherResponse openWeatherMapDetails = openWeatherService.getWeatherInfo(latitude,
								longitude);
						OpenWeatherResponse openWeatherMapData = extractOpenWeatherData(openWeatherMapDetails);
						return transformToWeatherInfo(accuWeatherData, openWeatherMapData);
					});
			return combinedResult.get();
		} catch (Exception e) {
			e.printStackTrace();
			return new WeatherInfo();
		}
	}
	private WeatherInfo transformToWeatherInfo(AccuWeatherResponse accuWeatherData,
			OpenWeatherResponse openWeatherMapData) {
		WeatherInfo weatherInfo = new WeatherInfo();
		weatherInfo.setWeatherText(openWeatherMapData.getWeather()[0].getMain());
		weatherInfo.setHasPrecipitation(accuWeatherData.isHasPrecipitation());
		weatherInfo.setPrecipitationType(accuWeatherData.getPrecipitationType());
		weatherInfo.setDayTime(accuWeatherData.isDayTime());
		AccuWeatherResponse.Temperature accuTemperature = accuWeatherData.getTemperature();
		if (accuTemperature != null) {
			double temperatureValue = accuTemperature.getMetric().getValue();
			String temperatureUnit = accuTemperature.getMetric().getUnit();
			weatherInfo.setTemperature(new WeatherInfo.TemperatureInfo(temperatureValue, temperatureUnit));
			if (accuTemperature.getMetric() != null) {
				double feelsLikeValue = accuTemperature.getMetric().getValue();
				String feelsLikeUnit = accuTemperature.getMetric().getUnit();
				WeatherInfo.TemperatureInfo feels_Like = new WeatherInfo.TemperatureInfo(feelsLikeValue, feelsLikeUnit);
				weatherInfo.setFeelsLike(feels_Like);
			}
		}
		if (openWeatherMapData.getMain() != null) {
			weatherInfo.setPressure(openWeatherMapData.getMain().getPressure());
			weatherInfo.setHumidity(openWeatherMapData.getMain().getHumidity());
			weatherInfo.setVisibility(openWeatherMapData.getVisibility());
		}
		OpenWeatherResponse.Wind windInfo = openWeatherMapData.getWind();
		if (windInfo != null) {
			WeatherInfo.WindInfo windInfoTransformed = new WeatherInfo.WindInfo();
			windInfoTransformed.setSpeed(windInfo.getSpeed());
			windInfoTransformed.setDeg(windInfo.getDeg());
			windInfoTransformed.setGust(windInfo.getGust());
			weatherInfo.setWind(windInfoTransformed);
		}
		OpenWeatherResponse.Sys sysInfo = openWeatherMapData.getSys();
		if (sysInfo != null) {
			weatherInfo.setSunrise(sysInfo.getSunrise());
			weatherInfo.setSunset(sysInfo.getSunset());
		}
		return weatherInfo;
	}
	private OpenWeatherResponse extractOpenWeatherData(OpenWeatherResponse openWeatherMapDetails) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.valueToTree(openWeatherMapDetails);
			Coordinates coordinates = new Coordinates();
			coordinates.setLon(rootNode.path("coord").path("lon").asDouble());
			coordinates.setLat(rootNode.path("coord").path("lat").asDouble());
			JsonNode weatherNode = rootNode.path("weather");
			Weather[] weatherArray = objectMapper.treeToValue(weatherNode, Weather[].class);
			String base = rootNode.path("base").asText();
			Main main = new Main();
			main.setTemp(rootNode.path("main").path("temp").asDouble());
			main.setFeelsLike(rootNode.path("main").path("feels_like").asDouble());
			main.setTempMin(rootNode.path("main").path("temp_min").asDouble());
			main.setTempMax(rootNode.path("main").path("temp_max").asDouble());
			main.setPressure(rootNode.path("main").path("pressure").asInt());
			main.setHumidity(rootNode.path("main").path("humidity").asInt());
			main.setSeaLevel(rootNode.path("main").path("sea_level").asInt());
			main.setGroundLevel(rootNode.path("main").path("grnd_level").asInt());
			int visibility = rootNode.path("visibility").asInt();
			Wind wind = new Wind();
			wind.setSpeed(rootNode.path("wind").path("speed").asDouble());
			wind.setDeg(rootNode.path("wind").path("deg").asInt());
			wind.setGust(rootNode.path("wind").path("gust").asDouble());
			Clouds clouds = new Clouds();
			clouds.setAll(rootNode.path("clouds").path("all").asInt());
			long dt = rootNode.path("dt").asLong();
			Sys sys = new Sys();
			sys.setType(rootNode.path("sys").path("type").asInt());
			sys.setId(rootNode.path("sys").path("id").asInt());
			sys.setCountry(rootNode.path("sys").path("country").asText());
			sys.setSunrise(rootNode.path("sys").path("sunrise").asLong());
			sys.setSunset(rootNode.path("sys").path("sunset").asLong());
			int timezone = rootNode.path("timezone").asInt();
			long id = rootNode.path("id").asLong();
			String name = rootNode.path("name").asText();
			int cod = rootNode.path("cod").asInt();
			return new OpenWeatherResponse(coordinates, weatherArray, base, main, visibility, wind, clouds, dt, sys,
					timezone, id, name, cod);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private double getLongitude(String openGeolocatorInfo) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(openGeolocatorInfo);
			return rootNode.path("lon").asDouble();
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	private double getLatitude(String openGeolocatorInfo) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(openGeolocatorInfo);
			return rootNode.path("lat").asDouble();
		} catch (Exception e) {
			e.printStackTrace();
			return 0.0;
		}
	}
	public AccuWeatherResponse extractAccuWeatherData(String accuWeatherInfo) {
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(accuWeatherInfo);
			String weatherText = rootNode.path(0).path("WeatherText").asText();
			boolean hasPrecipitation = rootNode.path(0).path("HasPrecipitation").asBoolean();
			String precipitationType = rootNode.path(0).path("PrecipitationType").asText();
			boolean isDayTime = rootNode.path(0).path("IsDayTime").asBoolean();
			double temperatureValue = rootNode.path(0).path("Temperature").path("Metric").path("Value").asDouble();
			String temperatureUnit = rootNode.path(0).path("Temperature").path("Metric").path("Unit").asText();
			AccuWeatherResponse.Temperature temperature = new AccuWeatherResponse.Temperature();
			AccuWeatherResponse.Metric metric = new AccuWeatherResponse.Metric();
			metric.setValue(temperatureValue);
			metric.setUnit(temperatureUnit);
			temperature.setMetric(metric);
			AccuWeatherResponse accuWeatherResponse = new AccuWeatherResponse();
			accuWeatherResponse.setWeatherText(weatherText);
			accuWeatherResponse.setHasPrecipitation(hasPrecipitation);
			accuWeatherResponse.setPrecipitationType(precipitationType);
			accuWeatherResponse.setDayTime(isDayTime);
			accuWeatherResponse.setTemperature(temperature);
			return accuWeatherResponse;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
