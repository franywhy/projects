package com.hq.adaptive.dao;

import com.hq.adaptive.entity.AdlPhaseFilesEntity;
import org.apache.ibatis.annotations.Param;

/**
 * 评测阶段对应资料
 */
public interface AdlPhaseFilesDao {
    /**
     * 保存
     * @param AdlPhaseFilesEntity   保存对象
     * @return  操作成功数量
     */
    public int save(AdlPhaseFilesEntity adlPhaseFilesEntity);

    /**
     * 获取资料
     * @param phaseId 阶段pk
     * @param phaseType 阶段类型
     * @return
     */
    public  AdlPhaseFilesEntity queryObject(@Param(value="phaseId")Long phaseId, @Param(value="phaseType")Long phaseType);

    /**
     * 删除资料
     * @param phaseId
     */
    public void deleteByPhaseId(@Param(value="phaseId")Long phaseId);
}
