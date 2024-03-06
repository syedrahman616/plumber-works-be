package com.plumber.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.plumber.dao.RegisterRepository;
import com.plumber.dao.UserRepository;
import com.plumber.dao.UserprofileRepository;
import com.plumber.entity.SignupRequest;
import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;
import com.plumber.security.TokenProvider;

@Repository
public class RegisterRepoImpl implements RegisterRepository {

	@Autowired
	NamedParameterJdbcTemplate namedJdbcTemplate;

	@Autowired
	TokenProvider tokenProvider;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserprofileRepository profileRepo;

	@Override
	public boolean userSignUp(SignupRequest request) throws APIException {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("email", request.getEmail());
		int count = namedJdbcTemplate.queryForObject("select count(*) from user where user_email=:email", param,
				Integer.class);
		if (count == 0) {
			param.addValue("password", request.getPassword());
			param.addValue("userRole", request.getUserRole());
			KeyHolder key = new GeneratedKeyHolder();
			namedJdbcTemplate
					.update("insert into user (user_name,password,user_email,user_role,status,verified,created_date) "
							+ "value(:email,:password,:email,:userRole,true,true,now())", param, key);
			long userId = key.getKey().longValue();
			UserProfile obj = new UserProfile();
			obj.setAddress(request.getAddress());
			obj.setFirstName(request.getFirstName());
			obj.setLastName(request.getLastName());
			obj.setCity(request.getCity());
			obj.setPostCode(request.getPostCode());
			obj.setMobile(request.getMobile());
			profileRepo.updateProfile(obj, userId);
			return true;
		} else {
			throw new APIException("422", "You Are Already Register with us.");
		}
	}

	public void userNotify(String message, int userId) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("message", message);
		namedJdbcTemplate.update("insert into notify (user_id,message) value(:userId,:message)", param);
	}
}
