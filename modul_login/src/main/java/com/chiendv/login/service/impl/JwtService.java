package com.chiendv.login.service.impl;

import java.text.ParseException;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.chiendv.login.util.Constant;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

/**
 * Class JwtService.java dùng để tạo và validate token. (tạo token với thông tin
 * username, thời gian hết hạn; kiểm tra thời gian hết han, chữ ký có hợp lệ
 * không…)
 * 
 * @author ADMIN
 *
 */
@Service
public class JwtService {
	
	public String generateTokenLogin(String username) {
		String token = null;
		try {
			// Create HMAC signer
			JWSSigner signer = new MACSigner(generateShareSecret());
			// payload
			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			builder.claim(Constant.USERNAME, username);
			// thời gian tồn tại của token này
			builder.expirationTime(generateExpirationDate());
			JWTClaimsSet claimsSet = builder.build();
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
			// Apply the HMAC protection
			signedJWT.sign(signer);
			token = signedJWT.serialize();
		} catch (KeyLengthException e) {
			e.printStackTrace();
		} catch (JOSEException e) {
			e.printStackTrace();
		}
		return token;
	}

	private byte[] generateShareSecret() {
		return Constant.SECRET_KEY.getBytes();
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME);
	}

	/*
	 * getClaimsFromToken ta sẽ lấy ra JWTClaimsSet, trong đây phải có đúng chuỗi
	 * SECRET đã tạo ra token mới lấy ra được JWTClaimsSet.
	 */
	private JWTClaimsSet getClaimsSetFromToken(String token) {
		JWTClaimsSet claims = null;
		try {
			SignedJWT signedJWT = SignedJWT.parse(token);
			System.out.println("JwtService:-> getClaimsSetFromToken -> signedJWT: " + signedJWT);
			JWSVerifier jwsVerifier = new MACVerifier(generateShareSecret());
			if (signedJWT.verify(jwsVerifier)) {
				claims = signedJWT.getJWTClaimsSet();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return claims;
	}

	private Date getExperationDateFromToken(String token) {
		System.out.println("JwtService:-> getExperationDateFromToken ");
		JWTClaimsSet jwtClaimsSet = getClaimsSetFromToken(token);
		Date experation = jwtClaimsSet.getExpirationTime();
		System.out.println("ExperationDate " + experation);
		return experation;
	}

	public String getUsernameFromToken(String token) {
		System.out.println("JwtService:-> getUsernameFromToken ");
		try {
			JWTClaimsSet jwtClaimsSet = getClaimsSetFromToken(token);
			return jwtClaimsSet.getStringClaim(Constant.USERNAME);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	// kiểm tra xem token còn Experation không
	private boolean isExpriedToken(String token) {
		Date exprition = getExperationDateFromToken(token);
		return exprition.before(new Date());
	}

	public boolean validateTokenLogin(String token) {
		System.out.println("JwtService:-> validateTokenLogin ");
		if (token == null || token.trim().length() == 0) {
			System.out.println("Tokken null");
			return false;
		}
		String username = getUsernameFromToken(token);
		if (username == null || username.isEmpty()) {
			return false;
		}
		if (isExpriedToken(token)) {
			return false;
		}
		return true;
	}
}
