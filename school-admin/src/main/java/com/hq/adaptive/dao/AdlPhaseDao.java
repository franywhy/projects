package com.hq.adaptive.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.entity.AdlPhaseEntity;
import com.hq.adaptive.pojo.AdlPhasePOJO;
import com.hq.adaptive.pojo.query.AdlPhaseQuery;

/**
 * 评测阶段表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public interface AdlPhaseDao {
	/**
	 * 根据ID查询查询评测阶段表
	 * @param adlPhaseQuery
	 * @return	评测阶段表
	 */
	public AdlPhasePOJO queryObject(AdlPhaseQuery adlPhaseQuery);
//	public AdlPhasePOJO queryObject(@Param(value="phaseId")Long phaseId);

	/**
	 * 查询评测阶段表列表
	 * @param adlPhaseQuery
	 * @return	评测阶段表列表
	 */
	public List<AdlPhasePOJO> queryList(AdlPhaseQuery adlPhaseQuery);
	
	/**
	 * 查询评测阶段表数量
	 * @return	评测阶段表数量
	 */
	public int queryTotal(AdlPhaseQuery adlPhaseQuery);
	
	/**
	 * 新增评测阶段表
	 */
	public int save(AdlPhaseEntity adlPhaseEntity);
	
	/**
	 * 更新评测阶段表
	 */
	public int update(AdlPhaseEntity adlPhaseEntity);
	
	/**
	 * 按照ID删除
	 * @param phaseId	主键
	 */
	public int delDr(@Param(value="phaseId")Long phaseId);

	/**
	 * 更新状态
	 * @param status	状态 0：禁用 1：启用
	 * @param phaseId 	阶段ID
	 * @return
	 */
	public int updateStatus(@Param(value="status")Integer status , @Param(value="phaseId")Long phaseId);

	/**
	 * 校验阶段名称是否重复
	 * @param courseId	课程ID
	 * @param phaseId	不需要校验的阶段ID
	 * @param phaseName	要校验的阶段名称
	 * @return	查询相同阶段名称的数量
	 */
	int selectPhaseNameTotal(@Param(value="courseId")Long courseId , @Param(value="phaseId")Long phaseId ,@Param(value="phaseName")String phaseName);

	/**
	 * 校验阶段编号是否重复
	 * @param phaseId	不需要校验的阶段ID
	 * @param phaseNo	需要校验的阶段编号
	 * @return	查询相同阶段编号的数量
	 */
	int selectPhaseNoTotal(@Param(value="phaseId")Long phaseId , @Param(value="phaseNo")String phaseNo);
}
