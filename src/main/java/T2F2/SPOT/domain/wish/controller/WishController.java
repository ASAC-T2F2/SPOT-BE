package T2F2.SPOT.domain.wish.controller;

import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.service.AuthService;
import T2F2.SPOT.domain.wish.dto.*;
import T2F2.SPOT.domain.wish.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "찜", description = "찜 관련 API")
@RestController
@RequestMapping("/api/wish")
@Slf4j
public class WishController {

    private final WishService wishService;
    private final AuthService authService;

    public WishController(WishService wishService, AuthService authService) {
        this.wishService = wishService;
        this.authService = authService;
    }

    @PostMapping("/add")
    @Operation(summary = "찜 추가 API", description = "요청 시, 찜 데이터 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "찜이 성공적으로 추가됨",
                    content = @Content(schema = @Schema(implementation = AddWishResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "사용자 또는 게시글을 찾을 수 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<?> addWish(@RequestBody AddWishRequest addWishRequest) {

        String userEmail = authService.getAuthenticatedUserEmail();

        AddWishResponse response = wishService.addWish(addWishRequest, userEmail);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @Operation(summary = "찜 취소 API", description = "요청 시 찜 데이터를 취소")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜이 성공적으로 취소됨",
                    content = @Content(schema = @Schema(implementation = CancelWishResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "찜을 찾을 수 없음")
    })
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelWish(@RequestBody CancelWishRequest cancelWishRequest) {

        String userEmail = authService.getAuthenticatedUserEmail();

        CancelWishResponse cancelWishResponse = wishService.cancelWish(cancelWishRequest, userEmail);
        return new ResponseEntity<>(cancelWishResponse, HttpStatus.OK);
    }


    @Operation(summary = "찜 목록 조회 API", description = "현재 사용자의 모든 찜 데이터를 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "찜 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = PreviewWishResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "찜 또는 사용자를 찾을 수 없음")
    })
    @GetMapping("/wishes")
    public ResponseEntity<?> getWishes() {

        String userEmail = authService.getAuthenticatedUserEmail();

        List<PreviewWishResponse> wishes = wishService.findAllWish(userEmail);
        return new ResponseEntity<>(wishes, HttpStatus.OK);
    }
}
