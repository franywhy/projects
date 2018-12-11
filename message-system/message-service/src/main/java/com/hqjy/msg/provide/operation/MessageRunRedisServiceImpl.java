package com.hqjy.msg.provide.operation;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgChannels;
import com.hqjy.msg.model.MsgGroup;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.GetObjectFromRedisService;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.provide.SendAdapterService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.ListUtils;
import com.hqjy.msg.util.StringUtils;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/1/31 0031.
 */
@Service("redis")
public class MessageRunRedisServiceImpl implements MessageRunService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private RedisMsgService<Objects> redisMsgService;
    @Autowired
    private GetObjectFromRedisService getObjectFromRedisService;

    @Autowired
    private SendAdapterService sendAdapterService;

    public boolean hasKey(String key) {
        return redisMsgService.hasKey(key);
    }

    @Override
    public List checkGroup(List groupChannels) throws DefaultException {
        //String notExistsGroups = "";
        List existsList = new ArrayList();
        List notExists = new ArrayList();
        if (ListUtils.listIsExists(groupChannels)) {

            for (int i = 0; i < groupChannels.size(); i++) {
                String groupChannel = (String) groupChannels.get(i);
                if (!redisMsgService.hasKey(groupChannel + Constant.CHANNEL_REDIS)) {
                    //throw new DefaultException(groupChannel + "频道不存在");
                    notExists.add(groupChannel);
                } else {
                    existsList.add(groupChannel);
                }

            }
            if (ListUtils.listIsExists(notExists)) {
                if (notExists.size() == groupChannels.size()) {
                    String notExistsStr = "";
                    for (int i = 0; i < notExists.size(); i++) {
                        String groupChannel = (String) notExists.get(i);
                        notExistsStr += groupChannel + ",";

                    }
                    throw new DefaultException(notExistsStr + "频道不存在");
                }

            }

        }
        return existsList;
        //return notExistsGroups;

         /*groupChannels.stream().forEach((Object p) ->{
             if (!redisMsgService.hasKey(p+Constant.CHANNEL_REDIS)) {
                 try {
                     throw  new DefaultException(p+"频道不存在");
                 } catch (DefaultException e) {
                     throw  new DefaultException(p+"频道不存在");
                 }
             }});*/

    }

    @Override
    public synchronized void saveMsg(MsgMessage message) {
       /* Date after = DateUtils.addDays(DateUtils.stringToDate(message.getSendTimeStr(),DateUtils.DATE_FORMAT),Constant.MSG_SAVE_DAYS);
        int compare = DateUtils.compare(DateUtils.stringToDate(message.getSendTimeStr(),DateUtils.DATE_FORMAT),after);*/
        // 过期时间为-1
        int compare = -1;
        Map map = new HashMap();
        map.put("sendTime", message.getSendTimeStr());
        map.put("msg", message.getMessage());
        map.put("rec", message.getRecBy());
        map.put("title", message.getTitle());
        map.put("content", message.getContent());
        map.put("sendStatus", message.getSendStatus());
        map.put("sendType", message.getSendType());
        map.put("dr", null == message.getDr() ? 0 : message.getDr());
        map.put("msgType", null == message.getMsgType() ? Constant.MSG_TYPE_GENERALLY : message.getMsgType());
        map.put("msgSort", null == message.getMsgSort() ? Constant.MSG_SORT_NOT : message.getMsgSort());

        //System.out.println(StringUtils.objToJsonStr(message));

        // 如果是按时发送，将过期时间设置为永不过期
        if (message.getSendType() == 1) {
            compare = -1;
        }
        redisMsgService.setCacheMap(message.getCode() + Constant.MESSAGE_REDIS, map, compare);
        setMsgVersion(message.getCode());
    }

    @Override
    public List getGroupChildrens(List groupChannels, List channelsJson) {
        List list = new ArrayList();
        if (ListUtils.listIsExists(groupChannels)) {


            groupChannels.stream().forEach(str -> {
                findMsgChannelsByGroup((String) str).stream().forEach(p -> {
                    Map map = new HashMap();
                    map.put(Constant.REDIS_KEY, p);
                    list.add(map);
                    map = null;
                });
            });
        }
        if (!ListUtils.listIsExists(channelsJson)) {
            return list;
        }

        channelsJson.stream().forEach(p -> {
            Map map = new HashMap();
            map.put(Constant.REDIS_KEY, p);
            list.add(map);
            map = null;
        });
        /*for (int i = 0; i < channelsJson.size(); i++) {
            map = new HashMap();
            map.put(Constant.REDIS_KEY, channelsJson.get(i));
            list.add(map);
        }*/

        return list;
    }

    @Override
    public MsgMessage findMsgByMsgIdWithNoDr(String msgId) {
        if (!redisMsgService.hasKey(msgId + Constant.MESSAGE_REDIS)) {
            return null;
        }

        MsgMessage message = new MsgMessage();
        Map map = redisMsgService.getCacheMap(msgId + Constant.MESSAGE_REDIS);
        try {
            if (null != map.get("sendTime")) {
                if (map.get("sendTime") instanceof Date) {
                    message.setSendTime((Date) map.get("sendTime"));
                } else if (map.get("sendTime") instanceof String) {
                    message.setSendTime(DateUtils.stringToDate((String) map.get("sendTime"), DateUtils.DATE_FORMAT));
                }
            } else if (null != map.get("sendTimeStr")) {
                message.setSendTime(DateUtils.stringToDate((String) map.get("sendTimeStr"), DateUtils.DATE_FORMAT));
            } else {
                throw new Exception();
            }
            //message.setSendTime(DateUtils.stringToDate((String)map.get("sendTime"),DateUtils.DATE_FORMAT));
            //message.setSendTime(DateUtils.stringToDate((String)map.get("sendTimeStr"),DateUtils.DATE_FORMAT));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            logger.error(StringUtils.objToJsonStr(map), e);
        }

        message.setMessage((String) map.get("msg"));
        message.setRecBy((String) map.get("rec"));
        message.setTitle((String) map.get("title"));
        message.setContent((String) map.get("content"));
        message.setSendStatus((Integer) map.get("sendStatus"));
        message.setSendType((Integer) map.get("sendType"));
        message.setCode(msgId);
        String sendTimeStr = "";
        if (null == map.get("sendTimeStr")) {
            sendTimeStr = (String) map.get("sendTime");
        } else {
            sendTimeStr = (String) map.get("sendTimeStr");
        }

        message.setSendTimeStr(sendTimeStr);
        message.setDr(null == map.get("dr") ? 0 : Integer.valueOf((Integer) map.get("dr")));
        message.setMsgType(null == map.get("msgType") ? Constant.MSG_TYPE_GENERALLY : Integer.valueOf((Integer) map.get("msgType")));
        message.setMsgSort(null == map.get("msgSort") ? Constant.MSG_SORT_NOT : Integer.valueOf((Integer) map.get("msgSort")));

        return message;
    }

    /*@Override
    public MsgMessage findMsgByMsgId(String msgId)  {

        MsgMessage message = findMsgByMsgIdWithNoDr(msgId);
        if (null!=message && message.getDr()==1) {
            return null;
        }
        return message;
    }*/

    @Override
    public List findMsgChannelsByGroup(String groupChannel) {
        if (isExistsGroup(groupChannel)) {
            return redisMsgService.getCacheZSet(groupChannel + Constant.CHANNEL_REDIS).stream().collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public List findMsgChannelsByMsg(String msgId) {
        String key = msgId + Constant.MSG_USER_REDIS + Constant.CHANNEL_REDIS;
        if (hasKey(key)) {
            return redisMsgService.getCacheZSet(key).stream().collect(Collectors.toList());
        }
        return Arrays.asList();
    }

    @Override
    public boolean isExistsGroup(String groupChannel) {
        if (redisMsgService.hasKey(groupChannel + Constant.CHANNEL_REDIS)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isExistsByGroupAndLeaguer(String groupChannel, String leaguerChannel) {
        return false;
    }

    @Override
    public int saveChannel(MsgChannels msgChannels) {
        redisMsgService.setCacheZSet(msgChannels.getGroupChannel() + Constant.CHANNEL_REDIS, msgChannels.getLeaguerChannel(), DateUtils.getNowDate().getTime());
        setChannelVersion(msgChannels.getGroupChannel());
        return 1;
    }

    public int saveChannelsByGroup(String group, List msgChannels) {
        List list = new ArrayList();
        msgChannels.stream().forEach(p -> {
            Map map = new HashMap();
            map.put(Constant.REDIS_KEY, group + Constant.CHANNEL_REDIS);
            map.put("value", (String) p);
            map.put("score", DateUtils.getNowDate().getTime());
            list.add(map);
            map = null;
        });
        redisMsgService.pipeline(group + Constant.CHANNEL_REDIS, list);
        setChannelVersion(group);
        return 1;
    }

    @Override
    public int updateMsgGroupInfoByChannel(List userIds, String channel) {
        List addList = new ArrayList();
        List delList = new ArrayList();
        List channels = new ArrayList();
        if (ListUtils.listIsExists(userIds)) {
            userIds.stream().forEach(p -> {

                Map map = (Map) p;
                String userId = (String) map.get("user_id");
                Integer type = ((Double) map.get("type")).intValue();

                if (type == Constant.GROUP_ADD) {
                    Map result = new HashMap();
                    result.put(Constant.REDIS_KEY, channel + Constant.CHANNEL_REDIS);
                    result.put("value", userId);
                    result.put("score", DateUtils.getNowDate().getTime());
                    addList.add(result);

                    result = null;
                }
                if (type == Constant.GROUP_DEL) {
                    //批量删除成员频道
                    Map result = new HashMap();
                    result.put(Constant.REDIS_KEY, channel + Constant.CHANNEL_REDIS);
                    result.put("value", userId);
                    delList.add(result);
                    result = null;
                }
                Map items = new HashMap();
                Map item = new HashMap();
                Map result = new HashMap();
                item.put("type", type);
                item.put("channel", channel);
                items.put("user_id", userId);
                items.put("channels", item);
                result.put(Constant.REDIS_KEY, "groups_" + Constant.CHANNEL_REDIS);
                result.put("value", StringUtils.objToJsonStr(items));
                result.put("score", DateUtils.getNowDate().getTime());
                channels.add(result);

            });
            redisMsgService.pipeline(channel + Constant.CHANNEL_REDIS, addList);
            redisMsgService.remove(channel + Constant.CHANNEL_REDIS, channel, delList);
            sendAdapterService.setMsgGroupInfoToRedis(channel, channels);
            //redisMsgService.pipeline(channel+Constant.CHANNEL_REDIS,channels);
            return userIds.size();
        }
        return 0;
    }

    @Override
    public int updateMsgGroupInfo(String userId, List groups) {
        List addList = new ArrayList();
        List delList = new ArrayList();
        List channels = new ArrayList();
        if (ListUtils.listIsExists(groups)) {
            groups.stream().forEach(p -> {

                Map map = (Map) p;
                String channel = (String) map.get("channel");
                Integer type = ((Double) map.get("type")).intValue();

                if (type == Constant.GROUP_ADD) {
                    Map result = new HashMap();
                    result.put(Constant.REDIS_KEY, channel + Constant.CHANNEL_REDIS);
                    result.put("value", userId);
                    result.put("score", DateUtils.getNowDate().getTime());
                    addList.add(result);

                    result = null;
                }
                if (type == Constant.GROUP_DEL) {
                    //批量删除成员频道
                    Map result = new HashMap();
                    result.put(Constant.REDIS_KEY, channel + Constant.CHANNEL_REDIS);
                    result.put("value", userId);
                    delList.add(result);
                    result = null;
                }
                channels.add(channel);

            });
            redisMsgService.pipeline(userId + Constant.CHANNEL_REDIS, addList);
            redisMsgService.remove(userId + Constant.CHANNEL_REDIS, userId, delList);
            /*addList = null;
            delList = null;*/
            //channels.stream().forEach(p->{setChannelVersion((String)p);});
            Map map = new HashMap();
            map.put("user_id", userId);
            map.put("channels", groups);
            //String uuid = StringUtils.getUUID();
            //Map map1 = new HashMap();
            //map1.put(uuid,StringUtils.objToJsonStr(map));
            redisMsgService.setCacheZSet("groups_" + Constant.CHANNEL_REDIS, StringUtils.objToJsonStr(map), DateUtils.getNowDate().getTime());
            //redisMsgService.setCacheMap("groups_map_"+Constant.CHANNEL_REDIS,map1,-1);
            //redisMsgService.setCacheZSet("groups_set_"+Constant.CHANNEL_REDIS,uuid,DateUtils.getNowDate().getTime());
            return groups.size();
        }
        return 0;
    }

    @Override
    public int saveGroup(MsgGroup msgGroup) {
        redisMsgService.setCacheZSet(msgGroup.getChannel() + Constant.CHANNEL_REDIS, msgGroup.getChannel(), DateUtils.getNowDate().getTime());
        setChannelVersion(msgGroup.getChannel());

        return 1;
    }

    @Override
    public int updateMsg(MsgMessage message) {
        /*Date now = DateUtils.getNowDate();
        Date after = DateUtils.addDays(now,Constant.MSG_SAVE_DAYS);
        int compare = DateUtils.compare(now,after);*/
        //  这里的时间应该先为-1
        int compare = -1;
        Map map = new HashMap();
        map.put("sendTimeStr", message.getSendTimeStr());
        map.put("sendTime", message.getSendTime());
        map.put("msg", message.getMessage());
        map.put("rec", message.getRecBy());
        map.put("title", message.getTitle());
        map.put("content", message.getContent());
        map.put("sendStatus", message.getSendStatus());
        map.put("sendType", message.getSendType());
        map.put("dr", null == message.getDr() ? 0 : message.getDr());
        map.put("msgSort", null == message.getMsgSort() ? Constant.MSG_SORT_NOT : message.getMsgSort());
        //System.out.println(StringUtils.objToJsonStr(message));
        redisMsgService.setCacheMap(message.getCode() + Constant.MESSAGE_REDIS, map, compare);
        setMsgVersion(message.getCode());
        return 1;
    }

    public Object getUpdateGroupInfo() {
        return redisMsgService.getCacheZSet("groups_" + Constant.CHANNEL_REDIS).stream().collect(Collectors.toList());
        //return redisMsgService.getCacheZSet("groups_set_"+Constant.CHANNEL_REDIS).stream().collect(Collectors.toList());
    }

    public Object getReadedMsg() {
        return redisMsgService.getCacheZSet("readed" + Constant.CHANNEL_REDIS).stream().collect(Collectors.toList());
        //return redisMsgService.getCacheZSet("groups_set_"+Constant.CHANNEL_REDIS).stream().collect(Collectors.toList());
    }

    @Override
    public List getCommonMsg(String channel, String msg, String startTime, String endTime) {
        /*return redisMsgService.getCacheZSet(channel+Constant.MESSAGE_REDIS,startTime,endTime).stream().filter(p->{
            if (!StringUtils.isEmpty(msg)) {
                return p.toString() == msg;
            }
            return true;

        }).collect(Collectors.toList());*/
        return getObjectFromRedisService.getConsumerMsgFromRedis(channel, startTime, endTime);

    }


    /*public Object getUpdateGroupInfo(List list){

        return list.stream().map(p->{
            String str = (String)p;
            Map map = redisMsgService.getCacheMap("groups_map_"+Constant.CHANNEL_REDIS);
            return map.get(str);
        }).collect(Collectors.toList());
    }*/

    @Override
    public void setReceivers(List channelsJson) {
        Map map = null;
        Map map1 = null;
        List groups = null;

        for (int i = 0; i < channelsJson.size(); i++) {
            String str = (String) channelsJson.get(i);
            redisMsgService.setCacheZSet(str + Constant.CHANNEL_REDIS, str, DateUtils.getNowDate().getTime());
            //setChannelVersion(str);
            map = new HashMap();
            map1 = new HashMap();
            groups = new ArrayList();

            map1.put("type", "add");
            map1.put("channel", str);
            groups.add(map1);
            map.put("user_id", str);
            map.put("channels", groups);
            //String uuid = StringUtils.getUUID();
            //Map map1 = new HashMap();
            //map1.put(uuid,StringUtils.objToJsonStr(map));
            redisMsgService.setCacheZSet("groups_" + Constant.CHANNEL_REDIS, StringUtils.objToJsonStr(map), DateUtils.getNowDate().getTime());


        }
    }

    private void setMsgVersion(String msgId) {
        Map map = redisMsgService.getCacheMap("msg" + Constant.MESSAGE_REDIS);
        if (null == map) {
            map = new HashMap();
        }
        double vlaue = null == map.get(msgId) ? 1 : (double) map.get(msgId);
        vlaue = vlaue + 0.1;
        map.put(msgId, vlaue);
        redisMsgService.setCacheMap("msg" + Constant.MESSAGE_REDIS, map, -1);
    }

    public double getMsgVersion(String msgId) {
        Map map = redisMsgService.getCacheMap("msg" + Constant.MESSAGE_REDIS);
        double value = null == map.get(msgId) ? 1 : (double) map.get(msgId);
        //Integer value = null==map.get(msgId)?0:Integer.valueOf((String) map.get(msgId));
        return value;
    }

    private void setChannelVersion(String groupChannel) {
       /* Map map = redisMsgService.getCacheMap("channel"+Constant.CHANNEL_REDIS);
        if (null == map) {
            map = new HashMap();
        }
        double vlaue = null==map.get(groupChannel)?1: (double) map.get(groupChannel);
        //Integer vlaue = null==map.get(groupChannel)?0:Integer.valueOf((String) map.get(groupChannel));
        vlaue = vlaue + 0.1;
        map.put(groupChannel,vlaue);
        redisMsgService.setCacheMap("channel"+Constant.CHANNEL_REDIS,map,-1);*/
    }

    public double getChannelVersion(String groupChannel) {
        /*Map map = redisMsgService.getCacheMap("channel"+Constant.CHANNEL_REDIS);
        double vlaue = null==map.get(groupChannel)?1: (double) map.get(groupChannel);
        //Integer vlaue = null==map.get(groupChannel)?0: (Integer) map.get(groupChannel);
        return vlaue;*/
        return 0;
    }

    public Map getAyncMsg() {
        return redisMsgService.getCacheMap("msg" + Constant.MESSAGE_REDIS);
    }

    public void saveRelationBetweenUserAndMsg(String msgId, List users) {
        Set set = (Set) users.stream().collect(Collectors.toSet());
        redisMsgService.setCacheSet(msgId + Constant.MSG_USER_REDIS + Constant.CHANNEL_REDIS, set);
    }

    public List getUsersByMsg(String msgId) {

        return redisMsgService.getCacheZSet(msgId + Constant.MSG_USER_REDIS + Constant.CHANNEL_REDIS).stream().collect(Collectors.toList());
    }

    public void removeMsgWithUser(String msgId, List list) {
        removeZset(msgId + Constant.MSG_USER_REDIS + Constant.CHANNEL_REDIS, list);

        //redisMsgService.removeCacheZSet(msgId+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS,list.toArray());
    }

    public void removeChannels(List zset) {
        removeZset("groups_" + Constant.CHANNEL_REDIS, zset);
        //redisMsgService.removeCacheZSet("groups_"+Constant.CHANNEL_REDIS,zset.toArray());
    }

    public void removeReadedMsg(List zset) {
        removeZset("readed" + Constant.CHANNEL_REDIS, zset);
    }

    public void removeZset(String key, List zset) {
        redisMsgService.removeCacheZSet(key, zset.toArray());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            //e.printStackTrace();
        }
    }

    public void removeHashMap(String key, String column) {
        redisMsgService.removeCacheMap(key, column);
    }

    public void setZset(String key, String value, long time) {
        redisMsgService.setCacheZSet(key, value, time);
    }

    public Object getZset(String key) {
        return redisMsgService.getCacheZSet(key).stream().collect(Collectors.toList());
    }

    public synchronized Set setsIntoCacheZSet(String key, List strs) {
        return redisMsgService.setsIntoCacheZSet(key, strs);
    }

    public void removeKey(String key) {
        redisMsgService.removeKey(key);
    }

    public Set setMsgChannel(MsgMessage msgMessage) {
        List resultlists = (List) ListUtils.strToListBySplit(msgMessage.getRecBy(), ",").stream().map(p -> {
            Set resultset = new HashSet<String>((List) getZset(p + Constant.CHANNEL_REDIS));
            return resultset;
        }).collect(Collectors.toList());
        Set set = new HashSet();
        for (int i = 0; i < resultlists.size(); i++) {
            Set resultlist = (Set) resultlists.get(i);
            set.addAll(resultlist);
        }
        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<ZSetOperations.TypedTuple<String>>();
        Iterator iterator = set.iterator();
        ZSetOperations.TypedTuple<String> tuple0 = null;
        int idx = 1;
        while (iterator.hasNext()) {
            if (idx % 200000 == 0) {
                redisMsgService.setCacheZSet(msgMessage.getCode() + Constant.MSG_USER_REDIS + Constant.CHANNEL_REDIS, tuples);
                tuples.clear();
            } else {
                tuple0 = new DefaultTypedTuple<String>(iterator.next().toString(), Double.valueOf(DateUtils.getNowDate().getTime()));
                tuples.add(tuple0);
            }
            idx++;
        }
        redisMsgService.setCacheZSet(msgMessage.getCode() + Constant.MSG_USER_REDIS + Constant.CHANNEL_REDIS, tuples);
        return set;
    }

}
