package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.entity.SysLogEntity;
import io.renren.service.SysLogService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 系统日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-29 09:57:44
 */
@Controller
@RequestMapping("syslog")
public class SysLogController extends AbstractController {
	@Autowired
	private SysLogService sysLogService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("syslog:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "username");
		stringQuery(map, request, "operation");
		//查询列表数据
		List<SysLogEntity> sysLogList = sysLogService.queryList(map);
		int total = sysLogService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(sysLogList, total , request);	
		return R.ok().put(pageUtil);
	}
}
