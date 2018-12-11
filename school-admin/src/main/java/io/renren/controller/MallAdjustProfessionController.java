package io.renren.controller;

import io.renren.pojo.MallAdjustProfessionPOJO;
import io.renren.service.MallAdjustProfessionService;
import io.renren.service.SysUserRoleService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
 * 特殊情况-转省份
 * 
 * @author linchaokai
 * @date 2018-08-10 9:54:13
 */
@Controller
@RequestMapping("mallAdjustProfession")
public class MallAdjustProfessionController extends AbstractController {
	@Autowired
	private MallAdjustProfessionService mallAdjustProfessionService;

	@Autowired
	private SysUserRoleService sysUserRoleService;
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("mallAdjustProfession:list")
	public R list(HttpServletRequest request){
		Map<String, Object> map = getMapPage(request);
		stringQuery(map, request, "userName");
		intQuery(map, request, "applystatus");
		longQuery(map, request, "userId");
		//查询列表数据
		List<MallAdjustProfessionPOJO> mallAdjustProfessionList = mallAdjustProfessionService.queryPojoList(map);
		int total = mallAdjustProfessionService.queryTotal(map);
		PageUtils pageUtil = new PageUtils(mallAdjustProfessionList, total , request);
		return R.ok().put(pageUtil);
	}
}
