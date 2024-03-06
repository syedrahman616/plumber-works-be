package com.plumber.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.plumber.entity.UserNotify;
import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;

public interface UserService {

	void userProfile(UserProfile request, Long id)throws APIException;

	UserProfile getProfile(Long id)throws APIException;

	void JobStartDate(int jobId, Long id)throws APIException;

	void JobEndDate(int jobId, Long id)throws APIException;

	String uploadFile(MultipartFile uploadFile, String location, Long id)throws APIException;

	Resource getFile(String fileName)throws APIException;

	List<UserNotify> getNotify(Long id);

	void userNotify(String id)throws APIException;

}
