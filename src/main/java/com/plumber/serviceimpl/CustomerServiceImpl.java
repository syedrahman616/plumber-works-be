package com.plumber.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plumber.dao.CustomerRepository;
import com.plumber.entity.Customer;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository repo;

	@Override
	public APIResponse<Object> customerProfile(Customer request, Long id) throws APIException {
		APIResponse<Object> response = repo.customerProfile(request, id);
		return response;
	}

	@Override
	public APIResponse<Object> Jobs(com.plumber.entity.Jobs request, Long id) throws APIException {

		return repo.job(request, id);
	}

	@Override
	public List<com.plumber.entity.Jobs> customerJobs(Long id) throws APIException {
		return repo.customerJobs(id);
	}
	

}
