package com.hqjy.synchronize_la_statistics.dao;

import com.hqjy.synchronize_la_statistics.base.BaseDao;
import com.hqjy.synchronize_la_statistics.entity.BiLiveAnswerRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * bi_live_answer_record 持久化层
 * 
 * @author : zhouyibin   gralves@163.com 
 * @date : 2018/12/13 10:08
 */
@Mapper
public interface BiLiveAnswerRecordDao extends BaseDao<BiLiveAnswerRecord, Long> {

}