package f4.eventproducerservice.domain.auctionlogger.persist.repository;

import f4.eventproducerservice.domain.auctionlogger.persist.entity.HistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity,Long> {

  public List<HistoryEntity> findByUserId(long userId);
}
