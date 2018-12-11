package com.hqjy.msg.provide.operation;

import com.hqjy.msg.enumeration.PushMsgTypeEnum;
import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.interfaces.Aggregator;
import com.hqjy.msg.interfaces.impl.CountAggregator;
import com.hqjy.msg.model.MsgChannels;
import com.hqjy.msg.model.MsgGroup;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.Objects;
import com.hqjy.msg.model.PushMsgBase;
import com.hqjy.msg.provide.*;
import com.hqjy.msg.provide.interfaces.MessageTypeInterface;
import com.hqjy.msg.service.impl.XingePushServiceImpl;
import com.hqjy.msg.util.*;
import com.hqjy.msg.utils.RunUtil;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by baobao on 2017/12/25 0025.
 */

@Service
public class MsgManagerServiceImpl implements MsgManagerService {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private SendAdapterService sendAdapterService;
    @Autowired
    private RemoveObjectToRedisService removeObjectToRedisService;

    @Autowired
    private GetObjectFromRedisService getObjectFromRedisService;

    @Value("${common.group}")
    private String groupChannel;

    @Value("${xinge.enable}")
    private boolean xingeEnable;

    @Autowired
    private XingePushServiceImpl xingePushService;

    @Autowired
    private MessageTypeService messageTypeService;

    @Autowired
    private RedisMsgService<Objects> redisMsgService;

    private MessageTypeInterface common;
    private MessageTypeInterface generally;

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;
    @Autowired
    @Qualifier("mysql")
    private MessageRunService messageMysqlRunService;

    @Autowired
    public void setCommon() {
        this.common = (channelsJson, message, groupChannels, messageRunService) -> send(channelsJson, message, groupChannels, common, messageRunService);
    }

    @Autowired
    public void setGenerally() {
        this.generally = (channelsJson, message, groupChannels, messageRunService) -> send(channelsJson, message, groupChannels, generally, messageRunService);
    }

    public String send(List channelsJson, MsgMessage message, List groupChannels, MessageTypeInterface messageTypeInterface, MessageRunService messageRunService) throws Exception {

        Date nowDate = DateUtils.getNowDate();
        //1.生成唯一的消息代码
        String msgId = StringUtils.getUnquie();
        String sendTime = setPushTime(message.getSendTimeStr(), nowDate);
        int type = setPushType(sendTime, nowDate);
        //操作群组信息
        //String result =   msgGroupService.checkGroup(groupChannels);
        List exists = null;
        if (!messageTypeInterface.equals(common)) {
            exists = messageRunService.checkGroup(groupChannels);
        }


        String rec = setReceivers(channelsJson, exists, messageRunService);
        //
        message.setCode(msgId);
        message.setSendType(type);
        message.setSendStatus(Constant.MSG_SEND_STATUS_NOT);
        message.setRecBy(rec);
        message.setSendTimeStr(sendTime);

        push(groupChannels, channelsJson, message, nowDate, messageTypeInterface, messageRunService);
        //sendAdapterService.setSendMsgToRedis(groupChannels,channelsJson,message,nowDate,messageTypeInterface,messageRunService);
        //3.推送
        /*ThreadPoolExecutorUtils.getThreadPoolExecutor().execute(() ->{
            push(groupChannels, channelsJson, message, nowDate, messageTypeInterface,messageRunService);
        });*/

        return msgId;

    }

    public String sendMsg(List channelsJson, MsgMessage message, List groupChannels) throws Exception {

        return this.messageTypeService.send(channelsJson, message, groupChannels, generally, messageRunService);

    }

    public String sendCommonMsg(List channelsJson, MsgMessage message, List groupChannels) throws Exception {
        return this.messageTypeService.send(channelsJson, message, groupChannels, common, messageRunService);
    }


    /**
     * 推送消息
     *
     * @param groupChannels
     * @param jsonChannels
     */
    public void push(List groupChannels, List jsonChannels, MsgMessage message, Date nowDate, MessageTypeInterface messageTypeInterface, MessageRunService messageRunService) {
        String msg = message.getMessage();
        message.setMessage("");
        if (message.getSendType() == Constant.MSG_SEND_TYPE_TIME) {
            //持久化消息到消息表
            message.setSendStatus(Constant.MSG_SEND_STATUS_NOT);
            //msgMessageService.insertMsgMessage(message);
            //messageRunService.saveMsg(message);
            //缓存edis）消息体（R
            //sendAdapterService.setMsgToRedis(message, msg);
            sendAdapterService.setWaitMsg(message.getCode(), message.getSendTimeStr(), nowDate, message, msg);
            //sendAdapterService.setMsgDetailToRedis(message,msg);
            return;
        }
        message.setSendStatus(Constant.MSG_SEND_STATUS_ED);
        //msgMessageService.insertMsgMessage(message);
        //messageRunService.saveMsg(message);
        //缓存消息体（Redis）
        //sendAdapterService.setMsgToRedis(message, msg);

        //ThreadPoolExecutorUtils.getDefaultThreadPoolExecutor().execute(()->{
        List leaguers = null;
        if (messageTypeInterface.equals(common)) {
            leaguers = new ArrayList();
            Map map = new HashMap();
            map.put(Constant.REDIS_KEY, message.getRecBy());
            leaguers.add(map);
        } else {
            //leaguers =msgGroupService.getGroupChildrens(groupChannels,jsonChannels);
            //leaguers =messageRunService.getGroupChildrens(groupChannels,jsonChannels);
        }

        sendAdapterService.setSendMsgToRedis(leaguers, message, msg);
        //});


    }

    public void runing(List channelsJson, MsgMessage message) {
        if (channelsJson.isEmpty() || null == channelsJson) return;
        //群发消息
        List recList = ListUtils.strToList(message.getRecBy());
        PushMsgBase pushMsgBase = new PushMsgBase();
        pushMsgBase.setTitle(message.getTitle());
        pushMsgBase.setContent(message.getContent());
        pushMsgBase.setMsgId(message.getCode());

        //  判断发送类型是 generally 还是find
        if (message.getMsgType() == 0) {
            pushMsgBase.setModuleType(4);
        } else if (message.getMsgType() == 2) {
            pushMsgBase.setModuleType(5);
        }

        if (xingeEnable) {
            xingePushService.pushTagAndroidOrIos(recList, pushMsgBase);
        }
        /*messageRunService.saveRelationBetweenUserAndMsg(message.getCode(),(List)channelsJson.stream().map(p->{
            return ((Map)p).get(Constant.REDIS_KEY);
        }).collect(Collectors.toList()));*/
        //缓存消息到消费者的列表（Redis）
        sendAdapterService.setConsumerMsgToRedis(channelsJson, message.getCode(), message.getSendTimeStr());


    }

    /**
     * 按时推送的消息执行操作
     *
     * @param msgId 消息ID
     * @return 是否存在消息ID（存在1，不存在-1）
     */
    @Override
    public int runByTime(String msgId) {
        //MsgMessage message = msgMessageService.findMsgMessageByCode(msgId);
        MsgMessage message = messageRunService.findMsgByMsgId(msgId);
        if (null == message)
            return -1;
        //List channelsJson = msgChannelsService.findMsgChannelsByGroupJdbc(message.getRecBy());
        List channelsJson = messageRunService.findMsgChannelsByGroup(message.getRecBy());
        //List channelsJson = messageRunService.findMsgChannelsByMsg(msgId);
        message.setSendStatus(Constant.MSG_SEND_STATUS_ED);

        //  设置过期时间

        //  这里的时间是分钟   14 * （24 * 60）天
        redisMsgService.setExpire(msgId+Constant.MESSAGE_REDIS,Constant.MSG_SAVE_DAYS*24*60);

        //msgMessageService.update(message);
        messageRunService.updateMsg(message);
        message.setSendTimeStr(DateUtils.dateToString(message.getSendTime()));
        runing((List) channelsJson.stream().map(p -> {
            Map map = new HashMap();
            map.put(Constant.REDIS_KEY, (String) p);

            // 发完之后再设置过期时间
            redisMsgService.setExpire(Constant.REDIS_KEY, Constant.MSG_SAVE_DAYS);

            return map;
        }).collect(Collectors.toList()), message);
        return 1;
    }

    @Override
    public int updateMsg(String msgId, String sendTime, String msg, List groups, List channelsJson, Integer msgSort) throws DefaultException {

        //
        Date nowDate = DateUtils.getNowDate();
        //MsgMessage msgMessage = msgMessageService.findMsgMessageByCode(msgId);
        MsgMessage msgMessage = messageRunService.findMsgByMsgId(msgId);
        if (msgMessage == null) {
            throw new DefaultException("消息不存在");
        }
        Date sendDate;
        if (null != msgMessage.getSendTime()) {
            sendDate = msgMessage.getSendTime();
        }else if(null != msgMessage.getSendTimeStr()){
            sendDate = DateUtils.stringToDate(msgMessage.getSendTimeStr(),DateUtils.DATE_FORMAT);
        }else{
            throw new DefaultException("发送时间为空");
        }

        int compareTime = DateUtils.compare(nowDate, sendDate);

        if (compareTime < 0) {
            throw new DefaultException("消息已发送,不能修改");
        }
        if (compareTime >= 3 * Constant.COMPARE_MINITUS) {
            if (null != sendTime && DateUtils.isValidDate(sendTime)) {

                try {
                    sendDate = DateUtils.stringToDate(sendTime, DateUtils.DATE_FORMAT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int compareTime1 = DateUtils.compare(nowDate, sendDate);

                if (compareTime1 <= 0) {
                    throw new DefaultException("新的发送时间小于当前时间，更新失败");
                }

                if (/*sendDate.getTime()!=msgMessage.getSendTime().getTime() &&*/ compareTime1 >= 2 * Constant.COMPARE_MINITUS) {
                    msgMessage.setSendTime(sendDate);
                    msgMessage.setSendTimeStr(sendTime);
                    this.sendAdapterService.setMsgExpireToRedis(msgId, compareTime1);
                } else {
                    throw new DefaultException("新的发送时间距离发送时间很近，更新失败");
                }


            }
            if (null != msg && !msg.trim().equals("")) {
                msgMessage.setMessage(msg);
            }
            if (ListUtils.listIsExists(groups)) {
                //msgGroupService.checkGroup(groups);
                messageRunService.checkGroup(groups);

            }
            if (null != groups || null != channelsJson) {
                msgMessage.setRecBy(setReceivers(channelsJson, groups, messageRunService));
            }
            //保存数据
            /*ThreadPoolExecutorUtils.getThreadPoolExecutor().execute(new Runnable() {
                @Override
                public void run() {*/
            //msgMessageService.update(msgMessage);
            messageRunService.updateMsg(msgMessage);
            msgMessage.setMessage("");
            sendAdapterService.setMsgDetailToRedis(msgMessage, msg);
            /*    }
            });*/
            return 1;
        }
        throw new DefaultException("修改时间距离发送时间很近，更新失败");

        //return 0;
    }

    @Override
    public int setMsgRecommend(String msgId, Integer msgSort) throws DefaultException {

        //MsgMessage msgMessage = msgMessageService.findMsgMessageByCode(msgId);
        MsgMessage msgMessage = messageRunService.findMsgByMsgId(msgId);
        if (msgMessage == null) {
            throw new DefaultException("消息不存在");
        }
        msgMessage.setMsgSort(msgSort);
        messageRunService.updateMsg(msgMessage);
        String msg = msgMessage.getMessage();
        msgMessage.setMessage("");
        sendAdapterService.setMsgDetailToRedis(msgMessage, msg);

        return 1;
    }

    @Override
    public int delMsg(String msgId) throws DefaultException {
        Date nowDate = DateUtils.getNowDate();
        //MsgMessage msgMessage = msgMessageService.findMsgMessageByCode(msgId);
        MsgMessage msgMessage = messageRunService.findMsgByMsgId(msgId);
        if (msgMessage == null) {
            throw new DefaultException("消息不存在");
        }

        if (msgMessage.getSendStatus() == Constant.MSG_SEND_STATUS_ED) {
            throw new DefaultException("已发送的消息，不能删除");
            /*removeMsg(msgId);
            return 1;*/
        }
        int compareTime = DateUtils.compare(nowDate, msgMessage.getSendTime());
        if (compareTime < 0) {
            throw new DefaultException("消息已发送,不能删除");
        }
        if (compareTime >= 5 * Constant.COMPARE_MINITUS) {
            msgMessage.setDr(1);
            this.sendAdapterService.delWaitMsgToRedis(msgId);

            //return this.msgMessageService.update(msgMessage);
            return this.messageRunService.updateMsg(msgMessage);
        }
        throw new DefaultException("删除时间距离发送时间很近，删除失败");

        //return 0;
    }

    /**
     * 设置消息接收人
     *
     * @param channelsJson
     * @param groupChannels
     * @return
     */
    private String setReceivers(List channelsJson, List groupChannels, MessageRunService messageRunService) {
        if (null == channelsJson || channelsJson.isEmpty()) {
            return ListUtils.listTostr(groupChannels);
        }
        messageRunService.setReceivers(channelsJson);

        return ListUtils.listTostr(ListUtils.twoListInto(groupChannels, channelsJson));
    }


    @Override
    public void removeMsg(String msgId) throws DefaultException {

        //MsgMessage message = msgMessageService.findMsgMessageByCode(msgId);
        MsgMessage message = messageMysqlRunService.findMsgByMsgId(msgId);
        if (null == message)
            return;
        List channelsJson = null;
        if (message.getRecBy().equals(groupChannel)) {
            channelsJson = new ArrayList();
            Map map = new HashMap();
            map.put(Constant.REDIS_KEY, groupChannel);
            channelsJson.add(map);
        } else {
            //channelsJson = msgChannelsService.findMsgChannelsByGroupJdbc(message.getRecBy());
            //channelsJson = messageRunService.findMsgChannelsByGroup(message.getRecBy());
            channelsJson = (List) messageRunService.findMsgChannelsByMsg(msgId).stream().map(p -> {
                Map map = new HashMap();
                map.put(Constant.REDIS_KEY, (String) p);
                return map;
            }).collect(Collectors.toList());
        }
        removeObjectToRedisService.removeMsgToRedis(msgId, channelsJson);

    }

    /**
     * 根据UserId获得消息
     *
     * @param startTime
     * @param endTime
     * @param userId
     * @return
     */
    @Override
    public List getMsgByUserIdGroup(String startTime, String endTime, String userId) {
        List list = getObjectFromRedisService.getConsumerMsgFromRedis(userId, startTime, endTime);
        List redisResults = null;
        List resultlist = new ArrayList();
        try {
            redisResults = (List) list.stream().map(p -> {
                String msgId = (String) p;
                Integer isReaded = 0;
                if (msgId.startsWith(Constant.READED_START_KEY)) {
                    msgId = msgId.split(Constant.READED_START_KEY)[1];
                    isReaded = 1;
                }
                MsgMessage msg = messageRunService.findMsgByMsgId(msgId);

                if (null != msg) {
                    msg.setIsReaded(isReaded);
                    msg.setMsgType(null == msg.getMsgType() ? Constant.MSG_TYPE_GENERALLY : msg.getMsgType());
                    if (msg.getSendTime() != null) {
                        msg.setSendTimeStr(DateUtils.dateToString(msg.getSendTime(), "yyyy-MM-dd"));
                    }
                    // 修复sendtime为空的时候，sendTimeStr没有转换格式的bug
                    else {
                        Date date = null;
                        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                        try {
                            date = format1.parse(msg.getSendTimeStr());
                            if (date != null) {
                                msg.setSendTimeStr(DateUtils.dateToString(date, "yyyy-MM-dd"));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    return msg;
                }

                return null;

            }).collect(Collectors.toList());
            redisResults = (List) redisResults.stream().filter(p -> {
                MsgMessage msg = (MsgMessage) p;
                return null != msg && (msg.getMsgType() == Constant.MSG_TYPE_GENERALLY || null == msg.getMsgType());
            }).collect(Collectors.toList());

            Map<String, Aggregator<MsgMessage>> aggregatorMap = new HashMap<>();
            aggregatorMap.put("count", new CountAggregator<MsgMessage>());
            Map<Object, Map<String, Object>> aggResults = MapUtils.sortMapByKey(GroupUtils.groupByProperty(redisResults, MsgMessage.class, "sendTimeStr", aggregatorMap));

            Map resultmap = new HashMap();

            for (Object key : aggResults.keySet()) {
                resultmap = new HashMap();
                Map<String, Object> results = aggResults.get(key);
                resultmap.put("date", key);
                //System.out.println("Key:" + key+"->"+results.keySet().size());
                for (String aggKey : results.keySet()) {
                    //System.out.println("     aggkey->" + results.get(aggKey));
                    resultmap.put("count", results.get(aggKey));
                }
                resultlist.add(resultmap);

            }
//            System.out.print(StringUtils.objToJsonStr(resultlist));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultlist;

    }


    /**
     * 根据UserId获得消息
     *
     * @param startTime
     * @param endTime
     * @param userId
     * @return
     */
    @Override
    public List getMsgByUserId(String startTime, String endTime, String userId, int type, int msgType) {
        List list = getObjectFromRedisService.getConsumerMsgFromRedis(userId, startTime, endTime);
        List redisResults = null;
        try {
            redisResults = (List) list.stream().map(p -> {
                String msgId = (String) p;
                Integer isReaded = 0;
                if (msgId.startsWith(Constant.READED_START_KEY)) {
                    msgId = msgId.split(Constant.READED_START_KEY)[1];
                    isReaded = 1;
                }
                MsgMessage msg = messageRunService.findMsgByMsgId(msgId);

                if (null != msg) {
                    msg.setIsReaded(isReaded);
                    msg.setMsgType(null == msg.getMsgType() ? Constant.MSG_TYPE_GENERALLY : msg.getMsgType());
                    return msg;
                }

                return null;
            }).collect(Collectors.toList());
            redisResults = (List) redisResults.stream().filter(p -> {
                MsgMessage msg = (MsgMessage) p;
                return null != msg && (msg.getMsgType() == msgType || null == msg.getMsgType());
            }).collect(Collectors.toList());
            if (type == Constant.MSG_STATUS_NOT_READ) {
                redisResults = (List) redisResults.stream().filter(p -> {
                    MsgMessage msg = (MsgMessage) p;
                    return msg.getIsReaded() == Constant.MSG_STATUS_NOT_READ;
                }).collect(Collectors.toList());
            }
            if (type == Constant.MSG_STATUS_READED) {
                redisResults = (List) redisResults.stream().filter(p -> {
                    MsgMessage msg = (MsgMessage) p;
                    return msg.getIsReaded() == Constant.MSG_STATUS_READED;
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
        }
        /*Comparator<MsgMessage> comparator = new Comparator<MsgMessage>() {
            @Override
            public int compare(MsgMessage x, MsgMessage y) {
                if(x.getMsgSort()==y.getMsgSort()) {
                    return y.getSendTime().compareTo(x.getSendTime());
                }
                return  ((Integer) y.getMsgSort()).compareTo(x.getMsgSort());
            }
        };*/
       /* MsgMessage [] arrays = (MsgMessage[]) redisResults.toArray(new MsgMessage[redisResults.size()]);
        Arrays.sort(arrays,comparator*//*(MsgMessage x,MsgMessage y)->{
            if(x.getMsgSort()==y.getMsgSort()) {
                return y.getSendTime().compareTo(x.getSendTime());
            }
            return  ((Integer) y.getMsgSort()).compareTo(x.getMsgSort());
        }*//*);*/
        // redisResults.sort(comparator);
        if (ListUtils.listIsExists(redisResults)) {
            return (List) redisResults.stream().map(p -> {
                MsgMessage msg = (MsgMessage) p;
                Map map = new HashMap();
                map.put("is_readed", msg.getIsReaded());
                map.put("msg_id", msg.getCode());
                map.put("msg", msg.getMessage());
                return map;
            }).collect(Collectors.toList());
        }
        return new ArrayList();

        /*Map map = new HashMap();
        if (ListUtils.listIsExists(redisResults) && redisResults.size()>=size) {
            return (List) ListUtils.listsToArray(redisResults, size).get(page);
        }*/

        /*if (null!=list && !list.isEmpty()){
            if(type == Constant.MSG_STATUS_ALL){
                for (int i = 0; i < list.size(); i++) {
                    String msgId = (String) list.get(i);
                    map = new HashMap();

                    if (msgId.startsWith(Constant.READED_START_KEY)){
                        map.put("is_readed",1);
                        map.put("msg",getObjectFromRedisService.getMsgFromRedis(msgId.split(Constant.READED_START_KEY)[1]));
                        map.put("msg_id",msgId);
                        result.add(map);
                        //result.add(getObjectFromRedisService.getMsgFromRedis(msgId.split(Constant.READED_START_KEY)[0]));
                    }else{
                        map.put("is_readed",0);
                        map.put("msg_id",msgId);
                        map.put("msg",getObjectFromRedisService.getMsgFromRedis(msgId));
                        result.add(map);
                    }

                }
            }}
        if(type == Constant.MSG_STATUS_NOT_READ){
            for (int i = 0; i < list.size(); i++) {
                String msgId = (String) list.get(i);
                if (msgId.startsWith(Constant.READED_START_KEY)){

                    //result.add(getObjectFromRedisService.getMsgFromRedis(msgId.split(Constant.READED_START_KEY)[0]));
                }else{
                    map = new HashMap();
                    map.put("msg_id",msgId);
                    map.put("msg",getObjectFromRedisService.getMsgFromRedis(msgId));
                    result.add(map);
                    //result.add(getObjectFromRedisService.getMsgFromRedis(msgId));
                }

            }
        }

        if(type == Constant.MSG_STATUS_READED){
            for (int i = 0; i < list.size(); i++) {
                String msgId = (String) list.get(i);
                if (msgId.startsWith(Constant.READED_START_KEY)){
                    map = new HashMap();
                    map.put("msg_id",msgId);
                    map.put("msg",getObjectFromRedisService.getMsgFromRedis(msgId.split(Constant.READED_START_KEY)[0]));
                    result.add(map);
                    //result.add(getObjectFromRedisService.getMsgFromRedis(msgId.split(Constant.READED_START_KEY)[0]));
                }else{

                    //result.add(getObjectFromRedisService.getMsgFromRedis(msgId));
                }

            }
        }*/


        //return redisResults;
    }


    /**
     * 根据UserId获得消息(发现模块)
     *
     * @param startTime
     * @param endTime
     * @param userId
     * @param strcomparator
     * @param strpredicate
     * @param pageSize
     * @param pageNum
     * @return
     */
    public PageUtils getMsgByUserIdFind(String startTime, String endTime, String userId, String strcomparator, String strpredicate, int pageSize, int pageNum) {
        List list = getObjectFromRedisService.getConsumerMsgFromRedis(userId, startTime, endTime);
        List redisResults = null;
        PageUtils pageUtils = new PageUtils();
        try {
            /*redisResults = (List) list.stream().map(p->{
                String msgId = (String) p;
                Integer isReaded = 0;
                if (msgId.startsWith(Constant.READED_START_KEY)){
                    msgId = msgId.split(Constant.READED_START_KEY)[1];
                    isReaded = 1;
                }
                MsgMessage msg = messageRunService.findMsgByMsgId(msgId);
                //msg.getCode()
                if (null != msg) {
                    msg.setIsReaded(isReaded);
                    msg.setMsgType(null==msg.getMsgType()?PushMsgTypeEnum.generally.getValue():msg.getMsgType());
                    return msg;
                }

                return null;

            }).collect(Collectors.toList());
            redisResults = (List) redisResults.stream().filter(p->{
                MsgMessage msg = (MsgMessage)p;
                return null!=msg && msg.getMsgType()==PushMsgTypeEnum.find.getValue();
            }).collect(Collectors.toList());
            if (!StringUtils.isEmpty(strpredicate)) {
                Predicate predicate = runUtil.getPredicate(strpredicate);
                //System.out.println("test:"+StringUtils.objToJsonStr(predicate));
                redisResults = DataSortAndFilterUtils.filter(redisResults,predicate);
            }

            if (!StringUtils.isEmpty(strcomparator)) {
                Comparator comparator = runUtil.getComparator(strcomparator);
                redisResults = DataSortAndFilterUtils.sort(redisResults,comparator);
            }*/
            redisResults = (List) list.stream().map(p -> {
                String msgId = (String) p;
                Integer isReaded = 0;
                if (msgId.startsWith(Constant.READED_START_KEY)) {
                    msgId = msgId.split(Constant.READED_START_KEY)[1];
                    isReaded = 1;
                }
                MsgMessage msg = messageRunService.findMsgByMsgId(msgId);
                //msg.getCode()
                Map map = new ConcurrentHashMap();
                if (null != msg) {
                    msg.setIsReaded(isReaded);
                    msg.setMsgType(null == msg.getMsgType() ? PushMsgTypeEnum.generally.getValue() : msg.getMsgType());
                    map.put("is_readed", msg.getIsReaded());
                    map.put("msg_id", msg.getCode());
                    map.put("msg", msg.getMessage());
                    map.put("msg_type", msg.getMsgType());
                    return map;
                }

                return null;

            }).collect(Collectors.toList());
            redisResults = (List) redisResults.stream().filter(p -> {
                Map msg = (ConcurrentHashMap) p;
                //ConcurrentHashMap concurrentHashMap = (ConcurrentHashMap) msg;
                return null != msg && (Integer) msg.get("msg_type") == PushMsgTypeEnum.find.getValue();
            }).collect(Collectors.toList());
            if (!StringUtils.isEmpty(strpredicate)) {
                Predicate predicate = RunUtil.getPredicate(strpredicate);
                //System.out.println("test:"+StringUtils.objToJsonStr(predicate));
                redisResults = DataSortAndFilterUtils.filter(redisResults, predicate);
            }

            if (!StringUtils.isEmpty(strcomparator)) {
                Comparator comparator = RunUtil.getComparator(strcomparator);
                redisResults = DataSortAndFilterUtils.sort(redisResults, comparator);
            }


            redisResults = (List) redisResults.stream().map(p -> {
                /*MsgMessage msg = (MsgMessage) p;
                Map map = new HashMap();
                map.put("is_readed",msg.getIsReaded());
                map.put("msg_id",msg.getCode());
                map.put("msg",msg.getMessage());*/
                Map map = (Map) p;
                map.remove("msg_type");
                return map;
            }).collect(Collectors.toList());

            //
            pageUtils = DataSortAndFilterUtils.page(redisResults, pageSize, pageNum);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageUtils;
    }

    @Override
    public List getMsgIdsByUserId(String startTime, String endTime, String userId, int msgStatusAll, Integer msgTypeGenerally) {
        List msgIds = getObjectFromRedisService.getConsumerMsgFromRedis(userId, startTime, endTime);

        return msgIds;
    }

    @Override
    public List getCommonMsg(String startTime, String endTime, int type) {
//        String userId = groupChannel;
//        return (List) getMsgByUserId(startTime,endTime,userId,type).stream().filter(p->{
//            List result = messageRunService.getCommonMsg(groupChannel, (String) ((Map) p).get("msg_id"));
//            return !ListUtils.listIsExists(result) && result.size()==1;
//        }).collect(Collectors.toList());
        List list = messageRunService.getCommonMsg(groupChannel, "", startTime, endTime);
        List redisResults = (List) list.stream().map(p -> {
            String msgId = (String) p;
            MsgMessage msg = messageRunService.findMsgByMsgId(msgId);
            return msg;

        }).collect(Collectors.toList());
        return (List) redisResults.stream().map(p -> {
            MsgMessage msg = (MsgMessage) p;
            Map map = new HashMap();
            map.put("msg_id", msg.getCode());
            map.put("msg", msg.getMessage());
            return map;
        }).collect(Collectors.toList());

    }

    /**
     * @param msgIds
     * @param userId
     */
    @Override
    public void setMsgReadedByUserId(List msgIds, String userId) {
        this.sendAdapterService.setReadedConsumerMsgToRedis(msgIds, userId);

    }


    /**
     * 设置发送时间
     *
     * @param sendTime
     * @param nowDate
     * @return 发送类型（0:立即 1:按时）
     */
    private String setPushTime(String sendTime, Date nowDate) throws Exception {
        //当发送时间为空时，取当前的时间
        if (StringUtils.isEmpty(sendTime)) return DateUtils.dateToString(nowDate);
        Date sendDate = DateUtils.stringToDate(sendTime, DateUtils.DATE_FORMAT);
        if (DateUtils.compare(sendDate, nowDate) > 0) throw new DefaultException("发送时间不为小于当前时间");
        return sendTime;


    }

    /**
     * 设置发送时间
     *
     * @param sendTime
     * @param nowDate
     * @return 发送类型（0:立即 1:按时）
     */
    private int setPushType(String sendTime, Date nowDate) {

        if (StringUtils.isEmpty(sendTime)) {
            //当发送时间为空时，取当前的时间
            sendTime = DateUtils.dateToString(nowDate);
        } else {
            //不为空时，对比发送时间
            Date sendDate = DateUtils.stringToDate(sendTime, DateUtils.DATE_FORMAT);
            int compareTime = DateUtils.compare(nowDate, sendDate);
            if (compareTime >= Constant.COMPARE_MINITUS) {
                //按时推送
                return Constant.MSG_SEND_TYPE_TIME;
            }

        }
        return Constant.MSG_SEND_TYPE_NOW;
    }


    /**
     * 获取未读消息的条数
     *
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public int getNotReadMessagesCountByUserId(String userId, String startTime, String endTime) {
        List list = getMsgByUserId(startTime, endTime, userId, Constant.MSG_STATUS_NOT_READ, Constant.MSG_TYPE_GENERALLY);
        return list.size();
    }

    @Override
    public int updateMsgGroupInfo(String userId, List groups) {
        sendAdapterService.setUpdateMsgGroupInfo(userId, groups);
        //return messageRunService.updateMsgGroupInfo(userId,groups);
        return groups.size();
    }

    @Override
    public int updateMsgGroupInfoByChannel(List userIds, String channel) {

        return messageRunService.updateMsgGroupInfoByChannel(userIds, channel);

    }


}