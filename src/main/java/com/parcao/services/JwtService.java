package com.parcao.services;

import org.springframework.security.core.Authentication;

public interface JwtService {
	String generateToken(Authentication auth);
	String extractUsername(String token);
	boolean validateToken(String authToken);
}
