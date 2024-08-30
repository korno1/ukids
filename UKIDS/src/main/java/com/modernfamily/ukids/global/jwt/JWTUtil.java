package com.modernfamily.ukids.global.jwt;

import com.modernfamily.ukids.domain.user.dto.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtil {
    private Key key;

    private final UserDetailsService userDetailsService;


    public JWTUtil(@Value("${spring.jwt.secret}") String secret, UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
        byte[] byteSecretKey = Decoders.BASE64.decode(secret);
        key = Keys.hmacShaKeyFor(byteSecretKey);
    }

    public Long getUserId(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("userId", Long.class);
    }

    public String getId(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("id", String.class);
    }

    public Boolean isExpired(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token){
        UserDetails user = userDetailsService.loadUserByUsername(getId(token));
        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
    }


    public String CreateJwt(CustomUserDetails user, Long expiredMs){
        Claims claims = Jwts.claims();
        claims.put("userId", user.getUserId());
        claims.put("id", user.getUsername());
        claims.put("name", user.getName());
        claims.put("phone", user.getPhone());
        claims.put("email", user.getEmail());
        claims.put("birthDate", user.getBirthDate());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

}
