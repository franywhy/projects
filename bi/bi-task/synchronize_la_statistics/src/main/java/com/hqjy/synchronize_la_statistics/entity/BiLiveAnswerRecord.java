package com.hqjy.synchronize_la_statistics.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * bi_live_answer_record 实体类
 * 
 * @author : zhouyibin   gralves@163.com 
 * @date : 2018/12/13 10:08
 */
@Data
public class BiLiveAnswerRecord implements Serializable {
    /**
	 * 主键 id
	 **/
    private Long id;

    /**
	 * 直播课次ID classplan_live_id
	 **/
    private String classplanLiveId;

    /**
	 * 直播课次问答已答总量 live_answer_num
	 **/
    private Long liveAnswerNum;

    /**
	 * 直播课次问答提问总量 live_total_num
	 **/
    private Long liveTotalNum;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}