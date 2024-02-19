package com.plumber.daoimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.plumber.dao.AdminRepository;
import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.PlumberUser;
import com.plumber.exception.APIException;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

	@Autowired
	PlumberUserRepository plumberRepo;

	@Autowired
	CustomerUserRepository customerRepo;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	JobRepository jobRepo;

	@Override
	public List<Plumber> adminPlumber() throws APIException {
		List<Plumber> plumberList = new ArrayList<>();
		plumberList = plumberRepo.findAll();
		for (Plumber obj : plumberList) {
			if (obj.getPlumberId() > 0) {
				Optional<PlumberUser> user = userRepo.findById(obj.getPlumberId());
				obj.setUserEmail(user.get().getEmail());
			}
		}
		return plumberList;
	}

	@Override
	public List<Customer> adminCustomer() throws APIException {
		List<Customer> customerList = new ArrayList<>();
		customerList = customerRepo.findAll();
		for (Customer obj : customerList) {
			if (obj.getCustomerId() > 0) {
				Optional<PlumberUser> user = userRepo.findById(obj.getCustomerId());
				obj.setUserEmail(user.get().getEmail());
			}
		}
		return customerList;
	}

	@Override
	public List<Jobs> adminJobs() throws APIException {
		List<Jobs> response = jobRepo.findAll();
		return response;
	}

}
