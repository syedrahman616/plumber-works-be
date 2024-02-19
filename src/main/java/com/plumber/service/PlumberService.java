package com.plumber.service;

import java.util.List;

import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.Skill;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface PlumberService {

	APIResponse<Object> plumberProfile(Plumber request, Long id)throws APIException;

	void addSkill(Skill request, Long id)throws APIException;

	List<Jobs> plumberJobs(Long id)throws APIException;

	List<Jobs> allJobs(Long id)throws APIException;

}
