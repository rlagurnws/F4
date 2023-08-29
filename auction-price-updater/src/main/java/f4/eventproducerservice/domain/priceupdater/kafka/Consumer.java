package f4.eventproducerservice.domain.priceupdater.kafka;

import f4.eventproducerservice.domain.priceupdater.service.impl.PriceServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class Consumer {

  private final PriceServiceImpl priceService;

  @KafkaListener(topics = "${kafka.topic.name}")
  public void consume(ConsumerRecord<String, String> record) {
    try {
      priceService.done(record.value());
    } catch (Exception e) {
      log.error("error : { }", e);
    }
  }
}
