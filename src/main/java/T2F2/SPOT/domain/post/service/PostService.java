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
import T2F2.SPOT.util.exception.CustomException;
import T2F2.SPOT.util.exception.error_code.PostErrorCode;
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

    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new CustomException(PostErrorCode.NOT_FOUND));
    }

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
        Post findPost = postRepository.findById(id).orElseThrow();
        if(findPost.getIsDeleted())
        {
            throw new CustomException(PostErrorCode.ALREADY_DELETED);
        }
        return responsePostDto.of(findPost);
    }

    @Transactional(readOnly = true)
    public Slice<QPostDto> getSearchFilterList(
            String keyword,
            Category category,
            PostFor postFor,
            PostStatus postStatus,
            int minPrice,
            int maxPrice,
            SortBy sortBy,
            int startIndex
            ) {
        Pageable pageable = PageRequest.of(startIndex, 10);
        SearchPostConditionDto condition = SearchPostConditionDto.of(keyword, category, postFor, postStatus, minPrice, maxPrice, sortBy);
        log.info("Keyword : {}, Category : {}, PostFor : {}, PostStatus : {}, price : {} ~ {}, Sort : {}, StartIndex : {} ", condition.getKeyword(), condition.getCategory(),
                condition.getPostFor(), condition.getPostStatus(), condition.getMinPrice(), condition.getMaxPrice(), condition.getSortBy(), startIndex
        );
        return postRepository.searchPosts(
                pageable,
                condition
        );
    }
    @Transactional(readOnly = true)
    public List<QPostDto> findPostByMajor(String major) {
        return postRepository.findByMajor(major)
                .stream()
                .map(post ->
                        post.getIsDeleted() ? null : QPostDto.of(post))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<QPostDto> findPostByUserId(Long userId) {
        return postRepository.findByUserId(userId)
                .stream()
                .map(post ->
                        post.getIsDeleted() ? null : QPostDto.of(post))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    public void updateStatus(Long id, String status) {
        Post findPost = postRepository.findById(id).orElseThrow();
        if(findPost.getIsDeleted())
        {
            throw new CustomException(PostErrorCode.ALREADY_DELETED);
        }
        findPost.updatePostStatus(status);
    }

    public void modifyPost(Long id, ModifyPostDto modifyPostDto) {
        Post findPost = postRepository.findById(id).orElseThrow();
        if(findPost.getIsDeleted())
        {
            throw new CustomException(PostErrorCode.ALREADY_DELETED);
        }
        findPost.modifyPost(modifyPostDto);
    }
}