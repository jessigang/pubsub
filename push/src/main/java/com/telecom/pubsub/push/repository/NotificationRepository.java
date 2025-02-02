package com.telecom.pubsub.push.repository;

import com.telecom.pubsub.push.model.NotificationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationHistory, String> {
}
