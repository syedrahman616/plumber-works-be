package com.plumber.daoimpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.JobRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
import com.plumber.dao.UserprofileRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.Jobs;
import com.plumber.entity.Plumber;
import com.plumber.entity.PlumberUser;
import com.plumber.entity.UserNotify;
import com.plumber.entity.UserProfile;
import com.plumber.exception.APIException;

@Repository
public class UserDaoImpl implements UserprofileRepository {

	@Autowired
	CustomerUserRepository customerRepo;

	@Autowired
	PlumberUserRepository plumberRepo;

	@Autowired
	UserRepository usrRepo;

	@Autowired
	JobRepository jobRepo;

	@Autowired
	NamedParameterJdbcTemplate jdbcTemplate;

	@Value("${upload-dir}")
	private String uploadDirectory;

	@Override
	public void updateProfile(UserProfile request, Long id) throws APIException {
		Optional<PlumberUser> user = usrRepo.findById(id);
		if (user.isPresent()) {
			if ("customer".equalsIgnoreCase(user.get().getUserRole())) {
				Customer obj = new Customer();
				obj.setCustomerId(id);
				obj.setFirstName(request.getFirstName());
				obj.setLastName(request.getLastName());
				obj.setAddress(request.getAddress());
				obj.setCity(request.getCity());
				obj.setPostCode(request.getPostCode());
				obj.setMobile(request.getMobile());
				customerRepo.save(obj);
			} else if ("plumber".equalsIgnoreCase(user.get().getUserRole())) {
				Plumber obj = new Plumber();
				obj.setPlumberId(id);
				obj.setFirstName(request.getFirstName());
				obj.setLastName(request.getLastName());
				obj.setAddress(request.getAddress());
				obj.setCity(request.getCity());
				obj.setPostCode(request.getPostCode());
				obj.setMobile(request.getMobile());
				plumberRepo.save(obj);
			}
		}
	}

	@Override
	public UserProfile getProfile(Long id) throws APIException {
		Optional<PlumberUser> user = usrRepo.findById(id);
		if (user.isPresent()) {
			UserProfile obj = new UserProfile();
			if ("customer".equalsIgnoreCase(user.get().getUserRole())) {
				Optional<Customer> customer = customerRepo.findByCustomerId(id);
				obj.setId(customer.get().getId());
				obj.setFirstName(customer.get().getFirstName());
				obj.setLastName(customer.get().getLastName());
				obj.setAddress(customer.get().getAddress());
				obj.setCity(customer.get().getCity());
				obj.setPostCode(customer.get().getPostCode());
				obj.setMobile(customer.get().getMobile());
			} else if ("plumber".equalsIgnoreCase(user.get().getUserRole())) {
				Optional<Plumber> plumber = plumberRepo.findByPlumberId(id);
				obj.setId(plumber.get().getId());
				obj.setFirstName(plumber.get().getFirstName());
				obj.setLastName(plumber.get().getLastName());
				obj.setAddress(plumber.get().getAddress());
				obj.setCity(plumber.get().getCity());
				obj.setPostCode(plumber.get().getPostCode());
				obj.setMobile(plumber.get().getMobile());
			}
			return obj;
		} else {
			throw new APIException("21", "Invalid Data.");
		}
	}

	@Override
	public void jobStartDate(int jobId, Long id) throws APIException {
		Optional<PlumberUser> user = usrRepo.findById(id);
		if (user.isPresent()) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			String currentDateTime = dateFormat.format(currentDate);
			Optional<Jobs> job = jobRepo.findById((long) jobId);
			if (job.isPresent()) {
				param.addValue("start_date", currentDateTime);
				param.addValue("jobId", job.get().getId());
				if (user.get().getUserRole().equalsIgnoreCase("customer")) {
					jdbcTemplate.update("update job set customer_start_date=:start_date where id=:jobId", param);
					String message = "Your Job was started the work by customer";
					userNotify(message, (int) job.get().getPlumberId());
				} else if (user.get().getUserRole().equalsIgnoreCase("plumber")) {
					jdbcTemplate.update("update job set plumber_start_date=:start_date where id=:jobId", param);
					String message = "Your Job was started the work by plumber";
					userNotify(message, (int) job.get().getCustomerId());
				}
			}
		}
	}

	@Override
	public void jobEndDate(int jobId, Long id) throws APIException {
		Optional<PlumberUser> user = usrRepo.findById(id);
		if (user.isPresent()) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
			String currentDateTime = dateFormat.format(currentDate);
			Optional<Jobs> job = jobRepo.findById((long) jobId);
			if (job.isPresent()) {
				param.addValue("end_date", currentDateTime);
				param.addValue("jobId", job.get().getId());
				if (user.get().getUserRole().equalsIgnoreCase("customer")) {
					jdbcTemplate.update("update job set customer_end_date=:end_date,finished=true where id=:jobId",
							param);
					String message = "Your recent job is finished approved by customer.";
					userNotify(message, (int) job.get().getPlumberId());
				} else if (user.get().getUserRole().equalsIgnoreCase("plumber")) {
					jdbcTemplate.update("update job set plumber_end_date=:end_date where id=:jobId", param);
					String message = "Your recent job is finished approved by plumber.";
					userNotify(message, (int) job.get().getCustomerId());
				}
			}
		}

	}

	@Override
	public String uploadFile(MultipartFile uploadFile, Long id) throws APIException {
		if (uploadFile.isEmpty()) {
			throw new APIException("21", "Please select a file to upload");
		}
		try {
			System.out.println(uploadDirectory);
			String originalFileName = uploadFile.getOriginalFilename();
			String fileExtension = "";
			if (originalFileName != null) {
				int lastIndex = originalFileName.lastIndexOf('.');
				if (lastIndex >= 0) {
					fileExtension = originalFileName.substring(lastIndex);
				}
			}
			String currentTimeMillis = String.valueOf(System.currentTimeMillis());
			String fileName = currentTimeMillis + fileExtension;
			Path path = Paths.get(uploadDirectory, fileName);
			Files.copy(uploadFile.getInputStream(), path);
			String filePath = fileName;
			return filePath;
		} catch (IOException e) {
			e.printStackTrace();
			throw new APIException("21", "Failed to upload file");
		}
	}

	@Override
	public Resource getFiles(String fileName) throws APIException {
		Path imagePath = Paths.get(uploadDirectory, fileName);
		try {
			Resource resource = new UrlResource(imagePath.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new APIException("21", "Data Not Found.");
			}
		} catch (IOException e) {
			throw new APIException("21", "Invalid Data.");
		}
	}

	@Override
	public List<UserNotify> getNotify(Long id) {
		Optional<PlumberUser> user = usrRepo.findById(id);
		List<UserNotify> response = new ArrayList<>();
		if (user.isPresent()) {
			MapSqlParameterSource param = new MapSqlParameterSource();
			param.addValue("userId", id);
			response = jdbcTemplate.query(
					"select * from notify where user_id=:userId and notify_date > now - INTERVAL 12 HOUR or notify_date is null",
					param, new GetNotifyMapper());

		}
		return response;
	}

	private static final class GetNotifyMapper implements RowMapper<UserNotify> {

		@Override
		public UserNotify mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserNotify obj = new UserNotify();
			obj.setId(rs.getInt("id"));
			obj.setMessage(rs.getString("message"));
			return obj;
		}
	}

	public void userNotify(String message, int userId) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("userId", userId);
		param.addValue("message", message);
		jdbcTemplate.update("insert into notify (user_id,message) value(:userId,:message)", param);
	}

	@Override
	public void userNotify(String id) throws APIException {
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("id", id);
		int count = jdbcTemplate.queryForObject("select count(*) from notify where id=:id", param, Integer.class);
		if (count > 0) {
			jdbcTemplate.update("update notify set notify_date=now() where id=:id", param);
		} else {
			throw new APIException("21", "Invalid Data.");
		}

	}

}
