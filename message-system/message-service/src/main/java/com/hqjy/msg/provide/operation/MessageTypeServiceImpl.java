package com.hqjy.msg.provide.operation;

import com.hqjy.msg.model.MsgMessage;
import com.hqjy.msg.provide.MessageRunService;
import com.hqjy.msg.provide.MessageTypeService;
import com.hqjy.msg.provide.interfaces.MessageTypeInterface;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/1/26 0026.
 */
@Service
public class MessageTypeServiceImpl implements MessageTypeService{

    public String send(List channelsJson, MsgMessage message, List groupChannels, MessageTypeInterface messageTypeInterface,MessageRunService messageRunService)throws Exception {
        return messageTypeInterface.send(channelsJson,message,groupChannels,messageRunService);
    }
}
