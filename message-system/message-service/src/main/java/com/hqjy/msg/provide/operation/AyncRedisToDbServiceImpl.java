package com.hqjy.msg.provide.operation;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.AyncRedisToDbService;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.MsgManagerService;
import com.hqjy.msg.provide.SendAdapterService;
import com.hqjy.msg.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/2/1 0001.
 */
@Service
public class AyncRedisToDbServiceImpl implements AyncRedisToDbService {

    private static Logger logger = LoggerFactory.getLogger(AyncRedisToDbService.class);

    @Autowired
    @Qualifier("redis")
    private MessageRunService reidsMessageRunService;

    @Autowired
    @Qualifier("mysql")
    private MessageRunService mysqlMessageRunService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MsgManagerService msgManagerService;

    @Value("${common.group}")
    private String groupChannel;

    @Autowired
    private SendAdapterService sendAdapterService;

    private  boolean isSync = false;

    private void checkConn(){
        try {
            if (null==jdbcTemplate.getDataSource().getConnection() ) {
                logger.warn("jdbc is not connect");
                return;
            }
        } catch (SQLException e) {
            logger.warn("jdbc is not connect");
            return;
        }
    }

    @Override
    public void syncMsg() {

        //checkConn();
        Map msgMap = reidsMessageRunService.getAyncMsg();
        if(null==msgMap) {
            logger.warn(" syncMsg has not sync data..");
            return;
        }
        logger.info("syncMsg is start sync..."+DateUtils.dateToString(DateUtils.getNowDate()));
        long beginTime = System.currentTimeMillis(); // 开始时间
        msgMap.forEach((key,value)->{
            String msgId = (String) key;
            double redisVersion = (double) value;
            double mysqlVersion = mysqlMessageRunService.getMsgVersion(msgId);
            MsgMessage message = reidsMessageRunService.findMsgByMsgIdWithNoDr(msgId);
            if (null == message) {
                return;
            }
            //每次每條消息更新50000
            /*List list = (List) ListUtils.listsToArray(reidsMessageRunService.getUsersByMsg(msgId), 100000).get(0);
            ListUtils.listsToArray(list, 10000).stream().forEach(p->{
                mysqlMessageRunService.saveMsgDetail(message,(List)p,reidsMessageRunService);
            });*/
            //数据库没记录
            if (mysqlVersion==0) {
                message.setDr(0);
                message.setVersion(1.0);
                mysqlMessageRunService.saveMsg(message);
            }
            //更新
            if (redisVersion > mysqlVersion) {
                message.setVersion(redisVersion);
                mysqlMessageRunService.updateMsg(message);
            }

            //del
            if (message.getDr()==1) {
                //reidsMessageRunService.removeHashMap("msg"+Constant.MESSAGE_REDIS,msgId);
            }
            int compare = DateUtils.compare(DateUtils.addDays(message.getSendTime(),Constant.MSG_SAVE_DAYS),DateUtils.getNowDate());
            if (compare>0) {
                try {
                    msgManagerService.removeMsg(msgId);
                } catch (DefaultException e) {
                    e.printStackTrace();
                }
            }
        });
        long endTime = (System.currentTimeMillis() - beginTime) / 1000;
        logger.info("syncMsg  is end("
                + DateUtils.dateToString(DateUtils.getNowDate()) + ")...cost " + endTime + "s");


    }
    private Integer detailSize = 0;
    private Integer maxDetailSize = 100000;
    @Override
    public void syncDetail() {
        detailSize = 0;
        //checkConn();
        if (!isSync) {

            //start
            List lists = (List) reidsMessageRunService.getZset("detail"+Constant.CHANNEL_REDIS);
        if(!ListUtils.listIsExists(lists)){
            logger.warn(" syncDetail has not sync data..");
            return;
        }
        logger.info("syncDetail is start sync..."+DateUtils.dateToString(DateUtils.getNowDate()));
        long beginTime = System.currentTimeMillis(); // 开始时间
        isSync = true;
        logger.info("sync Detail is start sync....");
        //
            for (int i = 0; i < lists.size(); i++) {
                String msgId = (String) lists.get(i);
                boolean isFinish = false;
                try {
                MsgMessage message = reidsMessageRunService.findMsgByMsgIdWithNoDr(msgId);
                if (null == message) {
                    isSync = false;
                    continue;
                }
                if (message.getDr()==Constant.MSG_SEND_STATUS_NOT && message.getSendStatus() == Constant.MSG_SEND_STATUS_NOT) {
                    isSync = false;
                    continue;
                }
                if (detailSize > maxDetailSize) {
                    break;
                }

                //
                //每次每條消息更新50000
                List users = reidsMessageRunService.getUsersByMsg(msgId);
                ListUtils.listsToArray(users, 50000).forEach(pp->{
                    ListUtils.listsToArray((List)pp, 12000).stream().forEach(ppp->{
                        mysqlMessageRunService.saveMsgDetail(message,(List)ppp,reidsMessageRunService);

                    });

                });
                isFinish = true;
                }catch (Exception e){
                    isSync = false;
                    return;
                }
                if (isFinish) {
                    ///reidsMessageRunService.removeKey(msgId+Constant.MSG_USER_REDIS+Constant.CHANNEL_REDIS);
                    reidsMessageRunService.removeZset("detail"+Constant.CHANNEL_REDIS, Arrays.asList(msgId));
                    detailSize += lists.size();
                }
                try{
                    Thread.sleep(2000*60);
                }catch (Exception e){

                }


            }

        //
        /*lists = (List) ListUtils.listsToArray(lists,5).get(0);
        lists.stream().forEach(p->{
            String msgId = (String) p;
            boolean isFinish = false;
            try {
                MsgMessage message = reidsMessageRunService.findMsgByMsgIdWithNoDr(msgId);
                if (null == message) {
                    isSync = false;
                    return;
                }
                if (message.getDr()==Constant.MSG_SEND_STATUS_NOT && message.getSendStatus() == Constant.MSG_SEND_STATUS_NOT) {
                    isSync = false;
                    return;
                }
                //每次每條消息更新50000
                List users = reidsMessageRunService.getUsersByMsg(msgId);
                ListUtils.listsToArray(users, 50000).forEach(pp->{
                     ListUtils.listsToArray((List)pp, 12000).stream().forEach(ppp->{
                         mysqlMessageRunService.saveMsgDetail(message,(List)ppp,reidsMessageRunService);

                     });
                    try{
                        Thread.sleep(1000*60);
                    }catch (Exception e){

                    }
                });
                isFinish = true;

            }catch (Exception e){
                isSync = false;
                return;
            }
            if (isFinish) {
                reidsMessageRunService.removeZset("detail"+Constant.CHANNEL_REDIS, Arrays.asList(msgId));
            }
        });*/
        long endTime = (System.currentTimeMillis() - beginTime) / 1000;
        logger.info("syncDetail  is end("
                + DateUtils.dateToString(DateUtils.getNowDate()) + ")...cost " + endTime + "s");

            //end
            try {
                Thread.sleep(4000*60);
            } catch (InterruptedException e) {
                //e.printStackTrace();
            }
            isSync = false;
        }else{
            logger.info("sync Detail is  syncing....");
        }




    }

    @Override
    public void syncChannel() {

        //checkConn();
        boolean isRunStatus = false;
        List list = (List) reidsMessageRunService.getUpdateGroupInfo();

        if(!ListUtils.listIsExists(list)){
            logger.warn(" syncChannel has not sync data..");
            return;
        }
        logger.info("syncChannel is start sync..."+DateUtils.dateToString(DateUtils.getNowDate()));
        long beginTime = System.currentTimeMillis(); // 开始时间
        try {
        list.stream().forEach(p->{
            String str = (String)p;

                Map map = (Map) StringUtils.strToObj(str,Map.class);
                String userId = (String) map.get("user_id");
                List groups = (List) map.get("channels");
                mysqlMessageRunService.updateMsgGroupInfo(userId,groups);

        });
        isRunStatus = true;
        }catch (Exception e){
            //e.printStackTrace();
            return;
        }finally {
            if (isRunStatus && ListUtils.listIsExists(list)) {
                reidsMessageRunService.removeChannels(list);

            }
        }
        long endTime = (System.currentTimeMillis() - beginTime) / 1000;
        logger.info("syncChannel  is end("
                + DateUtils.dateToString(DateUtils.getNowDate()) + ")...cost " + endTime + "s");
    }

    @Override
    public void syncReadedMsg() {
        boolean isRunStatus = false;
        //checkConn();
        List results = (List)reidsMessageRunService.getReadedMsg();
        if (!ListUtils.listIsExists(results) ) {
            logger.warn(" syncReadedMsg has not sync data..");
            return;
        }
        logger.info("syncReadedMsg is start sync..."+DateUtils.dateToString(DateUtils.getNowDate()));
        long beginTime = System.currentTimeMillis(); // 开始时间
        List list = (List) ListUtils.listsToArray(results ,3000).get(0);
        List removeList = new ArrayList();
        try {
            StringBuilder sb = new StringBuilder();
            List runSqls = (List) list.stream().map(p->{
                String str = (String)p;
                String [] result = str.split("#");
                String userId = result[0];
                String msgId = result[1];
                //
                String sql = "";
                MsgMessage msgMessage = reidsMessageRunService.findMsgByMsgIdWithNoDr(msgId);
                if(null==msgMessage) return "";
                if(mysqlMessageRunService.isExistMsgDetail(userId,msgMessage)<=0){
                    if (msgMessage.getMsgType()==Constant.MSG_TYPE_COMMON) {
                        mysqlMessageRunService.saveMsgDetail(msgMessage, Arrays.asList(userId),reidsMessageRunService);
                    }
                    return "";
                }

                Date date = msgMessage.getSendTime();
                sql = mysqlMessageRunService.setMsgDetailReaded(userId,msgId,date);
                removeList.add(str);
                return sql;
            }).collect(Collectors.toList());
            runSqls.stream().forEach(p->{
                String sql = (String)p;
                sb.append(sql);
            });
            if (!StringUtils.isEmpty(sb.toString())) {
                mysqlMessageRunService.runJdbc(sb.toString());
                isRunStatus = true;
            }

        }catch (Exception e){
            e.printStackTrace();
            return;
        }finally {
            if (isRunStatus && ListUtils.listIsExists(removeList)) {
                reidsMessageRunService.removeReadedMsg(removeList);

            }
        }

        long endTime = (System.currentTimeMillis() - beginTime) / 1000;
        logger.info("syncReadedMsg  is end("
                + DateUtils.dateToString(DateUtils.getNowDate()) + ")...cost " + endTime + "s");

    }
}
