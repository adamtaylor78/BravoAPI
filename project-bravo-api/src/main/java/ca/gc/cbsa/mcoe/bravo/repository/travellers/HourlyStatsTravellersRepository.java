package ca.gc.cbsa.mcoe.bravo.repository.travellers;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import ca.gc.cbsa.mcoe.bravo.domain.travellers.HourlyStatsTravellers;

@Repository
public interface HourlyStatsTravellersRepository extends MongoRepository<HourlyStatsTravellers, Integer> {

	HourlyStatsTravellers findById(String id);
	
	@Query("{'id': {$gte: ?0, $lte:?1 }}")
	List<HourlyStatsTravellers> findHourlyStatsBetween(String startDate, String endDate);
	
}