package app.storage;

import org.cg.base.HistoryItem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoryItemRepository extends MongoRepository<HistoryItem, String>  {
}