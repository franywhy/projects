package com.hq.bi.search.service;

import com.hq.bi.search.entity.ClassExamQualityEntity;
import com.hq.bi.search.util.PageQuery;
import com.hq.bi.search.vo.LiveEvaluateVo;
import com.hq.bi.search.vo.RecordEvaluateVo;
import com.hq.bi.search.vo.TeachEvaluateVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 评价 服务类
 * </p>
 *
 * @author hzr
 * @since 2018-12-05
 */
public interface TeachEvaluateService  {

    /**
     * 获取详情评价
      * @param pageQuery
     * @return
     */
    List<TeachEvaluateVo> listEvaluate(PageQuery pageQuery);

    /**
     * 获取直播评价
     * @param pageQuery
     * @return
     */
    List<LiveEvaluateVo> listLiveEvaluate(PageQuery pageQuery);

    /**
     * 获取录播评价
     * @param pageQuery
     * @return
     */
    List<RecordEvaluateVo> listRecordEvaluate(PageQuery pageQuery);

}
