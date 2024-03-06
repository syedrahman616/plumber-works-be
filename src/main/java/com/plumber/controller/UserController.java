package com.plumber.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.plumber.dao.UserRepository;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.PlumberUser;
import com.plumber.entity.UserNotify;
import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;
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

	@PostMapping("/job-startdate")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> jobStartDate(@RequestParam("jobId") int jobId)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (user.isPresent()) {
			repo.JobStartDate(jobId, userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Updated Successfully", null));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@PostMapping("/job-enddate")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> jobEndDate(@RequestParam("jobId") int jobId)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (user.isPresent()) {
			repo.JobEndDate(jobId, userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Updated Successfully", null));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@PostMapping("/upload-files")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> uploadFiles(
			@RequestParam("file") MultipartFile uploadFile)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (user.isPresent()) {
			String response = repo.uploadFile(uploadFile, userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "File uploaded successfully.", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@GetMapping("/get-files")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> getFiles(@RequestParam("fileName") String fileName)
			throws APIException, IOException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (user.isPresent()) {
			Resource resource = repo.getFile(fileName);
			byte[] imageBytes = resource.getInputStream().readAllBytes();
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "File uploaded successfully.", imageBytes));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@GetMapping("/get-notify")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> getNotify() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			List<UserNotify> response = repo.getNotify(userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "User Notification", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

	@PostMapping("/user-notify")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> userNotify(@RequestParam("id") String id)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		Optional<PlumberUser> user = usrRepo.findById(userprincipal.getId());
		if (user.isPresent()) {
			repo.userNotify(id);
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "User Notify successfully.", null));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

}
