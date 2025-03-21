package com.jspider.e_commerce.config;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
	 SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
	
	public String generateToken(Authentication auth) {
		String jwt = Jwts.builder()
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime()+ 846000000)) // 24 hours
				.claim("email", auth.getName())
				.signWith(key).compact();
				return jwt;
				
	}
	
	public String getEmailFromToken(String jwt) {
		
	jwt = jwt.substring(7);   //  token se phle koi keyword rhta hai aur use hi htane k liye hm substring method ka use krte hai;
	
	 // Parse the claims from the JWT
    Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .getBody();
    
    // Extract the email and authorities from claims
    String email = String.valueOf(claims.get("email"));
    
    return email;
	}
	
}
