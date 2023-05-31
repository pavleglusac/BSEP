package com.bsep.admin.config;


import com.bsep.admin.security.CustomAuthenticationProvider;
import com.bsep.admin.security.CustomUserDetailsService;
import com.bsep.admin.security.RestAuthenticationEntryPoint;
import com.bsep.admin.security.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity(debug = false)
@EnableGlobalMethodSecurity(
		prePostEnabled = true
)
public class SecurityConfig {

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Bean
	public AuthenticationManager authManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder =
				http.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(authProvider);
		authenticationManagerBuilder.parentAuthenticationManager(null);
		return authenticationManagerBuilder.build();
	}

	@Bean
	public TokenAuthenticationFilter tokenAuthenticationFilter() {
		return new TokenAuthenticationFilter();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http

				.cors()
					.and()
				.sessionManagement()
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					.and()
				.csrf()
					.disable()
				.addFilterAfter(new CspFilter(), HeaderWriterFilter.class)
				.headers()
					.xssProtection()
					.and()
					.frameOptions().disable()
					.and()
				.formLogin()
					.disable()
				.httpBasic()
					.disable()
				.exceptionHandling()
					.authenticationEntryPoint(new RestAuthenticationEntryPoint())
					.and()
				.requiresChannel()
				.anyRequest()
				.requiresSecure()
				.and()
				.authorizeHttpRequests()
					.requestMatchers(new AntPathRequestMatcher("/api/**")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/swagger-ui/**")).permitAll()
					.requestMatchers(new AntPathRequestMatcher("/v3/api-docs/**")).permitAll()
					.anyRequest().authenticated();

		http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);


		return http.build();
	}


}