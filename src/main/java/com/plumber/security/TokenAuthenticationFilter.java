package com.plumber.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.plumber.dao.UserRepository;
import com.plumber.daoimpl.RegisterRepoImpl;
import com.plumber.exception.APIException;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	UserRepository userRepo;

	@Autowired
	RegisterRepoImpl regRepo;

	private static final Logger logger = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			long jwtUsrId = getJwtFromRequest(request);
			if (jwtUsrId > 0) {
				UserDetails userDetails = customUserDetailsService.loadUserById(jwtUsrId);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (APIException exp) {
			throw new ServletException(exp.getApiError().getError().getDescription());
		} catch (Exception ex) {
			logger.error("Could not set user authentication in security context", ex);
			throw new ServletException("Invalid Access.");
		}
		filterChain.doFilter(request, response);
	}

	private long getJwtFromRequest(HttpServletRequest request) throws APIException {
		String token = request.getHeader("Authorization");
 		if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
			String tokenSplit = token.substring(7, token.length());
			long usrId = tokenVerifer(tokenSplit);
			return usrId;
		}
		return 0;
	}

	private long tokenVerifer(String token) throws APIException {
		DecodedJWT decodedJWT = JWT.decode(token);
		String verifyTokenCreatedTime = decodedJWT.getClaim("currentDateTime").asString();
		boolean tokenValidator = tokenProvider.validateToken(token);
		long usrId = 0;
		if (tokenValidator) {
			usrId = tokenProvider.getUserIdFromToken(token);
		}
		return usrId;
	}
}
