package io.renren.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.entity.SchoolReportEntity;
import io.renren.service.SchoolReportService;
import io.renren.utils.PageUtils;
import io.renren.utils.R;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 * 校区报表
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-03-19 09:17:36
 */
@RestController
@RequestMapping("/schoolReport")
public class SchoolReportController {
	@Autowired
	private SchoolReportService schoolReportService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	public R list(HttpServletRequest request){
        Long classTeacherId = null;//班主任
        String startDate = null;
        String endDate = null;
        String createDate = null;
        int reportType = -1;

        //总在读人数
        int totalLearningNum = 0;
        //总休学人数
        int totalPauseNum = 0;
        //总免考人数
        int totalExemptNum = 0;
        //总弃考人数
        int totalAbandonNum = 0;
        //总失联人数
        int totalNocontactNum = 0;
        //总其他人数
        int totalOthersNum = 0;
        //总人数
        int totalNum = 0;
        //总换算管理人数
        int totalManageNum = 0;
        //总实际出勤时间
        Long totalWatchDuration = 0L;
        //总应出勤时间
        Long totalFullDuration = 0L;
        try {
            classTeacherId = ServletRequestUtils.getLongParameter(request, "classTeacherId", 0);
            startDate = ServletRequestUtils.getStringParameter(request, "startDate");
            endDate = ServletRequestUtils.getStringParameter(request, "endDate");
            createDate = ServletRequestUtils.getStringParameter(request, "createDate");
            reportType = ServletRequestUtils.getIntParameter(request,"reportType",-1);
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("classTeacherId",classTeacherId);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("createDate",createDate);
        if (reportType != -1) {
            map.put("reportType", reportType);
        }
		List<SchoolReportEntity> schoolReportList = schoolReportService.queryList(map);
		int total = schoolReportService.queryTotal(map);
		if (schoolReportList != null && schoolReportList.size()>0){
            for (SchoolReportEntity entity : schoolReportList) {
                //换算管理人数(休学,失联5个算一个)
                int pauseNum = entity.getPauseNum()/5;
                int noContactNum = entity.getNonContactNum()/5;
                entity.setManageNum(entity.getLearningNum()+entity.getAbandonNum()+entity.getExemptNum()+pauseNum+noContactNum);
                //出勤率
                if (entity.getFullDuration() != 0L ) {
                    entity.setLivePerTxt(new BigDecimal(entity.getWatchDuration() * 100.0 / entity.getFullDuration()).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
                }else {
                    entity.setLivePerTxt("0.00%");
                }
                //小计
                totalLearningNum += entity.getLearningNum();
                totalPauseNum += entity.getPauseNum();
                totalExemptNum += entity.getExemptNum();
                totalAbandonNum += entity.getAbandonNum();
                totalNocontactNum += entity.getNonContactNum();
                totalOthersNum += entity.getOthersNum();
                totalNum += entity.getTotalNums();
                totalManageNum += entity.getManageNum();
                totalWatchDuration += entity.getWatchDuration();
                totalFullDuration += entity.getFullDuration();
            }
            SchoolReportEntity entity = new SchoolReportEntity();
            entity.setClassTeacherName("小计");
            entity.setLearningNum(totalLearningNum);
            entity.setPauseNum(totalPauseNum);
            entity.setAbandonNum(totalAbandonNum);
            entity.setExemptNum(totalExemptNum);
            entity.setNonContactNum(totalNocontactNum);
            entity.setOthersNum(totalOthersNum);
            entity.setTotalNums(totalNum);
            entity.setManageNum(totalManageNum);
            if (totalFullDuration != 0L) {
                entity.setLivePerTxt(new BigDecimal(totalWatchDuration * 100.0 / totalFullDuration).setScale(2, BigDecimal.ROUND_HALF_UP) + "%");
            }else {
                entity.setLivePerTxt("0.00%");
            }
            schoolReportList.add(entity);
        }
		
		PageUtils pageUtil = new PageUtils(schoolReportList, total,request);
		
		return R.ok().put("data", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	public R info(@PathVariable("id") Long id){
		SchoolReportEntity schoolReport = schoolReportService.queryObject(id);
		
		return R.ok().put("schoolReport", schoolReport);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	public R save(@RequestBody SchoolReportEntity schoolReport){
		schoolReportService.save(schoolReport);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	public R update(@RequestBody SchoolReportEntity schoolReport){
		schoolReportService.update(schoolReport);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	public R delete(@RequestBody Long[] ids){
		schoolReportService.deleteBatch(ids);
		
		return R.ok();
	}
	
}
