package io.renren.mongo.dao.impl;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import io.renren.mongo.dao.ITestDao2;
import io.renren.mongo.entity.MongoPagination;
import io.renren.mongo.entity.TestEntity;

@Repository("testDaoImpl2")
public class TestDaoImpl2 extends MongodbBaseDao<TestEntity> implements ITestDao2 {

	@Override
	public List<TestEntity> findAllTestBySex(String sex) {
		List<TestEntity> result = mongoTemplate.find(new Query(Criteria.where("sex").is(sex)), TestEntity.class);
		return result;
	}

	@Override
	protected Class<TestEntity> getEntityClass() {
		return TestEntity.class;
	}

	


}