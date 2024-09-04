package T2F2.SPOT.domain.note.websocket;

import T2F2.SPOT.domain.user.Role;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.exception.TokenException;
import T2F2.SPOT.domain.user.jwt.JWTUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if(accessor.getCommand() == StompCommand.CONNECT) {
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            if (!this.validateAccessToken(accessToken)) {
                throw new RuntimeException("access token이 필요합니다");
            }

            String email = this.getEmail(accessToken);
            accessor.addNativeHeader("senderEmail", email);
        }
        return message;
    }

    private String getEmail(String accessToken) {
        String bearerToken = accessToken.trim();

        if(!bearerToken.trim().isEmpty() && bearerToken.startsWith("Bearer ")) {
            accessToken = bearerToken.substring(7);

            try {
                String email = jwtUtil.getUsername(accessToken);
                return email;
            } catch (ExpiredJwtException | MalformedJwtException e) {
                throw new RuntimeException("해당 토큰이 만료되었거나 올바른 형태가 아닙니다.");
            }
        }

        return null;
    }

    private boolean validateAccessToken(String accessToken) {
        if(accessToken == null) {
            return false;
        }

        String bearerToken = accessToken.trim();

        if(!bearerToken.trim().isEmpty() && bearerToken.startsWith("Bearer ")) {
            accessToken = bearerToken.substring(7);

            try {
                String email = jwtUtil.getUsername(accessToken);
                return true;
            } catch (ExpiredJwtException | MalformedJwtException e) {
                return false;
            }
        }
        return false;
    }

    @EventListener(SessionConnectEvent.class)
    public void onApplicationEvent(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String accessToken = accessor.getFirstNativeHeader("Authorization");
        if (this.validateAccessToken(accessToken)) {
            String email = this.getEmail(accessToken);
            accessor.getSessionAttributes().put("senderEmail", email);
        }
    }

}
