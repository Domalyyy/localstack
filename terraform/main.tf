provider "aws" {
  region = "us-east-2"

  endpoints {
    s3  = "https://s3.localhost.localstack.cloud:4566"
    sqs = "https://sqs.localhost.localstack.cloud:4566"
  }
}
