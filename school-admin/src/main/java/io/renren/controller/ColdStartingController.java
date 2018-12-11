package io.renren.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.common.doc.SysLog;
import io.renren.entity.ColdStartingEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.ColdStartingService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 * 冷启动数据
 * 
 * @author linyuebin
 * @email trust_100@163.com
 * @date 2017-12-30 11:30:54
 */
@RestController
@RequestMapping("coldstarting")
public class ColdStartingController extends AbstractController{
	@Autowired
	private ColdStartingService coldStartingService;


	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("coldstarting:list")
	public R list(HttpServletRequest request){
        String schoolId = SchoolIdUtils.getSchoolId(request);
		//查询列表数据
        Map<String, Object> map = getMapPage(request);
        map.put("schoolId",schoolId);
		List<ColdStartingEntity> coldStartingList = coldStartingService.queryList(map);
		int total = coldStartingService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(coldStartingList, total, request);
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("coldstarting:info")
	public R info(@PathVariable("id") Long id){
		ColdStartingEntity coldStarting = coldStartingService.queryObject(id);
		
		return R.ok().put("coldStarting", coldStarting);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("coldstarting:save")
	public R save(@RequestBody ColdStartingEntity coldStarting){
	    coldStarting.setCreateTime(new Date());
	    coldStarting.setUpdateTime(new Date());
	    coldStarting.setIsShow(1);
	    coldStarting.setDr(0);
		coldStartingService.save(coldStarting);
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("coldstarting:update")
	public R update(@RequestBody ColdStartingEntity coldStarting){
	    coldStarting.setUpdateTime(new Date());
		coldStartingService.update(coldStarting);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("coldstarting:delete")
	public R delete(@RequestBody Long[] ids){
		coldStartingService.deleteBatch(ids);
		
		return R.ok();
	}



    /**
     * 上架
     */
    @ResponseBody
    @RequiresPermissions("coldstarting:resume")
    @RequestMapping("/resume")
    public R resume(@RequestBody Long[] ids){
        coldStartingService.resume(ids);
        return R.ok();
    }

    /**
     * 下架
     */
    @ResponseBody
    @RequestMapping("/pause")
    @RequiresPermissions("coldstarting:pause")
    public R pause(@RequestBody Long[] ids){
        coldStartingService.pause(ids);
        return R.ok();
    }
	
}
