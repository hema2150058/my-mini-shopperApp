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

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfigurer {
	
	@Autowired
	JwtRequestFilter jwtRequestFilter;

	@Autowired
	private LRService userService;
	
	private static final String[] WHITE_LIST_URL = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swagger-ui/**", "/webjars/**", "/swagger-ui.html", "/api/auth/**",
			"/api/test/**", "/authenticate" };


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
		
		 http
		 .csrf(AbstractHttpConfigurer::disable)
		 .authorizeHttpRequests(registry ->{
			registry.requestMatchers("/register", "/signin","/createNewRole","/validate","/getCustomerDetails","/example").permitAll();
			registry.requestMatchers("/getAllCustomersData","getAll").hasRole("SHOPPER");
			registry.requestMatchers("/updateAddress").hasRole("CUSTOMER");
			registry.requestMatchers(WHITE_LIST_URL).permitAll();
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
	
//	@Bean
//	public OpenAPI openAPI() {
//		return new OpenAPI().info(new Info().title("MiniShopper Login Register Service")
//											.description("Minishopper login register microservice")
//											.contact(new Contact().name("miniShopper"))
//											.version("1.0.0."));
//	}
}
