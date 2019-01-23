package com.hqjy.synchronize_la_statistics.service.impl;

import com.hqjy.synchronize_la_statistics.base.BaseServiceImpl;
import com.hqjy.synchronize_la_statistics.dao.BiLiveAnswerRecordDao;
import com.hqjy.synchronize_la_statistics.entity.BiLiveAnswerRecord;
import com.hqjy.synchronize_la_statistics.service.BiLiveAnswerRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author zhouyibin
 * @date 2018/12/13
 * @desc
 */
@Slf4j
@Service
public class BiLiveAnswerRecordServiceImpl extends BaseServiceImpl<BiLiveAnswerRecordDao, BiLiveAnswerRecord,Long> implements BiLiveAnswerRecordService {
    @Override
    public Integer total(String classPlanLiveId) {
        return totalCount(classPlanLiveId);
    }
}
