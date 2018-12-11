package com.hqjy.msg.mapper;

import com.hqjy.msg.model.MsgMessageDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/12/20 0020.
 */
public interface TestMapper  {
    public void batchSave(@Param("tableName") String tableName,@Param("list") List<MsgMessageDetail> list);
//    public void batchSave(Map<String, Object> map,List<MsgMessageDetail> list);

    public List selectAll();

    public  int tableExist(Map<String, Object> map);

    public void createTable(Map<String, Object> map);

    public MsgMessageDetail getMsgDetail(@Param("tableName") String tableName,@Param("map")Map<String, Object> map);
    public int isExistMsgDetail(@Param("tableName") String tableName,@Param("map")Map<String, Object> map);

    public int updateMsgDetailReaded(@Param("tableName") String tableName,@Param("user_id")String userId,@Param("code")String code);
    public int batchUpdateMsgDetailReaded(@Param("lists") List lists);

    public List<MsgMessageDetail> getDetailsByUserAndTimes(@Param("user_id") String userId,@Param("tables") List tables,@Param("map")Map<String, Object> map);
}
