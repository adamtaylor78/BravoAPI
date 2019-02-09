package ca.gc.cbsa.mcoe.bravo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "ca.gc.cbsa.mcoe.bravo.repository.travellers", mongoTemplateRef = "travellersMongoTemplate")
public class TravellersMongoConfig {
}
