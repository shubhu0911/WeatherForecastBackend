package com.nagarro.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.nagarro.model.WeatherAggregateResponse;
import com.nagarro.service.WeatherAggregatorService;

@ExtendWith(MockitoExtension.class)
public class WeatherControllerTest {
    @Mock
    private WeatherAggregatorService weatherAggregatorService;

    @InjectMocks
    private WeatherController weatherController;

    @Test
    public void testGetWeather() {
    	when(weatherAggregatorService.getWeatherAggregate(anyString(), anyString(), anyString()))
    	.thenReturn(new WeatherAggregateResponse());
    	
    	ResponseEntity<WeatherAggregateResponse>responseEntity=
    			weatherController.getWeather("Akbarpur", "224122", "IN");
    	
    	assertNotNull(responseEntity.getBody());
    	assertEquals(HttpStatus.OK, responseEntity.getStatusCode());;
        
    }
}
