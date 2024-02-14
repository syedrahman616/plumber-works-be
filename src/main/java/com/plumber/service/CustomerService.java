package com.plumber.service;

import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface CustomerService {

	APIResponse<Object> customerProfile(Customer request, Long id)throws APIException;

	APIResponse<Object> Jobs(Jobs request, Long id)throws APIException;

}
