package com.plumber.dao;

import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;

public interface UserprofileRepository {

	void updateProfile(UserProfile request, Long id)throws APIException;

	UserProfile getProfile(Long id)throws APIException;

}
