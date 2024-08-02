package T2F2.SPOT.domain.post.repository;

import T2F2.SPOT.domain.post.dto.QPostDto;
import T2F2.SPOT.domain.post.dto.SearchPostConditionDto;
import T2F2.SPOT.domain.post.dto.responsePostDto;
import T2F2.SPOT.domain.post.entity.QPost;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.util.List;

public interface PostRepositoryCustom {
    Slice<QPostDto> searchPosts(
            Pageable pageable,
            SearchPostConditionDto searchPostConditionDto
    );
}
