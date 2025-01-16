package se.sowl.stitchapi.user_cam_info.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.CampusException;
import se.sowl.stitchapi.exception.MajorException;
import se.sowl.stitchapi.exception.UserException;
import se.sowl.stitchapi.user_cam_info.dto.UserCamInfoResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.school.repository.MajorRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.repository.UserRepository;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserCamInfoServiceTest {

    @Autowired
    private UserCamInfoService userCamInfoService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCamInfoRepository userCamInfoRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private CampusRepository campusRepository;

    private User testUser;

    private Campus testCampus;

    private Major testMajor;

    @BeforeEach
    void setUp() {
        testCampus = Campus.builder()
                .name("서울대학교")
                .build();
        testCampus = campusRepository.save(testCampus);

        testMajor = Major.builder()
                .name("컴퓨터공학과")
                .build();
        testMajor = majorRepository.save(testMajor);

        testUser = User.builder()
                .email("test@snu.ac.kr")
                .name("테스트 유저")
                .campusCertified(false)
                .provider("kakao")
                .build();
        testUser = userRepository.save(testUser);

    }

    @Nested
    @DisplayName("유저 캠퍼스 정보 생성")
    class createUserCamInfo{

        @Test
        @DisplayName("유저 캠퍼스 정보 생성 성공")
        void createUserCamInfoSuccess(){

            //when
            UserCamInfoResponse userCamInfoResponse = userCamInfoService.createUserCamInfo(testUser.getEmail(), testCampus.getName(), testMajor.getId());

            //then
            assertAll(
                    "유저 캠퍼스 정보가 올바르게 저장되어야 합니다.",
                    () -> assertNotNull(userCamInfoResponse.getId()),
                    () -> assertEquals(testUser.getId(), userCamInfoResponse.getUserId()),
                    () -> assertEquals(testCampus.getId(), userCamInfoResponse.getCampusId()),
                    () -> assertEquals(testMajor.getId(), userCamInfoResponse.getMajorId()),
                    () -> assertEquals(testUser.getName(), userCamInfoResponse.getUserName()),
                    () -> assertEquals(testCampus.getName(), userCamInfoResponse.getCampusName()),
                    () -> assertEquals(testMajor.getName(), userCamInfoResponse.getMajorName()),
                    () -> assertEquals(testUser.getEmail(), userCamInfoResponse.getCampusEmail()),
                    () -> assertNotNull(userCamInfoResponse.getCreatedAt())
            );
        }

        @Test
        @DisplayName("존재하지 않는 유저로 생성 시도")
        void createWithNonExistentUser() {
            assertThrows(UserException.UserNotFoundException.class,
                    () -> userCamInfoService.createUserCamInfo(
                            "nonexistent@snu.ac.kr",
                            testCampus.getName(),
                            testMajor.getId()
                    ));
        }

        @Test
        @DisplayName("존재하지 않는 캠퍼스로 생성 시도")
        void createWithNonExistentCampus() {
            assertThrows(CampusException.CampusNotFoundException.class,
                    () -> userCamInfoService.createUserCamInfo(
                            testUser.getEmail(),
                            "존재하지않는대학교",
                            testMajor.getId()
                    ));
        }

        @Test
        @DisplayName("존재하지 않는 전공으로 생성 시도")
        void createWithNonExistentMajor() {
            assertThrows(MajorException.MajorNotFoundException.class,
                    () -> userCamInfoService.createUserCamInfo(
                            testUser.getEmail(),
                            testCampus.getName(),
                            9999L
                    ));
        }

        @Test
        @DisplayName("이미 인증된 유저로 생성 시도")
        void createWithAlreadyCertifiedUser() {
            // given
            testUser.certifyCampus();
            userRepository.save(testUser);

            assertThrows(UserException.UserAlreadyCertifiedException.class,
                    () -> userCamInfoService.createUserCamInfo(
                            testUser.getEmail(),
                            testCampus.getName(),
                            testMajor.getId()
                    ));
        }
    }


}