package com.hqjy.msg.provide;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.interfaces.MessageTypeInterface;

import java.util.List;

/**
 * Created by Administrator on 2018/1/26 0026.
 */
public interface MessageTypeService {

    public String send(List channelsJson, MsgMessage message, List groupChannels,MessageTypeInterface messageTypeInterface,MessageRunService messageRunService)throws Exception;
}
