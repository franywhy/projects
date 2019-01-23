package com.hqjy.synchronize_la_statistics.base;

import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author : zhouyibin
 * @date : 2018/12/13
 */
@Component
public interface BaseDao<T, ID extends Serializable> {

    /**
     * 增加
     */
    int save(T entity);

    int save(Map<String, Object> map);

    int saveBatch(List<T> list);

    /**
     * 更新
     */
    int update(T entity);

    int update(Map<String, Object> map);

    /**
     * 删除
     */
    int delete(ID id);

    int delete(Map<String, Object> map);

    int deleteBatch(ID[] id);

    int deleteBatch(Object id);

    int deleteList(List<T> entity);

    /**
     * 查询
     */
    T findOne(ID id);

    List<T> findList(T entity);

    List<T> findList(Map<String, Object> map);

    List<T> findAllList();

    /**
     * 实体属性的记录数量
     */
    int count(T entity);

    /**
     * 实体是否存在
     */
    boolean exist(T entity);

    Integer totalCount(String arg);
}
