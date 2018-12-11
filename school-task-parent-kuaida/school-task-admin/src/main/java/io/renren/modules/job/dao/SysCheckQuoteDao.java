package io.renren.modules.job.dao;

import java.util.Map;

import io.renren.modules.job.utils.SyncDateConstant;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:31:36
 */
public interface SysCheckQuoteDao {

    String syncDate(Map<String, Object> map);
    
    void updateSyncTime(Map<String, Object> map);
}
