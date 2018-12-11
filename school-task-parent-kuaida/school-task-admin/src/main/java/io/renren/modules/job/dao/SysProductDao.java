package io.renren.modules.job.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.modules.job.entity.SysProductEntity;

/**
 * 产品线
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 17:12:41
 */
public interface SysProductDao extends BaseMDao<SysProductEntity> {
	Float queryCoefficient(@Param("productId") Long productId);

    Float queryRecordEfficient(Long productId);
}
