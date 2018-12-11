package com.hqjy.msg.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = MockServletContext.class)
@AutoConfigureMockMvc
@SpringBootTest
@WebAppConfiguration
public class BaseTest {

    @Autowired
    private MockMvc mvc;

    public MockMvc getMvc() {
        return mvc;
    }

    //@Test
   /* public void getStudentList() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("/users/get"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();
        System.out.println(result.getResponse().getContentAsString().toString());
        //.andExpect(MockMvcResultMatchers.content().string("365"));  //测试接口返回内容
    }*/
}
