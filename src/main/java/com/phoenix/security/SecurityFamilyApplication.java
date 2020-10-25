package com.phoenix.security;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.phoenix.security.mapper")
public class SecurityFamilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityFamilyApplication.class, args);
	}

}
