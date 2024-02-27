package com.plumber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "jobquotes")
public class JobQuotes {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "job_id", nullable = false)
	private long jobId;
	@Column(name = "plumber_id", nullable = false)
	private long plumberId;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "hours", nullable = false)
	private int hours;
	@Column(name = "rate_per_hour", nullable = false)
	private int ratePerHour;
	@Column(name = "price", nullable = false)
	private int price;
	@Column(name = "approved", nullable = false)
	private boolean approved;
	@Transient
	private String flag;

}
