package com.hqjy.msg.service;

import com.hqjy.msg.model.PushMsgBase;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/2.
 */
public interface XingePushService {

   /**
     * 发送给单独一个账号（不管对方是IOS还是android）
     * @param userId 用户id
     * @param title 消息标题题
     */
    public void pushAccountAndroidOrIosByUserId(long userId, String title);

    /**
     * 群发给指定的tag用户（不管对方是IOS还是android）
     * @param tagList 指定标签List
     * @param pushMsgBase
     */
    public void pushTagAndroidOrIos(List<String> tagList,PushMsgBase pushMsgBase);

    /**
     * 推送消息给大批量账号
     * @param pushId 接口返回的 push_id
     * @param accountList 元素为推送的目标账号，string 类型，数量有限制，目前为 1000个
     */
    public void pushAccountListMultiple(int pushId, List<String> accountList);

    /**
     * 群发给指定的tag用户（IOS用户）
     * @param tagList 指定标签List
     * @param pushMsgBase
     */
    public void pushTagIos(List<String> tagList, PushMsgBase pushMsgBase);

    /**
     * 群发给指定的tag用户（android用户）
     * @param tagList 指定标签List
     * @param pushMsgBase
     */
    public void pushTagAndroid(List<String> tagList, PushMsgBase pushMsgBase);

    /**
     * 群发给所有用户
     * @param title 消息标题
     */
    public void pushAllAccount(String title);


}
