package com.plumber.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.plumber.entity.Jobs;

public interface JobRepository extends JpaRepository<Jobs, Long> {

}
