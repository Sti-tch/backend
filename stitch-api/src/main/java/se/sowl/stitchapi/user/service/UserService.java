package se.sowl.stitchapi.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.user.dto.request.EditUserRequest;
import se.sowl.stitchapi.user.dto.request.UserInfoRequest;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserCamInfoRepository userCamInfoRepository;

    public List<User> getList() {
        return userRepository.findAll();
    }

    @Transactional
    public UserInfoRequest getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        return new UserInfoRequest(
                user.getId(),
                user.getUserCamInfo().getMajor().getId(),
                user.getEmail(),
                user.isCampusCertified(),
                user.getName(),
                user.getNickname(),
                user.getProvider()
        );
    }


    @Transactional
    public void editUser(Long userId, EditUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        user.updateNickname(request.getNickname());
        userRepository.save(user);
    }

}
