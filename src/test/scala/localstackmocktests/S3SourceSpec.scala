package localstackmocktests

import java.io.File

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}
import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService, ForAllTestContainer}
import org.scalatest.{FunSuite, BeforeAndAfter, Matchers}

class S3SourceSpec extends FunSuite with BeforeAndAfter with Matchers with ForAllTestContainer {

  override val container: DockerComposeContainer =
    DockerComposeContainer(new File("./docker-compose.yml"),
      exposedServices = Seq(
        ExposedService("localstack", 4566)
      ))

  private val mockS3Client: AmazonS3 = AmazonS3ClientBuilder.standard()
    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566","us-east-1"))
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("1234", "1234")))
    .withPathStyleAccessEnabled(true)
    .build()

  private val testDataBucket = "localstack"
  private val testDataKey = "test-data.json"

  before {
    mockS3Client.createBucket(testDataBucket)
  }


  test("copy files to bucket and read from them") {

    //Given
    val file = new File("src/test/resources/json-data/test-data.json")

    mockS3Client.putObject(testDataBucket, s"$testDataKey", file)

    val expected = Seq(
      """{"valueField":"valueField-1"}""",
      """{"valueField":"valueField-2"}""",
      """{"valueField":"valueField-3"}""").toStream

    //When
    val actual = S3Source.readJSONData(mockS3Client, testDataBucket, testDataKey)

    // Then
    actual shouldBe expected

  }

}
