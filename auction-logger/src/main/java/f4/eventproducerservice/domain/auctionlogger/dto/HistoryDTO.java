package f4.eventproducerservice.domain.auctionlogger.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HistoryDTO {
  private long productId;
  private String productName;
  private String productImage;
  private String bidPrice;
  private LocalDateTime bidTime;
  private String bidStatus;
}
