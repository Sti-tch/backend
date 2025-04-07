package se.sowl.stitchdomain.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.sowl.stitchdomain.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
