package f4.eventproducerservice.domain.priceupdater.persist.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "product")
public class ProductEntity {

  @Id
  private Long id;
  private String identifier;
  private String name;
  private String artist;
  private String country;
  private String description;
  private Date completionDate;
  private String size;
  private String theme;
  private String style;
  private String technique;
  private String mediums;
  private String bidPrice;
  private String auctionStatus;
  private Date auctionStartTime;
  private Date auctionEndTime;
  private Long bidUserId;
}
