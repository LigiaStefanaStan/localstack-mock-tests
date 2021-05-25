package localstackmocktests

import com.amazonaws.services.sns.AmazonSNS
import com.amazonaws.services.sns.model.{PublishRequest, PublishResult, SubscribeResult}

class SNSMessagePublisher(snsClient: AmazonSNS) {

  def subscribeToSQSQueue(topic: String, queueEndpoint: String): SubscribeResult = {
    snsClient.subscribe(topic, "sqs", queueEndpoint)
  }

  def publishToSNS(topic: String, message: String, subject: String): PublishResult = {
    snsClient.publish(new PublishRequest(topic, message, subject))
  }
}
