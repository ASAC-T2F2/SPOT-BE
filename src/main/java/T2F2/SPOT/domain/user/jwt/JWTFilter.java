package T2F2.SPOT.domain.user.jwt;

import T2F2.SPOT.domain.user.Role;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");
        log.info("JWT Token 1: {}", authorization);

        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            log.info("Token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        String token = authorization.split(" ")[1];
        log.info("JWT Token 2: {}", token);

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }


        String username = jwtUtil.getUsername(token);
        String role = String.valueOf(jwtUtil.getRole(token));
        log.info("username {}", username);
        log.info("role {}", role);

        User user = new User();
        user.setEmail(username);
        user.setPassword("temppassword");

        // Role 열거형 값 처리
        Role userRole;
        try {
            userRole = Role.valueOf(role);
            log.info("[doFilterInternal] userRole {}", userRole);
        } catch (IllegalArgumentException e) {
            // 열거형 값이 적절하지 않은 경우 기본값으로 설정하거나 예외 처리
            userRole = Role.USER; // 예를 들어, 기본값 설정
            // 로깅 등 필요한 예외 처리 로직 추가 가능
        }
        user.setRole(userRole);

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
