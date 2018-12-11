package io.renren.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import io.renren.service.MallLevelService;
import io.renren.utils.R;
import io.renren.utils.SchoolIdUtils;


/**
 * 学历层次表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-25 16:38:54
 */
@Controller
@RequestMapping("/mall/level")
public class MallLevelController {
	@Autowired
	private MallLevelService mallLevelService;
	
	
	/**
	 * 学历层次下拉列表
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/selectList")
	public R professionList(Integer page, Integer limit ,HttpServletRequest request){
		Map<String, Object> map = new HashMap<>();
		map.put("offset", (page - 1) * limit);
		map.put("limit", limit);
		map.put("schoolId", SchoolIdUtils.getSchoolId(request));
		return R.ok().put("data4", mallLevelService.findLevelList(map));
	}
	
	
	/**
	 * 学历层次映射列表
	 * */
	
	@ResponseBody
	@RequestMapping("/getSelectionList")
	public R getSelectionList(HttpServletRequest request){
		return R.ok().put("levelSelections", mallLevelService.querySelectionList(SchoolIdUtils.getSchoolId(request)));
	}
}
