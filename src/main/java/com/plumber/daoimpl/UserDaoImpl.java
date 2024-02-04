package com.plumber.daoimpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.plumber.dao.CustomerUserRepository;
import com.plumber.dao.PlumberUserRepository;
import com.plumber.dao.UserRepository;
import com.plumber.dao.UserprofileRepository;
import com.plumber.entity.Customer;
import com.plumber.entity.Plumber;
import com.plumber.entity.PlumberUser;
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

	@Override
	public void updateProfile(UserProfile request, Long id) throws APIException {
		Optional<PlumberUser> user = usrRepo.findById(id);
		if (user.isPresent()) {
			if ("customer".equalsIgnoreCase(user.get().getUserRole())) {
				Customer obj = new Customer();
				obj.setCustomerId(id);
				obj.setFullName(request.getFullName());
				obj.setAddress(request.getAddress());
				obj.setCity(request.getCity());
				obj.setPostCode(request.getPostCode());
				obj.setMobile(request.getMobile());
				customerRepo.save(obj);
			} else if ("plumber".equalsIgnoreCase(user.get().getUserRole())) {
				Plumber obj = new Plumber();
				obj.setPlumberId(id);
				obj.setFullName(request.getFullName());
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
				obj.setFullName(customer.get().getFullName());
				obj.setAddress(customer.get().getAddress());
				obj.setCity(customer.get().getCity());
				obj.setPostCode(customer.get().getPostCode());
				obj.setMobile(customer.get().getMobile());
			} else if ("plumber".equalsIgnoreCase(user.get().getUserRole())) {
				Optional<Plumber> plumber = plumberRepo.findByPlumberId(id);
				obj.setId(plumber.get().getId());
				obj.setFullName(plumber.get().getFullName());
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

}
