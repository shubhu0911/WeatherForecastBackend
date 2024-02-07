package com.nagarro.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.nagarro.model.OpenWeatherGeolocationResponse;
import com.nagarro.model.OpenWeatherResponse;

@ExtendWith(MockitoExtension.class)
public class OpenWeatherServiceTest {
	@Mock
	private RestTemplate restTemplate;
	@InjectMocks
	private OpenWeatherService openWeatherService;
	@Test
	public void testGetGeolocationInfo() {
		when(restTemplate.getForObject(anyString(), any(),anyString(),anyString(),any()))
				.thenReturn(new OpenWeatherGeolocationResponse());
		OpenWeatherGeolocationResponse geolocationResponse=openWeatherService.getGeolocationInfo("224231", "IN");
		assertNotNull(geolocationResponse);
	}
	@Test
	public void testGetWeatherInfo() {
		when(restTemplate.getForObject(anyString(), any(),anyDouble(),anyDouble(),any()))
		.thenReturn(new OpenWeatherResponse());
		OpenWeatherResponse weatherResponse=openWeatherService.getWeatherInfo(82.4621, 26.2667);
		assertNotNull(weatherResponse);
	}
}
