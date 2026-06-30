package com.shopdds.security;

import com.shopdds.common.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具：签发、解析、校验
 */
@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${shopdds.jwt.secret}")
    private String secret;

    @Value("${shopdds.jwt.expiration-ms}")
    private long expirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 签发 token
     */
    public String generate(LoginUser user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        return Jwts.builder()
                .subject(user.getUserId())
                .claim("name", user.getName())
                .claim("role", user.getRole().getCode())
                .claim("facilityId", user.getFacilityId() == null ? "" : user.getFacilityId())
                .claim("facilityName", user.getFacilityName() == null ? "" : user.getFacilityName())
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    /**
     * 解析 token，返回登录用户信息
     */
    public LoginUser parse(String token) {
        try {
            Claims c = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            String roleCode = c.get("role", String.class);
            return LoginUser.builder()
                    .userId(c.getSubject())
                    .name(c.get("name", String.class))
                    .role(RoleEnum.valueOf(roleCode))
                    .facilityId(nullIfEmpty(c.get("facilityId", String.class)))
                    .facilityName(nullIfEmpty(c.get("facilityName", String.class)))
                    .build();
        } catch (Exception e) {
            log.debug("JWT 解析失败: {}", e.getMessage());
            return null;
        }
    }

    public boolean isValid(String token) {
        return parse(token) != null;
    }

    private static String nullIfEmpty(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}