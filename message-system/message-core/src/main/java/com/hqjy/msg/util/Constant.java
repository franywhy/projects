package com.hqjy.msg.util;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class Constant {

    public final static String WAIT_REDIS = "_wait"; //Redis键值的后缀(待消费队列)
    public final static String MESSAGE_REDIS = "_msg";//Redis键值的后缀(消息体)
    public final static String USER_REDIS = "_user";//Redis键值的后缀(默认)
    public final static String CHANNEL_REDIS = "_channel";
    public final static String MSG_USER_REDIS = "_relation";


    public final static String REDIS_KEY = "key";  //存储的键值

    public final static String READED_START_KEY = "readed_"; //标识用户已读的消息前缀
    public final static String MSG_INTO_MQ = "_mq"; //标识用户已读的消息前缀

    public final static int  GET_MSG_MAX_SIZE = 0; //获取消息条数的默认值
    public final static int  MSG_STATUS_READED = 1;
    public final static int  MSG_STATUS_NOT_READ = 0;
    public final static int  MSG_STATUS_ALL = -1;

    public final static int SPLIT_LIST_SIZE = 10000;//缓存用户消息的块个体数

    public final static int THREAD_POOL_CORE_SIZE = 20; //线程池的实际个数
    public final static int THREAD_POOL_MAX_SIZE = 1100;//线程池的最大个数
    public final static int THREAD_POOL_ALIVE_TIME = 60;//线程池的生命时间
    public final static int THREAD_POOL_QUEUE = 100000; //等待线程池的队列

    public final static int COMPARE_MINITUS = 2; //日期相差的分钟数

    public final static int GROUP_TYPE_GROUP = 1; //群组类型（群组）
    public final static int GROUP_TYPE_SYS = 0;//系统
    public final static int GROUP_TYPE_USER = 2;//系统

    public final static int MSG_SEND_TYPE_NOW = 0; //消息发送类型（立即）
    public final static int MSG_SEND_TYPE_TIME = 1;//消息发送类型（按时）

    public final static int MSG_SEND_STATUS_NOT = 0; //消息发送状态（未发送）
    public final static int MSG_SEND_STATUS_ED = 1; //消息发送状态（已发送）

    //public final static int MSG_SAVE_DAYS = 7;
    public final static int MSG_SAVE_DAYS = 14;


    public  final static String queques = "msg_";
    /*public  final static String queques = "";*/


    public final static String QUEUE_WAIT = queques+"wait";  //待消费队列
    public final static String QUEUE_MSG = queques+"msg"; //缓存消息队列
    public final static String QUEUE_MSG_EXPIRE = queques+"msgExpire"; //消息过期队列
//    public final static String QUEUE_CONSUMER_ONE = queques+"consumer1";//缓存消息队列1
//    public final static String QUEUE_CONSUMER_TWO = queques+"consumer2";//缓存消息队列2
    public final static String QUEUE_CONSUMER = queques+"consumer";//缓存消息队列
    public final static String QUEUE_CONSUMER_HANDLER = queques+"consumerHandler";//缓存消息处理队列


    public final static String QUEUE_CONSUMER_READED_REDIS = queques+"setConsumerReadedRedis";//设置已读的处理队列
    public final static String QUEUE_CONSUMER_READED_DB = queques+"setConsumerReadedDb";

    public final static String QUEUE_WAIT_EXPIRE = queques+"setWaitMsgExpire";
    public final static String QUEUE_WAIT_DEL = queques+"delWaitMsg";

    public final static String QUEUE_SEND_MSG = queques+"sendMsg";
    public static final java.lang.String QUEUE_MSG_DETAIL = queques+"msgDetail";

    public final static String QUEUE_SYNC_REDIS_TO_MYSQL = queques+"syncRedisToMysql";

    public final static Integer GROUP_ADD = 1;
    public final static Integer GROUP_DEL = 0;


    public final static Integer MSG_TYPE_GENERALLY = 0; //"generally"
    public final static Integer MSG_TYPE_COMMON = 1;//"common"
    public final static Integer MSG_TYPE_FIND = 2;//"find"

    public final static Integer MSG_SORT_NOT =0;
    public final static Integer MSG_SORT_RECOMMEND =1;

    public final static String QUEUE_UPDATE_GROUP = queques+"updateGroup";
    public final static String QUEUE_UPDATE_GROUP_RECORD = queques+"updateGroupRecord";
    //public final static String QUEUE_REDIS_MSG_WITH_USER = queques+"msgWithUser";

}
