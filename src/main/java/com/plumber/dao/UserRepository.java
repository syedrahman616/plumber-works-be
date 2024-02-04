package com.plumber.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plumber.entity.PlumberUser;

@Repository
public interface UserRepository extends JpaRepository<PlumberUser, Long> {

	Optional<PlumberUser> findByEmail(String email);

	Optional<PlumberUser> findById(Long id);

	Optional<PlumberUser> findByusrName(String usrName);
}
