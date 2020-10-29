package localstackmocktests

import java.io.{BufferedReader, InputStreamReader}

import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.{AmazonS3, AmazonS3ClientBuilder}

class S3Source(amazonS3Client: AmazonS3, bucket: String, key: String) {

  def readJSONData(): Seq[String] = {
    val obj = amazonS3Client.getObject(bucket, key)
    val reader = new BufferedReader(new InputStreamReader(obj.getObjectContent))

    Iterator.continually(reader.readLine).takeWhile(_ != null).toSeq
  }

}
