package com.hq.adaptive.service;

import com.hq.adaptive.entity.AdlPhaseEntity;
import com.hq.adaptive.entity.AdlPhaseSpaceEntity;
import com.hq.adaptive.pojo.AdlPhasePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评测阶段对应知识点空间表
 */
public interface AdlPhaseSpaceService {

    /**
     * 查询 根据阶段PK
     * @param phaseId
     * @return
     */
    public List<AdlPhaseSpaceEntity> queryList(Long phaseId);
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
    public int deleteByPhaseId(Long phaseId);

    /**
     * 保存阶段节下包含的知识点
     * <code>1.删除阶段下知识点包含关系</code>
     * <code>2.新增知识点包含关系</code>
     * @param phaseId   阶段PK
     */
    public void saveOrUpdate(AdlPhaseEntity adlPhaseEntity, List<Long> knowledgeIdList);

    public int queryKnowledgeTotal(Long phaseId);

    /**
     * 查询阶段下的知识点列表
     * @param phaseId
     * @return
     */
    public List<Long> queryKnowledgeList(Long phaseId);

    /**
     * 查询包含该知识点的阶段列表
     * @param knowledgeId
     * @return
     */
    public List<AdlPhasePOJO> queryKnowledgePhaseList(Long knowledgeId);
}
