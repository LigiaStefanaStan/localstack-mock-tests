package localstackmocktests

import java.io.{BufferedReader, InputStreamReader}

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

object S3Source extends App {

  private val amazonS3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build()
  private val bucket = "bucket"
  private val key = "key"

  readJSONData(amazonS3Client, bucket, key)

  def readJSONData(amazonS3Client: AmazonS3, bucket: String, key: String): Seq[String] = {
    val obj = amazonS3Client.getObject(bucket, key)
    val reader = new BufferedReader(new InputStreamReader(obj.getObjectContent))

    Iterator.continually(reader.readLine).takeWhile(_ != null).toSeq
  }

}
