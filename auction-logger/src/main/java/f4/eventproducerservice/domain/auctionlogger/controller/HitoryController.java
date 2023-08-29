package f4.eventproducerservice.domain.auctionlogger.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import f4.eventproducerservice.domain.auctionlogger.dto.HistoryDTO;
import f4.eventproducerservice.domain.auctionlogger.dto.ProductDTO;
import f4.eventproducerservice.domain.auctionlogger.service.impl.HistoryServiceImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("history/")
public class HitoryController {

  private final HistoryServiceImpl service;

  @GetMapping
  public List<HistoryDTO> getHistory(@RequestHeader(name = "accessToken") String accessToken) throws Exception {
    return service.returnHistory(accessToken);
  }

  @ExceptionHandler
  public String handler(Exception e){
    return "시스템 오류입니당~";
  }
}
