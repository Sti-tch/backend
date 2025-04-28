package se.sowl.stitchdomain.notification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import se.sowl.stitchdomain.notification.enumm.NotificationType;
import se.sowl.stitchdomain.user.domain.UserCamInfo;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_cam_info_id", nullable = false)
    private UserCamInfo userCamInfo; // 알림을 받는 사람 지칭

    @Column(nullable = false)
    private String message;

    private String link;

    @Column(nullable = false)
    private boolean isRead = false;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    private Long targetId; // 알림을 클릭했을 때 이동할 게시글, 댓글, 스터디원 프로필 등

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Builder
    public Notification(UserCamInfo userCamInfo, String message, String link, boolean isRead, NotificationType notificationType, Long targetId) {
        this.userCamInfo = userCamInfo;
        this.message = message;
        this.link = link;
        this.isRead = isRead;
        this.notificationType = notificationType;
        this.targetId = targetId;
    }

    public void markAsRead() { // 읽음 처리
        this.isRead = true;
    }
}
