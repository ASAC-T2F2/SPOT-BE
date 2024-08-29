package T2F2.SPOT.domain.review.controller;

import T2F2.SPOT.domain.post.exception.PostException;
import T2F2.SPOT.domain.review.dto.ReviewResponse;
import T2F2.SPOT.domain.review.dto.CreateReviewRequest;
import T2F2.SPOT.domain.review.exception.ReviewException;
import T2F2.SPOT.domain.review.service.ReviewService;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/write")
    public ResponseEntity<?> write(@RequestBody CreateReviewRequest writeReviewRequest) {

        try {
            // 현재 인증된 사용자 조회
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String userEmail = userDetails.getUsername();

            ReviewResponse result = reviewService.createReview(userEmail, writeReviewRequest);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserExceptions.UserNotFoundException | PostException.PostNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ReviewException.ReviewAlreadyExist e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
