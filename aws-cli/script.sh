#!/bin/bash

aws s3 mb s3://mybucket --endpoint-url http://localhost:4566

aws s3api list-buckets --query "Buckets[].Name" --endpoint-url http://localhost:4566

aws sqs create-queue --queue-name aws-cli-queue --endpoint-url http://localhost:4566

aws sqs list-queues --endpoint-url http://localhost:4566
