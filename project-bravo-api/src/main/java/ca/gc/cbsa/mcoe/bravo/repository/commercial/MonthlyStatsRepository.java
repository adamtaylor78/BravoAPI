package ca.gc.cbsa.mcoe.bravo.repository.commercial;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ca.gc.cbsa.mcoe.bravo.domain.MonthlyStats;

public interface MonthlyStatsRepository extends MongoRepository<MonthlyStats, Integer> {

	MonthlyStats findById(String id);
	
	@Query("{'id': {$gte: ?0, $lte:?1 }}")
	List<MonthlyStats> findMonthlyStatsBetween(String startDate, String endDate);
	
}
