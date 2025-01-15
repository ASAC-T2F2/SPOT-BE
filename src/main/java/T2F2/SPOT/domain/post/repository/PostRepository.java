package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Modifying
    @Query(value = "UPDATE post SET view_count = view_count + 1 WHERE post_id = :postId", nativeQuery = true)
    void increaseViewCount(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.postImages")
    List<Post> findAllWithImages();
}