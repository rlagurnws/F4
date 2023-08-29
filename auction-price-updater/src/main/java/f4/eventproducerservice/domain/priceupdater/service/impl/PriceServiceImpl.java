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
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceServiceImpl implements PriceService {

  private final ProductRepository repository;
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final ProductImageRepository imageRepository;
  private final Producer producer;

  @Value(value = "${mock.url}")
  private String url;
  private Logger logger = LoggerFactory.getLogger(PriceServiceImpl.class);

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

    MockUpdateRequestDTO mockDto;

    if (product.getBidUserId() == null && Long.parseLong(product.getBidPrice()) < Long.parseLong(
        sendProductInfo.getBidPrice())) {
      mockDto = option1Builder(sendProductInfo);
    } else if (product.getBidUserId() != null && !product.getBidUserId().equals(sendProductInfo.getBidUserId())
        && Long.parseLong(product.getBidPrice()) < Long.parseLong(sendProductInfo.getBidPrice())) {
      mockDto = option2Builder(sendProductInfo, product);
    } else {
      sendProductInfo.setBidStatus("FAIL");
      producer.produce(sendProductInfo);
      return;
    }

    try{
      restTemplateToMock(mockDto);
      product.setBidPrice(sendProductInfo.getBidPrice());
      product.setBidUserId(sendProductInfo.getBidUserId());
      repository.save(product);
      sendProductInfo.setBidStatus("SUCCESS");
      producer.produce(sendProductInfo);
    }catch(Exception e){
      sendProductInfo.setBidStatus("ERROR");
      producer.produce(sendProductInfo);
      logger.error("error : { }", e);
      throw new Exception("mock error");
    }
  }

  public Map<String, Object> decode(String info) throws JsonProcessingException {
    Base64.Decoder deco = Base64.getDecoder();
    Map<String, Object> token = objectMapper.readValue(new String(deco.decode(info)), Map.class);
    return token;
  }

  public void restTemplateToMock(MockUpdateRequestDTO mock) {
    URI uri = UriComponentsBuilder
        .fromUriString(url)
        .path("/woori/account/v1/bid")
        .encode()
        .build()
        .expand("Flature")
        .toUri();

    RestTemplate restTemplate = new RestTemplate();
    restTemplate.put(uri, mock);
  }

  public MockUpdateRequestDTO option1Builder(ProductDTO sendProductInfo) {
    return MockUpdateRequestDTO.builder()
        .curUserId(sendProductInfo.getBidUserId())
        .curBidPrice(sendProductInfo.getBidPrice())
        .option(1)
        .build();
  }

  public MockUpdateRequestDTO option2Builder(ProductDTO sendProductInfo, ProductEntity product) {
    return MockUpdateRequestDTO.builder()
        .preUserId(product.getBidUserId())
        .preBidPrice(product.getBidPrice())
        .curUserId(sendProductInfo.getBidUserId())
        .curBidPrice(sendProductInfo.getBidPrice())
        .option(2)
        .build();
  }
}
