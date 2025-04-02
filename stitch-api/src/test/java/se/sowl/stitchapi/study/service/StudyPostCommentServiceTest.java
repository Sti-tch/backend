package se.sowl.stitchapi.study.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.StudyPostException;
import se.sowl.stitchapi.study.dto.request.StudyPostCommentRequest;
import se.sowl.stitchapi.study.dto.response.StudyPostCommentResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.school.repository.MajorRepository;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.domain.StudyPostComment;
import se.sowl.stitchdomain.study.repository.StudyPostCommentRepository;
import se.sowl.stitchdomain.study.repository.StudyPostRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudyPostCommentServiceTest {

    @Autowired
    private StudyPostCommentService studyPostCommentService;

    @Autowired
    private StudyPostCommentRepository studyPostCommentRepository;

    @Autowired
    private StudyPostRepository studyPostRepository;

    @Autowired
    private CampusRepository campusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCamInfoRepository userCamInfoRepository;

    @Autowired
    private MajorRepository majorRepository;

    private Major testMajor;
    private User testUser;
    private Campus testCampus;
    private UserCamInfo testUserCamInfo;
    private StudyPost testStudyPost;
    private StudyPostComment testStudyPostComment;

    @BeforeEach
    void setUp(){
        testCampus = Campus.builder()
                .name("테스트 캠퍼스")
                .domain("test.ac.kr")
                .build();
        testCampus = campusRepository.save(testCampus);

        testUser = User.builder()
                .name("테스트 유저")
                .email("test@email.com")
                .nickname("test")
                .provider("kakao")
                .campusCertified(true)
                .build();
        testUser = userRepository.save(testUser);

        testUserCamInfo = UserCamInfo.builder()
                .user(testUser)
                .campus(testCampus)
                .campusEmail("test@skhu.ac.kr")
                .build();
        testUserCamInfo = userCamInfoRepository.save(testUserCamInfo);

        testMajor = Major.builder()
                .name("컴퓨터공학과")
                .build();
        testMajor = majorRepository.save(testMajor);

        testStudyPost = StudyPost.builder()
                .title("테스트 스터디 포스트")
                .content("테스트 스터디 포스트 내용")
                .userCamInfo(testUserCamInfo)
                .build();
        testStudyPost = studyPostRepository.save(testStudyPost);

        testStudyPostComment = StudyPostComment.builder()
                .content("테스트 스터디 포스트 댓글")
                .userCamInfo(testUserCamInfo)
                .studyPost(testStudyPost)
                .build();
        testStudyPostComment = studyPostCommentRepository.save(testStudyPostComment);
    }

    @Nested
    @DisplayName("댓글 조회 테스트")
    class GetCommentsTest{
        @Test
        @DisplayName("스터디 포스트 ID로 댓글 조회")
        void getCommentsByPostId() {
            // given
            Long studyPostId = testStudyPost.getId();

            // when
            List<StudyPostCommentResponse> comments = studyPostCommentService.getCommentsByPostId(studyPostId);

            // then
            assertFalse(comments.isEmpty());
            assertEquals(1, comments.size());
            assertEquals(testStudyPostComment.getContent(), comments.get(0).getContent());
        }

        @Test
        @DisplayName("존재하지 않는 게시글 ID로 댓글 조회")
        void getCommentsNonExistPostIdFail(){
            // given
            Long nonExistPostId = 999L;

            // when & then
            assertThrows(StudyPostException.StudyPostNotFoundException.class, () -> {
                studyPostCommentService.getCommentsByPostId(nonExistPostId);
            });
        }
    }

    @Nested
    @DisplayName("댓글 생성 테스트")
    class CreateCommentTest{
        @Test
        @DisplayName("댓글 생성 성공")
        void createCommentSuccess(){
            //given
            StudyPostCommentRequest request = new StudyPostCommentRequest("테스트 댓글 내용", testStudyPost.getId());

            //when
            StudyPostCommentResponse response = studyPostCommentService.createStudyComments(request, testUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(request.getContent(), response.getContent());
            assertEquals(testUserCamInfo.getId(), response.getUserCamInfoId());
        }

        @Test
        @DisplayName("존재하지 않는 게시글에 댓글 생성 시 예외 발생")
        void createCommentFail(){
            //given
            StudyPostCommentRequest request = new StudyPostCommentRequest("테스트 댓글 내용", 999L);

            //when & then
            assertThrows(StudyPostException.StudyPostNotFoundException.class, () -> {
                studyPostCommentService.createStudyComments(request, testUserCamInfo.getId());
            });
        }
    }

    @Nested
    @DisplayName("댓글 수정 테스트")
    class UpdateCommentTest{
        @Test
        @DisplayName("댓글 수정 성공")
        void updateCommentSuccess(){
            //given
            Long commentId = testStudyPostComment.getId();
            StudyPostCommentRequest request = new StudyPostCommentRequest("수정된 댓글 내용", testStudyPost.getId());

            //when
            StudyPostCommentResponse response = studyPostCommentService.updateStudyPostComment(commentId, request, testUserCamInfo.getId());

            //then
            assertNotNull(response);
            assertEquals(request.getContent(), response.getContent());
        }

        @Test
        @DisplayName("존재하지 않는 댓글 수정 시 예외 발생")
        void updateCommentFail(){
            //given
            Long nonExistCommentId = 999L;
            StudyPostCommentRequest request = new StudyPostCommentRequest("수정된 댓글 내용", testStudyPost.getId());

            //when & then
            assertThrows(StudyPostException.StudyPostCommentNotFoundException.class, () -> {
                studyPostCommentService.updateStudyPostComment(nonExistCommentId, request, testUserCamInfo.getId());
            });
        }
    }

    @Nested
    @DisplayName("댓글 삭제 테스트")
    class DeleteCommentTest{
        @Test
        @DisplayName("댓글 삭제 성공")
        void deleteCommentSuccess(){
            //given
            Long commentId = testStudyPostComment.getId();

            //when
            studyPostCommentService.deleteStudyPostComment(commentId, testUserCamInfo.getId());

            //then
            assertFalse(studyPostCommentRepository.findById(commentId).isPresent());
        }

        @Test
        @DisplayName("존재하지 않는 댓글 삭제 시 예외 발생")
        void deleteCommentFail(){
            //given
            Long nonExistCommentId = 999L;

            //when & then
            assertThrows(StudyPostException.StudyPostCommentNotFoundException.class, () -> {
                studyPostCommentService.deleteStudyPostComment(nonExistCommentId, testUserCamInfo.getId());
            });
        }
    }
}