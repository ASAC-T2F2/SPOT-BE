package T2F2.SPOT.domain.user.controller;

import T2F2.SPOT.domain.user.exception.TokenException;
import T2F2.SPOT.domain.user.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/token")
@Slf4j
public class TokenController {

    private final TokenService tokenService;

    public TokenController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissueToken(HttpServletRequest request, HttpServletResponse response) {

        try {
            String newAccessToken = tokenService.reissueToken(request, response);
            return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
        } catch (TokenException.RefreshTokenIsNullException | TokenException.RefreshTokenExpiredException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
