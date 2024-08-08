package T2F2.SPOT.domain.user.dto;

import T2F2.SPOT.domain.user.Role;
import T2F2.SPOT.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JoinDTO {

    private String email;
    private String password;
    private String nickname;
    private String university;
    private String major;
    private String entranceYear;
    private String imageUrl;

    public static User toUser(JoinDTO joinDTO, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return User.builder()
                .email(joinDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(joinDTO.getPassword()))
                .nickname(joinDTO.getNickname())
                .university(joinDTO.getUniversity())
                .major(joinDTO.getMajor())
                .entranceYear(joinDTO.getEntranceYear())
                .imageUrl(joinDTO.getImageUrl())
                .isDeleted(false)
                .role(Role.USER)
                .build();
    }
}
