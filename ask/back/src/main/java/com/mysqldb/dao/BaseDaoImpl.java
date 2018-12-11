package com.mysqldb.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.izhubo.rest.anno.Rest;
/**
 * Dao基类
 */
@Rest
public abstract class BaseDaoImpl<T> implements BaseDao<T>
{
	@Autowired
	protected SessionFactory sessionFactory;	//hibernate的会话工厂
	
	private Class<T> class_T;				//泛型T的Class类型
	
	public BaseDaoImpl()
	{
		
		//对泛型T的Class类型进行初始化赋值，this表示继承该类的子类对象
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		class_T = (Class<T>) type.getActualTypeArguments()[0];
	}
	
	public void init(SessionFactory _sessionFactory)
	{
		sessionFactory = _sessionFactory;
	}

	/**
	 * 保存一个实体
	 */
	@Override
	public Serializable saveEntity(T t)
	{
		
		return sessionFactory.getCurrentSession().save(t);
	}

	/**
	 * 删除一个实体
	 */
	@Override
	public void deleteEntity(T t)
	{
		sessionFactory.getCurrentSession().delete(t);
	}

	/**
	 * 更新实体
	 */
	@Override
	public void updateEntity(T t)
	{
		sessionFactory.getCurrentSession().update(t);
	}

	/**
	 * 保存或更新一个实体
	 */
	@Override
	public void saveOrUpdateEntity(T t)
	{
		sessionFactory.getCurrentSession().saveOrUpdate(t);
	}

	/**
	 * 根据主键获取实体
	 */
	@Override
	public T getEntity(Serializable id)
	{
		return (T) sessionFactory.getCurrentSession().get(class_T, id);
	}

	/**
	 * 根据主键加载实体
	 */
	@Override
	public T loadEntity(Serializable id)
	{
		return (T) sessionFactory.getCurrentSession().load(class_T, id);
	}

	/**
	 * 获取所有的实体
	 */
	@Override
	public List<T> getAllEntitys()
	{
		return sessionFactory.getCurrentSession().createQuery("from " + class_T.getName()).list();
	}

	/**
	 * 根据hql语句批处理实体
	 */
	@Override
	public int batchEntityByHQL(String hql, Object... params)
	{
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i = 0; i < params.length; i++)
		{
			query.setParameter(i, params[i]);
		}
		return query.executeUpdate();
	}

	/**
	 * 根据hql语句获取实体
	 */
	@Override
	public List<T> findEntitysByHQL(String hql, Object... params)
	{
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i = 0; i < params.length; i++)
		{
			query.setParameter(i, params[i]);
		}
		return query.list();
	}

	/**
	 * 根据hql语句获取唯一结果实体
	 */
	@Override
	public T findUniqueResult(String hql, Object... params)
	{
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		for(int i = 0; i < params.length; i++)
		{
			query.setParameter(i, params[i]);
		}
		return (T) query.uniqueResult();
	}

	/**
	 * 执行SQL语言
	 */
	@Override
	public int executeSQL(String sql, Object... params)
	{
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		for (int i = 0; i < params.length; i++)
		{
			query.setParameter(i, params[i]);
		}
		return query.executeUpdate();
	}
	
	/**
	 * 执行SQL语言
	 */
	public List<T> executeSQL1(String sql, Object... params)
	{
		SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(sql);
		for (int i = 0; i < params.length; i++)
		{
			query.setParameter(i, params[i]);
		}
		return query.list();
	}

}