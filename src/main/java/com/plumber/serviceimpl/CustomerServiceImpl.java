package com.plumber.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plumber.dao.CustomerRepository;
import com.plumber.entity.Customer;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.service.CustomerService;
@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	CustomerRepository repo;

	@Override
	public APIResponse<Object> customerProfile(Customer request, Long id) throws APIException {
		 APIResponse<Object> response = repo.customerProfile(request,id);
		 return response;
	}

}
