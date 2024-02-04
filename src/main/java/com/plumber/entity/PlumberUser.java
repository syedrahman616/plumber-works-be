package com.plumber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;

import lombok.Data;
@Data
@Entity
@Table(name = "user")
public class PlumberUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "user_email", nullable = false)
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "user_name", nullable = false)
	private String usrName;
	@Column(name = "verified", nullable = false)
	private boolean verified;
	@Column(name = "status", nullable = false)
	private boolean status;
	@Column(name = "user_role", nullable = false)
	private String userRole;
}
