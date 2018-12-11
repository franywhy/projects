package com.hqjy.msg.service;

import com.hqjy.msg.model.Objects;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * Created by Administrator on 2017/12/30 0030.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisMsgServiceTest {
    @Autowired
    private RedisMsgService<Objects> redisMsgService;

    @Test
    public void test(){
        List list = new ArrayList();
        for (int i = 0; i < 100; i++) {
            Map map = new HashMap();
            map.put("value",String.valueOf(i+100));
            map.put("score",Long.valueOf(i+1000));
            map.put("key",String.valueOf(i));
            list.add(map);
        }
        redisMsgService.pipeline("aa"+Constant.USER_REDIS,list);
    }

    //@Test
    public void test1(){
        List list = new ArrayList();
        for (int i = 0; i < 10000; i++) {
            Map map = new HashMap();
            map.put("value",String.valueOf(i+1));
            map.put("key",String.valueOf(i));
            list.add(map);
        };
        redisMsgService.remove("1","101",list);
    }

    //@Test
    public void test2(){
        Object result  = this.redisMsgService.getZsetCache("11",Long.valueOf("1514822400000"), Long.valueOf("1515168000000"),0);
        List list = (List ) result;
        System.out.println(StringUtils.objToJsonStr(list));
        //String value = (String) list.get(0);
        String value = "3AFAB07AHCCFAH4C11HB5F7H1C7348FE632E20180102173752";
        System.out.println(value);
        //this.redisMsgService.setZsetCacheUpdate("11",value);
        //this.redisMsgService.setZsetCacheUpdate("11",value);
        //List reuslts = StringUtils.linkedHashSetsToStrings(list);
       // System.out.println(StringUtils.objToJsonStr(reuslts));
       /* LinkedHashSet set = (LinkedHashSet) list.get(0);
        Iterator iterator = set.iterator();
        String value = (String) iterator.next();
        System.out.print(value);*/
        //this.redisMsgService.setZsetCacheUpdate("11",value);
        //ZRANGEBYSCORE 11 1514822400000 1515168000000
    }
    @Test
    public void test3(){
        System.out.println(new String((byte[]) redisMsgService.execute("Info")));
    }

    @Test
    public void testHasKey(){
        System.out.println(redisMsgService.hasKey("test"+Constant.CHANNEL_REDIS));
    }

    @Test
    public void testUnion(){
        Set set = redisMsgService.setsIntoCacheZSet("baobao"+Constant.CHANNEL_REDIS,Arrays.asList("test_2_channel","test_1_channel"));

    }

}
