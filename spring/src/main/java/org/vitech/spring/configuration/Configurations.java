package org.vitech.spring.configuration;

import io.awspring.cloud.sqs.config.SqsBootstrapConfiguration;
import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.vitech.spring.model.Person;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Import(SqsBootstrapConfiguration.class)
@Configuration
public class Configurations {
    @Bean
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory() {
        return SqsMessageListenerContainerFactory.builder()
                .sqsAsyncClient(sqsAsyncClient())
                .configure(options -> options.acknowledgementMode(AcknowledgementMode.MANUAL))
                .build();
    }

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .endpointOverride(URI.create("https://sqs.localhost.localstack.cloud:4566"))
                .build();
    }

    @Bean
    public SqsClient sqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create("https://sqs.localhost.localstack.cloud:4566"))
                .build();
    }


    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .endpointOverride(URI.create("https://s3.localhost.localstack.cloud:4566"))
                .build();
    }

    @Bean
    public DynamoDbEnhancedClient dynamoDbClient() {
        final var dynamoDbClient = DynamoDbClient.builder()
                .endpointOverride(URI.create("https://dynamodb.localhost.localstack.cloud:4566"))
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @Bean
    public DynamoDbTable<Person> personTable(DynamoDbEnhancedClient dynamoDbClient) {
        return dynamoDbClient.table("person", TableSchema.fromBean(Person.class));
    }
}
