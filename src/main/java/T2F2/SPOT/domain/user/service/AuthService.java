package T2F2.SPOT.domain.user.service;

import T2F2.SPOT.domain.email.service.EmailService;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.dto.JoinDTO;
import T2F2.SPOT.domain.user.dto.PasswordDTO;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import T2F2.SPOT.util.exception.CustomException;
import T2F2.SPOT.util.exception.error_code.EmailErrorCode;
import T2F2.SPOT.util.exception.error_code.UserErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;
    private final JavaMailSender mailSender;
    private static final String sendEmail = "developodol@gmail.com";
    private Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, EmailService emailService, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailService = emailService;
        this.mailSender = mailSender;
    }

    /**
     * 현재 사용자 이메일 반환
     * @return
     */
    public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new CustomException(UserErrorCode.UNAUTHENTICATED);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    /**
     * 회원가입
     * @param joinDTO
     * @return 회원가입 성공여부
     */
    @Transactional
    public Boolean signUp(JoinDTO joinDTO) {
        String nickname = joinDTO.getNickname();

        Boolean isExistNickname = userRepository.existsByNickname(nickname);

        if(isExistNickname){
            throw new CustomException(UserErrorCode.NICKNAME_ALREADY_EXIST);
        }

        try {
            User newUser = JoinDTO.toUser(joinDTO, bCryptPasswordEncoder);
            userRepository.save(newUser);
            return true;
        } catch (Exception e) {
            throw new CustomException(UserErrorCode.SIGN_UP_FAILED);
        }
    }

    public boolean nicknameCheck(String nickname) {
        Boolean isExistNickname = userRepository.existsByNickname(nickname);

        if(isExistNickname){
            throw new CustomException(UserErrorCode.NICKNAME_ALREADY_EXIST);
        }
        return true;
    }

    /**
     * 이메일로 비밀번호 변경 인증 코드 발송
     * @param email
     */
    public void sendNewPasswordCode(String email) {

        Boolean isExistUser = userRepository.existsByEmail(email);

        if(!isExistUser) {
            throw new CustomException(UserErrorCode.NOT_FOUND);
        } else {
            String code = emailService.createCode();
            verificationCodes.put(email, code);

            scheduler.schedule(() -> verificationCodes.remove(email), 3, TimeUnit.MINUTES);
            try {
                MimeMessage message = sendNewPasswordCodeEmailForm(email, code);
                mailSender.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 비밀번호 변경 인증 코드 발송 메일 폼
     * @param email
     * @param code
     * @return
     */
    public MimeMessage sendNewPasswordCodeEmailForm(String email, String code) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            message.setFrom(sendEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("SPOT 비밀번호 찾기");
            String body = "<div style='font-family: Arial, sans-serif; text-align: center; padding: 20px;'>";
            body += "<h3 style='color: #333;'>비밀번호 변경 인증 코드입니다.</h3>";
            body += "<div style='display: inline-block; padding: 10px 20px; border: 2px solid #4CAF50; border-radius: 5px;'>";
            body += "<h1 style='margin: 0; color: #4CAF50; font-size: 24px;'>" + code + "</h1>";
            body += "</div>";
            body += "<h3 style='color: #333; margin-top: 20px;'>기존 페이지로 돌아가 입력 후 비밀번호 변경 부탁드립니다. \n 감사합니다.</h3>";
            body += "</div>";

            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return message;
    }

    /**
     *  비밀번호 변경을 위한 인증 코드 확인 로직
     * @param email
     * @param code
     */
    public void verificationCode(String email, String code) {

        String storedCode = verificationCodes.get(email);
        if(storedCode == null || !storedCode.equals(code)) {
            throw new CustomException(EmailErrorCode.INVALID_VERIFICATION_CODE);
        }
    }

    /**
     * 비밀번호 변경 로직
     * @param passwordDTO
     */
    @Transactional
    public void changePassword(PasswordDTO passwordDTO) {
        String email = passwordDTO.getEmail();
        String newPassword = passwordDTO.getNewPassword();

        User user = userRepository.findByEmail(email);

        if(user == null) {
            throw new CustomException(UserErrorCode.NOT_FOUND);
        }

        if(bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
            throw new CustomException(UserErrorCode.SAME_PASSWORD_INPUT);
        }

        user.changePassword(newPassword, bCryptPasswordEncoder);
        userRepository.save(user);

        verificationCodes.remove(email);
    }
}
