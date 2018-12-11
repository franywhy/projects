package com.hq.adaptive.service.impl;

import com.hq.adaptive.dao.AdlKnowledgeDao;
import com.hq.adaptive.entity.AdlKnowledgeEntity;
import com.hq.adaptive.entity.AdlSectionEntity;
import com.hq.adaptive.pojo.*;
import com.hq.adaptive.pojo.query.AdlKnowledgeQuery;
import com.hq.adaptive.pojo.query.AdlSectionQuery;
import com.hq.adaptive.service.*;
import com.hq.adaptive.util.PolyvUtils;
import com.hq.courses.pojo.CsKnowledgeExcelColumnEnum;
import io.renren.common.doc.ParamKey;
import io.renren.common.validator.Assert;
import io.renren.utils.BeanHelper;
import io.renren.utils.EmptyUtils;
import io.renren.utils.ExcelReaderJXL;
import io.renren.utils.RRException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
@Service("adlKnowledgeService")
public class AdlKnowledgeServiceImpl implements AdlKnowledgeService {
	@Autowired
	private AdlKnowledgeDao adlKnowledgeDao;
	@Autowired
	private AdlSectionService adlSectionService;
	@Autowired
	private AdlConfigService adlConfigService;
	@Autowired
	private AdlKnowledgeContainService adlKnowledgeContainService;
	@Autowired
	private AdlKnowledgeVideoService adlKnowledgeVideoService;
	@Autowired
	private AdlKnowledgeFileService adlKnowledgeFileService;

	
	/**
	 * 根据ID查询查询知识点档案
	 * @param knowledgeId	主键
	 * @return	知识点档案
	 */
	@Override
	public AdlKnowledgePOJO queryObject(Long knowledgeId){
		if(null !=knowledgeId) {
			AdlKnowledgeEntity adlKnowledgeEntity = adlKnowledgeDao.queryObject(knowledgeId);
			return entityTOPojo(null, adlKnowledgeEntity);
		}
		return null;
	}

	@Override
	public AdlKnowledgePOJO queryObject(AdlKnowledgePOJO adlKnowledgePOJO) {
		return adlKnowledgeDao.queryObjectbyPoJO(adlKnowledgePOJO);
	}

	/**
	 * 查询知识点档案列表
	 * @param adlKnowledgeQuery
//	 * @param limit		返回记录行的最大数目
	 * @return	知识点档案列表
	 */
	@Override
	public List<AdlKnowledgePOJO> queryList(AdlKnowledgeQuery adlKnowledgeQuery){
		if(adlKnowledgeQuery == null ||(
				EmptyUtils.isEmpty(adlKnowledgeQuery.getChapterId())
				&& EmptyUtils.isEmpty(adlKnowledgeQuery.getCourseId())
				&& EmptyUtils.isEmpty(adlKnowledgeQuery.getSectionId()))
				) {
			throw new RRException("请选择课程或者章节!");
		}
		List<AdlKnowledgePOJO> adlKnowledgePOJOs = null;
		List<AdlKnowledgeEntity> adlKnowledgeEntities = adlKnowledgeDao.queryList(adlKnowledgeQuery);
		if(null != adlKnowledgeEntities && !adlKnowledgeEntities.isEmpty()) {
			adlKnowledgePOJOs = new ArrayList<>();
			for (AdlKnowledgeEntity en : adlKnowledgeEntities) {
				AdlKnowledgePOJO pojo = entityTOPojo(null, en);
				adlKnowledgePOJOs.add(pojo);
			}
		}
		return adlKnowledgePOJOs;
	}

	@Override
	public List<AdlKnowledgeEntity> queryEntityList(AdlKnowledgeQuery adlKnowledgeQuery) {
		if(adlKnowledgeQuery == null ||(
				EmptyUtils.isEmpty(adlKnowledgeQuery.getChapterId())
						&& EmptyUtils.isEmpty(adlKnowledgeQuery.getCourseId())
						&& EmptyUtils.isEmpty(adlKnowledgeQuery.getSectionId()))
		) {
			throw new RRException("请选择课程或者章节!");
		}
		List<AdlKnowledgeEntity> adlKnowledgeEntities = adlKnowledgeDao.queryList(adlKnowledgeQuery);
		return adlKnowledgeEntities;
	}

	/**
	 * entity转POJO
	 * @param adlKnowledgePOJO
	 * @param adlKnowledgeEntity
	 * @return	pojo
	 */
	private AdlKnowledgePOJO entityTOPojo(AdlKnowledgePOJO adlKnowledgePOJO , AdlKnowledgeEntity adlKnowledgeEntity) {
		if(adlKnowledgeEntity != null) {
			if(null == adlKnowledgePOJO) {
				adlKnowledgePOJO = new AdlKnowledgePOJO();
			}
			adlKnowledgePOJO.setChapterId(adlKnowledgeEntity.getChapterId());
			adlKnowledgePOJO.setCourseId(adlKnowledgeEntity.getCourseId());
			AdlConfigPOJO keyPointPOJO = this.adlConfigService.queryObject(AdlConfigKeyEnum.ADP_KNOWLEDGE_KEY_POINT, String.valueOf(adlKnowledgeEntity.getKeyPoint()));
			if(null != keyPointPOJO) {
				adlKnowledgePOJO.setKeyPointName(keyPointPOJO.getCname());
				adlKnowledgePOJO.setKeyPoint(adlKnowledgeEntity.getKeyPoint());
			}
			//难度
			adlKnowledgePOJO.setLevelName(adlKnowledgeEntity.getLevelName());
			//知识点信息
			adlKnowledgePOJO.setKnowledgeId(adlKnowledgeEntity.getKnowledgeId());
			adlKnowledgePOJO.setKnowledgeName(adlKnowledgeEntity.getKnowledgeName());
			adlKnowledgePOJO.setKnowledgeNo(adlKnowledgeEntity.getKnowledgeNo());
			//节信息
			if(null != adlKnowledgeEntity.getSectionId()) {
				AdlSectionPOJO adlSectionPOJO = this.adlSectionService.queryObject(adlKnowledgeEntity.getSectionId());
				if(null != adlSectionPOJO) {
					adlKnowledgePOJO.setSectionNo(adlSectionPOJO.getSectionNo());
					adlKnowledgePOJO.setSectionName(adlSectionPOJO.getSectionName());
				}
			}
			adlKnowledgePOJO.setSectionId(adlKnowledgeEntity.getSectionId());
			adlKnowledgePOJO.setStatus(adlKnowledgeEntity.getStatus());
			//题型
			adlKnowledgePOJO.setQuestiontypeName(adlKnowledgeEntity.getQuestionName());
			//包含知识点
			List<AdlKnowledgeContainPOJO> adlKnowledgeContainPOJOs = this.adlKnowledgeContainService.queryListBySelfId(adlKnowledgeEntity.getKnowledgeId());
			adlKnowledgePOJO.setChildList(adlKnowledgeContainPOJOs);
			//资料
			AdlKnowledgeFilePOJO adlKnowledgeFilePOJO = this.adlKnowledgeFileService.queryObject(adlKnowledgeEntity.getKnowledgeId());
			adlKnowledgePOJO.setAdlKnowledgeFile(adlKnowledgeFilePOJO);
			//视频
			AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO = this.adlKnowledgeVideoService.queryObject(adlKnowledgeEntity.getKnowledgeId());
			adlKnowledgePOJO.setAdlKnowledgeVideo(adlKnowledgeVideoPOJO);
		}
		return adlKnowledgePOJO;
	}
	
	/**
	 * 查询知识点档案数量
	 * @return	知识点档案数量
	 */
	@Override
	public int queryTotal(AdlKnowledgeQuery adlKnowledgeQuery){
		if(adlKnowledgeQuery == null ||(
				EmptyUtils.isEmpty(adlKnowledgeQuery.getChapterId())
				&& EmptyUtils.isEmpty(adlKnowledgeQuery.getCourseId())
				&& EmptyUtils.isEmpty(adlKnowledgeQuery.getSectionId()))
				) {
			throw new RRException("请选择课程或者章节！");
		}
		return adlKnowledgeDao.queryTotal(adlKnowledgeQuery);
	}

	/**
	 * 查询数量-通过章ID
	 *
	 * @param chapterId 章ID
	 * @return 知识点数量
	 */
	@Override
	public int queryTotalByChapterId(Long chapterId) {
		int num = 0;
		if (null != chapterId) {
			AdlSectionQuery adlSectionQuery = new AdlSectionQuery();
			adlSectionQuery.setChapterId(chapterId);
			List<AdlSectionEntity> adlSectionList = this.adlSectionService.queryList(adlSectionQuery);
			if(null != adlSectionList && !adlSectionList.isEmpty()){
				for(AdlSectionEntity adlSectionEntity : adlSectionList){
					AdlKnowledgeQuery adlKnowledgeQuery = new AdlKnowledgeQuery();
					adlKnowledgeQuery.setSectionId(adlSectionEntity.getSectionId());
					num += this.adlKnowledgeDao.queryTotal(adlKnowledgeQuery);
				}
			}
		}
		return num;
	}

	/**
	 * 更新知识点档案
	 */
	@Override
	public int updateZL(AdlKnowledgePOJO adlKnowledgeVideoPOJO){
		this.verifyForm(adlKnowledgeVideoPOJO);
		//知识点资料
		this.adlKnowledgeFileService.saveOrUpdate(adlKnowledgeVideoPOJO.getKnowledgeId(),adlKnowledgeVideoPOJO.getAdlKnowledgeFile());
		//知识点视频
		this.adlKnowledgeVideoService.saveOrUpdate(adlKnowledgeVideoPOJO.getKnowledgeId(), adlKnowledgeVideoPOJO.getAdlKnowledgeVideo());
		return 1;
	}
	
	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(AdlKnowledgePOJO adlKnowledgePOJO){
		Assert.isNull(adlKnowledgePOJO, "知识点对象不能为空！");
		Assert.isNull(adlKnowledgePOJO.getKnowledgeId(), "知识点ID不能为空");
	}

	/**
	 * 批量导入
	 * @param courseId 课程id
	 * @param productId 产品线pk
	 * @param file
	 * @return
	 */
	@Transactional(value = ParamKey.Transactional.hq_adaptive ,readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public String importExcel(Long courseId,Long productId, MultipartFile file) {
		if(null != file) {
			StringBuffer errorMsg = new StringBuffer();
			try {
				FileInputStream fio = (FileInputStream) file.getInputStream();
				List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
				String[] header = dataList.get(0);
				if(null == dataList || dataList.isEmpty() || dataList.size() <= 1) {
					throw new RRException("文件或内容有问题，请重新导入！");
				}
				int columnLength = 11;
				//总共六行数据
				//章名称	节名称	知识点名称	难度	考点	题型(多个请用逗号隔开)
				if(header.length < columnLength){
//				exceptMsg.append("总列数不正确，请核对一下列数；");
					throw new RRException("总列数不正确，请核对一下列数或重新下载导入模板！");
				}
				for(int i = 1 , length = dataList.size() ; i < length ; i++) {
					int line = i+1;
					String[] dataItem = dataList.get(i);
					//列数校验
					if(dataItem.length < columnLength) {
						errorMsg.append("第" + line + "行错误：" + "列数不正确！<br/>");
						continue;
					}
					//列
					String zsdbm = dataItem[CsKnowledgeExcelColumnEnum.zsdbm.getExcelIndex()];
					String vid = dataItem[CsKnowledgeExcelColumnEnum.vid.getExcelIndex()];
					if(StringUtils.isNotBlank(vid)){
						Long knowledgeId = queryKnowledgeIdByNo(courseId,zsdbm);
						if(knowledgeId != null) {
							if(adlKnowledgeVideoService.queryKnowledgeIdIsNotExist(knowledgeId)) {
								Map<String,Object> videoMap = PolyvUtils.getVideoObject(productId,vid);
								if(videoMap != null) {
									String polyvVid = videoMap.get("vid").toString();
									String polyvName = videoMap.get("title").toString();
									String polyvDuration = videoMap.get("duration").toString();
									String screenShot = videoMap.get("first_image").toString();

									AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO = new AdlKnowledgeVideoPOJO();
									adlKnowledgeVideoPOJO.setKnowledgeId(knowledgeId);
									adlKnowledgeVideoPOJO.setPolyvVid(polyvVid);
									adlKnowledgeVideoPOJO.setPolyvName(polyvName);
									adlKnowledgeVideoPOJO.setPolyvDuration(polyvDuration);
									adlKnowledgeVideoPOJO.setScreenShot(screenShot);
									BeanHelper.beanAttributeValueTrim(adlKnowledgeVideoPOJO);
									adlKnowledgeVideoService.save(adlKnowledgeVideoPOJO);
								}else {
									errorMsg.append("第"+ line +"行错误：" + "该知识点的视频不存在！<br/>");
								}

							}else{
								errorMsg.append("第"+ line +"行错误：" + "该知识点的视频已存在，请通过知识点资料界面重新上传！<br/>");
							}
						}else {
							errorMsg.append("第"+ line +"行错误：" + "该知识点不存在！<br/>");
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return errorMsg.toString();
		}
		return null;
	}
	/**
	 * 查询知识点id
	 * @param courseId	课程id
	 * @param knowledgeNo	知识点编码
	 * @return
	 */
	@Override
	public Long queryKnowledgeIdByNo(Long courseId, String knowledgeNo) {
		return adlKnowledgeDao.queryKnowledgeIdByNo(courseId, knowledgeNo);
	}

}
