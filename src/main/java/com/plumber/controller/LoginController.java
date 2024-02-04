package com.plumber.controller;

import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.plumber.constants.Constants;
import com.plumber.dao.RegisterRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.LoginRequest;
import com.plumber.entity.PlumberUser;
import com.plumber.entity.SignupRequest;
import com.plumber.entity.TokenCreate;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.response.AuthResponse;
import com.plumber.security.TokenProvider;
import com.plumber.security.UserPrincipal;
import com.plumber.utils.ResponseBuilder;
import com.plumber.validators.SignupValidator;

@RestController
@CrossOrigin()
public class LoginController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RegisterRepository repo;

	@PostMapping(path = Constants.CONTEXT_AUTH + "/signup") //
	public APIResponse<Object> userSigUp(@RequestBody SignupRequest request)
			throws APIException, MessagingException, JsonMappingException, JsonProcessingException {
		Map<String, String> errorMapper = SignupValidator.validate(request);
		String tempPwd = request.getPassword();
		if (errorMapper.size() <= 0) {
			String tpwd = request.getPassword();
			request.setPassword(passwordEncoder.encode(request.getPassword()));
			repo.userSignUp(request);
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), tpwd));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			TokenCreate token = tokenProvider.createToken(authentication);
			AuthResponse authResponse = new AuthResponse(token.getToken());
			return ResponseBuilder.build("Success", "User Successfully Registered", authResponse);
		} else {
			return ResponseBuilder.build("Error", null, errorMapper);
		}
	}

	@PostMapping(path = Constants.CONTEXT_AUTH + "/login")
	public APIResponse<Object> authenticateUser(@RequestBody LoginRequest loginRequest) throws APIException {
		TokenCreate token = null;
		try {
			Optional<PlumberUser> usr = userRepo.findByEmail(loginRequest.getEmail());
			if (usr.isPresent()) {
				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				token = tokenProvider.createToken(authentication);
			}
		} catch (AuthenticationException e) {
			return ResponseBuilder.build("Failure", "Invalid credentials", null);
		}
		AuthResponse authResponse = new AuthResponse(token.getToken());
		return ResponseBuilder.build("Success", "Login Successful", authResponse);
	}

}