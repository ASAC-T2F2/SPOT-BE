package T2F2.SPOT.domain.email.service;

import T2F2.SPOT.domain.email.dto.EmailDto;

public interface EmailService {

    String createCode();

    void sendEmail(String email);

    EmailDto createEmailForm(String email);

    Boolean verifyCode(String email, String code);
}
