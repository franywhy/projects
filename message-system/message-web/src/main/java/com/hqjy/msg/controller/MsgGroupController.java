package com.hqjy.msg.controller;

import com.hqjy.msg.config.AmqpConfig;
import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.WrappedResponse;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.MsgManagerService;
import com.hqjy.msg.service.MsgGroupService;
import com.hqjy.msg.util.Constant;
import com.hqjy.msg.util.DateUtils;
import com.hqjy.msg.util.StringUtils;
import com.hqjy.msg.util.ThreadPoolExecutorUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/group")
public class MsgGroupController extends AbstractRestController {
    @Autowired
    private MsgManagerService msgManagerService;

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;

    @Autowired
    @Qualifier("mysql")
    private MessageRunService messageRunMysqlService;

    @ApiOperation(value="更新群组信息", notes="更新群组信息")
    @RequestMapping(value="/updateMsgGroup", method= RequestMethod.POST)
    public ResponseEntity<WrappedResponse> updateGroup(
            @RequestParam(value = "user_id",required = true) String userId,//用户ID
            @RequestParam(value = "groups",required = true) String groups//	群组ID
    ) throws DefaultException {
        if(StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");
        if(StringUtils.isEmpty(groups)) return this.error("群组json不能为空");
        List<Map> list = null;
        try{list = (List<Map>) StringUtils.strToObj(groups,List.class);}catch (Exception e){
            throw new DefaultException("群组json格式错误");
        }

        int count = msgManagerService.updateMsgGroupInfo(userId, list);

        Map map = new HashMap();
        map.put("count",count);

        return this.success(map);
    }

    @ApiOperation(value="根据频道更新群组信息", notes="根据频道更新群组信息")
    @RequestMapping(value="/updateMsgGroupByChannel", method= RequestMethod.POST)
    public ResponseEntity<WrappedResponse> updateGroupByChannel(
            @RequestParam(value = "user_ids",required = true) String userIds,//用户ID
            @RequestParam(value = "channel",required = true) String channel//	群组ID
    ) throws DefaultException {
        if(StringUtils.isEmpty(userIds)) return this.error("用户ID不能为空");
        if(StringUtils.isEmpty(channel)) return this.error("群组json不能为空");
        List<Map> list = null;
        try{list = (List<Map>) StringUtils.strToObj(userIds,List.class);}catch (Exception e){
            throw new DefaultException("群组json格式错误");
        }

        int count = msgManagerService.updateMsgGroupInfoByChannel(list,channel );

        Map map = new HashMap();
        map.put("count",count);

        return this.success(map);
    }

/*

    @ApiOperation(value="初始化群组信息", notes="初始化群组信息")
    @RequestMapping(value="/initMsgGroup", method= RequestMethod.POST)
    public ResponseEntity<WrappedResponse> initGroup(

            @RequestParam(value = "groups",required = true) String groups//	群组ID
    ) throws DefaultException {

        if(StringUtils.isEmpty(groups)) return this.error("群组json不能为空");
        System.out.println("start:"+ DateUtils.dateToString(DateUtils.getNowDate()));
        //"test_1", "test_2", "test_3", "test", "kuaiji","dept_1"*/
/*,"sex_2"*//*

        String [] strs = groups.split(",");
        ThreadPoolExecutorUtils.getMysqlThreadPoolExecutor().execute(()->{
            try {
                Arrays.asList(strs*/
/*new String[]{"bao_1", "bao_2", "bao_3", "test", "bao_4"}*//*
).stream().forEach((String str)->{
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
                messageRunService.setsIntoCacheZSet("common"+Constant.CHANNEL_REDIS,
                        Arrays.asList(strs).stream().map(p->{
                            return p+Constant.CHANNEL_REDIS;
                        }).collect(Collectors.toList()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        //test_1,test_2,test_3,kuaiji,all,test
        System.out.println("end:"+ DateUtils.dateToString(DateUtils.getNowDate()));
        return this.success("初始化成功");
    }

*/


    /*@ApiOperation(value="获取列表", notes="")
    @RequestMapping(value={"/get"}, method= RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getUserList() {
        //List<Test> r = testService.getAll();
      *//*  List r = msgGroupService.selectAll();*//*
        return this.success();
        //return r;
    }*/

    @ApiOperation(value="获取当前时间", notes="")
    @RequestMapping(value={"/getNowTime"}, method= RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getNowTime() {
        //List<Test> r = testService.getAll();

        return this.success(DateUtils.dateToString(DateUtils.getNowDate()));
        //return r;
    }
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @ApiOperation(value="test", notes="")
    @RequestMapping(value={"/test"}, method= RequestMethod.GET)
    public ResponseEntity<WrappedResponse> test() {
        //List<Test> r = testService.getAll();
        rabbitTemplate.convertAndSend(AmqpConfig.EXCHANGE, Constant.QUEUE_SYNC_REDIS_TO_MYSQL, StringUtils.objToJsonStr("test"));
        return this.success(DateUtils.dateToString(DateUtils.getNowDate()));
        //return r;
    }
}
