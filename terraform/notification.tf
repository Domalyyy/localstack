resource "aws_s3_bucket" "local_bucket" {
  bucket = "local-bucket"
}
resource "aws_sqs_queue" "local_queue" {
  name = "local-queue"

  visibility_timeout_seconds = 30
  delay_seconds              = 90
  max_message_size           = 262144
  message_retention_seconds  = 60
  receive_wait_time_seconds  = 10

  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.local_queue_dl.arn
    maxReceiveCount     = 2
  })
}

resource "aws_sqs_queue" "local_queue_dl" {
  name = "local-queue-dl"
}

resource "aws_s3_bucket_notification" "local_notification" {
  depends_on = [
    aws_s3_bucket.local_bucket,
    aws_sqs_queue.local_queue
  ]

  bucket = aws_s3_bucket.local_bucket.id

  queue {
    queue_arn = aws_sqs_queue.local_queue.arn
    events    = ["s3:ObjectCreated:*"]
  }
}
