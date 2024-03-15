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
import com.plumber.dao.JobQoutesRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.JobAccept;
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

	@Autowired
	RegisterRepoImpl regRepo;

	@Autowired
	JobQoutesRepository jobQoutesRepo;

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
				Optional<PlumberUser> users = userRepo.findById(customer.get().getCustomerId());
				PlumberUser obj = new PlumberUser();
				obj.setStatus(false);
				userRepo.save(obj);
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
			response = jdbcTemplate.query("select * from job tj left join plumber tp on tj.plumber_id=tp.plumber_id "
					+ "left join customer tc on tj.customer_id=tc.customer_id "
					+ "where tj.customer_id=:customer_id and finished=false", param, new JobMapper());
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
			obj.setCustomerName(rs.getString("tc.first_name") == null ? ""
					: rs.getString("tc.first_name") + " " + rs.getString("tc.last_name") == null ? ""
							: rs.getString("tc.last_name"));
			obj.setPlumberName(rs.getString("tp.first_name") == null ? ""
					: rs.getString("tp.first_name") + " " + rs.getString("tp.last_name") == null ? ""
							: rs.getString("tp.last_name"));
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
			obj.setPlumberFinished(rs.getBoolean("plumber_finished"));
			return obj;
		}
	}

	@Override
	public List<Plumber> plumberDetails(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.isPresent() && user.get().getUserRole().equalsIgnoreCase("customer")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("customer_id", id);
			List<Integer> AlreadyInvitePlumberId = jdbcTemplate.queryForList(
					"select ti.plumber_id from job tj ,job_invite ti where tj.id=ti.job_id and tj.customer_id=:customer_id",
					param, Integer.class);
			List<Plumber> plumber = jdbcTemplate.query(
					"select * from plumber tp ,user tu where tp.plumber_id=tu.id and tu.verified=true",
					new PlumberMapper());
			return plumber;
		} else {
			throw new APIException("21", "You Are Not Authorized Person.");
		}
	}

	private class PlumberMapper implements RowMapper<Plumber> {

		@Override
		public Plumber mapRow(ResultSet rs, int rowNum) throws SQLException {
			Plumber obj = new Plumber();
			obj.setId(rs.getLong("id"));
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("plumber_id", rs.getInt("plumber_id"));
			int totalCount = jdbcTemplate.queryForObject("select count(*) from skill where plumber_id=:plumber_id",
					param, Integer.class);
			double ratingPercentage = 0;
			if (totalCount > 0) {
				int rating = jdbcTemplate.queryForObject(
						"select sum(rating) as totalRating from skill where plumber_id=:plumber_id", param,
						Integer.class);
				ratingPercentage = Math.round(((double) rating / totalCount) / 5 * 100);
			}
			obj.setSkill(ratingPercentage);
			obj.setPostCode(rs.getString("tp.postcode"));
			obj.setPlumberId(rs.getInt("plumber_id"));
			obj.setFirstName(rs.getString("tp.first_name"));
			obj.setAddress(rs.getString("tp.address"));
			obj.setLastName(rs.getString("tp.last_name"));
			obj.setUserEmail(rs.getString("tu.user_name"));
			obj.setCity(rs.getString("tp.city"));
			return obj;
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
				String message = "You have new job invitation from customer.";
				regRepo.userNotify(message, request.getPlumberId());
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
			obj.setPlumberName(rs.getString("tp.first_name") == null ? ""
					: rs.getString("tp.first_name") + " " + rs.getString("tp.last_name") == null ? ""
							: rs.getString("tp.last_name"));
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
			obj.setPlumberName(rs.getString("tp.first_name") == null ? ""
					: rs.getString("tp.first_name") + " " + rs.getString("tp.last_name") == null ? ""
							: rs.getString("tp.last_name"));
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

	/**
	 *
	 */
	@Override
	public void JobAccept(JobAccept request, Long id) throws APIException {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("id", request.getId());
		int count = request.getAction().equalsIgnoreCase("invitation")
				? jdbcTemplate.queryForObject("select count(*) from job_invite where id=:id", param, Integer.class)
				: jdbcTemplate.queryForObject("select count(*) from job_quotes where id=:id", param, Integer.class);
		param.addValue("Job_id", request.getJobId());
		if (count > 0) {
			JobAccept job = request.getAction().equalsIgnoreCase("invitation")
					? jdbcTemplate.queryForObject("select * from job_invite where id=:id", param, new JobAcceptMapper())
					: jdbcTemplate.queryForObject("select * from job_quotes where id=:id", param,
							new JobAcceptMapper());
			param.addValue("price", job.getPrice());
			param.addValue("plumber_id", job.getPlumberId());
			param.addValue("job_id", job.getJobId());
			if (request.getAction().equalsIgnoreCase("invitation")) {
				param.addValue("start_date", request.getStartDate());
				param.addValue("end_date", request.getEndDate());
				int dateCheck = jdbcTemplate.queryForObject(
						"select count(*) from job where start_date <=:start_date and end_date >=:end_date and plumber_id=:plumber_id",
						param, Integer.class);
				if (dateCheck == 0) {
					jdbcTemplate.update("update job_invite set accept=true where id=:id", param);
					jdbcTemplate.update(
							"update job set isFixed=true,fixed_price=:price,plumber_id=:plumber_id,start_date=:start_date,end_date=:end_date where id=:job_id; ",
							param);
					Optional<Jobs> jobId = jobRepo.findById((long) job.getJobId());
					String message = "Your Job Invitation was accepted.";
					regRepo.userNotify(message, (int) jobId.get().getCustomerId());
				} else {
					throw new APIException("21", "Please select another date.");
				}
			} else if (request.getAction().equalsIgnoreCase("quotes")) {
				Optional<JobQuotes> quotes = jobQoutesRepo.findById((long) request.getId());
				param.addValue("start_date", quotes.get().getStartDate());
				param.addValue("end_date", quotes.get().getEndDate());
				jdbcTemplate.update("update job_quotes set accept=true where id=:id", param);
				jdbcTemplate.update(
						"update job set isFixed=true,fixed_price=:price,plumber_id=:plumber_id,start_date=:start_date,end_date=:end_date where id=:job_id; ",
						param);
				String message = "Your Job Quotes was accepted.";
				regRepo.userNotify(message, job.getPlumberId());
			} else {
				throw new APIException("21", "Invalid Data.");
			}
		}
	}

	private static final class JobAcceptMapper implements RowMapper<JobAccept> {

		@Override
		public JobAccept mapRow(ResultSet rs, int rowNum) throws SQLException {
			JobAccept obj = new JobAccept();
			obj.setPlumberId(rs.getInt("plumber_id"));
			obj.setPrice(rs.getDouble("price"));
			obj.setJobId(rs.getInt("job_id"));
			return obj;
		}
	}

	@Override
	public List<Jobs> finishedCustomerJob(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		List<Jobs> response = new ArrayList<>();
		if (user.get().getUserRole().equalsIgnoreCase("customer")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("customer_id", id);
			response = jdbcTemplate.query("select * from job tj left join plumber tp on tj.plumber_id=tp.plumber_id "
					+ "left join customer tc on tj.customer_id=tc.customer_id "
					+ "where tj.customer_id=:customer_id and tj.plumber_finished=true", param, new JobMapper());
		}
		return response;
	}

	@Override
	public void finishedCustomerjob(Long id, int jobId) throws APIException {
		MapSqlParameterSource param = new MapSqlParameterSource();
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.get().getUserRole().equalsIgnoreCase("customer")) {
			Optional<Jobs> job = jobRepo.findById((long) jobId);
			if (job.isPresent() && job.get().getCustomerId() == id) {
				param.addValue("job_id", jobId);
				jdbcTemplate.update("update job set finished=true where id=:job_id", param);
				String message = "Customer Approved your job.";
				regRepo.userNotify(message, (int) job.get().getPlumberId());
			} else {
				throw new APIException("21", "You are Invalid Customer.");
			}
		}
	}

	@Override
	public List<Plumber> plumberinviteDetails(Long id, int jobId) throws APIException {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("job_id", jobId);
		List<Plumber> plumber = jdbcTemplate.query(
				"select * from plumber tp where not exists(select 1 from job_invite tv where tp.plumber_id=tv.plumber_id and tv.job_id=:job_id);",
				param, new PlumberMapper());
		return plumber;
	}

}
