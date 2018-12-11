package com.izhubo.web.api.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-23
 * Time: 上午10:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class MedalAwardObserver {
    private static final List<MedalAwardListener> listenerList = new ArrayList<>();

    public static void fireAwardEvent(Integer userId, Integer user_type,  String mid){
        for (MedalAwardListener awardListener : listenerList){
            awardListener.fireEvent(userId, user_type, mid);
        }
    }

    public static void addAwardListner(MedalAwardListener medalAwardListener){
        listenerList.add(medalAwardListener);
    }
}
