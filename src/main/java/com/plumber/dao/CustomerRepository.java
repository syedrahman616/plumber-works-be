package com.plumber.dao;

import com.plumber.entity.Customer;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface CustomerRepository {

	APIResponse<Object> customerProfile(Customer request, Long id)throws APIException;

}
