package io.renren.mongo.sync.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import io.renren.mongo.entity.MongoPagination;

public abstract class MongoBaseDao {

	/**
	 * spring mongodb 集成操作类
	 */
//	@Resource
	protected MongoTemplate mongoTemplate;
//	@Resource
	protected MongoTemplate zikaoTemplate;

	/**
	 * 通过条件查询实体(集合)
	 * 
	 * @param query
	 */
	public List find(Query query, Class c) {
		return find(query, c, mongoTemplate);
	}

	public List find(Query query, Class c, MongoTemplate template) {
		return template.find(query, c);
	}

	/**
	 * 通过一定的条件查询一个实体
	 * 
	 * @param query
	 * @return
	 */
	public Object findOne(Query query, Class c) {
		return findOne(query, c, mongoTemplate);
	}

	public Object findOne(Query query, Class c, MongoTemplate template) {
		return template.findOne(query, c);
	}

	/**
	 * 查询出所有数据
	 * 
	 * @return
	 */
	public List findAll(Class c) {
		return findAll(c, mongoTemplate);
		// return this.mongoTemplate.findAll(getEntityClass());
	}

	public List findAll(Class c, MongoTemplate template) {
		return template.findAll(c);
	}

	/**
	 * 查询并且修改记录
	 * 
	 * @param query
	 * @param update
	 * @return
	 */
	public Object findAndModify(Query query, Update update, Class c, MongoTemplate template) {
		return template.findAndModify(query, update, c);
	}

	public Object findAndModify(Query query, Update update, Class c) {
		return findAndModify(query, update, c, mongoTemplate);
	}

	/**
	 * 按条件查询,并且删除记录
	 * 
	 * @param query
	 * @return
	 */
	public Object findAndRemove(Query query, Class c) {
		return findAndRemove(query, c, mongoTemplate);
	}

	public Object findAndRemove(Query query, Class c, MongoTemplate template) {
		return this.mongoTemplate.findAndRemove(query, c);
	}

	/**
	 * 通过条件查询更新数据
	 * 
	 * @param query
	 * @param update
	 * @return
	 */
	public void updateFirst(Query query, Update update, Class c) {
		updateFirst(query, update, c, mongoTemplate);
	}

	public void updateFirst(Query query, Update update, Class c, MongoTemplate template) {
		template.updateFirst(query, update, c);
	}

	/**
	 * 保存一个对象到mongodb
	 * 
	 * @param bean
	 * @return
	 */
	public Object save(Object object) {
		return save(object, mongoTemplate);
	}

	public Object save(Object object, MongoTemplate template) {
		template.save(object);
		return object;
	}

	/**
	 * 通过ID获取记录
	 * 
	 * @param id
	 * @return
	 */
	public Object findById(String id, Class c) {
		return findById(id, c, mongoTemplate);
	}

	public Object findById(String id, Class c, MongoTemplate template) {
		return template.findById(id, c);
	}

	/**
	 * 通过ID获取记录,并且指定了集合名(表的意思)
	 * 
	 * @param id
	 * @param collectionName
	 *            集合名
	 * @return
	 */
	public Object findById(String id, String collectionName, Class c) {
		return findById(id, collectionName, c, mongoTemplate);
	}

	public Object findById(String id, String collectionName, Class c, MongoTemplate template) {
		return template.findById(id, c, collectionName);
	}

	/**
	 * 通过条件查询,查询分页结果
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	public MongoPagination getPage(int pageNo, int pageSize, Query query, Class c) {
		return getPage(pageNo, pageSize, query, c, mongoTemplate);
	}

	/**
	 * 通过条件查询,查询分页结果
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param query
	 * @return
	 */
	public MongoPagination getPage(int pageNo, int pageSize, Query query, Class c, MongoTemplate template) {
		long totalCount = template.count(query, c);
		MongoPagination page = new MongoPagination(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);// 从skip开始,取多少条记录
		List datas = this.find(query, c);
		page.setDatas(datas);
		return page;
	}

}
