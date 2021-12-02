package com.melon.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String CLAIM_KEY_USERID = "sub";

    private static final String CLAIM_KEY_CREATED = "created";

    private static final String secret = "melon-auth-secret";

    private static final Long expiration = 86400L;

    // 生成 token
    public String generateToken(String username, String userId) {
        // 添加载荷
//        log.info("generate token for user {}", username);
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERID, userId);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }


    // 根据token获取用户 ID
    public String getUserIdFromToken(String token) {
        String userId = null;
        // 获取荷载
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            userId = claims.getSubject();
        }
//        else {
//            log.error("get userId from token failed, token: {}", token);
//        }

        return userId;
    }


    // 验证token是否有效
    public boolean validateToken(String token) {
        // 是否超时
        return !isTokenExpired(token);
    }


    // 判断token是否失效
    private boolean isTokenExpired(String token) {
        Date expireDate = getExpireDateFromToken(token);

        if (expireDate != null)
            return expireDate.before(new Date());
        return true;
    }


    // 从token中获取失效时间
    private Date getExpireDateFromToken(String token) {
        Date expirationDate = null;
        Claims claims = getClaimsFromToken(token);
        if (claims != null) {
            expirationDate = claims.getExpiration();
        }
//        else {
//            log.error("get expiration date from token failed, token: {}", token);
//        }

        return expirationDate;
    }


    // 从token中获取载荷
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
//            log.error("get claims from token failed, token: {}", token);
            e.printStackTrace();
        }

        return claims;
    }


    // 根据荷载生成token
    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }


    // 生成token失效时间
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

}
