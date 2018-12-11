package com.izhubo.model;

/**
 * 签到100天， 羽毛365片
 * 系统徽章
 */
public enum Msg {

    申请家族通过("申请家族通过","你所申请的家族${family_name}已通过你的加入申请,欢迎加入家族的大家庭,共享家族荣耀"),
    申请家族拒绝("申请家族拒绝","你所申请的家族${family_name}已拒绝你的加入申请,再接再厉尝试加入其它家族吧!"),
    家族创建成功("家族创建成功","恭喜你,已成功创建家族${family_name},你的家族主页地址是:${family_url},尽快修改家族标识吸引更多的人加入你的家族吧!"),
    家族解散("家族解散","你所在的家族${family_name}已解散!"),
    家族踢出("您已被家族踢出","你所在的家族族长已把你请出家族,再接再厉尝试加入其它家族吧!"),
    ;

    private final String title;
    private final String content;

    Msg(String title, String content){
        this.title = title;
        this.content = content;
    }

    public String getTitle(){
        return this.title;
    }

    public String getContent(){
        return this.content;
    }
}
