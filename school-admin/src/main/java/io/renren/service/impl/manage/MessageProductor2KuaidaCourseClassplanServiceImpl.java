package io.renren.service.impl.manage;

import com.google.gson.Gson;
import io.renren.entity.CourseClassplanEntity;
import io.renren.service.CoursesService;
import io.renren.service.SysUserService;
import io.renren.service.manage.MessageProductor2KuaidaCourseClassplanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/21 0021.
 * @author hq
 */
@Service
public class MessageProductor2KuaidaCourseClassplanServiceImpl implements MessageProductor2KuaidaCourseClassplanService {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProductor2KuaidaCourseClassplanServiceImpl.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private CoursesService coursesService;

    @Value("#{rabbitmq['teacher.plan.sync.kd']}")
    private String COURSECLASSPLANMQ;

    @Override
    public void pushToMessageQueue(CourseClassplanEntity en) {
        String mobile = sysUserService.queryMobileByUserId(en.getTeacherId());
        List<Map<String,Object>> list = coursesService.queryMapByCourseId(en.getCourseId());
        String courseNo = "";
        String businessId = "";
        for (Map<String,Object> item : list) {
            businessId = (String) item.get("businessId");
            courseNo = (String) item.get("courseNo");
            if(businessId.startsWith("kuaiji")) {
                businessId = "kuaiji";
                break;
            }
            if(businessId.startsWith("zikao")) {
                businessId = "zikao";
                break;
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("mobile",mobile);
        map.put("courseNo",courseNo);
        map.put("businessId",businessId);
        Gson gson = new Gson();
        String message = gson.toJson(map).toString();
        LOG.info(COURSECLASSPLANMQ+"    product message:    "+message);
        amqpTemplate.convertAndSend(COURSECLASSPLANMQ, message);
    }

}
