package com.bsep.admin;

import com.bsep.admin.config.AppProperties;
import com.bsep.admin.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class AdminApplication {

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// show all users one by one in for loop
		userRepository.findAll().forEach(System.out::println);
	}

}
