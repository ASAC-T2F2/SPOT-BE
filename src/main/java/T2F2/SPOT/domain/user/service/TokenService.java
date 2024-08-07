package T2F2.SPOT.domain.user.service;

import T2F2.SPOT.domain.user.exception.TokenException;
import T2F2.SPOT.domain.user.jwt.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TokenService {

    private final JWTUtil jwtUtil;

    public TokenService(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Access 토큰 재발급
     * @param request
     * @param response
     * @return 새로운 Access 토큰
     */
    public String reissueToken(HttpServletRequest request, HttpServletResponse response) {

        String refresh = getRefreshTokenFromCookie(request.getCookies());
        log.info("[Reissue Service] - Received refresh token: {}", refresh);

        if (refresh == null) {
            // response status code : 프론트와 협업한 상태코드
            throw new TokenException.RefreshTokenIsNullException("Refresh token is null");
        }

        // 토큰 만료 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            throw new TokenException.RefreshTokenExpiredException("Refresh token is expired");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = String.valueOf(jwtUtil.getRole(refresh));

        log.info("[Reissue Service] - username: {}, role: {}", username, role);

        // 새로운 JWT 생성
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        log.info("[Reissue Service] - newAccess: {}", newAccess);

        // response
        response.setHeader("access", newAccess);

        // 토큰을 굳이 보낼 이유는 없다. 후에 고민
        return newAccess;
    }

    /**
     * 요청의 Cookie에서 refresh 토큰 추출
     * @param cookies
     * @return
     */
    private String getRefreshTokenFromCookie(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
