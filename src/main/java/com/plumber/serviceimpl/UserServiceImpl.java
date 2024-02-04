package com.plumber.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plumber.dao.UserprofileRepository;
import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;
import com.plumber.service.UserService;
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserprofileRepository repo;

	@Override
	public void userProfile(UserProfile request, Long id) throws APIException {
		repo.updateProfile(request,id);
		
	}

	@Override
	public UserProfile getProfile(Long id) throws APIException {
		UserProfile response = repo.getProfile(id);
		return response; 
		
	}

}
