package io.renren.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.entity.SexEntity;
import io.renren.service.SexService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 性别表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-27 14:49:25
 */
@Controller
@RequestMapping("/mall/sex")
public class SexController {
	@Autowired
	private SexService sexService;
	
	/**
	 * 性别下拉列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectList")
	public R areaList(){
		return R.ok().put("data", sexService.findSexList());
	}
	
}
