package ca.gc.cbsa.mcoe.bravo.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(MultipleMongoProperties.class)
public class MultipleMongoConfig {
	private final MultipleMongoProperties mongoProperties;

	@Primary
	@Bean(name = "commercialMongoTemplate")
	public MongoTemplate commercialMongoTemplate() throws Exception {
		return new MongoTemplate(commercialFactory(this.mongoProperties.getCommercial()));
	}

	@Bean(name = "travellersMongoTemplate")
	public MongoTemplate travellersMongoTemplate() throws Exception {
		return new MongoTemplate(travellersFactory(this.mongoProperties.getTravellers()));
	}

	@Bean
	@Primary
	public MongoDbFactory commercialFactory(final MongoProperties mongo) throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(mongo.getUri())), mongo.getDatabase());
	}

	@Bean
	public MongoDbFactory travellersFactory(final MongoProperties mongo) throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(new MongoClientURI(mongo.getUri())), mongo.getDatabase());
	}
}
