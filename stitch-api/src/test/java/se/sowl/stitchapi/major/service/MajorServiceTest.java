package se.sowl.stitchapi.major.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.major.dto.request.MajorRequest;
import se.sowl.stitchapi.major.dto.response.MajorDetailResponse;
import se.sowl.stitchapi.major.dto.response.MajorListResponse;
import se.sowl.stitchapi.major.dto.response.MajorResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.school.repository.MajorRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MajorServiceTest {

    @Autowired
    private MajorService majorService;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCamInfoRepository userCamInfoRepository;

    @Autowired
    private CampusRepository campusRepository;

    private User testUser;

    private Campus testCampus;

    private UserCamInfo testUserCamInfo;

    private Major testMajor;

    private Major anotherMajor;

    @BeforeEach
    void setUp() {
        testMajor = Major.builder()
                .name("컴퓨터공학과")
                .build();
        testMajor = majorRepository.save(testMajor);

        anotherMajor = Major.builder()
                .name("경영학과")
                .build();
        anotherMajor = majorRepository.save(anotherMajor);

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
    }


    @Nested
    @DisplayName("모든 전공 조회 테스트")
    class GetAllMajorsTest{
        @Test
        @DisplayName("모든 전공 조회 성공")
        void getAllMajorsSuccess(){
            majorRepository.deleteAll();

            //given
            for (int i = 1; i <= 5; i++) {
                Major major = Major.builder()
                        .name("테스트 전공" + i)
                        .build();
                majorRepository.save(major);
            }

            //when
            List<MajorListResponse> majorResponses = majorService.getAllMajors();

            //then
            assertEquals(5, majorResponses.size());
        }
    }

    @Nested
    @DisplayName("전공 상세 조회 테스트")
    class GetMajorDetailTest{
        @Test
        @DisplayName("전공 상세 조회 성공")
        void getMajorDetailSuccess(){
            //given
            Long majorId = testMajor.getId();

            //when
            MajorDetailResponse majorResponse = majorService.getMajorDetail(majorId);

            //then
            assertEquals(testMajor.getName(), majorResponse.getName());
        }

        @Test
        @DisplayName("존재하지 않는 전공 조회 시 예외 발생")
        void getMajorDetailFail(){
            //given
            Long nonExistentMajorId = 99999L;

            //when & then
            assertThrows(MajorException.MajorNotFoundException.class, () -> majorService.getMajorDetail(nonExistentMajorId));
        }
    }

    @Nested
    @DisplayName("전공 선택 테스트")
    class SelectMajorTest{

        @Test
        @DisplayName("처음 전공 선택 성공")
        void selectFirstMajorSuccess(){
            //given
            MajorRequest request = new MajorRequest(testMajor.getId(), testUser.getId());

            //when
            MajorResponse response = majorService.selectMajor(request);

            //then
            assertNotNull(response);
            assertEquals(testMajor.getId(), response.getId());
            assertEquals(testMajor.getName(), response.getName());

            // DB에 실제로 반영되었는지 확인
            UserCamInfo savedUserCamInfo = userCamInfoRepository.findByUser(testUser)
                    .orElseThrow();
            assertNotNull(savedUserCamInfo.getMajor());
            assertEquals(testMajor.getId(), savedUserCamInfo.getMajor().getId());
        }

        @Test
        @DisplayName("이미 선택한 전공이 있는 경우 전공 변경 성공")
        void changeMajorSuccess(){
            //given
            testUserCamInfo.setMajor(testMajor);
            userCamInfoRepository.save(testUserCamInfo);

            MajorRequest request = new MajorRequest(anotherMajor.getId(), testUser.getId());

            //when
            MajorResponse response = majorService.selectMajor(request);

            //then
            assertNotNull(response);
            assertEquals(anotherMajor.getId(), response.getId());
            assertEquals(anotherMajor.getName(), response.getName());

            // DB에 실제로 반영되었는지 확인
            UserCamInfo savedUserCamInfo = userCamInfoRepository.findByUser(testUser)
                    .orElseThrow();
            assertNotNull(savedUserCamInfo.getMajor());
            assertEquals(anotherMajor.getId(), savedUserCamInfo.getMajor().getId());

            // 이전 전공과의 관계가 제거되었는지 확인
            Major oldMajor = majorRepository.findById(testMajor.getId()).orElseThrow();
            assertFalse(oldMajor.getUserCamInfos().contains(savedUserCamInfo));
        }
    }
}