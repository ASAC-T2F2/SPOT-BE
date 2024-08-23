package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.dto.QPostDto;
import T2F2.SPOT.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}