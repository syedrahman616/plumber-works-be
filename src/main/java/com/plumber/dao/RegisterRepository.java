package com.plumber.dao;

import com.plumber.entity.SignupRequest;
import com.plumber.exception.APIException;

public interface RegisterRepository {

	boolean userSignUp(SignupRequest request)throws APIException;


}
