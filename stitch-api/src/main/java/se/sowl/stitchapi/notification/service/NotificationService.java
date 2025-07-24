package se.sowl.stitchapi.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.sowl.stitchapi.exception.*;
import se.sowl.stitchapi.notification.dto.NotificationEvent;
import se.sowl.stitchapi.notification.dto.NotificationListResponse;
import se.sowl.stitchapi.notification.dto.NotificationResponse;
import se.sowl.stitchdomain.notification.domain.Notification;
import se.sowl.stitchdomain.notification.enumm.NotificationType;
import se.sowl.stitchdomain.notification.repository.NotificationRepository;
import se.sowl.stitchdomain.study.domain.StudyMember;
import se.sowl.stitchdomain.study.domain.StudyPost;
import se.sowl.stitchdomain.study.domain.StudyPostComment;
import se.sowl.stitchdomain.study.repository.StudyMemberRepository;
import se.sowl.stitchdomain.study.repository.StudyPostCommentRepository;
import se.sowl.stitchdomain.user.domain.UserCamInfo;
import se.sowl.stitchdomain.user.repository.UserCamInfoRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserCamInfoRepository userCamInfoRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final StudyPostCommentRepository studyPostCommentRepository;
    private final NotificationEmitterService emitterService;

    // 사용자 알림 목록 조회
    @Transactional
    public List<NotificationListResponse> getUserNotifications(Long userCamInfoId) {
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        List<Notification> notificationListResponses = notificationRepository
                .findByUserCamInfoOrderByCreatedAtDesc(userCamInfo);

        return notificationListResponses.stream()
                .map(NotificationListResponse::from)
                .toList();
    }

    // 읽지 않은 알림 개수 조회
    @Transactional
    public int getUnreadNotificationCount(Long userCamInfoId){
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        // 읽지 않은 알림의 개수를 조회
        return notificationRepository.countByUserCamInfoAndIsReadFalse(userCamInfo);
    }

    // 알림 상세 조회
    @Transactional
    public NotificationResponse getNotificationDetail(Long notificationId, Long userCamInfoId){
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationException.NotificationNotFoundException::new);

        if (!notification.getUserCamInfo().getId().equals(userCamInfoId)) {
            throw new NotificationException.UnauthorizedException();
        }

        return NotificationResponse.from(notification);
    }

    // 알림 읽음 처리
    @Transactional
    public NotificationResponse markOneAsRead(Long notificationId, Long userCamInfoId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationException.NotificationNotFoundException::new);

        if (!notification.getUserCamInfo().getId().equals(userCamInfoId)) {
            throw new NotificationException.UnauthorizedException();
        }

        notification.markAsRead();
        return NotificationResponse.from(notification);
    }

    // 모든 알림 읽음 처리
    @Transactional
    public void markAllAsRead(Long userCamInfoId) {
        UserCamInfo userCamInfo = userCamInfoRepository.findById(userCamInfoId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        List<Notification> unReadNotifications = notificationRepository
                .findByUserCamInfoAndIsReadFalse(userCamInfo);

        unReadNotifications.forEach(Notification::markAsRead);
    }

    // 알림 삭제
    @Transactional
    public void deleteNotification(Long notificationId, Long userCamInfoId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(NotificationException.NotificationNotFoundException::new);

        if (!notification.getUserCamInfo().getId().equals(userCamInfoId)) {
            throw new NotificationException.UnauthorizedException();
        }
        notificationRepository.delete(notification);
    }

    // 알림 생성(스터디 가입 신청)
    @Transactional
    public NotificationResponse createStudyApplyNotification(Long receiverId, Long studyMemberId) {
        UserCamInfo receiver = userCamInfoRepository.findById(receiverId)
                .orElseThrow(UserCamInfoException.UserCamNotFoundException::new);

        StudyMember studyMember = studyMemberRepository.findById(studyMemberId)
                .orElseThrow(StudyMemberException.MemberNotFoundException::new);

        Notification notification = Notification.builder()
                .userCamInfo(receiver) // 알림 받는 사람: 스터디 관리자(리더)
                .message(studyMember.getUserCamInfo().getUser().getName() +
                        "님이 '" + studyMember.getStudyPost().getTitle() + "' 스터디에 가입 신청했습니다.")
                .link("/api/study-members/list?studyPostId=" + studyMember.getStudyPost().getId())
                .notificationType(NotificationType.STUDY_APPLY)
                .targetId(studyMember.getId()) // 알림의 대상 ID: 스터디 멤버 ID
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        NotificationResponse response = NotificationResponse.from(notification);

        // SSE 실시간 알림 전송
        emitterService.sendNotification(receiverId, NotificationEvent.from(notification));

        return response;
    }

    // 알림 생성(스터디 가입 승인)
    @Transactional
    public NotificationResponse createApproveNotification(Long studyMemberId) {
        StudyMember studyMember = studyMemberRepository.findById(studyMemberId)
                .orElseThrow(StudyMemberException.MemberNotFoundException::new);

        Notification notification = Notification.builder()
                .userCamInfo(studyMember.getUserCamInfo()) // 알림 받는 당사자: 신청자
                .message("'" + studyMember.getStudyPost().getTitle() + "' 스터디 가입이 승인되었습니다.")
                .link("/api/study-posts/" + studyMember.getStudyPost().getId())
                .notificationType(NotificationType.STUDY_APPLY_APPROVED)
                .targetId(studyMember.getStudyPost().getId())
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        NotificationResponse response = NotificationResponse.from(notification);

        // SSE 실시간 알림 전송
        emitterService.sendNotification(studyMember.getUserCamInfo().getId(), NotificationEvent.from(notification));

        return response;
    }

    // 알림 생성(스터디 가입 거절)
    @Transactional
    public NotificationResponse createRejectNotification(Long studyMemberId) {
        StudyMember studyMember = studyMemberRepository.findById(studyMemberId)
                .orElseThrow(StudyMemberException.MemberNotFoundException::new);

        Notification notification = Notification.builder()
                .userCamInfo(studyMember.getUserCamInfo()) // 알림 받는 당사자: 신청자
                .message("'" + studyMember.getStudyPost().getTitle() + "' 스터디 가입이 거절되었습니다.")
                .link("/api/study-posts/" + studyMember.getStudyPost().getId())
                .notificationType(NotificationType.STUDY_APPLY_REJECTED)
                .targetId(studyMember.getStudyPost().getId())
                .isRead(false)
                .build();

        notification = notificationRepository.save(notification);
        NotificationResponse response = NotificationResponse.from(notification);

        // SSE 실시간 알림 전송
        emitterService.sendNotification(studyMember.getUserCamInfo().getId(), NotificationEvent.from(notification));

        return response;
    }

    // 알림 생성(새 댓글)
    @Transactional
    public NotificationResponse createNewCommentNotification(Long commentId) {
        StudyPostComment comment = studyPostCommentRepository.findById(commentId)
                .orElseThrow(StudyPostException.StudyPostCommentNotFoundException::new);

        StudyPost studyPost = comment.getStudyPost();
        UserCamInfo commentWriter = comment.getUserCamInfo();

        // 게시글 작성자에게만 알림 (자신의 글에 자신이 댓글을 달면 알림 제외)
        if (!studyPost.getUserCamInfo().getId().equals(commentWriter.getId())) {
            Notification notification = Notification.builder()
                    .userCamInfo(studyPost.getUserCamInfo()) // 알림 받는 당사자: 게시글 작성자
                    .message(commentWriter.getUser().getName() +
                            "님이 '" + studyPost.getTitle() + "' 게시글에 댓글을 남겼습니다.")
                    .link("/api/study-posts/" + studyPost.getId() + "/comments")
                    .notificationType(NotificationType.STUDY_COMMENT_ADDED)
                    .targetId(comment.getId()) // 댓글 ID를 타겟으로 설정
                    .isRead(false)
                    .build();

            notification = notificationRepository.save(notification);
            NotificationResponse response = NotificationResponse.from(notification);

            // SSE 실시간 알림 전송
            emitterService.sendNotification(studyPost.getUserCamInfo().getId(), NotificationEvent.from(notification));

            return response;
        }

        return null; // 자신의 글에 자신이 댓글을 달면 알림 없음
    }
}