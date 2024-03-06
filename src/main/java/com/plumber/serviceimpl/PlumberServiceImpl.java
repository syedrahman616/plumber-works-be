package com.plumber.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plumber.dao.PlumberRepository;
import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
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

	@Override
	public List<JobInvitation> getPlumberJobInvitation(Long id) throws APIException {
		return repo.getPlumberJobInvitation(id);
	}

	@Override
	public APIResponse<Object> plumberJobQoutes(JobQuotes request, Long id) throws APIException {
		return repo.plumberJobQoutes(request,id);
	}

	@Override
	public List<JobQuotes> getPlumberQoutes(Long id) throws APIException {
		return repo.getPlumberJobQuotes(id);
	}

	@Override
	public List<Jobs> plumberFinishedJobs(Long id) throws APIException {
		return repo.plumberFinishedJobs(id);
	}
	

}
