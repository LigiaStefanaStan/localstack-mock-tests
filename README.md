# localstack-tests
A small repo to show how to set up LocalStack for testing apps that use AWS cloud services.

[LocalStack PowerPoint Presentation](https://github.com/LigiaStefanaStan/localstack-tests/blob/f0b10fcc0d25cb76a2f237a3611f62bbbc7bc528/LocalStack-LStan.pdf)

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

Create bucket: 
```
aws --endpoint-url=http://localhost:4566 s3 mb s3://my-bucket
```
Copy local files to the bucket
```
aws --endpoint-url=http://localhost:4566 s3 cp src/test/resources/json-data/test-data.json s3://my-bucket
```
List bucket content: 
```
aws --endpoint-url=http://localhost:4566 s3 ls s3://my-bucket
```

## Running LocalStack using test containers
Clean up the docker container: 
```
bash docker-clean.sh
```
Run test
```
sbt test
```

             
