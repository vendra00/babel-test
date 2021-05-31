package com.babel.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableSpringDataWebSupport
@EnableCaching
@EnableSwagger2
public class BabetTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BabetTestApplication.class, args);
	}

}
