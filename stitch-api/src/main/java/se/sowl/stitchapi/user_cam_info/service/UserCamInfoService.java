package se.sowl.stitchapi.user_cam_info.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.CampusException;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.exception.UserException;
import se.sowl.stitchapi.user_cam_info.dto.response.UserCamInfoResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.school.repository.MajorRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserRepository;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;

@Service
@RequiredArgsConstructor
public class UserCamInfoService {

    private final UserCamInfoRepository userCamInfoRepository;
    private final MajorRepository majorRepository;
    private final UserRepository userRepository;
    private final CampusRepository campusRepository;


    @Transactional
    public UserCamInfoResponse createUserCamInfo(String email, String univName, Long majorId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(UserException.UserNotFoundException::new);

        Campus campus = campusRepository.findByName(univName)
                .orElseThrow(CampusException.CampusNotFoundException::new);

        Major major = majorRepository.findById(majorId)
                .orElseThrow(MajorException.MajorNotFoundException::new);


        if (user.isCampusCertified()){
            throw new UserException.UserAlreadyCertifiedException();
        }

        UserCamInfo userCamInfo = UserCamInfo.builder()
                .user(user)
                .campus(campus)
                .major(major)
                .campusEmail(email)
                .build();

        user.certifyCampus();

        userCamInfoRepository.save(userCamInfo);

        return UserCamInfoResponse.from(userCamInfo);
    }
}
