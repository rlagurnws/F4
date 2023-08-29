package f4.eventproducerservice.domain.priceupdater.persist.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "images")
public class ProductImageEntity {
  @Id
  private Long id;
  private Long productId;
  private String imageUrl;
}
