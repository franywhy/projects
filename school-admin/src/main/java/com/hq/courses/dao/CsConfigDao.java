package com.hq.courses.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.courses.pojo.CsConfigPOJO;
import com.hq.courses.pojo.query.CsConfigQuery;

/**
 * 静态变量表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public interface CsConfigDao {
	/**
	 * 根据ID查询查询静态变量表
	 * @param ckey		ckey
	 * @param cvalue	cvalue
	 * @return	静态变量表
	 */
	public CsConfigPOJO queryObject(@Param(value="ckey")String ckey ,@Param(value="cvalue") String cvalue);
	public String queryNameByValue(@Param(value="ckey")String ckey,@Param(value="cname") String cname);
	
	/**
	 * 查询静态变量表列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	静态变量表列表
	 */
	public List<CsConfigPOJO> queryList(CsConfigQuery csConfigQuery);
	
	/**
	 * 查询静态变量表数量
	 * @return	静态变量表数量
	 */
	public int queryTotal(CsConfigQuery csConfigQuery);
	
}
