resource "aws_dynamodb_table" "person" {
  name           = "person"
  read_capacity  = "10"
  write_capacity = "10"

  attribute {
    name = "id"
    type = "S"
  }

  hash_key = "id"
}
