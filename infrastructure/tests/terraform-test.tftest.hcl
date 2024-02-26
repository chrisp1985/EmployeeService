# ECR
run "ecr_test" {

  command = plan

  assert {
    condition     = aws_ecr_repository.ecr_repository.name == "ecr-docker-repository"
    error_message = "Name was not correct for ECR repo. Check whether this has been changed."
  }

}