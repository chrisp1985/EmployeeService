provider "aws" {
  region = "eu-west-2"
  access_key = var.access_key_value
  secret_key = var.secret_key_value
}

resource "aws_ecr_repository" "ecr_repository" {
  name                 = "chrisp1985_ecr_docker_repo"
  image_tag_mutability = "MUTABLE"
  image_scanning_configuration {
    scan_on_push = true
  }
}

## must be created AFTER the image is pushed to ecr
resource "aws_iam_role" "myroles" {
  name = "myroles"
  assume_role_policy = jsonencode({
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Service": "build.apprunner.amazonaws.com"
        },
        "Action": "sts:AssumeRole"
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "myrolespolicy" {
  role = aws_iam_role.myroles.id
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSAppRunnerServicePolicyForECRAccess"
}

resource "time_sleep" "waitrolecreate" {
  depends_on = [aws_iam_role.myroles]
  create_duration = "60s"
}

resource "aws_apprunner_service" "chrisp1985_app_runners" {
  service_name = "chrisp1985_app_runners"

  source_configuration {
    image_repository {
      image_configuration {
        port = "8000"
      }
      image_identifier      = "195571588534.dkr.ecr.eu-west-2.amazonaws.com/chrisp1985_ecr_docker_repo:7643f1a786b3b7741174d43b6867f114c5a19a74" ## TODO: REMOVE HARDCODING
      image_repository_type = "ECR"
    }

    authentication_configuration {
      access_role_arn = aws_iam_role.myroles.arn
    }
    auto_deployments_enabled = false
  }

  tags = {
    Name = "employee-service-app-runner"
  }
}