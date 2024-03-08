package com.plumber.dao;

import java.util.List;

import com.plumber.entity.Customer;
import com.plumber.entity.JobAccept;
import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface CustomerRepository {

	APIResponse<Object> customerProfile(Customer request, Long id)throws APIException;

	APIResponse<Object> job(Jobs request, Long id)throws APIException;

	List<Jobs> customerJobs(Long id)throws APIException;

	List<Plumber> plumberDetails(Long id)throws APIException;

	APIResponse<Object> jobInvitation(JobInvitation request, Long id)throws APIException;

	List<JobInvitation> getJobInvitation(Long id)throws APIException;

	List<JobQuotes> getCustomerQuotes(Long id)throws APIException;

	void JobAccept(JobAccept request, Long id)throws APIException;

	List<Jobs> finishedCustomerJob(Long id)throws APIException;

	void finishedCustomerjob(Long id, int jobId)throws APIException;

	List<Plumber> plumberinviteDetails(Long id, int jobId)throws APIException;

}
