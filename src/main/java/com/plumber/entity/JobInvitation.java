package com.plumber.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobInvitation {
	private int id;
	private int jobId;
	private int plumberId;
	private int customerId;
	private double price;
	private boolean accept;
	private String flag;
	private String jobTitle;
	private String description;
	private String image1;
	private String image2;
	private String video;
	private String address;
	private String postCode;
	private int hours;
	private int ratePerHour;
	private int fixedPrice;
	private String customerName;
	private String plumberName;

}
