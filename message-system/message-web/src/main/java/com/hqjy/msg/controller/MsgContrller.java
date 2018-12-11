package com.hqjy.msg.controller;

import com.hqjy.msg.enumeration.PushMsgTypeEnum;
import com.hqjy.msg.exception.DefaultException;
import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.model.WrappedResponse;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.MsgManagerService;
import com.hqjy.msg.provide.RedisMsgService;
import com.hqjy.msg.service.MsgMessageService;
import com.hqjy.msg.util.*;
import com.rabbitmq.tools.json.JSONUtil;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Administrator on 2017/12/25 0025.
 */
@RestController
@RequestMapping(value = "/msg")     // 通过这里配置使下面的映射都在/users下，可去除
public class MsgContrller extends AbstractRestController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MsgManagerService msgManagerService;
   /* @Autowired
    private MsgMessageService msgMessageService;
    @Autowired
    private RedisMsgService redisMsgService;*/

    @Autowired
    @Qualifier("redis")
    private MessageRunService messageRunService;

    @Value("${common.group}")
    private String groupChannel;

    private Integer initPageSize = 10;


    @ApiOperation(value = "给用户发消息", notes = "给用户发消息")
    @RequestMapping(value = "/sendUserMessage", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> sendUserMessage(@RequestParam(value = "channels_json", required = false) List channelsJson,//群组频道
                                                           @RequestParam(value = "send_time", required = false) String sendTime,//发送时间send_time
                                                           @RequestParam(value = "title", required = true) String title,//发送标题
                                                           @RequestParam(value = "content", required = true) String content,//发送副标题
                                                           @RequestParam(value = "msg", required = false) String msg,//发送内容send_content
                                                           @RequestParam(value = "send_person", required = false) String sendPerson,//发送人
                                                           @RequestParam(value = "group_channels", required = false) List groupChannels//群组表

    ) throws Exception {
        //
        if (!ListUtils.listIsExists(groupChannels) && !ListUtils.listIsExists(channelsJson))
            return this.error("群组频道不能为空");
        if (StringUtils.isEmpty(msg)) return this.error("发送内容不能为空");//throw new Exception("发送内容不能为空");
        if (StringUtils.isEmpty(sendTime)) sendTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (!DateUtils.isValidDate(sendTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (StringUtils.isEmpty(title)) return this.error("发送标题不能为空");
        if (StringUtils.isEmpty(content)) return this.error("发送副标题不能为空");
        String msgId = "";
        MsgMessage message = new MsgMessage();
        message.setMessage(msg);
        message.setSendTimeStr(sendTime);
        message.setSendTime(DateUtils.stringToDate(sendTime, DateUtils.DATE_FORMAT));
        message.setMsgType(Constant.MSG_TYPE_GENERALLY);
        message.setTitle(title);
        message.setContent(content);
        msgId = msgManagerService.sendMsg(channelsJson, message, groupChannels);


        Map result = new HashMap();
        result.put("msg_id", msgId);
        return this.success(result);
    }

   /* @ApiOperation(value = "给用户发消息(发现)", notes = "给用户发消息（发现）")
    @RequestMapping(value = "/sendUserMessageFind", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> sendUserMessageFind(@RequestParam(value = "channels_json", required = false) List channelsJson,//群组频道
                                                               @RequestParam(value = "send_time", required = false) String sendTime,//发送时间send_time
                                                               @RequestParam(value = "msg_recommend", required = false) String msgRecommend,//发送时间send_time
                                                               @RequestParam(value = "title", required = true) String title,//发送标题
                                                               @RequestParam(value = "content", required = true) String content,//发送副标题
                                                               @RequestParam(value = "msg", required = false) String msg,//发送内容send_content
                                                               @RequestParam(value = "send_person", required = false) String sendPerson,//发送人
                                                               @RequestParam(value = "group_channels", required = false) List groupChannels//群组表

    ) throws Exception {
        //
        if(!ListUtils.listIsExists(groupChannels) &&!ListUtils.listIsExists(channelsJson)) return this.error("群组频道不能为空");
        if(StringUtils.isEmpty(msg)) return this.error("发送内容不能为空");//throw new Exception("发送内容不能为空");
        if(StringUtils.isEmpty(sendTime)) sendTime = DateUtils.dateToString(DateUtils.getNowDate());
        if(!DateUtils.isValidDate(sendTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if(StringUtils.isEmpty(title)) return this.error("发送标题不能为空");
        if(StringUtils.isEmpty(content)) return this.error("发送副标题不能为空");
        if(!StringUtils.isEmpty(msgRecommend) && !StringUtils.isInteger(msgRecommend) )return this.error("推荐只能是数字");
        Integer msgSort = null;
        try{msgSort = Integer.valueOf(msgRecommend);}catch (Exception e){}

        if (null==msgSort) {
            msgSort = Constant.MSG_SORT_NOT;
        }
        String msgId = "";
        MsgMessage message = new MsgMessage();
        message.setMessage(msg);
        message.setSendTimeStr(sendTime);
        message.setTitle(title);
        message.setContent(content);
        message.setMsgType(Constant.MSG_TYPE_FIND);
        message.setMsgSort(msgSort);
        msgId = msgManagerService.sendMsg(channelsJson,message, groupChannels);


        Map result = new HashMap();
        result.put("msg_id", msgId);
        return this.success(result);
    }*/


    @ApiOperation(value = "给用户发消息(发现)", notes = "给用户发消息（发现）")
    @RequestMapping(value = "/sendUserMessageFind", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> sendUserMessageFind(@RequestParam(value = "channels_json", required = false) List channelsJson,//群组频道
                                                               @RequestParam(value = "send_time", required = false) String sendTime,//发送时间send_time
                                                               @RequestParam(value = "title", required = true) String title,//发送标题
                                                               @RequestParam(value = "content", required = true) String content,//发送副标题
                                                               @RequestParam(value = "msg", required = false) String msg,//发送内容send_content
                                                               @RequestParam(value = "send_person", required = false) String sendPerson,//发送人
                                                               @RequestParam(value = "group_channels", required = false) List groupChannels//群组表

    ) throws Exception {
        //
        if (!ListUtils.listIsExists(groupChannels) && !ListUtils.listIsExists(channelsJson))
            return this.error("群组频道不能为空");
        if (StringUtils.isEmpty(msg)) return this.error("发送内容不能为空");//throw new Exception("发送内容不能为空");
        if (StringUtils.isEmpty(sendTime)) sendTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (!DateUtils.isValidDate(sendTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (StringUtils.isEmpty(title)) return this.error("发送标题不能为空");
        if (StringUtils.isEmpty(content)) return this.error("发送副标题不能为空");
        //if(!StringUtils.isEmpty(msgRecommend) && !StringUtils.isInteger(msgRecommend) )return this.error("推荐只能是数字");
        Integer msgSort = null;
//        try{msgSort = Integer.valueOf(msgRecommend);}catch (Exception e){}
//
//        if (null==msgSort) {
//            msgSort = Constant.MSG_SORT_NOT;
//        }
        String msgId = "";
        MsgMessage message = new MsgMessage();
        message.setMessage(msg);
        message.setSendTimeStr(sendTime);
        message.setTitle(title);
        message.setContent(content);
        message.setMsgType(PushMsgTypeEnum.find.getValue());
        //message.setMsgSort(msgSort);
        msgId = msgManagerService.sendMsg(channelsJson, message, groupChannels);


        Map result = new HashMap();
        result.put("msg_id", msgId);
        return this.success(result);
    }

    @ApiOperation(value = "公共群发消息", notes = "公共群发消息")
    @RequestMapping(value = "/sendCommonMessage", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> sendCommonMessage(
            @RequestParam(value = "send_time", required = false) String sendTime,//发送时间send_time
            @RequestParam(value = "title", required = true) String title,//发送标题
            @RequestParam(value = "content", required = true) String content,//发送副标题
            @RequestParam(value = "msg", required = false) String msg//发送内容send_content

    ) throws Exception {
        //
        if (StringUtils.isEmpty(msg)) return this.error("发送内容不能为空");//throw new Exception("发送内容不能为空");
        if (StringUtils.isEmpty(sendTime)) sendTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (!DateUtils.isValidDate(sendTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (StringUtils.isEmpty(title)) return this.error("发送标题不能为空");
        if (StringUtils.isEmpty(content)) return this.error("发送副标题不能为空");
        String msgId = "";
        List channelsJson = new ArrayList();
        List groupChannels = new ArrayList();
        groupChannels.add(groupChannel);
        MsgMessage message = new MsgMessage();
        message.setMessage(msg);
        message.setSendTimeStr(sendTime);
        message.setTitle(title);
        message.setContent(content);
        message.setMsgType(Constant.MSG_TYPE_COMMON);
        msgId = msgManagerService.sendCommonMsg(channelsJson, message, groupChannels);


        Map result = new HashMap();
        result.put("msg_id", msgId);
        return this.success(result);
    }

    @ApiOperation(value = "获取公共频道", notes = "获取公共频道")
    @RequestMapping(value = "/getCommonChannel", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCommonChannel(

    ) throws Exception {
        return this.success(groupChannel);
    }

    @ApiOperation(value = "更新消息", notes = "更新消息")
    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> updateMessage(
            @RequestParam(value = "msg_id", required = true) String msgId,//	消息ID
            @RequestParam(value = "send_time", required = false) String sendTime,//发送时间send_time
            @RequestParam(value = "msg", required = false) String msg,//发送内容send_content
            @RequestParam(value = "group_channels", required = false) List groupChannels,//群组表
            @RequestParam(value = "channels_json", required = false) List channelsJson//群组频道


    ) throws DefaultException {

        if (StringUtils.isEmpty(msgId)) return this.error("消息ID不能为空");
        Object object = messageRunService.findMsgByMsgId(msgId);
        if (null == object) {
            return this.error("消息不存在");
        }
        this.msgManagerService.updateMsg(msgId, sendTime, msg, groupChannels, channelsJson, Constant.MSG_SORT_NOT);
        return this.success();
    }

    /* @ApiOperation(value = "更新推荐状态（发现）", notes = "更新推荐状态（发现）")
     @RequestMapping(value = "/setMessageRecommend", method = RequestMethod.POST)
     public ResponseEntity<WrappedResponse> setMessageRecommend(
             @RequestParam(value = "msg_id", required = true) String msgId,//	消息ID

             @RequestParam(value = "msg_recommend", required = false) String msgRecommend//发送时间send_time



     ) throws DefaultException {

         if(StringUtils.isEmpty(msgId)) return this.error("消息ID不能为空");
         if(!StringUtils.isEmpty(msgRecommend) && !StringUtils.isInteger(msgRecommend) )return this.error("推荐只能是数字");
         MsgMessage object = messageRunService.findMsgByMsgId(msgId);
         if (null == object) {
             return this.error("消息不存在");
         }
         if (object.getMsgType()!=Constant.MSG_TYPE_FIND)return this.error("该消息不是发现模块的消息，不能修改推荐状态");
         Integer msgSort = null;
         try{msgSort = Integer.valueOf(msgRecommend);}catch (Exception e){}

         if (null==msgSort) {
             msgSort = Constant.MSG_SORT_NOT;
         }

         this.msgManagerService.setMsgRecommend(msgId,msgSort);
         return this.success();
     }*/
    @ApiOperation(value = "获取消息", notes = "获取消息")
    @RequestMapping(value = "/getMessage", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getMessage(
            @RequestParam(value = "msg_id", required = true) String msgId//	消息ID
    ) throws DefaultException {
        if (StringUtils.isEmpty(msgId)) return this.error("消息ID不能为空");
        MsgMessage object = messageRunService.findMsgByMsgId(msgId);
        if (null == object) {
            return this.error("消息不存在");
        }

        return this.success(object);
    }

    @ApiOperation(value = "删除消息", notes = "删除消息")
    @RequestMapping(value = "/delMessage", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> delMessage(
            @RequestParam(value = "msg_id", required = true) String msgId//	消息ID


    ) throws DefaultException {
        if (StringUtils.isEmpty(msgId))
            return this.error("消息ID不能为空");
        Object object = messageRunService.findMsgByMsgId(msgId);
        if (null == object) {
            return this.error("消息不存在");
        }
        int result = this.msgManagerService.delMsg(msgId);
        if (result > 0) {
            return this.success(msgId);
        }
        return this.error("删除失败");
    }

    @ApiOperation(value = "设置消息已读状态", notes = "设置消息已读状态")
    @RequestMapping(value = "/setMessageReaded", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> setMessageReaded(
            @RequestParam(value = "user_id", required = true) String userId//用户ID

    ) {
        //if (!ListUtils.listIsExists(msgIds)) return this.error("消息ID不能为空");
        if (StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");

        String endTime = DateUtils.dateToString(DateUtils.getNowDate());
        String startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT), -Constant.MSG_SAVE_DAYS));

        // 获取用户的所有消息id（从redis可看到有已读前缀(readed_)的消息ID和普通的消息ID）
        List msgIds = msgManagerService.getMsgIdsByUserId(startTime, endTime, userId, Constant.MSG_STATUS_ALL, Constant.MSG_TYPE_GENERALLY);
        // 获取消息id中的未读消息id
        ListUtils.getUnreadMsgIds(msgIds);
        boolean isExistUser = messageRunService.hasKey(userId);
        if (!isExistUser) {
            return this.error("用户不存在");
        }
        msgManagerService.setMsgReadedByUserId(msgIds, userId);
        return this.success();
    }

    @ApiOperation(value = "获取公共消息列表", notes = "获取公共消息列表")
    @RequestMapping(value = "/getCommonMessage", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getCommonMessage(
            @RequestParam(value = "start_time", required = true) String startTime,//	开始时间
            @RequestParam(value = "end_time", required = true) String endTime//	结束时间
    ) {
        if (StringUtils.isEmpty(endTime)) endTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (StringUtils.isEmpty(startTime))
            startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT), -Constant.MSG_SAVE_DAYS));
        if (!DateUtils.isValidDate(startTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (!DateUtils.isValidDate(endTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");

        if (DateUtils.compare(DateUtils.stringToDate(startTime, DateUtils.DATE_FORMAT), DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT)) < 0)
            return this.error("开始时间不能大于结束时间");

        int count = 0;
        List list = msgManagerService.getCommonMsg(startTime, endTime, Constant.MSG_STATUS_ALL);

        count = list.size();
        Map result = new HashMap();
        result.put("count", count);
        result.put("list", list);
        return this.success(result);
    }

    @ApiOperation(value = "获取用户消息列表", notes = "获取用户消息列表")
    @RequestMapping(value = "/getMessageByUserId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getMessageByUserId(@RequestParam(value = "user_id", required = true) String userId,//用户ID
                                                              @RequestParam(value = "start_time", required = false) String startTime,//	开始时间
                                                              @RequestParam(value = "end_time", required = false) String endTime//	结束时间
    ) {
        if (StringUtils.isEmpty(endTime)) endTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (StringUtils.isEmpty(startTime))
            startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT), -Constant.MSG_SAVE_DAYS));
        if (!DateUtils.isValidDate(startTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (!DateUtils.isValidDate(endTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (DateUtils.compare(DateUtils.stringToDate(startTime, DateUtils.DATE_FORMAT), DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT)) < 0)
            return this.error("开始时间不能大于结束时间");
        if (StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");
        int count = 0;
        List list = msgManagerService.getMsgByUserId(startTime, endTime, userId, Constant.MSG_STATUS_ALL, Constant.MSG_TYPE_GENERALLY);

        count = list.size();
        Map result = new HashMap();
        result.put("count", count);
        result.put("list", list);
        return this.success(result);
    }

    @ApiOperation(value = "获取用户消息列表分页", notes = "获取用户消息列表分页")
    @RequestMapping(value = "/getMessageByUserIdPaging", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getMessageByUserIdPaging(@RequestParam(value = "user_id", required = true) String userId,//用户ID
                                                              @RequestParam(value = "start_time", required = false) String startTime,//	开始时间
                                                              @RequestParam(value = "end_time", required = false) String endTime,//	结束时间
                                                              @RequestParam(value = "page", required = false) int page,//	页数
                                                              @RequestParam(value = "page_size", required = false) int pageSize//	页的数据量

    ) {
        if (StringUtils.isEmpty(endTime)) endTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (StringUtils.isEmpty(startTime))
            startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT), -Constant.MSG_SAVE_DAYS));
        if (!DateUtils.isValidDate(startTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (!DateUtils.isValidDate(endTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (DateUtils.compare(DateUtils.stringToDate(startTime, DateUtils.DATE_FORMAT), DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT)) < 0)
            return this.error("开始时间不能大于结束时间");
        if (StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");
        int count = 0;
        List list = msgManagerService.getMsgByUserId(startTime, endTime, userId, Constant.MSG_STATUS_ALL, Constant.MSG_TYPE_GENERALLY);

        // 时间降序
        Collections.reverse(list);
        // List分页
        List pagingList = ListUtils.pagingList(list,page,pageSize);
        count = pagingList.size();
        Map result = new HashMap();
        result.put("count", count);
        result.put("list", pagingList);
        return this.success(result);
    }

    @ApiOperation(value = "获取用户消息列表(发现)", notes = "获取用户消息列表(发现)")
    @RequestMapping(value = "/getMessageByUserIdFind", method = RequestMethod.POST)
    public ResponseEntity<WrappedResponse> getMessageByUserIdFind(@RequestParam(value = "user_id", required = true) String userId,//用户ID
                                                                  @RequestParam(value = "start_time", required = false) String startTime,//	开始时间
                                                                  @RequestParam(value = "end_time", required = false) String endTime,//	结束时间
                                                                  @RequestParam(value = "page_num", required = false) Integer pageNum,//	页码
                                                                  @RequestParam(value = "page_size", required = false) Integer pageSize,//	行数
                                                                  @RequestParam(value = "predicate", required = false) String predicate,//	结束时间
                                                                  @RequestParam(value = "comparator", required = false) String comparator
    ) {
        if (StringUtils.isEmpty(endTime)) endTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (StringUtils.isEmpty(startTime))
            startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT), -Constant.MSG_SAVE_DAYS));
        if (!DateUtils.isValidDate(startTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (!DateUtils.isValidDate(endTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (DateUtils.compare(DateUtils.stringToDate(startTime, DateUtils.DATE_FORMAT), DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT)) < 0)
            return this.error("开始时间不能大于结束时间");
        if (StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");
        if (null == pageSize) pageSize = initPageSize;
        if (null == pageNum) pageNum = 1;
        PageUtils pageUtils = msgManagerService.getMsgByUserIdFind(startTime, endTime, userId, comparator, predicate, pageSize, pageNum);
        return this.success(pageUtils);
    }

    @ApiOperation(value = "统计用户每天消息数量", notes = "统计用户每天消息数量")
    @RequestMapping(value = "/getMessageByUserIdGroup", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getMessageByUserIdGroup(@RequestParam(value = "user_id", required = true) String userId,//用户ID
                                                                   @RequestParam(value = "start_time", required = false) String startTime,//	开始时间
                                                                   @RequestParam(value = "end_time", required = false) String endTime//	结束时间


    ) {
        if (StringUtils.isEmpty(endTime)) endTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (StringUtils.isEmpty(startTime))
            startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT), -Constant.MSG_SAVE_DAYS));
        if (!DateUtils.isValidDate(startTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (!DateUtils.isValidDate(endTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (DateUtils.compare(DateUtils.stringToDate(startTime, DateUtils.DATE_FORMAT), DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT)) < 0)
            return this.error("开始时间不能大于结束时间");
        if (StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");
        List list = msgManagerService.getMsgByUserIdGroup(startTime, endTime, userId);

        return this.success(list);
    }


    /*@ApiOperation(value = "获取用户消息列表(发现)", notes = "获取用户消息列表(发现)")
    @RequestMapping(value = "/getMessageByUserIdFind", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getMessageByUserIdFind(@RequestParam(value = "user_id", required = true) String userId,//用户ID
                                                                  @RequestParam(value = "start_time", required = true) String startTime,//	开始时间
                                                                  @RequestParam(value = "end_time", required = true) String endTime//	结束时间


    ) {
        if(StringUtils.isEmpty(endTime)) endTime = DateUtils.dateToString(DateUtils.getNowDate());
        if(StringUtils.isEmpty(startTime)) startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime,DateUtils.DATE_FORMAT),-Constant.MSG_SAVE_DAYS));
        if(!DateUtils.isValidDate(startTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if(!DateUtils.isValidDate(endTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if(DateUtils.compare(DateUtils.stringToDate(startTime,DateUtils.DATE_FORMAT),DateUtils.stringToDate(endTime,DateUtils.DATE_FORMAT))<0) return this.error("开始时间不能大于结束时间");
        if(StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");
        int count = 0;
        List list = msgManagerService.getMsgByUserId(startTime, endTime, userId, Constant.MSG_STATUS_ALL,Constant.MSG_TYPE_FIND);

        count = list.size();
        Map result = new HashMap();
        result.put("count", count);
        result.put("list", list);
        return this.success(result);
    }*/

    @ApiOperation(value = "获取用户未读消息列表", notes = "获取用户未读消息列表")
    @RequestMapping(value = "/getNotReadMessagesByUserId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getNotReadMessagesByUserId(@RequestParam(value = "user_id", required = true) String userId,//用户ID
                                                                      @RequestParam(value = "start_time", required = true) String startTime,//	开始时间
                                                                      @RequestParam(value = "end_time", required = true) String endTime//	结束时间
    ) {
        if (StringUtils.isEmpty(endTime)) endTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (StringUtils.isEmpty(startTime))
            startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT), Constant.MSG_SAVE_DAYS));

        if (!DateUtils.isValidDate(startTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (!DateUtils.isValidDate(endTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (DateUtils.compare(DateUtils.stringToDate(startTime, DateUtils.DATE_FORMAT), DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT)) < 0)
            return this.error("开始时间不能大于结束时间");
        if (StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");
        int count = 0;
        List list = msgManagerService.getMsgByUserId(startTime, endTime, userId, Constant.MSG_STATUS_NOT_READ, Constant.MSG_TYPE_GENERALLY);

        count = list.size();
        Map result = new HashMap();
        result.put("count", count);
        result.put("list", list);
        return this.success(result);
    }

    @ApiOperation(value = "获取用户未读消息条数", notes = "获取用户未读消息条数")
    @RequestMapping(value = "/getNotReadMessagesCountByUserId", method = RequestMethod.GET)
    public ResponseEntity<WrappedResponse> getNotReadMessagesCountByUserId(@RequestParam(value = "user_id", required = true) String userId,//用户ID
                                                                           @RequestParam(value = "start_time", required = true) String startTime,//	开始时间
                                                                           @RequestParam(value = "end_time", required = true) String endTime//	结束时间


    ) {
        if (StringUtils.isEmpty(endTime)) endTime = DateUtils.dateToString(DateUtils.getNowDate());
        if (StringUtils.isEmpty(startTime))
            startTime = DateUtils.dateToString(DateUtils.addDays(DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT), Constant.MSG_SAVE_DAYS));
        if (StringUtils.isEmpty(userId)) return this.error("用户ID不能为空");
        if (!DateUtils.isValidDate(startTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (!DateUtils.isValidDate(endTime)) return this.error("时间格式不正确(yyyy-MM-dd HH:mm:ss)");
        if (DateUtils.compare(DateUtils.stringToDate(startTime, DateUtils.DATE_FORMAT), DateUtils.stringToDate(endTime, DateUtils.DATE_FORMAT)) < 0)
            return this.error("开始时间不能大于结束时间");

        int count = 0;
        count = msgManagerService.getNotReadMessagesCountByUserId(userId, startTime, endTime);
        Map result = new HashMap();
        result.put("count", count);
        return this.success(result);
    }

}
