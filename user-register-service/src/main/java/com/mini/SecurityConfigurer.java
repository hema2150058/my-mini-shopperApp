package com.mini;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.mini.filter.JwtRequestFilter;
import com.mini.service.LRService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer {
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;

	@Autowired
	private LRService userService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		 http
		 .csrf(AbstractHttpConfigurer::disable)
		 .authorizeHttpRequests(registry ->{
			registry.requestMatchers("/register", "/signin","/createNewRole","/validate","/getCustomerDetails","/example").permitAll();
			registry.requestMatchers("/getAllCustomersData","getAll").hasRole("SHOPPER");
			registry.requestMatchers("/updateAddress").hasRole("CUSTOMER");
			registry.requestMatchers(HttpHeaders.ALLOW).permitAll();
			registry.anyRequest().authenticated();
		});
		 http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
		 http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		 return http.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		return userService;
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
