package com.izhubo.rest.pushmsg.model;

public enum PushMsgTypeEnum {
	talk(0),text(1),adver(2),openpage(3),nothing(4);
   
   private final int value;

   //构造器默认也只能是private, 从而保证构造函数只能在内部使用
   PushMsgTypeEnum(int value) {
       this.value = value;
   }
   
   public int getValue() {
       return value;
   }
}
