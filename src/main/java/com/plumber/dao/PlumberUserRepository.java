package com.plumber.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plumber.entity.Plumber;

public interface PlumberUserRepository extends JpaRepository<Plumber, Long>{
	Optional<Plumber> findByPlumberId(long id);
}
