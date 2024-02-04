package com.plumber.serviceimpl;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.plumber.service.LoginService;

@Repository
public class LoginServiceImpl implements LoginService {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Override
	public String generateToken(String receivedMail) throws NoSuchAlgorithmException {
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
		cal.add(Calendar.HOUR_OF_DAY, 1);
		String currentTimeZone = sdf.format(cal.getTime());
		String[] splitArray = { receivedMail, currentTimeZone };
		String combinedString = String.join(",", splitArray);
		String encodedBytes = Base64.getEncoder().encodeToString(combinedString.getBytes());
		return encodedBytes;
	}
}
