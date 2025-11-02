package se.sowl.stitchapi.major.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.exception.UserException;
import se.sowl.stitchapi.major.dto.response.MajorDetailResponse;
import se.sowl.stitchapi.major.dto.response.MajorListResponse;
import se.sowl.stitchapi.major.dto.response.MajorResponse;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.MajorRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorRepository majorRepository;
    private final UserRepository userRepository;
    private final UserCamInfoRepository userCamInfoRepository;

    @Transactional
    public List<MajorListResponse> getAllMajors(){
        return majorRepository.findAll().stream()
                .map(MajorListResponse::from)
                .toList();
    }

    @Transactional
    public MajorDetailResponse getMajorDetail(Long majorId){
        Major major = majorRepository.findById(majorId)
                .orElseThrow(MajorException.MajorNotFoundException::new);
        return MajorDetailResponse.from(major);
    }



    @Transactional
    public MajorResponse selectMajor(Long majorId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserException.UserNotFoundException::new);

        if (!user.isCampusCertified()) {
            throw new UserException.UserNotCertifiedException();
        }

        UserCamInfo userCamInfo = userCamInfoRepository.findByUser(user)
                .orElseThrow(UserException.UserCamInfoNotFoundException::new);

        Major newMajor = findMajorById(majorId);


        // 새 전공 설정
        userCamInfo.setMajor(newMajor);

        userCamInfoRepository.save(userCamInfo);

        return MajorResponse.from(newMajor);
    }


    private Major findMajorById(Long majorId) {
        return majorRepository.findById(majorId)
                .orElseThrow(MajorException.MajorNotFoundException::new);
    }
}
