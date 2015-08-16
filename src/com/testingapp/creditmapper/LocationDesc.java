package com.testingapp.creditmapper;

public class LocationDesc {
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private int id;
	private String addressLine1;
	private String addressLine2;
	private String city;
	private String state;
	private int zipCode;
	
	public LocationDesc(){
		//TODO: get current GPS Location
		this.id = -1;
		this.addressLine1 = "3520 Kilburn Circle";
		this.addressLine2 = "Apt 1432";
		this.city = "Richmond";
		this.state = "VA";
		this.zipCode = 23233;
	}
	
	public LocationDesc(String addressLine1, String addressLine2, String city, String state, int zipCode){
		this.id = -1;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.city = city;
		this.state = state;
		this.zipCode = zipCode;
	}
	
	@Override
	public String toString(){
		return addressLine1 + " " + addressLine2 + 
				" " + city + ", " + state + " " + zipCode; 
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getZipCode() {
		return zipCode;
	}

	public void setZipCode(int zipCode) {
		this.zipCode = zipCode;
	}

}
