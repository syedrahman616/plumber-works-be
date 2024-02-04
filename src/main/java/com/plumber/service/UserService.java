package com.plumber.service;

import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;

public interface UserService {

	void userProfile(UserProfile request, Long id)throws APIException;

	UserProfile getProfile(Long id)throws APIException;

}
