package com.plumber.dao;

import java.util.List;

import com.plumber.entity.AdminApproved;
import com.plumber.entity.Customer;
import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;

public interface AdminRepository {

	List<Plumber> adminPlumber() throws APIException;

	List<Customer> adminCustomer() throws APIException;

	List<Jobs> adminJobs() throws APIException;

	void adminApproved(AdminApproved request) throws APIException;

	APIResponse<Object> adminCustomerProfile(Customer request)throws APIException;

	APIResponse<Object> adminPlumberProfile(Plumber request)throws APIException;

	List<JobInvitation> adminJobInvitation(Long id)throws APIException;

	List<JobQuotes> getAdminJobQoutes(Long id)throws APIException;

}
