package se.sowl.stitchapi.user_cam_info.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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


        String emailDomain = extractDomain(campusEmail);

        log.debug("Searching for university: {}", univName);
        Campus campus = campusRepository.findByName(univName)
                .orElseThrow(UserException.CampusNotFoundException::new);
        log.debug("Found campus: {}", campus);

        if (!emailDomain.endsWith(campus.getDomain())) {
            throw new UserException.InvalidCampusEmailDomainException();
        }

        UserCamInfo userCamInfo = UserCamInfo.builder()
                .user(user)
                .campus(campus)
                .campusEmail(campusEmail)
                .build();

        user.certifyCampus();
        userCamInfoRepository.save(userCamInfo);

        return UserCamInfoResponse.from(userCamInfo);
    }

    private String extractDomain(String email) {
        if (email == null || !email.contains("@")) {
            throw new UserException.InvalidEmailFormatException();
        }
        return email.substring(email.indexOf("@") + 1);
    }


    @Transactional
    public UserCamInfoResponse getUserCamInfo(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(UserException.UserNotFoundException::new);

        if (!user.isCampusCertified()){
            throw new UserException.UserNotCertifiedException();
        }

        UserCamInfo userCamInfo = userCamInfoRepository.findByUser(user)
                .orElseThrow(UserException.UserNotCertifiedException::new);

        return UserCamInfoResponse.from(userCamInfo);
    }
}
