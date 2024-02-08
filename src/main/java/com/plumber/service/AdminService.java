package com.plumber.service;

import java.util.List;

import com.plumber.entity.Customer;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;

public interface AdminService {

	List<Plumber> plumberAdmin()throws APIException;

	List<Customer> customerAdmin()throws APIException;

}
