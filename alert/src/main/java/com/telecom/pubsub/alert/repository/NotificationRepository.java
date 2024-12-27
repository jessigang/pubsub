package com.telecom.pubsub.alert.repository;

import com.telecom.pubsub.alert.model.NotificationHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationHistory, String> {
}
