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
        try {
            emailService.sendEmail(emailDto.getEmail());
            return ResponseEntity.ok("Successfully sent");
        } catch (UserExceptions.EmailAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/verification")
    public ResponseEntity<String> verificationEmail(@Validated @RequestBody EmailDto emailDto) {
        try {
            emailService.verifyCode(emailDto.getEmail(), emailDto.getVerifyCode());
            return ResponseEntity.ok("Successfully verification");
        } catch (EmailException.InvalidVerificationCodeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification code");
        } catch (EmailException.ExpiredVerificationCodeException e) {
            return ResponseEntity.status(HttpStatus.GONE).body("Expired verification code");
        }

    }
}
