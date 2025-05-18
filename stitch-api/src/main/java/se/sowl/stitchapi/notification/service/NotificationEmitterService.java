package se.sowl.stitchapi.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import se.sowl.stitchapi.notification.dto.NotificationEvent;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
public class NotificationEmitterService {
    // 사용자 ID를 키로 SseEmitter를 저장하는 Map
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    // SSE 연결 타임아웃 시간 (1시간)
    private static final long TIMEOUT = 60 * 60 * 1000L;

    /**
     * 새로운 SSE 연결 생성 및 등록
     * @param userCamInfoId 사용자 ID
     * @return SseEmitter 객체
     */
    public SseEmitter subscribe(Long userCamInfoId) {
        // 타임아웃 설정으로 SseEmitter 생성
        SseEmitter emitter = new SseEmitter(TIMEOUT);

        // 완료, 타임아웃, 오류 시 콜백 등록
        emitter.onCompletion(() -> {
            log.info("SSE 연결 완료: userId={}", userCamInfoId);
            removeEmitter(userCamInfoId);
        });

        emitter.onTimeout(() -> {
            log.info("SSE 연결 타임아웃: userId={}", userCamInfoId);
            emitter.complete();
            removeEmitter(userCamInfoId);
        });

        emitter.onError((e) -> {
            log.error("SSE 연결 오류: userId={}, error={}", userCamInfoId, e.getMessage());
            emitter.complete();
            removeEmitter(userCamInfoId);
        });

        // 기존 연결이 있으면 제거 후 새로운 연결 등록
        removeEmitter(userCamInfoId);
        emitters.put(userCamInfoId, emitter);
        log.info("SSE 연결 생성: userId={}, 현재 연결 수={}", userCamInfoId, emitters.size());

        // 연결 확인용 초기 이벤트 전송
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("Connected successfully"));
        } catch (IOException e) {
            log.error("초기 이벤트 전송 실패: userId={}, error={}", userCamInfoId, e.getMessage());
            emitter.complete();
            removeEmitter(userCamInfoId);
        }

        return emitter;
    }

    /**
     * SseEmitter 제거
     * @param userCamInfoId 사용자 ID
     */
    public void removeEmitter(Long userCamInfoId) {
        SseEmitter removed = emitters.remove(userCamInfoId);
        if (removed != null) {
            log.info("SSE 연결 제거: userId={}, 남은 연결 수={}", userCamInfoId, emitters.size());
        }
    }

    /**
     * 특정 사용자에게 알림 이벤트 전송
     * @param userCamInfoId 수신자 ID
     * @param event 전송할 알림 이벤트
     */
    public void sendNotification(Long userCamInfoId, NotificationEvent event) {
        SseEmitter emitter = emitters.get(userCamInfoId);
        if (emitter == null) {
            log.debug("알림 전송 대상 없음 (연결 없음): userId={}", userCamInfoId);
            return;
        }

        try {
            emitter.send(SseEmitter.event()
                    .name("notification")
                    .data(event));
            log.info("알림 전송 완료: userId={}, notificationId={}, type={}",
                    userCamInfoId, event.getId(), event.getNotificationType());
        } catch (IOException e) {
            log.error("알림 전송 실패: userId={}, error={}", userCamInfoId, e.getMessage());
            emitter.complete();
            removeEmitter(userCamInfoId);
        }
    }

    /**
     * 현재 연결된 클라이언트 수 조회
     * @return 연결된 클라이언트 수
     */
    public int countEmitters() {
        return emitters.size();
    }

    /**
     * 현재 연결된 사용자 ID 목록 조회
     * @return 연결된 사용자 ID 목록
     */
    public List<Long> getConnectedUsers() {
        return emitters.keySet().stream().collect(Collectors.toList());
    }
}