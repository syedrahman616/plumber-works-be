package com.plumber.utils;

import com.plumber.response.APIResponse;
import com.plumber.response.Pagination;

public class ResponseBuilder {

	public static APIResponse<Object> build(String status, String message, Object data) {
		APIResponse<Object> response = new APIResponse<>();
		response.setStatus(status);
		response.setMessage(message);
		response.setData(data);
		return response;
	}
	
	public static APIResponse<Object> buildWithPaging(String status, String message, Object data, Pagination pagination) {
		APIResponse<Object> response = new APIResponse<>();
		response.setStatus(status);
		response.setMessage(message);
		response.setData(data);
		response.setPagination(pagination);
		return response;
	}
}
