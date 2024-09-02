package T2F2.SPOT.domain.note.websocket;

import T2F2.SPOT.domain.user.Role;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JWTUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("Received headers: {}", accessor.toNativeHeaderMap());

        // 이미 인증된 사용자인지 확인
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return message; // 이미 인증된 사용자면 바로 리턴
        }

        // WebSocket 핸드셰이크 요청에서 Authorization 헤더에서 JWT 토큰 추출
        String jwtToken = String.valueOf(accessor.getNativeHeader("Authorization"));

        if(jwtToken != null && jwtToken.startsWith("Bearer ")) {
            jwtToken = jwtToken.substring(7);
        } else {
            log.warn("Authorization header is missing or does not start with 'Bearer '");
            return message;
        }

        try {
            // JWT 토큰 만료되었는지 확인
            if (jwtUtil.isExpired(jwtToken)) {
                log.warn("JWT token is expired");
                return message;
            }

            // 토큰의 카테고리가 'access'인지 확인
            String category = jwtUtil.getCategory(jwtToken);
            if (!category.equals("access")) {
                log.warn("Token's category is not 'access'.");
                return message;
            }

            // 토큰에서 username, role을 가져와 User 객체 생성
            String username = jwtUtil.getUsername(jwtToken);
            Role role = Role.valueOf(jwtUtil.getRole(jwtToken));

            User user = new User();
            user.setEmail(username);
            user.setRole(role);

            // CustomUserDetails로 감싸 Spring Security의 Authentication 객체 생성
            CustomUserDetails customUserDetails = new CustomUserDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, customUserDetails.getAuthorities()
            );

            // SecurityContext에 인증 객체 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Authenticated user: {}", username);
        } catch (Exception e) {
            log.error("JWT token validation failed: {}", e.getMessage());
        }

        return message;
    }
}
