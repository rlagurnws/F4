package f4.eventproducerservice.domain.priceupdater.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import f4.eventproducerservice.domain.priceupdater.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

  @Value(value = "${kafka.produce.topic.name}")
  private String topicName;

  private KafkaTemplate<String, ProductDTO> kafkaTemplate;

  @Autowired
  private ObjectMapper mapper;
  public Producer(KafkaTemplate<String, ProductDTO> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void produce(ProductDTO data) throws JsonProcessingException {
    kafkaTemplate.send(topicName,data.getProductName(),data);
  }
}
