package ca.gc.cbsa.mcoe.bravo.repository.travellers;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import ca.gc.cbsa.mcoe.bravo.domain.travellers.MonthlyStatsTravellers;

public interface MonthlyStatsTravellersRepository extends MongoRepository<MonthlyStatsTravellers, Integer> {

	MonthlyStatsTravellers findById(String id);
	
	@Query(value="{'id': {$gte: ?0, $lte:?1 }}")
	List<MonthlyStatsTravellers> findMonthlyStatsBetween(String startDate, String endDate);
	
}
