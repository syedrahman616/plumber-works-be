package com.plumber.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plumber.dao.PlumberRepository;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.Skill;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.service.PlumberService;
@Service
public class PlumberServiceImpl implements PlumberService{
	
	@Autowired
	PlumberRepository repo;

	@Override
	public APIResponse<Object> plumberProfile(Plumber request, Long id) throws APIException {
		 APIResponse<Object> response = repo.plumberProfile(request,id);
		 return response;
	}

	@Override
	public void addSkill(Skill request, Long id) throws APIException {
		repo.addSkill(request,id);
	}

	@Override
	public List<Jobs> plumberJobs(Long id) throws APIException {
		return repo.plumberJobs(id);
	}

	@Override
	public List<Jobs> allJobs(Long id) throws APIException {
		return repo.allJobs(id);
	}

}
