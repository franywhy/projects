package io.renren.modules.job.task;

import io.renren.modules.job.service.SysConfigMysqlService;
import io.renren.modules.job.utils.HttpUtils;
import io.renren.modules.sys.service.SysConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 根据nc排课信息在蓝鲸生成学员规划和学习计划
 * Created by DL on 2018/8/20.
 */
@Component("io.renren.modules.job.task.OpenUserClassPlanByNCMsgTask")
public class OpenUserClassPlanByNCMsgTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static  final String TASK_KEY = "task_key";
    @Value("${lj.admin.url}")
    private String ljDomain;
    @Autowired
    private SysConfigMysqlService sysConfigMysqlService;

    public void execute(Map<String,Object> params) {
        try {
            String value = sysConfigMysqlService.getValue("TASK_KEY", "task");
            String url = ljDomain+"/task/openUserClassPlanByNCMsg?key="+value;
            logger.error("蓝鲸系统地址:{}",url);
            String result = HttpUtils.httpRequest(url);
            logger.error("http  result"+result);
            logger.error("OpenUserClassPlanByNCMsgTask success !!");
        }catch (Exception e){
           logger.error("OpenUserClassPlanByNCMsgTask file cause:{}",e.toString());
        }
    }
}
