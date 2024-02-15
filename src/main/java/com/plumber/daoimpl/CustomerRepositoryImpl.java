package com.plumber.daoimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.plumber.dao.CustomerRepository;
import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.entity.PlumberUser;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.utils.ResponseBuilder;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

	@Autowired
	CustomerUserRepository customerRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	JobRepository jobRepo;

	@Override
	public APIResponse<Object> customerProfile(Customer request, Long id) throws APIException {
		APIResponse<Object> response = null;
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && request.getFlag().equalsIgnoreCase("edit")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				customerRepo.save(request);
				response = ResponseBuilder.build("Success", "Edited Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("get")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				Optional<Customer> obj = customerRepo.findById(request.getId());
				response = ResponseBuilder.build("Success", "Plumber Details", obj);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("delete")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				customerRepo.deleteById(request.getId());
				response = ResponseBuilder.build("Success", "Deleted Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else {
			throw new APIException("21", "User Not Found.");
		}
		return response;
	}

	@Override
	public APIResponse<Object> job(Jobs request, Long id) throws APIException {
		APIResponse<Object> response = null;
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && request.getFlag().equalsIgnoreCase("add")) {
			request.setCustomerId(id);
			jobRepo.save(request);
			response = ResponseBuilder.build("Success", "Edited Successfully", null);
		} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("get")) {
			Optional<Jobs> job = jobRepo.findById(request.getId());
			if (job.isPresent()) {
				Optional<Customer> obj = customerRepo.findById(request.getId());
				response = ResponseBuilder.build("Success", "Plumber Details", obj);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("delete")) {
			Optional<Jobs> job = jobRepo.findById(request.getId());
			if (job.isPresent()) {
				customerRepo.deleteById(request.getId());
				response = ResponseBuilder.build("Success", "Deleted Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else {
			throw new APIException("21", "User Not Found.");
		}
		return response;
	}

}
