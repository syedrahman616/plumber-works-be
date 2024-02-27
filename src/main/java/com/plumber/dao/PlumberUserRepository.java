package com.plumber.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.plumber.entity.Plumber;

public interface PlumberUserRepository extends JpaRepository<Plumber, Long> {
	Optional<Plumber> findByPlumberId(long id);

	@Query("select tp from Plumber tp where tp.status = true")
	List<Plumber> findByPlumbers();
}
