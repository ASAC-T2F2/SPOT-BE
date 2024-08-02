package T2F2.SPOT.domain.post.service;

import T2F2.SPOT.domain.category.entity.Category;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.SortBy;
import T2F2.SPOT.domain.post.dto.*;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.repository.PostRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void createPost(CreatePostDto createPostDto) {
        User findUser = userRepository.findById(createPostDto.getUserId()).orElseThrow();
        Post result = postRepository.save(Post.of(createPostDto, findUser));
    }

    @Transactional(readOnly = true)
    public List<responsePostDto> findAllPost() {
        return postRepository.findAll()
                .stream()
                .map(post ->
                        post.getIsDeleted() ? null : responsePostDto.of(post))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public responsePostDto findPostById(Long id) {
        Post reuslt = postRepository.findById(id).orElseThrow();
        return responsePostDto.of(reuslt);
    }

    @Transactional(readOnly = true)
    public Slice<QPostDto> getSearchFilterList(
            String keyword,
            Category category,
            PostFor postFor,
            PostStatus postStatus,
            String minPrice,
            String maxPrice,
            SortBy sortBy,
            int startIndex
            ) {
        Pageable pageable = PageRequest.of(startIndex, 10);
        SearchPostConditionDto condition = SearchPostConditionDto.of(keyword, category, postFor, postStatus, minPrice, maxPrice, sortBy);
        log.info("Keyword : {}, Category : {}, PostFor : {}, PostStatus : {}, price : {} ~ {}, Sort : {} ", condition.getKeyword(), condition.getCategory(),
                condition.getPostFor(), condition.getPostStatus(), condition.getMinPrice(), condition.getMaxPrice(), condition.getSortBy()
        );
        return postRepository.searchPosts(
                pageable,
                condition
        );
    }

    public void updateStatus(Long id, String status) {
        Post findPost = postRepository.findById(id).orElseThrow();
        if(findPost.getIsDeleted())
        {
            throw new RuntimeException("이미 삭제된 게시글");
        }
        findPost.updatePostStatus(status);
    }

    public void modifyPost(Long id, ModifyPostDto modifyPostDto) {
        Post findPost = postRepository.findById(id).orElseThrow();
        if(findPost.getIsDeleted())
        {
            throw new RuntimeException("이미 삭제된 게시글");
        }
        findPost.modifyPost(modifyPostDto);
    }
}
