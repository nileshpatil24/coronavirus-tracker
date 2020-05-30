package com.nilesh.coronavirustracker.model;

public class LocationStats {

	private String state;
	
	private String country;
	
	private int latestTotalCases;
	
	private int diffPrevDay;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getLatestTotalCases() {
		return latestTotalCases;
	}

	public void setLatestTotalCases(int latestTotalCases) {
		this.latestTotalCases = latestTotalCases;
	}

	public int getDiffPrevDay() {
		return diffPrevDay;
	}

	public void setDiffPrevDay(int diffPrevDay) {
		this.diffPrevDay = diffPrevDay;
	}

}
