package com.izhubo.web.api.event;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-4-23
 * Time: 上午10:14
 * To change this template use File | Settings | File Templates.
 */
public abstract class MedalAwardListener
{
    public abstract void fireEvent(Integer userId, Integer user_type, String mid);
}
