package se.sowl.stitchapi.user_cam_info.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.UserException;
import se.sowl.stitchapi.user_cam_info.dto.response.UserCamInfoResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserRepository;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;

@Service
@RequiredArgsConstructor
public class UserCamInfoService {

    private final UserCamInfoRepository userCamInfoRepository;
    private final UserRepository userRepository;
    private final CampusRepository campusRepository;


    @Transactional
    public UserCamInfoResponse createUserCamInfo(Long userId, String campusEmail, String univName) {
        User user  = userRepository.findById(userId)
                .orElseThrow(UserException.UserNotFoundException::new);

        if (user.isCampusCertified()){
            throw new UserException.UserAlreadyCertifiedException();
        }

        Campus campus = campusRepository.findByName(univName)
                .orElseGet(() ->{
                    Campus newCampus = Campus.builder()
                            .name(univName)
                            .build();
                    return campusRepository.save(newCampus);
                });

        UserCamInfo userCamInfo = UserCamInfo.builder()
                .user(user)
                .campus(campus)
                .campusEmail(campusEmail)
                .build();

        user.certifyCampus();
        userCamInfoRepository.save(userCamInfo);

        return UserCamInfoResponse.from(userCamInfo);
    }
}
