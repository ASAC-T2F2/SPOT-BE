package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.dto.PostResponse;
import T2F2.SPOT.domain.post.dto.QPostDto;
import T2F2.SPOT.domain.post.dto.SearchPostConditionDto;
import T2F2.SPOT.domain.post.entity.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostRepositoryCustom {
    Slice<QPostDto> searchPosts(
            Pageable pageable,
            SearchPostConditionDto searchPostConditionDto
    );
    List<Post> findByMajor(String major);

    List<Post> findByUserId(Long userId);

    List<Post> fetchPostsForPurposeSorted(int limit, Long lastPostId, PostFor postFor);

    boolean hasMorePosts(Long lastPostId, PostFor postFor);
}