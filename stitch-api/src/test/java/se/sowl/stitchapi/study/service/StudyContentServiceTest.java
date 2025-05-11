package se.sowl.stitchapi.study.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.StudyContentException;
import se.sowl.stitchapi.study.dto.request.StudyContentRequest;
import se.sowl.stitchapi.study.dto.response.StudyContentListResponse;
import se.sowl.stitchapi.study.dto.response.StudyContentResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.school.repository.MajorRepository;
import se.sowl.stitchdomain.study.domain.StudyContent;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.enumm.ContentType;
import se.sowl.stitchdomain.study.enumm.MemberRole;
import se.sowl.stitchdomain.study.enumm.MemberStatus;
import se.sowl.stitchdomain.study.enumm.StudyStatus;
import se.sowl.stitchdomain.study.repository.StudyContentRepository;
import se.sowl.stitchdomain.study.repository.StudyMemberRepository;
import se.sowl.stitchdomain.study.repository.StudyPostRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyContentServiceTest {

    @Autowired
    private StudyContentService studyContentService;

    @Autowired
    private StudyContentRepository studyContentRepository;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private UserCamInfoRepository userCamInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private CampusRepository campusRepository;

    private User leaderUser;
    private User memberUser;
    private User pendingUser;
    private Campus testCampus;
    private Major testMajor;
    private UserCamInfo leaderUserCamInfo;
    private UserCamInfo memberUserCamInfo;
    private UserCamInfo pendingUserCamInfo;
    private StudyPost testStudyPost;
    private StudyMember leaderMember;
    private StudyMember regularMember;
    private StudyMember pendingMember;
    private StudyContent testStudyContent;

    @BeforeEach
    void setUp(){
        // 테스트 캠퍼스 생성
        testCampus = Campus.builder()
                .name("Test Campus")
                .domain("test.ac.kr")
                .build();
        testCampus = campusRepository.save(testCampus);

        // 테스트 전공 생성
        testMajor = Major.builder()
                .name("Test Major")
                .build();
        testMajor = majorRepository.save(testMajor);

        // 리더 유저 생성
        leaderUser = User.builder()
                .name("Leader User")
                .email("leader@email.com")
                .nickname("leader")
                .provider("kakao")
                .campusCertified(true)
                .build();
        leaderUser = userRepository.save(leaderUser);

        leaderUserCamInfo = UserCamInfo.builder()
                .user(leaderUser)
                .campus(testCampus)
                .major(testMajor)
                .campusEmail("leader@test.ac.kr")
                .build();
        leaderUserCamInfo = userCamInfoRepository.save(leaderUserCamInfo);

        // 일반 멤버 유저 생성
        memberUser = User.builder()
                .name("Member User")
                .email("member@email.com")
                .nickname("member")
                .provider("kakao")
                .campusCertified(true)
                .build();
        memberUser = userRepository.save(memberUser);

        memberUserCamInfo = UserCamInfo.builder()
                .user(memberUser)
                .campus(testCampus)
                .major(testMajor)
                .campusEmail("member@test.ac.kr")
                .build();
        memberUserCamInfo = userCamInfoRepository.save(memberUserCamInfo);

        // 승인되지 않은 유저 생성
        pendingUser = User.builder()
                .name("Pending User")
                .email("pending@email.com")
                .nickname("pending")
                .provider("kakao")
                .campusCertified(true)
                .build();
        pendingUser = userRepository.save(pendingUser);

        pendingUserCamInfo = UserCamInfo.builder()
                .user(pendingUser)
                .campus(testCampus)
                .major(testMajor)
                .campusEmail("pending@test.ac.kr")
                .build();
        pendingUserCamInfo = userCamInfoRepository.save(pendingUserCamInfo);

        // 테스트 스터디 게시글 생성
        testStudyPost = StudyPost.builder()
                .userCamInfo(leaderUserCamInfo)
                .title("Test Study")
                .content("This is test study content.")
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

        // 승인되지 않은 멤버 생성
        pendingMember = StudyMember.builder()
                .studyPost(testStudyPost)
                .userCamInfo(pendingUserCamInfo)
                .memberRole(MemberRole.APPLICANT)
                .memberStatus(MemberStatus.PENDING)
                .build();
        pendingMember = studyMemberRepository.save(pendingMember);

        // 테스트 스터디 컨텐츠 생성
        testStudyContent = StudyContent.builder()
                .studyPost(testStudyPost)
                .userCamInfo(leaderUserCamInfo)
                .title("Test Content")
                .content("This is test content.")
                .studyContentType(ContentType.NOTICE)
                .build();
        testStudyContent = studyContentRepository.save(testStudyContent);
    }

    @Nested
    @DisplayName("스터디 컨텐츠 생성 테스트")
    class CreateStudyContentTest{

        @Test
        @DisplayName("스터디 컨텐츠 생성 성공 - 스터디 리더")
        void createStudyContentSuccessLeader(){
            //given
            StudyContentRequest request = StudyContentRequest.builder()
                    .studyPostId(testStudyPost.getId())
                    .title("New Content")
                    .content("This is new content.")
                    .contentType(ContentType.MATERIAL)
                    .build();

            //when
            StudyContentResponse response = studyContentService.createStudyContent(request, leaderUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(request.getTitle(), response.getTitle());
            assertEquals(request.getContent(), response.getContent());
            assertEquals(request.getContentType(), response.getContentType());
            assertEquals(leaderUserCamInfo.getId(), response.getUserCamInfoId());
        }

        @Test
        @DisplayName("스터디 컨텐츠 생성 성공 - 일반 멤버")
        void createStudyContentSuccessMember(){
            //given
            StudyContentRequest request = StudyContentRequest.builder()
                    .studyPostId(testStudyPost.getId())
                    .title("New Content")
                    .content("This is new content.")
                    .contentType(ContentType.MATERIAL)
                    .build();

            //when
            StudyContentResponse response = studyContentService.createStudyContent(request, memberUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(request.getTitle(), response.getTitle());
            assertEquals(request.getContent(), response.getContent());
            assertEquals(request.getContentType(), response.getContentType());
            assertEquals(memberUserCamInfo.getId(), response.getUserCamInfoId());
        }

        @Test
        @DisplayName("스터디 컨텐츠 생성 실패 - 승인되지 않은 멤버")
        void createStudyContentFailUnauthorized() {
            // given
            StudyContentRequest request = StudyContentRequest.builder()
                    .studyPostId(testStudyPost.getId())
                    .title("Pending Member Content")
                    .content("This is content created by a pending member.")
                    .contentType(ContentType.NOTICE)
                    .build();

            // when & then
            assertThrows(StudyContentException.UnauthorizedException.class,
                    () -> studyContentService.createStudyContent(request, pendingUserCamInfo.getId()));
        }

    }

    @Nested
    @DisplayName("스터디 컨텐츠 목록 조회 테스트")
    class GetStudyContentsTest{

        @Test
        @DisplayName("스터디 컨텐츠 목록 조회 성공")
        void getStudyContentsSuccess(){
            //given
            StudyContent additionalContent = StudyContent.builder()
                    .studyPost(testStudyPost)
                    .userCamInfo(leaderUserCamInfo)
                    .title("Additional Content")
                    .content("This is additional content.")
                    .studyContentType(ContentType.DISCUSSION)
                    .build();
            studyContentRepository.save(additionalContent);

            //when
            List<StudyContentListResponse> response = studyContentService.getStudyContentsByStudyPost(testStudyPost.getId(), leaderUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(2, response.size());
        }
    }

    @Nested
    @DisplayName("스터디 컨텐츠 수정 테스트")
    class UpdateStudyContentTest{

        @Test
        @DisplayName("스터디 컨텐츠 수정 성공")
        void updateStudyContentSuccess(){
            //given
            StudyContentRequest request = StudyContentRequest.builder()
                    .studyPostId(testStudyPost.getId())
                    .title("Updated Content")
                    .content("This is updated content.")
                    .contentType(ContentType.SCHEDULE)
                    .build();

            //when
            StudyContentResponse response = studyContentService.updateStudyContent(testStudyContent.getId(), request, leaderUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(request.getTitle(), response.getTitle());
            assertEquals(request.getContent(), response.getContent());
            assertEquals(request.getContentType(), response.getContentType());
        }

        @Test
        @DisplayName("스터디 컨텐츠 수정 실패 - 승인되지 않은 멤버")
        void updateStudyContentFailUnauthorized() {
            // given
            StudyContentRequest request = StudyContentRequest.builder()
                    .studyPostId(testStudyPost.getId())
                    .title("Unauthorized Update")
                    .content("This is an unauthorized update.")
                    .contentType(ContentType.NOTICE)
                    .build();

            // when & then
            assertThrows(StudyContentException.UnauthorizedException.class,
                    () -> studyContentService.updateStudyContent(testStudyContent.getId(), request, pendingUserCamInfo.getId()));
        }
    }

    @Nested
    @DisplayName("스터디 컨텐츠 삭제 테스트")
    class DeleteStudyContentTest{

        @Test
        @DisplayName("스터디 컨텐츠 삭제 성공 - 컨텐츠 작성자(리더)")
        void deleteStudyContentSuccessAuthorLeader() {
            // when
            studyContentService.deleteStudyContent(testStudyContent.getId(), leaderUserCamInfo.getId());

            // then
            // DB에서 삭제됐는지 확인
            Optional<StudyContent> deletedContent = studyContentRepository.findById(testStudyContent.getId());
            assertFalse(deletedContent.isPresent());
        }

        @Test
        @DisplayName("스터디 컨텐츠 삭제 성공 - 컨텐츠 작성자(일반 멤버)")
        void deleteStudyContentSuccessAuthorMember() {
            // given
            // 일반 멤버가 작성한 컨텐츠 생성
            StudyContent memberContent = StudyContent.builder()
                    .studyPost(testStudyPost)
                    .userCamInfo(memberUserCamInfo)
                    .title("Member Content")
                    .content("This is content created by a regular member.")
                    .studyContentType(ContentType.NOTICE)
                    .build();
            memberContent = studyContentRepository.save(memberContent);

            // when
            studyContentService.deleteStudyContent(memberContent.getId(), memberUserCamInfo.getId());

            // then
            // DB에서 삭제됐는지 확인
            Optional<StudyContent> deletedContent = studyContentRepository.findById(memberContent.getId());
            assertFalse(deletedContent.isPresent());
        }

        @Test
        @DisplayName("스터디 컨텐츠 삭제 실패 - 작성자도 리더도 아닌 멤버")
        void deleteStudyContentFailNotAuthor() {
            // when & then
            assertThrows(StudyContentException.UnauthorizedException.class,
                    () -> studyContentService.deleteStudyContent(testStudyContent.getId(), memberUserCamInfo.getId()));
        }

    }
}