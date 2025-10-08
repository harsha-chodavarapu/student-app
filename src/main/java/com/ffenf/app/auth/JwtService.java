package com.ffenf.app.auth;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${app.jwt.secret:ZmZlbmYtc2VjcmV0LWF0LWxlYXN0LTMyLWNoYXJzLWJhc2U2NA==}")
	private String secretBase64;

	@Value("${app.jwt.ttlSeconds:3600}")
	private long ttlSeconds;

	private javax.crypto.SecretKey getSigningKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretBase64);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String issueToken(String subject, Map<String, Object> claims) {
		Instant now = Instant.now();
		return Jwts.builder()
			.setSubject(subject)
			.addClaims(claims)
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(now.plusSeconds(ttlSeconds)))
			.signWith(getSigningKey())
			.compact();
	}

	public Claims parse(String jwt) {
		return Jwts.parser()
			.verifyWith(getSigningKey())
			.build()
			.parseSignedClaims(jwt)
			.getPayload();
	}
}


