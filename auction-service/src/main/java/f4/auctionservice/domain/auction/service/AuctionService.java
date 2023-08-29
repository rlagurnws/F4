package f4.auctionservice.domain.auction.service;

import javax.servlet.http.Cookie;
import org.springframework.http.RequestEntity;

public interface AuctionService {

  public String bidOrder(String accessToken, String body) throws Exception;
}
