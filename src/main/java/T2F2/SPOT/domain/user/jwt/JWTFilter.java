package T2F2.SPOT.domain.user.jwt;

import T2F2.SPOT.domain.user.Role;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {
    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 프론트 측에서 보낸 요청의 헤더에서 access 키에 담긴 토큰 추출
        String accessToken = request.getHeader("access");
        log.info("Access token: {}", accessToken);

        // 토큰이 없는 경우 처리 (다음 필터로 이동)
        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        } else {
            filterChain.doFilter(request, response);
            return;
        }

        // 토큰 만료 여부 확인, 만료 시 다음 필터로 넘기지 않는다.
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            // response status code : 프론트와 협의 된 코드
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // response body : 프론트와 협의 된 응답 메시지
            PrintWriter writer = response.getWriter();
            writer.println("access token expired");
            writer.close();

            return;
        }

        // 토큰 카테고리 확인 (발급 시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {

            // response body : 프론트와 협의 된 코드
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // response body : 프론트와 협의 된 응답 메시지
            PrintWriter writer = response.getWriter();
            writer.println("token's category is not access: " + category);
            writer.close();

            return;
        }

        // username, role 값 획득
        String username = jwtUtil.getUsername(accessToken);
        Role role = Role.valueOf(jwtUtil.getRole(accessToken));

        User user = new User();
        user.setEmail(username);
        user.setRole(role);
        CustomUserDetails cussUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(cussUserDetails, null, cussUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
