package ca.gc.cbsa.mcoe.bravo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ca.gc.cbsa.mcoe.bravo.domain.HourlyStats;

@Repository
public interface HourlyStatsRepository extends MongoRepository<HourlyStats, Integer> {

	HourlyStats findById(String id);
	
	@Query("{'id': {$gte: ?0, $lte:?1 }}")
	List<HourlyStats> findHourlyStatsBetween(String startDate, String endDate);
	
}