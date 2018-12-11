package com.hq.answerapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * @author hq
 */
@Configuration
@EnableMongoRepositories(basePackages = "com.hq.answerapi.dao.xy",
		mongoTemplateRef = XyMongoConfig.MONGO_TEMPLATE)
public class XyMongoConfig {

	protected static final String MONGO_TEMPLATE = "xyMongoTemplate";
}
