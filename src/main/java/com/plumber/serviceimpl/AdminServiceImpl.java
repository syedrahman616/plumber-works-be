package com.plumber.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plumber.dao.AdminRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;
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

}
