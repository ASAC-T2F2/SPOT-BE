package T2F2.SPOT.domain.email.repository;

import T2F2.SPOT.domain.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailRepository extends JpaRepository<Email, Long> {
    //인증코드 발송한 이메일 주소 조회
    Optional<Email> findByEmail(String email);
}

