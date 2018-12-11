package com.hq.learningcenter.school.service;

import com.hq.learningcenter.school.pojo.ExaminationResultPOJO;

import java.util.List;
import java.util.Map;

/**
 * 成绩登记
 * @author linchaokai
 * @date 2018/8/8 17:12
 */
public interface ExaminationResultService {
    List<ExaminationResultPOJO> queryList(Map<String, Object> map);
    /**
     * @author linchaokai
     * @date 2018/8/9 9:33
     * @param  userId 用户id
     * @param  registrationId 报考单id
     * @param  score 分数
     * @param  img 图片路径
     * @return
     */
    void save(Long userId,Long registrationId,int score,String img);
}
