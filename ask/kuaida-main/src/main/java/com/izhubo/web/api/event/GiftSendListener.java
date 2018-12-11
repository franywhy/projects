package com.izhubo.web.api.event;

/**
 * @author: wubinjie@ak.cc
 * Date: 14-4-22 下午6:23
 */
public interface GiftSendListener {
    public abstract void fireEvent(Integer room_id,Integer userId,Integer toId,Integer gift_id,Integer cost,Integer count);
}
