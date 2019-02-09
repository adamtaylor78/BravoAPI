package ca.gc.cbsa.mcoe.bravo.repository.travellers;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ca.gc.cbsa.mcoe.bravo.domain.travellers.DailyStatsTravellers;

public interface DailyStatsTravellersRepository extends MongoRepository<DailyStatsTravellers, Integer> {

	DailyStatsTravellers findById(String id);
	
	@Query("{'id': {$gte: ?0, $lte:?1 }}")
	List<DailyStatsTravellers> findDailyStatsBetween(String startDate, String endDate);
	
}
