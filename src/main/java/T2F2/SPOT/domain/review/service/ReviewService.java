package T2F2.SPOT.domain.review.service;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.exception.PostException;
import T2F2.SPOT.domain.post.repository.PostRepository;
import T2F2.SPOT.domain.review.dto.CreateReviewRequest;
import T2F2.SPOT.domain.review.dto.ReviewResponse;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.review.exception.ReviewException;
import T2F2.SPOT.domain.review.repository.ReviewRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReviewService {


    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(UserRepository userRepository, PostRepository postRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public ReviewResponse createReview(String username, CreateReviewRequest createReviewRequest) {

        User sender = userRepository.findByEmail(username);
        User receiver = userRepository.findById(createReviewRequest.getReceiverId())
                .orElseThrow(() -> new UserExceptions.UserNotFoundException("리뷰 대상을 찾을 수 없습니다."));
        log.info("리뷰 작성자 : {}", sender.getId());
        log.info("리뷰 대상 : {}", receiver.getId());

        Post targetPost = postRepository.findById(createReviewRequest.getPostId())
                .orElseThrow(() -> new PostException.PostNotFoundException("리뷰 대상 게시글을 찾을 수 없습니다."));
        log.info("리뷰 대상 게시글 : {}", targetPost.getId());

        checkReviewExistence(sender, receiver, targetPost);

        Review review = Review.createReview(sender, receiver, targetPost, createReviewRequest.getRate(), createReviewRequest.getMessage());

        /* 리뷰대상 (매너)점수 관련 로직 */
        float reviewRate = review.getRate();
        float updatedMannerScore = receiver.updateMannerScore(createReviewRequest.getRate());
        log.info("[Review Service]- Before updateMannerScore, reviewRate: {}", reviewRate);
        log.info("[Review Service]- After updateMannerScore, receiver's mannerScore: {}", updatedMannerScore);

        /* 리뷰대상 등급 관련 로직 */


        ReviewResponse reviewResponse = ReviewResponse.from(review);
        reviewRepository.save(review);

        return reviewResponse;
    }

    /**
     * 이미 리뷰를 작성했는지 검증
     * @param sender
     * @param receiver
     * @param targetPost
     */
    private void checkReviewExistence(User sender, User receiver, Post targetPost) {
        if (reviewRepository.existsBySenderAndReceiverAndPost(sender, receiver, targetPost)) {
            throw new ReviewException.ReviewAlreadyExist("이미 리뷰를 작성했습니다. \nsender: " + sender.getId() + ", receiver: " + receiver.getId() + ", post: " + targetPost.getId());
        }
    }
}
