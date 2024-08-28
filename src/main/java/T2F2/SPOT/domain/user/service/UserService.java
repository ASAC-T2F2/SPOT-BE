package T2F2.SPOT.domain.user.service;

import T2F2.SPOT.domain.user.dto.MyProfileResponse;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.repository.UserRepository;
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
            throw new UserExceptions.UserNotFoundException(userEmail);
        }

        log.info("[User Service] - Found User: {}", user.getEmail());

        return MyProfileResponse.from(user);
    }
}
