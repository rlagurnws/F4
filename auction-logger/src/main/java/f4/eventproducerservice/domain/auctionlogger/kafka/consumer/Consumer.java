package f4.eventproducerservice.domain.auctionlogger.kafka.consumer;

import f4.eventproducerservice.domain.auctionlogger.dto.ProductDTO;
import f4.eventproducerservice.domain.auctionlogger.service.impl.HistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class Consumer {

  private final HistoryServiceImpl service;

//  kafka event 구독, 로그 기록
  @KafkaListener(topics = "${kafka.topic.name}")
  public void consume(ConsumerRecord<String, String> record) {
      try{
        service.makeAndSave(record.value());
      }catch (Exception e){
        log.error("error : { }",e);
      }
  }


}
