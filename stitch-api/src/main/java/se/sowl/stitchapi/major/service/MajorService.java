package se.sowl.stitchapi.major.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.exception.UserException;
import se.sowl.stitchapi.major.dto.request.MajorRequest;
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


    // 학교 인증 직후 전공 선택 또는 건너뛰기를 처리하는 메서드
    @Transactional
    public MajorResponse selectMajor(MajorRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserException.UserNotFoundException::new);

        UserCamInfo userCamInfo = userCamInfoRepository.findByUser(user)
                .orElseThrow(UserException.UserCamInfoNotFoundException::new);

        // 마이페이지에서의 전공 선택 (이전에 건너뛰기 했던 경우)
        if (userCamInfo.isMajorSkipped() && request.getMajorId() != null) {
            return handleMyPageMajorSelection(request);
        }

        // 학교 인증 직후의 전공 선택 또는 건너뛰기
        return handleInitialMajorSelection(request);
    }

    private MajorResponse handleInitialMajorSelection(MajorRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserException.UserNotFoundException::new);

        if (!user.isCampusCertified()){
            throw new UserException.UserNotCertifiedException();
        }

        UserCamInfo userCamInfo = userCamInfoRepository.findByUser(user)
                .orElseThrow(UserException.UserCamInfoNotFoundException::new);

        if (request.isSkipped()){
            userCamInfo.setMajorSkipped(true);
            return null;
        }

        if (request.getMajorId() == null){
            throw new MajorException.MajorIdRequiredException();
        }

        Major major = findMajorById(request.getMajorId());
        userCamInfo.assignMajor(major);
        return MajorResponse.from(major);
    }


    private MajorResponse handleMyPageMajorSelection(MajorRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(UserException.UserNotFoundException::new);

        UserCamInfo userCamInfo = userCamInfoRepository.findByUser(user)
                .orElseThrow(UserException.UserCamInfoNotFoundException::new);

        if (request.getMajorId() == null){
            throw new MajorException.MajorIdRequiredException();
        }

        Major major = findMajorById(request.getMajorId());
        userCamInfo.assignMajor(major);
        return MajorResponse.from(major);
    }


    private Major findMajorById(Long majorId) {
        return majorRepository.findById(majorId)
                .orElseThrow(MajorException.MajorNotFoundException::new);
    }
}
