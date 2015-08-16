package com.testingapp.creditmapper;

import org.json.JSONException;
import org.json.JSONObject;

public class BusinessResult {
	
	private static final String JSON_MERCHANTNAME = "merchantName";
	private static final String JSON_MERCHANTCODE = "merchantCode";
	private static final String JSON_MERCHANTCATEGORY = "merchantCategory";
	private static final String JSON_LOCATION = "location";
	private static final String JSON_DISTANCE = "distance";
	private static final String JSON_IS_FAVORITE = "isFavorite";
	
	private int id;
	private String merchantName;
	private int merchantCode;
	private String merchantCategory;
	private LocationDesc location;
	private double distance;
	private boolean isFavorite;
	
	public BusinessResult(){
		//TODO: get the result for the current location
		this.id = -1;
		this.merchantName = "Chik-Fil-A";
		this.merchantCode = 5814;
		this.merchantCategory = "Fast Food Restaurant";
		this.location = new LocationDesc();
		this.distance = 0.01;
		this.isFavorite = false;
	}
	
	public BusinessResult(JSONObject json) throws JSONException{
		if(json.has(JSON_MERCHANTNAME)) merchantName = json.getString(JSON_MERCHANTNAME);
		if(json.has(JSON_MERCHANTCODE)) merchantCode = json.getInt(JSON_MERCHANTCODE);
		if(json.has(JSON_MERCHANTCATEGORY)) merchantCategory = json.getString(JSON_MERCHANTCATEGORY);
		if(json.has(JSON_LOCATION)) location = (LocationDesc)json.get(JSON_LOCATION);
		if(json.has(JSON_DISTANCE)) distance = json.getDouble(JSON_DISTANCE);
		if(json.has(JSON_IS_FAVORITE)) isFavorite = json.getBoolean(JSON_IS_FAVORITE);
	}
	
	public JSONObject toJSON() throws JSONException{
		JSONObject json = new JSONObject();
		json.put(JSON_MERCHANTNAME, merchantName);
		json.put(JSON_MERCHANTCODE, merchantCode);
		json.put(JSON_MERCHANTCATEGORY, merchantCategory);
		json.put(JSON_LOCATION, location);
		json.put(JSON_DISTANCE, distance);
		json.put(JSON_IS_FAVORITE, isFavorite);
		
		return json;
	}
	
	public BusinessResult(String merchantName, int merchantCode, 
			String merchantCategory,LocationDesc location, double distance, boolean isFavorite){
		this.id = -1;
		this.merchantName = merchantName;
		this.merchantCode = merchantCode;
		this.merchantCategory = merchantCategory;
		this.location = location;
		this.distance = distance;
		this.isFavorite = isFavorite;
		
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString(){
		return merchantName + ": " + merchantCode + " " + merchantCategory;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public int getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(int merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getMerchantCategory() {
		return merchantCategory;
	}

	public void setMerchantCategory(String merchantCategory) {
		this.merchantCategory = merchantCategory;
	}

	public LocationDesc getLocation() {
		return location;
	}

	public void setLocation(LocationDesc location) {
		this.location = location;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public boolean isFavorite() {
		return isFavorite;
	}

	public void setFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
