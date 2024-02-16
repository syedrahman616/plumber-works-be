package com.plumber.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.plumber.dao.UserRepository;
import com.plumber.entity.Plumber;
import com.plumber.entity.Skill;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.security.UserPrincipal;
import com.plumber.service.PlumberService;
import com.plumber.utils.ResponseBuilder;

@RestController
public class PlumberController {
	@Autowired
	UserRepository usrRepo;

	@Autowired
	PlumberService repo;

	@PostMapping("/plumber-profile")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> updatePlumber(@RequestBody Plumber request)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			APIResponse<Object> response = repo.plumberProfile(request, userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

	@PostMapping("/add-skill")
	public APIResponse<Object> addSkill(@RequestBody Skill request) throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			repo.addSkill(request, userprincipal.getId());
			return null;
		}
		return ResponseBuilder.build("Failure", "Your Are Not Authorized Person", null);
	}

}
