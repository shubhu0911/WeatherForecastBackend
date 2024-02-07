package com.nagarro.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.nagarro.model.AccuWeatherResponse;
import com.nagarro.model.OpenWeatherGeolocationResponse;
import com.nagarro.model.OpenWeatherResponse;
import com.nagarro.model.WeatherAggregateResponse;
@ExtendWith(MockitoExtension.class)
public class WeatherAggregatorServiceTest {
	@Mock
	private AccuWeatherService accuWeatherService;
	@Mock
	private OpenWeatherService openWeatherService;
	@InjectMocks
	private WeatherAggregatorService weatherAggregatorService;
	@Test
	public void testGetWeatherAggregation() {
		when(accuWeatherService.getLocationKey(anyString())).thenReturn("191158");
		when(accuWeatherService.getWeatherInfo(anyString())).thenReturn(new AccuWeatherResponse());
		when(openWeatherService.getGeolocationInfo(anyString(), anyString())).thenReturn(new OpenWeatherGeolocationResponse());
		when(openWeatherService.getWeatherInfo(anyDouble(),anyDouble())).thenReturn(new OpenWeatherResponse());
		WeatherAggregateResponse aggregateResponse=weatherAggregatorService.getWeatherAggregate("Akbarpur", "224231", "IN");
		assertNotNull(aggregateResponse);
	}
}
