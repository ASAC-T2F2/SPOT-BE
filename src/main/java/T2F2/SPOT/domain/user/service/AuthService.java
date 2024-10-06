package T2F2.SPOT.domain.user.service;

import T2F2.SPOT.domain.email.service.EmailService;
import T2F2.SPOT.domain.user.dto.JoinDTO;
import T2F2.SPOT.domain.user.dto.PasswordDTO;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.*;

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
     * 회원가입
     * @param joinDTO
     * @return 회원가입 성공여부
     */
    @Transactional
    public Boolean signUp(JoinDTO joinDTO) {
        String nickname = joinDTO.getNickname();

        Boolean isExistNickname = userRepository.existsByNickname(nickname);

        if(isExistNickname){
            throw new UserExceptions.NicknameAlreadyExistsException("Nickname(" + nickname + ") already exists");
        }

        try {
            User newUser = JoinDTO.toUser(joinDTO, bCryptPasswordEncoder);
            userRepository.save(newUser);
            return true;
        } catch (Exception e) {
            throw new UserExceptions.SignUpFailedException("Error while signing up");
        }
    }

    public boolean nicknameCheck(String nickname) {
        Boolean isExistNickname = userRepository.existsByNickname(nickname);

        if(isExistNickname){
            throw new UserExceptions.NicknameAlreadyExistsException("Nickname(" + nickname + ") already exists");
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
            throw new UserExceptions.UserNotFoundException(email + " user not found");
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
            throw new IllegalArgumentException(code + " is not correct");
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
            throw new UserExceptions.UserNotFoundException(email + " User not found");
        }

        if(bCryptPasswordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException("기존 비밀번호와 동일합니다.");
        }

        user.changePassword(newPassword, bCryptPasswordEncoder);
        userRepository.save(user);

        verificationCodes.remove(email);
    }

    public User findByNickname(String nickname) {
        return userRepository.findByNickname(nickname).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
