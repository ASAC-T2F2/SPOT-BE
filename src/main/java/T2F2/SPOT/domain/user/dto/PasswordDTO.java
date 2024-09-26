package T2F2.SPOT.domain.user.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class PasswordDTO {

    private String email;
    private String code;
    private String newPassword;

}
