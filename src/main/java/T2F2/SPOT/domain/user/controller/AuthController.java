package T2F2.SPOT.domain.user.controller;

import T2F2.SPOT.domain.user.dto.JoinDTO;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDTO joinDTO) {

        try {
            authService.signUp(joinDTO);
            return ResponseEntity.ok("Successfully joined");
        } catch (UserExceptions.EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (UserExceptions.UserSignUpException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
