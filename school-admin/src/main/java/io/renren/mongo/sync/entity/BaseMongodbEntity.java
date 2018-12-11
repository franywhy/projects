package io.renren.mongo.sync.entity;

/**
 * mongodb基类
 * @class io.renren.mongo.sync.entity.BaseMongodbEntity.java
 * @Description:
 * @author shihongjie
 * @dete 2017年6月10日
 * @param <T>
 */
public abstract class BaseMongodbEntity<T> {
		
	private T _id;

	public T get_id() {
		return _id;
	}

	public void set_id(T _id) {
		this._id = _id;
	}
	
	
}
