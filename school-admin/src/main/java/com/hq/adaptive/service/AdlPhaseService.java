package com.hq.adaptive.service;

import com.hq.adaptive.pojo.AdlPhasePOJO;
import com.hq.adaptive.pojo.query.AdlPhaseQuery;

import java.util.List;

/**
 * 评测阶段表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public interface AdlPhaseService {
	/**
	 * 根据ID查询查询评测阶段表
	 * @param phaseId	主键
	 * @return	评测阶段表
	 */
	AdlPhasePOJO queryObject(Long phaseId);
	
	/**
	 * 查询评测阶段表列表
	 * @param adlPhaseQuery		查询条件
	 * @return	评测阶段表列表
	 */
	List<AdlPhasePOJO> queryList(AdlPhaseQuery adlPhaseQuery);
	
	/**
	 * 查询评测阶段表数量
	 * @return	评测阶段表数量
	 */
	int queryTotal(AdlPhaseQuery adlPhaseQuery);
	
	/**
	 * 新增评测阶段表
	 */
	int save(AdlPhasePOJO adlPhase);
	
	/**
	 * 更新评测阶段表
	 */
	int update(AdlPhasePOJO adlPhase);
	
	/**
	 * 按照ID删除
	 * @param phaseId	主键
	 */
	int delete(Long phaseId);

	/**
	 * 启用
	 * @param phaseId
	 * @return
	 */
	int enablePhase(Long phaseId);

	/**
	 * 禁用
	 * @param phaseId
	 * @return
	 */
	int disablePhase(Long phaseId);

	/**
	 * 校验阶段名称是否重复
	 * @param courseId	课程ID
	 * @param phaseId	不需要校验的阶段ID
	 * @param phaseName	要校验的阶段名称
	 * @return	true:存在 false:不存在
	 */
	boolean checkPhaseName(Long courseId , Long phaseId ,String phaseName);

	/**
	 * 校验阶段编号是否重复
	 * @param phaseId	不需要校验的阶段ID
	 * @param phaseNo	需要校验的阶段编号
	 * @return	true:存在 false:不存在
	 */
	boolean checkPhaseNo(Long phaseId , String phaseNo);

	/**
	 * 更新阶段知识点空间版本
	 * @param phaseId	阶段ID
	 * @param update	强制更新 true=强制更新
	 * @return			错误信息
	 */
	String updatePhaceKnowledgeVersion(Long phaseId , Boolean update);
}
