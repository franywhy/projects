package com.hqjy.msg.mapper;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.mybatis.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

@Component
public interface MsgMessageMapper extends BaseMapper<MsgMessage> {
    @Update("UPDATE msg_message SET send_time = #{msgMessage.sendTime},dr=#{msgMessage.dr},rec_by=#{msgMessage.recBy},   "
            +"message = #{msgMessage.message},version=#{msgMessage.version},send_status=#{msgMessage.sendStatus},msg_sort=#{msgMessage.msgSort} WHERE id = #{id}")
    public int update(@Param("id") Integer id, @Param("msgMessage") MsgMessage msgMessage);
}
