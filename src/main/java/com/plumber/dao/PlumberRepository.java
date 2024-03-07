package com.plumber.dao;

import java.util.List;

import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.Skill;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface PlumberRepository {

	APIResponse<Object> plumberProfile(Plumber request, Long id) throws APIException;

	void addSkill(Skill request, Long id)throws APIException;

	List<Jobs> plumberJobs(Long id)throws APIException;

	List<Jobs> allJobs(Long id)throws APIException;

	List<JobInvitation> getPlumberJobInvitation(Long id)throws APIException;

	APIResponse<Object> plumberJobQoutes(JobQuotes request, Long id)throws APIException;

	List<JobQuotes> getPlumberJobQuotes(Long id)throws APIException;

	List<Jobs> plumberFinishedJobs(Long id)throws APIException;

	void finishedPlumberJobs(Long id, int jobId)throws APIException;

}
