package localstackmocktests

import java.io.File

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService, ForAllTestContainer}
import org.scalatest.{FunSuite, BeforeAndAfter, Matchers}

/*
Extending ForAllTestContainer means that Testcontainers will spin up a Docker container before tests are run and then
tear it down after tests finish
*/
class S3SourceSpec extends FunSuite with BeforeAndAfter with Matchers with ForAllTestContainer {

  //Expose the localhost service
  override val container: DockerComposeContainer =
    DockerComposeContainer(new File("./docker-compose.yml"),
      exposedServices = Seq(
        ExposedService("localstack", 4566)
      ))

  /*
  Create a standard AWS client
  Point it at the LocalStack endpoint
  Use it as if it was running against a real service
   */
  private val S3Client: AmazonS3 = AmazonS3ClientBuilder.standard()
    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566","us-east-1"))
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("1234", "1234")))
    .withPathStyleAccessEnabled(true)
    .build()

  private val testDataBucket = "my-bucket"
  private val testDataKey = "test-data.json"

  before {
    S3Client.createBucket(testDataBucket)
  }


  test("copy files to bucket and read from them") {

    //Given
    val file = new File("src/test/resources/json-data/test-data.json")

    S3Client.putObject(testDataBucket, s"$testDataKey", file)

    val expected = Seq(
      """{"valueField":"valueField-1"}""",
      """{"valueField":"valueField-2"}""",
      """{"valueField":"valueField-3"}""").toStream

    val reader = new S3Source(S3Client, testDataBucket, testDataKey)

    //When
    val actual = reader.readJSONData()

    // Then
    actual shouldBe expected

  }

}
