package com.plumber.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

	@RequestMapping("/v3/health")
	public String health() {
		return "Success";
	}
}


