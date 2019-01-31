package ca.gc.cbsa.mcoe.bravo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import ca.gc.cbsa.mcoe.bravo.domain.Division;

@Repository
public interface DivisionsRepository extends MongoRepository<Division, Integer> {

	List<Division> findByName(String name);
	Division findByCode(Integer code);
	
}
