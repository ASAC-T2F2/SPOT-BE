package T2F2.SPOT.domain.email.service;

import T2F2.SPOT.domain.email.dto.EmailDto;
import T2F2.SPOT.domain.email.entity.Email;
import T2F2.SPOT.domain.email.exception.EmailException;
import T2F2.SPOT.domain.email.repository.EmailRepository;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;
    private final EmailRepository emailRepository;
    private static final String sendEmail = "developodol@gmail.com";
    private final UserRepository userRepository;

    //랜덤한 6자리의 문자열 코드 생성
    private String createCode() {
        int leftLimit = 48; // number '0'
        int rightLimit = 122; // alphabet 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new,
                        StringBuilder::appendCodePoint,
                        StringBuilder::append)
                .toString();
    }

    /**
     * 이메일 전송하는 메서드
     * @param email(전송받을 이메일)
     */
    @Override
    public void sendEmail(String email) {
        Boolean isExistUser = userRepository.existsByEmail(email);

        if (isExistUser) {
            throw new UserExceptions.EmailAlreadyExistsException("Email(" + email + ") already exists");
        } else {
            Optional<Email> findEmail = emailRepository.findByEmail(email);
            if(findEmail.isPresent()) {
                emailRepository.delete(findEmail.get());
            }
            EmailDto emailForm = createEmailForm(email);
            try {
                mailSender.send(emailForm.getMimeMessage());
                saveEmailCode(emailForm);
            } catch (RuntimeException e) {
                log.error("Failed to send email to {}: {}", email, e.getMessage());
                throw new RuntimeException("Failed to send email", e);
            }
        }
    }

    // 이메일과 인증코드 저장하는 메서드
    private void saveEmailCode(EmailDto emailDto) {
        Email email = Email.builder()
                .email(emailDto.getEmail())
                .verifyCode(emailDto.getVerifyCode())
                .emailStatus(false)
                .build();
        emailRepository.save(email);
    }

    // 랜덤 코드 생성 후 이메일 폼 작성하는 메서드
    @Override
    public EmailDto createEmailForm(String email) {
        String code = createCode();
        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(sendEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("SPOT 이메일 인증");
            String body = "<div style='font-family: Arial, sans-serif; text-align: center; padding: 20px;'>";
            body += "<h3 style='color: #333;'>요청하신 인증 코드입니다</h3>";
            body += "<div style='display: inline-block; padding: 10px 20px; border: 2px solid #4CAF50; border-radius: 5px;'>";
            body += "<h1 style='margin: 0; color: #4CAF50; font-size: 24px;'>" + code + "</h1>";
            body += "</div>";
            body += "<h3 style='color: #333; margin-top: 20px;'>감사합니다.</h3>";
            body += "</div>";

            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return EmailDto.of(message, code, email);
    }

    /**
     * 인증코드를 검증하는 메서드
     * 저장 후 3분내로 검증요청
     * @param email(검증시도하는 메일)
     * @param code(검증시도하는 인증코드)
     * @return
     */
    @Override
    public Boolean verifyCode(String email, String code) {
        try {
            Email mail = emailRepository.findByEmail(email).orElseThrow(() ->
                    new IllegalArgumentException("Email(" + email + ") not found"));

            LocalDateTime validTime = mail.getCreatedDate().plusMinutes(1);

            // 인증 시간 만료 검사
            if (LocalDateTime.now().isAfter(validTime)) {
                log.info("인증시간 만료");
                throw new EmailException.ExpiredVerificationCodeException("Verification time expired");
            }

            // 인증 코드 일치 검사
            if (mail.getVerifyCode().equals(code)) {
                log.info("인증 완료");
                mail.modifyEmailStatus(true);
                emailRepository.save(mail);
                return true;
            } else {
                log.info("인증에 실패하셨습니다");
                throw new EmailException.InvalidVerificationCodeException("Invalid verification code");
            }
        } catch (EmailException.ExpiredVerificationCodeException e) {
            log.error("인증 시간 만료: {}", e.getMessage());
            throw e;
        } catch (EmailException.InvalidVerificationCodeException e) {
            log.error("잘못된 인증 코드: {}", e.getMessage());
            throw e; // 필요 시 재처리
        }
//        Email mail = emailRepository.findByEmail(email).orElseThrow();
//        LocalDateTime vaildTime = mail.getCreatedDate().plusMinutes(3);
//
//        if(LocalDateTime.now().isAfter(vaildTime)) {
//            log.info("인증시간 만료");
//            throw new
//        }
//
//        if(mail.getVerifyCode().equals(code)) {
//            log.info("인증이 완료되었습니다");
//            mail.modifyEmailStatus(mail.getVerifyCode().equals(code));
//            return true;
//        } else {
//            log.info("인증에 실패하셨습니다");
//            return "Authentication failed";
//        }
    }
}
