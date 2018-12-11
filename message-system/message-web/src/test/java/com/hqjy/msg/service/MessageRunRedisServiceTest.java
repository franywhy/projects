package com.hqjy.msg.service;

import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/2/1 0001.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageRunRedisServiceTest {

    @Autowired
    @Qualifier("mysql")
    private MessageRunService messageRunMysqlService;

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;

    //@Test
    public void test(){
        List list = new ArrayList();
        list.add("baobao");
        list.add("bao");
        messageRunService.setReceivers(list);
    }
    @Test
    public void test1(){
        System.out.println(messageRunService.getChannelVersion("baobao"));
        System.out.println(messageRunService.getChannelVersion("zikao"));
    }
    @Test
    public void test3(){
        System.out.println("start:"+ DateUtils.dateToString(DateUtils.getNowDate()));
        String [] strs = new String[]{"test_1", "test_3"};
        //"test_1", "test_2", "test_3", "test", "kuaiji","dept_1"/*,"sex_2"*/
        //"bao_1", "bao_2", "bao_3", "test", "bao_4"
        Arrays.asList(strs).stream().forEach((String str)->{
            List list = new ArrayList();
            messageRunMysqlService.findMsgChannelsByGroup(str).stream().forEach(p->{
                list.add((String)((Map)p).get(Constant.REDIS_KEY));
            });
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            messageRunService.saveChannelsByGroup(str,list);
        });

        //test_1,test_2,test_3,kuaiji,all,test
        System.out.println("end:"+ DateUtils.dateToString(DateUtils.getNowDate()));

        //System.out.println(messageRunService.getChannelVersion("baobao"));
    }

    @Test
    public void test4(){
        System.out.println("start:"+ DateUtils.dateToString(DateUtils.getNowDate()));
        messageRunService.setsIntoCacheZSet("common"+Constant.CHANNEL_REDIS,
                Arrays.asList(new String[]{"test_1", "test_2", "test_3","bao_2","baobao","dept_1","kuaiji","test"}).stream().map(p->{
            return p+Constant.CHANNEL_REDIS;
        }).collect(Collectors.toList()));
        System.out.println("end:"+ DateUtils.dateToString(DateUtils.getNowDate()));

        //System.out.println(messageRunService.getChannelVersion("baobao"));
    }

    @Test
    public void testGetGroup(){
        System.out.println("start:"+ DateUtils.dateToString(DateUtils.getNowDate()));

        Arrays.asList(new String[]{"test_1", "test_2", "test_3", "test", "kuaiji","dept_1"/*,"sex_2"*/}).stream().forEach((String str)->{
            System.out.println("size:"+messageRunService.findMsgChannelsByGroup(str).size());

        });
        //test_1,test_2,test_3,kuaiji,all,test
        System.out.println("end:"+ DateUtils.dateToString(DateUtils.getNowDate()));

        //System.out.println(messageRunService.getChannelVersion("baobao"));
    }
    @Test
    public void updateGroupInfo(){
        List groups = new ArrayList();
        Map map = new HashMap();

		map.put("channel", "zikao");
		map.put("type", "+");
		groups.add(map);

        map = new HashMap();
        map.put("channel", "baobao");
        map.put("type", "+");
        groups.add(map);

        map = new HashMap();
        map.put("channel", "test");
        map.put("type", "+");
        groups.add(map);
        System.out.println(StringUtils.objToJsonStr(groups));
        //messageRunService.updateMsgGroupInfo("baobao", groups);
        System.out.println("更改成功！！！");
    }

    @Test
    public void getGroupInfo(){


        ((List)messageRunService.getUpdateGroupInfo()).stream().forEach(p->System.out.println(p));

    }
    @Test
    public void testCheckGroups() throws DefaultException {
        List groups = new ArrayList();
        groups.add("test");
        groups.add("kuaiji2");
        messageRunService.checkGroup(groups);

    }
    @Test
    public void testVersion() throws DefaultException {
        List groups = new ArrayList();
        groups.add("test");
        groups.add("kuaiji2");
        messageRunService.checkGroup(groups);

    }
    @Test
    public void testUpdatMsg() throws DefaultException {
        MsgMessage msgMessage = new MsgMessage();
        msgMessage.setCode("5AA4A4316BA52646236930F6A9BE1AB488CB20180203093224");
        msgMessage.setCreateTime(new Date());
        msgMessage.setVersion(1.3);
        msgMessage.setDr(2);

        System.out.println(messageRunMysqlService.updateMsg(msgMessage));

    }




}
