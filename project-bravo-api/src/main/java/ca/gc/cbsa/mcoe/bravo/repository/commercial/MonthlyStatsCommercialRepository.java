package ca.gc.cbsa.mcoe.bravo.repository.commercial;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ca.gc.cbsa.mcoe.bravo.domain.commercial.MonthlyStatsCommercial;

public interface MonthlyStatsCommercialRepository extends MongoRepository<MonthlyStatsCommercial, Integer> {

	MonthlyStatsCommercial findById(String id);
	
	@Query("{'id': {$gte: ?0, $lte:?1 }}")
	List<MonthlyStatsCommercial> findMonthlyStatsBetween(String startDate, String endDate);
	
}
