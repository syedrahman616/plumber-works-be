package com.pgneet.entity;

import lombok.Data;

@Data
public class ResponseDate {
	private String date;
	private int timezone_type;
	private String timezone;
	
	public ResponseDate() {
		
	}
	
	public ResponseDate(String date, int timezone_type, String timezone) {
        this.date = date;
        this.timezone_type = timezone_type;
        this.timezone = timezone;
	}

}
