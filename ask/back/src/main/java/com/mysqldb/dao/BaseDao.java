package com.mysqldb.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;


/**
 * Dao基类接口
 */
public interface BaseDao<T>
{
	/*
	 * 写操作
	 */
	public Serializable saveEntity(T t); // 保存一个实体

	public void deleteEntity(T t); // 删除一个实体

	public void updateEntity(T t); // 更新实体

	public void saveOrUpdateEntity(T t); // 保存或更新一个实体

	/*
	 * 读操作
	 */
	public T getEntity(Serializable id); // 根据主键获取实体

	public T loadEntity(Serializable id); // 根据主键加载实体

	public List<T> getAllEntitys(); // 获取所有的实体

	/*
	 * HQL语句操作
	 */
	public int batchEntityByHQL(String hql, Object... params); // 根据hql语句批处理实体

	public List<T> findEntitysByHQL(String hql, Object... params);// 根据hql语句获取实体

	public T findUniqueResult(String hql, Object... params); // 根据hql语句获取唯一结果实体

	/*
	 * SQL语言操作
	 */
	public int executeSQL(String sql, Object... params); // 执行SQL语言

}



