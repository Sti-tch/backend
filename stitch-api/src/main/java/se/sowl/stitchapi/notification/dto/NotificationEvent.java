package se.sowl.stitchapi.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import se.sowl.stitchdomain.notification.domain.Notification;
import se.sowl.stitchdomain.notification.enumm.NotificationType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationEvent {
    private Long id;
    private Long userCamInfoId;
    private String message;
    private String link;
    private boolean isRead;
    private NotificationType notificationType;
    private Long targetId;
    private LocalDateTime createdAt;

    // Notification 엔티티로부터 NotificationEvent 생성
    public static NotificationEvent from(Notification notification) {
        return NotificationEvent.builder()
                .id(notification.getId())
                .userCamInfoId(notification.getUserCamInfo().getId())
                .message(notification.getMessage())
                .link(notification.getLink())
                .isRead(notification.isRead())
                .notificationType(notification.getNotificationType())
                .targetId(notification.getTargetId())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}