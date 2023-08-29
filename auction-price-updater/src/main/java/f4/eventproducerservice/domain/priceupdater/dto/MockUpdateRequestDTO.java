package f4.eventproducerservice.domain.priceupdater.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class MockUpdateRequestDTO {

  private Long preUserId;
  private String preBidPrice;
  private Long curUserId;
  private String curBidPrice;
  private int option;
}
