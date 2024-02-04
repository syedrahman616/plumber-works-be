package com.plumber.daoimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import com.plumber.dao.PlumberRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
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
	UserRepository userRepo;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

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

}
