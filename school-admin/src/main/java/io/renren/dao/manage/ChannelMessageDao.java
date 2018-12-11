package io.renren.dao.manage;

import io.renren.entity.manage.ChannelMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ChannelMessageDao {

    int deleteByMessage(@Param("messageId") Long messageId, @Param("messageType") Integer messageType);

    int insertBatch(List<ChannelMessage> list);
}