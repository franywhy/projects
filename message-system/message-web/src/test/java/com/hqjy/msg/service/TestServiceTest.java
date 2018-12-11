package com.hqjy.msg.service;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.MsgMessageDetail;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

/**
 * Created by Administrator on 2018/2/1 0001.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestServiceTest {

    @Autowired
    private TestService testService;


    @Test
    public void getData() {
        System.out.println(StringUtils.objToJsonStr(testService.selectAll()));
    }

    @Test
    public void testSave(){
        testService.check("msg_message_detail","msg_message_detail_1");
        List<MsgMessageDetail> list = new ArrayList<MsgMessageDetail>();
        MsgMessageDetail detail = new MsgMessageDetail();
        detail.setCode("test");
        detail.setCreateTime(new Date());
        detail.setSendTime(new Date());
        detail.setIsRead(0);
        detail.setRowId("test");
        detail.setRecBy("test");
        detail.setMessage("message test");
        list.add(detail);
        //testService.batchSave("msg_message_detail_1",list);
    }
    //@Test
    public void getDetailsData(){
        MsgMessage msgMessage = new MsgMessage();
        msgMessage.setSendTime(DateUtils.stringToDate("2018-02-03 13:09:30",DateUtils.DATE_FORMAT));
        //3DCB3EE1ZB551Z433EZB3A9Z75F4292AADDB20180203130930
        msgMessage.setCode("3DCB3EE1ZB551Z433EZB3A9Z75F4292AADDB20180203130930");
        //14092233
        String userId = "14092233";
        System.out.println(StringUtils.objToJsonStr(testService.getMsgMessageDetail(userId,msgMessage)));
        testService.updateMsgDetailSetReaded(userId,msgMessage);
        System.out.println(StringUtils.objToJsonStr(testService.getMsgMessageDetail(userId,msgMessage)));
        System.out.println(StringUtils.objToJsonStr(testService.isExistMsgDetail(userId,msgMessage)));
    }

    @Test
    public void getDetailsData2(){
       List list = testService.getDetailsByUserAndTimes("10010613","2018-01-01 13:09:30","2018-03-03 16:09:30",null);
        System.out.println(list.size());
        System.out.println(StringUtils.objToJsonStr(list));
    }

    @Test
    public void batchSaveReaded(){
        List list = new ArrayList();
        Map map = new HashMap();
        map.put("table_name","msg_message_detail_201802_1");
        map.put("user_id","10010053");
        map.put("code","432AD307RC445R4E1ARAE1ERA3117131663020180225141133");
        list.add(map);
        map.put("table_name","msg_message_detail_201802_1");
        map.put("user_id","10010053");
        map.put("code","34B71A4ANBC95N4A8DN9F8BN3BC9E29AA3D020180225141133");
        list.add(map);
        testService.batchUpdateMsgDetailReaded(list);
    }
}
