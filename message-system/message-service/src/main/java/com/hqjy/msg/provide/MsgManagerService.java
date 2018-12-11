package com.hqjy.msg.provide;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.interfaces.MessageTypeInterface;
import com.hqjy.msg.util.PageUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by baobao on 2017/12/25 0025.
 * 消息的管理接口
 */
public interface MsgManagerService {

    /**
     * 更新消息
     * @param msgId
     * @param sendTime
     * @param msg
     * @param groups
     * @return
     */
    public int updateMsg(String msgId,String sendTime,String msg,List groups,List channelsJson,Integer msgSort) throws DefaultException;

    /**
     * 更新推荐状态
     * @param msgId
     * @param msgSort
     * @return
     */
    public int setMsgRecommend(String msgId, Integer msgSort) throws DefaultException;

    /**
     * 删除消息
     * @param msgId
     * @return
     */
    public int delMsg(String msgId) throws DefaultException;

    /**
     * 推送消息
     * @param groupChannels
     * @param jsonChannels
     */
    public void push(List groupChannels, List jsonChannels, MsgMessage message, Date nowDate, MessageTypeInterface messageTypeInterface, MessageRunService messageRunService)  ;


        /**
         * 发送公共消息
         * @param channelsJson
         * @param message
         * @param groupChannels
         * @return
         */
    public String sendCommonMsg(List channelsJson, MsgMessage message, List groupChannels) throws Exception;


    /**
     * 发送消息
     * @param channelsJson
     * @param message
     * @param groupChannels
     * @return
     */
    public String sendMsg(List channelsJson,MsgMessage message,List groupChannels) throws Exception;

    /**
     * 消息执行操作
     * @param channelsJson
     * @param message
     */
    public void runing(List channelsJson, MsgMessage message);

    /**
     * 按时推送的消息执行操作
     * @param msgId 消息ID
     */
    public  int runByTime(String msgId) ;

    /**
     * 消息过期操作
     * @param msgId 消息ID
     */
    public  void removeMsg(String msgId) throws DefaultException;



    /**
     * 根据用户ID获取消息列表
     * @param startTime
     * @param endTime
     * @param userId
     * @param type
     * @return
     */
    public List getMsgByUserId(String startTime,String endTime,String userId,int type,int msgType);

    /**
     * 根据用户ID获取消息列表
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    public List getCommonMsg(String startTime,String endTime,int type);


    /**
     * 设置用户消息为已读
     * @param msgId
     * @param userId
     */
    public void setMsgReadedByUserId(List msgIds,String userId);

    /**
     * 获取未读消息的条数
     *
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public int getNotReadMessagesCountByUserId(String userId, String startTime, String endTime);


    /**
     *	根据UserId获得消息
     * @param startTime
     * @param endTime
     * @param userId
     * @return
     */
    public List getMsgByUserIdGroup(String startTime, String endTime, String userId);
    public int updateMsgGroupInfo(String userId, List groups) ;
    public int updateMsgGroupInfoByChannel(List userIds, String channel) ;

    /**
     * 根据UserId获得消息(发现模块)
     * @param startTime
     * @param endTime
     * @param userId
     * @param comparator
     * @param predicate
     * @param pageSize
     * @param pageNum
     * @return
     */
    public PageUtils getMsgByUserIdFind(String startTime, String endTime, String userId, String comparator, String predicate, int pageSize, int pageNum) ;

    List getMsgIdsByUserId(String startTime, String endTime, String userId, int msgStatusAll, Integer msgTypeGenerally);
}
