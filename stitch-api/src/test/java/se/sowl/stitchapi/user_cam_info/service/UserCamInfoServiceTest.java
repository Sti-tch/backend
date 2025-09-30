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
import se.sowl.stitchapi.user_cam_info.dto.response.UserCamInfoResponse;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.domain.Major;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.school.repository.MajorRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
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

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .email("test@email.com")
                .name("테스트 유저")
                .campusCertified(false)
                .provider("kakao")
                .build();
        testUser = userRepository.save(testUser);
    }

    @Nested
    @DisplayName("유저 캠퍼스 정보 생성")
    class createUserCamInfo {

        @Test
        @DisplayName("유저 캠퍼스 정보 생성 성공")
        void createUserCamInfoSuccess() {
            //given
            String univName = "성공회대학교";  // DB에 존재하는 대학
            String campusEmail = "test@skhu.ac.kr";  // 실제 대학 도메인

            //when
            UserCamInfoResponse userCamInfoResponse = userCamInfoService.createUserCamInfo(
                    testUser.getId(),
                    campusEmail,
                    univName
            );

            //then
            assertAll(
                    "유저 캠퍼스 정보가 올바르게 저장되어야 합니다.",
                    () -> assertNotNull(userCamInfoResponse.getId()),
                    () -> assertEquals(testUser.getId(), userCamInfoResponse.getUserId()),
                    () -> assertNotNull(userCamInfoResponse.getCampusId()),
                    () -> assertEquals(testUser.getName(), userCamInfoResponse.getUserName()),
                    () -> assertEquals(univName, userCamInfoResponse.getCampusName()),
                    () -> assertEquals(campusEmail, userCamInfoResponse.getCampusEmail()),
                    () -> assertTrue(testUser.isCampusCertified()),
                    () -> assertNotNull(userCamInfoResponse.getCreatedAt())
            );
        }

        @Test
        @DisplayName("유저 캠퍼스 정보 생성 실패 - 잘못된 이메일 형식")
        void createUserCamInfoFailInvalidEmail() {
            //given
            String univName = "성공회대학교";
            String invalidEmail = "invalid-email";

            //when & then
            assertThrows(UserException.InvalidEmailFormatException.class,
                    () -> userCamInfoService.createUserCamInfo(
                            testUser.getId(),
                            invalidEmail,
                            univName
                    ));
        }

        @Test
        @DisplayName("유저 캠퍼스 정보 생성 실패 - 잘못된 도메인")
        void createUserCamInfoFailInvalidDomain() {
            //given
            String univName = "성공회대학교";
            String wrongDomainEmail = "test@wrong-domain.com";

            //when & then
            assertThrows(UserException.InvalidCampusEmailDomainException.class,
                    () -> userCamInfoService.createUserCamInfo(
                            testUser.getId(),
                            wrongDomainEmail,
                            univName
                    ));
        }

        @Test
        @DisplayName("유저 캠퍼스 정보 생성 실패 - 존재하지 않는 대학")
        void createUserCamInfoFailCampusNotFound() {
            //given
            String nonExistentCampus = "존재하지 않는 대학교";
            String email = "test@test.ac.kr";

            //when & then
            assertThrows(UserException.CampusNotFoundException.class,
                    () -> userCamInfoService.createUserCamInfo(
                            testUser.getId(),
                            email,
                            nonExistentCampus
                    ));
        }

        @Test
        @DisplayName("유저 캠퍼스 정보 생성 성공 - 서브도메인 포함")
        void createUserCamInfoSuccessWithSubdomain() {
            //given
            String univName = "성공회대학교";
            String campusEmail = "test@office.skhu.ac.kr";  // 서브도메인 포함

            //when
            UserCamInfoResponse userCamInfoResponse = userCamInfoService.createUserCamInfo(
                    testUser.getId(),
                    campusEmail,
                    univName
            );

            //then
            assertAll(
                    "서브도메인이 포함된 이메일로도 유저 캠퍼스 정보가 올바르게 저장되어야 합니다.",
                    () -> assertNotNull(userCamInfoResponse.getId()),
                    () -> assertEquals(testUser.getId(), userCamInfoResponse.getUserId()),
                    () -> assertNotNull(userCamInfoResponse.getCampusId()),
                    () -> assertEquals(campusEmail, userCamInfoResponse.getCampusEmail())
            );
        }
    }
}