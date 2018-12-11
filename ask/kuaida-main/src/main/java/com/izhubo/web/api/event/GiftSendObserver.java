package com.izhubo.web.api.event;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wubinjie@ak.cc
 * Date: 14-4-22 下午6:13
 */
public abstract class GiftSendObserver {
    private static final List<GiftSendListener> listenerList = new ArrayList<>();

    public static void fireGiftSendEvent(Integer room_id,Integer userId,Integer toId,Integer gift_id,Integer cost,Integer count){
        for (GiftSendListener awardListener : listenerList){
            awardListener.fireEvent(room_id, userId, toId,gift_id,cost,count);
        }
    }

    public static void addGiftSendListner(GiftSendListener giftSendListener){
        listenerList.add(giftSendListener);
    }
}
