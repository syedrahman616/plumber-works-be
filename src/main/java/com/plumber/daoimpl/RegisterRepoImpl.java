package com.plumber.daoimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import com.plumber.dao.RegisterRepository;
import com.plumber.entity.SignupRequest;
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

	@Override
	public boolean userSignUp(SignupRequest request) throws APIException {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("email", request.getEmail());
		int count = namedJdbcTemplate.queryForObject("select count(*) from user where user_email=:email", param,
				Integer.class);
		if (count == 0) {
			param.addValue("password", request.getPassword());
			param.addValue("userRole", request.getUserRole());
			namedJdbcTemplate.update(
					"insert into user (user_name,password,user_email,user_role,status,verified,created_date) "
							+ "value(:email,:password,:email,:userRole,true,true,now())",
					param);
			return true;
		} else {
		 throw new APIException("21", "You Are Already Register with us.");
		}
	}

}
