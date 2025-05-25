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

        Long majorId = null;
        String majorName = null;
        Long campusId = null;
        String campusName = null;
        Long useCamInfoId = null;

        if (user.isCampusCertified()) {
            UserCamInfo userCamInfo = userCamInfoRepository.findByUser(user)
                    .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);
            useCamInfoId = userCamInfo.getId();

            if (userCamInfo.getMajor() != null) {
                majorId = userCamInfo.getMajor().getId();
                majorName = userCamInfo.getMajor().getName();
            }
            if(userCamInfo.getCampus() != null) {
                campusId = userCamInfo.getCampus().getId();
                campusName = userCamInfo.getCampus().getName();
            }
        }

        return new UserInfoRequest(
                user.getId(),
                majorId,
                majorName,
                user.getEmail(),
                user.isCampusCertified(),
                campusId,
                campusName,
                user.getName(),
                user.getNickname(),
                user.getProvider(),
                useCamInfoId
        );
    }


    @Transactional
    public void editUser(Long userId, EditUserRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        user.updateNickname(request.getNickname());
        userRepository.save(user);
    }

}
