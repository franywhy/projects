package com.hqjy.msg.provide.operation;

import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.GetObjectFromRedisService;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by baobao on 2017/12/29 0029.
 */
@Service
public class GetObjectFromRedisServiceImpl implements GetObjectFromRedisService {

    @Autowired
    private RedisMsgService<Objects> redisMsgService;

    @Override
    public Object getMsgFromRedis(String msgId) {
        return this.redisMsgService.getCacheMap(msgId+Constant.MESSAGE_REDIS);
    }

    @Override
    public List getConsumerMsgFromRedis(String userId, String startTime, String endTime) {
        long start  = DateUtils.stringToDate(startTime,DateUtils.DATE_FORMAT).getTime();
        long end  = DateUtils.stringToDate(endTime,DateUtils.DATE_FORMAT).getTime();
        LinkedHashSet set = (LinkedHashSet) redisMsgService.getZsetCache(userId,start,end,Constant.GET_MSG_MAX_SIZE);
        List list = (List) set.stream().collect(Collectors.toList());
        return list;
    }
}
