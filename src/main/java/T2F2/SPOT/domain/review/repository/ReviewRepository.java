package T2F2.SPOT.domain.review.repository;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 커스텀이 필요한 경우 변경하여 사용
    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.sender = :sender AND r.receiver = :receiver AND r.post = :post")
    Boolean existsReviewBySenderAndReceiverAndPost(@Param("sender") User sender,
                                                   @Param("receiver") User receiver,
                                                   @Param("post") Post post);

    Boolean existsBySenderAndReceiverAndPost(User sender, User receiver, Post post);

}
