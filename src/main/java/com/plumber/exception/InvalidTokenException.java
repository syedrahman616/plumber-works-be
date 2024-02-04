package com.plumber.exception;

import com.plumber.response.APIError;
import com.plumber.response.Error;

public class InvalidTokenException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient APIError apiError;
	
	public InvalidTokenException(String message) {
		super(message);
	}
	
	public void setError(String code, String description) {
		Error error = new Error();
		error.setCode(code);
		error.setDescription(description);
		APIError errorResponse = new APIError();
		errorResponse.setError(error);
		this.apiError = errorResponse;
	}

	public APIError getApiError() {
		return apiError;
	}

	public void setApiError(APIError apiError) {
		this.apiError = apiError;
	}
}