package com.nagarro.service;

import static org.junit.Assert.assertEquals;
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

import com.nagarro.model.AccuWeatherLocationResponse;
import com.nagarro.model.AccuWeatherResponse;

@ExtendWith(MockitoExtension.class)
public class AccuWeatherServiceTest {
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private AccuWeatherService accuWeatherService;
	
	@Test
	public void testGetLocationKey() {
		AccuWeatherLocationResponse[] mockLocationResponse = { new AccuWeatherLocationResponse("224231") };
        when(restTemplate.getForObject(anyString(), eq(AccuWeatherLocationResponse[].class), any(), any()))
                .thenReturn(mockLocationResponse);
        String locationKey = accuWeatherService.getLocationKey("Akbarpur");
        assertEquals("224231", locationKey);
        verify(restTemplate).getForObject(anyString(), eq(AccuWeatherLocationResponse[].class), any(), any());
    
	}
	 @Test
	    public void testGetWeatherInfo() {
	        AccuWeatherResponse[] mockWeatherResponse = { new AccuWeatherResponse() };
	        when(restTemplate.getForObject(anyString(), eq(AccuWeatherResponse[].class), any(), any()))
	                .thenReturn(mockWeatherResponse);
	        AccuWeatherResponse weatherInfo = accuWeatherService.getWeatherInfo("191158");
	        assertNotNull(weatherInfo);
	        verify(restTemplate).getForObject(anyString(), eq(AccuWeatherResponse[].class), any(), any());
	    }
	    @Test
	    public void testGetWeatherInformation() {
	        when(restTemplate.getForObject(anyString(), eq(String.class), any(), any()))
	                .thenReturn("Weather Information");
	        String weatherInformation = accuWeatherService.getWeatherInformation("224231");
	        assertEquals("Weather Information", weatherInformation);
	        verify(restTemplate).getForObject(anyString(), eq(String.class), any(), any());
	    }
}
