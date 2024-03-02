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

import com.plumber.dao.CustomerRepository;
import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
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
public class CustomerRepositoryImpl implements CustomerRepository {

	@Autowired
	CustomerUserRepository customerRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	JobRepository jobRepo;

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	PlumberUserRepository plumberRepo;

	@Override
	public APIResponse<Object> customerProfile(Customer request, Long id) throws APIException {
		APIResponse<Object> response = null;
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && request.getFlag().equalsIgnoreCase("edit")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				customerRepo.save(request);
				response = ResponseBuilder.build("Success", "Edited Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("get")) {
			Optional<Customer> customer = customerRepo.findById(request.getId());
			if (customer.isPresent()) {
				Optional<Customer> obj = customerRepo.findById(request.getId());
				response = ResponseBuilder.build("Success", "Plumber Details", obj);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("delete")) {
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
	public APIResponse<Object> job(Jobs request, Long id) throws APIException {
		APIResponse<Object> response = null;
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && request.getFlag().equalsIgnoreCase("add")) {
			request.setCustomerId(id);
			jobRepo.save(request);
			response = ResponseBuilder.build("Success", "Added Successfully", null);
		} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("edit")) {
			Optional<Jobs> job = jobRepo.findById(request.getId());
			if (job.isPresent()) {
				jobRepo.save(request);
				response = ResponseBuilder.build("Success", "Edited Successfully", null);
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("delete")) {
			Optional<Jobs> job = jobRepo.findById(request.getId());
			if (job.isPresent()) {
				jobRepo.save(request);
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
	public List<Jobs> customerJobs(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		List<Jobs> response = new ArrayList<>();
		if (user.get().getUserRole().equalsIgnoreCase("customer")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("customer_id", id);
			response = jdbcTemplate.query("select * from job tj left join plumber tp on tj.plumber_id=tp.plumber_id\r\n"
					+ "left join customer tc on tj.customer_id=tc.customer_id \r\n"
					+ "where tj.customer_id=:customer_id", param, new JobMapper());
		}
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
			return obj;
		}
	}

	@Override
	public List<Plumber> plumberDetails(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("customer")) {
			List<Plumber> plumber = plumberRepo.findByPlumbers();
			return plumber;
		} else {
			throw new APIException("21", "You Are Not Authorized Person.");
		}
	}

	@Override
	public APIResponse<Object> jobInvitation(JobInvitation request, Long id) throws APIException {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("job_id", request.getJobId());
		param.addValue("plumber_id", request.getPlumberId());
		param.addValue("price", request.getPrice());
		param.addValue("description", request.getDescription());
		if (request.getFlag().equalsIgnoreCase("add")) {
			int count = jdbcTemplate.queryForObject(
					"select count(*) from job_invite where job_id=:job_id and plumber_id=:plumber_id", param,
					Integer.class);
			if (count == 0) {
				jdbcTemplate.update(
						"insert into job_invite(job_id,plumber_id,price,description,accept) value(:job_id,:plumber_id,:price,:description,false)",
						param);
			} else {
				throw new APIException("21", "You are already inivited this job.");
			}
		} else if (request.getFlag().equalsIgnoreCase("edit")) {
			param.addValue("id", request.getId());
			param.addValue("description", request.getDescription());
			int count = jdbcTemplate.queryForObject("select count(*) from job_invite where id=:id", param,
					Integer.class);
			if (count > 0) {
				jdbcTemplate.update("update job_invite set(price=:price,description=:description) where id=:id", param);
			}
		}
		return ResponseBuilder.build("Success", "invitation Successfully", null);
	}

	@Override
	public List<JobInvitation> getJobInvitation(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("customer")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("customer_id", id);
			List<JobInvitation> response = jdbcTemplate.query(
					"select * from job_invite tv,job tj,plumber tp where tv.job_id=tj.id and tv.plumber_id=tp.plumber_id "
							+ "and tj.customer_id=:customer_id",
					param, new CustomerJobInvitationMapper());
			return response;
		} else {
			throw new APIException("21", "You are not Authorized Person.");
		}
	}

	private static final class CustomerJobInvitationMapper implements RowMapper<JobInvitation> {

		@Override
		public JobInvitation mapRow(ResultSet rs, int rowNum) throws SQLException {
			JobInvitation obj = new JobInvitation();
			obj.setId(rs.getInt("tv.id"));
			obj.setJobId(rs.getInt("tj.id"));
			obj.setPostCode(rs.getString("postcode"));
			obj.setCustomerId(rs.getInt("customer_id"));
			obj.setPlumberId(rs.getInt("plumber_id"));
			obj.setPlumberName(rs.getString("tp.first_name") + " " + rs.getString("tp.last_name"));
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
	public List<JobQuotes> getCustomerQuotes(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("customer")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("customer_id", id);
			List<JobQuotes> response = jdbcTemplate.query(
					"select * from job_quotes tq,job tj,plumber tp where tq.job_id=tj.id and tq.plumber_id=tp.plumber_id and tj.customer_id=:customer_id",
					param, new CustomerJobQuotesMapper());
			return response;
		} else {
			throw new APIException("21", "You are not Authorized Person.");
		}
	}

	private static final class CustomerJobQuotesMapper implements RowMapper<JobQuotes> {

		@Override
		public JobQuotes mapRow(ResultSet rs, int rowNum) throws SQLException {
			JobQuotes obj = new JobQuotes();
			obj.setId(rs.getInt("tq.id"));
			obj.setJobId(rs.getInt("tq.job_id"));
			obj.setPostCode(rs.getString("postcode"));
			obj.setCustomerId(rs.getInt("customer_id"));
			obj.setPlumberId(rs.getInt("plumber_id"));
			obj.setPrice(rs.getDouble("tq.price"));
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
}
