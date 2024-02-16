package com.plumber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.plumber.dao.UserRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.security.UserPrincipal;
import com.plumber.service.CustomerService;

@RestController
public class CustomerController {
	@Autowired
	UserRepository usrRepo;

	@Autowired
	CustomerService repo;

	@PostMapping("/customer-profile")
	public ResponseEntity<Object> updatecustomer(@RequestBody Customer request) throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			APIResponse<Object> response = repo.customerProfile(request, userprincipal.getId());
			return ResponseEntity.status(200).body(response);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Your Are Not Authorized Person.");
	}
	
	@PostMapping("/add-job")
	public ResponseEntity<Object> addJobs(@RequestBody Jobs request) throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			APIResponse<Object> response = repo.Jobs(request, userprincipal.getId());
			return ResponseEntity.status(200).body(response);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body("Your Are Not Authorized Person.");
	}
	
	
	
	
}
