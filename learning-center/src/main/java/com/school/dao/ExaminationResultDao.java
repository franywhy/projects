package com.school.dao;

import com.school.entity.ExaminationResultEntity;
import com.school.pojo.ExaminationResultPOJO;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by DL on 2018/4/2.
 */
@Repository
public interface ExaminationResultDao {

    //获取成绩列表
    List<ExaminationResultPOJO> queryPojoList(Map<String, Object> map);

    //登记成绩
    void save(ExaminationResultEntity entity);

}
