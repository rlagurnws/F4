package f4.auctionservice.domain.auction.controller;

import f4.auctionservice.domain.auction.service.impl.AuctionServiceImpl;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid/v1")
@Slf4j
public class AuctionController {

  private final AuctionServiceImpl service;

  @PostMapping("/order")
  public ResponseEntity<String> bid(@RequestHeader(name = "accessToken") String accessToken,
      @RequestBody String body) throws Exception {
    String result = service.bidOrder(accessToken, body);
    return new ResponseEntity<>(result,HttpStatus.OK);
  }

  @ExceptionHandler
  public ResponseEntity<String> handler(Exception e) {
    log.error("error : { }", e);
    return new ResponseEntity<>("시스템 오류 다시 시도해 주세요", HttpStatus.NOT_MODIFIED);
  }
}
