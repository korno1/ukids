package com.modernfamily.ukids.global.jwt;

import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.util.HttpResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if(authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bearer 부분 제거 토큰 획득
        String token = authorization.replace("Bearer ", "");
        if(jwtUtil.isExpired(token)){
            filterChain.doFilter(request, response);

            return ;
        }

        Authentication authToken = jwtUtil.getAuthentication(token);

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);

    }
}
