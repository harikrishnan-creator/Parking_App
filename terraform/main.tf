terraform {
  required_version = ">= 1.5"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-2"
}

locals {
  cluster_name = "eks-dr-cluster"
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.8.1"

  name = "eks-dr-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["ap-south-1a", "ap-south-1b", "ap-south-1c"]
  private_subnets = ["10.0.1.0/24", "10.0.2.0/24", "10.0.3.0/24"]
  public_subnets  = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]

  enable_nat_gateway = true
  single_nat_gateway = false

  public_subnet_tags = {
    "kubernetes.io/role/elb" = 1
  }

  private_subnet_tags = {
    "kubernetes.io/role/internal-elb" = 1
  }
}

module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "20.15.0"

  cluster_name    = local.cluster_name
  cluster_version = "1.30"

  subnet_ids                   = module.vpc.private_subnets
  vpc_id                       = module.vpc.vpc_id
  cluster_endpoint_public_access = true

  eks_managed_node_groups = {
    general = {
      instance_types = ["t3.small"]
      min_size       = 3
      max_size       = 6
      desired_size   = 3

      capacity_type = "ON_DEMAND"
    }
  }
}

resource "aws_db_subnet_group" "postgres" {
  name       = "postgres-subnet-group"
  subnet_ids = module.vpc.private_subnets
}

resource "aws_security_group" "postgres" {
  name_prefix = "postgres-sg"
  vpc_id      = module.vpc.vpc_id

  ingress {
    from_port   = 5432
    to_port     = 5432
    protocol    = "tcp"
    cidr_blocks = ["10.0.0.0/16"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_db_instance" "postgres" {
  identifier             = "dr-postgres-db"
  engine                 = "postgres"
  engine_version         = "16"
  instance_class         = "db.t3.small"
  allocated_storage      = 20

  db_name  = "appdb"
  username = "postgres"
  password = "ChangeMe123!"

  multi_az               = true
  storage_encrypted      = true
  backup_retention_period = 7
  skip_final_snapshot    = true

  db_subnet_group_name   = aws_db_subnet_group.postgres.name
  vpc_security_group_ids = [aws_security_group.postgres.id]
}

output "cluster_name" {
  value = module.eks.cluster_name
}

output "rds_endpoint" {
  value = aws_db_instance.postgres.address
}
