package se.sowl.stitchapi.study.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.StudyPostException;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.study.dto.request.StudyPostRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostDetailResponse;
import se.sowl.stitchapi.study.dto.response.StudyPostResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.school.repository.MajorRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyPostServiceTest {

    @Autowired
    private StudyPostService studyPostService;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    private UserCamInfoRepository userCamInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private MajorRepository majorRepository;

    private UserCamInfo testUserCamInfo;

    private UserCamInfo otherUserCamInfo;

    private StudyPost testStudyPost;

    private User testUser;

    private User otherUser;

    private Campus testCampus;

    private Major testMajor;

    private StudyPostResponse createdPost; // 생성된 게시글

    @BeforeEach
    void setUp() {

        testUser = User.builder()
                .name("testUser")
                .nickname("testNickname")
                .email("test@naver.com")
                .provider("kaKao")
                .campusCertified(true)
                .build();
        testUser = userRepository.save(testUser);

        testCampus = Campus.builder()
                .name("testCampus")
                .domain("test.ac.kr")
                .build();
        testCampus = campusRepository.save(testCampus);

        testMajor = Major.builder()
                .name("testMajor")
                .build();
        testMajor = majorRepository.save(testMajor);

        testUserCamInfo = UserCamInfo.builder()
                .user(testUser)
                .campus(testCampus)
                .major(testMajor)
                .campusEmail("test@skhu.ac.kr")
                .build();
        testUserCamInfo = userCamInfoRepository.save(testUserCamInfo);

        // 다른 사용자 생성
        otherUser = User.builder()
                .name("다른사용자")
                .nickname("다른닉네임")
                .email("other@example.com")
                .provider("kakao")
                .campusCertified(true)
                .build();
        userRepository.save(otherUser);

        otherUserCamInfo = UserCamInfo.builder()
                .user(otherUser)
                .campus(testCampus)
                .major(testMajor)
                .campusEmail("other@test.ac.kr")
                .build();
        userCamInfoRepository.save(otherUserCamInfo);


        StudyPostRequest createRequest = new StudyPostRequest(
                "원본 제목",
                "원본 내용",
                StudyStatus.RECRUITING);

        createdPost = studyPostService.createStudyPost(createRequest, testUserCamInfo.getId());
    }

    @Nested
    @DisplayName("스터디 게시글 생성 테스트")
    class CreateStudyPostTest{
        @Test
        @DisplayName("스터디 게시글 생성 성공")
        void createStudyPost(){
            // given
            StudyPostRequest studyPostRequest = new StudyPostRequest(
                    "testTitle",
                    "testContent",
                    StudyStatus.RECRUITING);

            // when
            StudyPostResponse response = studyPostService.createStudyPost(studyPostRequest, testUserCamInfo.getId());

            // then
            assertNotNull(response);
            assertEquals("testTitle", response.getTitle());
            assertEquals("testContent", response.getContent());
            assertEquals(StudyStatus.RECRUITING, response.getStudyStatus());

            List<StudyMember> members = studyMemberRepository.findByStudyPostId(response.getId());
            assertFalse(members.isEmpty());
            assertEquals(1, members.size());
            assertEquals(MemberRole.LEADER, members.get(0).getMemberRole());
            assertEquals(MemberStatus.APPROVED, members.get(0).getMemberStatus());
            assertEquals(testUserCamInfo.getId(), members.get(0).getUserCamInfo().getId());
        }

        @Test
        @DisplayName("존재하지 않는 userCamInfo로 게시글 생성 시 예외 발생")
        void createStudyPost_UserCamInfoNotFound() {
            // given
            StudyPostRequest studyPostRequest = new StudyPostRequest(
                    "testTitle",
                    "testContent",
                    StudyStatus.RECRUITING);
            Long nonExistentUserCamInfoId = 9999L;

            // when & then
            assertThrows(UserCamInfoException.UserCamNotFoundException.class, () -> {
                studyPostService.createStudyPost(studyPostRequest, nonExistentUserCamInfoId);
            });
        }
    }

    @Nested
    @DisplayName("스터디 게시글 상세 조회 테스트")
    class GetStudyPostDetailTest{
        @Test
        @DisplayName("스터디 게시글 상세 조회 성공")
        void getStudyPostDetail(){
            // given
            StudyPostRequest studyPostRequest = new StudyPostRequest(
                    "testTitle",
                    "testContent",
                    StudyStatus.RECRUITING);

            StudyPostResponse createdPost = studyPostService.createStudyPost(studyPostRequest, testUserCamInfo.getId());

            // when
            StudyPostDetailResponse response  = studyPostService.getStudyPostDetail(createdPost.getId(),testUserCamInfo.getId());

            // then
            assertNotNull(response);
            assertEquals("testTitle", response.getTitle());
            assertEquals("testContent", response.getContent());
            assertEquals(StudyStatus.RECRUITING, response.getStudyStatus());
        }

        @Test
        @DisplayName("모집 중인 게시글은 작성자가 아니어도 조회 가능")
        void getStudyPostDetail_NonAuthor_Recruiting_Success() {
            // given
            // 게시글 생성
            StudyPostRequest request = new StudyPostRequest(
                    "테스트 스터디",
                    "테스트 내용입니다",
                    StudyStatus.RECRUITING);

            StudyPostResponse createdPost = studyPostService.createStudyPost(request, testUserCamInfo.getId());


            // when
            StudyPostDetailResponse response = studyPostService.getStudyPostDetail(
                    createdPost.getId(), otherUserCamInfo.getId());

            // then
            assertNotNull(response);
            assertEquals(request.getTitle(), response.getTitle());
        }

        @Test
        @DisplayName("진행 중인 게시글은 작성자가 아니어도 조회 가능")
        void getStudyPostDetail_NonAuthor_InProgress_Success() {
            // given
            // 게시글 생성
            StudyPostRequest request = new StudyPostRequest(
                    "테스트 스터디",
                    "테스트 내용입니다",
                    StudyStatus.IN_PROGRESS);

            StudyPostResponse createdPost = studyPostService.createStudyPost(request, testUserCamInfo.getId());


            // when
            StudyPostDetailResponse response = studyPostService.getStudyPostDetail(
                    createdPost.getId(), otherUserCamInfo.getId());

            // then
            assertNotNull(response);
            assertEquals(request.getTitle(), response.getTitle());
        }
    }
    @Nested
    @DisplayName("스터디 게시글 수정 테스트")
    class UpdateStudyPostTest{
        @Test
        @DisplayName("게시글 작성자가 수정 시 성공")
        void updateStudyPost_ByAuthor_Success(){
            // given
            StudyPostRequest request = new StudyPostRequest(
                    "수정된 제목",
                    "수정된 내용",
                    StudyStatus.IN_PROGRESS);

            // when
            StudyPostResponse response = studyPostService.updateStudyPost(request,
                    createdPost.getId(), testUserCamInfo.getId());

            // then
            assertNotNull(response);
            assertEquals("수정된 제목", response.getTitle());
            assertEquals("수정된 내용", response.getContent());
            assertEquals(StudyStatus.IN_PROGRESS, response.getStudyStatus());

            // DB에도 반영되었는지 확인
            StudyPost updatedPost = studyPostRepository.findById(createdPost.getId()).orElseThrow();
            assertEquals("수정된 제목", updatedPost.getTitle());
            assertEquals("수정된 내용", updatedPost.getContent());
            assertEquals(StudyStatus.IN_PROGRESS, updatedPost.getStudyStatus());
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 수정 시 예외 발생")
        void updateStudyPost_ByNonAuthor_ThrowsException() {
            // given
            StudyPostRequest updateRequest = new StudyPostRequest(
                    "수정된 제목",
                    "수정된 내용",
                    StudyStatus.IN_PROGRESS);

            // when & then
            assertThrows(StudyPostException.UnauthorizedException.class, () -> {
                studyPostService.updateStudyPost(
                        updateRequest, createdPost.getId(), otherUserCamInfo.getId());
            });

            // DB에 반영되지 않았는지 확인
            StudyPost unchangedPost = studyPostRepository.findById(createdPost.getId()).orElseThrow();
            assertEquals("원본 제목", unchangedPost.getTitle());
            assertEquals("원본 내용", unchangedPost.getContent());
            assertEquals(StudyStatus.RECRUITING, unchangedPost.getStudyStatus());
        }
    }
    @Nested
    @DisplayName("스터디 게시글 삭제 테스트")
    class DeleteStudyPostTest{
        @Test
        @DisplayName("게시글 작성자가 삭제 시 성공")
        void deleteStudyPost_ByAuthor_Success() {
            // when
            studyPostService.deleteStudyPost(
                    createdPost.getId(), testUserCamInfo.getId());

            // DB에서 실제로 삭제되었는지 확인
            assertTrue(studyPostRepository.findById(createdPost.getId()).isEmpty());
        }

        @Test
        @DisplayName("작성자가 아닌 사용자가 삭제 시 예외 발생")
        void deleteStudyPost_ByNonAuthor_ThrowsException() {
            // when & then
            assertThrows(StudyPostException.UnauthorizedException.class, () -> {
                studyPostService.deleteStudyPost(
                        createdPost.getId(), otherUserCamInfo.getId());
            });

            // DB에서 삭제되지 않았는지 확인
            assertTrue(studyPostRepository.findById(createdPost.getId()).isPresent());
        }

    }
}