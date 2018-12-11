package io.renren.service.manage;

import io.renren.entity.CourseClassplanEntity;

/**
 * Created by Administrator on 2018/8/21 0021.
 */
public interface MessageProductor2KuaidaCourseClassplanService {

    void pushToMessageQueue(CourseClassplanEntity en);

}
