package T2F2.SPOT.domain.wish.repository;

import T2F2.SPOT.domain.wish.entity.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishRepository extends JpaRepository<Wish, Integer>, CustomWishRepository {

    @Query("SELECT w FROM Wish w WHERE w.post.id = :postId AND w.user.email = :userEmail")
    Optional<Wish> findByPostIdAndUserEmail(@Param("postId") Long postId, @Param("userEmail") String userEmail);

    @Query("SELECT w FROM Wish w JOIN FETCH w.post WHERE w.user.email = :userEmail")
    Optional<List<Wish>> findAllByUserEmailWithPost(@Param("userEmail") String userEmail);
}

