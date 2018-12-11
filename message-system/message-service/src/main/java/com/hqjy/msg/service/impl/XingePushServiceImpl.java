package com.hqjy.msg.service.impl;


import com.hqjy.msg.model.PushMsgBase;
import com.hqjy.msg.service.XingePushService;
import com.tencent.xinge.Message;
import com.tencent.xinge.MessageIOS;
import com.tencent.xinge.XingeApp;
import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/2.
 */
@Service
public class XingePushServiceImpl implements XingePushService {


    private  Long ANDROID_ACCESS_Id ;//= 2100258303L;//android应用id
    @Value("${xinge.android.accessid}")
    private void setANDROID_ACCESS_Id(String str){
        if(StringUtils.isNotBlank(str)){
            ANDROID_ACCESS_Id = Long.valueOf(str);
        }
    }

    @Value("${xinge.android.secretkey}")
    private  String ANDROID_SECRET_KEY ;//= "cc238257d1c9a52b710de548b94162c1";//android应用密钥

    private  Long IOS_ACCESS_Id ;//= 2200260029L;//ios应用id
    @Value("${xinge.ios.accessid}")
    private void setIOS_ACCESS_Id(String str){
        if(StringUtils.isNotBlank(str)){
            IOS_ACCESS_Id = Long.valueOf(str);
        }
    }

    @Value("${xinge.ios.secretkey}")
    private  String IOS_SECRET_KEY ;//= "e3a2d014bb58259b1be89eae5c39a538";//ios应用密钥
    private int XINGE_APP_IOSENV ;//= 2;

    @Value("${xinge.iosenv}")
    private void setIosnv(String str){
        if(StringUtils.isNotBlank(str)){
            XINGE_APP_IOSENV = Integer.valueOf(str);
        }
    }


    /**
     * 发送给单独一个账号
     * @param userId 用户id
     * @param title 消息标题
     */
    public void pushAccountAndroidOrIosByUserId(long userId, String title) {
        //根据userId发送消息给ios用户
        PushMsgIOSByUserId(userId, IOS_ACCESS_Id, IOS_SECRET_KEY, title);
        //根据userId发送消息给Android用户
        PushMsgAndroidByUserId(userId, ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, title);

    }

    /**
     * 群发给指定的tag用户（不管对方是IOS还是android）
     * @param tagList 指定标签List
     * @param pushMsgBase
     */
    public void pushTagAndroidOrIos(List<String> tagList, PushMsgBase pushMsgBase) {
        //群发给指定的目标用户（IOS用户）
        PushTagIos(tagList, IOS_ACCESS_Id, IOS_SECRET_KEY, pushMsgBase);
        //群发给指定的Android用户
        PushTagAndroid(tagList, ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, pushMsgBase);
    }

    /**
     * 群发给指定的tag用户（IOS用户）
     * @param tagList 指定标签List
     * @param pushMsgBase
     */
    public void pushTagIos(List<String> tagList,  PushMsgBase pushMsgBase) {
        PushTagIos(tagList, IOS_ACCESS_Id, IOS_SECRET_KEY, pushMsgBase);
    }

   /**
     * 群发给指定的tag用户（android用户）
     * @param tagList 指定标签List
     * @param pushMsgBase
     */
    public void pushTagAndroid(List<String> tagList,  PushMsgBase pushMsgBase) {
        PushTagAndroid(tagList, ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, pushMsgBase);
    }

    /**
     * 群发给所有用户
     * @param title 消息标题
     */
    public void pushAllAccount(String title) {
        if(XINGE_APP_IOSENV == 1){  //生产环境
            //群发给所有IOS用户
            PushAllIosAccount(IOS_ACCESS_Id, IOS_SECRET_KEY, title);
            //群发给所有Android用户
            PushAllAndroidAccount(ANDROID_ACCESS_Id, ANDROID_SECRET_KEY, title);
        }
    }


    //通过userId发送android消息
    private void PushMsgAndroidByUserId(long userId, long accessId, String secretKey, String title){

        XingeApp xinge = new XingeApp(accessId, secretKey);
        Message message = new Message();
        message.setExpireTime(259200);//设置离线消息存活时间为3天（单位是秒）
        message.setType(Message.TYPE_NOTIFICATION);    //设置消息类型 （消息通知）
        message.setTitle(title);
        JSONObject resultAndroid = xinge.pushSingleAccount(0, String.valueOf(userId), message);

        System.out.println("android:"+resultAndroid);
    }

    //通过UserId发送IOS消息
    private void PushMsgIOSByUserId(long userId, long accessId, String secretKey, String title){

        XingeApp xinge = new XingeApp(accessId, secretKey);
        MessageIOS iosmessage = new MessageIOS();
        iosmessage.setExpireTime(259200);//设置离线消息存活时间为3天（单位是秒）
        iosmessage.setAlert(title);
        JSONObject resultIOS = xinge.pushSingleAccount(0, String.valueOf(userId), iosmessage, XINGE_APP_IOSENV);//表示推送开发环境

        System.out.println("ios:"+resultIOS);
    }

    //通过tag发送android消息
    private void PushTagAndroid(List<String> tagList, long accessId, String secretKey,  PushMsgBase pushMsgBase){

        XingeApp xinge = new XingeApp(accessId, secretKey);
        Message message = new Message();
        message.setExpireTime(259200);//设置离线消息存活时间为3天（单位是秒）
        message.setType(Message.TYPE_NOTIFICATION);//消息通知
        message.setTitle(pushMsgBase.getTitle());
        message.setContent(pushMsgBase.getContent());
        message.setCustom(pushMsgBase.getCustom());

        org.json.JSONObject resultAndroid = xinge.pushTags(0, tagList, "OR", message);
        System.out.println("android:"+resultAndroid);
    }

    //通过tag发送IOS消息
    private void PushTagIos(List<String> tagList, long accessId, String secretKey,  PushMsgBase pushMsgBase){

        XingeApp xinge = new XingeApp(accessId, secretKey);
        MessageIOS iosmessage = new MessageIOS();
        iosmessage.setExpireTime(259200);//设置离线消息存活时间为3天（单位是秒）
        iosmessage.setAlert(new JSONObject(pushMsgBase.toString()));
        iosmessage.setBadge(+1);
        iosmessage.setSound("beep.wav");
        iosmessage.setCustom(pushMsgBase.getCustom());
        iosmessage.setCategory(pushMsgBase.getContent());
        JSONObject resultIOS = xinge.pushTags(0, tagList, "OR", iosmessage, XINGE_APP_IOSENV);

        System.out.println("ios:"+resultIOS);
    }

    //发送消息给所有android用户
    private void PushAllAndroidAccount(long accessId, String secretKey, String title){
        XingeApp xinge = new XingeApp(accessId, secretKey);
        Message message = new Message();
        message.setExpireTime(259200);//设置离线消息存活时间为3天（单位是秒）
        message.setType(Message.TYPE_NOTIFICATION);
        message.setTitle(title);
        JSONObject resultAndroid = xinge.pushAllDevice(0, message);

        System.out.println("android:"+resultAndroid);
    }

    //发送消息给所有ios用户
    private void PushAllIosAccount(long accessId, String secretKey, String title){
        XingeApp xinge = new XingeApp(accessId, secretKey);
        MessageIOS iosmessage = new MessageIOS();
        iosmessage.setExpireTime(259200);//设置离线消息存活时间为3天（单位是秒）
        iosmessage.setAlert(title);
        iosmessage.setBadge(+1);
        iosmessage.setSound("beep.wav");
        JSONObject resultIOS = xinge.pushAllDevice(0, iosmessage, XINGE_APP_IOSENV);

        System.out.println("ios:"+resultIOS+"ENV:"+XINGE_APP_IOSENV);
    }
    //推送消息给大批量账号
    public void pushAccountListMultiple(int pushId, List<String> accountList){
        XingeApp xinge = new XingeApp(000, "myKey");
        pushId = 11;
        List<String> accountList1 = new ArrayList<String>();
        accountList1.add("nickName1");

        JSONObject  ret1 = xinge.pushAccountListMultiple (pushId, accountList1);
        List<String> accountList2 = new ArrayList<String>();
        accountList2.add("nickName2");
        JSONObject  ret2 = xinge.pushAccountListMultiple (pushId, accountList2);
        System.out.println("__" + ret1);
        System.out.println("__" + ret2);
    }


}
