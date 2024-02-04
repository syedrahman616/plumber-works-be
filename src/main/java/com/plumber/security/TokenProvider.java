package com.plumber.security;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.plumber.config.AppProperties;
import com.plumber.entity.TokenCreate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenProvider {

	private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);

	private AppProperties appProperties;

	public TokenProvider(AppProperties appProperties) {
		this.appProperties = appProperties;
	}

	private Key getSigningKey() {
		byte[] keyBytes = appProperties.getAuth().getTokenSecret().getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}

	private Key getMeetSigningKey() {
		byte[] keyBytes = appProperties.getAuth().getMeetSecret().getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public TokenCreate createToken(Authentication authentication) {
		TokenCreate obj = new TokenCreate();
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		Date now = new Date();
		String currentDateTime = "_" + System.currentTimeMillis();
		Map<String, Object> claims = new HashMap<>();
		claims.put("currentDateTime", currentDateTime.toString());
		Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
		obj.setToken(Jwts.builder().setClaims(claims).setSubject(Long.toString(userPrincipal.getId()))
				.setIssuedAt(new Date()).setExpiration(expiryDate).signWith(getSigningKey()).compact());
		obj.setCurrentTime(currentDateTime.toString());
		return obj;
	}

	public TokenCreate createSocialToken(long usrId) {
		TokenCreate obj = new TokenCreate();
		Date now = new Date();
		String currentDateTime = "_" + System.currentTimeMillis();
		Map<String, Object> claims = new HashMap<>();
		claims.put("currentDateTime", currentDateTime.toString());
		Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());
		obj.setToken(Jwts.builder().setClaims(claims).setSubject(Long.toString(usrId)).setIssuedAt(new Date())
				.setExpiration(expiryDate).signWith(getSigningKey()).compact());
		obj.setCurrentTime(currentDateTime.toString());
		return obj;
	}

	public String createVerifyToken(String issuer, String reciever) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

		return Jwts.builder().setSubject(reciever).setIssuer(issuer).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(getSigningKey()).compact();
	}

	public String createRegisterToken(String userId) {
		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

		return Jwts.builder().setSubject(userId).setIssuedAt(new Date()).setExpiration(expiryDate)
				.signWith(getSigningKey()).compact();
	}

	public String createMeetToken(Authentication authentication, String room) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

		Date now = new Date();
		Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getMeetTokenExpirationMsec());
		return Jwts.builder().setSubject("meeting.iskulu.in").setAudience("meetingjola").setIssuer("meetingjola")
				.claim("room", room).signWith(getMeetSigningKey()).setHeaderParam("typ", "JWT").compact();
	}

	public Long getUserIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getSubject());
	}

	public Long getIssuerIdFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(token).getBody();

		return Long.parseLong(claims.getIssuer());
	}

	public boolean validateToken(String authToken) {

		try {
			Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException ex) {
			logger.error("Invalid JWT token");
		} catch (ExpiredJwtException ex) {
			logger.error("Expired JWT token");
		} catch (UnsupportedJwtException ex) {
			logger.error("Unsupported JWT token");
		} catch (IllegalArgumentException ex) {
			logger.error("JWT claims string is empty.");
		}
		return false;
	}

}
