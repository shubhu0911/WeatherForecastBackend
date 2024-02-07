package com.nagarro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenWeatherGeolocationResponse {
	@JsonProperty("zip")
	private String zip;
	@JsonProperty("name")
	private String name;
	@JsonProperty("lat")
	private double lat;
	@JsonProperty("lon")
	private double lon;
	@JsonProperty("country")
	private String country;
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLon() {
		return lon;
	}
	public void setLon(double lon) {
		this.lon = lon;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
}
