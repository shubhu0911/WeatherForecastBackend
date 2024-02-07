package com.nagarro.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccuWeatherLocationResponse {
	@JsonProperty("Version")
	private int version;
	@JsonProperty("Key")
	private String key;
	@JsonProperty("Type")
	private String type;
	@JsonProperty("Rank")
	private String rank;
	@JsonProperty("LocalizedName")
	private String localizedName;
	@JsonProperty("EnglishName")
	private String englishName;
	@JsonProperty("PrimaryPostalCode")
	private String primaryPostalCode;
	@JsonProperty("Region")
	private Region region;
	@JsonProperty("Country")
	private Country country;
	public AccuWeatherLocationResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AccuWeatherLocationResponse(String key) {
		super();
		this.key = key;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getLocalizedName() {
		return localizedName;
	}
	public void setLocalizedName(String localizedName) {
		this.localizedName = localizedName;
	}
	public String getEnglishName() {
		return englishName;
	}
	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}
	public String getPrimaryPostalCode() {
		return primaryPostalCode;
	}
	public void setPrimaryPostalCode(String primaryPostalCode) {
		this.primaryPostalCode = primaryPostalCode;
	}
	public Region getRegion() {
		return region;
	}
	public void setRegion(Region region) {
		this.region = region;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}
	
	public static class Region{
		
		@JsonProperty("ID")
		private String id;
		
		@JsonProperty("LocalizedName")
		private String localizedName;
		
		@JsonProperty("EnglishName")
		private String englishName;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLocalizedName() {
			return localizedName;
		}

		public void setLocalizedName(String localizedName) {
			this.localizedName = localizedName;
		}

		public String getEnglishName() {
			return englishName;
		}

		public void setEnglishName(String englishName) {
			this.englishName = englishName;
		}
	}
	
	public static class Country{
		@JsonProperty("ID")
		private String id;
		
		@JsonProperty("LocalizedName")
		private String localizedName;
		
		@JsonProperty("EnglishName")
		private String englishName;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getLocalizedName() {
			return localizedName;
		}

		public void setLocalizedName(String localizedName) {
			this.localizedName = localizedName;
		}

		public String getEnglishName() {
			return englishName;
		}

		public void setEnglishName(String englishName) {
			this.englishName = englishName;
		}
	}

}
