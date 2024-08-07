package T2F2.SPOT.domain.user.service;

import T2F2.SPOT.domain.user.entity.RefreshToken;
import T2F2.SPOT.domain.user.exception.TokenException;
import T2F2.SPOT.domain.user.jwt.JWTUtil;
import T2F2.SPOT.domain.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class TokenService {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(JWTUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
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

        // 토큰 카테고리 확인
        String category = jwtUtil.getCategory(refresh);
        if (!category.equals("refresh")) {
            throw new TokenException.InvalidTokenCategory("Token's category is not refresh: " + category);
        }

        // DB에 저장되어 있는 지 확인
        Boolean refreshTokenExist = refreshTokenRepository.existsByRefreshToken(refresh);
        if (!refreshTokenExist) {
            throw new TokenException.InvalidRefreshToken("Invalid Refresh Token");
        }

        String username = jwtUtil.getUsername(refresh);
        String role = String.valueOf(jwtUtil.getRole(refresh));

        log.info("[Reissue Service] - username: {}, role: {}", username, role);

        // 새로운 JWT 생성
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);
        log.info("[Reissue Service] - newAccess: {}", newAccess);
        log.info("[Reissue Service] - newRefresh: {}", newRefresh);

        // Refresh 토큰 교체
        refreshTokenRepository.deleteByRefreshToken(refresh);
        addRefreshToken(username, newRefresh, 86400000L);

        // response
        response.setHeader("access", newAccess);
        response.addCookie(createCookie("refresh", newRefresh));

        // 토큰을 굳이 보낼 이유는 없다. 후에 고민
        return newAccess;
    }

    /**
     * Refresh 토큰 교체
     * @param username
     * @param newRefresh
     * @param expiredMs
     */
    private void addRefreshToken(String username, String newRefresh, Long expiredMs) {

        Date expirationDate = new Date(System.currentTimeMillis() + expiredMs);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsername(username);
        refreshToken.setRefreshToken(newRefresh);
        refreshToken.setExpiration(expirationDate.toString());

        refreshTokenRepository.save(refreshToken);
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

    /**
     * 쿠키 생성
     * @param key
     * @param value
     * @return 생성된 쿠키
     */
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);

//        cookie.setSecure(true);
//        cookie.setPath("/");

        return cookie;
    }
}
