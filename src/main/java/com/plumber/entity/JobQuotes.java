package com.plumber.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@Entity
@Table(name = "job_quotes")
@JsonInclude(value = Include.NON_EMPTY, content = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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
	@Column(name = "start_date", nullable = false)
	private String startDate;
	@Column(name = "end_date", nullable = false)
	private String endDate;
	@Column(name = "hours", nullable = false)
	private int hours;
	@Column(name = "price", nullable = false)
	private double price;
	@Column(name = "accept", nullable = false)
	private boolean accept;
	@Transient
	private String flag;
	@Transient
	private String image1;
	@Transient
	private String image2;
	@Transient
	private String video;
	@Transient
	private String address;
	@Transient
	private String postCode;
	@Transient
	private String customerName;
	@Transient
	private String plumberName;
	@Transient
	private String jobTitle;
	@Transient
	private int customerId;

}
