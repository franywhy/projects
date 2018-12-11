package io.renren.datasources.props;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hq
 */
@ConfigurationProperties(prefix = "spring.mongodb")
@Component
public class MultipleMongoProperties {

	private MongoProperties xy = new MongoProperties();
	private MongoProperties xyQqUser = new MongoProperties();

	public MongoProperties getXy() {
		return xy;
	}

	public MongoProperties getXyQqUser() {
		return xyQqUser;
	}
}
