package T2F2.SPOT.domain.email.entity;

import T2F2.SPOT.util.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Email extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_id")
    private Long id;

    private String email;

    private String verifyCode;

    private boolean emailStatus;

    @Builder
    public Email(Long id, String email, String verifyCode, boolean emailStatus) {
        this.id = id;
        this.email = email;
        this.verifyCode = verifyCode;
        this.emailStatus = emailStatus;
    }

    public void modifyEmailStatus(boolean emailStatus) {
        this.emailStatus = emailStatus;
    }
}
