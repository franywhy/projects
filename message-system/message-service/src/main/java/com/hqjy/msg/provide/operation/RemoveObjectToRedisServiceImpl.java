package com.hqjy.msg.provide.operation;

import com.hqjy.msg.provide.MsgSend;
import com.hqjy.msg.provide.RemoveObjectToRedisService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by baobao on 2017/12/29 0029.
 */
@Service
public class RemoveObjectToRedisServiceImpl implements RemoveObjectToRedisService {

    @Autowired
    @Qualifier("sendHaveNotAck")
    private MsgSend msgSend;

    @Override
    public void removeMsgToRedis(String msgId, List list) {
        List channels = ListUtils.listsToArray(list, Constant.SPLIT_LIST_SIZE);
        for (int i = 0; i < channels.size(); i++) {
            List items = (List) channels.get(i);
            Map map = new HashMap();
            map.put("list",items);
            map.put("msg_id",msgId);
            //移除到消息的过期队列
            msgSend.sendMsg(Constant.QUEUE_MSG_EXPIRE,map);
        }
    }
}
