package com.plumber.daoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.plumber.dao.AdminRepository;
import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.AdminApproved;
import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.PlumberUser;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.utils.ResponseBuilder;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

	@Autowired
	PlumberUserRepository plumberRepo;

	@Autowired
	CustomerUserRepository customerRepo;

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	UserRepository userRepo;

	@Autowired
	JobRepository jobRepo;

	@Override
	public List<Plumber> adminPlumber() throws APIException {
		List<Plumber> plumberList = new ArrayList<>();
		List<Plumber> finalResponse = new ArrayList<>();
		plumberList = plumberRepo.findAll();
		for (Plumber obj : plumberList) {
			if (obj.getPlumberId() > 0) {
				Optional<PlumberUser> user = userRepo.findById(obj.getPlumberId());
				if (user.isPresent()) {
					obj.setUserEmail(user.get().getEmail());
					finalResponse.add(obj);
				}
			}
		}
		return finalResponse;
	}

	@Override
	public List<Customer> adminCustomer() throws APIException {
		List<Customer> customerList = new ArrayList<>();
		List<Customer> finalResponse = new ArrayList<>();
		customerList = customerRepo.findAll();
		for (Customer obj : customerList) {
			if (obj.getCustomerId() > 0) {
				Optional<PlumberUser> user = userRepo.findById(obj.getCustomerId());
				if (user.isPresent()) {
					obj.setUserEmail(user.get().getEmail());
					finalResponse.add(obj);
				}
			}
		}
		return finalResponse;
	}

	@Override
	public List<Jobs> adminJobs() throws APIException {
		List<Jobs> response = jobRepo.findAll();
		return response;
	}

	@Override
	public void adminApproved(AdminApproved request) throws APIException {
		Optional<PlumberUser> user = userRepo.findById((long) request.getUserId());
		if (user.isPresent()) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("status", request.getAction());
			param.addValue("user_id", request.getUserId());
			String sqlQuery = "";
			if (request.getUserRole().equalsIgnoreCase("customer")) {
				sqlQuery = "update customer set status=:status where customer_id=:user_id";
			} else {
				sqlQuery = "update plumber set status=:status where plumber_id=:user_id";
			}

			jdbcTemplate.update(sqlQuery, param);

		}

	}

	@Override
	public APIResponse<Object> adminCustomerProfile(Customer request) throws APIException {
		APIResponse<Object> response = null;
		if (request.getFlag().equalsIgnoreCase("edit")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				customerRepo.save(request);
				response = ResponseBuilder.build("Success", "Edited Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (request.getFlag().equalsIgnoreCase("get")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				Optional<Customer> obj = customerRepo.findById(request.getId());
				response = ResponseBuilder.build("Success", "Plumber Details", obj);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (request.getFlag().equalsIgnoreCase("delete")) {
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
	public APIResponse<Object> adminPlumberProfile(Plumber request) throws APIException {
		APIResponse<Object> response = null;
		if (request.getFlag().equalsIgnoreCase("edit")) {
			Optional<Plumber> plumber = plumberRepo.findById(request.getId());
			if (plumber.isPresent()) {
				plumberRepo.save(request);
				response = ResponseBuilder.build("Success", "Edited Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (request.getFlag().equalsIgnoreCase("get")) {
			Optional<Plumber> plumber = plumberRepo.findById(request.getId());
			if (plumber.isPresent()) {
				Optional<Plumber> obj = plumberRepo.findById(request.getId());
				response = ResponseBuilder.build("Success", "Plumber Details", obj);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (request.getFlag().equalsIgnoreCase("delete")) {
			Optional<Plumber> plumber = plumberRepo.findById(request.getId());
			if (plumber.isPresent()) {
				plumberRepo.deleteById(request.getId());
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
