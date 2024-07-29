package T2F2.SPOT.domain.user.repository;

import T2F2.SPOT.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
