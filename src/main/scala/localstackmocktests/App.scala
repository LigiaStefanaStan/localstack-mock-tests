package localstackmocktests

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3ClientBuilder


object App {

  def main(args: Array[String]): Unit = {

    val amazonS3Client = AmazonS3ClientBuilder.standard().withRegion(Regions.US_EAST_1).build()

    val bucket = "bucket"
    val key = "key"

    val reader = new S3Source(amazonS3Client, bucket, key)
    reader.readJSONData()
  }

}
