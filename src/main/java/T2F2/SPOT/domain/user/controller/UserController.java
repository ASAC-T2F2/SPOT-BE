package T2F2.SPOT.domain.user.controller;

import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.dto.profile.MyProfileResponse;
import T2F2.SPOT.domain.user.dto.profile.UserProfileResponse;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me/profile")
    @Operation(summary = "내 프로필 반환", description = "내 사용자 정보를 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = MyProfileResponse.class))),
            @ApiResponse(responseCode = "401", description = "User is not authenticated"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getMyProfile() {

        try {
            // 현재 인증된 사용자 조회
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
                return new ResponseEntity<>("User is not authenticated", HttpStatus.UNAUTHORIZED);
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String userEmail = userDetails.getUsername();

            MyProfileResponse myProfileResponse = userService.getMyProfile(userEmail);
            return new ResponseEntity<>(myProfileResponse, HttpStatus.OK);
        } catch (UserExceptions.UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{userId}/profile")
    @Operation(summary = "사용자 프로필 반환", description = "사용자 id를 받아 해당 사용자의 정보를 반환하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> getUserProfile(@PathVariable Long userId) {

        try {
            UserProfileResponse userProfileResponse = userService.getUserProfile(userId);
            return new ResponseEntity<>(userProfileResponse, HttpStatus.OK);
        } catch (UserExceptions.UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
