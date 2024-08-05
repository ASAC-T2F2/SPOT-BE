package T2F2.SPOT.domain.email.controller;

import T2F2.SPOT.domain.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/verification-request")
    public ResponseEntity sendMessage(@RequestParam("email") String email) {
        emailService.sendEmail(email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/verification")
    public ResponseEntity verificationEmail(@RequestParam("email") String email,
                                            @RequestParam("code") String code) {
        emailService.verifyCode(email, code);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
