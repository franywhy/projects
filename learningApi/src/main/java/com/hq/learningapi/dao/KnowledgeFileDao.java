package com.hq.learningapi.dao;

import com.hq.learningapi.entity.AppBannerEntity;
import com.hq.learningapi.entity.KnowledgeFileEntity;
import com.hq.learningapi.pojo.KnowledgeFilePOJO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by liangdongbin on 2018/1/18 0026.
 */
@Repository
public interface KnowledgeFileDao {

	public void batchSave(List<KnowledgeFileEntity> knowledgeFiles);

	List<KnowledgeFilePOJO> queryForList(Map<String, Object> map);

	void cleanOldRecord(Map<String, Object> map);
}
