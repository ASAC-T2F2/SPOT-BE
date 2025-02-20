package T2F2.SPOT.domain.post.service;

import T2F2.SPOT.domain.post.Category;
import T2F2.SPOT.domain.post.PostFor;
import T2F2.SPOT.domain.post.PostStatus;
import T2F2.SPOT.domain.post.SortBy;
import T2F2.SPOT.domain.post.dto.*;
import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.entity.PostImage;
import T2F2.SPOT.domain.post.repository.PostImageRepository;
import T2F2.SPOT.domain.post.repository.PostRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import T2F2.SPOT.domain.user.service.AuthService;
import T2F2.SPOT.util.exception.CustomException;
import T2F2.SPOT.util.exception.error_code.PostErrorCode;
import T2F2.SPOT.util.exception.error_code.UserErrorCode;
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
    private final AuthService authService;
    private final PostImageRepository postImageRepository;

    /**
     * PostId 기반 게시글 가져오기
     * @param id
     * @return 단일 게시글
     */
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new CustomException(PostErrorCode.NOT_FOUND));
    }


    /**
     * 게시글 생성 로직
     * @param createPostDto
     */
    public Long  createPost(CreatePostDto createPostDto) {
        User findUser = userRepository.findById(authService.getAuthenticatedUserId())
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND));
        Post post = postRepository.save(Post.of(createPostDto, findUser));

        createPostDto.getImageUrls().forEach(url -> {
            PostImage postImage = PostImage.builder()
                    .post(post)
                    .imageUrl(url)
                    .build();
            post.addPostImage(postImage);
        });

        postRepository.save(post);
        return post.getId();
    }


    /**
     * 모든 게시글 목록 반환
     * @return 모든 게시글
     */
    @Transactional(readOnly = true)
    public List<PostResponse> findAllPost() {

        List<Post> posts = postRepository.findAllWithImages();

        return posts.stream()
                .map(post ->
                        post.getIsDeleted() ? null : PostResponse.of(post, post.getFirstImageOrDefault()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    /**
     * 살래요/ 팔래요 게시글 목록 반환.
     * 무한 스크롤 방식
     * @param limit
     * @param lastPostId
     * @param postFor
     * @return 목적에 따른 게시글 목록
     */
    @Transactional(readOnly = true)
    public PostListWithPagination findPostsForPurpose(int limit, Long lastPostId, PostFor postFor) {
        List<Post> fetchedPosts = postRepository.fetchPostsForPurposeSorted(limit, lastPostId, postFor);

        List<PostResponse> postResponses = fetchedPosts.stream()
                .map(post -> {
                    String firstImageUrl = post.getPostImages().isEmpty() ? "default" : post.getPostImages().get(0).getImageUrl();
                    return PostResponse.of(post, firstImageUrl);
                })
                .collect(Collectors.toList());

        Long lastId = fetchedPosts.isEmpty() ? null : fetchedPosts.get(fetchedPosts.size() - 1).getId();
        boolean hasMore = postRepository.hasMorePosts(lastId, postFor);

        return PostListWithPagination.of(postResponses, lastId, hasMore);
    }


    /**
     * 상품 상세정보 반환
     * @param id
     * @return 내가 작성한 게시글인지 확인 된 상품 상세정보
     */
    @Transactional(readOnly = true)
    public PostResponse getPostDetail(Long id) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(PostErrorCode.NOT_FOUND));

        if(findPost.getIsDeleted())
        {
            throw new CustomException(PostErrorCode.ALREADY_DELETED);
        }

        boolean isAuthor = isAuthor(findPost.getUser().getId());
        List<PostImage> images = findPost.getPostImages();
        return PostDetail.of(findPost, isAuthor, images);
    }


    /**
     * 검색필터를 통한 게시글 반환
     * @param keyword
     * @param category
     * @param postFor
     * @param postStatus
     * @param minPrice
     * @param maxPrice
     * @param sortBy
     * @param startIndex
     * @return 필터 적용된 게시글
     */
    @Transactional(readOnly = true)
    public Slice<PostPreviewResponse> getSearchFilterList(
            int limit,
            int startIndex,
            String keyword,
            Category category,
            PostFor postFor,
            PostStatus postStatus,
            int minPrice,
            int maxPrice,
            SortBy sortBy
            ) {
        Pageable pageable = PageRequest.of(startIndex, limit);
        SearchPostConditionDto condition = SearchPostConditionDto.of(keyword, category, postFor, postStatus, minPrice, maxPrice, sortBy);

        return postRepository.searchPosts(
                pageable,
                condition
        );
    }


    /**
     * 전공에 맞는 게시글 반환
     * @return 내 전공 게시글
     */
    @Transactional(readOnly = true)
    public Slice<PostPreviewResponse> getPostByMajor(int limit, int startIndex, SortBy sortBy) {
        String userEmail = authService.getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND));
        String major = user.getMajor();

        Pageable pageable = PageRequest.of(startIndex, limit);

        return postRepository.findByMajor(pageable, major, sortBy);
    }


    /**
     * 사용자 Id 기반 게시글 반환
     * @return 게시글
     */
    public List<PostPreviewResponse> findPostByUserId() {

        Long userId = authService.getAuthenticatedUserId();

        return postRepository.findByUserId(userId)
                .stream()
                .map(post ->
                        post.getIsDeleted() ? null : PostPreviewResponse.of(post))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


    /**
     * 조회수 증가 (Native Query를 총해 동시성 문제 해결)
     * @param postId
     */
    @Transactional
    public void updateViewCount(Long postId) {
        postRepository.increaseViewCount(postId);
    }


    /**
     * 게시글 상태 변경
     * @param findPost
     * @param status
     */
    public void updateStatus(Post findPost, String status) {
        if(findPost.getIsDeleted())
        {
            throw new CustomException(PostErrorCode.ALREADY_DELETED);
        }
        findPost.updatePostStatus(status);
    }


    /**
     * 게시글 정보 수정
     * @param id
     * @param modifyPostDto
     */
    public void modifyPost(Long id, ModifyPostDto modifyPostDto) {
        Post findPost = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(PostErrorCode.NOT_FOUND));

        if(!isAuthor(findPost.getUser().getId())) {
            throw new CustomException(UserErrorCode.FORBIDDEN);
        }

        if(findPost.getIsDeleted())
        {
            throw new CustomException(PostErrorCode.ALREADY_DELETED);
        }

        findPost.modifyPost(modifyPostDto);


        if(modifyPostDto.getImageUrls() != null) {
            findPost.getPostImages().clear();

            modifyPostDto.getImageUrls().forEach(url -> {
                PostImage postImage = PostImage.builder()
                        .post(findPost)
                        .imageUrl(url)
                        .build();
                findPost.addPostImage(postImage);
            });
        }

        postRepository.save(findPost);
    }

    @Transactional(readOnly = true)
    public ModifyPostDto beforePost(Long id) {

        Post beforePost = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(PostErrorCode.NOT_FOUND));

        if(beforePost.getIsDeleted())
        {
            throw new CustomException(PostErrorCode.ALREADY_DELETED);
        }

        List<String> imageUrls = beforePost.getPostImages().stream()
                .map(PostImage::getImageUrl)
                .toList();


        return new ModifyPostDto(
                beforePost.getTitle(),
                beforePost.getContent(),
                beforePost.getPostFor(),
                beforePost.getPostStatus(),
                beforePost.getPrice(),
                beforePost.getCategory(),
                imageUrls
        );

    }


    /**
     * 내가 작성한 게시글인지 확인
     * @param writerId
     * @return 참/거짓
     */
    public boolean isAuthor(Long writerId) {
        String userEmail = authService.getAuthenticatedUserEmail();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND));

        return Objects.equals(user.getId(), writerId);
    }

    /**
     * softdelete로 상태변경
     * @param postId
     */
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(PostErrorCode.NOT_FOUND));

        if(!post.getUser().getId().equals(authService.getAuthenticatedUserId())){
            throw new CustomException(UserErrorCode.FORBIDDEN);
        }

        post.updatePostStatus("DELETE");
        postRepository.save(post);
    }
}