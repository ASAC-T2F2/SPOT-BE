package T2F2.SPOT.domain.user.service;

import T2F2.SPOT.domain.user.dto.profile.MyProfileResponse;
import T2F2.SPOT.domain.user.dto.profile.UserProfileResponse;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.repository.UserRepository;
import T2F2.SPOT.util.exception.CustomException;
import T2F2.SPOT.util.exception.error_code.UserErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 내 정보 반환
     * @param userEmail
     * @return 내 사용자 정보
     */
    public MyProfileResponse getMyProfile(String userEmail) {

        User user = userRepository.findByEmail(userEmail);

        if (user == null) {
            throw new CustomException(UserErrorCode.NOT_FOUND);
        }

        log.info("[User Service] - Found User: {}", user.getEmail());

        return MyProfileResponse.from(user);
    }

    public UserProfileResponse getUserProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new CustomException(UserErrorCode.NOT_FOUND)
        );

        log.info("[User Service] - Found User: {}", user.getEmail());
        return UserProfileResponse.from(user);
    }
}
