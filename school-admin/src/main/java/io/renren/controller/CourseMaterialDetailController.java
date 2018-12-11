package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.CourseMaterialDetailEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.CourseMaterialDetailService;
import io.renren.utils.PageUtils;
//import io.renren.utils.PropertiesUtil;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;

/**
 * 资料库明细档案
 * @author xiechaojun
 *2017年6月1日
 */
@Controller
@RequestMapping("coursematerialdetail")
public class CourseMaterialDetailController extends AbstractController {
	@Autowired
	private CourseMaterialDetailService courseMaterialDetailService;
	@Resource
	KGS courseMaterialDetailKGS;
	@Value("#{application['material.detail.url']}")
	private String material_detail_url = "";
	@RequestMapping("/coursematerialdetail.html")
	public String list(){
		return "coursematerialdetail/coursematerialdetail.html";
	}
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("coursematerialdetail:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "detailName");
		longQuery(map, request, "materialId");
		longQuery(map, request, "materialTypeId");
		//查询列表数据
		List<Map<String, Object>> courseMaterialDetailList = courseMaterialDetailService.queryListMap(map);
		int total = courseMaterialDetailService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(courseMaterialDetailList, total , request);	
		return R.ok().put(pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@ResponseBody
	@RequestMapping("/info/{detailId}")
	@RequiresPermissions("coursematerialdetail:info")
	public R info(@PathVariable("detailId") Long detailId , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		/*longQuery(map, request, "detailId");*/
		map.put("detailId", detailId);
		CourseMaterialDetailEntity courseMaterialDetail = courseMaterialDetailService.queryObjectById(map);
		return R.ok().put(courseMaterialDetail);
	}
	
	/**
	 * 保存
	 * @throws Exception 
	 */
	@SysLog("保存资料库明细")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("coursematerialdetail:save")
	public R save(@RequestBody CourseMaterialDetailEntity courseMaterialDetail , HttpServletRequest request) throws Exception{
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
	 * @throws Exception 
	 */
	@SysLog("修改资料库明细")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("coursematerialdetail:update")
	public R update(@RequestBody CourseMaterialDetailEntity courseMaterialDetail , HttpServletRequest request) throws Exception{
		/*Map<String, Object> map = getMap(request);
		map.put("detailId",courseMaterialDetail.getDetailId());
		courseMaterialDetailService.delete(map);	*/
		Long id=courseMaterialDetail.getDetailId();
		//URL处理
				String url=courseMaterialDetail.getUrl();
				if(StringUtils.isBlank(url)){
				/*	String urll=PropertiesUtil.loadInterfaceConfigProperties().getMaterialDetailUrl()+id;*/
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
	 */
	@SysLog("删除资料库明细")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("coursematerialdetail:delete")
	public R delete(@RequestBody Long[] detailIds , HttpServletRequest request){
		Map<String, Object> map = getMap(request);
		map.put("ids",detailIds);
		courseMaterialDetailService.deleteBatch(map);	
		return R.ok();
	}
	
}
