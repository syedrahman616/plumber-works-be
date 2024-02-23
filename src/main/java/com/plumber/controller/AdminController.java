package com.plumber.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.plumber.dao.RegisterRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.PlumberUser;
import com.plumber.entity.SignupRequest;
import com.plumber.entity.TokenCreate;
import com.plumber.exception.APIException;
import com.plumber.response.AuthResponse;
import com.plumber.security.UserPrincipal;
import com.plumber.service.AdminService;
import com.plumber.utils.ResponseBuilder;
import com.plumber.validators.SignupValidator;

@RestController
public class AdminController {

	@Autowired
	UserRepository usrRepo;

	@Autowired
	AdminService repo;

	@Autowired
	private RegisterRepository regRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/admin-plumber")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> plumberAdmin() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (userprincipal.getId() > 0 && user.get().getUserRole().equalsIgnoreCase("Admin")) {
			List<Plumber> response = repo.plumberAdmin();
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Plumber Details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@GetMapping("/admin-customer")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> customerAdmin() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (userprincipal.getId() > 0 && user.get().getUserRole().equalsIgnoreCase("Admin")) {
			List<Customer> response = repo.customerAdmin();
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Customer details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@GetMapping("/admin-jobs")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> adminJobs() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (userprincipal.getId() > 0 && user.get().getUserRole().equalsIgnoreCase("Admin")) {
			List<Jobs> response = repo.adminJobs();
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "All Job details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@PostMapping("/add-user")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> userSigUp(@RequestBody SignupRequest request)
			throws APIException, MessagingException, JsonMappingException, JsonProcessingException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (userprincipal.getId() > 0 && user.get().getUserRole().equalsIgnoreCase("Admin")) {
			Map<String, List<String>> errorMapper = SignupValidator.validate(request);
			String tempPwd = request.getPassword();
			if (errorMapper.size() <= 0) {
				String tpwd = request.getPassword();
				request.setPassword(passwordEncoder.encode(request.getPassword()));
				regRepo.userSignUp(request);
				return ResponseEntity.status(HttpStatus.OK)
						.body(ResponseBuilder.build("Success", "User Successfully Registered", null));
			} else {
				return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
						.body(ResponseBuilder.build("Error", null, errorMapper));
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
					.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));

		}
	}
}
