package se.sowl.stitchapi.study.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.notification.service.NotificationService;
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

    @Autowired
    private NotificationService notificationService;

    @Autowired
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

        /* NotificationService mock 설정
        StudyMemberService)만 테스트하고,
        그 외 의존하는 서비스(NotificationService)는 모킹하여 격리시킴
        (즉, 실제 NotificationService의 동작을 수행하지 않음)
         */
        doNothing().when(notificationService).createStudyApplyNotification(any(Long.class), any(Long.class));
        doNothing().when(notificationService).createApproveNotification(any(Long.class));
        doNothing().when(notificationService).createRejectNotification(any(Long.class));
    }

    @Nested
    @DisplayName("스터디 가입 신청 테스트")
    class ApplyStudyMemberTest{

        @Test
        @DisplayName("스터디 가입 신청 성공")
        void applyStudyMemberSuccess(){
            // given


            //when

            // then
        }
    }
}