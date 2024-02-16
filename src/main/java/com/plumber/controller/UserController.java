package com.plumber.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.plumber.dao.UserRepository;
import com.plumber.entity.PlumberUser;
import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.security.UserPrincipal;
import com.plumber.service.UserService;
import com.plumber.utils.ResponseBuilder;

@RestController
public class UserController {

	@Autowired
	UserRepository usrRepo;

	@Autowired
	UserService repo;

	@PostMapping("/update-profile")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> customer(@RequestBody UserProfile request)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (user.isPresent()) {
			repo.userProfile(request, userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Updated Successfully", null));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@GetMapping("/get-profile")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> getProfile() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (user.isPresent()) {
			UserProfile response = repo.getProfile(userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Profile Details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

}
