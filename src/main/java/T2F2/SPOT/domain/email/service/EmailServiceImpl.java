package T2F2.SPOT.domain.email.service;

import T2F2.SPOT.domain.email.dto.EmailDto;
import T2F2.SPOT.domain.email.entity.Email;
import T2F2.SPOT.domain.email.repository.EmailRepository;
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
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final EmailRepository emailRepository;
    private static final String sendEmail = "developodol@gmail.com";

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
        Optional<Email> findEmail = emailRepository.findByEmail(email);
        if(findEmail.isPresent()) {
            emailRepository.delete(findEmail.get());
        }
        EmailDto emailForm = createEmailForm(email);
        try {
            mailSender.send(emailForm.getMimeMessage());
            saveEmailCode(emailForm);
        } catch (RuntimeException e) {
            log.debug("MailService.sendMail exception occur email: {}, ", email);
            throw new RuntimeException();
        }
    }

    // 이메일과 인증코드 저장하는 메서드
    private void saveEmailCode(EmailDto emailDto) {
        Email email = Email.builder()
                .email(emailDto.getMail())
                .verifyCode(emailDto.getVerifyCode())
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
            String body = "";
            body += "<h3>" + "요청하신 인증 코드입니다." + "</h3>";
            body += "<h1>" + code + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return EmailDto.of(message, code, email);
    }

    /**
     * 인증코드를 검증하는 메서드
     * 저장 후 3분내로 검증요청
     * @param mail(검증시도하는 메일)
     * @param code(검증시도하는 인증코드)
     * @return
     */
    @Override
    public String verifyCode(String mail, String code) {
        Email email = emailRepository.findByEmail(mail).orElseThrow();
        LocalDateTime vaildTime = email.getCreatedDate().plusMinutes(3);
        if(email.getVerifyCode().equals(code) && LocalDateTime.now().isBefore(vaildTime)) {
            log.info("인증이 완료되었습니다");
            email.modifyEmailStatus(email.getVerifyCode().equals(code));
            return "인증이 완료되었습니다";
        } else {
            if(LocalDateTime.now().isAfter(vaildTime)) {
                log.info("인증시간 만료");
            }
            log.info("인증이 실패하셨습니다");
            return "인증에 실패하셨습니다";
        }
    }
}
