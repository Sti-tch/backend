package se.sowl.stitchapi.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.StudyPostException;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.study.dto.request.StudyPostRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostDetailResponse;
import se.sowl.stitchapi.study.dto.response.StudyPostResponse;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.MemberRole;
import se.sowl.stitchdomain.study.enumm.MemberStatus;
import se.sowl.stitchdomain.study.enumm.StudyStatus;
import se.sowl.stitchdomain.study.repository.StudyContentRepository;
import se.sowl.stitchdomain.study.repository.StudyMemberRepository;
import se.sowl.stitchdomain.study.repository.StudyPostRepository;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;

@Service
@RequiredArgsConstructor
public class StudyPostService {

    private final StudyPostRepository studyPostRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyContentRepository studyContentRepository;
    private final UserCamInfoRepository userCamInfoRepository;


    @Transactional
    public StudyPostResponse createStudyPost(StudyPostRequest studyPostRequest, Long userCamIfoId) {

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamIfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        StudyPost studyPost = StudyPost.builder()
                .title(studyPostRequest.getTitle())
                .content(studyPostRequest.getContent())
                .studyStatus(studyPostRequest.getStatus())
                .userCamInfo(userCamInfo)
                .build();

        StudyPost savedStudyPost = studyPostRepository.save(studyPost);

        StudyMember leader = StudyMember.builder()
                .studyPost(savedStudyPost)
                .userCamInfo(userCamInfo)
                .memberRole(MemberRole.LEADER)
                .memberStatus(MemberStatus.APPROVED)
                .build();

        studyMemberRepository.save(leader);
        return StudyPostResponse.from(savedStudyPost);
    }


    @Transactional
    public StudyPostDetailResponse getStudyPostDetail(Long studyPostId, Long userCamInfoId) {
        StudyPost studyPost = studyPostRepository.findById(studyPostId)
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        boolean isAuthor = studyPost.getUserCamInfo().getId().equals(userCamInfoId);

        if (!isAuthor && !(StudyStatus.RECRUITING.equals(studyPost.getStudyStatus()) ||
                StudyStatus.IN_PROGRESS.equals(studyPost.getStudyStatus()))) {
            throw new StudyPostException.UnauthorizedException();
        }

        return StudyPostDetailResponse.from(studyPost);
    }
}
