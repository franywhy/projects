package io.renren.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import io.renren.entity.RecordReFundsEntity;
import io.renren.service.RecordReFundsService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;


/**
 * 报考登记
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-26 15:21:19
 */
@Controller
@RequestMapping("recordRefunds")
public class RecordReFundsController extends AbstractController {
    @Autowired
    private RecordReFundsService recordReFundsService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    @RequiresPermissions("recordRefunds:list")
    public R list(HttpServletRequest request){
       Map<String, Object> map = getMapPage(request);
        intQuery(map, request, "teacherId");
        intQuery(map, request, "applyStatus");
        stringQuery(map, request, "name");
        //查询列表数据
        List<RecordReFundsEntity> recordReFundsList = recordReFundsService.queryList(map);
        int total = recordReFundsService.queryTotal(map);
        PageUtils pageUtil = new PageUtils(recordReFundsList, total , request);
        return R.ok().put(pageUtil);
    }
    
	/**
	 * 导入
	 */
	@ResponseBody
	@RequestMapping(value = "/importData" , method = RequestMethod.POST)
	//@RequiresPermissions("examinationresult:import")
	@Transactional
	public R importData(HttpServletRequest request){
		MultipartHttpServletRequest mpReq = (MultipartHttpServletRequest) request;
		MultipartFile file = mpReq.getFile("file_data");
		int result = 0;
		try {
			result = recordReFundsService.importData(file,getUser());
		} catch (Exception e) { 
			e.printStackTrace();
			return R.ok(e.getMessage());
		}
		return R.ok("成功导入 " + result + " 条数据"); 
	} 
}
