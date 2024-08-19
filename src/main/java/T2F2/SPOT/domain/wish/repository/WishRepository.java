package T2F2.SPOT.domain.wish.repository;

import T2F2.SPOT.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Integer> {

    Optional<Wish> findById(Long id);
}

