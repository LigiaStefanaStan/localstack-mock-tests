# localstack-mock-tests
A small repo to show how to set up LocalStack for testing apps that use AWS cloud services.

## Requirements
- Python (both Python 2.x and 3.x supported)
- pip
- Docker

## Installing LocalStack
Install via pip
```
pip install localstack
```

## Running LocalStack using `docker-compose`
Spin up docker: 
```
docker-compose up
```
Check the health of the endpoint: 
```
curl -X GET http://localhost:4566/health
```

### AWS S3 API
Export credentials: 
```
export AWS_ACCESS_KEY_ID=1234
export AWS_DEFAULT_REGION=us-east-1
export AWS_REGION=us-east-1
export AWS_SECRET_ACCESS_KEY=1234
```
                       
AWS CLI can be used to make calls to the S3 service.

List bucket content: 
```
aws --endpoint-url=http://localhost:4566 s3 ls s3://
```
Create bucket: 
```
aws --endpoint-url=http://localhost:4566 s3 mb s3://localstack
```

## Running LocalStack using test containers
Ensure the docker container is not up: 
```
bash docker-clean.sh
```
Run test

             
