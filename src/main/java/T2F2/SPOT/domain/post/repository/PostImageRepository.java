package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {

    List<PostImage> findAllByPostId(Long postId);

    @Query("SELECT pi.imageUrl FROM PostImage pi WHERE pi.post.id = :postId ORDER BY pi.id ASC")
    Optional<String> findFirstImageUrlByPostId(@Param("postId") Long postId);
}
