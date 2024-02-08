package com.plumber.dao;

import java.util.List;

import com.plumber.entity.Customer;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;

public interface AdminRepository {

	List<Plumber> adminPlumber()throws APIException;

	List<Customer> adminCustomer()throws APIException;

}