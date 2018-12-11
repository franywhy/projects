package com.hq.adaptive.dao;

import com.hq.adaptive.entity.AdlPhaseSpaceEntity;
import com.hq.adaptive.pojo.AdlPhasePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评测阶段对应知识点空间表
 */
public interface AdlPhaseSpaceDao {
    /**
     * 查询 根据阶段PK
     * @param phaseId
     * @return
     */
    public List<AdlPhaseSpaceEntity> queryList(@Param("phaseId") Long phaseId);
    /**
     * 保存
     * @param adlPhaseSpaceEntity   保存对象
     * @return  操作成功数量
     */
    public int save(AdlPhaseSpaceEntity adlPhaseSpaceEntity);

    /**
     * 批量删除 根据阶段PK
     * @param phaseId   阶段PK
     * @return          删除数量
     */
    public int deleteByPhaseId(@Param("phaseId") Long phaseId);

    /**
     * 查询阶段下的知识点数量
     * @param phaseId
     * @return
     */
    public int queryKnowledgeTotal(@Param("phaseId") Long phaseId);

    /**
     * 查询阶段下的知识点列表
     * @param phaseId
     * @return
     */
    public List<Long> queryKnowledgeList(@Param("phaseId") Long phaseId);

    /**
     * 查询包含该知识点的阶段列表
     * @param knowledgeId
     * @return
     */
    public List<AdlPhasePOJO> queryKnowledgePhaseList(@Param("knowledgeId") Long knowledgeId);
}
