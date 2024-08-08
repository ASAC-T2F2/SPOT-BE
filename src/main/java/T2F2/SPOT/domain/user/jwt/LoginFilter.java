package T2F2.SPOT.domain.user.jwt;

import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.dto.LoginRequest;
import T2F2.SPOT.domain.user.entity.RefreshToken;
import T2F2.SPOT.domain.user.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final RefreshTokenRepository refreshTokenRepository;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, ObjectMapper objectMapper, RefreshTokenRepository refreshTokenRepository) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    /**
     * 사용자 아이디와 비밀번호를 받아 인증
     * @param request
     * @param response
     * @return 인증
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        try {
            // JSON 데이터에서 사용자 인증 정보를 추출
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);
            String username = loginRequest.getUsername();
            String password = loginRequest.getPassword();

            log.info("Attempting to authenticate user: {}", username);

            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

            // 인증 요청 처리
            return authenticationManager.authenticate(authToken);

        } catch (IOException e) {
            throw new AuthenticationException("Failed to parse authentication request body", e) {};
        }
    }

    /**
     * 인증 성공 시
     * @param request
     * @param response
     * @param chain
     * @param authentication
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        // 유저 정보
        String username = authentication.getName();
        log.info("Successfully authenticated user: {}", username);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> authoritiesIterator = authorities.iterator();
        GrantedAuthority authority = authoritiesIterator.next();
        String role = authority.getAuthority();
        log.info("{} has role: {}", username, role);

        // 토큰 생성
        String accessToken = jwtUtil.createJwt("access", username, role, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // Refresh 토큰 저장
        addRefreshToken(username, refreshToken, 86400000L);

        log.info("[LoginFilter] - AccessToken: {}", accessToken);
        log.info("[LoginFilter] - RefreshToken: {}", refreshToken);

        // 응답 설정
        response.setHeader("access", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
        response.setStatus(HttpStatus.OK.value());
    }

    /**
     * 인증 실패 시
     * @param request
     * @param response
     * @param failed
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        log.debug("Unsuccessful authentication");
        response.setStatus(401);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*64*80);

        // https 적용 시 활성화
        //cookie.setSecure(true);

        // 쿠키 적용 범위 설정 가능
        // cookie.setPath("/");

        // JS 접근 차단
        cookie.setHttpOnly(true);

        return cookie;

    }

    private void addRefreshToken(String username, String refreshToken, Long expiredMs) {

        Date expiration = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refresh = new RefreshToken();
        refresh.setUsername(username);
        refresh.setRefreshToken(refreshToken);
        refresh.setExpiration(expiration.toString());

        refreshTokenRepository.save(refresh);
    }
}
