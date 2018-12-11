package io.renren.controller;

import io.renren.entity.SysUserLaberEntity;
import io.renren.pojo.TopicsLaberPOJO;
import io.renren.service.SysUserLaberService;
import io.renren.utils.JSONUtil;
import io.renren.utils.R;
import io.renren.utils.http.HttpClientUtil4_3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

	



/**
 * 标签管理员表
 * 
 * @author vince
 * @date 2018-05-24 17:01:37
 */
@RestController
@RequestMapping("/topics/topicsLaber")
public class TopicsLaberController {

	@Value("#{application['kuaida.api']}")
	private String KUAIDA_API;

	@Autowired
	private SysUserLaberService sysUserLaberService;
	
	/**
	 * 获取所有标签树
	 */
	@RequestMapping("/allLaberList")
	public R allLaberList(@RequestParam Map<String, Object> params,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
        List<Object> list = new ArrayList();
		//查询列表数据
		String url = KUAIDA_API+"/userinfo/topic_input_tips_lanjing";
		Map<String,Object> kuaijiMap = new HashMap<>();
		Map<String,Object> zikaoMap = new HashMap<>();
		TopicsLaberPOJO kuaijipojo = new TopicsLaberPOJO();
		TopicsLaberPOJO zikaopojo = new TopicsLaberPOJO();
		try {
			String topicsKuaiji = HttpClientUtil4_3.get(url+"?product=0", new HashMap<String,String>());

            kuaijipojo = JSONUtil.jsonToBean(topicsKuaiji,TopicsLaberPOJO.class);
			String topicsZikao = HttpClientUtil4_3.get(url+"?product=1", new HashMap<String,String>());

            zikaopojo = JSONUtil.jsonToBean(topicsZikao,TopicsLaberPOJO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String,Object> map1 = new HashMap<>();
		map1.put("_id", -10);
		map1.put("tip_name", "标签");
		map1.put("parent_tip_id", -100);
		Map<String,Object> map2 = new HashMap<>();
		map2.put("_id", -1);
		map2.put("tip_name", "会计标签");
		map2.put("parent_tip_id", -10);
		Map<String,Object> map3 = new HashMap<>();
		map3.put("_id", -2);
		map3.put("tip_name", "自考标签");
		map3.put("parent_tip_id", -10);
		
		list.add(map1);
		list.add(map2);
		list.add(map3);
		
		
		list.addAll(kuaijipojo.getData());
		list.addAll(zikaopojo.getData());
		
		System.out.println(list);
        return R.ok().put(list);
	}
	
	/**
	 * 获取一级标签树
	 */
	@RequestMapping("/allFirstLaberList")
	public R allFirstLaberList(@RequestParam Map<String, Object> params,HttpServletRequest request){
		Map<String,Object> result = new HashMap<>();
        List<Object> list = new ArrayList();
		//查询列表数据
		String url = KUAIDA_API+"/userinfo/topic_input_tips_lanjing_v1";
		Map<String,Object> kuaijiMap = new HashMap<>();
		Map<String,Object> zikaoMap = new HashMap<>();
		TopicsLaberPOJO kuaijipojo = new TopicsLaberPOJO();
		TopicsLaberPOJO zikaopojo = new TopicsLaberPOJO();
		try {
			String topicsKuaiji = HttpClientUtil4_3.get(url+"?product=0", new HashMap<String,String>());

            kuaijipojo = JSONUtil.jsonToBean(topicsKuaiji,TopicsLaberPOJO.class);
			String topicsZikao = HttpClientUtil4_3.get(url+"?product=1", new HashMap<String,String>());

            zikaopojo = JSONUtil.jsonToBean(topicsZikao,TopicsLaberPOJO.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Map<String,Object> map1 = new HashMap<>();
		map1.put("_id", -10);
		map1.put("tip_name", "标签");
		map1.put("parent_tip_id", -100);
		Map<String,Object> map2 = new HashMap<>();
		map2.put("_id", -1);
		map2.put("tip_name", "会计标签");
		map2.put("parent_tip_id", -10);
		Map<String,Object> map3 = new HashMap<>();
		map3.put("_id", -2);
		map3.put("tip_name", "自考标签");
		map3.put("parent_tip_id", -10);
		
		list.add(map1);
		list.add(map2);
		list.add(map3);
		
		list.addAll(kuaijipojo.getData());
		list.addAll(zikaopojo.getData());
		
		System.out.println(list);
        return R.ok().put(list);
	}
	
	/**
	 * 总列表
	 */
	@RequestMapping("/diffLaberList")
	public R diffLaberList(@RequestParam Map<String, Object> params,HttpServletRequest request){
		Map<String,Object> resultMap = new HashMap<>();
		//查询列表数据
		String paramProduct = (String) params.get("product");//0：会计标签，1：自考标签
		String url = KUAIDA_API+"/userinfo/topic_input_tips_lanjing";
		String param = "product="+paramProduct;
		try {
			String resultString = HttpClientUtil4_3.get(url+"?"+param, new HashMap<String,String>());
			
			resultMap = JSONUtil.jsonToMap(resultString);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		return R.ok().put("data", resultMap.get("data"));
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id){
		SysUserLaberEntity sysUserLaber = sysUserLaberService.queryObject(id);
		
		return R.ok().put("sysUserLaber", sysUserLaber);
	}
	
	
}
