package f4.eventproducerservice.domain.auctionlogger.service;

import f4.eventproducerservice.domain.auctionlogger.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductApi {

  @GetMapping(value = "/product/{id}")
  public ProductDTO getProduct(@PathVariable long id);
}
