package T2F2.SPOT.domain.user.jwt;

import T2F2.SPOT.domain.user.Role;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
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
        log.info("JWT Filter----------------------------");
        String accessToken = request.getHeader("Authorization");
        log.info("Received Access token: {}", accessToken);

        if (accessToken != null && accessToken.startsWith("Bearer ")) {
            accessToken = accessToken.substring(7);
        } else {
            log.info("No token or invalid token format. Proceeding to next filter.");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Checking Access token expired----------------");
        try {
            jwtUtil.isExpired(accessToken);
            log.info("Token is valid.");
        } catch (ExpiredJwtException e) {
            log.info("Token expired: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("access token expired");
            response.getWriter().close();
            return;
        }

        log.info("Checking Access token category--------------------");
        String category = jwtUtil.getCategory(accessToken);

        if (!category.equals("access")) {
            log.info("Invalid token category: {}", category);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().println("token's category is not access: " + category);
            response.getWriter().close();
            return;
        }

        log.info("Checking Access token user, role--------------------");
        String username = jwtUtil.getUsername(accessToken);
        Role role = Role.valueOf(jwtUtil.getRole(accessToken));
        log.info("Username: {}", username);
        log.info("Role: {}", role);

        User user = new User();
        user.setEmail(username);
        user.setRole(role);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        log.info("Setting authentication: {}", authToken);
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("Security context set: {}", SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(request, response);

    }
}
