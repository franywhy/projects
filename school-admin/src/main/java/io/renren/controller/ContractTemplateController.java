package io.renren.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.renren.entity.ContractTemplateEntity;
import io.renren.service.ContractTemplateService;
import io.renren.service.SysCheckQuoteService;
import io.renren.utils.PageUtils;
import io.renren.utils.QuoteConstant;
import io.renren.utils.R;

@Controller
@RequestMapping("/contractTemplate")
public class ContractTemplateController extends AbstractController{
	
	@Autowired
	private ContractTemplateService contractTemplateService;
	@Autowired
	private SysCheckQuoteService sysCheckQuoteService;
	
	@ResponseBody
	@RequestMapping("/list")
	public R list(HttpServletRequest request){
		Map<String,Object> map = getMapPage(request);
		List<Map<String,Object>> resultMap = contractTemplateService.queryListMap(map);
		int total = contractTemplateService.queryTotal(map);
		
		PageUtils pageUtil = new PageUtils(resultMap, total, request);	
		return R.ok().put("page",pageUtil); 
	}
	
	@ResponseBody
	@RequestMapping("/save")
	public R save(@RequestBody ContractTemplateEntity contractTemplate,HttpServletRequest request){
		if(StringUtils.isBlank(contractTemplate.getTemplateId()) || StringUtils.isBlank(contractTemplate.getTemplateName())
				||StringUtils.isBlank(contractTemplate.getCompanyName()) ||  contractTemplate.getCompanyId()==null)	 {		
				
			return R.error("模板Id、模板名称不能为空,模板公司Id和模板公司名称不能为空！");
		}
		contractTemplateService.save(contractTemplate);
	    return R.ok();
	}
	
	@ResponseBody
	@RequestMapping("/update")
	public R update(@RequestBody ContractTemplateEntity contractTemplate,HttpServletRequest request){
		if(StringUtils.isBlank(contractTemplate.getTemplateId()) || StringUtils.isBlank(contractTemplate.getTemplateName())
				||StringUtils.isBlank(contractTemplate.getCompanyName()) ||  contractTemplate.getCompanyId()==null
				){
			return R.error("模板Id、模板名称不能为空,模板公司Id和模板公司名称不能为空！");
		}
		contractTemplateService.update(contractTemplate);
		return R.ok();
	}
	
	@ResponseBody
	@RequestMapping("/delete/{id}")
	public R delete(@PathVariable("id") Long id,HttpServletRequest request){
		Map<String, Object> map = getMap(request);
 		if(id == null){
			return R.error();
		}
		map.put("id", id);
		map.put("value", id);
		ArrayList<String> exceptList = new ArrayList<String>();
		String exceptMsg = "删除失败的具体原因如下:";
		String errMsg="";
		if(sysCheckQuoteService.checkQuote(map , QuoteConstant.contract_template_id)){
			exceptMsg="商品档案有引用！";
			exceptList.add(exceptMsg);
		}
		for (int i = 0; i < exceptList.size(); i++)
		{
			errMsg += exceptList.get(i) + "<br>";
		}
		if(errMsg.equals("")){
			contractTemplateService.delete(map);	
			return R.ok();
		}else{ 
			return R.error(errMsg);
		}
	}
	
	@ResponseBody
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id,HttpServletRequest request){
		Map<String, Object> map = getMap(request);
 		if(id == null){
			return R.error();
		}
		map.put("id", id);
	
		ContractTemplateEntity entity = contractTemplateService.queryObject(map);
		return R.ok().put(entity);
	}
	
}
