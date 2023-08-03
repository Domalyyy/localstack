resource "aws_sqs_queue" "local_queue" {
  name                      = "local-queue"
  delay_seconds             = 0
  max_message_size          = 2048
  message_retention_seconds = 60
  receive_wait_time_seconds = 5
  redrive_policy            = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.local_queue_deadletter.arn
    maxReceiveCount     = 2
  })
}

resource "aws_sqs_queue" "local_queue_deadletter" {
  name = "local-queue-dl"
}

resource "aws_s3_bucket" "local_bucket" {
  bucket = "local-bucket"
}


resource "aws_s3_bucket_notification" "input_notification" {
  depends_on = [
    aws_sqs_queue.local_queue,
    aws_s3_bucket.local_bucket
  ]

  bucket = aws_s3_bucket.local_bucket.id

  queue {
    queue_arn = aws_sqs_queue.local_queue.arn
    events    = ["s3:ObjectCreated:*"]
  }
}
