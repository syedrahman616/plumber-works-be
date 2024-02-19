package com.plumber.service;

import java.util.List;

import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface CustomerService {

	APIResponse<Object> customerProfile(Customer request, Long id)throws APIException;

	APIResponse<Object> Jobs(Jobs request, Long id)throws APIException;

	List<com.plumber.entity.Jobs> customerJobs(Long id)throws APIException;

}
