package ca.gc.cbsa.mcoe.bravo.repository.commercial;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ca.gc.cbsa.mcoe.bravo.domain.commercial.HourlyStatsCommercial;

@Repository
public interface HourlyStatsCommercialRepository extends MongoRepository<HourlyStatsCommercial, Integer> {

	HourlyStatsCommercial findById(String id);
	
	@Query("{'id': {$gte: ?0, $lte:?1 }}")
	List<HourlyStatsCommercial> findHourlyStatsBetween(String startDate, String endDate);
	
}