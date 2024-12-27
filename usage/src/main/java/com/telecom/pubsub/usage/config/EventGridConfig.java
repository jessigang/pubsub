// File: publisher/src/main/java/com/telecom/pubsub/publisher/config/EventGridConfig.java
package com.telecom.pubsub.usage.config;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.messaging.eventgrid.EventGridPublisherClient;
import com.azure.messaging.eventgrid.EventGridPublisherClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EventGridConfig {
    @Value("${azure.eventgrid.endpoint}")
    private String endpoint;

    @Value("${azure.eventgrid.key}")
    private String key;

    @Bean
    public EventGridPublisherClient eventGridPublisherClient() {
        // 엔드포인트 정리 - 불필요한 특수문자 제거 및 URL 정규화
        String cleanEndpoint = endpoint.trim()
                .replaceAll("[\r\n\t]", "")  // 캐리지 리턴, 뉴라인, 탭 제거
                .replaceAll("\\s+", "");     // 모든 공백 제거

        // URL이 api/events로 끝나는지 확인 및 수정
        if (!cleanEndpoint.endsWith("/api/events")) {
            cleanEndpoint = cleanEndpoint.replaceAll("/+$", "") + "/api/events";
        }

        log.info("### Event Grid endpoint: {}", cleanEndpoint);

        return new EventGridPublisherClientBuilder()
                .endpoint(cleanEndpoint)
                .credential(new AzureKeyCredential(key))
                .buildEventGridEventPublisherClient();
    }
}