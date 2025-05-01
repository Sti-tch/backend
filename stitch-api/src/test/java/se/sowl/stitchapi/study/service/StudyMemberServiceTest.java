package se.sowl.stitchapi.study.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.StudyMemberException;
import se.sowl.stitchapi.notification.service.NotificationService;
import se.sowl.stitchapi.study.dto.request.ChangeLeaderRequest;
import se.sowl.stitchapi.study.dto.request.StudyMemberApplyRequest;
import se.sowl.stitchapi.study.dto.response.StudyMemberResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.MemberRole;
import se.sowl.stitchdomain.study.enumm.MemberStatus;
import se.sowl.stitchdomain.study.enumm.StudyStatus;
import se.sowl.stitchdomain.study.repository.StudyMemberRepository;
import se.sowl.stitchdomain.study.repository.StudyPostRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class StudyMemberServiceTest {

    @Autowired
    private StudyMemberService studyMemberService;

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private UserCamInfoRepository userCamInfoRepository;

    @MockBean
    private NotificationService mockNotificationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampusRepository campusRepository;

    private User leaderUser;
    private User memberUser;
    private User applicantUser;
    private Campus testCampus;
    private UserCamInfo leaderUserCamInfo;
    private UserCamInfo memberUserCamInfo;
    private UserCamInfo applicantUserCamInfo;
    private StudyPost testStudyPost;
    private StudyMember leaderMember;
    private StudyMember regularMember;
    private StudyMember pendingMember;


    @BeforeEach
    void setUp() {
        // 테스트 캠퍼스 생성
        testCampus = Campus.builder()
                .name("Test Campus")
                .domain("test.ac.kr")
                .build();
        testCampus = campusRepository.save(testCampus);


        // 리더 유저 생성
        leaderUser = User.builder()
                .name("리더 유저")
                .email("leader@email.com")
                .nickname("leader")
                .provider("kakao")
                .campusCertified(true)
                .build();
        leaderUser = userRepository.save(leaderUser);

        leaderUserCamInfo = UserCamInfo.builder()
                .user(leaderUser)
                .campus(testCampus)
                .campusEmail("leader@test.ac.kr")
                .build();
        leaderUserCamInfo = userCamInfoRepository.save(leaderUserCamInfo);

        // 일반 멤버 유저 생성
        memberUser = User.builder()
                .name("일반 멤버")
                .email("member@email.com")
                .nickname("member")
                .provider("kakao")
                .campusCertified(true)
                .build();
        memberUser = userRepository.save(memberUser);

        memberUserCamInfo = UserCamInfo.builder()
                .user(memberUser)
                .campus(testCampus)
                .campusEmail("member@test.ac.kr")
                .build();
        memberUserCamInfo = userCamInfoRepository.save(memberUserCamInfo);

        // 신청자 유저 생성
        applicantUser = User.builder()
                .name("신청자 유저")
                .email("applicant@email.com")
                .nickname("applicant")
                .provider("kakao")
                .campusCertified(true)
                .build();
        applicantUser = userRepository.save(applicantUser);

        applicantUserCamInfo = UserCamInfo.builder()
                .user(applicantUser)
                .campus(testCampus)
                .campusEmail("applicant@test.ac.kr")
                .build();
        applicantUserCamInfo = userCamInfoRepository.save(applicantUserCamInfo);

        // 테스트 스터디 게시글 생성
        testStudyPost = StudyPost.builder()
                .userCamInfo(leaderUserCamInfo)
                .title("테스트 스터디")
                .content("테스트 스터디 내용입니다.")
                .studyStatus(StudyStatus.RECRUITING)
                .build();
        testStudyPost = studyPostRepository.save(testStudyPost);

        // 리더 멤버 생성
        leaderMember = StudyMember.builder()
                .studyPost(testStudyPost)
                .userCamInfo(leaderUserCamInfo)
                .memberRole(MemberRole.LEADER)
                .memberStatus(MemberStatus.APPROVED)
                .build();
        leaderMember = studyMemberRepository.save(leaderMember);

        // 일반 멤버 생성
        regularMember = StudyMember.builder()
                .studyPost(testStudyPost)
                .userCamInfo(memberUserCamInfo)
                .memberRole(MemberRole.MEMBER)
                .memberStatus(MemberStatus.APPROVED)
                .build();
        regularMember = studyMemberRepository.save(regularMember);

        // 대기 중인 신청자 멤버 생성
        pendingMember = StudyMember.builder()
                .studyPost(testStudyPost)
                .userCamInfo(applicantUserCamInfo)
                .memberRole(MemberRole.APPLICANT)
                .memberStatus(MemberStatus.PENDING)
                .build();
        pendingMember = studyMemberRepository.save(pendingMember);
    }

    @Nested
    @DisplayName("스터디 가입 신청 테스트")
    class ApplyStudyMemberTest{

        @Test
        @DisplayName("스터디 가입 신청 성공")
        void applyStudyMemberSuccess(){
            // given
            StudyMemberApplyRequest request = new StudyMemberApplyRequest(testStudyPost.getId(), "신청 메시지");

            //when
            StudyMemberResponse response = studyMemberService.applyStudyMember(request, applicantUserCamInfo.getId());

            // then
            assertNotNull(response);
            assertEquals(applicantUserCamInfo.getId(), response.getUserCamInfoId());
            assertEquals(MemberRole.APPLICANT, response.getMemberRole());
            assertEquals(MemberStatus.PENDING, response.getMemberStatus());

            // DB에 실제로 반영되었는지 확인
            Optional<StudyMember>  savedStudyMember = studyMemberRepository.findById(response.getId());
            assertTrue(savedStudyMember.isPresent());
            assertEquals(MemberRole.APPLICANT, savedStudyMember.get().getMemberRole());
            assertEquals(MemberStatus.PENDING, savedStudyMember.get().getMemberStatus());
        }

        @Test
        @DisplayName("스터디 가입 신청 시 이미 신청했거나 멤버인 경우 예외 발생")
        void applyStudyMemberFailAlreadyApplied() {
            // given
            StudyMember existingApplication = StudyMember.builder()
                    .studyPost(testStudyPost)
                    .userCamInfo(applicantUserCamInfo)
                    .memberRole(MemberRole.APPLICANT)
                    .memberStatus(MemberStatus.PENDING)
                    .build();
            studyMemberRepository.save(existingApplication);

            StudyMemberApplyRequest request = new StudyMemberApplyRequest(testStudyPost.getId(),"신청 메시지");

            // when & then
            assertThrows(StudyMemberException.AlreadyAppliedOrMemberException.class,
                    () -> studyMemberService.applyStudyMember(request, applicantUserCamInfo.getId()));
        }
    }


    @Nested
    @DisplayName("스터디 가입 승인 테스트")
    class ApproveStudyMemberTest{

        @Test
        @DisplayName("스터디 가입 승인 성공")
        void approveStudyMemberSuccess(){

            // when
            StudyMemberResponse response = studyMemberService.approveStudyMember(pendingMember.getId(), leaderUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(applicantUserCamInfo.getId(), response.getUserCamInfoId());
            assertEquals(MemberRole.MEMBER, response.getMemberRole());
            assertEquals(MemberStatus.APPROVED, response.getMemberStatus());

            // DB에 실제로 반영되었는지 확인
            Optional<StudyMember> savedStudyMember = studyMemberRepository.findById(response.getId());
            assertTrue(savedStudyMember.isPresent());
            assertEquals(MemberRole.MEMBER, savedStudyMember.get().getMemberRole());
            assertEquals(MemberStatus.APPROVED, savedStudyMember.get().getMemberStatus());
        }

        @Test
        @DisplayName("스터디 가입 승인 시 리더가 아닌 경우 예외 발생")
        void approveStudyMemberFailNotLeader(){
            // when & then
            assertThrows(StudyMemberException.NotLeaderException.class,
                    () -> studyMemberService.approveStudyMember(pendingMember.getId(), memberUserCamInfo.getId()));
        }
    }

    @Nested
    @DisplayName("스터디 가입 거절 테스트")
    class RejectStudyMemberTest{

        @Test
        @DisplayName("스터디 가입 거절 성공")
        void rejectStudyMemberSuccess(){

            // when
            StudyMemberResponse response = studyMemberService.rejectStudyMember(pendingMember.getId(), leaderUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(applicantUserCamInfo.getId(), response.getUserCamInfoId());
            assertEquals(MemberRole.APPLICANT, response.getMemberRole());
            assertEquals(MemberStatus.REJECTED, response.getMemberStatus());

            // DB에 실제로 반영되었는지 확인
            Optional<StudyMember> savedStudyMember = studyMemberRepository.findById(response.getId());
            assertTrue(savedStudyMember.isPresent());
            assertEquals(MemberRole.APPLICANT, savedStudyMember.get().getMemberRole());
            assertEquals(MemberStatus.REJECTED, savedStudyMember.get().getMemberStatus());
        }

        @Test
        @DisplayName("스터디 가입 거절 시 리더가 아닌 경우 예외 발생")
        void rejectStudyMemberFailNotLeader(){
            // when & then
            assertThrows(StudyMemberException.NotLeaderException.class,
                    () -> studyMemberService.rejectStudyMember(pendingMember.getId(), memberUserCamInfo.getId()));
        }
    }

    @Nested
    @DisplayName("스터디 탈퇴 테스트")
    class LeaveStudyMemberTest{

        @Test
        @DisplayName("스터디 탈퇴 성공")
        void leaveStudyMemberSuccess() {
            // given
            Long studyPostId = testStudyPost.getId();
            Long memberUserCamInfoId = memberUserCamInfo.getId();

            // when
            studyMemberService.leaveStudyMember(studyPostId, memberUserCamInfoId);

            // then
            // DB에서 삭제되었는지 확인
            Optional<StudyMember> deletedMember = studyMemberRepository.findByStudyPostAndUserCamInfo(testStudyPost, memberUserCamInfo);
            assertFalse(deletedMember.isPresent());
        }

        @Test
        @DisplayName("스터디 탈퇴 시 스터디 리더인 경우 예외 발생")
        void leaveStudyMemberFailLeader() {
            // given
            Long studyPostId = testStudyPost.getId();
            Long leaderUserCamInfoId = leaderUserCamInfo.getId();

            // when & then
            assertThrows(StudyMemberException.LeaderCannotLeaveException.class,
                    () -> studyMemberService.leaveStudyMember(studyPostId, leaderUserCamInfoId));
        }
    }

    @Nested
    @DisplayName("리더 변경 테스트")
    class ChangeLeaderTest{

        @Test
        @DisplayName("스터디 리더 변경 성공")
        void changeLeaderSuccess(){
            // given
            ChangeLeaderRequest request = new ChangeLeaderRequest(testStudyPost.getId(), regularMember.getId());

            //when
            StudyMemberResponse response = studyMemberService.changeLeader(request, leaderUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(memberUserCamInfo.getId(), response.getUserCamInfoId());
            assertEquals(MemberRole.LEADER, response.getMemberRole());

            // DB에 실제로 반영되었는지 확인
            StudyMember updateOldLeader = studyMemberRepository.findById(leaderMember.getId())
                    .orElseThrow(StudyMemberException.MemberNotFoundException::new);
            StudyMember updateNewLeader = studyMemberRepository.findById(response.getId())
                    .orElseThrow(StudyMemberException.MemberNotFoundException::new);

            assertEquals(MemberRole.MEMBER, updateOldLeader.getMemberRole());
            assertEquals(MemberRole.LEADER, updateNewLeader.getMemberRole());
        }

        @Test
        @DisplayName("리더가 아닌 경우 리더 변경 실패")
        void changeLeaderFailNotLeader() {
            // given
            ChangeLeaderRequest request = new ChangeLeaderRequest(testStudyPost.getId(), regularMember.getId());

            // when & then
            assertThrows(StudyMemberException.NotLeaderException.class,
                    () -> studyMemberService.changeLeader(request, memberUserCamInfo.getId()));
        }
    }

}



