package org.vitech.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = ApplicationTests.TestConfig.class)
class ApplicationTests {

    private static final DockerImageName LOCALSTACK_IMAGE = DockerImageName.parse("localstack/localstack:2.2.0");

    @Container
    private static final LocalStackContainer LOCAL_STACK_CONTAINER = new LocalStackContainer(LOCALSTACK_IMAGE)
            .withEnv("AWS_DEFAULT_REGION", Region.US_EAST_2.toString())
            .withCopyFileToContainer(MountableFile.forClasspathResource("init-aws.sh", 0744), "/etc/localstack/init/ready.d/init-aws.sh")
            .withServices(
                    LocalStackContainer.Service.S3,
                    LocalStackContainer.Service.SQS,
                    LocalStackContainer.Service.DYNAMODB
            );

    @Autowired
    private SqsClient sqsClient;

    @Test
    void test() {
        final var queueUrls = sqsClient.listQueues().queueUrls();

        assertEquals(1, queueUrls.size());
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean("sqsClientTest")
        @Primary
        public SqsClient sqsClient() {
            return SqsClient.builder()
                    .endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.SQS))
                    .build();
        }

        @Bean("sqsAsyncClientTest")
        @Primary
        public SqsAsyncClient sqsAsyncClient() {
            return SqsAsyncClient.builder()
                    .endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.SQS))
                    .build();
        }

        @Bean("dynamoDbClientTest")
        @Primary
        public DynamoDbEnhancedClient dynamoDbClient() {
            final var dynamoDbClient = DynamoDbClient.builder()
                    .endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.DYNAMODB))
                    .build();

            return DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        }

        @Bean("s3ClientTest")
        @Primary
        public S3Client s3Client() {
            return S3Client.builder()
                    .endpointOverride(LOCAL_STACK_CONTAINER.getEndpointOverride(LocalStackContainer.Service.S3))
                    .build();
        }
    }
}
