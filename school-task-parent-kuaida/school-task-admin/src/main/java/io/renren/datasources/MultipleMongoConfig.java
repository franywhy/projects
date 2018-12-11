package io.renren.datasources;

import com.mongodb.MongoClient;
import io.renren.datasources.props.MultipleMongoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

/**
 * @author hq
 */
@Configuration
public class MultipleMongoConfig {

	@Autowired
	private MultipleMongoProperties mongoProperties;

	@Primary
	@Bean(name = XyMongoConfig.MONGO_TEMPLATE)
	public MongoTemplate xyMongoTemplate() throws Exception {
		return new MongoTemplate(xyFactory(this.mongoProperties.getXy()));
	}

	@Bean
	@Qualifier(XyQqUserMongoConfig.MONGO_TEMPLATE)
	public MongoTemplate xyQqUserMongoTemplate() throws Exception {
        return new MongoTemplate(xyQqUserFactory(this.mongoProperties.getXyQqUser()));
	}

	@Bean
    @Primary
	public MongoDbFactory xyFactory(MongoProperties mongo) throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
				mongo.getDatabase());
	}

	@Bean
	public MongoDbFactory xyQqUserFactory(MongoProperties mongo) throws Exception {
		return new SimpleMongoDbFactory(new MongoClient(mongo.getHost(), mongo.getPort()),
				mongo.getDatabase());
	}
}
