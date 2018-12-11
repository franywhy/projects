package com.hq.courses.service.impl;

import com.hq.adaptive.pojo.AdlPhasePOJO;
import com.hq.adaptive.pojo.KnowledgeKeyPointEnum;
import com.hq.adaptive.service.AdlPhaseSpaceService;
import com.hq.courses.dao.CsKnowledgeDao;
import com.hq.courses.dao.CsSectionDao;
import com.hq.courses.entity.CsChapterEntity;
import com.hq.courses.entity.CsCourseEntity;
import com.hq.courses.entity.CsKnowledgeEntity;
import com.hq.courses.entity.CsSectionEntity;
import com.hq.courses.pojo.*;
import com.hq.courses.pojo.query.CsKnowledgeQuery;
import com.hq.courses.service.*;
import com.hq.courses.util.CodeSplitUtils;
import com.hq.courses.util.CodeValidateUtils;
import com.hq.courses.util.CsCodeUtils;
import io.renren.common.doc.ParamKey;
import io.renren.common.validator.Assert;
import io.renren.rest.persistent.KGS;
import io.renren.utils.*;
import io.renren.utils.service.adaptive.http.AdaptiveServiceHttpUtils;
import io.renren.utils.service.adaptive.pojo.ASCyclicTopologyKnowledgeNode;
import io.renren.utils.service.adaptive.pojo.ASCyclicTopologyPOJO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
@Service("csKnowledgeService")
public class CsKnowledgeServiceImpl implements CsKnowledgeService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CsKnowledgeDao csKnowledgeDao;
	@Autowired
	private CsSectionDao csSectionDao;
	@Autowired
	private CsSectionService csSectionService;
	@Autowired
	private CsConfigService csConfigService;
	@Autowired
	private CsKnowledgeContainService csKnowledgeContainService;
	@Autowired
	private CsKnowledgeQuestiontypeService csKnowledgeQuestiontypeService;
//	@Autowired
//	private CsKnowledgeVideoService csKnowledgeVideoService;
//	@Autowired
//	private CsKnowledgeFileService csKnowledgeFileService;
	@Autowired
	private CsCourseService csCourseService;
	
	@Autowired
	private CsChapterService csChapterService;
	@Resource
	private KGS csKnowledgeNoKGS;
	
	private static String DOWN_EXCEL_STRING = "";
	
	private static final int MAX_KNOWLEDGENO_LENGTH = 6;
	
	private static final int IMPORT_INITIALIZATION = 0;
	
	private static final int IMPORT_KNOWLEDGE = 1;

	@Autowired
	private AdlPhaseSpaceService adlPhaseSpaceService;
	/**
	 * 根据ID查询查询知识点档案
	 * @param knowledgeId	主键
	 * @return	知识点档案
	 */
	@Override
	public CsKnowledgePOJO queryObject(Long knowledgeId){
		if(null !=knowledgeId) {
			CsKnowledgeEntity csKnowledgeEntity = csKnowledgeDao.queryObject(knowledgeId);
			return entityTOPojo(null, csKnowledgeEntity);
		}
		return null;
	}
	
	/**
	 * 查询知识点档案列表
	 * @return	知识点档案列表
	 */
	@Override
	public List<CsKnowledgePOJO> queryList(CsKnowledgeQuery csKnowledgeQuery){
		if(csKnowledgeQuery == null ||(
				EmptyUtils.isEmpty(csKnowledgeQuery.getChapterId())
				&& EmptyUtils.isEmpty(csKnowledgeQuery.getCourseId())
				&& EmptyUtils.isEmpty(csKnowledgeQuery.getSectionId()))
				) {
			throw new RRException("请选择课程或者章节!");
		}
		List<CsKnowledgePOJO> csKnowledgePOJOs = null;
		List<CsKnowledgeEntity> csKnowledgeEntities = csKnowledgeDao.queryList(csKnowledgeQuery);
		if(null != csKnowledgeEntities && !csKnowledgeEntities.isEmpty()) {
			csKnowledgePOJOs = new ArrayList<>();
			for (CsKnowledgeEntity en : csKnowledgeEntities) {
				CsKnowledgePOJO pojo = entityTOPojo(null, en);
				csKnowledgePOJOs.add(pojo);
			}
		}
		return csKnowledgePOJOs;
	}
	/**
	 * entity转POJO
	 * @param csKnowledgePOJO
	 * @param csKnowledgeEntity
	 * @return	pojo
	 */
	private CsKnowledgePOJO entityTOPojo(CsKnowledgePOJO csKnowledgePOJO , CsKnowledgeEntity csKnowledgeEntity) {
		if(csKnowledgeEntity != null) {
			if(null == csKnowledgePOJO) {
				csKnowledgePOJO = new CsKnowledgePOJO();
			}
			csKnowledgePOJO.setChapterId(csKnowledgeEntity.getChapterId());
			csKnowledgePOJO.setCourseId(csKnowledgeEntity.getCourseId());
//			重点:1.正常;0.重点
			csKnowledgePOJO.setKeyPoint(csKnowledgeEntity.getKeyPoint());
			if(null != csKnowledgeEntity.getKeyPoint()) {
				CsConfigPOJO keyPointPOJO = this.csConfigService.queryObject(CsConfigKeyEnum.ADP_KNOWLEDGE_KEY_POINT, String.valueOf(csKnowledgeEntity.getKeyPoint()));
				if(null != keyPointPOJO) {
					csKnowledgePOJO.setKeyPointName(keyPointPOJO.getCname());
				}
			}
			//难度
			csKnowledgePOJO.setLevelId(csKnowledgeEntity.getLevelId());
			if(null != csKnowledgeEntity.getKeyPoint()) {
				CsConfigPOJO levelCP = this.csConfigService.queryObject(CsConfigKeyEnum.KNOWLEDGE_LEVEL, String.valueOf(csKnowledgeEntity.getLevelId()));
				if(null != levelCP) {
					csKnowledgePOJO.setLevelName(levelCP.getCname());
				}
			}
			//知识点信息
			csKnowledgePOJO.setKnowledgeId(csKnowledgeEntity.getKnowledgeId());
			csKnowledgePOJO.setKnowledgeName(csKnowledgeEntity.getKnowledgeName());
			csKnowledgePOJO.setKnowledgeNo(csKnowledgeEntity.getKnowledgeNo());
			//节信息
			if(null != csKnowledgeEntity.getSectionId()) {
				CsSectionPOJO csSectionPOJO = this.csSectionService.queryObject(csKnowledgeEntity.getSectionId());
				if(null != csSectionPOJO) {
					csKnowledgePOJO.setSectionNo(csSectionPOJO.getSectionNo());
					csKnowledgePOJO.setSectionName(csSectionPOJO.getSectionName());
				}
			}
			csKnowledgePOJO.setSectionId(csKnowledgeEntity.getSectionId());
			csKnowledgePOJO.setStatus(csKnowledgeEntity.getStatus());
			//题型
			List<CsKnowledgeQuestiontypePOJO> csKnowledgeQuestiontypePOJOs = this.csKnowledgeQuestiontypeService.queryList(csKnowledgeEntity.getKnowledgeId());
			csKnowledgePOJO.setQuestiontypeList(csKnowledgeQuestiontypePOJOs);
			//包含知识点
			List<CsKnowledgeContainPOJO> csKnowledgeContainPOJOs = this.csKnowledgeContainService.queryListBySelfId(csKnowledgeEntity.getKnowledgeId());
			csKnowledgePOJO.setChildList(csKnowledgeContainPOJOs);
		}
		return csKnowledgePOJO;
	}
	
	/**
	 * 查询知识点档案数量
	 * @return	知识点档案数量
	 */
	@Override
	public int queryTotal(CsKnowledgeQuery csKnowledgeQuery){
		if(csKnowledgeQuery == null ||(
				EmptyUtils.isEmpty(csKnowledgeQuery.getChapterId())
				&& EmptyUtils.isEmpty(csKnowledgeQuery.getCourseId())
				&& EmptyUtils.isEmpty(csKnowledgeQuery.getSectionId()))
				) {
			throw new RRException("请选择课程或者章节！");
		}
		return csKnowledgeDao.queryTotal(csKnowledgeQuery);
	}
	
	/**
	 * 新增知识点档案
	 */
	@Transactional(value = ParamKey.Transactional.hq_courses ,readOnly = false, propagation = Propagation.REQUIRES_NEW,rollbackFor={RuntimeException.class, Exception.class})
	@Override
	public int save(CsKnowledgePOJO csKnowledgeVideoPOJO){
		saveKnowledgeBase(csKnowledgeVideoPOJO);
		return 1;
	}

	/**
	 * 保存知识点基础信息
	 * @param csKnowledgeVideoPOJO
	 * @return
	 */
	private CsKnowledgeEntity saveKnowledgeBase(CsKnowledgePOJO csKnowledgeVideoPOJO) {
		this.verifyForm(csKnowledgeVideoPOJO);
		CsKnowledgeEntity csKnowledge = this.pojoToEntity(csKnowledgeVideoPOJO, null);
		csKnowledge.setDr(0);
		csKnowledge.setStatus(1);
		setCourseIdChapterId(csKnowledge);
		//知识点编号
//		setKnowledgeNo(csKnowledge);
		//编码验证
		checkKnowledgeNo(csKnowledge.getCourseId(),csKnowledge.getKnowledgeId(),csKnowledge.getKnowledgeNo());
		//重名
//		this.checkKnowledgeName(csKnowledge.getCourseId(), csKnowledge.getKnowledgeName(),null);
		//保存知识点基础信息
		csKnowledgeDao.save(csKnowledge);
		//保存知识点包含题型信息
		this.csKnowledgeQuestiontypeService.saveList(csKnowledge.getKnowledgeId(), csKnowledgeVideoPOJO.getQuestiontypeList());
		//保存知识点包含知识点信息
		this.csKnowledgeContainService.saveList(csKnowledge.getKnowledgeId(), csKnowledgeVideoPOJO.getChildList());
		return csKnowledge;
	}


	/**
	 * @param csKnowledge
	 */
	private void setKnowledgeNo(CsKnowledgeEntity csKnowledge) {
		csKnowledge.setKnowledgeNo(this.getKnowledgeNo(csKnowledge.getSectionId()));
	}
	
	/**
	 * 更新知识点档案
	 */
	@Transactional(value = ParamKey.Transactional.hq_courses , propagation = Propagation.REQUIRES_NEW,rollbackFor={RuntimeException.class, Exception.class})
	@Override
	public int update(CsKnowledgePOJO csKnowledgeVideoPOJO){
		this.verifyForm(csKnowledgeVideoPOJO);
		//POJO->entity
		CsKnowledgeEntity csKnowledge = this.pojoToEntity(csKnowledgeVideoPOJO, null);
		//父类ID
		setCourseIdChapterId(csKnowledge);
		//编码验证
		checkKnowledgeNo(csKnowledge.getCourseId(),csKnowledge.getKnowledgeId(),csKnowledge.getKnowledgeNo());
		//重名
		this.checkKnowledgeName(csKnowledge.getCourseId(), csKnowledge.getKnowledgeName(), csKnowledge.getKnowledgeId());
		csKnowledge.setUpdateTime(new Date());
		int update = csKnowledgeDao.update(csKnowledge);
		//保存知识点包含题型信息
		this.csKnowledgeQuestiontypeService.saveList(csKnowledge.getKnowledgeId(), csKnowledgeVideoPOJO.getQuestiontypeList());
		//保存知识点包含知识点信息
		this.csKnowledgeContainService.saveList(csKnowledge.getKnowledgeId(), csKnowledgeVideoPOJO.getChildList());
		return update;
	}
	/**
	 * 知识点章ID和课程ID
	 * @param csKnowledge
	 */
	private void setCourseIdChapterId(CsKnowledgeEntity csKnowledge) {
		CsSectionPOJO csSectionPOJO = this.csSectionService.queryObject(csKnowledge.getSectionId());
		if(null != csSectionPOJO) {
			csKnowledge.setChapterId(csSectionPOJO.getChapterId());
			csKnowledge.setCourseId(csSectionPOJO.getCourseId());
		}
	}
	/**
	 * POJO 转换 Entity
	 * @param csChapterPOJO
	 * @param csKnowledgeEntity
	 * @return
	 */
	private CsKnowledgeEntity pojoToEntity(CsKnowledgePOJO csChapterPOJO , CsKnowledgeEntity csKnowledgeEntity) {
		if(null != csChapterPOJO) {
			if(null == csKnowledgeEntity) {
				csKnowledgeEntity = new CsKnowledgeEntity();
				csKnowledgeEntity.setKnowledgeId(csChapterPOJO.getKnowledgeId());
				csKnowledgeEntity.setCourseId(csChapterPOJO.getCourseId());
				csKnowledgeEntity.setKeyPoint(csChapterPOJO.getKeyPoint());
				csKnowledgeEntity.setKnowledgeId(csChapterPOJO.getKnowledgeId());
				csKnowledgeEntity.setKnowledgeName(csChapterPOJO.getKnowledgeName());
				csKnowledgeEntity.setKnowledgeNo(csChapterPOJO.getKnowledgeNo());
				csKnowledgeEntity.setLevelId(csChapterPOJO.getLevelId());
				csKnowledgeEntity.setSectionId(csChapterPOJO.getSectionId());
				csKnowledgeEntity.setStatus(csChapterPOJO.getStatus());
			}
		}
		return csKnowledgeEntity;
	}
	
	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(CsKnowledgePOJO csChapterPOJO){
		Assert.isNull(csChapterPOJO, "知识点对象不能为空！");
		Assert.isBlank(csChapterPOJO.getKnowledgeName(), "知识点名称不能为空！");
		if(null == csChapterPOJO.getKeyPoint() || 
				(KnowledgeKeyPointEnum.kp.getValue() != csChapterPOJO.getKeyPoint() && 
				KnowledgeKeyPointEnum.normal.getValue() != csChapterPOJO.getKeyPoint())) {
			throw new RRException("知识点考点不能为空！");
		}
		Assert.isNull(csChapterPOJO.getLevelId(), "知识点难度不能为空！");
		Assert.isNull(csChapterPOJO.getSectionId(), "知识点所属节！");
	}
	
	/**
	 * 按照ID删除
	 * @param knowledgeId	主键
	 */
	@Override
	public int updateDr(Long knowledgeId){
		knowledgeQuote(knowledgeId);
		return csKnowledgeDao.updateDr(knowledgeId);
	}

	@Override
	public String getKnowledgeNo(Long sectionId) {
		if(null == sectionId || sectionId <= 0) {
			throw new RRException("节ID获取错误!");
		}
		CsSectionPOJO csSectionPOJO = this.csSectionService.queryObject(sectionId);
		Assert.isNull(csSectionPOJO, "节获取错误!");
		String code = CsCodeUtils.getKnowledgeCode(csSectionPOJO.getSectionNo(), this.csKnowledgeNoKGS.nextId()+"");
		return code;
	}

	@Override
	public int enable(Long knowledgeId) {
		Assert.isNull(knowledgeId, "知识点ID不能为空!");
		return this.updateStatus(knowledgeId, 1);
	}

	@Override
	public int unenable(Long knowledgeId) {
		Assert.isNull(knowledgeId, "知识点ID不能为空!");
		knowledgeQuote(knowledgeId);
		return this.updateStatus(knowledgeId, 0);
	}

	/**
	 * @param knowledgeId
	 */
	private void knowledgeQuote(Long knowledgeId) {
		String quote = checkQuote(knowledgeId);
		Assert.isNotBlank(quote, quote);
	}
	/**
	 * 更新知识点状态
	 * @param knowledgeId
	 * @param status
	 * @return
	 */
	private int updateStatus(Long knowledgeId , Integer status) {
		return this.csKnowledgeDao.updateStatus(knowledgeId, status);
	}

	/**
	 * 知识点是否被引用
	 * @param knowledgeId	知识点ID
	 * @return				被引用说明从;未被引用返回NULL
	 */
	@Override
	public String checkQuote(Long knowledgeId) {
		List<CsKnowledgeContainPOJO> listByChildId = this.csKnowledgeContainService.queryListByChildId(knowledgeId);
		StringBuffer sbf = new StringBuffer();
		if(null != listByChildId && !listByChildId.isEmpty()) {
			sbf.append("以下知识点包含该知识点，请取消后再删除!");
			for(int i = 0 , length = listByChildId.size(); i < length ; i++) {
				CsKnowledgeContainPOJO csKnowledgeContainPOJO = listByChildId.get(i);
				sbf.append("【"+csKnowledgeContainPOJO.getSelfNo() + "】" +csKnowledgeContainPOJO.getSelfName());
				if(i < length-1) {
					sbf.append(",");
				}
			}
		}
		List<AdlPhasePOJO> phaseList = adlPhaseSpaceService.queryKnowledgePhaseList(knowledgeId);
		if(phaseList != null && phaseList.size() > 0){
			sbf.append("<br>");
			sbf.append("以下阶段引用该知识点，请在阶段内取消勾选后再删除！");
			for (int i = 0 , length = phaseList.size(); i < length ; i++){
				AdlPhasePOJO phasePOJO = phaseList.get(i);
				sbf.append("【"+phasePOJO.getPhaseNo() + "】" +phasePOJO.getPhaseName() );
				if(i < length-1) {
					sbf.append(",");
				}
			}
		}
		return sbf.toString();
	}
	/**
	 * 知识点重名校验
	 * @param courseId
	 * @param knowledgeName
	 * @param knowledgeId
	 */
	private void checkKnowledgeName(Long courseId , String knowledgeName , Long knowledgeId) {
		if(queryCountByName(courseId, knowledgeName, knowledgeId)>0) {
			throw new RRException("知识点名称已经被使用！");
		}
	}
	/**
	 * 同一课程下知识点校验名字是否重复
	 * @param courseId		课程ID
	 * @param knowledgeName	知识点名称
	 * @param knowledgeId	不需要校验的知识点
	 */
	private int queryCountByName(Long courseId , String knowledgeName , Long knowledgeId) {
		if(courseId != null && StringUtils.isNotBlank(knowledgeName)) {
			return this.csKnowledgeDao.queryCountByName(courseId, knowledgeName, knowledgeId);
		}
		return 0;
	}

	@Override
	public String downExcel() {
		if(StringUtils.isBlank(DOWN_EXCEL_STRING)) {
			StringBuffer sbf = new StringBuffer();
			//顶部
			sbf.append("0,0,0,0,章编码&0,1,0,0,章名称&0,2,0,0,节编码&0,3,0,0,节名称&0,4,0,0,知识点编码&0,5,0,0,知识点名称&0,6,0,0,难度(分1.2.3.4.5)&0,7,0,0,考点&0,8,0,0,题型(该知识点可能会出题的题型，若有多个请用丨隔开)&0,9,0,0,前续知识点(请忽略该列)&0,10,0,0,VID");
			//1
			sbf.append("&1,0,0,0,cngs01Z01&1,1,0,0,第一章 出纳基础&1,2,0,0,cngs01Z01J01&1,3,0,0,第一节 实训公司概况&1,4,0,0,cngs01Z01J01K001&1,5,0,0,企业名称&1,6,0,0,1&1,7,0,0,否&1,8,0,0,单选题丨多选题丨判断说明题&1,9,0,0,cngsZ01J01K006");
			//2
			sbf.append("&2,0,0,0,cngs01Z01&2,1,0,0,第一章 出纳基础&2,2,0,0,cngs01Z01J01&2,3,0,0,第一节 实训公司概况&2,4,0,0,cngs01Z01J01K002&2,5,0,0,纳税人识别号&2,6,0,0,1&2,7,0,0,是&2,8,0,0,单选题");
			DOWN_EXCEL_STRING = sbf.toString();
		}
		return DOWN_EXCEL_STRING;
	}
	

	@Override
	public void checkKnowledgeNo(Long courseId,Long knowledgeId, String knowledgeNo) {
		if(StringUtils.isBlank(knowledgeNo)) {
			throw new RRException("知识点编号不能为空！");
		}
		CodeValidateUtils.knowledgeCodeValidate(knowledgeNo);
		CsKnowledgeEntity csKnowledgeEntity = csKnowledgeDao.queryObjectKnowledgeNo(courseId,knowledgeId, knowledgeNo);
		if(csKnowledgeEntity != null) {
			throw new RRException("知识点编号已经存在!");
		}
	}
	
	
	/**
	 * 初始化导入
	 */
	@Override
	public String importExcelInitialization(MultipartFile file) {
		return resolveExcute(file,7,new ImportExcelService<String>() {
			@Override
			public String execute(String[] dataItem,int line) {
				System.out.println(">>>>>>>>>"+line+">>>>>>>>>zsdbm");
				//列
				String kcbm = dataItem[CsKnowledgeExcelInitializationColumnEnum.kcbm.getExcelIndex()];
				String zbm = dataItem[CsKnowledgeExcelInitializationColumnEnum.zbm.getExcelIndex()];
				String zmc = dataItem[CsKnowledgeExcelInitializationColumnEnum.zmc.getExcelIndex()];
				String jbm = dataItem[CsKnowledgeExcelInitializationColumnEnum.jbm.getExcelIndex()];
				String jmc = dataItem[CsKnowledgeExcelInitializationColumnEnum.jmc.getExcelIndex()];
				String zsdbm = dataItem[CsKnowledgeExcelInitializationColumnEnum.zsdbm.getExcelIndex()];
				String zsdmc = dataItem[CsKnowledgeExcelInitializationColumnEnum.zsdmc.getExcelIndex()];
				if(StringUtils.isBlank(kcbm)){ return "第" + line + "行错误：" + CsKnowledgeExcelInitializationColumnEnum.kcbm.getColumnName()+"不能为空！" +"<br/>";}
				CsCourseEntity csCourseEntity = csCourseService.queryObject(kcbm);
				if(csCourseEntity == null) {
					return "第" + line + "行错误：" + CsKnowledgeExcelInitializationColumnEnum.kcbm.getColumnName()+":"+kcbm+"不存在！" +"<br/>";
				}
				Long courseId = csCourseEntity.getCourseId();
				String saveExcelColumnString = saveExcelColumn(IMPORT_INITIALIZATION,courseId ,zbm, zmc,jbm, jmc, zsdbm, zsdmc,null,null,null,null,line);
				if(StringUtils.isNotBlank(saveExcelColumnString)) {
					return "第" + line + "行错误：" + saveExcelColumnString +"<br/>";
				}
				return null;
			}
			
		});
	}

	/**
	 * 更新code，根据上一级code编码
	 *
	 * @param csSectionEntity
	 */
	@Transactional(value = ParamKey.Transactional.hq_courses,rollbackFor={RuntimeException.class, Exception.class})
	@Override
	public void updateCodeByParentId(CsSectionEntity csSectionEntity) {
		if(csSectionEntity != null){
			if(null != csSectionEntity){
				CsKnowledgeQuery csKnowledgeQuery = new CsKnowledgeQuery();
				csKnowledgeQuery.setSectionId(csSectionEntity.getSectionId());
				//知识点列表
				List<CsKnowledgePOJO> csKnowledgePOJOS = this.queryList(csKnowledgeQuery);
				if(CollectionUtils.isNotEmpty(csKnowledgePOJOS)){
					//节编号
					String sectionNo = csSectionEntity.getSectionNo();
					for (int i = 0; i < csKnowledgePOJOS.size(); i++) {
						CsKnowledgePOJO csKnowledgePOJO = csKnowledgePOJOS.get(i);
						//原code
						String oldKnowledgeNo = csKnowledgePOJO.getKnowledgeNo();
						//新code
						String newKnowledgeNo = CodeSplitUtils.knowledgeFullCode(oldKnowledgeNo, sectionNo);
						//更新判断
						if(StringUtils.isNotBlank(newKnowledgeNo) && !newKnowledgeNo.equals(oldKnowledgeNo)){
							csKnowledgePOJO.setKnowledgeNo(newKnowledgeNo);
							this.update(csKnowledgePOJO);
						}
					}
				}
			}
		}
	}

	/**
	 * 批量导入
	 * @param file
	 * @return
	 */
	@Transactional(value = ParamKey.Transactional.hq_courses ,readOnly = false, propagation = Propagation.REQUIRES_NEW,rollbackFor={RuntimeException.class, Exception.class})
	@Override
	public String importExcel(final Long courseId , MultipartFile file) {
		return resolveExcute(file,6,new ImportExcelService<String>() {

			@Override
			public String execute(String[] dataItem, int line) {
				//列
				String zbm = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.zbm.getExcelIndex()]);
				String zmc = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.zmc.getExcelIndex()]);
				String jbm = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.jbm.getExcelIndex()]);
				String jmc = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.jmc.getExcelIndex()]);
				String zsdbm = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.zsdbm.getExcelIndex()]);
				String zsdmc = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.zsdmc.getExcelIndex()]);
				String nd = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.nd.getExcelIndex()]);
				String kd = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.kd.getExcelIndex()]);
				String tx = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.tx.getExcelIndex()]);
				String qxzsd = StringUtils.trim(dataItem[CsKnowledgeExcelColumnEnum.qxzsd.getExcelIndex()]);
				String saveExcelColumnString = saveExcelColumn(IMPORT_KNOWLEDGE,courseId ,zbm, zmc,jbm, jmc, zsdbm, zsdmc, nd, kd, tx, qxzsd,line);
				if(StringUtils.isNotBlank(saveExcelColumnString)) {
					return "第" + line + "行错误：" + saveExcelColumnString +"<br/>";
				}
				return null;
			}
		});
	}
	

	/**
	 * 导入公共代码
	 * @param file 文件
	 * @param columnLength 列数
	 * @param ex 接口对象
	 * @return
	 */
	private <T> String resolveExcute(MultipartFile file,int columnLength,ImportExcelService<T> ex) {
		if(null != file) {
			StringBuffer errorMsg = new StringBuffer();
			try {
				FileInputStream fio = (FileInputStream) file.getInputStream();
				List<String[]> dataList = ExcelReaderJXL.readExcel(fio);
				String[] header = dataList.get(0);
				if(null == dataList || dataList.isEmpty() || dataList.size() <= 1) {
					throw new RRException("文件或内容有问题，请重新导入！");
				}
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
					String err = (String) ex.execute(dataItem,line);
					if(err != null) {
						errorMsg.append(err);
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
	 * 保存章、节、知识点信息
	 * @param importType 0初始化导入 1知识点导入
	 * @param courseId
	 * @param zbm
	 * @param zmc
	 * @param jbm
	 * @param jmc
	 * @param zsdbm
	 * @param zsdmc
	 * @param nd
	 * @param kd
	 * @param tx
	 * @param qxzsd
	 * @param line
	 * @return
	 */
	private String saveExcelColumn(int importType,long courseId,String zbm ,
			String zmc,String jbm,String jmc,String zsdbm,String zsdmc ,String nd,String kd,String tx,String qxzsd ,Integer line) {
		//非空校
		if(StringUtils.isBlank(zbm)){ return CsKnowledgeExcelInitializationColumnEnum.zbm.getColumnName()+"不能为空！";}
		CsChapterEntity csChapterEntity = csChapterService.queryObjectByChapterNo(zbm);
		if(csChapterEntity == null) {
			if(StringUtils.isBlank(zmc)){ return CsKnowledgeExcelInitializationColumnEnum.zmc.getColumnName()+"不能为空！";}
            CsChapterEntity csChapter = new CsChapterEntity();
            csChapter.setCourseId(courseId);
            csChapter.setChapterNo(zbm);
            csChapter.setChapterName(zmc);
            String parent = csCourseService.queryObject(courseId).getCourseNo();
            String result = CodeValidateUtils.codeValidate(zbm, CodeValidateUtils.CSCHAPTER_PATTERN,parent,CodeValidateUtils.CSCHAPTER_TYPE);
            if(StringUtils.isNotBlank(result)){
                return result;
            }
			BeanHelper.beanAttributeValueTrim(csChapter);
			csChapterService.save(csChapter);
            csChapterEntity = csChapterService.queryObjectByChapterNo(zbm);
		}else if(csChapterEntity.getDr().equals(1)){
			return "历史记录存在该章编码，请修改章编码以及包含的节编码、知识点编码！！！";
		}
		Long chapterId = csChapterEntity.getChapterId();
		if(StringUtils.isNotBlank(jmc) && StringUtils.isBlank(jbm)) {
			return CsKnowledgeExcelInitializationColumnEnum.jbm.getColumnName()+"不能为空！";
		}
		if(StringUtils.isNotBlank(jbm)) {//节编码不为空
			CsSectionEntity csSectionEntity = csSectionService.queryObjectBySectionNo(jbm);
			if(csSectionEntity == null) {
				if(StringUtils.isBlank(jmc)){ return CsKnowledgeExcelInitializationColumnEnum.jmc.getColumnName()+"不能为空！";}
				CsSectionEntity csSection = new CsSectionEntity();
				csSection.setChapterId(chapterId);
				csSection.setSectionNo(jbm);
				csSection.setSectionName(jmc);
				csSection.setOrderNum(1);
                String result = CodeValidateUtils.codeValidate(jbm, CodeValidateUtils.CSSECTION_PATTERN,zbm,CodeValidateUtils.CSSECTION_TYPE);
                if(StringUtils.isNotBlank(result)){
                    return result;
                }
				BeanHelper.beanAttributeValueTrim(csSection);
				csSectionService.save(csSection);
				csSectionEntity = csSectionService.queryObjectBySectionNo(jbm);
			}else if(csSectionEntity.getDr().equals(1)){
				return "历史记录存在该节编码，请修改节编码以及包含的知识点编码！！！";
			}
			Long sectionId = csSectionEntity.getSectionId();
			if(StringUtils.isNotBlank(zsdbm) && StringUtils.isNotBlank(zsdmc)) {
				CsKnowledgeEntity csKnowledgeEntity = csKnowledgeDao.queryObjectKnowledgeNo(courseId,null, zsdbm);
				if(csKnowledgeEntity != null && csKnowledgeEntity.getDr().equals(1)) {
					return "历史记录存在该知识点编码，请修改知识点编码！！！";
				}
				if(csKnowledgeEntity != null){
					return "该知识点存在，不允许重复导入！！！";
				}
				String result = CodeValidateUtils.codeValidate(zsdbm, CodeValidateUtils.CSKNOWLEDGE_PATTERN,jbm,CodeValidateUtils.CSKNOWLEDGE_TYPE);
				if(StringUtils.isNotBlank(result)){
					return result;
				}
				if(importType == 1) {
					//导入知识点
					return saveExcelKnowledgeColumn(courseId,sectionId,zsdbm,zsdmc, nd, kd, tx, qxzsd , line);
				}else {
					//初始化导入知识点
					CsKnowledgePOJO csKnowledgePOJO = new CsKnowledgePOJO();
					csKnowledgePOJO.setCourseId(courseId);
					csKnowledgePOJO.setSectionId(sectionId);
					csKnowledgePOJO.setKnowledgeNo(zsdbm);
					csKnowledgePOJO.setKnowledgeName(zsdmc);
					csKnowledgePOJO.setKeyPoint(1);
					csKnowledgePOJO.setLevelId(1);
//					if(queryCountByName(courseId, zsdmc, null)>0) {
//						return "知识点名称已经被使用！";
//					}
					BeanHelper.beanAttributeValueTrim(csKnowledgePOJO);
					save(csKnowledgePOJO);
				}
			}else if(StringUtils.isBlank(zsdbm) && StringUtils.isBlank(zsdmc)){
				
			}else {
				return "知识点信息不能为空！";
			}
		}else if(StringUtils.isNotBlank(zsdbm) || StringUtils.isNotBlank(zsdmc)){//知识点信息存在，节编码为空
			return CsKnowledgeExcelInitializationColumnEnum.jmc.getColumnName()+"不能为空！";
		}
		return null;
	}
	/**
	 * 保存excel的知识点
	 * @param courseId
	 * @param sectionId
	 * @param zsdbm
	 * @param zsdmc
	 * @param nd
	 * @param kd
	 * @param tx
	 * @param qxzsd
	 * @param line
	 * @return
	 */
	private String saveExcelKnowledgeColumn(Long courseId, long sectionId,String zsdbm ,
			String zsdmc,String nd,String kd,String tx,String qxzsd , Integer line) {
		//非空校验
		if(StringUtils.isBlank(zsdbm)){ return CsKnowledgeExcelColumnEnum.zsdbm.getColumnName()+"不能为空！";}
		if(StringUtils.isBlank(zsdmc)){ return CsKnowledgeExcelColumnEnum.zsdmc.getColumnName()+"不能为空！";}
		if(StringUtils.isBlank(nd)){ return CsKnowledgeExcelColumnEnum.nd.getColumnName()+"不能为空！";}
		if(StringUtils.isBlank(kd)){ return CsKnowledgeExcelColumnEnum.kd.getColumnName()+"不能为空！";}
		if(StringUtils.isBlank(tx)){ return CsKnowledgeExcelColumnEnum.tx.getColumnName()+"不能为空！";}
		//节信息
		CsKnowledgePOJO csKnowledgePOJO = new CsKnowledgePOJO();
		//知识点名称
		csKnowledgePOJO.setKnowledgeName(zsdmc);
		//知识点编码
		csKnowledgePOJO.setKnowledgeNo(zsdbm.trim());
//		//难度
		String ndString = this.csConfigService.queryNameByValue(CsConfigKeyEnum.KNOWLEDGE_LEVEL.getCkey(), nd);
		if(StringUtils.isBlank(ndString)) {
			return CsKnowledgeExcelColumnEnum.nd.getColumnName()+"错误!";
		}
		csKnowledgePOJO.setLevelId(Integer.valueOf(ndString));
		//考点
		Integer keyPoint = 1;
		if(kd.indexOf("是") > -1 ||kd.indexOf("考点") > -1 ||kd.indexOf("重点") > -1) keyPoint = 2;
		csKnowledgePOJO.setKeyPoint(keyPoint);
		csKnowledgePOJO.setStatus(1);
		//父ID
		csKnowledgePOJO.setSectionId(sectionId);
		csKnowledgePOJO.setCourseId(courseId);
		// 题型
		String[] txNameArray = tx.split("\\||丨");
		List<CsKnowledgeQuestiontypePOJO> questiontypeList = new ArrayList<>();;
		try {
			for (String txName : txNameArray) {
				String txValue = this.csConfigService.queryNameByValue(CsConfigKeyEnum.ADP_KNOWLEDGE_QUESTION_TYPE.getCkey(), txName);
				if(StringUtils.isBlank(txValue)) {
					throw new RRException(CsKnowledgeExcelColumnEnum.tx.getColumnName()+"【"+txName+"】错误!");
				}
				CsKnowledgeQuestiontypePOJO csKnowledgeQuestiontypePOJO= new CsKnowledgeQuestiontypePOJO(); 
				csKnowledgeQuestiontypePOJO.setCname(txName);
				csKnowledgeQuestiontypePOJO.setQuestiontypeId(Long.valueOf(txValue));
				questiontypeList.add(csKnowledgeQuestiontypePOJO);
			}
		} catch (RRException e) {
			logger.error("e.getMessageis{ }",e);
			return e.getMsg();
		}
		
		if(CollectionUtils.isEmpty(questiontypeList)){ return "题型错误!";}
		csKnowledgePOJO.setQuestiontypeList(questiontypeList);
		
		// 包含知识点集合
//		List<CsKnowledgeContainPOJO> childList = null;
//		if(StringUtils.isNotBlank(qxzsd)) {
//			childList = new ArrayList<>();
//			csKnowledgePOJO.setChildList(childList);
//			String[] qxzsdArrays = qxzsd.split("\\||丨");
//			try {
//				for (String knowledgeNo : qxzsdArrays) {
//					CsKnowledgeEntity csKnowledgeEntity = this.csKnowledgeDao.queryObjectKnowledgeNo(courseId,null, knowledgeNo);
//					if(null == csKnowledgeEntity) {
//						throw new RRException(CsKnowledgeExcelColumnEnum.qxzsd.getColumnName()+"【"+knowledgeNo+"】不存在!");
//					}
//					CsKnowledgeContainPOJO csKnowledgeContainPOJO = new CsKnowledgeContainPOJO();
//					csKnowledgeContainPOJO.setChildId(csKnowledgeEntity.getKnowledgeId());
//					childList.add(csKnowledgeContainPOJO);
//				}
//			} catch (RRException e) {
//				return e.getMsg();
//			}
//		}
		if(queryCountByName(courseId, zsdmc, null)>0) {
			return "知识点名称已经被使用！";
		}
		try {
			this.saveKnowledgeBase(csKnowledgePOJO);
		} catch (RRException e) {
			logger.error("e.getMessageis{ }",e);
			return e.getMsg();
		}
		return null;
	}
	/**
	 * 校验知识点点空间是否出现闭环
	 *
	 * @param courseId
	 * @return
	 */
	@Override
	public R checkKnowledge(Long courseId) {
		Assert.isNull(courseId , "课程PK不能为空!");
		//知识点查询条件
		CsKnowledgeQuery csKnowledgeQuery = new CsKnowledgeQuery();
		csKnowledgeQuery.setCourseId(courseId);
		//知识点集合
		List<CsKnowledgePOJO> csKnowledgePOJOS = this.queryList(csKnowledgeQuery);
		if(CollectionUtils.isNotEmpty(csKnowledgePOJOS)){
			//查询条件-对象
			ASCyclicTopologyPOJO asCyclicTopologyPOJO = getAsCyclicTopologyPOJO(courseId, csKnowledgePOJOS);
			return AdaptiveServiceHttpUtils.cyclicTopology(asCyclicTopologyPOJO);
		}else{
			throw new RRException("该课程不存在知识点!");
		}
	}

	private ASCyclicTopologyPOJO getAsCyclicTopologyPOJO(Long courseId, List<CsKnowledgePOJO> csKnowledgePOJOS) {
		ASCyclicTopologyPOJO asCyclicTopologyPOJO = new ASCyclicTopologyPOJO(courseId);
		//查询条件-知识点集合
		List<ASCyclicTopologyKnowledgeNode> knowledgeNodeSet = asCyclicTopologyPOJO.getKnowledgeNodeSet();
		asCyclicTopologyPOJO.setKnowledgeNodeSet(knowledgeNodeSet);
		//迭代知识点
		for (int i = 0; i < csKnowledgePOJOS.size(); i++) {
            //某知识点
            CsKnowledgePOJO csKnowledgePOJO = csKnowledgePOJOS.get(i);
            //某知识点PK
            Long knowledgeId = csKnowledgePOJO.getKnowledgeId();
            //某知识点包含的知识点集合
            List<CsKnowledgeContainPOJO> csKnowledgeContainPOJOS = this.csKnowledgeContainService.queryListBySelfId(knowledgeId);

            //查询条件-知识点对象
            ASCyclicTopologyKnowledgeNode asCyclicTopologyKnowledgeNode = new ASCyclicTopologyKnowledgeNode();
            //查询条件-知识点包含节点集合(包含本级知识点)
            List<Long> priorNodeSet = new ArrayList<>();
//            priorNodeSet.add(knowledgeId);
            if(CollectionUtils.isNotEmpty(csKnowledgeContainPOJOS)){
                for (int j = 0; j < csKnowledgeContainPOJOS.size(); j++) {
                    priorNodeSet.add(csKnowledgeContainPOJOS.get(j).getChildId());
                }
            }

            asCyclicTopologyKnowledgeNode.setNodeId(knowledgeId);
            asCyclicTopologyKnowledgeNode.setPriorNodeSet(priorNodeSet);
            asCyclicTopologyKnowledgeNode.setNextNodeQuantity(priorNodeSet.size());
            knowledgeNodeSet.add(asCyclicTopologyKnowledgeNode);
        }
		return asCyclicTopologyPOJO;
	}

}
