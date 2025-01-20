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
    class createUserCamInfo{

        @Test
        @DisplayName("유저 캠퍼스 정보 생성 성공 - 기존 캠퍼스")
        void createUserCamInfoExistingSuccess(){
            //given
            Campus campus = Campus.builder()
                    .name("서울대학교")
                    .build();
            campusRepository.save(campus);

            String campusEmail = "test@snu.ac.kr";

            //when
            UserCamInfoResponse userCamInfoResponse = userCamInfoService.createUserCamInfo(
                    testUser.getId(),
                    campusEmail,
                    campus.getName()
            );

            //then
            assertAll(
                    "유저 캠퍼스 정보가 올바르게 저장되어야 합니다.",
                    () -> assertNotNull(userCamInfoResponse.getId()),
                    () -> assertEquals(testUser.getId(), userCamInfoResponse.getUserId()),
                    () -> assertEquals(campus.getId(), userCamInfoResponse.getCampusId()),
                    () -> assertEquals(testUser.getName(), userCamInfoResponse.getUserName()),
                    () -> assertEquals(campus.getName(), userCamInfoResponse.getCampusName()),
                    () -> assertEquals(campusEmail, userCamInfoResponse.getCampusEmail()),
                    () -> assertTrue(testUser.isCampusCertified()),  // 인증 상태 확인
                    () -> assertNotNull(userCamInfoResponse.getCreatedAt())
            );
        }

        @Test
        @DisplayName("유저 캠퍼스 정보 생성 성공 - 새로운 캠퍼스")
        void createUserCamInfoNewSuccess(){
            //given
            String campusName = "서울대학교";
            String campusEmail = "test@skhu.ac.kr";

            //when
            UserCamInfoResponse userCamInfoResponse = userCamInfoService.createUserCamInfo(
                    testUser.getId(),
                    campusEmail,
                    campusName
            );

            //then
            assertAll(
                    "새로운 캠퍼스로 유저 캠퍼스 정보가 올바르게 저장되어야 합니다.",
                    () -> assertNotNull(userCamInfoResponse.getId()),
                    () -> assertEquals(testUser.getId(), userCamInfoResponse.getUserId()),
                    () -> assertNotNull(userCamInfoResponse.getCampusId()),
                    () -> assertEquals(testUser.getName(), userCamInfoResponse.getUserName()),
                    () -> assertEquals(campusName, userCamInfoResponse.getCampusName()),
                    () -> assertEquals(campusEmail, userCamInfoResponse.getCampusEmail()),
                    () -> assertTrue(testUser.isCampusCertified()),
                    () -> assertNotNull(userCamInfoResponse.getCreatedAt())
            );

        }

    }


}