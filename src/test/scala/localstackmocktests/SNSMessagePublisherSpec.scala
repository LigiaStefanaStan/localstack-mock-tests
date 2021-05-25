package localstackmocktests

import com.amazonaws.auth.{AWSStaticCredentialsProvider, BasicAWSCredentials}
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.sns.{AmazonSNS, AmazonSNSClientBuilder}
import com.amazonaws.services.sqs.{AmazonSQS, AmazonSQSClientBuilder}
import com.dimafeng.testcontainers.{DockerComposeContainer, ExposedService, ForAllTestContainer}
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers}

import java.io.File

class SNSMessagePublisherSpec extends FunSuite with BeforeAndAfter with Matchers with ForAllTestContainer {

  override val container: DockerComposeContainer =
    DockerComposeContainer(new File("./docker-compose.yml"),
      exposedServices = Seq(
        ExposedService("localstack", 4566)
      ))

  private val snsClient: AmazonSNS = AmazonSNSClientBuilder.standard()
    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566","us-east-1"))
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("1234", "1234")))
    .build()

  private val sqsClient: AmazonSQS = AmazonSQSClientBuilder.standard()
    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:4566","us-east-1"))
    .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("1234", "1234")))
    .build()

  before {
    snsClient.createTopic("test-topic")
    sqsClient.createQueue("queue")
  }

  test("consume messages from SQS queue") {

    //Given
    val expected = "\"Message\": \"message\""

    val publisher = new SNSMessagePublisher(snsClient)

    publisher.subscribeToSQSQueue("arn:aws:sns:us-east-1:000000000000:test-topic", "http://localhost:4566/000000000000/queue")
    publisher.publishToSNS("arn:aws:sns:us-east-1:000000000000:test-topic", "message", "UPDATE")

    //When
    val message = sqsClient.receiveMessage("http://localhost:4566/000000000000/queue").toString

    //"[{Messages: [{MessageId: c87864b2-a33f-1f18-df0b-b632ae64c073,ReceiptHandle: jsgxaqdznthagfmfykhauieulzczfoclkqtorohsjlcwcaqxegymwxlaulpmeyctpmmbxsjwgjvwuxdqclcaysijzkttdwhltddhwmtzcmmtaycslrambuyqnloixwrtavmemoobmduxgfceyxpkijruphvhhsejocnxexamxrygfxcqzhggkcwmb,MD5OfBody: 320b5e632d4c8a6307505bbec662c927,Body: {"Type": "Notification", "MessageId": "50872cd9-3067-4716-8f6c-bc8a251dc0e0", "TopicArn": "arn:aws:sns:us-east-1:000000000000:test-topic", "Message": "message", "Timestamp": "2021-05-24T18:09:21.630Z", "SignatureVersion": "1", "Signature": "EXAMPLEpH+..", "SigningCertURL": "https://sns.us-east-1.amazonaws.com/SimpleNotificationService-0000000000000000000000.pem", "Subject": "UPDATE"},Attributes: {},MessageAttributes: {}}]}]"

    // Then
    val pattern = "\"Message\": \"([a-zA-Z])+([a-zA-Z])\"".r
    val actual = pattern.findAllIn(message).mkString

    actual shouldBe expected

  }
}
