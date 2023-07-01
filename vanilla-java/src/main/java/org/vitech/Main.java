package org.vitech;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.net.URI;

public class Main {
    public static void main(final String[] args) {
        final var s3 = S3Client.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .build();

        final var buckets = s3.listBuckets().buckets();
        for (Bucket bucket : buckets) {
            System.out.println(bucket.name());
        }
    }
}