package com.telecom.pubsub.usage.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationHistory {
    private String id;
    private String userId;
    private String notificationType;
    private String content;
    private LocalDateTime sentAt;
    private String status;
}
