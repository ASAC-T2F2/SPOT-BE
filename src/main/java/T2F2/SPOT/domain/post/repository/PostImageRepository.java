package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostImageRepository extends JpaRepository<PostImage, Long> {
}
