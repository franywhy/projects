package com.hqjy.msg.provide.interfaces;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.MessageRunService;

import java.util.List;

/**
 * Created by Administrator on 2018/1/26 0026.
 */
public interface MessageTypeInterface {

    public String send(List channelsJson, MsgMessage message, List groupChannels,MessageRunService messageRunService)throws Exception;
}
