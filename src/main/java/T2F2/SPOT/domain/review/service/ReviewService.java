package T2F2.SPOT.domain.review.service;

import T2F2.SPOT.domain.post.entity.Post;
import T2F2.SPOT.domain.post.exception.PostException;
import T2F2.SPOT.domain.post.repository.PostRepository;
import T2F2.SPOT.domain.review.dto.CreateReviewRequest;
import T2F2.SPOT.domain.review.dto.ReviewResponse;
import T2F2.SPOT.domain.review.entity.Review;
import T2F2.SPOT.domain.review.repository.ReviewRepository;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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

        log.info("리뷰 작성자 : {}", sender);
        log.info("리뷰 대상 : {}", receiver);

        Post targetPost = postRepository.findById(createReviewRequest.getPostId())
                .orElseThrow(() -> new PostException.PostNotFoundException("리뷰 대상 게시글을 찾을 수 없습니다."));

        log.info("리뷰 대상 게시글 : {}", targetPost);

        Review review = Review.builder()
                        .rate(createReviewRequest.getRate())
                        .message(createReviewRequest.getMessage())
                        .sender(sender)
                        .receiver(receiver)
                        .post(targetPost)
                        .build();

        ReviewResponse reviewResponse = ReviewResponse.from(review);
        reviewRepository.save(review);

        /* 리뷰대상 (매너)점수 관련 로직 */


        /* 리뷰대상 등급 관련 로직 */

        return reviewResponse;
    }
}
