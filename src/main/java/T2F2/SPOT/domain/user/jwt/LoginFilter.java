package T2F2.SPOT.domain.user.jwt;

import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
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

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("Attempting to authenticate user: {}", username);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        return authenticationManager.authenticate(authToken);
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

        log.info("LoginFilter AccessToken: {}", accessToken);
        log.info("LoginFilter RefreshToken: {}", refreshToken);

        // 응답 설정
        response.setHeader("access", accessToken);
        response.addCookie(createCookie("refresh", refreshToken));
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
}
