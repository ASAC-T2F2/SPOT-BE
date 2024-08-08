package T2F2.SPOT.domain.email.dto;

import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailDto {

    // 이메일 주소
    private String mail;
    // 인증 코드
    private String verifyCode;

    private MimeMessage mimeMessage;

    private EmailDto(MimeMessage mimeMessage, String verifyCode, String mail) {
        this.mimeMessage = mimeMessage;
        this.verifyCode = verifyCode;
        this.mail = mail;
    }


    public static EmailDto of(MimeMessage mimeMessage, String verifyCode, String mail){
        return new EmailDto(mimeMessage, verifyCode, mail);
    }
}
