package com.telecom.pubsub.push.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document(collection = "notification_history")
public class NotificationHistory {
    @Id
    private String id;
    private String userId;
    private String notificationType;  // SMS, PUSH 등
    private String content;           // 알림 내용
    private LocalDateTime sentAt;     // 발송 시간
    private String status;            // 발송 상태
}
