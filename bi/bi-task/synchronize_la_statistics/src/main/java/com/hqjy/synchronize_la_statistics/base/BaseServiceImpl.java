package com.hqjy.synchronize_la_statistics.base;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author : zhouyibin
 * @date : 2018/12/13
 */

public class BaseServiceImpl<M extends BaseDao<T, ID>, T, ID extends Serializable> implements BaseService<T, ID> {
    @Autowired
    private M baseDao;

    @Override
    public int save(T entity) {
        return baseDao.save(entity);
    }

    @Override
    public int save(Map<String, Object> map) {
        return baseDao.save(map);
    }

    @Override
    public int saveBatch(List<T> list) {
        return baseDao.saveBatch(list);
    }

    @Override
    public int update(T entity) {
        return baseDao.update(entity);
    }

    @Override
    public int update(Map<String, Object> map) {
        return baseDao.update(map);
    }

    @Override
    public int delete(ID id) {
        return baseDao.delete(id);
    }

    @Override
    public int delete(Map<String, Object> map) {
        return baseDao.delete(map);
    }

    @Override
    public int deleteBatch(ID[] id) {
        return baseDao.deleteBatch(id);
    }

    @Override
    public int deleteBatch(Object[] ids) {
        return baseDao.deleteBatch(ids);
    }

    @Override
    public int deleteList(List<T> entity) {
        return baseDao.deleteList(entity);
    }

    @Override
    public T findOne(ID id) {
        return baseDao.findOne(id);
    }

    @Override
    public List<T> findList(T entity) {
        return baseDao.findList(entity);
    }

    @Override
    public List<T> findList(Map<String, Object> map) {
        return baseDao.findList(map);
    }



    @Override
    public int count(T entity) {
        return baseDao.count(entity);
    }

    @Override
    public boolean exist(T entity) {
        return baseDao.exist(entity);
    }

    @Override
    public Integer totalCount(String arg) {
        return baseDao.totalCount(arg);
    }


}
