package f4.auctionservice.domain.auction.kafka;

import f4.auctionservice.domain.auction.dto.KafkaDTO;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

  @Value(value = "${kafka.topic.name}")
  private String topicName;
  private final KafkaTemplate<String, String> kafkaTemplate;

  public Producer(KafkaTemplate<String, String> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void produce(KafkaDTO data){
    kafkaTemplate.send(topicName,data.getKey(),data.getValue());
  }
}
