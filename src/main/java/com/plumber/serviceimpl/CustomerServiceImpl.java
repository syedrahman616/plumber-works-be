package com.plumber.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plumber.dao.CustomerRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.JobAccept;
import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository repo;

	@Override
	public APIResponse<Object> customerProfile(Customer request, Long id) throws APIException {
		APIResponse<Object> response = repo.customerProfile(request, id);
		return response;
	}

	@Override
	public APIResponse<Object> Jobs(com.plumber.entity.Jobs request, Long id) throws APIException {

		return repo.job(request, id);
	}

	@Override
	public List<com.plumber.entity.Jobs> customerJobs(Long id) throws APIException {
		return repo.customerJobs(id);
	}

	@Override
	public List<Plumber> plumberDetails(Long id) throws APIException {

		return repo.plumberDetails(id);
	}

	@Override
	public APIResponse<Object> JobsInvitation(JobInvitation request, Long id) throws APIException {
		return repo.jobInvitation(request, id);
	}

	@Override
	public List<JobInvitation> getJobInvitation(Long id) throws APIException {
		return repo.getJobInvitation(id);
	}

	@Override
	public List<JobQuotes> getCustomerQoutes(Long id) throws APIException {
		return repo.getCustomerQuotes(id);
	}

	@Override
	public void JobsAccept(JobAccept request, Long id) throws APIException {
		repo.JobAccept(request, id);

	}

	@Override
	public List<com.plumber.entity.Jobs> finishedCustomerJobs(Long id) throws APIException {
		return repo.finishedCustomerJob(id);
	}

	@Override
	public void finsihedCustomerJob(Long id, int jobId) throws APIException {
		repo.finishedCustomerjob(id, jobId);

	}

	@Override
	public List<Plumber> plumberInviteDetails(Long id, int jobId) throws APIException {
		return repo.plumberinviteDetails(id, jobId);
	}

}
