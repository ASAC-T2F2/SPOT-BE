package T2F2.SPOT.domain.review.repository;

import T2F2.SPOT.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


}
