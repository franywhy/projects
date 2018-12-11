package io.renren.datasources;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author hq
 */
@Configuration
@EnableMongoRepositories(basePackages = "io.renren.modules.job.dao.xyqquser",
		mongoTemplateRef = XyQqUserMongoConfig.MONGO_TEMPLATE)
public class XyQqUserMongoConfig {

	protected static final String MONGO_TEMPLATE = "xyQqUserMongoTemplate";
}
