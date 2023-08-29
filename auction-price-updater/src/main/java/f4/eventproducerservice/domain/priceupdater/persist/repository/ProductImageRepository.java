package f4.eventproducerservice.domain.priceupdater.persist.repository;

import f4.eventproducerservice.domain.priceupdater.persist.entity.ProductImageEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImageEntity,Long> {
  public List<ProductImageEntity> findByProductIdOrderByIdAsc(Long productId);
}
