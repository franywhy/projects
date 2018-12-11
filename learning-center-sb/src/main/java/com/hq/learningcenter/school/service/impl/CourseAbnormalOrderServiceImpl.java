package com.hq.learningcenter.school.service.impl;

import com.hq.learningcenter.school.dao.CourseAbnormalOrderDao;
import com.hq.learningcenter.school.entity.CourseAbnormalOrderEntity;
import com.hq.learningcenter.school.enums.AuditStatusEnum;
import com.hq.learningcenter.school.enums.CourseAbormalTypeEnum;
import com.hq.learningcenter.school.pojo.CourseAbnormalOrderPOJO;
import com.hq.learningcenter.school.service.CourseAbnormalOrderService;
import com.hq.learningcenter.utils.DateUtils;
import com.hq.learningcenter.school.rest.persistent.KGS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service("courseAbnormalOrderService")
public class CourseAbnormalOrderServiceImpl implements CourseAbnormalOrderService {
	@Autowired
	private CourseAbnormalOrderDao courseAbnormalOrderDao;

	@Resource
	KGS courseAbnormalOrderNoKGS;

	private static String DOWN_EXCEL_STRING = "";

    @Override
    public List<CourseAbnormalOrderPOJO> queryList(Map<String, Object> map){
        return courseAbnormalOrderDao.queryPojoList(map);
    }

    @Override
    public List<Map<String,Object>> queryOrderList( Long userId,String businessId) {
        return courseAbnormalOrderDao.queryOrderPOJOList(userId,businessId);
    }

    @Override
	public void save(Long userId,Long orderId,String startTime,String expectEndTime,String abnormalReason){
        CourseAbnormalOrderEntity courseAbnormalOrder = new CourseAbnormalOrderEntity();
        courseAbnormalOrder.setOrderId(orderId);
        courseAbnormalOrder.setStartTime(DateUtils.parse(startTime));
        courseAbnormalOrder.setExpectEndTime(DateUtils.parse(expectEndTime));
        courseAbnormalOrder.setAbnormalReason(abnormalReason);
        courseAbnormalOrder.setCreatePerson(userId);
        courseAbnormalOrder.setProductId(courseAbnormalOrderDao.queryProductId(orderId));
        courseAbnormalOrder.setAbnormalType(CourseAbormalTypeEnum.xiuxue.getValue());
        courseAbnormalOrder.setAuditStatus(AuditStatusEnum.daishenhe.getValue());
        final String orderNo = "AO" + courseAbnormalOrderNoKGS.nextId();
        courseAbnormalOrder.setOrderno(orderNo);
        courseAbnormalOrderDao.save(courseAbnormalOrder);
	}
    @Override
	public String verifyStatus(Long orderId, Date startTime, Date endTime){
		Map queryMap = new HashMap();
		queryMap.put("orderId",orderId);
		queryMap.put("startTime",startTime);
		CourseAbnormalOrderPOJO courseAbnormalOrderPOJO = courseAbnormalOrderDao.verifyStatus(queryMap);
		if(courseAbnormalOrderPOJO != null){
			return "此班型已申请，请勿重复提交！";
		}
		return null;
	}

    @Override
    public void updateCancel(Integer auditStatus,Long id,Long userId,Date date){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",id);
        map.put("auditStatus",auditStatus);
        map.put("updatePerson",userId);
        map.put("updateTime",date);
        courseAbnormalOrderDao.updateCancel(map);
    }
}
