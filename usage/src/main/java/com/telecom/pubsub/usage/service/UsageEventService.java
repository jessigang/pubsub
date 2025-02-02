package com.telecom.pubsub.usage.service;

import com.azure.core.util.BinaryData;
import com.azure.messaging.eventgrid.EventGridEvent;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.telecom.pubsub.common.event.UsageAlertEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UsageEventService {
    private final EventGridPublisherClient eventGridClient;
    private final ObjectMapper objectMapper;

    public void publishUsageEvent(String userId, double usage, double threshold) {
        try {
            log.info("## make event object");
            UsageAlertEvent event = UsageAlertEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .type("UsageAlert")
                .userId(userId)
                .usage(usage)
                .threshold(threshold)
                .timestamp(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .build();

            log.info("## generate event data");
            String eventData = objectMapper.writeValueAsString(event);
            log.info("event data: {}", eventData);

            //subject, eventType, data, dataVersion
            log.info("## subject, eventType, data, dataVersion");
            EventGridEvent gridEvent = new EventGridEvent(
                    "phones/usages",
                    "UsageAlert",
                    BinaryData.fromString(eventData),
                    "1.0"
            );

            log.info("## try to send event");
            eventGridClient.sendEvent(gridEvent);
            log.info("Usage event published for user: {}", userId);

        } catch (Exception e) {
            log.error("Error publishing usage event: {}", e.getMessage(), e);
            throw new RuntimeException("이벤트 발행 중 오류가 발생했습니다.", e);
        }
    }
}
