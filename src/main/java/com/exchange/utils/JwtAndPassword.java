package com.exchange.utils;

import com.exchange.postgres.service.MemberService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAndPassword {

    @Value("${secretKey}")
    private String secretKey;

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /*public boolean comparePassword(String password, String hashed) {
        return BCrypt.checkpw(password, hashed);
    }*/

    public String makeJwt(String memberId) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        Date expireTime = new Date();
        // 20분
        expireTime.setTime(expireTime.getTime() + 1000 * 60 * 20);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        Map<String, Object> headerMap = new HashMap<String, Object>();

        headerMap.put("type", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("memberId", memberId);

        try {
            return Jwts.builder()
                    .setHeader(headerMap)
                    .setClaims(map)
                    .setExpiration(expireTime)
                    .signWith(signatureAlgorithm, signingKey)
                    .compact();
        } catch (Exception e) {
            log.info("토큰 검증 실패");
            return null;
        }
    }

    public String checkJwt(String jwt) {
        try{
            // 정상 수행되면 해당 토큰은 정상이다.
            Claims claims = Jwts.parserBuilder().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
                    .build().parseClaimsJws(jwt).getBody();

            log.info("expire time: " + claims.getExpiration());
            log.info("memberId: " + claims.get("memberId"));

            return claims.get("memberId").toString();
        }catch(ExpiredJwtException exception){
            log.info("토큰 만료");
            return null;
        }catch(JwtException exception){
            log.info("토큰 변조");
            return null;
        }
    }
}
