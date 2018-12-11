package io.renren.service.manage;

import io.renren.entity.manage.ChannelMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public interface ChannelMessageService {

    void saveBatch(List<ChannelMessage> channelMessages);

    void deleteByMessage(Long messageId, int messageType);
}
