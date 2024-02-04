package com.pg.neet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "com.*")
public class PlumberApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlumberApplication.class, args);
	}

}
