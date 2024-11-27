package T2F2.SPOT.domain.user.repository;

import T2F2.SPOT.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);
}
