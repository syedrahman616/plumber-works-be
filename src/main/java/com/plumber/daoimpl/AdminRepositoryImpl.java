package com.plumber.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.plumber.dao.AdminRepository;
import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.AdminApproved;
import com.plumber.entity.Customer;
import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.PlumberUser;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.utils.ResponseBuilder;

@Repository
public class AdminRepositoryImpl implements AdminRepository {

	@Autowired
	PlumberUserRepository plumberRepo;

	@Autowired
	CustomerUserRepository customerRepo;

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	UserRepository userRepo;

	@Autowired
	JobRepository jobRepo;

	@Override
	public List<Plumber> adminPlumber() throws APIException {
		List<Plumber> plumberList = new ArrayList<>();
		List<Plumber> finalResponse = new ArrayList<>();
		plumberList = plumberRepo.findAll();
		for (Plumber obj : plumberList) {
			if (obj.getPlumberId() > 0) {
				Optional<PlumberUser> user = userRepo.findById(obj.getPlumberId());
				if (user.isPresent()) {
					obj.setUserEmail(user.get().getEmail());
					finalResponse.add(obj);
				}
			}
		}
		return finalResponse;
	}

	@Override
	public List<Customer> adminCustomer() throws APIException {
		List<Customer> customerList = new ArrayList<>();
		List<Customer> finalResponse = new ArrayList<>();
		customerList = customerRepo.findAll();
		for (Customer obj : customerList) {
			if (obj.getCustomerId() > 0) {
				Optional<PlumberUser> user = userRepo.findById(obj.getCustomerId());
				if (user.isPresent()) {
					obj.setUserEmail(user.get().getEmail());
					finalResponse.add(obj);
				}
			}
		}
		return finalResponse;
	}

	@Override
	public List<Jobs> adminJobs() throws APIException {
		List<Jobs> response = jdbcTemplate.query(
				"select * from job tj left join customer tc on tj.customer_id=tc.customer_id left join plumber tp on tj.plumber_id=tp.plumber_id where tj.finished=false",
				new JobMapper());
		return response;
	}

	private static final class JobMapper implements RowMapper<Jobs> {

		@Override
		public Jobs mapRow(ResultSet rs, int rowNum) throws SQLException {
			Jobs obj = new Jobs();
			obj.setId(rs.getLong("tj.id"));
			obj.setPostCode(rs.getString("postcode"));
			obj.setCustomerId(rs.getLong("customer_id"));
			obj.setPlumberId(rs.getLong("plumber_id"));
			obj.setCustomerName(rs.getString("tc.first_name") + " " + rs.getString("tc.last_name"));
			obj.setPlumberName(rs.getString("tp.first_name") + " " + rs.getString("tp.last_name"));
			obj.setAddress(rs.getString("tj.address"));
			obj.setJobTitle(rs.getString("job_title"));
			obj.setDescription(rs.getString("tj.description"));
			obj.setImage1(rs.getString("image1"));
			obj.setImage2(rs.getString("image2"));
			obj.setVideo(rs.getString("video"));
			obj.setFinished(rs.getBoolean("finished"));
			obj.setFixedPrice(rs.getInt("fixed_price"));
			obj.setCustomerStartDate(rs.getString("customer_start_date"));
			obj.setPlumberStartDate(rs.getString("plumber_start_date"));
			obj.setCustomerEndDate(rs.getString("customer_end_date"));
			obj.setPlumberEndDate(rs.getString("plumber_end_date"));
			return obj;
		}
	}

	@Override
	public void adminApproved(AdminApproved request) throws APIException {
		Optional<PlumberUser> user = userRepo.findById((long) request.getUserId());
		if (user.isPresent()) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("status", request.getAction());
			param.addValue("user_id", request.getUserId());
			String sqlQuery = "";
			if (request.getUserRole().equalsIgnoreCase("customer")) {
				sqlQuery = "update customer set status=:status where customer_id=:user_id";
			} else {
				sqlQuery = "update plumber set status=:status where plumber_id=:user_id";
			}

			jdbcTemplate.update(sqlQuery, param);

		}

	}

	@Override
	public APIResponse<Object> adminCustomerProfile(Customer request) throws APIException {
		APIResponse<Object> response = null;
		if (request.getFlag().equalsIgnoreCase("edit")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				customerRepo.save(request);
				response = ResponseBuilder.build("Success", "Edited Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (request.getFlag().equalsIgnoreCase("get")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				Optional<Customer> obj = customerRepo.findById(request.getId());
				response = ResponseBuilder.build("Success", "Plumber Details", obj);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (request.getFlag().equalsIgnoreCase("delete")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				customerRepo.deleteById(request.getId());
				response = ResponseBuilder.build("Success", "Deleted Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else {
			throw new APIException("21", "User Not Found.");
		}
		return response;
	}

	@Override
	public APIResponse<Object> adminPlumberProfile(Plumber request) throws APIException {
		APIResponse<Object> response = null;
		if (request.getFlag().equalsIgnoreCase("edit")) {
			Optional<Plumber> plumber = plumberRepo.findById(request.getId());
			if (plumber.isPresent()) {
				plumberRepo.save(request);
				response = ResponseBuilder.build("Success", "Edited Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (request.getFlag().equalsIgnoreCase("get")) {
			Optional<Plumber> plumber = plumberRepo.findById(request.getId());
			if (plumber.isPresent()) {
				Optional<Plumber> obj = plumberRepo.findById(request.getId());
				response = ResponseBuilder.build("Success", "Plumber Details", obj);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (request.getFlag().equalsIgnoreCase("delete")) {
			Optional<Plumber> plumber = plumberRepo.findById(request.getId());
			if (plumber.isPresent()) {
				plumberRepo.deleteById(request.getId());
				response = ResponseBuilder.build("Success", "Deleted Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else {
			throw new APIException("21", "User Not Found.");
		}
		return response;
	}

	@Override
	public List<JobInvitation> adminJobInvitation(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("Admin")) {
			List<JobInvitation> response = jdbcTemplate.query(
					"select * from job_invite tv,job tj,plumber tp,customer tc where tv.job_id=tj.id and tv.plumber_id=tp.plumber_id "
							+ "and tj.customer_id=tc.customer_id",
					new AdminJobInvitationMapper());
			return response;
		} else {
			throw new APIException("21", "You are not Authorized Person.");
		}
	}

	private static final class AdminJobInvitationMapper implements RowMapper<JobInvitation> {

		@Override
		public JobInvitation mapRow(ResultSet rs, int rowNum) throws SQLException {
			JobInvitation obj = new JobInvitation();
			obj.setId(rs.getInt("tv.id"));
			obj.setJobId(rs.getInt("tj.id"));
			obj.setPostCode(rs.getString("postcode"));
			obj.setCustomerId(rs.getInt("customer_id"));
			obj.setPlumberId(rs.getInt("plumber_id"));
			obj.setPrice(rs.getDouble("tv.price"));
			obj.setPlumberName(rs.getString("tp.first_name") + " " + rs.getString("tp.last_name"));
			obj.setCustomerName(rs.getString("tc.first_name") + " " + rs.getString("tc.last_name"));
			obj.setAddress(rs.getString("tj.address"));
			obj.setJobTitle(rs.getString("job_title"));
			obj.setDescription(rs.getString("tj.description"));
			obj.setImage1(rs.getString("image1"));
			obj.setImage2(rs.getString("image2"));
			obj.setVideo(rs.getString("video"));
			obj.setAccept(rs.getBoolean("accept"));
			return obj;
		}
	}

	@Override
	public List<JobQuotes> getAdminJobQoutes(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("Admin")) {
			List<JobQuotes> response = jdbcTemplate.query(
					"select * from job_quotes tq,job tj,customer tc,plumber tp where tq.job_id=tj.id and tj.customer_id=tc.customer_id and tq.plumber_id=tp.plumber_id",
					new PlumberJobQuotesMapper());
			return response;
		} else {
			throw new APIException("21", "You are not Authorized Person.");
		}
	}

	private static final class PlumberJobQuotesMapper implements RowMapper<JobQuotes> {

		@Override
		public JobQuotes mapRow(ResultSet rs, int rowNum) throws SQLException {
			JobQuotes obj = new JobQuotes();
			obj.setId(rs.getInt("tq.id"));
			obj.setJobId(rs.getInt("tq.job_id"));
			obj.setPostCode(rs.getString("postcode"));
			obj.setCustomerId(rs.getInt("customer_id"));
			obj.setPlumberId(rs.getInt("plumber_id"));
			obj.setPrice(rs.getDouble("tq.price"));
			obj.setCustomerName(rs.getString("tc.first_name") + " " + rs.getString("tc.last_name"));
			obj.setPlumberName(rs.getString("tp.first_name") + " " + rs.getString("tp.last_name"));
			obj.setAddress(rs.getString("tj.address"));
			obj.setJobTitle(rs.getString("job_title"));
			obj.setDescription(rs.getString("tq.description"));
			obj.setImage1(rs.getString("image1"));
			obj.setImage2(rs.getString("image2"));
			obj.setAccept(rs.getBoolean("accept"));
			obj.setVideo(rs.getString("video"));
			return obj;
		}
	}

	@Override
	public List<Jobs> finishedAdminJobs(Long id) throws APIException {
		List<Jobs> response = jdbcTemplate.query(
				"select * from job tj left join customer tc on tj.customer_id=tc.customer_id left join plumber tp on tj.plumber_id=tp.plumber_id where tj.finished=true",
				new JobMapper());
		return response;
	}

}
