package com.modernfamily.ukids.global.handler;

import com.modernfamily.ukids.domain.family.entity.Family;
import com.modernfamily.ukids.domain.familyMember.entity.FamilyMember;
import com.modernfamily.ukids.domain.familyMember.model.repository.FamilyMemberRepository;
import com.modernfamily.ukids.global.config.JwtConfig;
import com.modernfamily.ukids.global.exception.CustomException;
import com.modernfamily.ukids.global.exception.ExceptionResponse;
import com.modernfamily.ukids.global.jwt.JWTUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.security.Key;
import java.security.Principal;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
//@Order(Ordered.HIGHEST_PRECEDENCE + 99)  // Spring security보다 우선순위를 높여야 함
public class StompHandler implements ChannelInterceptor {

    private final JwtConfig jwtConfig;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // 커스텀 헤더 접근용 -> JWT 가져오기
        StompHeaderAccessor accessor = StompHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        byte[] byteSecretKey = Decoders.BASE64.decode(jwtConfig.getSecret());
        Key key = Keys.hmacShaKeyFor(byteSecretKey);

        assert accessor != null;

        log.info("======websocket command========= : {}", accessor.getCommand().name());
        if(accessor.getCommand() == StompCommand.CONNECT) {

            String token = String.valueOf(accessor.getNativeHeader("Authorization").get(0));

            if(token == null || token.isBlank() || !token.startsWith("Bearer ")) {
                log.info("++++++JWT token is null+++++");
                return null;
            }

            token = token.replace("Bearer ", "");
            boolean isExpired = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getExpiration().before(new Date());

            if(isExpired) {
                log.info("++++++++JWT token is Expired++++++++");
                return null;
            }

            String userId = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("id", String.class);

            Principal userPrincipal = new Principal() {
                @Override
                public String getName() {
                    return userId;
                }
            };

            accessor.setUser(userPrincipal);
        }

        return message;
    }
}
