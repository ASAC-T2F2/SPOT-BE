    package T2F2.SPOT.domain.wish.service;

    import T2F2.SPOT.domain.post.entity.Post;
    import T2F2.SPOT.domain.post.exception.PostException;
    import T2F2.SPOT.domain.post.repository.PostRepository;
    import T2F2.SPOT.domain.user.entity.User;
    import T2F2.SPOT.domain.user.exception.UserExceptions;
    import T2F2.SPOT.domain.user.repository.UserRepository;
    import T2F2.SPOT.domain.wish.dto.*;
    import T2F2.SPOT.domain.wish.entity.Wish;
    import T2F2.SPOT.domain.wish.exception.WishException;
    import T2F2.SPOT.domain.wish.repository.WishRepository;
    import jakarta.transaction.Transactional;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;

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
        @Transactional
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

        /**
         * 찜 취소(삭제)
         * @param cancelWishRequest
         * @return 삭제된 찜 정보(찜 Id, 대상 Id, 주체 Id)
         */
        @Transactional
        public CancelWishResponse cancelWish(CancelWishRequest cancelWishRequest) {

            Long wishId = cancelWishRequest.getWishId();

            Wish wish = wishRepository.findById(wishId)
                            .orElseThrow(() -> new WishException.WishNotFoundException("Wish Not Found: " + wishId));

            wishRepository.delete(wish);

            return CancelWishResponse.fromWish(wish);
        }


//        public List<PreviewWishResponse> findAllWish(String username) {
//
//            User user = userRepository.findByEmail(username);
//
//            if (user == null) {
//                throw new UserExceptions.UserNotFoundException("User not found: " + username);
//            }
//
//            List<Wish> wishes = wishRepository.findAllByUserId(user.getId())
//                    .orElseThrow(() -> new WishException.WishNotFoundException("Wish Not Found: " + username));
//
//            List<PreviewWishResponse> responses =
//
//            return
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
