package T2F2.SPOT.domain.wish.controller;

import T2F2.SPOT.domain.post.exception.PostException;
import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.wish.dto.AddWishRequest;
import T2F2.SPOT.domain.wish.dto.AddWishResponse;
import T2F2.SPOT.domain.wish.dto.CancelWishRequest;
import T2F2.SPOT.domain.wish.dto.CancelWishResponse;
import T2F2.SPOT.domain.wish.exception.WishException;
import T2F2.SPOT.domain.wish.service.WishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wish")
@Slf4j
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addWish(@RequestBody AddWishRequest addWishRequest) {
        try {
            // 현재 인증된 사용자 조회
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String userEmail = userDetails.getUsername();

            AddWishResponse response = wishService.addWish(addWishRequest, userEmail);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (UserExceptions.UserNotFoundException | PostException.PostNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error while adding wish", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelWish(@RequestBody CancelWishRequest cancelWishRequest) {

        try {
            CancelWishResponse cancelWishResponse = wishService.cancelWish(cancelWishRequest);
            return new ResponseEntity<>(cancelWishResponse, HttpStatus.OK);
        } catch (WishException.WishNotFoundException e) {
            log.error("Error while canceling wish", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
