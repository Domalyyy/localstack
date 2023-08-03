package org.vitech.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.s3.S3PathMatchingResourcePatternResolver;
import io.awspring.cloud.sqs.annotation.SqsListener;
import io.awspring.cloud.sqs.listener.acknowledgement.Acknowledgement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.vitech.spring.model.Person;
import org.vitech.spring.model.event.NotificationRecord;
import org.vitech.spring.model.event.S3Event;
import org.vitech.spring.storage.DynamoDb;
import software.amazon.awssdk.services.s3.S3Client;

import java.nio.charset.StandardCharsets;

@Component
public class Cloud {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ResourcePatternResolver resourcePatternResolver;
    private final DynamoDb dynamoDb;

    @Autowired
    public Cloud(S3Client s3Client, ApplicationContext applicationContext, DynamoDb dynamoDb) {
        this.dynamoDb = dynamoDb;
        this.resourcePatternResolver = new S3PathMatchingResourcePatternResolver(s3Client, applicationContext);
    }

    @SqsListener("https://localhost:4566/000000000000/local-queue")
    public void listen(S3Event message, Acknowledgement acknowledgement) {
        try {
            for (NotificationRecord notificationRecord : message.records()) {
                final var s3Info = notificationRecord.s3();
                final var resource = resourcePatternResolver.getResource("s3://" + s3Info.bucket().name() + "/" + s3Info.object().key());
                final var contentAsString = resource.getContentAsString(StandardCharsets.UTF_8);
                final var person = MAPPER.readValue(contentAsString, Person.class);
                dynamoDb.save(person);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            acknowledgement.acknowledge();

        }
    }
}
