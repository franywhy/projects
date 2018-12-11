package com.hq.adaptive.service.impl;

import com.hq.adaptive.dao.AdlPhaseSpaceDao;
import com.hq.adaptive.entity.AdlPhaseEntity;
import com.hq.adaptive.entity.AdlPhaseSpaceEntity;
import com.hq.adaptive.pojo.AdlKnowledgeContainPOJO;
import com.hq.adaptive.pojo.AdlKnowledgePOJO;
import com.hq.adaptive.pojo.AdlPhasePOJO;
import com.hq.adaptive.service.AdlPhaseSpaceService;
import io.renren.common.doc.ParamKey;
import io.renren.common.validator.Assert;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service("adlPhaseSpaceService")
public class AdlPhaseSpaceServiceImpl implements AdlPhaseSpaceService {

    @Autowired
    private AdlPhaseSpaceDao adlPhaseSpaceDao;
    @Autowired
    private com.hq.adaptive.service.AdlKnowledgeService adlKnowledgeService;
    @Autowired
    private com.hq.adaptive.service.AdlKnowledgeContainService adlKnowledgeContainService;
    /**
     * 查询 根据阶段PK
     *
     * @param phaseId
     * @return
     */
    @Override
    public List<AdlPhaseSpaceEntity> queryList(Long phaseId) {
        Assert.isNull(phaseId , "阶段PK不能为空!");
        return this.adlPhaseSpaceDao.queryList(phaseId);
    }

    /**
     * 保存
     *
     * @param adlPhaseSpaceEntity 保存对象
     * @return 操作成功数量
     */
    @Override
    public int save(AdlPhaseSpaceEntity adlPhaseSpaceEntity) {
        Assert.isNull(adlPhaseSpaceEntity , "adlPhaseSpaceEntity对象不能为空!");
        return this.adlPhaseSpaceDao.save(adlPhaseSpaceEntity);
    }

    /**
     * 保存阶段节下包含的知识点
     * <code>1.删除阶段下知识点包含关系</code>
     * <code>2.新增知识点包含关系</code>
     * @param phaseId   阶段PK
     */
    @Transactional(value = ParamKey.Transactional.hq_adaptive ,readOnly = false, propagation = Propagation.REQUIRES_NEW)
    @Override
    public void saveOrUpdate(AdlPhaseEntity adlPhaseEntity, List<Long> knowledgeIdList){
        if(null == adlPhaseEntity){
            return;
        }
        //删除原来的阶段下包含的知识点
        this.deleteByPhaseId(adlPhaseEntity.getPhaseId());

        if(CollectionUtils.isNotEmpty(knowledgeIdList)){
            for (int i = 0; i < knowledgeIdList.size(); i++) {
                Long knowledgeId = knowledgeIdList.get(i);
                AdlKnowledgePOJO adlKnowledgePOJO = adlKnowledgeService.queryObject(knowledgeId);
                if(adlPhaseEntity.getCourseId().equals(adlKnowledgePOJO.getCourseId())){
                    //查询知识点下级知识点
                    List<AdlKnowledgeContainPOJO> adlKnowledgeContainPOJOS = this.adlKnowledgeContainService.queryListBySelfId(knowledgeId);
                    if(CollectionUtils.isNotEmpty(adlKnowledgeContainPOJOS)){
                        for (int k = 0; k < adlKnowledgeContainPOJOS.size(); k++) {
                            AdlKnowledgeContainPOJO adlKnowledgeContainPOJO = adlKnowledgeContainPOJOS.get(k);
                            AdlPhaseSpaceEntity adlPhaseSpaceEntity = new AdlPhaseSpaceEntity();
                            adlPhaseSpaceEntity.setCourseId(adlPhaseEntity.getCourseId());
                            adlPhaseSpaceEntity.setPhaseId(adlPhaseEntity.getPhaseId());
                            adlPhaseSpaceEntity.setSelfId(adlKnowledgeContainPOJO.getSelfId());
                            adlPhaseSpaceEntity.setChildId(adlKnowledgeContainPOJO.getChildId());
                            this.save(adlPhaseSpaceEntity);
                        }
                    }else{
                        AdlPhaseSpaceEntity adlPhaseSpaceEntity = new AdlPhaseSpaceEntity();
                        adlPhaseSpaceEntity.setCourseId(adlPhaseEntity.getCourseId());
                        adlPhaseSpaceEntity.setPhaseId(adlPhaseEntity.getPhaseId());
                        adlPhaseSpaceEntity.setSelfId(knowledgeId);
                        adlPhaseSpaceEntity.setChildId(0L);
                        this.save(adlPhaseSpaceEntity);
                    }
                }
            }
        }

    }

    @Override
    public int queryKnowledgeTotal(Long phaseId) {
        return adlPhaseSpaceDao.queryKnowledgeTotal(phaseId);
    }

    @Override
    public List<Long> queryKnowledgeList(Long phaseId) {
        return adlPhaseSpaceDao.queryKnowledgeList(phaseId);
    }

    @Override
    public List<AdlPhasePOJO> queryKnowledgePhaseList(Long knowledgeId) {
        return adlPhaseSpaceDao.queryKnowledgePhaseList(knowledgeId);
    }

    /**
     * 批量删除 根据阶段PK
     *
     * @param phaseId 阶段PK
     * @return 删除数量
     */
    @Override
    public int deleteByPhaseId(Long phaseId) {
        Assert.isNull(phaseId , "阶段PK不能为空!");
        return this.adlPhaseSpaceDao.deleteByPhaseId(phaseId);
    }
}
