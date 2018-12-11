package com.hqjy.msg.service;

import com.hqjy.msg.mybatis.BaseMapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/12/25 0025.
 */
public abstract class BaseService<T> {


    private BaseMapper<T> baseMapper;

    public void setBaseMapper(BaseMapper<T> baseMapper) {
        this.baseMapper = baseMapper;
    }

    public BaseMapper<T> getBaseMapper() {
        return baseMapper;
    }

    //查询
    public List<T> selectAll(){
        return baseMapper.selectAll();
    }


    //根据ID查询信息
    public T selectByPrimaryKey(Integer id){
        return baseMapper.selectByPrimaryKey(id);
    }

    //添加数据
    public int insert(T data){
        return baseMapper.insert(data);
    }

    //更新数据
    public int updateByPrimaryKey(T data){
        return baseMapper.updateByPrimaryKey(data);
    }

    //删除数据
   public int deleteByPrimaryKey(Integer id){
        return baseMapper.deleteByPrimaryKey(id);
   }
   //条件查询
    public List example(Map map,Class clz){
        //通用Example查询
        Example example = new Example(clz);
        Set keySets = map.keySet();
        Criteria criteria = example.createCriteria();
        for (Object keySet :keySets){
            Object value = map.get(keySet);

            criteria.andEqualTo(keySet.toString(),value);


        }
        //example.createCriteria().andEqualTo("name","李四")/*.andGreaterThan("id", 100)*/;//这里给出的条件查询为id>100
        return baseMapper.selectByExample(example);
    }
   
    //条件删除
    public int delExample(Map map,Class clz){
    	//通用Example查询
    	Example example = new Example(clz);
    	
    	Set keySets = map.keySet();
    	for (Object keySet :keySets){
    		Object value = map.get(keySet);
    		example.createCriteria().andEqualTo(keySet.toString(),value.toString());
    	}
    	return baseMapper.deleteByExample(example);
    }


}
