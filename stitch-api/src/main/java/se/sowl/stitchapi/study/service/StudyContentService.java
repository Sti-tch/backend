package se.sowl.stitchapi.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.StudyContentException;
import se.sowl.stitchapi.exception.StudyMemberException;
import se.sowl.stitchapi.exception.StudyPostException;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.notification.service.NotificationService;
import se.sowl.stitchapi.study.dto.request.StudyContentRequest;
import se.sowl.stitchapi.study.dto.response.StudyContentDetailResponse;
import se.sowl.stitchapi.study.dto.response.StudyContentListResponse;
import se.sowl.stitchapi.study.dto.response.StudyContentResponse;
import se.sowl.stitchdomain.study.domain.StudyContent;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.MemberRole;
import se.sowl.stitchdomain.study.enumm.MemberStatus;
import se.sowl.stitchdomain.study.repository.StudyContentRepository;
import se.sowl.stitchdomain.study.repository.StudyMemberRepository;
import se.sowl.stitchdomain.study.repository.StudyPostRepository;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyContentService {

    private final StudyContentRepository studyContentRepository;
    private final StudyPostRepository studyPostRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final UserCamInfoRepository userCamInfoRepository;
    private final NotificationService notificationService;


    /**
     * 스터디 컨텐츠 생성
     * 요청된 정보로 새로운 스터디 컨텐츠를 생성합니다.
     */
    @Transactional
    public StudyContentResponse createStudyContent(StudyContentRequest request, Long userCamInfoId) {

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        StudyPost studyPost = studyPostRepository.findById(request.getStudyPostId())
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        // 스터디 멤버인지 확인
        validateStudyMember(studyPost, userCamInfo);

        StudyContent studyContent = StudyContent.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .studyPost(studyPost)
                .userCamInfo(userCamInfo)
                .studyContentType(request.getContentType())
                .build();
        StudyContent savedStudyContent = studyContentRepository.save(studyContent);

        notificationService.createNewContentNotification(savedStudyContent.getId(), userCamInfoId);

        return StudyContentResponse.from(savedStudyContent);
    }

    /**
     * 스터디별 컨텐츠 목록 조회
     * 특정 스터디에 속한 모든 컨텐츠 목록을 조회합니다.
     */
    @Transactional
    public List<StudyContentListResponse> getStudyContentsByStudyPost(Long studyPostId, Long userCamInfoId) {

        StudyPost studyPost = studyPostRepository.findById(studyPostId)
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        validateStudyMember(studyPost, userCamInfo);

        List<StudyContent> contents = studyContentRepository.findByStudyPostOrderByCreatedAtDesc(studyPost);
        return contents.stream()
                .map(StudyContentListResponse::from)
                .toList();
    }


    /**
     * 스터디 컨텐츠 상세 조회
     * 특정 컨텐츠의 상세 정보를 조회합니다.
     */
    @Transactional
    public StudyContentDetailResponse getStudyContentDetail(Long contentId, Long userCamInfoId) {

        StudyContent studyContent = studyContentRepository.findById(contentId)
                .orElseThrow(StudyContentException.ContentNotFoundException::new);

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        validateStudyMember(studyContent.getStudyPost(), userCamInfo);

        return StudyContentDetailResponse.from(studyContent);
    }


    /**
     * 스터디 컨텐츠 수정
     * 기존 컨텐츠를 새로운 정보로 업데이트합니다.
     */
    @Transactional
    public StudyContentResponse updateStudyContent(Long contentId, StudyContentRequest request, Long userCamInfoId){
        StudyContent studyContent = studyContentRepository.findById(contentId)
                .orElseThrow(StudyContentException.ContentNotFoundException::new);

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        validateUpdateDeletePermission(studyContent, userCamInfo);

        // 수정된 정보로 컨텐츠 업데이트
        studyContent.updateContent(
                request.getTitle(),
                request.getContent(),
                request.getContentType()
        );

        return StudyContentResponse.from(studyContent);
    }

    /**
     * 스터디 컨텐츠 삭제
     * 특정 컨텐츠를 삭제합니다.
     */
    @Transactional
    public void deleteStudyContent(Long contentId, Long userCamInfoId) {
        StudyContent studyContent = studyContentRepository.findById(contentId)
                .orElseThrow(StudyContentException.ContentNotFoundException::new);

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        validateUpdateDeletePermission(studyContent, userCamInfo);

        studyContentRepository.delete(studyContent);
    }

    /**
     * 컨텐츠 수정/삭제 권한 검증 메서드
     * 작성자 또는 리더만 컨텐츠를 수정하거나 삭제할 수 있습니다.
     */
    private void validateUpdateDeletePermission(StudyContent studyContent, UserCamInfo userCamInfo) {
        // 작성자 확인
        boolean isAuthor = studyContent.getUserCamInfo().getId().equals(userCamInfo.getId());

        // 리더 확인
        boolean isLeader = studyMemberRepository.existsByStudyPostAndUserCamInfoAndMemberRole(
                studyContent.getStudyPost(), userCamInfo, MemberRole.LEADER);

        // 작성자도 아니고 리더도 아니면 권한 없음
        if (!isAuthor && !isLeader) {
            throw new StudyContentException.UnauthorizedException();
        }
    }

    /**
     * 스터디 멤버 유효성 검증
     * 사용자가 해당 스터디의 승인된 멤버 또는 리더인지 확인합니다.
     * @return 검증된 StudyMember 객체
     */
    private void validateStudyMember(StudyPost studyPost, UserCamInfo userCamInfo){

        StudyMember studyMember = studyMemberRepository.findByStudyPostAndUserCamInfo(studyPost, userCamInfo)
                .orElseThrow(StudyMemberException.MemberNotFoundException::new);

        // 리더이거나 승인된 멤버인지 확인
        boolean isLeader = studyMember.getMemberRole() == MemberRole.LEADER;

        boolean isApprovedMember = studyMember.getMemberRole() == MemberRole.MEMBER &&
                studyMember.getMemberStatus() == MemberStatus.APPROVED;

        if (!(isLeader || isApprovedMember)) {
            throw new StudyContentException.UnauthorizedException();
        }
    }
}