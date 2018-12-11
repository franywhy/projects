package com.hqjy.msg.provide;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgChannels;
import com.hqjy.msg.model.MsgGroup;
import com.hqjy.msg.model.MsgMessage;

import java.util.*;

/**
 * Created by Administrator on 2018/1/31 0031.
 */
public interface MessageRunService {
    default public Map getAyncMsg(){
        return null;
    }
    default  public int saveChannelsByGroup(String group,List msgChannels){
        return -1;
    }

    default  public List findMsgChannelsByMsg(String msgId){
        return Arrays.asList();
    }
    default public void saveRelationBetweenUserAndMsg(String msgId,List users){
    }
    default public List getUsersByMsg(String msgId){
        return null;
    }
    default public Object getUpdateGroupInfo(){return null;}
    default public void removeMsgWithUser(String msgId,List list){

    }
    default public void removeKey(String key){

    }
    default public void removeChannels(List zset){

    }

    default public void removeReadedMsg(List zset){

    }
    default public void removeZset(String key,List zset){

    }
    default public void setZset(String key,String value,long time){

    }

    default  public  Set setsIntoCacheZSet(String key, List strs){return null;}

    default public Object getZset(String key){
        return null;
    }

    default public void removeHashMap(String key,String column){

    }
    default public boolean hasKey(String key){
        return  false;
    }
    default public Object getReadedMsg(){
        return null;
    }
    default public int updateMsgGroupInfoByChannel(List userIds, String channel){return  0;}
    default public Set  setMsgChannel(MsgMessage msg){
        return null;
    }

    /*-----------------------**/
    default  public void saveMsgDetail(MsgMessage msgMessage,List list,MessageRunService messageRunService){

    }
    default  public String setMsgDetailReaded(String userId,String code,Date date){
        return null;
    }

    default public int isExistMsgDetail(String userId,MsgMessage message){
        return 0;
    }

    default  public void runJdbc(String sql){

    }

    default  public List getCommonMsg(String channel,String msg,String startTime,String endTime){
        return null;
    }

    public double getMsgVersion(String msgId);

    public double  getChannelVersion(String groupChannel);

    /**
     * 更新群组信息
     * @param userId 用户 ID
     * @param groups 群体数组
     * @return
     */
    public abstract int updateMsgGroupInfo(String userId,List groups) ;

    public List checkGroup(List groupChannels) throws DefaultException;

    public void saveMsg(MsgMessage message);

    public List getGroupChildrens(List groupChannels,List jsonChannels);

    public  MsgMessage findMsgByMsgIdWithNoDr(String msgId);

    /**
     * 根据消息ID获取消息记录
     * @param msgId
     * @return
     */
    default public  MsgMessage findMsgByMsgId(String msgId) {
        MsgMessage message = findMsgByMsgIdWithNoDr(msgId);
        if (null!=message && message.getDr()==1) {
            return null;
        }

        return message;
    }

    /**
     * 根据群组频道查询群组成员
     * @param groupChannel
     * @return
     */
    public  List findMsgChannelsByGroup(String groupChannel);


    /**
     * 判断群组是否存在
     * @param groupChannel  群组频道
     * @return
     */
    public  boolean isExistsGroup(String groupChannel);

    /**
     * 查询群组频道跟群组成员是否存在
     * @param groupChannel
     * @return
     */
    public  boolean isExistsByGroupAndLeaguer(String groupChannel,String leaguerChannel);

    public int saveChannel(MsgChannels msgChannels);

    public int saveGroup(MsgGroup msgGroup);

    public   int updateMsg(MsgMessage message);

    public void setReceivers(List channelsJson);
}
