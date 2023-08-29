package f4.eventproducerservice.domain.auctionlogger.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import f4.eventproducerservice.domain.auctionlogger.dto.ProductDTO;
import javax.servlet.http.Cookie;
import org.springframework.web.bind.annotation.CookieValue;

public interface HistoryService {

  public void makeAndSave(String product) throws JsonProcessingException;
}
