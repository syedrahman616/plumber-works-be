package com.plumber.daoimpl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BatchProperties.Job;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.JobQoutesRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.PlumberRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.JobInvitation;
import com.plumber.entity.JobQuotes;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.PlumberUser;
import com.plumber.entity.Skill;
import com.plumber.exception.APIException;
import com.plumber.response.APIResponse;
import com.plumber.utils.ResponseBuilder;

@Repository
public class PlumberRepositoryImpl implements PlumberRepository {

	@Autowired
	PlumberUserRepository plumberRepo;

	@Autowired
	CustomerUserRepository customerRepo;

	@Autowired
	UserRepository userRepo;

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	JobRepository jobRepo;

	@Autowired
	JobQoutesRepository jobQoutesRepo;

	@Autowired
	RegisterRepoImpl regImpl;

	@Override
	public APIResponse<Object> plumberProfile(Plumber request, Long id) throws APIException {
		APIResponse<Object> response = null;
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("plumber")) {
			if (user.isPresent() && request.getFlag().equalsIgnoreCase("edit")) {
				Optional<Plumber> plumber = plumberRepo.findById(request.getId());
				if (plumber.isPresent()) {
					plumberRepo.save(request);
					response = ResponseBuilder.build("Success", "Edited Successfully", null);
				} else {
					throw new APIException("21", "Invalid Data.");
				}
			} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("get")) {
				Optional<Plumber> plumber = plumberRepo.findById(request.getId());
				if (plumber.isPresent()) {
					Optional<Plumber> obj = plumberRepo.findById(request.getId());
					response = ResponseBuilder.build("Success", "Plumber Details", obj);
				} else {
					throw new APIException("21", "Invalid Data.");
				}
			} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("delete")) {
				Optional<Plumber> plumber = plumberRepo.findById(request.getId());
				if (plumber.isPresent()) {
					Optional<PlumberUser> users = userRepo.findById(plumber.get().getPlumberId());
					PlumberUser obj = new PlumberUser();
					obj.setStatus(false);
					userRepo.save(obj);
					plumberRepo.deleteById(request.getId());
					response = ResponseBuilder.build("Success", "Deleted Successfully", null);
				} else {
					throw new APIException("21", "Invalid Data.");
				}
			} else {
				throw new APIException("21", "User Not Found.");
			}
		} else {
			throw new APIException("21", "You are not Authorized Person.");
		}
		return response;
	}

	@Override
	public void addSkill(Skill request, Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.get().getUserRole().equalsIgnoreCase("customer")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("plumber_id", request.getPlumberId());
			param.addValue("job_id", request.getJobId());
			param.addValue("rating", request.getRating());
			param.addValue("customer_id", id);
			int count = jdbcTemplate.queryForObject(
					"select count(*) from skill where job_id=:job_id and plumber_id=:plumber_id", param, Integer.class);
			if (count == 0) {
				jdbcTemplate.update(
						"insert into skill (plumber_id,customer_id,job_id,rating) value(:plumber_id,:customer_id,:job_id,:rating)",
						param);
			} else {
				throw new APIException("21", "This Job Already Skill rating added.");
			}
		}else {
			throw new APIException("21", "Invalid Data.");
		}

	}

	@Override
	public List<Jobs> plumberJobs(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		List<Jobs> response = new ArrayList<>();
		if (user.get().getUserRole().equalsIgnoreCase("plumber")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("plumber_id", id);
			response = jdbcTemplate.query(
					"select * from job tj,plumber tp ,customer tc where tj.plumber_id=tp.plumber_id and tj.customer_id=tc.customer_id and tj.plumber_id=:plumber_id and tj.finished=false",
					param, new JobMapper());
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
			obj.setFixedPrice(rs.getInt("fixed_price"));
			obj.setFinished(rs.getBoolean("finished"));
			obj.setStartDate(rs.getString("start_date"));
			obj.setEndDate(rs.getString("end_date"));
			return obj;
		}
	}

	@Override
	public List<Jobs> allJobs(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.get().getUserRole().equalsIgnoreCase("plumber")) {
			List<Jobs> response = jdbcTemplate.query(
					"select * from job tj left join plumber tp on tj.plumber_id=tp.plumber_id left join customer tc on tj.customer_id=tc.customer_id \r\n"
							+ "where finished = false and tj.plumber_id=0",
					new JobMapper());
//			List<Jobs> job = new ArrayList<>();
//			response = jobRepo.findAll();
//			for (Jobs jb : response) {
//				Optional<Plumber> plumber = plumberRepo.findById(jb.getPlumberId());
//				if (plumber.isPresent()) {
//					jb.setPlumberName(plumber.get().getFirstName() + " " + plumber.get().getLastName());
//				}
//				Optional<Customer> customer = customerRepo.findById(jb.getCustomerId());
//				if (customer.isPresent()) {
//					jb.setCustomerName(customer.get().getFirstName() + " " + customer.get().getLastName());
//				}
//				job.add(jb);
//			}
			return response;
		} else {
			throw new APIException("21", "You Are Not Authorized Person.");
		}
	}

	@Override
	public List<JobInvitation> getPlumberJobInvitation(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("plumber")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("plumber_id", id);
			List<JobInvitation> response = jdbcTemplate.query(
					"select * from job_invite tv,job tj,customer tc where tv.job_id=tj.id and tj.customer_id=tc.customer_id "
							+ "and tv.plumber_id=:plumber_id",
					param, new PlumberJobInvitationMapper());
			return response;
		} else {
			throw new APIException("21", "You are not Authorized Person.");
		}
	}

	private static final class PlumberJobInvitationMapper implements RowMapper<JobInvitation> {

		@Override
		public JobInvitation mapRow(ResultSet rs, int rowNum) throws SQLException {
			JobInvitation obj = new JobInvitation();
			obj.setId(rs.getInt("tv.id"));
			obj.setJobId(rs.getInt("tj.id"));
			obj.setPostCode(rs.getString("postcode"));
			obj.setCustomerId(rs.getInt("customer_id"));
			obj.setPlumberId(rs.getInt("plumber_id"));
			obj.setPrice(rs.getDouble("tv.price"));
			obj.setCustomerName(rs.getString("tc.first_name") + " " + rs.getString("tc.last_name"));
			obj.setAddress(rs.getString("tj.address"));
			obj.setJobTitle(rs.getString("job_title"));
			obj.setDescription(rs.getString("tj.description"));
			obj.setImage1(rs.getString("image1"));
			obj.setImage2(rs.getString("image2"));
			obj.setAccept(rs.getBoolean("accept"));
			obj.setVideo(rs.getString("video"));
			return obj;
		}
	}

	@Override
	public APIResponse<Object> plumberJobQoutes(JobQuotes request, Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		APIResponse<Object> response = null;
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("plumber")) {
			if (user.isPresent() && request.getFlag().equalsIgnoreCase("add")) {
				MapSqlParameterSource param = new MapSqlParameterSource();
				param.addValue("job_id", request.getJobId());
				int count = jdbcTemplate.queryForObject("select count(*) from job_invite where job_id=:job_id", param,
						Integer.class);
				if (count == 0) {
					request.setPlumberId(id);
					jobQoutesRepo.save(request);
					response = ResponseBuilder.build("Success", "Added Successfully", null);
				} else {
					throw new APIException("21", "This job already Customer invited to you,Kindly check.");
				}
			} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("edit")) {
				Optional<JobQuotes> job = jobQoutesRepo.findById(request.getId());
				if (job.isPresent()) {
					jobQoutesRepo.save(request);
					response = ResponseBuilder.build("Success", "Edited Successfully", null);
				} else {
					throw new APIException("21", "Invalid Data.");
				}
			} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("get")) {
				Optional<JobQuotes> jobs = jobQoutesRepo.findById(request.getId());
				if (jobs.isPresent()) {
					Optional<JobQuotes> obj = jobQoutesRepo.findById(request.getId());
					response = ResponseBuilder.build("Success", "Plumber Details", obj);
				} else {
					throw new APIException("21", "Invalid Data.");
				}
			} else if (user.isPresent() && request.getFlag().equalsIgnoreCase("delete")) {
				Optional<JobQuotes> job = jobQoutesRepo.findById(request.getId());
				if (job.isPresent()) {
					jobQoutesRepo.deleteById(request.getId());
					response = ResponseBuilder.build("Success", "Deleted Successfully", null);
				} else {
					throw new APIException("21", "Invalid Data.");
				}
			} else {
				throw new APIException("21", "User Not Found.");
			}
		} else {
			throw new APIException("21", "You are not Authorized Person.");
		}
		return response;
	}

	@Override
	public List<JobQuotes> getPlumberJobQuotes(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("plumber")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("plumber_id", id);
			List<JobQuotes> response = jdbcTemplate.query(
					"select * from job_quotes tq,job tj,customer tc where tq.job_id=tj.id and tj.customer_id=tc.customer_id and tq.plumber_id=:plumber_id",
					param, new PlumberJobQuotesMapper());
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
	public List<Jobs> plumberFinishedJobs(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		List<Jobs> response = new ArrayList<>();
		if (user.get().getUserRole().equalsIgnoreCase("plumber")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("plumber_id", id);
			response = jdbcTemplate.query(
					"select * from job tj,plumber tp ,customer tc where tj.plumber_id=tp.plumber_id and tj.customer_id=tc.customer_id and tj.plumber_id=:plumber_id and tj.finished=true",
					param, new JobMapper());
		}
		return response;
	}

	@Override
	public void finishedPlumberJobs(Long id, int jobId) throws APIException {
		MapSqlParameterSource param = new MapSqlParameterSource();
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.get().getUserRole().equalsIgnoreCase("plumber")) {
			Optional<Jobs> job = jobRepo.findById((long) jobId);
			if (job.isPresent() && job.get().getPlumberId() == id) {
				param.addValue("job_id", jobId);
				jdbcTemplate.update("update job set plumber_finished=true where id=:job_id", param);
				String message = "plumber finished your job kindly Check and Approved.";
				regImpl.userNotify(message, (int) job.get().getCustomerId());
			}
		}
	}

}
