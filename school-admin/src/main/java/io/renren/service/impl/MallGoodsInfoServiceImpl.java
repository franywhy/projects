package io.renren.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.renren.dao.GoodsCoursetkDao;
import io.renren.dao.MallGoodsInfoDao;
import io.renren.entity.GoodsCoursetkEntity;
import io.renren.entity.MallGoodsDetailsEntity;
import io.renren.entity.MallGoodsInfoEntity;
import io.renren.entity.MallGoodsInfoLayEntity;
import io.renren.pojo.MallGoodsDetailsPOJO;
import io.renren.pojo.MallGoodsInfoPOJO;
import io.renren.service.GivingCoursesService;
import io.renren.service.MallGoodsDetailsService;
import io.renren.service.MallGoodsInfoService;
import io.renren.utils.Constant;
import io.renren.utils.RRException;


@Transactional(readOnly = true)
@Service("mallGoodsInfoService")
public class MallGoodsInfoServiceImpl implements MallGoodsInfoService {
	@Autowired
	private MallGoodsInfoDao mallGoodsInfoDao;
	@Autowired
	private MallGoodsDetailsService mallGoodsDetailsService;
	@Autowired
	private GoodsCoursetkDao goodsCoursetkDao;
	
	
	
	@Autowired
	private GivingCoursesService givingCoursesService;
	
	@Override
	public MallGoodsInfoEntity queryObject(Map<String, Object> map){
		return mallGoodsInfoDao.queryObject(map);
	}
	@Override
	public Map<String, Object> queryMap(Map<String, Object> map) {
		return mallGoodsInfoDao.queryMap(map);
	}
	
	@Override
	public List<Map<String, Object>> queryListMap(Map<String, Object> map){
		return mallGoodsInfoDao.queryListMap(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return mallGoodsInfoDao.queryTotal(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void save(MallGoodsInfoPOJO mallGoodsInfo){
		//初始化上架状态
		mallGoodsInfo.setStatus(0);
		//初始化dr
		mallGoodsInfo.setDr(0);
		//初始化审核状态
		mallGoodsInfo.setIsAudited(0);
		//创建时间
		mallGoodsInfo.setCreateTime(new Date());
		//修改时间
		mallGoodsInfo.setModifyTime(mallGoodsInfo.getCreateTime());
		MallGoodsInfoEntity en = mallGoodsInfo.getEntity(mallGoodsInfo);
		//保存主表
		mallGoodsInfoDao.save(en);
		//TODO
		if(mallGoodsInfo.getOnlyOne() == 1){
			
			if(StringUtils.isNotBlank(mallGoodsInfo.getTkCourseCode())){
				this.saveGoodsCoursetkCode(en.getId(), mallGoodsInfo.getTkCourseCode());
			}
		}
		//遍历子表
		List<MallGoodsDetailsPOJO> detailList = mallGoodsInfo.getDetailList();
		//子表保存
		if(null != detailList && detailList.size() > 0){
			for(int i=0;i<detailList.size();i++){
				//pojo
				MallGoodsDetailsPOJO mgdp = detailList.get(i);
				//entity
				MallGoodsDetailsEntity mgde = MallGoodsDetailsPOJO.getEntity(mgdp);
				//set 主表id
				mgde.setMallGoodsId(en.getId());
				//set 省份id
				mgde.setMallAreaId(mgdp.getMallAreaId());
				//set 课程id
				mgde.setCourseId(mgdp.getCourseId());
				//set 被替代课程
				mgde.setIsSubstituted(mgdp.getIsSubstituted());
				//set 代替课程
				mgde.setIsSubstitute(mgdp.getIsSubstitute());
				//set 是否统考
				mgde.setIsUnitedExam(mgdp.getIsUnitedExam());
				//set 专业不对口课程
				mgde.setIsSuitable(mgdp.getIsSuitable());
				//set 平台ID
				mgde.setSchoolId(en.getSchoolId());
				//排序
				mgde.setOrderNum(i);
				//保存子表
				mallGoodsDetailsService.save(mgde);
			}
		}
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void update(MallGoodsInfoPOJO mallGoodsInfo){
		//修改时间
		mallGoodsInfo.setModifyTime(new Date());
		MallGoodsInfoEntity en = mallGoodsInfo.getEntity(mallGoodsInfo);
		//保存主表修改
		mallGoodsInfoDao.update(en);
		//TODO
		if(1 == mallGoodsInfo.getOnlyOne()){
			//更新前先删除原来的对应关系
			this.goodsCoursetkDao.deleteByCommodityId(en.getId());
			
			if(StringUtils.isNotBlank(mallGoodsInfo.getTkCourseCode())){
				this.saveGoodsCoursetkCode(en.getId(), mallGoodsInfo.getTkCourseCode());
			}
		}else{
			//更新前先删除原来的对应关系
			this.goodsCoursetkDao.deleteByCommodityId(en.getId());
		}
		//遍历子表
		List<MallGoodsDetailsPOJO> detailList = mallGoodsInfo.getDetailList();
		//用于存放被保存或修改的子表id集合
		List<Long> delIds = new ArrayList<Long>();
		if(null != detailList && detailList.size() > 0){
			for(int i=0;i<detailList.size();i++){
				//pojo
				MallGoodsDetailsPOJO mgdp = detailList.get(i);
				//entity
				MallGoodsDetailsEntity mgde = MallGoodsDetailsPOJO.getEntity(mgdp);
				//set 主表id
				mgde.setMallGoodsId(en.getId());
				//set 省份id
				mgde.setMallAreaId(mgdp.getMallAreaId());
				//set 课程id
				mgde.setCourseId(mgdp.getCourseId());
				//set 被替代课程
				mgde.setIsSubstituted(mgdp.getIsSubstituted());
				//set 代替课程
				mgde.setIsSubstitute(mgdp.getIsSubstitute());
				//set 是否统考
				mgde.setIsUnitedExam(mgdp.getIsUnitedExam());
				//set 专业不对口课程
				mgde.setIsSuitable(mgdp.getIsSuitable());
				//set 平台ID
				mgde.setSchoolId(en.getSchoolId());
				//排序
				mgde.setOrderNum(i);
				if(null == mgde.getId()){
					mallGoodsDetailsService.save(mgde);
				}else{//更新
					mallGoodsDetailsService.update(mgde);
				}
				delIds.add(mgde.getId());
			}
		}
		Map<String , Object> map = new HashMap<String , Object>();
		map.put("ids", delIds);
		//主表id
		map.put("mallGoodsId", en.getId());
		mallGoodsDetailsService.deleteBatchNotIn(map);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		mallGoodsInfoDao.delete(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void deleteBatch(Map<String, Object> map){
		mallGoodsDetailsService.deleteBatch(map);
		mallGoodsInfoDao.delete(map);
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void pause(Long[] ids){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.PAUSE.getValue());
		mallGoodsInfoDao.updateBatch(map);
	}
		
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public void resume(Long[] ids){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", ids);
    	map.put("status", Constant.Status.RESUME.getValue());
		mallGoodsInfoDao.updateBatch(map);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public int accept(Long id) {
	 	return mallGoodsInfoDao.accept(id);
		
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public int reject(Long id) {
		return mallGoodsInfoDao.reject(id);
		
	}

	@Override
	public List<MallGoodsInfoEntity> findGoodsList() {
		return mallGoodsInfoDao.findGoodsList();
	}
	
	/**
	 * 简单查询
	 * @param map
	 * @param status 上架状态
	 * @param professionId 专业
	 * @param levelId 层次
	 * @param name 名称
	 * @return
	 */
	@Override
	public List<Map<String, Object>> simpleQueryList(Map<String, Object> map){
		return this.mallGoodsInfoDao.simpleQueryList(map);
	}
	@Override
	public List<MallGoodsInfoEntity> queryList(Map<String, Object> map) {
		return mallGoodsInfoDao.queryList(map);
	}
	@Override
	public int checkClassType(long id) {
		return this.mallGoodsInfoDao.checkClassType(id);
	}
	@Override
	public int checkProfession(long id) {
		return this.mallGoodsInfoDao.checkClassType(id);
	}
	@Override
	public List<MallGoodsInfoLayEntity> queryLayList(Map<String, Object> map) {
		return this.mallGoodsInfoDao.queryLayList(map);
	}
	@Override
	public MallGoodsInfoEntity queryGoodsInfo(String commodityId) {
		return this.mallGoodsInfoDao.queryGoodsInfo(commodityId);
	}
	@Override
	public int queryTotal1(Map<String, Object> map) {
		return this.mallGoodsInfoDao.queryTotal1(map);
	}
	@Override
	public MallGoodsInfoEntity queryGoodsInfoId(Map<String, Object> map) {
		return this.mallGoodsInfoDao.queryGoodsInfoId(map);
	}
	@Override
	public boolean checkExist(Map<String, Object> map) {
		return this.mallGoodsInfoDao.checkExist(map)>0;
	}
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	@Override
	public boolean copyArea(Long id, Long areaId1, Long areaId2 , String schoolId) {
		if(null == id){
			throw new RRException("无法获取商品ID!");
		}else if(null == areaId1 || null == areaId2){
			throw new RRException("省份ID获取异常!");
		}else if(areaId1 == areaId2){
			throw new RRException("省份不能相同");
		}
//		//校验新省份是否存在
//		Map<String, Object> checkMap = new HashMap<>();
//		checkMap.put("schoolId", schoolId);
//		checkMap.put("id", id);
//		checkMap.put("areaId", areaId2);
//		List<MallGoodsDetailsEntity> checkList = this.mallGoodsDetailsService.selectAreaCouresList(checkMap);
//		if(null != checkList && checkList.size() != 0){
//			throw new RRException("操作失败!商品下已经有要拷贝的省份!");
//		}
		//查询要拷贝的省份下的课程
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", schoolId);
		map.put("id", id);
		map.put("areaId", areaId1);
		List<MallGoodsDetailsEntity> areaCouresList = this.mallGoodsDetailsService.selectAreaCouresList(map);
		if(null == areaCouresList || areaCouresList.size() == 0){
			throw new RRException("copy的省份没有课程");
		}
		//拷贝
		for (int i = 0; i < areaCouresList.size(); i++) {
			//校验新省份下是否有该课程
			Map<String, Object> checkMap = new HashMap<>();
			checkMap.put("schoolId", schoolId);
			checkMap.put("id", id);
			checkMap.put("areaId", areaId2);
			checkMap.put("courseId",  areaCouresList.get(i).getCourseId());
			List<MallGoodsDetailsEntity> checkList = this.mallGoodsDetailsService.selectAreaCouresList(checkMap);
			if(null != checkList && checkList.size() != 0){
				continue;
			}
			
			//原省份下的课程
			MallGoodsDetailsEntity mallGoodsDetailsEntity = areaCouresList.get(i);
			
			//新省份下的课程
			MallGoodsDetailsEntity detailsEntity = new MallGoodsDetailsEntity();
			detailsEntity.setMallGoodsId(id);
			detailsEntity.setMallAreaId(areaId2);
			detailsEntity.setCourseId(mallGoodsDetailsEntity.getCourseId());
			detailsEntity.setIsSubstituted(mallGoodsDetailsEntity.getIsSubstituted());
			detailsEntity.setIsSubstitute(mallGoodsDetailsEntity.getIsSubstitute());
			detailsEntity.setIsUnitedExam(mallGoodsDetailsEntity.getIsUnitedExam());
			detailsEntity.setIsSuitable(mallGoodsDetailsEntity.getIsSuitable());
			detailsEntity.setSchoolId(schoolId);
			detailsEntity.setOrderNum(i);
			detailsEntity.setDr(0);
			this.mallGoodsDetailsService.save(detailsEntity);
		}
		return true;
	}

    @Override
    public MallGoodsInfoEntity queryGoodsInfoByNcId(String ncId) {
        return mallGoodsInfoDao.queryGoodsInfoByNcId(ncId);
    }

    private void saveGoodsCoursetkCode(Long commodityId, String codeStr){

		Map<String,Object> map = new HashMap<>();
		String[] tkCourseCodeArr = codeStr.split(",");
		for (String tkCourseCode : tkCourseCodeArr) {
			map.put("tkCourseCode", tkCourseCode.trim());
			map.put("commodityId", commodityId);
			int count = this.goodsCoursetkDao.queryTotalByMap(map);
			if(count == 0){
				GoodsCoursetkEntity GoodsCoursetkEntity = new GoodsCoursetkEntity();
				GoodsCoursetkEntity.setCommodityId(commodityId);
				GoodsCoursetkEntity.setCourseTkCode(tkCourseCode.trim());
				this.goodsCoursetkDao.save(GoodsCoursetkEntity);
			}
		}
	
	}
	@Override
	public List<MallGoodsInfoEntity> groupGoodInfo(String NcCommodityId,boolean hasDr) {
		List<MallGoodsInfoEntity> goodList= new ArrayList<MallGoodsInfoEntity>();
	  
	     //判断这个班型是否是组合班型
	    if ( givingCoursesService.checkNcCommodity(NcCommodityId)) {
	    	List<Long> gidList=givingCoursesService.getGroupGoodFromRedis(NcCommodityId,hasDr);
	    	if (gidList!=null&&gidList.size()>0) {
	    		for (Long gid:gidList) {
	    			if (gid!=null&&gid.intValue()>0) {
	    				Map<String, Object> queryGoodMap = new HashMap<>();
						queryGoodMap.put("id",gid); 
						MallGoodsInfoEntity goodsInfo = this.queryObject(queryGoodMap);
							if (goodsInfo!=null&&goodsInfo.getId()!=null&&goodsInfo.getId().intValue()>0) {
								goodList.add(goodsInfo);
							}

	    			}
	    				    		 
	    		}
	    		
	    	}
	    	
	    }else { //不是组合班型的话则返回
	    	
	        MallGoodsInfoEntity goods = this.queryGoodsInfo(NcCommodityId);
	        	if (goods!=null&&goods.getId()>0) {

	    	    	goodList.add(goods);
	        	}
	    	
	    	
	    }
		return goodList;
	}

}
