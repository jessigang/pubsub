package com.telecom.pubsub.push.service;

import com.telecom.pubsub.common.event.UsageAlertEvent;
import com.telecom.pubsub.push.model.NotificationHistory;
import com.telecom.pubsub.push.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void processEvent(UsageAlertEvent event) {
        try {
            // 알림 내용 생성
            String content = createAlertContent(event);

            // Send SMS
            sendPush(event.getUserId(), content);

            // MongoDB에 알림 이력 저장
            NotificationHistory history = NotificationHistory.builder()
                    .userId(event.getUserId())
                    .notificationType("SMS")
                    .content(content)
                    .sentAt(LocalDateTime.now())
                    .status("SENT")
                    .build();

            notificationRepository.save(history);
            log.info("알림 이력이 저장되었습니다. userId: {}", event.getUserId());

        } catch (Exception e) {
            log.error("이벤트 처리 중 오류 발생: {}", e.getMessage(), e);
            throw new RuntimeException("이벤트 처리 중 오류가 발생했습니다.", e);
        }
    }

    private String createAlertContent(UsageAlertEvent event) {
        return String.format(
            "[데이터 사용량 알림] 현재 사용량이 %.1fGB로 %.1fGB 한도를 초과했습니다.",
            event.getUsage(),
            event.getThreshold()
        );
    }

    private void saveNotificationHistory(String type, UsageAlertEvent event, String content) {
        NotificationHistory history = NotificationHistory.builder()
            .userId(event.getUserId())
            .notificationType(type)
            .content(content)
            .sentAt(LocalDateTime.now())
            .status("SENT")
            .build();

        notificationRepository.save(history);
        log.info("{} notification history saved for user: {}", type, event.getUserId());
    }
    public void sendPush(String userId, String content) {
        // Push 알림 발송 로직 구현 (실습을 위해 로그만 출력)
        log.info("Push notification sent to user {}: {}", userId, content);
    }
}
