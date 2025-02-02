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

    @Value("${azure.eventgrid.topic}")
    private String topicName;

    @Bean
    public EventGridPublisherClient eventGridPublisherClient() {
        log.info("Initializing Event Grid client for topic: {}", topicName);
        log.info("### Event Grid endpoint: {}", endpoint);

        return new EventGridPublisherClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(key))
                .buildEventGridEventPublisherClient();
    }
}