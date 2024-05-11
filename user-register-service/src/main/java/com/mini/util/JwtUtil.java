package com.mini.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
	private static final String SECRET_KEY = "afafasfafafasfasfasfafacasdasfasxASFACASDFACASDFASFASFDAFASFASDAADSCSDFADCVSGCFVADXCcadwavfsfarvf";
	private long currentTime;
	private long expirationTime;

	public String extractUsername(String token) {
		logger.info("BEGIN - [extractUsername(token)]");
		logger.info("END - [extractUsername(token)]");
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		logger.info("BEGIN - [extractExpiration(token)]");
		logger.info("END - [extractUsername(token)]");
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		logger.info("BEGIN - [extractClaims()]");
		final Claims claims = extractAllClaims(token);
		logger.info("END - [extractClaims()]");
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String token) {
		logger.info("BEGIN - [extractAllClaims(token)]");
		logger.info("END - [extractAllClaims()]");
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		logger.info("BEGIN - [isTokenExpired(token)]");
		logger.info("END - [isTokenExpired(token)]");
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		logger.info("BEGIN - [generateToken(userDetails)]");
		Map<String, Object> claims = new HashMap<>();
		logger.debug("CLaims" + claims);
		logger.info("END - [generateToken(userDetails)]");
		return createToken(claims, userDetails.getUsername());
	}

	private String createToken(Map<String, Object> claims, String subject) {
		logger.info("BEGIN - [createToken()]");
		logger.info("END - [createToken()]");

		setCurrentTime(System.currentTimeMillis());
		setExpirationTime(getCurrentTime() + 1000 * 60 * 60);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(getExpirationTime())).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		logger.info("BEGIN - [validateToken(token,userDetails)]");
		final String username = extractUsername(token);
		logger.debug("Username " + username);
		logger.info("END - [validateToken(token,userDetails)]");
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

}
