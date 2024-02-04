package com.plumber.entity;

import com.plumber.response.Pagination;

import lombok.Data;

@Data
public class APIResponse<T extends Object> {
	private T result;
	public String status;
	public String message;
	private Pagination pagination;

}
