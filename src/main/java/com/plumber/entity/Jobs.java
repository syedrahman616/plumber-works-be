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
@Table(name = "job")
public class Jobs {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Column(name = "customer_id", nullable = false)
	private long customerId;
	@Column(name = "plumber_id", nullable = false)
	private long plumberId;
	@Column(name = "job_title", nullable = false)
	private String jobTitle;
	@Column(name = "description", nullable = false)
	private String description;
	@Column(name = "image1", nullable = false)
	private String image1;
	@Column(name = "image2", nullable = false)
	private String image2;
	@Column(name = "video", nullable = false)
	private String video;
	@Column(name = "address", nullable = false)
	private String address;
	@Column(name = "postcode", nullable = false)
	private String postCode;
	@Column(name = "hours", nullable = false)
	private int hours;
	@Column(name = "rate_per_hour", nullable = false)
	private int ratePerHour;
	@Column(name = "fixed_price", nullable = false)
	private int fixedPrice;
	@Column(name = "customer_start_date", nullable = false)
	private String customerStartDate;
	@Column(name = "customer_end_date", nullable = false)
	private String customerEndDate;
	@Column(name = "plumber_start_date", nullable = false)
	private String plumberStartDate;
	@Column(name = "plumber_end_date", nullable = false)
	private String plumberEndDate;
	@Column(name = "finished", nullable = false)
	private boolean finished;
	@Transient
	private String flag;
	@Transient
	private String customerName;
	@Transient
	private String plumberName;
}
