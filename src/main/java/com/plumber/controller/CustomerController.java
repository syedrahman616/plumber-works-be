package com.plumber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.plumber.dao.UserRepository;
import com.plumber.entity.Customer;
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
	public APIResponse<Object> updatecustomer(@RequestBody Customer request) throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			APIResponse<Object> response = repo.customerProfile(request, userprincipal.getId());
			return response;
		}
		return ResponseBuilder.build("Failure", "Your Are Not Authorized Person", null);
	}
	
}
