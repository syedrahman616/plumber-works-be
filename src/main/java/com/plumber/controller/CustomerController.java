package com.plumber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.plumber.utils.ResponseBuilder;

@RestController
public class CustomerController {
	@Autowired
	UserRepository usrRepo;

	@Autowired
	CustomerService repo;

	@PostMapping("/customer-profile")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> updatecustomer(@RequestBody Customer request)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			APIResponse<Object> response = repo.customerProfile(request, userprincipal.getId());
			return ResponseEntity.status(200).body(response);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@PostMapping("/add-job")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> addJobs(@RequestBody Jobs request)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			APIResponse<Object> response = repo.Jobs(request, userprincipal.getId());
			return ResponseEntity.status(200).body(response);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}
	
	@GetMapping("/customer-jobs")
	public ResponseEntity<com.plumber.response.APIResponse<Object>>  customerJobs() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			List<Jobs>response = repo.customerJobs(userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Customer Job details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}
	

}
