package io.renren.service.impl.manage;

import io.renren.dao.manage.ChannelMessageDao;
import io.renren.entity.manage.ChannelMessage;
import io.renren.service.manage.ChannelMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
@Service
public class ChannelMessageServiceImpl implements ChannelMessageService {

    @Autowired
    private ChannelMessageDao channelMessageDao;

    @Override
    public void saveBatch(List<ChannelMessage> channelMessages) {
        channelMessageDao.insertBatch(channelMessages);
    }

    @Override
    public void deleteByMessage(Long messageId, int messageType) {
        channelMessageDao.deleteByMessage(messageId, messageType);
    }
}
