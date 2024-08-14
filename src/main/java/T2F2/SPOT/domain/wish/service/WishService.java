    package T2F2.SPOT.domain.wish.service;

    import T2F2.SPOT.domain.post.entity.Post;
    import T2F2.SPOT.domain.post.exception.PostException;
    import T2F2.SPOT.domain.post.repository.PostRepository;
    import T2F2.SPOT.domain.user.entity.User;
    import T2F2.SPOT.domain.user.exception.UserExceptions;
    import T2F2.SPOT.domain.user.repository.UserRepository;
    import T2F2.SPOT.domain.wish.dto.AddWishRequest;
    import T2F2.SPOT.domain.wish.dto.AddWishResponse;
    import T2F2.SPOT.domain.wish.entity.Wish;
    import T2F2.SPOT.domain.wish.repository.WishRepository;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;

    @Service
    @Slf4j
    public class WishService {

        private final WishRepository wishRepository;
        private final PostRepository postRepository;
        private final UserRepository userRepository;

        public WishService(WishRepository wishRepository, PostRepository postRepository, UserRepository userRepository) {
            this.wishRepository = wishRepository;
            this.postRepository = postRepository;
            this.userRepository = userRepository;
        }

        /**
         * 찜 추가
         * @param addWishRequest
         * @return 추가 된 찜(대상 게시글 id, 주체 사용자 id)
         */
        public AddWishResponse addWish(AddWishRequest addWishRequest, String username) {

            // 사용자 확인
            User user = userRepository.findByEmail(username);
            if (user == null) {
                throw new UserExceptions.UserNotFoundException("User not found: " + username);
            }
            log.info("[AddWish] - User: {}", user.getEmail());

            // 대상 게시글 확인
            Post post = getPostById(addWishRequest.getTargetPostId());
            log.info("[AddWish] - Post: {}", post.getId());

            Wish wish = Wish.builder()
                    .user(user)
                    .post(post)
                    .build();

            wishRepository.save(wish);

            return AddWishResponse.fromPost(post, user);
        }

//        /**
//         * 현재 사용자 불러오기
//         * @return 현재 로그인 상태의 사용자
//         */
//        private User getAuthenticatedUser() {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            log.info("[WishService] - User: {}", authentication.getPrincipal());
//            log.info("[WishService] - User: {}", authentication.getPrincipal().getClass().getName());
//
//            if (authentication == null) {
//                throw new UserExceptions.UserNotFoundException("User is not authenticated");
//            }
//
//            if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
//                throw new UserExceptions.UserNotFoundException("User is not of type CustomUserDetails");
//            }
//
//            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//            String userEmail = userDetails.getUsername();
//
//            User user = userRepository.findByEmail(userEmail);
//            if (user == null) {
//                throw new UserExceptions.UserNotFoundException("User not found: " + userEmail);
//            }
//
//            return user;
//        }

        /**
         * Id 기반 게시글 불러오기
         * @param postId
         * @return 게시글
         */
        private Post getPostById(Long postId) {
            return postRepository.findById(postId)
                    .orElseThrow(() -> new PostException.PostNotFoundException("Post not found: " + postId));
        }
    }
