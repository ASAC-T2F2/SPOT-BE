package T2F2.SPOT.domain.note.websocket;

import T2F2.SPOT.domain.user.Role;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
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
        StompCommand command = accessor.getCommand();

        log.info("WebSocket Message Command: {}", command);
        log.info("Received headers: {}", accessor.toNativeHeaderMap());

        if (StompCommand.CONNECT == command) {
            List<String> authHeaders = accessor.getNativeHeader("Authorization");
            String jwtToken = null;
            if (authHeaders != null && !authHeaders.isEmpty()) {
                String authHeader = authHeaders.get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    jwtToken = authHeader.substring(7);
                }
            }
            if (jwtToken != null) {
                try {
                    if (jwtUtil.isExpired(jwtToken)) {
                        log.warn("JWT token is expired");
                        throw new IllegalArgumentException("JWT token is expired");
                    }

                    String category = jwtUtil.getCategory(jwtToken);
                    if (!category.equals("access")) {
                        log.warn("Token's category is not 'access'.");
                        throw new IllegalArgumentException("Invalid token category");
                    }

                    String username = jwtUtil.getUsername(jwtToken);
                    Role role = Role.valueOf(jwtUtil.getRole(jwtToken));
                    User user = new User();
                    user.setEmail(username);
                    user.setRole(role);

                    CustomUserDetails customUserDetails = new CustomUserDetails(user);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            customUserDetails, null, customUserDetails.getAuthorities()
                    );

                    // WebSocketSession에 인증 정보 설정
                    accessor.setUser(authentication);

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.info("Authenticated user: {}", username);
                } catch (Exception e) {
                    log.error("JWT token validation failed: {}", e.getMessage());
                    throw new IllegalArgumentException("JWT token validation failed", e);
                }
            } else {
                log.warn("Authorization header is missing or does not start with 'Bearer '");
                throw new IllegalArgumentException("Missing Authorization header"); // 인증 헤더 없음
            }
        }

        return message;
    }

    @Override
    public void postSend(Message<?> message, org.springframework.messaging.MessageChannel channel, boolean sent) {
        // Check if the message is of type SEND
        if (message.getHeaders().get("command") != null && message.getHeaders().get("command").equals("SEND")) {
            // Retrieve the authentication information
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication != null && authentication.isAuthenticated()) {
                System.out.println("Authenticated user: " + authentication.getName());
            } else {
                System.out.println("Authentication is null or invalid");
            }
        }
    }
//    @Override
//    public Message<?> preSend(Message<?> message, MessageChannel channel) {
//        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
//
//        StompCommand command = accessor.getCommand();
//        log.info("WebSocket Message Command: {}", command);
//
//        log.info("Received headers: {}", accessor.toNativeHeaderMap());
//
//        // 이미 인증된 사용자인지 확인
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            return message; // 이미 인증된 사용자면 바로 리턴
//        }
//
//        // CONNECT 요청이 아닌 경우 추가 인증 처리 생략
//        if (StompCommand.CONNECT != command) {
//            log.info("Non-CONNECT command received, skipping authentication.");
//            return message;
//        }
//
//        // WebSocket 핸드셰이크 요청에서 Authorization 헤더에서 JWT 토큰 추출
//        List<String> authHeaders = accessor.getNativeHeader("Authorization");
//
//        String jwtToken = null;
//        if(authHeaders != null && !authHeaders.isEmpty()) {
//            String authHeader = authHeaders.get(0);
//            if (authHeader != null && authHeader.startsWith("Bearer ")) {
//                jwtToken = authHeader.substring(7);
//            }
//        }
//        if(jwtToken == null) {
//            log.warn("Authorization header is missing or does not start with 'Bearer '");
//            return message;
//        }
//
//        try {
//            // JWT 토큰 만료되었는지 확인
//            if (jwtUtil.isExpired(jwtToken)) {
//                log.warn("JWT token is expired");
//                return message;
//            }
//
//            // 토큰의 카테고리가 'access'인지 확인
//            String category = jwtUtil.getCategory(jwtToken);
//            if (!category.equals("access")) {
//                log.warn("Token's category is not 'access'.");
//                return message;
//            }
//
//            // 토큰에서 username, role을 가져와 User 객체 생성
//            String username = jwtUtil.getUsername(jwtToken);
//            Role role = Role.valueOf(jwtUtil.getRole(jwtToken));
//
//            User user = new User();
//            user.setEmail(username);
//            user.setRole(role);
//
//            // CustomUserDetails로 감싸 Spring Security의 Authentication 객체 생성
//            CustomUserDetails customUserDetails = new CustomUserDetails(user);
//            Authentication authentication = new UsernamePasswordAuthenticationToken(
//                    customUserDetails, null, customUserDetails.getAuthorities()
//            );
//
//            // SecurityContext에 인증 객체 설정
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            log.info("Authenticated user: {}", username);
//        } catch (Exception e) {
//            log.error("JWT token validation failed: {}", e.getMessage());
//        }
//
//        return message;
//    }
}
