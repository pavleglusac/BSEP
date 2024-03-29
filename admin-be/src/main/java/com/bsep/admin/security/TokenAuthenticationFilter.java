package com.bsep.admin.security;

import com.bsep.admin.exception.InvalidAccessTokenException;
import com.bsep.admin.exception.InvalidTokenTypeException;
import com.bsep.admin.exception.ResponseError;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private TokenProvider tokenProvider;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private TokenManager tokenManager;

	private String[] ignoredUrls = {
			"/api/auth/login",
	};


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = readTokenFromRequest(request);

		if (request.getRequestURI().equals("/api/auth/login")) {
			filterChain.doFilter(request, response);
			return;
		}

		if (!StringUtils.hasLength(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		try {
			tokenProvider.validateToken(token, TokenType.ACCESS);
			if(!tokenManager.isTokenValid(token)) {
				sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid authorization.");
				return;
			}

			String secretFromToken = tokenProvider.getSecretFromToken(token);
			String secretFromCookie = readSecretFromCookie(request);

			if (!secretFromToken.equals(secretFromCookie)) {
				System.out.println("Secrets don't match!");
//				System.out.println("Secret from token: " + secretFromToken);
//				System.out.println("Secret from cookie: " + secretFromCookie);
				sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid authorization.");
				return;
			}

			UUID userId = tokenProvider.getUserIdFromToken(token);

			UserDetails userDetails = customUserDetailsService.loadUserById(userId);

			if (userDetails == null) {
				sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid authorization.");
				return;
			}

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
			filterChain.doFilter(request, response);
		} catch (InvalidTokenTypeException e) {
			e.printStackTrace();
			sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token type.");
		} catch (InvalidAccessTokenException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
			sendResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
		} catch (Exception e) {
			sendResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error has occurred.");
			e.printStackTrace();
		}
	}

	private String readTokenFromRequest(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.hasLength(authHeader) && authHeader.startsWith("Bearer "))
			return authHeader.substring(7);

		String token = request.getParameter("token");
		if (StringUtils.hasLength(token))
			return token;

		return null;
	}

	private String readSecretFromCookie(HttpServletRequest request) {
		// Reads secret value from cookie
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("secret")) {
				return cookie.getValue();
			}
		}
		return null;
	}


	private void sendResponse(HttpServletResponse response, Integer status, String message) throws IOException {
		ResponseError responseError = new ResponseError(status, message);
		response.setStatus(status);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		new ObjectMapper().writeValue(response.getOutputStream(), responseError);
	}

}