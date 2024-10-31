package T2F2.SPOT.domain.review.controller;

import T2F2.SPOT.domain.post.exception.PostException;
import T2F2.SPOT.domain.review.dto.ReviewResponse;
import T2F2.SPOT.domain.review.dto.CreateReviewRequest;
import T2F2.SPOT.domain.review.exception.ReviewException;
import T2F2.SPOT.domain.review.service.ReviewService;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/write")
    @Operation(summary = "리뷰 작성", description = "현재 인증된 사용자가 상대방에 대해 리뷰를 작성하는 API. 판매자, 구매자 모두 해당 API를 사용 가능하다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 작성 성공",
                    content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 게시물을 찾을 수 없음"),
            @ApiResponse(responseCode = "400", description = "리뷰가 이미 존재함")
    })
    public ResponseEntity<?> write(@RequestBody CreateReviewRequest writeReviewRequest) {

        // 현재 인증된 사용자 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String userEmail = userDetails.getUsername();

        ReviewResponse result = reviewService.createReview(userEmail, writeReviewRequest);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
