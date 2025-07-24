package se.sowl.stitchapi.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.notification.dto.NotificationListResponse;
import se.sowl.stitchapi.notification.dto.NotificationResponse;
import se.sowl.stitchapi.notification.service.NotificationService;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 사용자의 알림 목록 조회
     * - 최신순으로 정렬된 알림 목록을 반환
     * - 읽음/안읽음 상태 포함
     */
    @GetMapping("/list")
    public CommonResponse<List<NotificationListResponse>> getUserNotifications(
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        List<NotificationListResponse> responses = notificationService.getUserNotifications(userCamInfoId);
        return CommonResponse.ok(responses);
    }

    /**
     * 읽지 않은 알림 개수 조회
     * - 알림 뱃지 표시용
     * - 헤더나 네비게이션 바에서 활용
     */
    @GetMapping("/unread-count")
    public CommonResponse<Integer> getUnreadNotificationCount(
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        int count = notificationService.getUnreadNotificationCount(userCamInfoId);
        return CommonResponse.ok(count);
    }

    /**
     * 특정 알림 상세 조회
     * - 알림 클릭 시 상세 정보 확인
     * - 권한 검증 포함 (본인 알림만 조회 가능)
     */
    @GetMapping("/detail")
    public CommonResponse<NotificationResponse> getNotificationDetail(
            @RequestParam("notificationId") Long notificationId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        NotificationResponse response = notificationService.getNotificationDetail(notificationId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    /**
     * 개별 알림 읽음 처리
     * - 특정 알림 하나를 읽음 상태로 변경
     * - 알림 클릭 시 자동으로 읽음 처리되도록 활용
     */
    @PostMapping("/read")
    public CommonResponse<NotificationResponse> markOneRead(
            @RequestParam("notificationId") Long notificationId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        NotificationResponse response = notificationService.markOneAsRead(notificationId, userCamInfoId);
        return CommonResponse.ok(response);
    }

    /**
     * 모든 알림 읽음 처리
     * - "모두 읽음" 버튼 기능
     * - 읽지 않은 모든 알림을 일괄 읽음 처리
     */
    @PostMapping("/read/all")
    public CommonResponse<Void> markAllRead(
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        notificationService.markAllAsRead(userCamInfoId);
        return CommonResponse.ok(null);
    }

    /**
     * 알림 삭제
     * - 개별 알림을 완전히 삭제
     * - 알림 목록에서 제거하고 싶을 때 사용
     */
    @DeleteMapping("/delete")
    public CommonResponse<Void> deleteNotification(
            @RequestParam("notificationId") Long notificationId,
            @RequestParam("userCamInfoId") Long userCamInfoId
    ){
        notificationService.deleteNotification(notificationId, userCamInfoId);
        return CommonResponse.ok(null);
    }

    /**
     * 스터디 가입 신청 알림 생성
     * - 누군가 내 스터디에 가입 신청할 때 발생
     * - 스터디 리더/관리자에게 전송
     * - 실시간 SSE 알림과 함께 DB 저장
     */
    @PostMapping("/study-apply")
    public CommonResponse<NotificationResponse> createStudyApplyNotification(
            @RequestParam("receiverId") Long receiverId,
            @RequestParam("studyMemberId") Long studyMemberId
    ){
        NotificationResponse response = notificationService.createStudyApplyNotification(receiverId, studyMemberId);
        return CommonResponse.ok(response);
    }

    /**
     * 스터디 가입 승인 알림 생성
     * - 스터디 가입 신청이 승인되었을 때 발생
     * - 신청자에게 전송
     * - 승인 완료 후 자동으로 호출
     */
    @PostMapping("/study-approve")
    public CommonResponse<NotificationResponse> createStudyApproveNotification(
            @RequestParam("studyMemberId") Long studyMemberId
    ){
        NotificationResponse response = notificationService.createApproveNotification(studyMemberId);
        return CommonResponse.ok(response);
    }

    /**
     * 스터디 가입 거절 알림 생성
     * - 스터디 가입 신청이 거절되었을 때 발생
     * - 신청자에게 전송
     * - 거절 처리 후 자동으로 호출
     */
    @PostMapping("/study-reject")
    public CommonResponse<NotificationResponse> createStudyRejectNotification(
            @RequestParam("studyMemberId") Long studyMemberId
    ){
        NotificationResponse response = notificationService.createRejectNotification(studyMemberId);
        return CommonResponse.ok(response);
    }

    /**
     * 새 댓글 알림 생성
     * - 내 스터디 게시글에 댓글이 달렸을 때 발생
     * - 게시글 작성자에게 전송 (자신이 단 댓글 제외)
     * - 댓글 작성 후 자동으로 호출
     */
    @PostMapping("/study-comment")
    public CommonResponse<NotificationResponse> createNewCommentNotification(
            @RequestParam("commentId") Long commentId
    ){
        NotificationResponse response = notificationService.createNewCommentNotification(commentId);
        return CommonResponse.ok(response);
    }
}