package f4.auctionservice.domain.auction.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import f4.auctionservice.domain.auction.dto.BidCheckRequestDto;
import f4.auctionservice.domain.auction.dto.KafkaDTO;
import f4.auctionservice.domain.auction.kafka.Producer;
import f4.auctionservice.domain.auction.service.AuctionService;
import f4.auctionservice.global.utils.Encryptor;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final Encryptor encryptor;
    private final ObjectMapper mapper;
    private final DateTimeFormatter fomatter;
    private final Producer kafkaProducer;
    @Value(value = "${mock.url}")
    private String mockUrl;

    @Override
    public String bidOrder(String accessToken, String body) throws Exception{
        Map<String, Object> token = decode(accessToken.split("\\.")[1]);
        Map<String, Object> bodyMap = mapper.readValue(body, Map.class);

        BidCheckRequestDto user = BidCheckRequestDto.builder()
            .arteUserId(Long.parseLong(token.get("id").toString()))
            .bidPrice(bodyMap.get("price").toString())
            .password(encryptor.encrypt(bodyMap.get("password").toString())).build();

        Map<String,Object> result = mapper.readValue(userCheck(user),Map.class);

        if (result.get("status").toString().equals("success")) {
            event(accessToken, body);
            return "입찰 요청 되었습니다.";
        } else {
            return ((Map<String, Object>)result.get("error")).get("message").toString();
        }
    }

    public void event(String accessToken, String body){
        kafkaProducer.produce(KafkaDTO.builder()
            .key("bidservice")
            .value(LocalDateTime.now().format(fomatter) +
                "&&" + accessToken.split("\\.")[1]+
                "&&" + body).build());
    }

    public Map<String, Object> decode(String info) throws JsonProcessingException {
        Base64.Decoder deco = Base64.getDecoder();
        Map<String, Object> token = mapper.readValue(new String(deco.decode(info)), Map.class);
        return token;
    }

    public String userCheck(BidCheckRequestDto user){
        URI uri = UriComponentsBuilder
            .fromUriString(mockUrl)
            .path("/woori/account/v1/bid/check")
            .encode()
            .build()
            .expand("Flature")
            .toUri();

        RestTemplate restTemplate = new RestTemplate();
        String responseEntity = restTemplate.postForObject(
            uri, user, String.class
        );
        return responseEntity;
    }

}
