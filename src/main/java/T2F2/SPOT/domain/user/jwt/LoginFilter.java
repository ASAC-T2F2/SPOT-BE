package T2F2.SPOT.domain.user.jwt;

import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.dto.LoginRequest;
import T2F2.SPOT.domain.user.entity.RefreshToken;
import T2F2.SPOT.domain.user.repository.RefreshTokenRepository;
import T2F2.SPOT.domain.user.service.CustomUserDetailsService;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserDetailsService userDetailsService;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, ObjectMapper objectMapper, RefreshTokenRepository refreshTokenRepository, UserDetailsService userDetailsService) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userDetailsService = userDetailsService;
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
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(authentication.getName());

        log.info("Successfully authenticated user: {}", userDetails.getUsername());

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        Iterator<? extends GrantedAuthority> authoritiesIterator = authorities.iterator();
        GrantedAuthority authority = authoritiesIterator.next();
        String role = authority.getAuthority();
        log.info("{} has role: {}", userDetails.getUsername(), role);

        String accessToken = jwtUtil.createJwt("access", userDetails.getUsername(), role, 600000L);
        String refreshToken = jwtUtil.createJwt("refresh", userDetails.getUsername(), role, 86400000L);

        addRefreshToken(userDetails.getUsername(), refreshToken, 86400000L);

        log.info("[LoginFilter] - AccessToken: {}", accessToken);
        log.info("[LoginFilter] - RefreshToken: {}", refreshToken);

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
