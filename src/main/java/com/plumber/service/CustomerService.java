package com.plumber.service;

import java.util.List;

import com.plumber.entity.Customer;
import com.plumber.entity.JobAccept;
import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface CustomerService {

	APIResponse<Object> customerProfile(Customer request, Long id)throws APIException;

	APIResponse<Object> Jobs(Jobs request, Long id)throws APIException;

	List<com.plumber.entity.Jobs> customerJobs(Long id)throws APIException;

	List<com.plumber.entity.Plumber> plumberDetails(Long id)throws APIException;

	APIResponse<Object> JobsInvitation(JobInvitation request, Long id)throws APIException;

	List<JobInvitation> getJobInvitation(Long id)throws APIException;

	List<JobQuotes> getCustomerQoutes(Long id)throws APIException;

	void JobsAccept(JobAccept request, Long id)throws APIException;

	List<com.plumber.entity.Jobs> finishedCustomerJobs(Long id)throws APIException;

}
