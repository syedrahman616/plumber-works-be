package com.plumber.entity;

import lombok.Data;

@Data
public class UpdatePassword {
	private String newPassword;
	private String confirmPassword;
	private String email;
	private String oldPassword;
}
