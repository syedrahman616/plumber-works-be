package com.plumber.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.plumber.dao.UserprofileRepository;
import com.plumber.entity.UserNotify;
import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;
import com.plumber.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserprofileRepository repo;

	@Override
	public void userProfile(UserProfile request, Long id) throws APIException {
		repo.updateProfile(request, id);

	}

	@Override
	public UserProfile getProfile(Long id) throws APIException {
		UserProfile response = repo.getProfile(id);
		return response;

	}

	@Override
	public void JobStartDate(int jobId, Long id) throws APIException {
		repo.jobStartDate(jobId, id);

	}

	@Override
	public void JobEndDate(int jobId, Long id) throws APIException {
		repo.jobEndDate(jobId, id);

	}

	@Override
	public String uploadFile(MultipartFile uploadFile, Long id) throws APIException {
		return repo.uploadFile(uploadFile, id);

	}

	@Override
	public Resource getFile(String fileName) throws APIException {
		return repo.getFiles(fileName);
	}

	@Override
	public List<UserNotify> getNotify(Long id) {
		return repo.getNotify(id);
	}

	@Override
	public void userNotify(String id) throws APIException {
		repo.userNotify(id);
		
	}

}
