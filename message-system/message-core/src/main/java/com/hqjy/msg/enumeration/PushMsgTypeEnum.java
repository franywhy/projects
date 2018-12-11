package com.hqjy.msg.enumeration;
/**
 * Created by baobao on 2017/12/12 0012.
 * @author baobao
 * @email liangdongbin@hengqijy.com
 * @date 2017-12-20 14:20:09
 * @descrition 消息类型枚举类
 */
public enum PushMsgTypeEnum {
    generally(0),common(1),find(2);
   
   private final int value;

   //构造器默认也只能是private, 从而保证构造函数只能在内部使用
   PushMsgTypeEnum(int value) {
       this.value = value;
   }
   
   public int getValue() {
       return value;
   }
}
