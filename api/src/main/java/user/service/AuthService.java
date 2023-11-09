package user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.model.LoginRequest;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private Key secretKey;
    @Value("${jwt.secret.key}") private String JWT_SECRET_KEY;
    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }
    public String createAccessToken(String userId, String userNm) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("userNm", userNm);
        return Jwts.builder()
                .setClaims(claims)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, Object> generateToken(LoginRequest loginRequest){
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String accessToken = createAccessToken(loginRequest.getUserId(), loginRequest.getUserNm());
        resultMap.put("token", accessToken);
        return resultMap;
    }
}