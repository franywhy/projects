package com.hqjy.synchronize_la_statistics.service;

import com.hqjy.synchronize_la_statistics.base.BaseService;
import com.hqjy.synchronize_la_statistics.entity.BiLiveAnswerRecord;

/**
 * @author zhouyibin
 * @date 2018/12/13
 * @desc
 */
public interface BiLiveAnswerRecordService extends BaseService<BiLiveAnswerRecord,Long> {
    Integer total(String classPlanLiveId);
}
