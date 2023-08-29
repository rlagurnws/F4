package f4.eventproducerservice.domain.priceupdater.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import f4.eventproducerservice.domain.priceupdater.dto.MockUpdateRequestDTO;
import f4.eventproducerservice.domain.priceupdater.dto.ProductDTO;
import f4.eventproducerservice.domain.priceupdater.kafka.Producer;
import f4.eventproducerservice.domain.priceupdater.persist.entity.ProductEntity;
import f4.eventproducerservice.domain.priceupdater.persist.entity.ProductImageEntity;
import f4.eventproducerservice.domain.priceupdater.persist.repository.ProductImageRepository;
import f4.eventproducerservice.domain.priceupdater.persist.repository.ProductRepository;
import f4.eventproducerservice.domain.priceupdater.service.PriceService;
import java.net.URI;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class PriceServiceImpl implements PriceService {

  private final ProductRepository repository;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ProductImageRepository imageRepository;
  private final Producer producer;

  @Value(value = "${mock.url}")
  private String url;

  @Override
  @Transactional
  public void done(String info) throws Exception {
    Map<String, Object> token = decode(info.split("&&")[1]);
    Map<String, Object> bodyMap = objectMapper.readValue(info.split("&&")[2], Map.class);
    ProductEntity product = repository.findById(Long.parseLong(bodyMap.get("productId").toString())).get();
    ProductImageEntity image = imageRepository.findByProductIdOrderByIdAsc(product.getId()).get(0);

    ProductDTO sendProductInfo = ProductDTO.builder()
        .productId(product.getId())
        .productName(product.getName())
        .productImage(image.getImageUrl())
        .bidPrice(bodyMap.get("price").toString())
        .bidTime(info.split("&&")[0])
        .bidUserId(Long.parseLong(token.get("id").toString()))
        .userEmail(token.get("email").toString())
        .build();

    if (Long.parseLong(bodyMap.get("price").toString()) <= Long.parseLong(product.getBidPrice()) ||
        Long.parseLong(token.get("id").toString())==product.getBidUserId()) {
      sendProductInfo.setBidStatus("FAIL");
      producer.produce(sendProductInfo);
      return;
    }

//    MockUpdateRequestDTO mockDto;
//    if (product.getBidUserId() == null) {
//      mockDto = MockUpdateRequestDTO.builder()
//          .curUserId(Long.parseLong(token.get("id").toString()))
//          .curBidPrice(bodyMap.get("price").toString())
//          .option(1)
//          .build();
//    } else {
//      mockDto = MockUpdateRequestDTO.builder()
//          .preUserId(product.getBidUserId())
//          .preBidPrice(product.getBidPrice())
//          .curUserId(Long.parseLong(token.get("id").toString()))
//          .curBidPrice(bodyMap.get("price").toString())
//          .option(2)
//          .build();
//    }
//    Boolean result = restTemplateToMock(mockDto);

    if (true) {
      product.setBidPrice(sendProductInfo.getBidPrice());
      product.setBidUserId(sendProductInfo.getBidUserId());
      repository.save(product);
      sendProductInfo.setBidStatus("SUCCESS");
      producer.produce(sendProductInfo);
    } else {
      sendProductInfo.setBidStatus("ERROR");
      producer.produce(sendProductInfo);
      throw new Exception("mock error");
    }
  }

  public Map<String, Object> decode(String info) throws JsonProcessingException {
    Base64.Decoder deco = Base64.getDecoder();
    Map<String, Object> token = objectMapper.readValue(new String(deco.decode(info)), Map.class);
    return token;
  }

  public Boolean restTemplateToMock(MockUpdateRequestDTO mock) {
    URI uri = UriComponentsBuilder
        .fromUriString(url)
        .path("/woori/account/v1/bid")
        .encode()
        .build()
        .expand("Flature")
        .toUri();

    RestTemplate restTemplate = new RestTemplate();
    Boolean responseEntity = restTemplate.postForObject(
        uri, mock, Boolean.class
    );
    return responseEntity;
  }
}
