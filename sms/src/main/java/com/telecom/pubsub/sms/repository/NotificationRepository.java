package com.telecom.pubsub.sms.repository;

import com.telecom.pubsub.sms.model.NotificationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationHistory, String> {
}
