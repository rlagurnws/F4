package f4.eventproducerservice.domain.auctionlogger.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import f4.eventproducerservice.domain.auctionlogger.dto.HistoryDTO;
import f4.eventproducerservice.domain.auctionlogger.dto.ProductDTO;
import f4.eventproducerservice.domain.auctionlogger.persist.entity.HistoryEntity;
import f4.eventproducerservice.domain.auctionlogger.persist.repository.HistoryRepository;
import f4.eventproducerservice.domain.auctionlogger.service.HistoryService;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

  private final HistoryRepository repository;
  private final ObjectMapper objectMapper;
  private final ModelMapper modelMapper;

  //product 현재 정보 읽어서 Fail, Success 판단 후 저장
  @Override
  public void makeAndSave(String str) throws JsonProcessingException {
    System.out.println(str);
    Map<String, Object> product = objectMapper.readValue(str,Map.class);
    HistoryEntity history = HistoryEntity.builder()
        .productId(Long.parseLong(product.get("productId").toString()))
        .productName(product.get("productName").toString())
        .productImage(product.get("productImage").toString())
        .bidPrice(product.get("bidPrice").toString())
        .bidTime(product.get("bidTime").toString())
        .userId(Long.parseLong(product.get("bidUserId").toString()))
        .bidStatus(product.get("bidStatus").toString())
        .userEmail(product.get("userEmail").toString()).build();

    System.out.println(history);
    repository.save(history);
  }

  public Map<String, Object> decode(String info) throws JsonProcessingException {
    Base64.Decoder deco = Base64.getDecoder();
    Map<String, Object> token = objectMapper.readValue(new String(deco.decode(info)), Map.class);
    return token;
  }

  public List<HistoryDTO> returnHistory(String accessToken) throws Exception{
    Map<String, Object> map = decode(accessToken.split("\\.")[1]);
    if (map.get("role").toString().equals("user")) {
      return findHistory(Long.parseLong(map.get("id").toString()));
    }
    return findAllHistory();

  }

  //유저별 history List 반환
  public List<HistoryDTO> findHistory(long id) {
    return repository.findByUserId(id).stream().map(e -> modelMapper.map(e, HistoryDTO.class))
        .collect(Collectors.toList());
  }

  //전체 history list 반환
  public List<HistoryDTO> findAllHistory() {
    return repository.findAll().stream().map(e -> modelMapper.map(e, HistoryDTO.class)).collect(
        Collectors.toList());
  }
}
