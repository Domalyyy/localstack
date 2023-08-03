#!/bin/bash

awslocal s3 mb s3://lambda

#awslocal s3 mb s3://local-bucket
#
#awslocal sqs create-queue --queue-name local-queue
#
#awslocal s3api put-bucket-notification-configuration --bucket local-bucket --notification-configuration "{
#    \"QueueConfigurations\": [
#      {
#        \"QueueArn\": \"arn:aws:sqs:us-east-2:000000000000:local-queue\",
#        \"Events\": [
#          \"s3:ObjectCreated:*\"
#        ]
#      }
#    ]
#  }"