package ca.gc.cbsa.mcoe.bravo.repository.commercial;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ca.gc.cbsa.mcoe.bravo.domain.DailyStats;

public interface DailyStatsRepository extends MongoRepository<DailyStats, Integer> {

	DailyStats findById(String id);
	
	@Query("{'id': {$gte: ?0, $lte:?1 }}")
	List<DailyStats> findDailyStatsBetween(String startDate, String endDate);
	
}
