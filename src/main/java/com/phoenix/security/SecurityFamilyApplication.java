package com.phoenix.security;

import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.phoenix.security.incrementer.IntIdGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan("com.phoenix.security.mapper")
public class SecurityFamilyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SecurityFamilyApplication.class, args);
	}

}
