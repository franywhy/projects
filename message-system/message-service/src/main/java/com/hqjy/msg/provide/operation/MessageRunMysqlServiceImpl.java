package com.hqjy.msg.provide.operation;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgChannels;
import com.hqjy.msg.model.MsgGroup;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.MsgMessageDetail;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.service.MsgChannelsService;
import com.hqjy.msg.service.MsgGroupService;
import com.hqjy.msg.service.MsgMessageService;
import com.hqjy.msg.service.TestService;
import com.hqjy.msg.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/1/31 0031.
 */
@Service("mysql")
public class MessageRunMysqlServiceImpl implements MessageRunService {

    @Autowired
    private MsgGroupService msgGroupService;
    @Autowired
    private MsgChannelsService msgChannelsService;

    @Autowired
    private MsgMessageService msgMessageService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TestService testService;

    @Override
    public int updateMsgGroupInfo(String userId, List groups) {
        return msgGroupService.updateMsgGroupInfo(userId, groups);
    }

    @Override
    public List checkGroup(List groupChannels) throws DefaultException {
        msgGroupService.checkGroup(groupChannels);
        return null;
    }

    @Override
    public void saveMsg(MsgMessage message) {
        msgMessageService.insertMsgMessage(message);
    }

    @Override
    public List getGroupChildrens(List groupChannels, List jsonChannels) {
        return msgGroupService.getGroupChildrens(groupChannels,jsonChannels);
    }

    @Override
    public MsgMessage findMsgByMsgIdWithNoDr(String msgId) {
        return msgMessageService.findMsgMessageByCode(msgId);
    }

    /*@Override
    public MsgMessage findMsgByMsgId(String msgId) {
        MsgMessage message = findMsgByMsgIdWithNoDr(msgId);
        if (null!=message && message.getDr()==1) {
            return null;
        }

        return message;
    }*/

    @Override
    public List findMsgChannelsByGroup(String groupChannel) {
        return msgChannelsService.findMsgChannelsByGroupJdbc(groupChannel);
    }

    @Override
    public boolean isExistsGroup(String groupChannel) {
        return msgGroupService.isExistsGroup(groupChannel);
    }

    @Override
    public boolean isExistsByGroupAndLeaguer(String groupChannel, String leaguerChannel) {
        return msgChannelsService.isExistsByGroupAndLeaguer(groupChannel,leaguerChannel);
    }

    @Override
    public int saveChannel(MsgChannels msgChannels) {
        return msgChannelsService.insert(msgChannels);
    }

    @Override
    public int saveGroup(MsgGroup msgGroup) {
        return msgGroupService.insert(msgGroup);
    }

    @Override
    public int updateMsg(MsgMessage message) {

        MsgMessage msgMessage = msgMessageService.findMsgMessageByCode(message.getCode());
        if (null == msgMessage) {
            return -1;
        }
        message.setId(msgMessage.getId());
        return msgMessageService.update(message);
    }

    @Override
    public void setReceivers(List channelsJson) {
        for (int i = 0; i < channelsJson.size(); i++) {
            String str = (String) channelsJson.get(i);
            //if (this.msgGroupService.isExistsGroup(str)) {
            //    if (!this.msgChannelsService.isExistsByGroupAndLeaguer(str, str)) {
            if (isExistsGroup(str)) {
                if (!isExistsByGroupAndLeaguer(str, str)) {
                    //this.msgChannelsService.
                    MsgChannels msgChannels = new MsgChannels();
                    msgChannels.setGroupChannel(str);
                    msgChannels.setLeaguerChannel(str);
                    //this.msgChannelsService.insert(msgChannels);
                    saveChannel(msgChannels);
                }
            }else{
                MsgGroup msgGroup = new MsgGroup();
                msgGroup.setChannel(str);
                msgGroup.setName(str);
                msgGroup.setType(Constant.GROUP_TYPE_USER);
                //this.msgGroupService.insert(msgGroup);
                saveGroup(msgGroup);
            }

        }
    }

    public double getMsgVersion(String msgId){
        double version = 0;
        String sql = "select version from msg_message where code = '"+msgId.replaceAll("'","")+"';";
        List list = jdbcTemplate.queryForList(sql);
        if (ListUtils.listIsExists(list)) {
            Map map = (Map) list.get(0);
            version = null==map.get("version")?0:(double)map.get("version");
        }
        return version;
    }

    public double  getChannelVersion(String groupChannel){

        return 1;
    }

    public void saveMsgDetail(MsgMessage msgMessage,List list,MessageRunService messageRunService){

        //ThreadPoolExecutorUtils.getMysqlThreadPoolExecutor().execute(()->{
            saveMsgDetailPart(msgMessage,list,"0",messageRunService);
        //});
        //ThreadPoolExecutorUtils.getMysqlThreadPoolExecutor().execute(()->{
            saveMsgDetailPart(msgMessage,list,"1",messageRunService);
        //});
        //ThreadPoolExecutorUtils.getMysqlThreadPoolExecutor().execute(()->{
            saveMsgDetailPart(msgMessage,list,"2",messageRunService);
        //});

    }

    private synchronized void saveMsgDetailPart(MsgMessage msgMessage,List list,String after,MessageRunService messageRunService){
        String oldTableName = "msg_message_detail";
        String tableName = testService.getTableName(after,oldTableName,msgMessage.getSendTime());
        testService.check(oldTableName,tableName);
        boolean isRunStatus = false;
        try{
            testService.batchSaveMsgMessageDetail(tableName,(List)list.stream().filter(p->{
                Integer num = -1;
                if (StringUtils.isInteger((String)p)) {
                    num =  Integer.valueOf((String)p)%3;
                }else {
                    num =  ((String)p).length()%3;
                }
                return num==Integer.valueOf(after) /*&& testService.isExistMsgDetail((String)p,msgMessage)<=0*/;
            }).map(p->{
                String userId = (String)p;
                MsgMessageDetail detail = new MsgMessageDetail();
                detail.setMessage(msgMessage.getMessage());
                detail.setRecBy(userId);
                detail.setRowId(DateUtils.dateToString(msgMessage.getSendTime(),"YYYYMM")+"_"+after+"#"+msgMessage.getSendTime().getTime());
                detail.setIsRead(0);
                detail.setSendTime(msgMessage.getSendTime());
                detail.setCode(msgMessage.getCode());
                detail.setCreateTime(new Date());

                return detail;
            }).collect(Collectors.toList()));
            isRunStatus = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {

            if (isRunStatus && ListUtils.listIsExists(list)) {
                //messageRunService.removeMsgWithUser(msgMessage.getCode(),list);
            }

        }

    }

    public String  setMsgDetailReaded(String userId,String code,Date date){
        return testService.updateMsgDetailSetReadedSql(userId,code,date);
    }

    public void runJdbc(String sql){

        jdbcTemplate.execute(sql);
    }

    public int isExistMsgDetail(String userId,MsgMessage message){
        return  testService.isExistMsgDetail(userId,message);
    }
}
