package ca.gc.cbsa.mcoe.bravo.config;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "mongodb")
public class MultipleMongoProperties {
    private MongoProperties commercial = new MongoProperties();
    private MongoProperties travellers = new MongoProperties();
}