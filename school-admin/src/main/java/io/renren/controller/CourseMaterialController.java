package io.renren.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseMaterialDetailEntity;
import io.renren.entity.CourseMaterialTypeEntity;
import io.renren.pojo.BaseTreePOJO;
import io.renren.pojo.CourseRecordDetailPOJO;
import io.renren.pojo.coursematerial.CourserMaterialPOJO;
import io.renren.pojo.coursematerial.CourserMaterialTypePOJO;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseMaterialDetailService;
import io.renren.service.CourseMaterialService;
import io.renren.service.CourseMaterialTypeService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;

/**
 * 资料库管理
 * 
 * @author xiechaojun 2017年5月13日
 */
@Controller
@RequestMapping("coursematerial")
public class CourseMaterialController extends AbstractController {
	@Autowired
	private CourseMaterialService courseMaterialService;
	@Autowired
	private CourseMaterialTypeService courseMaterialTypeService;
	@Autowired
	private CourseMaterialDetailService courseMaterialDetailService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	@Resource
	KGS courseMaterialDetailKGS;
	@Value("#{application['material.detail.url']}")
	private String material_detail_url = "";
	@RequestMapping("/coursematerial.html")
	public String list() {
		return "coursematerial/coursematerial.html";
	}

	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("coursematerial:list")
	public R list(HttpServletRequest request) {
		String schoolId = SchoolIdUtils.getSchoolId(request);
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "materialName");
		map.put("schoolId", schoolId);
		// 查询列表数据
		List<Map<String, Object>> courseMaterialList = courseMaterialService.queryListMap(map);
		int total = courseMaterialService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseMaterialList, total, request);
		return R.ok().put(pageUtil);
	}

	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{materialId}")
	@RequiresPermissions("coursematerial:info")
	public R info(@PathVariable("materialId") Long materialId, HttpServletRequest request) {
		CourserMaterialPOJO courseMaterial = courseMaterialService.queryPojoObject(materialId);

		List<CourseMaterialTypeEntity> typeList = courseMaterialTypeService.queryObject(materialId);
		return R.ok().put("courseMaterial", courseMaterial).put("typeList", typeList);
	}
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/materialTypeInfo/{materialTypeId}")
	@RequiresPermissions("coursematerial:info")
	public R materialTypeInfo(@PathVariable("materialTypeId") Long materialTypeId, HttpServletRequest request) {
		CourserMaterialTypePOJO courserMaterialTypePOJO = courseMaterialTypeService.queryPojoObject(materialTypeId);
		//List<CourseMaterialTypeEntity> typeList = courseMaterialTypeService.queryObject(materialTypeId);
		return R.ok().put("courserMaterialTypePOJO", courserMaterialTypePOJO);
	}
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/materialDetailInfo/{materialDetailId}")
	@RequiresPermissions("coursematerial:info")
	public R materialDetailInfo(@PathVariable("materialDetailId") Long materialDetailId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		/*longQuery(map, request, "detailId");*/
		map.put("detailId", materialDetailId);
		CourseMaterialDetailEntity courseMaterialDetail = courseMaterialDetailService.queryObjectById(map);
		return R.ok().put(courseMaterialDetail);
	}
	@ResponseBody
	@RequestMapping("/materialDetailList/{materialId}")
	@RequiresPermissions("coursematerial:info")
	public List<BaseTreePOJO> materialDetailList(@PathVariable("materialId") Long materialId,
			HttpServletRequest request) {
		List<BaseTreePOJO> treeList = new ArrayList<>();
		List<CourseMaterialTypeEntity> typeList = courseMaterialTypeService.queryObject(materialId);
		// 第二级
		if (null != typeList && !typeList.isEmpty()) {
			for (CourseMaterialTypeEntity courseMaterialTypeEntity : typeList) {
				Map<String, Object> map = getMap(request);
				// 第二级
				BaseTreePOJO tree = new BaseTreePOJO(courseMaterialTypeEntity.getMaterialTypeId(), 0,
						courseMaterialTypeEntity.getMaterialTypeName(), true, null, courseMaterialTypeEntity , 1+","+courseMaterialTypeEntity.getMaterialTypeId()+",0");
				treeList.add(tree);
				// 第三级
				map.put("material_type_id", courseMaterialTypeEntity.getMaterialTypeId());
				List<CourseMaterialDetailEntity> detailList = courseMaterialDetailService
						.queryObjectByMaterialTypeId(map);
				for (CourseMaterialDetailEntity courseMaterialDetailEntity : detailList) {
					BaseTreePOJO tree1 = new BaseTreePOJO(courseMaterialDetailEntity.getDetailId(),
							courseMaterialDetailEntity.getMaterialTypeId(), courseMaterialDetailEntity.getDetailName(),
							true, null, courseMaterialDetailEntity, courseMaterialDetailEntity.getUrl(),
							courseMaterialDetailEntity.getOrderNum(), courseMaterialDetailEntity.getProductName() , 2+","+courseMaterialDetailEntity.getDetailId()+","+courseMaterialDetailEntity.getMaterialTypeId());
					// materialList.add(tree1);
					treeList.add(tree1);
				}
			}
		}
		return treeList;
	}

	/**
	 * 保存
	 */
	@SysLog("保存资料库")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("coursematerial:save")
	public R save(@RequestBody CourserMaterialPOJO courseMaterial, HttpServletRequest request) {
		if (StringUtils.isBlank(courseMaterial.getMaterialName())) {
			return R.error("[名称]不能为空！！！");
		}
//		if (StringUtils.isBlank("" + courseMaterial.getProductId())) {
//			return R.error("[产品线]不能为空！！！");
//		}
		if (StringUtils.isBlank("" + courseMaterial.getCourseId())) {
			return R.error("[课程]不能为空！！！");
		}
		// 平台id
		courseMaterial.setSchoolId(SchoolIdUtils.getSchoolId(request));
		// 创建人
		courseMaterial.setCreatePerson(getUserId());
		// 修改人
		courseMaterial.setModifyPerson(getUserId());
		courseMaterialService.save(courseMaterial);
		return R.ok();
	}
	/**
	 * 保存
	 */
	@SysLog("保存资料库类型")
	@ResponseBody
	@RequestMapping("/materialTypeSave")
	@RequiresPermissions("coursematerial:save")
	public R materialTypeSave(@RequestBody CourserMaterialTypePOJO courserMaterialTypePOJO, HttpServletRequest request) {
		if (StringUtils.isBlank(courserMaterialTypePOJO.getMaterialTypeName())) {
			return R.error("[名称]不能为空！！！");
		}
		if (StringUtils.isBlank("" + courserMaterialTypePOJO.getMaterialId())) {
			return R.error("[资料库]不能为空！！！");
		}
		// 平台id
		courserMaterialTypePOJO.setSchoolId(SchoolIdUtils.getSchoolId(request));
		// 创建人
		courserMaterialTypePOJO.setCreatePerson(getUserId());
		// 修改人
		courserMaterialTypePOJO.setModifyPerson(getUserId());
		//创建时间
		courserMaterialTypePOJO.setCreationTime(new Date());
				//修改时间
		courserMaterialTypePOJO.setModifiedTime(new Date());
		courseMaterialTypeService.save(CourserMaterialTypePOJO.getInstance(courserMaterialTypePOJO));
		return R.ok();
	}
	/**
	 * 保存
	 * @throws Exception 
	 */
	@SysLog("保存资料库明细")
	@ResponseBody
	@RequestMapping("/materialDtailSave")
	@RequiresPermissions("coursematerial:save")
	public R materialDtailSave(@RequestBody CourseMaterialDetailEntity courseMaterialDetail , HttpServletRequest request) throws Exception{
		Long id = (long)courseMaterialDetailKGS.nextId();
		/*//默认状态
	    courseMaterialDetail.setStatus(1);*/
        //	 自动生成ID
		courseMaterialDetail.setDetailId(id);
        //URL处理
		String url=courseMaterialDetail.getUrl();
		if(StringUtils.isBlank(url)){
			/*String urll=PropertiesUtil.loadInterfaceConfigProperties().getMaterialDetailUrl()+id;*/
			courseMaterialDetail.setUrl(material_detail_url+id);
		}
		  //平台ID
	    courseMaterialDetail.setSchoolId(SchoolIdUtils.getSchoolId(request));
	    //创建用户
		courseMaterialDetail.setCreatePerson(getUserId());
		//修改用户
		courseMaterialDetail.setModifyPerson(getUserId());
	    //创建时间
		courseMaterialDetail.setCreationTime(new Date());
		//修改时间
		courseMaterialDetail.setModifiedTime(new Date());
	/*	Long readNum=courseMaterialDetail.getReadNum();*/
		//阅读量
		if(courseMaterialDetail.getReadNum()==null){
			Long readNum=0L;
			courseMaterialDetail.setReadNum(readNum++);
		}
		courseMaterialDetail.setReadNum((courseMaterialDetail.getReadNum())+1L);
		//排序
		/*if(courseMaterialDetail.getOrderNum()==null){
			int orderNum=0;
			courseMaterialDetail.setOrderNum(orderNum);
		}*/
		courseMaterialDetail.setOrderNum((courseMaterialDetail.getOrderNum()));
		//保存
		courseMaterialDetailService.save(courseMaterialDetail);	
		return R.ok();
	}
	/**
	 * 修改
	 */
	@SysLog("修改资料库")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("coursematerial:update")
	public R update(@RequestBody CourserMaterialPOJO courseMaterial, HttpServletRequest request) {
		if (StringUtils.isBlank(courseMaterial.getMaterialName())) {
			return R.error("[名称]不能为空！！！");
		}
//		if (StringUtils.isBlank("" + courseMaterial.getProductId())) {
//			return R.error("[产品线]不能为空！！！");
//		}
		if (StringUtils.isBlank("" + courseMaterial.getCourseId())) {
			return R.error("[课程]不能为空！！！");
		}
		// 平台ID
		courseMaterial.setSchoolId(SchoolIdUtils.getSchoolId(request));
		/*
		 * //修改用户 courseMaterial.setModifyPerson(getUserId()); //修改时间
		 * courseMaterial.setModifiedTime(new Date()); //修改
		 * courseMaterialService.update(courseMaterial);
		 */
		// 创建人
		// courseMaterial.setCreatePerson(getUserId());
		// 修改人
		// 平台id
		courseMaterial.setSchoolId(SchoolIdUtils.getSchoolId(request));
		// 创建人
		courseMaterial.setCreatePerson(getUserId());
		courseMaterial.setModifyPerson(getUserId());
		courseMaterialService.update(courseMaterial);
		return R.ok();
	}
	/**
	 * 修改
	 */
	@SysLog("修改资料库类型")
	@ResponseBody
	@RequestMapping("/materialTypeEdit")
	@RequiresPermissions("coursematerial:update")
	public R materialTypeEdit(@RequestBody CourserMaterialTypePOJO courserMaterialTypePOJO, HttpServletRequest request) {
		if (StringUtils.isBlank(courserMaterialTypePOJO.getMaterialTypeName())) {
			return R.error("[名称]不能为空！！！");
		}
		// 平台id
		courserMaterialTypePOJO.setSchoolId(SchoolIdUtils.getSchoolId(request));
		// 修改人
		courserMaterialTypePOJO.setModifyPerson(getUserId());
		//修改时间
		courserMaterialTypePOJO.setModifiedTime(new Date());
		courseMaterialTypeService.update(CourserMaterialTypePOJO.getInstance(courserMaterialTypePOJO));
		return R.ok();
	}
	/**
	 * 修改
	 * @throws Exception 
	 */
	@SysLog("修改资料库明细")
	@ResponseBody
	@RequestMapping("/materialDetailEdit")
	@RequiresPermissions("coursematerial:update")
	public R materialDetailEdit(@RequestBody CourseMaterialDetailEntity courseMaterialDetail , HttpServletRequest request) throws Exception{
		/*if (courseMaterialDetailService.countMaterialDetail(courseMaterialDetail.getDetailId())) {
			return R.error("[选中明细]");
		}*/
		Long id=courseMaterialDetail.getDetailId();
		//URL处理
				String url=courseMaterialDetail.getUrl();
				if(StringUtils.isBlank(url)){
					courseMaterialDetail.setUrl(material_detail_url+id);
				}
		 //平台ID
	    courseMaterialDetail.setSchoolId(SchoolIdUtils.getSchoolId(request));
	  /*  //创建用户
		courseMaterialDetail.setCreatePerson(getUserId());*/
		//修改用户
		courseMaterialDetail.setModifyPerson(getUserId());
	  /*  //创建时间
		courseMaterialDetail.setCreationTime(new Date());*/
		//修改时间
		courseMaterialDetail.setModifiedTime(new Date());
	/*	Long readNum=courseMaterialDetail.getReadNum();*/
		//阅读量
		courseMaterialDetail.setReadNum((courseMaterialDetail.getReadNum())+1L);
		//排序
	/*	courseMaterialDetail.setOrderNum((courseMaterialDetail.getOrderNum())+1);*/
		courseMaterialDetailService.update(courseMaterialDetail);
		return R.ok();
	}
	/**
	 * 删除
	 * 
	 * @throws Exception
	 */
	@SysLog("删除资料库")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("coursematerial:delete")
	public R delete(@RequestBody Long[] materialIds, HttpServletRequest request) throws Exception {
		Map<String, Object> map = getMap(request);
		map.put("ids", materialIds);
		courseMaterialTypeService.deleteBatch(map);
		Thread.sleep(1000);
		courseMaterialService.deleteBatch(map);
		return R.ok();
	}
	/**
	 * 删除资料库类型
	 */
	@SysLog("删除资料库类型")
	@ResponseBody
	@RequestMapping("/materialTypeDelete/{materialTypeId}")
	@RequiresPermissions("coursematerial:delete")
	public R materialTypeDelete(@PathVariable("materialTypeId") Long materialTypeId,HttpServletRequest request){
		Map<String, Object> map = getMap(request); 
		map.put("value", materialTypeId);
		ArrayList<String> exceptList = new ArrayList<String>();
		String exceptMsg = "删除失败的具体原因如下:";
		String errMsg="";
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_material_detail_material_type_id)){
			exceptMsg="资料库明细有引用！";
			exceptList.add(exceptMsg);
		}
		for (int i = 0; i < exceptList.size(); i++)
		{
			errMsg += exceptList.get(i) + "<br>";
		}
		if(errMsg.equals("")){
			courseMaterialTypeService.delete(map);	
			return R.ok();
		}
		else{
			return R.error(errMsg);
		}
		
	}
	/**
	 * 删除
	 */
	@SysLog("删除资料库明细")
	@ResponseBody
	@RequestMapping("/materialDetailDelete/{materialDetailId}")
	@RequiresPermissions("coursematerial:delete")
	public R materialDetailDelete(@PathVariable("materialDetailId") Long materialDetailId,HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("detailId",materialDetailId);
		courseMaterialDetailService.delete(map);	
		return R.ok();
	}
}
