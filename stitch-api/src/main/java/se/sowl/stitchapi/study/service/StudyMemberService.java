package se.sowl.stitchapi.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.StudyMemberException;
import se.sowl.stitchapi.exception.StudyPostException;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.notification.service.NotificationService;
import se.sowl.stitchapi.study.dto.request.ChangeLeaderRequest;
import se.sowl.stitchapi.study.dto.request.StudyMemberApplyRequest;
import se.sowl.stitchapi.study.dto.response.MyStudyResponse;
import se.sowl.stitchapi.study.dto.response.StudyMemberResponse;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.MemberRole;
import se.sowl.stitchdomain.study.enumm.MemberStatus;
import se.sowl.stitchdomain.study.repository.StudyMemberRepository;
import se.sowl.stitchdomain.study.repository.StudyPostRepository;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyMemberService {

    private final StudyMemberRepository studyMemberRepository;
    private final StudyPostRepository studyPostRepository;
    private final UserCamInfoRepository userCamInfoRepository;
    private final NotificationService notificationService;

    // 스터디 가입 신청
    @Transactional
    public StudyMemberResponse applyStudyMember(StudyMemberApplyRequest request, Long userCamInfoId){
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        if (!userCamInfo.getUser().isCampusCertified()){
            throw new StudyMemberException.SchoolVerificationRequiredException();
        }

        StudyPost studyPost = studyPostRepository.findById(request.getStudyPostId())
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);
        
        // 이미 신청했거나 멤버인지 확인 
        boolean alreadyApplied = studyMemberRepository.existsByStudyPostAndUserCamInfo(studyPost, userCamInfo);

        if (alreadyApplied) {
            throw new StudyMemberException.AlreadyAppliedOrMemberException();
        }

        StudyMember studyMember = StudyMember.builder()
                .studyPost(studyPost)
                .userCamInfo(userCamInfo)
                .memberRole(MemberRole.APPLICANT)
                .memberStatus(MemberStatus.PENDING)
                .applyMessage(request.getApplyMessage())
                .build();
        
        StudyMember savedStudyMember = studyMemberRepository.save(studyMember);
        
        UserCamInfo leader = findStudyLeader(studyPost);
        notificationService.createStudyApplyNotification(leader.getId(), savedStudyMember.getId());
        
        return StudyMemberResponse.from(savedStudyMember);

    }

    private UserCamInfo findStudyLeader(StudyPost studyPost) {
        return studyMemberRepository.findByStudyPostAndMemberRole(studyPost, MemberRole.LEADER)
                .orElseThrow(StudyMemberException.LeaderNotFoundException::new)
                .getUserCamInfo();
    }

    // 스터디 가입 승인
    @Transactional
    public StudyMemberResponse approveStudyMember(Long studyMemberId, Long userCamInfoId){
        StudyMember studyMember = validateMemberAndLeader(studyMemberId, userCamInfoId);

        studyMember.updateMemberRole(MemberRole.MEMBER);
        studyMember.updateMemberStatus(MemberStatus.APPROVED);

        notificationService.createApproveNotification(studyMember.getId());

        return StudyMemberResponse.from(studyMember);
    }

    // 스터디 가입 거절
    @Transactional
    public StudyMemberResponse rejectStudyMember(Long studyMemberId, Long userCamInfoId) {
        StudyMember studyMember = validateMemberAndLeader(studyMemberId, userCamInfoId);

        studyMember.updateMemberStatus(MemberStatus.REJECTED);

        notificationService.createRejectNotification(studyMember.getId());

        return StudyMemberResponse.from(studyMember);
    }

    /**
     * 스터디 멤버와 리더 검증 (공통 메서드)
     */
    private StudyMember validateMemberAndLeader(Long studyMemberId, Long userCamInfoId) {
        // 스터디 멤버 조회
        StudyMember studyMember = studyMemberRepository.findById(studyMemberId)
                .orElseThrow(StudyMemberException.MemberNotFoundException::new);

        // 리더 조회
        UserCamInfo leader = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        // 리더 권한 확인
        boolean isLeader = studyMemberRepository.existsByStudyPostAndUserCamInfoAndMemberRole(
                studyMember.getStudyPost(), leader, MemberRole.LEADER);

        if (!isLeader) {
            throw new StudyMemberException.NotLeaderException();
        }

        // 신청자(APPLICANT)이고 대기 상태(PENDING)인 경우에만 처리 가능
        if (studyMember.getMemberRole() != MemberRole.APPLICANT || studyMember.getMemberStatus() != MemberStatus.PENDING) {
            throw new StudyMemberException.AlreadyProcessedException();
        }

        return studyMember;
    }

    /*
     * 스터디 멤버 목록 조회(승인된 멤버만)
     */
    @Transactional(readOnly = true)
    public List<StudyMemberResponse> getStudyMembers(Long studyPostId) {
        StudyPost studyPost = studyPostRepository.findById(studyPostId)
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        return studyMemberRepository.findByStudyPostAndMemberStatus(studyPost, MemberStatus.APPROVED)
                .stream()
                .map(StudyMemberResponse::from)
                .toList();
    }

    @Transactional
    public void leaveStudyMember(Long studyPostId, Long userCamInfoId){
        StudyPost studyPost = studyPostRepository.findById(studyPostId)
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        StudyMember studyMember = studyMemberRepository.findByStudyPostAndUserCamInfo(studyPost, userCamInfo)
                .orElseThrow(StudyMemberException.NotMemberException::new);

        // 리더는 탈퇴할 수 없음
        if (studyMember.getMemberRole() == MemberRole.LEADER) {
            throw new StudyMemberException.LeaderCannotLeaveException();
        }

        // 승인된 멤버만 탈퇴 가능
        if (studyMember.getMemberRole() == MemberRole.MEMBER && studyMember.getMemberStatus() != MemberStatus.APPROVED) {
            throw new StudyMemberException.NotApprovedMemberException();
        }

        studyMemberRepository.delete(studyMember);
    }

    // 리더 변경
    @Transactional
    public StudyMemberResponse changeLeader(ChangeLeaderRequest request, Long userCamInfoId) {
        StudyPost studyPost = studyPostRepository.findById(request.getStudyPostId())
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        // 현재 리더 정보
        UserCamInfo currentLeaderInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        // 현재 리더의 멤버 조회
        StudyMember currentLeader = studyMemberRepository.findByStudyPostAndUserCamInfo(studyPost, currentLeaderInfo)
                .orElseThrow(StudyMemberException.NotMemberException::new);

        if (currentLeader.getMemberRole() != MemberRole.LEADER) {
            throw new StudyMemberException.NotLeaderException();
        }

        // 새 리더 확인 (MEMBER이면서 APPROVED 상태여야 함)
        StudyMember newLeader = studyMemberRepository.findById(request.getNewLeaderId())
                .orElseThrow(StudyMemberException.MemberNotFoundException::new);

        if (newLeader.getMemberRole() != MemberRole.MEMBER || newLeader.getMemberStatus() != MemberStatus.APPROVED) {
            throw new StudyMemberException.NotApprovedMemberException();
        }

        currentLeader.updateMemberRole(MemberRole.MEMBER);
        newLeader.updateMemberRole(MemberRole.LEADER);

        return StudyMemberResponse.from(newLeader);
    }

    /*
     * 내 스터디 신청자 목록 조회(리더만 가능)
     */
    @Transactional
    public List<StudyMemberResponse> getApplicantsMyStudy(Long studyPostId, Long userCamInfoId){
        StudyPost studyPost = studyPostRepository.findById(studyPostId)
                .orElseThrow(StudyPostException.StudyPostNotFoundException::new);

        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        // 리더인지 확인
        boolean isLeader = studyMemberRepository.existsByStudyPostAndUserCamInfoAndMemberRole(
                studyPost, userCamInfo, MemberRole.LEADER);

        if (!isLeader) {
            throw new StudyMemberException.NotLeaderException();
        }

        return studyMemberRepository.findByStudyPostAndMemberRole(studyPost, MemberRole.APPLICANT)
                .stream()
                .map(StudyMemberResponse::from)
                .toList();
    }

    /*
     * 내가 신청한 스터디 목록 조회
     */
    @Transactional(readOnly = true)
    public List<MyStudyResponse> getMyApplications(Long userCamInfoId) {
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        return studyMemberRepository.findByUserCamInfo(userCamInfo)
                .stream()
                .map(MyStudyResponse::from)
                .toList();
    }

    /*
     * 내가 속한 스터디 목록 조회(승인된 스터디만)
     */
    @Transactional(readOnly = true)
    public List<MyStudyResponse> getMyJoinedStudies(Long userCamInfoId) {
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        List<StudyMember> joinedStudies = studyMemberRepository.findByUserCamInfoAndMemberStatus(
                userCamInfo, MemberStatus.APPROVED);

        return joinedStudies.stream()
                .map(MyStudyResponse::from)
                .toList();
    }
}
