package com.hq.adaptive.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.entity.AdlPhaseSectionEntity;
import com.hq.adaptive.pojo.query.AdlPhaseSectionQuery;

/**
 * 评测阶段包含节关系表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public interface AdlPhaseSectionDao {
	/**
	 * 根据ID查询查询评测阶段包含节关系表
	 * @param id	主键
	 * @return	评测阶段包含节关系表
	 */
	public AdlPhaseSectionEntity queryObject(@Param(value="id")Long id);
	
	/**
	 * 查询评测阶段包含节关系表列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	评测阶段包含节关系表列表
	 */
	public List<AdlPhaseSectionEntity> queryList(AdlPhaseSectionQuery adlPhaseSectionQuery);
	
	/**
	 * 查询评测阶段包含节关系表数量
	 * @return	评测阶段包含节关系表数量
	 */
	public int queryTotal(AdlPhaseSectionQuery adlPhaseSectionQuery);
	
	/**
	 * 新增评测阶段包含节关系表
	 */
	public int save(AdlPhaseSectionEntity adlPhaseSectionEntity);
	
	/**
	 * 更新评测阶段包含节关系表
	 */
	public int update(AdlPhaseSectionEntity adlPhaseSectionEntity);
	
	/**
	 * 按照ID删除
	 * @param id	主键
	 */
	public int delete(@Param(value="id")Long id);

	/**
	 * 批量删除
	 * @param notDelIds	不需要删除的ID
	 * @param phaseId	阶段ID
	 * @return
	 */
	public int deleteBatch(@Param(value="notDelIds")List<Long> notDelIds , @Param(value="phaseId")Long phaseId);

}
