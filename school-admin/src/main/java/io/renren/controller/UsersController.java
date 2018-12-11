package io.renren.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.renren.entity.CourseClassplanEntity;
import io.renren.pojo.classplan.ClassplanPOJO;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.entity.UsersEntity;
import io.renren.rest.persistent.KGS;
import io.renren.service.SysCheckQuoteService;
import io.renren.service.UsersService;
import io.renren.utils.EncryptionUtils;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;
import sun.misc.BASE64Encoder;


/**
 * 前台用户表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-27 10:26:24
 */
@Controller
@RequestMapping("/mall/users")
public class UsersController extends AbstractController {
	private static final String MD5_KEY = "%^\\$AF>.12*******zK";
	@Autowired
	private UsersService usersService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	@Resource
	KGS userKGS;
	/**
	 * 学员列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mall:users:list")
	public R list(Integer page, Integer limit,HttpServletRequest request){
//		System.out.println("dddddddddddddddddddddddddddddddddd:" + userKGS.nextId());
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		
		stringQuery(map, request, "nickName");
		longQuery(map, request, "userId");
		stringQuery(map, request, "mobile");
//		map.put("nickName", request.getParameter(""));
//		map.put("userId", request);
//		map.put("mobile", request);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);	
		//查询列表数据
		List<UsersEntity> usersList = usersService.queryList(map);
		int total = usersService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(usersList, total, limit, page);	
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 学员列表信息
	 */
	@ResponseBody
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("mall:users:info")
	public R info(@PathVariable("userId") Long userId){
		UsersEntity users = usersService.queryObject(userId);
		return R.ok().put("users", users);
	}
	
	/**
	 * 学员列表信息
	 * @throws UnsupportedEncodingException 
	 */
	@ResponseBody
	@RequestMapping("/restPsw/{userId}")
	@RequiresPermissions("mall:users:save")
	public R restPsw(@PathVariable("userId") Long userId) throws UnsupportedEncodingException{
		usersService.restPsw(EncryptionUtils.md5Hex( new BASE64Encoder().encode("123456".getBytes("utf-8")) + MD5_KEY) , userId);
		return R.ok();
	}
	
	/**
	 * 新增学员
	 * @throws NoSuchAlgorithmException 
	 */
	@SysLog("保存学员")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mall:users:save")
	public R save(@RequestBody UsersEntity users, HttpServletRequest request) throws NoSuchAlgorithmException{
	    // users.setStatus(1);
		if(StringUtils.isBlank(users.getMobile())){
			return R.error("[登录账号(手机号码)]不能为空！！！");
		}
		if(StringUtils.isBlank(users.getPassword())){
			return R.error("[密码]不能为空！！！");
		}
		if(StringUtils.isBlank(users.getNickName())){
			return R.error("[昵称]不能为空！！！");
		}
		if(StringUtils.isNotBlank( users.getRemake()) && users.getRemake().length() > 50){
			return R.error("[备注信息]不能超过50个字！！！");
		}
		Map<String, Object> cmap = getMap(request);
		cmap.put("mobile", users.getMobile());
		if(this.usersService.checkMobile(cmap)){
			return R.error("手机号码已经存在");
		}
		
		if(StringUtils.isNotBlank(users.getPic())){
			users.setPic("http://zikao.hqjy.com/Avatar.png");
		}
		
//		users.setPassword(EncryptionUtils.md5Hex(users.getPassword()));
		byte[] b = null; 
		String str64 = null; 
		try {
			b = users.getPassword().getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		str64 = new BASE64Encoder().encode(b); 
		users.setPassword(EncryptionUtils.md5Hex(str64 + MD5_KEY));
		
		users.setSchoolId(SchoolIdUtils.getSchoolId(request));
		users.setCreator(getUserId());
		users.setModifier(getUserId());
		users.setUserId((long) userKGS.nextId());
		usersService.save(users);	
		return R.ok();
	}
	
	/**
	 * 修改学员信息
	 * @throws NoSuchAlgorithmException 
	 */
	@SysLog("修改学员")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mall:users:update")
	public R update(@RequestBody UsersEntity users , HttpServletRequest request) throws NoSuchAlgorithmException{
		if(StringUtils.isBlank(users.getMobile())){
			return R.error("[登录账号(手机号码)]不能为空！！！");
		}
//		if(StringUtils.isBlank(users.getPassword())){
//			return R.error("请输入密码");
//		}
		if(StringUtils.isBlank(users.getNickName())){
			return R.error("[昵称]不能为空！！！");
		}
		if(StringUtils.isNotBlank( users.getRemake()) && users.getRemake().length() > 50){
			return R.error("[备注信息]不能超过50个字！！！");
		}
		Map<String, Object> cmap = getMap(request);
		cmap.put("mobile", users.getMobile());
		cmap.put("userId", users.getUserId());
		if(this.usersService.checkMobile(cmap)){
			return R.error("手机号码已经存在");
		}
		if(StringUtils.isNotBlank(users.getPassword())){
//			string 64str = base64(users.getPassword());
//			users.setPassword(EncryptionUtils.md5Hex(64str));
//			users.setPassword(EncryptionUtils.md5Hex(users.getPassword()));
			byte[] b = null; 
			String str64 = null; 
			try {
				b = users.getPassword().getBytes("utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			str64 = new BASE64Encoder().encode(b);
			users.setPassword(EncryptionUtils.md5Hex(str64 + MD5_KEY));
		}
		users.setModifier(getUserId());
		usersService.update(users);
		return R.ok();
	}
	
	/**
	 * 删除学员
	 */
	@SysLog("删除学员")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mall:users:delete")
	public R delete(@RequestBody Long userIds[], HttpServletRequest request){
		Map<String, Object> map = getMap(request); 
		ArrayList<String> exceptList = new ArrayList<String>();
		String exceptMsg = "删除失败的具体原因如下:";
		String errMsg="";
		for(int i=0; i < userIds.length; i++){
			Long userId = userIds[i];
			map.put("value", userId);
			if(sysCheckQuoteService.checkQuote(map , QuoteConstant.course_userplan_user_id)){
				exceptMsg="学员规划有引用！";
				exceptList.add(exceptMsg);
			}
			if(sysCheckQuoteService.checkQuote(map , QuoteConstant.mall_order_user_id)){
				exceptMsg="订单有引用！";
				exceptList.add(exceptMsg);
			}
			for (int j = 0; j < exceptList.size(); j++)
			{
				errMsg += exceptList.get(j) + "<br>";
			}
		}
		if(errMsg.equals("")){
			usersService.deleteBatch(userIds);;
			return R.ok();
		}
		else{
			return R.error(errMsg);
		}
		/*usersService.deleteBatch(userIds);	
		return R.ok();*/
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用学员")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mall:users:pause")
	public R pause(@RequestBody Long[] userIds){
		usersService.pause(userIds);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用学员")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mall:users:resume")
	public R resume(@RequestBody Long[] userIds){
		usersService.resume(userIds);
		return R.ok();
	}
}

