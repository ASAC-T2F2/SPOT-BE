package T2F2.SPOT.domain.user.controller;

import T2F2.SPOT.domain.user.dto.JoinDTO;
import T2F2.SPOT.domain.user.dto.PasswordDTO;
import T2F2.SPOT.domain.user.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinDTO joinDTO) {

        authService.signUp(joinDTO);
        return ResponseEntity.ok("회원가입 성공");
    }

    @PostMapping("/nicknameCheck")
    public ResponseEntity<String> nicknameCheck(@RequestBody Map<String, String> requestBody) {

        String nickname = requestBody.get("nickname");
        authService.nicknameCheck(nickname);
        return ResponseEntity.ok(nickname + "은(는) 사용가능한 닉네임입니다.");
    }

    @PostMapping("/findPassword")
    public ResponseEntity<String> findPassword(@RequestBody Map<String, String> requestBody) {

        String email = requestBody.get("email");

        authService.sendNewPasswordCode(email);
        requestBody.remove("email");

        return ResponseEntity.ok("비밀번호 변경코드 전송 완료");
    }

    @PostMapping("/verification")
    public ResponseEntity<String> verification(@RequestBody PasswordDTO passwordDTO) {

        authService.verificationCode(passwordDTO.getEmail(), passwordDTO.getCode());
        return ResponseEntity.ok("이메일 인증 성공");
    }

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody PasswordDTO passwordDTO) {

        authService.changePassword(passwordDTO);
        return ResponseEntity.ok("비밀번호 변경 성공");
    }
}
