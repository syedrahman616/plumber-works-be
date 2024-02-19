package com.plumber.dao;

import java.util.List;

import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface CustomerRepository {

	APIResponse<Object> customerProfile(Customer request, Long id)throws APIException;

	APIResponse<Object> job(Jobs request, Long id)throws APIException;

	List<Jobs> customerJobs(Long id)throws APIException;

}
