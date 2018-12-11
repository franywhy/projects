package com.hq.adaptive.service;

import com.hq.adaptive.entity.AdlPhaseFilesEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 评测阶段对应资料
 */
public interface AdlPhaseFilesService {
    /**
     * 保存
     * @param adlPhaseSpaceEntity   保存对象
     * @return  操作成功数量
     */
    public int save(Long phaseId,Long phaseType,AdlPhaseFilesEntity adlPhaseFilesEntity);

    /**
     * 获取资料
     * @param phaseId 阶段pk
     * @param phaseType 阶段类型
     * @return
     */
    public  AdlPhaseFilesEntity queryObject(Long phaseId,Long phaseType);


    /**
     * 删除资料
     * @param phaseId
     */
    public void deleteByPhaseId(Long phaseId);
}
