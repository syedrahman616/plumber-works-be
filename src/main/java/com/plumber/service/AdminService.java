package com.plumber.service;

import java.util.List;

import com.plumber.entity.AdminApproved;
import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface AdminService {

	List<Plumber> plumberAdmin()throws APIException;

	List<Customer> customerAdmin()throws APIException;

	List<Jobs> adminJobs() throws APIException;

	void adminApproved(AdminApproved request)throws APIException;

	APIResponse<Object> adminCustomerProfile(Customer request)throws APIException;

	APIResponse<Object> adminPlumberProfile(Plumber request)throws APIException;

}
