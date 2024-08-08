package T2F2.SPOT.domain.user.service;

import T2F2.SPOT.domain.user.dto.JoinDTO;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /**
     * 회원가입
     * @param joinDTO
     * @return 회원가입 성공여부
     */
    public Boolean signUp(JoinDTO joinDTO) {
        String email = joinDTO.getEmail();

        Boolean isExistUser = userRepository.existsByEmail(email);

        if (isExistUser) {
            throw new UserExceptions.EmailAlreadyExistsException("Email(" + email + ") already exists");
        }

        try {
            User newUser = JoinDTO.toUser(joinDTO, bCryptPasswordEncoder);
            userRepository.save(newUser);
            return true;
        } catch (Exception e) {
            throw new UserExceptions.SignUpFailedException("Error while signing up");
        }
    }
}
