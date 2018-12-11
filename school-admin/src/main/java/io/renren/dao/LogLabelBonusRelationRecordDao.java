package io.renren.dao;

import io.renren.entity.LogLabelBonusRelationRecordEntity;

import io.renren.pojo.LogLabelBonusRelationRecordPOJO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 标签对应红包限额关系的修改记录表
 * 
 * @author vince
 * @date 2018-08-29 11:08:11
 */
@Repository
public interface LogLabelBonusRelationRecordDao extends BaseDao<LogLabelBonusRelationRecordEntity> {

    List<LogLabelBonusRelationRecordPOJO> queryPOJOList(Map<String,Object> map);

    int queryPOJOListTotal(Map<String,Object> map);

}
