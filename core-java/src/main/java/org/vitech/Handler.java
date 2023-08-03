package org.vitech;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.HttpStatusCode;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.utils.StringUtils;

import java.net.URI;

public class Handler implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {
    private static final Gson GSON = new Gson();
    private static final S3Client S3 = S3Client.builder()
            .endpointOverride(URI.create("http://" + System.getenv("LOCALSTACK_HOSTNAME") + ":4566"))
            .build();

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context) {
        final var body = event.getBody();

        if (StringUtils.isNotBlank(body)) {
            final var person = GSON.fromJson(body, Person.class);

            final var putRequest = PutObjectRequest.builder()
                    .bucket(System.getenv("BUCKET"))
                    .key("person/" + person.getId() + ".json")
                    .build();

            S3.putObject(putRequest, RequestBody.fromString(body));

            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(HttpStatusCode.OK)
                    .withBody(body)
                    .build();
        } else {
            return APIGatewayV2HTTPResponse.builder()
                    .withStatusCode(HttpStatusCode.BAD_REQUEST)
                    .withBody("Id or body is not provided")
                    .build();
        }
    }
}
