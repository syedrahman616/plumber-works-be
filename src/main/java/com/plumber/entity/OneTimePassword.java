package com.plumber.entity;

public class OneTimePassword {
	
	private char[] otp;
	private String requestId;

	public char[] getOtp() {
		return otp;
	}

	public void setOtp(char[] otp) {
		this.otp = otp;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
}
