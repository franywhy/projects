package io.renren.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import io.renren.entity.SysUserEntity;
import io.renren.pojo.MallAdjustAreaPOJO;
import io.renren.service.SysUserRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.entity.MallAdjustAreaEntity;
import io.renren.service.MallAdjustAreaService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 特殊情况-转省份
 * 
 * @author linchaokai
 * @date 2018-08-10 9:54:13
 */
@Controller
@RequestMapping("malladjustarea")
public class MallAdjustAreaController extends AbstractController {
	@Autowired
	private MallAdjustAreaService mallAdjustAreaService;

	@Autowired
	private SysUserRoleService sysUserRoleService;
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("malladjustarea:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "userName");
		intQuery(map, request, "applystatus");
		longQuery(map, request, "userId");
		//查询列表数据
		List<MallAdjustAreaPOJO> mallAdjustAreaList = mallAdjustAreaService.queryPojoList(map);
		int total = mallAdjustAreaService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallAdjustAreaList, total , request);
		return R.ok().put(pageUtil);
	}
}
