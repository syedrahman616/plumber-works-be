package com.plumber.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.plumber.dao.UserRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.JobAccept;
import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
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
	public ResponseEntity<com.plumber.response.APIResponse<Object>> customerJobs() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			List<Jobs> response = repo.customerJobs(userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Customer Job details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

	@GetMapping("/finished-customer-jobs")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> FinishedCustomerJobs() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			List<Jobs> response = repo.finishedCustomerJobs(userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Customer Job details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

	@GetMapping("/customer-plumber-details")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> plumberDetails() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			List<Plumber> response = repo.plumberDetails(userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Customer Job details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

	@GetMapping("/customer-plumber-invite")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> customerPlumber(@RequestParam("jobId") int jobId)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			List<Plumber> response = repo.plumberInviteDetails(userprincipal.getId(),jobId);
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Customer Job details", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

	@PostMapping("/job-invitation")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> jobInvitation(@RequestBody JobInvitation request)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			APIResponse<Object> response = repo.JobsInvitation(request, userprincipal.getId());
			return ResponseEntity.status(200).body(response);
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@GetMapping("/customer-job-invitation")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> getJobInvitation() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			List<JobInvitation> response = repo.getJobInvitation(userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Customer Job invitation", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

	@GetMapping("/get-customer-quotes")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> getCustomerQuotes() throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			List<JobQuotes> response = repo.getCustomerQoutes(userprincipal.getId());
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Customer Job invitation", response));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

	@PostMapping("/job-accept")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> jobAccept(@RequestBody JobAccept request)
			throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			repo.JobsAccept(request, userprincipal.getId());
			return ResponseEntity.status(200).body(ResponseBuilder.build("Success", "Job Accepted Successfully", null));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person.", null));
	}

	@PostMapping("/finished-customer")
	public ResponseEntity<com.plumber.response.APIResponse<Object>> FinishedPlumberJobs(
			@RequestParam("jobId") int jobId) throws APIException {
		UserPrincipal userprincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		if (userprincipal.getId() > 0) {
			repo.finsihedCustomerJob(userprincipal.getId(), jobId);
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.build("Success", "Approved Finsihed Job.", null));
		}
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.body(ResponseBuilder.build("Failure", "You Are Not Authorized Person", null));
	}

}
