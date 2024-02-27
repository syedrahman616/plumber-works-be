package com.plumber.entity;

import lombok.Data;

@Data
public class AdminApproved {
	private int userId;
	private String userRole;
	private int action;
}
