package com.plumber.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plumber.entity.Customer;

public interface CustomerUserRepository extends JpaRepository<Customer, Long>{
	Optional<Customer> findByCustomerId(long id);

}
