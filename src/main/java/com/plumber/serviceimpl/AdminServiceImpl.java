package com.plumber.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plumber.dao.AdminRepository;
import com.plumber.entity.AdminApproved;
import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	AdminRepository repo;

	@Override
	public List<Plumber> plumberAdmin() throws APIException {
		return repo.adminPlumber();
	}

	@Override
	public List<Customer> customerAdmin() throws APIException {
		return repo.adminCustomer();
	}

	@Override
	public List<Jobs> adminJobs() throws APIException{
		return repo.adminJobs();
	}

	@Override
	public void adminApproved(AdminApproved request) throws APIException {
		repo.adminApproved(request);
		
	}

	@Override
	public APIResponse<Object> adminCustomerProfile(Customer request) throws APIException {
		return repo.adminCustomerProfile(request);
	}

	@Override
	public APIResponse<Object> adminPlumberProfile(Plumber request) throws APIException {
		return repo.adminPlumberProfile(request);
	}

	

}
