package com.hq.adaptive.service.impl;

import com.hq.adaptive.dao.AdlPhaseDao;
import com.hq.adaptive.entity.AdlPhaseEntity;
import com.hq.adaptive.entity.AdlPhaseSectionEntity;
import com.hq.adaptive.entity.AdlPhaseSpaceEntity;
import com.hq.adaptive.pojo.*;
import com.hq.adaptive.pojo.query.AdlKnowledgeQuery;
import com.hq.adaptive.pojo.query.AdlPhaseQuery;
import com.hq.adaptive.pojo.query.AdlPhaseSectionQuery;
import com.hq.adaptive.service.*;
import com.hq.courses.service.CsKnowledgeService;
import io.renren.common.doc.ParamKey;
import io.renren.common.validator.Assert;
import io.renren.utils.R;
import io.renren.utils.RRException;
import io.renren.utils.knowledge.KnowledgeHashObj;
import io.renren.utils.knowledge.KnowledgeHashUtils;
import io.renren.utils.service.adaptive.http.AdaptiveServiceHttpUtils;
import io.renren.utils.service.adaptive.pojo.AdlDecisionForestNode;
import io.renren.utils.service.adaptive.pojo.AdlDecisionForestPOJO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 评测阶段表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
@Service("adlPhaseService")
public class AdlPhaseServiceImpl implements AdlPhaseService {
	@Autowired
	private AdlPhaseDao adlPhaseDao;
	@Autowired
	private AdlConfigService adlConfigService;
	@Autowired
	private com.hq.adaptive.service.AdlSectionService adlSectionService;
	@Autowired
	private com.hq.adaptive.service.AdlKnowledgeService adlKnowledgeService;
	@Autowired
	private AdlPhaseSpaceService adlPhaseSpaceService;
	@Autowired
	private AdlKnowledgeContainService adlKnowledgeContainService;
	@Autowired
	private CsKnowledgeService csKnowledgeService;

	@Autowired
	private AdlPhaseFilesService adlPhaseFilesService;

	private static final long PHASE_TYPE_BEFORE = 1;

	private static final long PHASE_TYPE_AFTER = 2;

	private static final boolean ADD_CHILD_KNOWLEDGE = true;
	/**
	 * 根据ID查询查询评测阶段表
	 * @param phaseId	主键
	 * @return	评测阶段表
	 */
	@Override
	public AdlPhasePOJO queryObject(Long phaseId){
		AdlPhaseQuery adlPhaseQuery = new AdlPhaseQuery();
		adlPhaseQuery.setPhaseId(phaseId);
		AdlPhasePOJO adlPhasePOJO = adlPhaseDao.queryObject(adlPhaseQuery);
		if(null != adlPhasePOJO){
			adlPhasePOJO.setKnowledgeIdList(adlPhaseSpaceService.queryKnowledgeList(phaseId));
		}
		return adlPhasePOJO;
	}
	
	/**
	 * 查询评测阶段表列表
	 * @param adlPhaseQuery	查询条件
	 * @return	评测阶段表列表
	 */
	@Override
	public List<AdlPhasePOJO> queryList(AdlPhaseQuery adlPhaseQuery){
		List<AdlPhasePOJO> adlPhasePOJOs = adlPhaseDao.queryList(adlPhaseQuery);
		if(null != adlPhasePOJOs && !adlPhasePOJOs.isEmpty()) {
			for (AdlPhasePOJO adlPhasePOJO : adlPhasePOJOs) {
				//考点
				if(null != adlPhasePOJO.getKeyPoint()) {
					AdlConfigPOJO keyPointPOJO = this.adlConfigService.queryObject(AdlConfigKeyEnum.ADP_KNOWLEDGE_KEY_POINT, String.valueOf(adlPhasePOJO.getKeyPoint()));
					if(null != keyPointPOJO) {
						adlPhasePOJO.setKeyPointName(keyPointPOJO.getCname());
					}
				}
				//知识点数量
				int knowledgeCount = getKnowledgeCount(adlPhasePOJO.getPhaseId());
				adlPhasePOJO.setKnowledgeCount(knowledgeCount);
			}
		}
		return adlPhasePOJOs;
	}

	/**
	 * 阶段知识点数量
	 * @param phaseId
	 * @return
	 */
	private int getKnowledgeCount(Long phaseId) {
		return adlPhaseSpaceService.queryKnowledgeTotal(phaseId);
	}

	/**
	 * 阶段包含节POJOs
	 * @param adlPhaseSectionEntities
	 * @return
	 */
	private List<AdlPhaseSectionPOJO> getAdlPhaseSectionPOJOS(List<AdlPhaseSectionEntity> adlPhaseSectionEntities) {
		List<AdlPhaseSectionPOJO> sectionList = null;
		if(null != adlPhaseSectionEntities && !adlPhaseSectionEntities.isEmpty()) {
            sectionList = new ArrayList<>();
            for (AdlPhaseSectionEntity adlPhaseSectionEntity : adlPhaseSectionEntities){
                AdlPhaseSectionPOJO adlPhaseSectionPOJO = getAdlPhaseSectionPOJO(adlPhaseSectionEntity);
                sectionList.add(adlPhaseSectionPOJO);
            }
        }
		return sectionList;
	}

	/**
	 * entity转POJO
	 * @param adlPhaseSectionEntity
	 * @return
	 */
	private AdlPhaseSectionPOJO getAdlPhaseSectionPOJO(AdlPhaseSectionEntity adlPhaseSectionEntity) {
		AdlPhaseSectionPOJO adlPhaseSectionPOJO = new AdlPhaseSectionPOJO();
		//课程ID
		adlPhaseSectionPOJO.setCourseId(adlPhaseSectionEntity.getCourseId());
		//ID
		adlPhaseSectionPOJO.setId(adlPhaseSectionEntity.getId());
		//阶段ID
		adlPhaseSectionPOJO.setPhaseId(adlPhaseSectionEntity.getPhaseId());
		//节ID
		adlPhaseSectionPOJO.setSectionId(adlPhaseSectionEntity.getSectionId());
		//节对象
		AdlSectionPOJO adlSectionPOJO = this.adlSectionService.queryObject(adlPhaseSectionEntity.getSectionId());
		if(null != adlSectionPOJO){
            //节名称
            adlPhaseSectionPOJO.setSectionName(adlSectionPOJO.getSectionName());
            //节编号
            adlPhaseSectionPOJO.setSectionNo(adlSectionPOJO.getSectionNo());
        }
		return adlPhaseSectionPOJO;
	}

	/**
	 * 查询评测阶段表数量
	 * @return	评测阶段表数量
	 */
	@Override
	public int queryTotal(AdlPhaseQuery adlPhaseQuery){
		return adlPhaseDao.queryTotal(adlPhaseQuery);
	}
	
	/**
	 * 新增评测阶段表
	 */
	@Transactional(value = ParamKey.Transactional.hq_adaptive ,readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public int save(AdlPhasePOJO adlPhase){
		AdlPhaseEntity adlPhaseEntity = this.pojoToEntity(adlPhase);
		//默认值
		adlPhaseEntity.setDr(0);
		adlPhaseEntity.setStatus(1);
		//版本
		adlPhaseEntity.setVersionHash(KnowledgeHashUtils.defaultStr());
		adlPhaseEntity.setVersionNo(KnowledgeHashUtils.defaultNo());
		if(this.checkPhaseName(adlPhase.getCourseId(), null, adlPhase.getPhaseName())) {throw new RRException("阶段名称不能重复!");}
		if(this.checkPhaseNo(null, adlPhase.getPhaseNo())) {throw new RRException("阶段编号不能重复!");}
		//保存阶段信息
		adlPhaseDao.save(adlPhaseEntity);
		//保存或更新阶段包含知识点列表
		this.adlPhaseSpaceService.saveOrUpdate(adlPhaseEntity,adlPhase.getKnowledgeIdList());
		return 1;
	}

	private String getVersionHashString(Long phaseId) {
		//默认hash值
		String versionHash = KnowledgeHashUtils.defaultStr();
		List<Long> knowledgeIdList = adlPhaseSpaceService.queryKnowledgeList(phaseId);
		//知识点集合非空
		if(CollectionUtils.isNotEmpty(knowledgeIdList)){
			//知识点包含知识点关系集合-生成hash参数
			List<KnowledgeHashObj> knowledgeHashObjs = new ArrayList<>();
			for (int i = 0; i < knowledgeIdList.size(); i++) {
				Long knowledge = knowledgeIdList.get(i);
				//校验该知识点是否已经放入knowledgeHashObjs中
				for (int j = 0; j < knowledgeHashObjs.size(); j++) {
					if(knowledge.equals(knowledgeHashObjs.get(j).getKnowledegId())){
						break;
					}
				}
				//知识点包含的子集知识点PK集合
				List<Long> childList = new ArrayList<>();
				//查询知识点包含子集PK集合
				List<AdlKnowledgeContainPOJO> adlKnowledgeContainPOJOS = this.adlKnowledgeContainService.queryListBySelfId(knowledge);
				if(CollectionUtils.isNotEmpty(adlKnowledgeContainPOJOS)){
					for (int j = 0; j < adlKnowledgeContainPOJOS.size(); j++) {
						childList.add(adlKnowledgeContainPOJOS.get(j).getChildId());
					}
				}
				//
				knowledgeHashObjs.add(new KnowledgeHashObj(knowledge , childList));
			}
			//生成hash值
			versionHash = KnowledgeHashUtils.toStr(knowledgeHashObjs);
		}
		return versionHash;
	}

	/**
	 * 更新评测阶段表
	 */
	@Transactional(value = ParamKey.Transactional.hq_adaptive ,readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public int update(AdlPhasePOJO adlPhase){
		AdlPhaseEntity adlPhaseEntity = this.pojoToEntity(adlPhase);
		if(this.checkPhaseName(adlPhase.getCourseId(), adlPhase.getPhaseId(), adlPhase.getPhaseName())) {throw new RRException("阶段名称不能重复!");}
		if(this.checkPhaseNo(adlPhase.getPhaseId(), adlPhase.getPhaseNo())) {throw new RRException("阶段编号不能重复!");}
		adlPhaseDao.update(adlPhaseEntity);
        //保存或更新阶段下节包含的知识点信息
        this.adlPhaseSpaceService.saveOrUpdate(adlPhaseEntity,adlPhase.getKnowledgeIdList());
		return 1;
	}

	/**
	 * 按照ID删除
	 * @param phaseId	主键
	 */
	@Override
	public int delete(Long phaseId){
		return adlPhaseDao.delDr(phaseId);
	}

	/**
	 * 启用
	 * @param phaseId
	 * @return
	 */
	@Override
	public int enablePhase(Long phaseId) {
		return this.adlPhaseDao.updateStatus(1 , phaseId);
	}

	/**
	 * 禁用
	 * @param phaseId
	 * @return
	 */
	@Override
	public int disablePhase(Long phaseId) {
		return this.adlPhaseDao.updateStatus(0 , phaseId);
	}

	/**
	 * 校验阶段名称是否重复
	 *
	 * @param courseId  课程ID
	 * @param phaseId   不需要校验的阶段ID
	 * @param phaseName 要校验的阶段名称
	 * @return true:存在 false:不存在
	 */
	@Override
	public boolean checkPhaseName(Long courseId, Long phaseId, String phaseName) {
		Assert.isNull(courseId , "请上传课程ID!");
		Assert.isBlank(phaseName , "请输入阶段名称!");
		return this.adlPhaseDao.selectPhaseNameTotal(courseId ,phaseId , phaseName) > 0;
	}

	/**
	 * 校验阶段编号是否重复
	 *
	 * @param phaseId 不需要校验的阶段ID
	 * @param phaseNo 需要校验的阶段编号
	 * @return true:存在 false:不存在
	 */
	@Override
	public boolean checkPhaseNo(Long phaseId, String phaseNo) {
		Assert.isBlank(phaseNo , "请输入阶段编号!");
		return this.adlPhaseDao.selectPhaseNoTotal(phaseId , phaseNo) > 0;
	}

	/**
	 * POJO转Entity
	 * @param adlPhasePOJO	pojo
	 * @return				entity
	 */
	private AdlPhaseEntity pojoToEntity(AdlPhasePOJO adlPhasePOJO) {
		AdlPhaseEntity adlPhaseEntity = new AdlPhaseEntity();
		adlPhaseEntity.setCourseId(adlPhasePOJO.getCourseId());
		adlPhaseEntity.setKeyPoint(adlPhasePOJO.getKeyPoint());
		adlPhaseEntity.setPhaseId(adlPhasePOJO.getPhaseId());
		adlPhaseEntity.setPhaseName(adlPhasePOJO.getPhaseName());
		adlPhaseEntity.setPhaseNo(adlPhasePOJO.getPhaseNo());
		adlPhaseEntity.setStatus(adlPhasePOJO.getStatus());
		adlPhaseEntity.setVersionNo(adlPhasePOJO.getVersionNo());
		adlPhaseEntity.setVersionHash(adlPhaseEntity.getVersionHash());
		return adlPhaseEntity;
	}

	/**
	 * 更新阶段知识点空间版本
	 *
	 * @param phaseId 阶段ID
	 * @param update	强制更新 true=强制更新
	 * @return 错误信息
	 */
	@Transactional(value = ParamKey.Transactional.hq_adaptive ,readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public String updatePhaceKnowledgeVersion(Long phaseId , Boolean update) {
		Assert.isNull(phaseId , "阶段PK不能为空!");

		//阶段对象
		AdlPhasePOJO adlPhasePOJO = this.queryObject(phaseId);
		Assert.isNull(adlPhasePOJO , "阶段查询错误!");
		// 阶段版本号
		String versionHashString = getVersionHashString(phaseId);
		if(StringUtils.isBlank(versionHashString) || KnowledgeHashUtils.defaultStr().equals(versionHashString)){
			throw new RRException("阶段下的知识点不能为空!");
		}

		//版本是否是最新
		if(!Boolean.TRUE.equals(update) && adlPhasePOJO.getVersionNo() > KnowledgeHashUtils.defaultNo() &&
				versionHashString.equals(adlPhasePOJO.getVersionHash())){
			throw new RRException("阶段下的知识点空间已经是最新的了!");
		}
		//调用服务 生成阶段下知识点空间
		R checkKnowledgeR = this.csKnowledgeService.checkKnowledge(adlPhasePOJO.getCourseId());
		if(!checkKnowledgeR.isOK()){
			throw new RRException((String) checkKnowledgeR.get("msg"));
		}
		Long courseId = adlPhasePOJO.getCourseId();
		//最新版本
		int versionNo = (int)(adlPhasePOJO.getVersionNo() + 1);
		//生成知识点空间的知识点集合
		AdlDecisionForestPOJO adlDecisionForestPOJO = getAdlDecisionForestPOJO(phaseId, courseId, versionNo);
		//请求生成知识点空间方法
		R r = AdaptiveServiceHttpUtils.decisionForest(adlDecisionForestPOJO);
		int code = (int) r.get("code");
		if(0 == code){
			//update version
			AdlPhaseEntity adlPhaseEntity = new AdlPhaseEntity();
			adlPhaseEntity.setPhaseId(phaseId);
			//hash值
			adlPhaseEntity.setVersionHash(versionHashString);
			//版本
			adlPhaseEntity.setVersionNo(Long.valueOf(versionNo));
			this.adlPhaseDao.update(adlPhaseEntity);
            //保存或更新阶段下包含的知识点信息
//            this.adlPhaseSpaceService.saveOrUpdate(adlPhaseEntity);
		}else{
			return (String) r.get("msg");
		}
		return null;
	}

	/**
	 * 生成知识网络森林
	 * @param phaseId	阶段PK
	 * @param courseId	课程PK
	 * @param versionNo	版本号
	 * @return			生成结果
	 */
	private AdlDecisionForestPOJO getAdlDecisionForestPOJO(Long phaseId, Long courseId, int versionNo) {
		//阶段包含知识点
		List<AdlPhaseSpaceEntity> adlPhaseSpaceEntities = this.adlPhaseSpaceService.queryList(phaseId);
		if(CollectionUtils.isEmpty(adlPhaseSpaceEntities)){
			throw new RRException("阶段包含的知识点为空!");
		}
		//请求参数
		AdlDecisionForestPOJO adlDecisionForestPOJO = new AdlDecisionForestPOJO();
		//阶段ID
		adlDecisionForestPOJO.setPhaseId(phaseId);
		//课程ID
		adlDecisionForestPOJO.setCourseId(courseId);
		//版本
		adlDecisionForestPOJO.setVersionNo(versionNo);

		//有序知识点集合.
		List<AdlDecisionForestNode> graphNodeSet = new ArrayList<>();
		adlDecisionForestPOJO.setGraphNodeSet(graphNodeSet);
		//无需知识点集合
		List<AdlDecisionForestNode> randomNodeSet = new ArrayList<>();
		adlDecisionForestPOJO.setRandomNodeSet(randomNodeSet);
		//迭代 阶段包含的知识点
		for (int i = 0; i < adlPhaseSpaceEntities.size(); i++) {
			//知识点对象
			AdlPhaseSpaceEntity adlPhaseSpaceEntity = adlPhaseSpaceEntities.get(i);
			//本级知识点ID
			Long selfId = adlPhaseSpaceEntity.getSelfId();
			//判断知识点是否出现重复
			if(this.nodeListContains(selfId , graphNodeSet)== null && this.nodeListContains(selfId , randomNodeSet)==null){
				//树形知识点
				if(isGraph(selfId)){
					//子集知识点下包含的知识点
					graphNodeAddKnowledge(selfId , graphNodeSet);
				}else{//无序
					randomNodeSet.add(getAdlDecisionForestNode(selfId));
				}
			}
		}

		//TODO 处理有序知识点格式问题
		//A->B->C
		//处理前 A(A) B(AB) C(BC)
		//处理后 A(A) B(AB) C(ABC)
		nodeChildAddAll(graphNodeSet);
		return adlDecisionForestPOJO;
	}

	private AdlDecisionForestNode getAdlDecisionForestNode(Long selfId) {
		AdlDecisionForestNode adlDecisionForestNode = new AdlDecisionForestNode();
        //知识点PK
		adlDecisionForestNode.setNodeId(selfId);
        //包含知识点集合
		adlDecisionForestNode.setPriorNodeSet(this.addChildId(selfId , null));
		return adlDecisionForestNode;
	}

	private void graphNodeAddKnowledge(Long knowledgeId ,List<AdlDecisionForestNode> graphNodeSet){
		if(null == knowledgeId){
			return;
		}
		//查询graphNodeSet集合中是否有该知识点的对象
		//如果有该知识点,则不需处理
		if(nodeListContains(knowledgeId, graphNodeSet) != null){
			return;
		}
		//如果没有该知识点对象,创建一个该知识点对象
		AdlDecisionForestNode adlDecisionForestNode = getAdlDecisionForestNode(knowledgeId);
		//
		graphNodeSet.add(adlDecisionForestNode);

		if(ADD_CHILD_KNOWLEDGE){
			addKnowledgeChild(graphNodeSet, adlDecisionForestNode);
		}
	}

	/**
	 * 递归处理未选中节但是和本节知识点有关联的知识点加入集合中
	 * @param graphNodeSet
	 * @param adlDecisionForestNode
	 */
	private void addKnowledgeChild(List<AdlDecisionForestNode> graphNodeSet, AdlDecisionForestNode adlDecisionForestNode) {
		List<Long> childsKnowledgeIds = adlDecisionForestNode.getPriorNodeSet();
		if(CollectionUtils.isNotEmpty(childsKnowledgeIds)){
            for (int i = 0; i < childsKnowledgeIds.size(); i++) {
                graphNodeAddKnowledge(childsKnowledgeIds.get(i) , graphNodeSet);
            }
        }
	}

//	private List<AdlDecisionForestNode> nodeChildAddAll_(List<AdlDecisionForestNode> graphNodeSet){
//		if(CollectionUtils.isNotEmpty(graphNodeSet)){
//			//List => Map
//			Map<Long,AdlDecisionForestNode> nodeMap = new HashMap<>();
//			for (int i = 0; i < graphNodeSet.size(); i++) {
//				AdlDecisionForestNode adlDecisionForestNode = graphNodeSet.get(i);
//				nodeMap.put(adlDecisionForestNode.getNodeId() , adlDecisionForestNode);
//			}
//		}
//		return graphNodeSet;
//	}
//
//	/**
//	 * 处理有序知识点格式问题
//	 * A->B->C
//	 * 处理前 A(A) B(AB) C(BC)
//	 * 处理后 A(A) B(AB) C(ABC)
//	 * @param nodeId	知识点ID
//	 * @param nodeMap	集合
//	 * @return
//	 */
//	private List nodeChildAddAll_DG(Long nodeId , Map<Long,AdlDecisionForestNode> nodeMap){
//		if(null != nodeId && MapUtils.isNotEmpty(nodeMap)){
//			AdlDecisionForestNode node = nodeMap.get(nodeId);
//			for (Map.Entry<Long,AdlDecisionForestNode> entry : nodeMap.entrySet()){
//				Long itemNodeId = entry.getKey();
//				AdlDecisionForestNode itemNode = entry.getValue();
//				//知识点ID不相等 && 知识点NODE包含子集 && 子集中包含的知识点非空
//				if(!nodeId.equals(itemNodeId) && node.getPriorNodeSet().contains(itemNodeId) && CollectionUtils.isNotEmpty(itemNode.getPriorNodeSet())
//						){
//					nodeChildAddAll_DG(itemNodeId ,nodeMap);
//					//删除交接
//					node.getPriorNodeSet().removeAll(itemNode.getPriorNodeSet());
//					//添加子集合
//					node.getPriorNodeSet().addAll(itemNode.getPriorNodeSet());
//				}
//			}
//		}
//	}

	private void nodeChildAddAll(List<AdlDecisionForestNode> nodeList){
		if(CollectionUtils.isNotEmpty(nodeList)){
			for (int i = 0; i < nodeList.size(); i++) {
				nodeChildAddAll_DG(nodeList.get(i) , nodeList);
			}
		}
	}

	/**
	 * 处理有序知识点格式问题
	 * A->B->C
	 * 处理前 A(A) B(AB) C(BC)
	 * 处理后 A(A) B(AB) C(ABC)
	 * @param node		本级知识点
	 * @param nodeList	集合
	 * @return
	 */
	private void nodeChildAddAll_DG(AdlDecisionForestNode node , List<AdlDecisionForestNode> nodeList){
		if(null != node && CollectionUtils.isNotEmpty(nodeList) && nodeList.size() > 1){
			for (int i = 0 ; i < nodeList.size() ; i++){
				AdlDecisionForestNode itemNode = nodeList.get(i);
				Long itemNodeId = itemNode.getNodeId();
				//知识点ID不相等 && 知识点NODE包含子集 && 子集中包含的知识点非空
				if(!itemNodeId.equals(node.getNodeId())){
					if( CollectionUtils.isNotEmpty(itemNode.getPriorNodeSet()) && node.getPriorNodeSet().contains(itemNodeId)){
						nodeChildAddAll_DG(itemNode ,nodeList);
						//删除交接
						node.getPriorNodeSet().removeAll(itemNode.getPriorNodeSet());
						//添加子集合
						node.getPriorNodeSet().addAll(itemNode.getPriorNodeSet());
					}
//					else if(CollectionUtils.isNotEmpty(node.getPriorNodeSet()) && itemNode.getPriorNodeSet().contains(node.getNodeId())){
////						nodeChildAddAll_DG(itemNode ,nodeList);
//						//删除交接
//						itemNode.getPriorNodeSet().removeAll(node.getPriorNodeSet());
//						//添加子集合
//						itemNode.getPriorNodeSet().addAll(node.getPriorNodeSet());
////						nodeChildAddAll_DG(itemNode ,nodeList);
//					}

				}
//				if(!itemNodeId.equals(node.getNodeId()) && node.getPriorNodeSet().contains(itemNodeId) && CollectionUtils.isNotEmpty(itemNode.getPriorNodeSet())
//						){
//					nodeChildAddAll_DG(itemNode ,nodeList);
//					//删除交接
//					node.getPriorNodeSet().removeAll(itemNode.getPriorNodeSet());
//					//添加子集合
//					node.getPriorNodeSet().addAll(itemNode.getPriorNodeSet());
//				}
			}
		}
	}

	/**
	 * 校验知识点点是否是有序知识点
	 * @param knowledgeId	知识点PK
	 * @return		true:有序 false:无序
	 */
	private boolean isGraph(Long knowledgeId){
		boolean isGraph = false;
		if(null != knowledgeId && knowledgeId > 0){
			isGraph = this.adlKnowledgeContainService.queryTotalBySelfId(knowledgeId) > 0 ||
					this.adlKnowledgeContainService.queryTotalByChildId(knowledgeId) > 0;
		}
		return isGraph;
	}

	/**
	 * 有序知识点包含的子集合
	 * @param selfId
	 * @param list
	 * @return
	 */
	private List<Long> addChildId(Long selfId , List<Long> list){
		if(CollectionUtils.isEmpty(list)){
			list = new ArrayList<>();
		}
		list.add(selfId);
		List<AdlKnowledgeContainPOJO> adlKnowledgeContainPOJOS = this.adlKnowledgeContainService.queryListBySelfId(selfId);
		if(CollectionUtils.isNotEmpty(adlKnowledgeContainPOJOS)){
			for (int i = 0; i < adlKnowledgeContainPOJOS.size(); i++) {
				AdlKnowledgeContainPOJO adlKnowledgeContainPOJO = adlKnowledgeContainPOJOS.get(i);
				list.add(adlKnowledgeContainPOJO.getChildId());
			}
		}
		return list;
	}

	/**
	 * nodeList集合中是否包含该nodeId知识点PK
	 * @param nodeId	知识点PK
	 * @param nodeList	知识点包含集合
	 * @return		true:包含  false:不包含
	 */
	private AdlDecisionForestNode nodeListContains(Long nodeId , List<AdlDecisionForestNode> nodeList){
		AdlDecisionForestNode adlDecisionForestNode = null;
		if(CollectionUtils.isNotEmpty(nodeList)){
			for (int i = 0; i < nodeList.size(); i++) {
				if(nodeList.get(i).getNodeId() == nodeId){
					adlDecisionForestNode = nodeList.get(i);
					break;
				}
			}
		}
		return adlDecisionForestNode;
	}
}