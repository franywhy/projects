package com.hqjy.msg.service;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgMessage;

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
    public int updateMsg(String msgId,String sendTime,String msg,List groups,List channelsJson) throws DefaultException;

    /**
     * 删除消息
     * @param msgId
     * @return
     */
    public int delMsg(String msgId) throws DefaultException;

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
    public  int runByTime(String msgId);

    /**
     * 消息过期操作
     * @param msgId 消息ID
     */
    public  void removeMsg(String msgId);



    /**
     * 根据用户ID获取消息列表
     * @param startTime
     * @param endTime
     * @param userId
     * @param type
     * @return
     */
    public List getMsgByUserId(String startTime,String endTime,String userId,int type);

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
    public void setMsgReadedByUserId(String msgId,String userId);

    /**
     * 获取未读消息的条数
     *
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    public int getNotReadMessagesCountByUserId(String userId, String startTime, String endTime);

}
