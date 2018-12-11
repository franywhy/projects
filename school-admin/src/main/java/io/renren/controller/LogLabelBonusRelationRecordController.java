package io.renren.controller;
import io.renren.entity.LogLabelBonusRelationRecordEntity;
import io.renren.pojo.LogLabelBonusRelationRecordPOJO;
import io.renren.service.LogLabelBonusRelationRecordService;
import io.renren.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import io.renren.utils.R;

import javax.servlet.http.HttpServletRequest;


/**
 * 标签对应红包限额关系的修改记录表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-08-29 11:08:11
 */
@RestController
@RequestMapping("/manage/loglabelbonusrelationrecord")
public class LogLabelBonusRelationRecordController extends AbstractController{
	@Autowired
	private LogLabelBonusRelationRecordService logLabelBonusRelationRecordService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
//	@RequiresPermissions("generator:loglabelbonusrelationrecord:list")
	public R list(HttpServletRequest request){
		//查询列表数据
		Map<String, Object> map = getMapPage(request);

        List<LogLabelBonusRelationRecordEntity> logLabelBonusRelationRecordList = logLabelBonusRelationRecordService.queryList(map);
		int total = logLabelBonusRelationRecordService.queryTotal(map);


		PageUtils pageUtil = new PageUtils(logLabelBonusRelationRecordList, total, request);
		
		return R.ok().put("page", pageUtil);
	}

    /**
     * 列表
     */
    @RequestMapping("/listPOJO")
    public R listPOJO(HttpServletRequest request){
        //查询列表数据
        Map<String, Object> map = getMapPage(request);
        longQuery(map, request, "labelId");

        List<LogLabelBonusRelationRecordPOJO> logLabelBonusRelationRecordList = logLabelBonusRelationRecordService.queryPOJOList(map);
        int total = logLabelBonusRelationRecordService.queryPOJOListTotal(map);

        PageUtils pageUtil = new PageUtils(logLabelBonusRelationRecordList, total, request);

        return R.ok().put("page", pageUtil);
    }
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
//	@RequiresPermissions("generator:loglabelbonusrelationrecord:info")
	public R info(@PathVariable("id") Long id){
		LogLabelBonusRelationRecordEntity logLabelBonusRelationRecord = logLabelBonusRelationRecordService.queryObject(id);
		
		return R.ok().put("logLabelBonusRelationRecord", logLabelBonusRelationRecord);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("generator:loglabelbonusrelationrecord:save")
	public R save(@RequestBody LogLabelBonusRelationRecordEntity logLabelBonusRelationRecord){
		logLabelBonusRelationRecordService.save(logLabelBonusRelationRecord);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("generator:loglabelbonusrelationrecord:update")
	public R update(@RequestBody LogLabelBonusRelationRecordEntity logLabelBonusRelationRecord){
		logLabelBonusRelationRecordService.update(logLabelBonusRelationRecord);
		
		return R.ok();
	}
	

	
}
