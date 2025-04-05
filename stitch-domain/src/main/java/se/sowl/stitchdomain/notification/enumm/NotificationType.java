package se.sowl.stitchdomain.notification.enumm;

public enum NotificationType {
    STUDY_APPLY,          // 스터디 가입 신청 (리더에게 전송)
    STUDY_APPLY_APPROVED, // 가입 신청 승인 (신청자에게 전송)
    STUDY_APPLY_REJECTED, // 가입 신청 거절 (신청자에게 전송)
    STUDY_CONTENT_ADDED,  // 새 스터디 콘텐츠 추가 (모든 멤버에게 전송)
    STUDY_COMMENT_ADDED   // 새 댓글 추가 (게시글 작성자에게 전송)
}
