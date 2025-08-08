package io.github.NEVERMAIN.gateway.core.authorization;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @description jwt工具类
 */
public class JwtUtil {

    private static final String SECRET_KEY = "0123456789abcdef0123456789abcdef"; // 至少 256-bit (32 字符)

    // 1. 获取签名密钥
    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * 生成 JWT Token 字符串
     *
     * @param subject    签发人
     * @param ttlMillis 有效时间
     * @param claims    额外信息
     * @return JWT Token
     */
    public static String generateToken(Map<String, Object> claims, String subject, long ttlMillis) {
        if( null == claims){
            claims = new HashMap<>();
        }
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        JwtBuilder builder = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256);

        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            // 过期时间（exp）：荷载部分的标准字段之一，代表这个 JWT 的有效期。
            builder.setExpiration(exp);
        }

        return  builder.compact();
    }


    /**
     * 解析 JWT Token
     *
     * @param token
     * @return
     */
    // 3. 解析 Claims
    public static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 4. 提取指定字段
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 5. 获取 username / userId（你在 subject 中设置的）
    public static String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 6. 判断是否过期
    public static boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // 7. 校验 Token 是否有效
    public static boolean validateToken(String token, String expectedSubject) {
        final String subject = extractSubject(token);
        return (subject.equals(expectedSubject) && !isTokenExpired(token));
    }


}
