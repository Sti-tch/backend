package se.sowl.stitchapi.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import se.sowl.stitchapi.common.CommonResponse;
import se.sowl.stitchapi.notification.service.NotificationEmitterService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications/sse")
@RequiredArgsConstructor
public class NotificationSseController {
    private final NotificationEmitterService emitterService;

    /**
     * SSE 구독 엔드포인트
     * 클라이언트가 서버로부터 실시간 알림을 받기 위한 연결 설정
     *
     * @param userCamInfoId 사용자 ID
     * @return SseEmitter 객체
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestParam("userCamInfoId") Long userCamInfoId) {
        // SecurityConfig에서 이미 인증을 요구하므로, 여기서는 추가 확인 불필요
        return emitterService.subscribe(userCamInfoId);
    }

    /**
     * SSE 연결 상태 확인 엔드포인트 (관리자용)
     *
     * @return 연결 상태 정보
     */
    @GetMapping("/status")
    public CommonResponse<Map<String, Object>> getConnectionStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("connectedCount", emitterService.countEmitters());
        status.put("connectedUsers", emitterService.getConnectedUsers());

        return CommonResponse.ok(status);
    }
}