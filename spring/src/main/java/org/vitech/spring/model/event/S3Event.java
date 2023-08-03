package org.vitech.spring.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record S3Event(@JsonProperty("Records") List<NotificationRecord> records) {
}
