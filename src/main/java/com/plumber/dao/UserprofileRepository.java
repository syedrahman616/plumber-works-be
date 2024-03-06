package com.plumber.dao;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.plumber.entity.UserNotify;
import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;

public interface UserprofileRepository {

	void updateProfile(UserProfile request, Long id)throws APIException;

	UserProfile getProfile(Long id)throws APIException;

	void jobStartDate(int jobId, Long id)throws APIException;

	void jobEndDate(int jobId, Long id)throws APIException;

	String uploadFile(MultipartFile uploadFile, Long id)throws APIException;

	Resource getFiles(String fileName)throws APIException;

	List<UserNotify> getNotify(Long id);

	void userNotify(String id)throws APIException;

}
