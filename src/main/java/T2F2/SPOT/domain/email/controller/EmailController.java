package T2F2.SPOT.domain.email.controller;

import T2F2.SPOT.domain.email.dto.EmailDto;
import T2F2.SPOT.domain.email.exception.EmailException;
import T2F2.SPOT.domain.email.service.EmailService;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/verification-request")
    public ResponseEntity<String> sendMessage(@Validated @RequestBody EmailDto emailDto) {
        emailService.sendEmail(emailDto.getEmail());
        return ResponseEntity.ok("이메일 전송 성공");
    }

    @PostMapping("/verification")
    public ResponseEntity<String> verificationEmail(@Validated @RequestBody EmailDto emailDto) {
        emailService.verifyCode(emailDto.getEmail(), emailDto.getVerifyCode());
        return ResponseEntity.ok("이메일 인증 성공");
    }
}
