package se.sowl.stitchapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import se.sowl.stitchapi.user.dto.response.UserInfoRequest;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getList() {
        return userRepository.findAll();
    }

    public UserInfoRequest getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserInfoRequest(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getNickname(),
                user.getProvider()
        );
    }
}
