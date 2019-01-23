package com.hq.bi.search.mapper;

import com.hq.bi.search.util.PageQuery;
import com.hq.bi.search.vo.LiveEvaluateVo;
import com.hq.bi.search.vo.RecordEvaluateVo;
import com.hq.bi.search.vo.TeachEvaluateVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 评价 Mapper 接口
 * </p>
 *
 * @author hzr
 * @since 2018-12-05
 */
@Mapper
public interface TeachEvaluateMapper {

    /**
     * 获取评价信息
     * @param page
     * @return
     */
    List<TeachEvaluateVo> queryEvaluate(PageQuery page);

    /**
     * 获取直播评价数据
     * @param page
     * @return
     * @author cjc
     * @date 2019-01-08
     */
    List<LiveEvaluateVo> queryLiveEvaluate(PageQuery page);

    /**
     * 获取录播评价数据
     * @param page
     * @return
     * @author cjc
     * @date 2019-01-08
     */
    List<RecordEvaluateVo> queryRecordEvaluate(PageQuery page);
}
