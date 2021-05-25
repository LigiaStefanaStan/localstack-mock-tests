package localstackmocktests

import com.amazonaws.services.sqs.AmazonSQS
import com.amazonaws.services.sqs.model.CreateQueueResult

class SQSQueue(sqsClient: AmazonSQS) {

  def createSQSQueue(queue: String): CreateQueueResult = {
    sqsClient.createQueue(queue)
  }
}
