package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.CourseUserplanDao;
import io.renren.modules.job.entity.CourseUserplanEntity;
import io.renren.modules.job.service.CourseUserplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("courseUserplanService")
public class CourseUserplanServiceImpl implements CourseUserplanService {
	@Autowired
	private CourseUserplanDao courseUserplanDao;

	/**
	 * 查询 新的学员规划（会计产品线）
	 * @param ts
	 * @return
	 */
	@Override
	public List<Map<String, Object>> queryKJClassMessage(String ts) {
		return this.courseUserplanDao.queryKJClassMessage(ts);
	}


	@Override
	public List<String> queryCodeListByCommodityId(Object object) {
		return this.courseUserplanDao.queryCodeListByCommodityId(object);
	}

	@Override
	public CourseUserplanEntity queryUserplanObjectByOrderId(Long orderId) {
		return this.courseUserplanDao.queryUserplanObjectByOrderId(orderId);
	}

    @Override
    public List<Map<String, Object>> queryClassMessageByProductId(String ts) {
        return courseUserplanDao.queryClassMessageByProductId(ts);
    }


}
