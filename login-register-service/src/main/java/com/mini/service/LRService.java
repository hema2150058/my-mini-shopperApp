package com.mini.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.management.RuntimeErrorException;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.mini.dto.JwtRequest;
import com.mini.dto.JwtResponse;
import com.mini.dto.RegisterDto;
import com.mini.dto.UserDto;
import com.mini.exception.UserAlreadyExistException;
import com.mini.exception.UserNotFoundException;
import com.mini.model.Address;
import com.mini.model.Role;
import com.mini.model.User;
import com.mini.repo.AddressRepo;
import com.mini.repo.RoleRepo;
import com.mini.repo.UserRepo;
import com.mini.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LRService implements UserDetailsService {

	@Autowired
	private RoleRepo roleRepo;
	
	@Autowired
	private AddressRepo addressRepo;
	
	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
		
	public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		User user = userRepo.findByUserEmail(email);
		
		if(user==null) {
			log.error("User not found with emailId: "+email);
			throw new UsernameNotFoundException("User not found for email: "+email);
		}
		log.info("line 67"+user.getUserEmail()+ user.getUserName()+ getAuthority(user));
		return new org.springframework.security.core.userdetails.User
				(user.getUserEmail(), user.getUserPassword(), getAuthority(user));
	}
	
	private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

	public User saveUsers(User users) throws UserAlreadyExistException{
		try {
			log.info("User details saved to the database");
			return userRepo.save(users);
		}
		catch(InternalServerError e) {
			log.error("Email or username is already in use. Please choose a different one");
			throw new UserAlreadyExistException("Email or username is already in use. Please choose a different one");		}
		
	}

	public boolean existsByUserEmail(String emailId) {
		boolean exist = userRepo.existsByUserEmail(emailId);
		
		if(exist) {
			log.info("User exists by emailId : "+emailId );
		return userRepo.existsByUserEmail(emailId);
		}else {
			log.warn("User doesn't exist by emailId : "+ emailId);
			return false;
		}	
	}
	
	public boolean existsByUserName(String userName) {
		boolean exist = userRepo.existsByUserName(userName);
		if(exist) {
			log.info("User exists by username : "+userName);
			return userRepo.existsByUserName(userName);
		}else {
			log.warn("User doesn't exist by username :"+ userName);
			return false;
		}
		
	}
	
	public RegisterDto registerUser(UserDto userDTO) throws UserAlreadyExistException{
		
		boolean b = (this.existsByUserEmail(userDTO.getUserEmail()));
		if(this.existsByUserEmail(userDTO.getUserEmail()) && this.existsByUserName(userDTO.getUserName())) {
			log.error("Username and EmailId are already Exist.");
			throw new UserAlreadyExistException("Username and EmailId are already Exist.");
		}
		else if (b) {
			log.error("Email '" + userDTO.getUserEmail()+ "' Already Exist ");
			throw new UserAlreadyExistException("Email '" + userDTO.getUserEmail()+ "' Already Exist ");
		}
		else if(this.existsByUserName(userDTO.getUserName()) ) {
			log.error("UserName '" + userDTO.getUserName() + "' Already Exist" );
			throw new UserAlreadyExistException("UserName '" + userDTO.getUserName() + "' Already Exist" );
		}
	
		else {
			User users = new User();
			users.setUserName(userDTO.getUserName());
			users.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
			users.setUserFirstName(userDTO.getUserFirstName());
			users.setUserLastName(userDTO.getUserLastName());
			users.setUserEmail(userDTO.getUserEmail());
			users.setCreatedDate(userDTO.getCreatedDate());
			//address
			Address address = new Address();
			address.setAddressId(users.getUserName());
			address.setAddressLine(userDTO.getAddress().getAddressLine());
			address.setStreet(userDTO.getAddress().getStreet());
			address.setCity(userDTO.getAddress().getCity());
			address.setState(userDTO.getAddress().getState());
			address.setPincode(userDTO.getAddress().getPincode());
			users.setAddress(address);
			log.info("Address data added to the address table");
			
			HashSet<Role> roles = new HashSet<>();
			Role role = new Role();
			role.setRoleName("CUSTOMER");
			roles.add(role);
			log.info("Every new user's role is set as 'Customer'");
			users.setRole(roles);
			this.saveUsers(users);
			RegisterDto regisDto = new RegisterDto();
			regisDto.setUserName(users.getUserName());
			regisDto.setEmail(users.getUserEmail());
			log.info("User registered Successfully");
			return regisDto;
		}
	}
	
	public JwtResponse loginNewUser(JwtRequest jwtRequest) throws UserNotFoundException {
		try {
		log.info(jwtRequest.getEmailId());
		Boolean b = this.existsByUserEmail(jwtRequest.getEmailId());
		log.info(b.toString());
		if (!this.existsByUserEmail(jwtRequest.getEmailId())) {
			log.error("User Not Found with email : " + jwtRequest.getEmailId());
			throw new UserNotFoundException("User Not Found with email : " + jwtRequest.getEmailId());
		} else {

			final UserDetails userDetails = loadUserByUsername(jwtRequest.getEmailId());
			User user = userRepo.findByUserEmail(jwtRequest.getEmailId());
			log.info(user.getUserName());
			System.out.println("jwt password "+ jwtRequest.getUserPassword() + "userdeatils password : "+ userDetails.getPassword());
			if ((jwtRequest.getUserPassword()).equals(userDetails.getPassword())) {
				return 
						new JwtResponse(userDetails.getUsername(), user.getUserName(), jwtTokenUtil.generateToken(userDetails),
								jwtTokenUtil.getCurrentTime(), jwtTokenUtil.getExpirationTime());
					
			}else if (passwordEncoder.matches(jwtRequest.getUserPassword(), userDetails.getPassword())){
				return 
						new JwtResponse(userDetails.getUsername(), user.getUserName(), jwtTokenUtil.generateToken(userDetails),
								jwtTokenUtil.getCurrentTime(), jwtTokenUtil.getExpirationTime());
			}
			
			
		}
		}
		catch(BadCredentialsException e) {
			throw new UsernameNotFoundException("not");
		}
		return null;
	}
	
}
