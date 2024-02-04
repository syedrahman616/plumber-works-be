package com.plumber;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import com.plumber.config.AppProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
@ComponentScan(basePackages = "com.*")
public class PlumberApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlumberApplication.class, args);
	}

}
