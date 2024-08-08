package T2F2.SPOT.domain.email.service;

import T2F2.SPOT.domain.email.dto.EmailDto;

public interface EmailService {


    void sendEmail(String email);

    EmailDto createEmailForm(String email);

    String verifyCode(String mail, String code);
}
