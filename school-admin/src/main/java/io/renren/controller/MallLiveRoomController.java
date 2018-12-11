package io.renren.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.common.doc.SysLog;
import io.renren.entity.MallLiveRoomEntity;
import io.renren.pojo.liveRoom.LiveRoomPOJO;
import io.renren.service.MallLiveRoomService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 直播间档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-21 17:00:10
 */
@Controller
@RequestMapping("/mall/liveroom")
public class MallLiveRoomController extends AbstractController {
	
	@Autowired
	private MallLiveRoomService mallLiveRoomService;
		
	/**
	 * 直播间列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mall:liveroom:list")
	public R list(String liveRoomName, String productName, Integer page, Integer limit, HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		Map<String, Object> map = new HashMap<>();
		map.put("schoolId", schoolId);
		map.put("liveRoomName", liveRoomName);
		map.put("productName", productName);
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		//查询列表数据（实现检索）
		List<MallLiveRoomEntity> mallLiveRoomList = mallLiveRoomService.queryList(map);
		int total = mallLiveRoomService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(mallLiveRoomList, total, limit, page);
		return R.ok().put("page", pageUtil);
	}
	/**
	 * 直播间列表信息
	 */
	@ResponseBody
	@RequestMapping("/info/{liveRoomId}")
	@RequiresPermissions("mall:liveroom:info")
	public R info(@PathVariable("liveRoomId") Long liveRoomId, HttpServletRequest request){
		String schoolId = SchoolIdUtils.getSchoolId(request);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("schoolId", schoolId);
		map.put("liveRoomId", liveRoomId);
		MallLiveRoomEntity mallLiveRoom = mallLiveRoomService.queryObject(map);
		return R.ok().put("mallLiveRoom", mallLiveRoom);
	}
	
	/**
	 * 新增直播间
	 */
	@SysLog("保存直播间")
	@ResponseBody
	@RequestMapping("/save")
	@RequiresPermissions("mall:liveroom:save")
	public R save(@RequestBody LiveRoomPOJO mallLiveRoom ,HttpServletRequest request){
		if(StringUtils.isBlank(mallLiveRoom.getLiveRoomName())){
			return R.error("[直播室名称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+mallLiveRoom.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		/*if(mallLiveRoom.getLiveRoomChannelId() == 0){
			return R.error("[直播间频道号]不能为空");
		}
		if(StringUtils.isBlank(mallLiveRoom.getLiveRoomChannelPassword())){
			return R.error("[直播间频道密码]不能为空");
		}*/
		if(StringUtils.isBlank(mallLiveRoom.getGenseeLiveId())){
			return R.error("[展示互动直播id]不能为空！！！");
		}
		if(StringUtils.isBlank(mallLiveRoom.getGenseeLiveNum())){
			return R.error("[展示互动直播房间号]不能为空！！！");
		}
		if(StringUtils.isNotBlank(mallLiveRoom.getLiveRoomRemake()) && mallLiveRoom.getLiveRoomRemake().length() > 50){
			return R.error("[直播房间描述]不能超过50个字！！！");
		}
		mallLiveRoom.setSchoolId(SchoolIdUtils.getSchoolId(request));
		mallLiveRoom.setCreator(getUserId());
		mallLiveRoom.setModifier(mallLiveRoom.getCreator());
		mallLiveRoomService.save(mallLiveRoom);	
		
		return R.ok();
	}
	
	/**
	 * 修改直播间信息
	 */
	@SysLog("修改直播间")
	@ResponseBody
	@RequestMapping("/update")
	@RequiresPermissions("mall:liveroom:update")
	public R update(@RequestBody LiveRoomPOJO mallLiveRoom){
		if(StringUtils.isBlank(mallLiveRoom.getLiveRoomName())){
			return R.error("[直播室名称]不能为空！！！");
		}
		if(StringUtils.isBlank(""+mallLiveRoom.getProductId())){
			return R.error("[产品线]不能为空！！！");
		}
		/*if(mallLiveRoom.getLiveRoomChannelId() == 0){
			return R.error("[直播间频道号]不能为空");
		}
		if(StringUtils.isBlank(mallLiveRoom.getLiveRoomChannelPassword())){
			return R.error("[直播间频道密码]不能为空");
		}*/
		if(StringUtils.isBlank(mallLiveRoom.getGenseeLiveId())){
			return R.error("[展示互动直播id]不能为空！！！");
		}
		if(StringUtils.isBlank(mallLiveRoom.getGenseeLiveNum())){
			return R.error("[展示互动直播房间号]不能为空！！！");
		}
		if(StringUtils.isNotBlank(mallLiveRoom.getLiveRoomRemake()) && mallLiveRoom.getLiveRoomRemake().length() > 50){
			return R.error("[直播房间描述]不能超过50个字！！！");
		}
		mallLiveRoom.setModifier(getUserId());
		mallLiveRoomService.update(mallLiveRoom);
		return R.ok();
	}
	
	/**
	 * 删除直播间信息
	 */
	@SysLog("删除直播间")
	@ResponseBody
	@RequestMapping("/delete")
	@RequiresPermissions("mall:liveroom:delete")
	public R delete(@RequestBody Long[] liveRoomIds){
		mallLiveRoomService.deleteBatch(liveRoomIds);	
		return R.ok();
	}
	
	/**
	 * 禁用
	 */
	@SysLog("禁用直播间")
	@ResponseBody
	@RequestMapping("/pause")
	@RequiresPermissions("mall:liveroom:pause")
	public R pause(@RequestBody Long[] liveRoomIds){
		mallLiveRoomService.pause(liveRoomIds);
		return R.ok();
	}
	
	/**
	 * 启用
	 */
	@SysLog("启用直播间")
	@ResponseBody
	@RequestMapping("/resume")
	@RequiresPermissions("mall:liveroom:resume")
	public R resume(@RequestBody Long[] liveRoomIds){
		mallLiveRoomService.resume(liveRoomIds);
		return R.ok();
	}
	
}
