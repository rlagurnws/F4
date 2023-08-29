package f4.eventproducerservice.domain.priceupdater.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ProductDTO {

  private Long productId;
  private String productName;
  private String productImage;
  private String userEmail;
  private Long bidUserId;
  private String bidPrice;
  private String bidTime;
  private String bidStatus;
}
