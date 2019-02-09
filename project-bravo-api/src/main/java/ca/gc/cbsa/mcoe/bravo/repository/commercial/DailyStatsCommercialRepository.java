package ca.gc.cbsa.mcoe.bravo.repository.commercial;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ca.gc.cbsa.mcoe.bravo.domain.commercial.DailyStatsCommercial;

public interface DailyStatsCommercialRepository extends MongoRepository<DailyStatsCommercial, Integer> {

	DailyStatsCommercial findById(String id);
	
	@Query("{'id': {$gte: ?0, $lte:?1 }}")
	List<DailyStatsCommercial> findDailyStatsBetween(String startDate, String endDate);
	
}
