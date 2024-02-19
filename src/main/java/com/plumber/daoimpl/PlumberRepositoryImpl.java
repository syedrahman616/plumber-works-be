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

import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.PlumberRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
import com.plumber.entity.Customer;
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

	@Override
	public APIResponse<Object> plumberProfile(Plumber request, Long id) throws APIException {
		APIResponse<Object> response = null;
		Optional<PlumberUser> user = userRepo.findById(id);
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
	public void addSkill(Skill request, Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.get().getUserRole().equalsIgnoreCase("plumber")) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("skillName", request.getSkillName());
			param.addValue("level", request.getSkillLevel());
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
					"select * from job tj,plumber tp ,customer tc where tj.plumber_id=tp.plumber_id and tj.customer_id=tc.customer_id and tj.plumber_id=:plumber_id", param,
					new JobMapper());
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
			obj.setDescription(rs.getString("tj.description"));
			obj.setImage1(rs.getString("image1"));
			obj.setImage2(rs.getString("image2"));
			obj.setVideo(rs.getString("video"));
			return obj;
		}
	}

	@Override
	public List<Jobs> allJobs(Long id) throws APIException {
		Optional<PlumberUser> user = userRepo.findById(id);
		if (user.get().getUserRole().equalsIgnoreCase("plumber")) {
			List<Jobs> response = new ArrayList<>();
			response = jobRepo.findAll();
			for (Jobs jb : response) {
				Optional<Plumber> plumber = plumberRepo.findById(jb.getPlumberId());
				if (plumber.isPresent()) {
					jb.setPlumberName(plumber.get().getFirstName() + " " + plumber.get().getLastName());
				}
				Optional<Customer> customer = customerRepo.findById(jb.getCustomerId());
				if (customer.isPresent()) {
					jb.setCustomerName(customer.get().getFirstName() + " " + customer.get().getLastName());
				}
			}
			return response;
		} else {
			throw new APIException("21", "You Are Not Authorized Person.");
		}

	}

}
