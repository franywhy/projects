package io.renren.service.manage;

import io.renren.entity.OrderMessageConsumerEntity;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public interface StudentCourseService {

    void insertBatch(Long userId, OrderMessageConsumerEntity order);

    int deleteByNcId(String ncId);
}
