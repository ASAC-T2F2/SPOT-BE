package T2F2.SPOT.domain.wish.controller;

import T2F2.SPOT.domain.post.exception.PostException;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.wish.dto.AddWishRequest;
import T2F2.SPOT.domain.wish.dto.AddWishResponse;
import T2F2.SPOT.domain.wish.service.WishService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wish")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addWish(@RequestBody AddWishRequest addWishRequest) {
        try {
            AddWishResponse response = wishService.addWish(addWishRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (UserExceptions.UserNotFoundException | PostException.PostNotFoundException e) {
            // 사용자 또는 게시글을 찾지 못한 경우
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // 기타 예외 상황
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
