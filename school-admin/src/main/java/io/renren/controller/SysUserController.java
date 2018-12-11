package io.renren.controller;

import io.renren.common.doc.SysLog;
import io.renren.entity.SysUserEntity;
import io.renren.service.SysUserRoleService;
import io.renren.service.SysUserService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import io.renren.utils.ShiroUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	/**
	 * 班主任下拉列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectList")
	public R classTeacherList(HttpServletRequest request ){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", schoolId);
		return R.ok().put("data", sysUserService.findClassTeacherList(map));
	}
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "username");
		stringQuery(map, request, "mobile");
		stringQuery(map, request, "nickName");
		intQuery(map, request, "classTeacher");
		intQuery(map, request, "teacher");
		//查询列表数据
		List<SysUserEntity> userList = sysUserService.queryList(map);
		int total = sysUserService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(userList, total, request);
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改登录用户密码")
	@RequestMapping("/password")
	public R password(String password, String newPassword){
		if(StringUtils.isBlank(newPassword)){
			return R.error("新密码不为能空");
		}
		
		//sha256加密
		password = new Sha256Hash(password).toHex();
		//sha256加密
		newPassword = new Sha256Hash(newPassword).toHex();
				
		//更新密码
		int count = sysUserService.updatePassword(getUserId(), password, newPassword);
		if(count == 0){
			return R.error("原密码不正确");
		}
		
		//退出
		ShiroUtils.logout();
		
		return R.ok();
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.queryObject(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user , HttpServletRequest request ){
		if(StringUtils.isBlank(user.getUsername())){
			return R.error("[账号]不能为空");
		}
		if(StringUtils.isBlank(user.getPassword())){
			return R.error("[密码]不能为空");
		}
		if(StringUtils.isBlank(user.getNickName())){
			return R.error("[昵称]不能为空");
		}
		if(StringUtils.isBlank(""+user.getDeptId())){
			return R.error("[部门]不能为空");
		}
		String schoolId = SchoolIdUtils.getSchoolId(request);
		user.setSchoolId(schoolId);
		sysUserService.save(user);
		
		return R.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		if(StringUtils.isBlank(user.getUsername())){
			return R.error("[账号]不能为空！！！");
		}
//		if(StringUtils.isBlank(user.getPassword())){
//			return R.error("[新密码]不能为空！！！");
//		}
		if(StringUtils.isBlank(user.getNickName())){
			return R.error("[昵称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+user.getDeptId())){
			return R.error("[部门]不能为空");
		}
		sysUserService.update(user);
		
		return R.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
	/**
	 * 禁用
	 */
	@SysLog("禁用用户")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("sys:user:pause")
	public R pause(@RequestBody Long[] userIds){
		sysUserService.pause(userIds);
		return R.ok();
	}
	/**
	 * 启用
	 */
	@SysLog("启用用户")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("sys:user:resume")
	public R resume(@RequestBody Long[] userIds){
		sysUserService.resume(userIds);
		return R.ok();
	}
}
