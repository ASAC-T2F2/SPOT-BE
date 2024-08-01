package T2F2.SPOT.domain.user.service;

import T2F2.SPOT.domain.user.dto.CustomUserDetails;
import T2F2.SPOT.domain.user.entity.User;
import T2F2.SPOT.domain.user.exception.UserExceptions;
import T2F2.SPOT.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    /**
     * Email 기반으로 UserDetails 획득
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserExceptions.UserNotFoundException("Username " + email + " not found");
        }

        // AuthenticationManager가 검증할 수 있도록 UserDetails에 담아서 반환
        return new CustomUserDetails(user);
    }
}
