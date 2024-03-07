package com.plumber.entity;

import lombok.Data;

@Data
public class JobAccept {
	private int id;
	private double price;
	private int jobId;
	private int plumberId;
	private int customerId;
	private String action;
	private String startDate;
	private String endDate;

}
