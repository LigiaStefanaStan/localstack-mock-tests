package localstackmocktests

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.sns.AmazonSNSClient
import com.amazonaws.services.sqs.AmazonSQSClient


object App {

  def main(args: Array[String]): Unit = {

    val amazonS3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build()
    val amazonSNSClient = AmazonSNSClient.builder().withRegion(Regions.US_EAST_1).build()
    val amazonSQSClient = AmazonSQSClient.builder().withRegion(Regions.US_EAST_1).build()

    val bucket = "bucket"
    val key = "key"

    val reader = new S3Source(amazonS3Client, bucket, key)
    reader.readJSONData()

    val queueCreator = new SQSQueue(amazonSQSClient)
    queueCreator.createSQSQueue("queue")

    val publisher = new SNSMessagePublisher(amazonSNSClient)
    publisher.subscribeToSQSQueue("topic", "queue")
    publisher.publishToSNS("topic", "Ligia is awesome", "UPDATE")
  }

}
