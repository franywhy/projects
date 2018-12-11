package io.renren.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.renren.entity.SysUserEntity;
import io.renren.entity.SysUserLaberEntity;
import io.renren.pojo.SysUserLaberPOJO;
import io.renren.service.SysUserLaberService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.ShiroUtils;



/**
 * 标签管理员表
 * 
 * @author vince
 * @date 2018-05-24 17:01:37
 */
@RestController
@RequestMapping("/sys/userlaber")
public class SysUserLaberController extends AbstractController{
	@Autowired
	private SysUserLaberService sysUserLaberService;
	
	protected SysUserEntity getUser() {
		return ShiroUtils.getUserEntity();
	}
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sysUserLaber:list")
	public R list(HttpServletRequest request){
//		System.out.println(request+"====================="+params);
		//查询列表数据
//		Map<String, Object> query = getMapPage(request);
		
		Map<String, Object> map = getMapPage(request);
        stringQuery(map, request, "mobile");
        stringQuery(map, request, "nickName");
		
		List<SysUserLaberPOJO> sysUserLaberList = sysUserLaberService.queryUserLaberList(map);
		int total = sysUserLaberService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(sysUserLaberList, total, request);
		return R.ok().put("data", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sysUserLaber:info")
	public R info(@PathVariable("id") Long id){
		SysUserLaberPOJO sysUserLaber = sysUserLaberService.queryPOJO(id);
		return R.ok().put("sysUserLaber", sysUserLaber);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("sysUserLaber:save")
	public R save(@RequestBody SysUserLaberEntity sysUserLaber){
		
		if(null != sysUserLaber.getSysUserId() && 0 != sysUserLaber.getSysUserId()){
			//查询自己在表中是否存在
			String s = sysUserLaberService.queryLaberByUserId(sysUserLaber.getSysUserId());
			if(StringUtils.isNotBlank(s)){
				return R.ok().put("该老师已经添加标签,请勿重新添加");
			}
		}
		SysUserEntity sysUserEntity = getUser();
		sysUserLaber.setCreator(sysUserEntity.getUserId());
		sysUserLaber.setModifier(sysUserEntity.getUserId());
		sysUserLaberService.save(sysUserLaber);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("sysUserLaber:update")
	public R update(@RequestBody SysUserLaberEntity sysUserLaber){
		sysUserLaberService.update(sysUserLaber);
		return R.ok();
	}
	/**
	 * 冻结
	 * @param id
	 * @return
	 */
	@RequestMapping("/stopUse")
	public R stopUse(@RequestBody Long[] ids){
		sysUserLaberService.stopUse(ids);
		return R.ok();
	}
	
	/**
	 * 启用
	 * @param id
	 * @return
	 */
	@RequestMapping("/startUse")
	public R startUse(@RequestBody Long[] ids){
		sysUserLaberService.startUse(ids);
		return R.ok();
	}
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@RequestMapping("/del")
	public R del(@RequestBody Long[] ids){
		sysUserLaberService.del(ids);
		return R.ok();
	}
	
}
