package se.sowl.stitchapi.notification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.NotificationException;
import se.sowl.stitchapi.exception.UserCamInfoException;
import se.sowl.stitchapi.notification.dto.NotificationListResponse;
import se.sowl.stitchdomain.notification.domain.Notification;
import se.sowl.stitchdomain.notification.enumm.NotificationType;
import se.sowl.stitchdomain.notification.repository.NotificationRepository;
import se.sowl.stitchdomain.school.domain.Campus;
import se.sowl.stitchdomain.school.repository.CampusRepository;
import se.sowl.stitchdomain.user.domain.User;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;
import se.sowl.stitchdomain.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserCamInfoRepository userCamInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CampusRepository campusRepository;

    private Campus testCampus;
    private UserCamInfo testUserCamInfo;
    private Notification testNotification;
    private User testUser;


    @BeforeEach
    void setUp() {

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

        testNotification = Notification.builder()
                .userCamInfo(testUserCamInfo)
                .message("테스트 알림")
                .isRead(false)
                .notificationType(NotificationType.STUDY_APPLY)
                .build();
        testNotification = notificationRepository.save(testNotification);
    }

    @Nested
    @DisplayName("사용자 알림 목록 조회")
    class GetUserNotifications {
        @Test
        @DisplayName("사용자 알림 목록 조회 성공")
        void getUserNotificationsSuccess(){
            //given
            Long userCamInfoId = testUserCamInfo.getId();

            //when
            List<NotificationListResponse> notificationListResponses = notificationService.getUserNotifications(userCamInfoId);

            //then
            assertFalse(notificationListResponses.isEmpty(), "알림 목록이 비어있지 않아야 합니다.");
            assertEquals(1, notificationListResponses.size(), "알림 목록의 크기가 1이어야 합니다.");
            assertEquals(testNotification.getMessage(), notificationListResponses.get(0).getMessage(), "알림 메시지가 일치해야 합니다.");
            assertEquals(testNotification.getId(), notificationListResponses.get(0).getId(), "알림 ID가 일치해야 합니다.");
        }

        @Test
        @DisplayName("사용자 알림 목록 조회 실패 - 사용자 정보 없음")
        void getUserNotificationsFail_UserNotFound() {
            //given
            Long userCamInfoId = 999L;


            //when & then
            assertThrows(UserCamInfoException.UserCamNotFoundException.class, () -> {
                notificationService.getUserNotifications(userCamInfoId);
            }, "존재하지 않는 사용자 ID로 알림 목록을 조회할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("알림 읽음 처리")
    class MarkAsRead {
        @Test
        @DisplayName("알림 읽음 처리 성공")
        void markAsReadSuccess() {
            //given
            Long notificationId = testNotification.getId();
            Long userCamInfoId = testUserCamInfo.getId();

            //when
            notificationService.markOneAsRead(notificationId, userCamInfoId);

            //then
            Notification updatedNotification = notificationRepository.findById(notificationId).orElseThrow();
            assertTrue(updatedNotification.isRead(), "알림이 읽음으로 처리되어야 합니다.");
        }

        @Test
        @DisplayName("알림 읽음 처리 실패 - 알림 정보 없음")
        void markAsReadFail_NotificationNotFound() {
            //given
            Long notificationId = 999L;
            Long userCamInfoId = testUserCamInfo.getId();

            //when & then
            assertThrows(NotificationException.NotificationNotFoundException.class, () -> {
                notificationService.markOneAsRead(notificationId, userCamInfoId);
            }, "존재하지 않는 알림 ID로 알림을 읽음 처리할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("모든 알림 읽음 처리")
    class MarkAllAsRead {
        @Test
        @DisplayName("모든 알림 읽음 처리 성공")
        void markAllAsReadSuccess() {
            //given
            Long userCamInfoId = testUserCamInfo.getId();

            //when
            notificationService.markAllAsRead(userCamInfoId);

            //then
            List<Notification> notifications = notificationRepository.findByUserCamInfoAndIsReadFalse(testUserCamInfo);
            assertTrue(notifications.isEmpty(), "모든 알림이 읽음으로 처리되어야 합니다.");
        }

        @Test
        @DisplayName("모든 알림 읽음 처리 실패 - 사용자 정보 없음")
        void markAllAsReadFail_UserNotFound() {
            //given
            Long userCamInfoId = 999L;

            //when & then
            assertThrows(UserCamInfoException.UserCamNotFoundException.class, () -> {
                notificationService.markAllAsRead(userCamInfoId);
            }, "존재하지 않는 사용자 ID로 모든 알림을 읽음 처리할 수 없습니다.");
        }
    }
}