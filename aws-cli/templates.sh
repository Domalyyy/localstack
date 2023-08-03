aws s3 mb s3://local-bucket --endpoint-url http://localhost:4566

aws s3api list-buckets --query "Buckets[].Name" --endpoint-url http://localhost:4566

aws sqs create-queue --queue-name local-queue --endpoint-url http://localhost:4566

aws sqs list-queues --endpoint-url http://localhost:4566

aws s3api put-bucket-notification-configuration --bucket local-bucket --endpoint-url http://localhost:4566 --notification-configuration "{
    \"QueueConfigurations\": [
      {
        \"QueueArn\": \"arn:aws:sqs:us-east-2:000000000000:local-queue\",
        \"Events\": [
          \"s3:ObjectCreated:*\"
        ]
      }
    ]
  }"

aws sqs receive-message --queue-url http://localhost:4566/000000000000/local-queue --endpoint-url http://localhost:4566

aws s3 cp ./file.json s3://local-bucket/file.json --endpoint-url http://localhost:4566
