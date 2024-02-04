package com.plumber.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateProfile {
	private int sateId;
	private String fullName;
	private String mobile;
	private String address1;
	private String address2;
	private String city;
	private String postalCode;
	private String state;
	private String country;
}
