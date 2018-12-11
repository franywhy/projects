package io.renren.mongo.dao;

import java.util.List;

import io.renren.mongo.entity.TestEntity;

public interface ITestDao2 {

	public List<TestEntity> findAllTestBySex(String sex);
	
}