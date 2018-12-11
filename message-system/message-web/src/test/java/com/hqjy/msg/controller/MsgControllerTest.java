package com.hqjy.msg.controller;

import com.hqjy.msg.util.DateUtils;
import org.junit.Test;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

/**
 * Created by Administrator on 2018/1/2 0002.
 */
public class MsgControllerTest extends BaseTest {


    //@Test
    public void test1() throws Exception {
        MvcResult result = super.getMvc().perform(
                MockMvcRequestBuilders.post("/msg/sendUserMessage")
                       // .param("channels_json","11,22,33,44,55,66")
                .param("send_time","2018-01-01 11:08:00")
                .param("msg","this is a test")
                .param("send_person","")
                .param("group_channel","bao")
                )

                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();
        System.out.println(result.getResponse().getContentAsString().toString());
    }



    @Test
    public void test3() throws Exception {
        //String group = "kuaiji,zikao,kuaiji_app,sex_1,sex_2,dept_1,dept_736,all";
        String group = "test";
        for (int i = 0; i < 30; i++) {
            String sendTime = "";
            //String sendTime = DateUtils.dateToString(DateUtils.addMinutes(new Date(),(i+3)));
            String channels = "ldb,baobao";

            MvcResult result = null;
            try {
                result = getMvc().perform(
                        MockMvcRequestBuilders.post("/msg/sendUserMessage")
                                .param("channels_json",channels)
                                .param("send_time",sendTime)
                                .param("msg","this is "+i+" info in  20180110")
                                .param("send_person","")
                                .param("group_channels",group)
                )

                        .andExpect(MockMvcResultMatchers.status().isOk())
                        //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                        .andReturn();

                System.out.println(result.getResponse().getContentAsString().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /*group="test_2,test_1";
        for (int i = 0; i < 30; i++) {
            String sendTime = DateUtils.dateToString(DateUtils.addMinutes(new Date(),(i+10)));
            String channels = "";
            if (i%10 == 0) {
                channels = "baobao,bao";
            }
            MvcResult result = null;
            try {
                result = getMvc().perform(
                        MockMvcRequestBuilders.post("/msg/sendUserMessage")
                                .param("channels_json",channels)
                                .param("send_time",sendTime)
                                .param("msg","this is "+i+" msg in Test 20180110")
                                .param("send_person","")
                                .param("group_channels",group)
                )

                        .andExpect(MockMvcResultMatchers.status().isOk())
                        //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                        .andReturn();

                System.out.println(result.getResponse().getContentAsString().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    //@Test
    public void test2() throws Exception {

        /*ThreadPoolExecutorUtils.getDefaultThreadPoolExecutor().execute(new Runnable() {
            @Override
            public void run() {*/



                String group="kuaiji,zikao,test,zikao_app,sex_0";
                for (int i = 0; i < 50; i++) {
                    String sendTime = DateUtils.dateToString(DateUtils.addMinutes(new Date(),(i+3)));
                    String channels = "";
                    if (i%10 == 0) {
                        channels = "baobao,bao";
                    }
                    MvcResult result = null;
                    try {
                        result = getMvc().perform(
                                MockMvcRequestBuilders.post("/msg/sendUserMessage")
                                        .param("channels_json",channels)
                                        .param("send_time",sendTime)
                                        .param("msg","this is "+i+" test in Test 20180109")
                                        .param("send_person","")
                                        .param("group_channels",group)
                        )

                                .andExpect(MockMvcResultMatchers.status().isOk())
                                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                                .andReturn();

                    System.out.println(result.getResponse().getContentAsString().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                group = "kuaiji,zikao,kuaiji_app,sex_1,sex_2,dept_1,dept_736,all";
                for (int i = 0; i < 50; i++) {
                    String sendTime = "";
                    //String sendTime = DateUtils.dateToString(DateUtils.addMinutes(new Date(),(i+3)));
                    String channels = "ldb,abc,test";

                    MvcResult result = null;
                    try {
                        result = getMvc().perform(
                                MockMvcRequestBuilders.post("/msg/sendUserMessage")
                                        .param("channels_json",channels)
                                        .param("send_time",sendTime)
                                        .param("msg","this is "+i+" test in  20180109")
                                        .param("send_person","")
                                        .param("group_channels",group)
                        )

                                .andExpect(MockMvcResultMatchers.status().isOk())
                                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                                .andReturn();

                    System.out.println(result.getResponse().getContentAsString().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                group = "kuaiji,zikao,sex_2,dept_1,all";
                for (int i = 0; i < 50; i++) {
                    String sendTime = "";
                    //String sendTime = DateUtils.dateToString(DateUtils.addMinutes(new Date(),(i+3)));
                    String channels = "";

                    MvcResult result = null;
                    try {
                        result = getMvc().perform(
                                MockMvcRequestBuilders.post("/msg/sendUserMessage")
                                        .param("channels_json",channels)
                                        .param("send_time",sendTime)
                                        .param("msg","this is "+i+" test in  group 2")
                                        .param("send_person","")
                                        .param("group_channels",group)
                        )

                                .andExpect(MockMvcResultMatchers.status().isOk())
                                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                                .andReturn();

                    System.out.println(result.getResponse().getContentAsString().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                group = "dept_736,test";
                for (int i = 0; i < 50; i++) {

                    String sendTime = DateUtils.dateToString(DateUtils.addMinutes(new Date(),(i+2)));
                    String channels = "";

                    MvcResult result = null;
                    try {
                        result = getMvc().perform(
                                MockMvcRequestBuilders.post("/msg/sendUserMessage")
                                        .param("channels_json",channels)
                                        .param("send_time",sendTime)
                                        .param("msg","this is "+i+" test in  group 3")
                                        .param("send_person","")
                                        .param("group_channels",group)
                        )

                                .andExpect(MockMvcResultMatchers.status().isOk())
                                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                                .andReturn();

                    System.out.println(result.getResponse().getContentAsString().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                group = "all";
                for (int i = 0; i < 10; i++) {
                    String sendTime = "";
                    //String sendTime = DateUtils.dateToString(DateUtils.addMinutes(new Date(),(i+3)));
                    String channels = "";

                    MvcResult result = null;
                    try {
                        result = getMvc().perform(
                                MockMvcRequestBuilders.post("/msg/sendUserMessage")
                                        .param("channels_json",channels)
                                        .param("send_time",sendTime)
                                        .param("msg","this is "+i+" test in  group 4")
                                        .param("send_person","")
                                        .param("group_channels",group)
                        )

                                .andExpect(MockMvcResultMatchers.status().isOk())
                                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                                .andReturn();

                    System.out.println(result.getResponse().getContentAsString().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
         /*   }
        });*/



       /* MvcResult result = super.getMvc().perform(
                MockMvcRequestBuilders.post("/msg/sendUserMessage")
                        .param("channels_json","11,22,33,44,55,66")
                        .param("send_time","2018-01-01 11:08:00")
                        .param("msg","")
                        .param("send_person","")
                        .param("group_channel","bao")
        )

                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();
        System.out.println(result.getResponse().getContentAsString().toString());*/
    }


}
