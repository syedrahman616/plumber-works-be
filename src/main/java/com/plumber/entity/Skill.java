package com.plumber.entity;

import lombok.Data;

@Data
public class Skill {
	private int id;
	private int plumberId;
	private int customerId;
	private int jobId;
	private int rating;
}
