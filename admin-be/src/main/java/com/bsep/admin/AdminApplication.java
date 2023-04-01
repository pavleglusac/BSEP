package com.bsep.admin;

import com.bsep.admin.config.AppProperties;
import com.bsep.admin.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.security.Security;

@SpringBootApplication
@EnableTransactionManagement
@EnableConfigurationProperties(AppProperties.class)
public class AdminApplication {

	@Autowired
	private UserRepository userRepository;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@PostConstruct
	public void init() {
		// show all users one by one in for loop
		Security.addProvider(new BouncyCastleProvider());
		userRepository.findAll().forEach(System.out::println);
	}

}
