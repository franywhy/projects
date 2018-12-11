package com.izhubo.common.constant;

/**
 *
 * Param key.
 *
 * date: 13-10-25 上午10:26
 *
 * @author: wubinjie@ak.cc
 */
public interface Constant {

        //快播移动("km-")  user
        public static final String KB_USER_API = "http://mobile.kuaibo.com/api/user/info";
        public static final String APP_SECURITY = "0d909c3824dad00f9448245829c194375aedb5e8";    //test-fc20089bc50d4c6b1811ca61047480cb51f25669
        public static final String APP_ID = "200005";    //test- 199999
        //快播移动 pay
        public static final String PRIVATE_KEY = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJYU/3JMLB8jrDoEH5yQSgUAlcLaNiCs1llUXsD25uNJPG2/lTsGSP5LrmA6vwMJ26rUnM1TrMiHyGjNBYz3V3uBBMcZ2I/MOj0MjJyw4DLK1q20xeh1vlqr6afHvdofHSvj6X/h67T/7RXHUMCKkE5xYjVxPxAxQIqn3WFvRr7DAgMBAAECgYBe9NAeh/VOimp4lo1NqLk8av9WNmwSxmgkQs5ktpKk8XxLT/DiVHqwcj/U4LZj/MPAVR8UhkGG6uow7K77Xq85G/bPg7SFJ5d3ZWNKVTgFkIV6nn/hbTs0ojz1ObehnUjOCjWt6QUe5PMkCmukT+45qaadDT6+n7yfXHcduiTwOQJBAMe5GzkSJndmv9y9ssRYLKKRIAPcGDo7ObI0ro/38x4n+89xj58ZUHTaFmBERg3QNOZNEUb9wYnmxXGO6OeJiPUCQQDAXxEYe1TtDTvALI+Yz7Ku3kN5l574aq/vYWtkudlRF5fFLEO3Gb0YRUpINlB4czZ3/C0JrUIkeBI36jsBgjXXAkBcHpDj3e9IG2hbVFPkJBdSVEXEAfxav355J7tsRB0qx55prCNmarKlcwHnHRaCwInlv548dEaUaJChNB1QzisRAkAgOs0+cMtVu/XayJx2WbXyHkLnyENWE2nOjV/UzF5ge7dtzXC66JEJ0/ISDyeERqlFiPZyvJhsS8GhpJimNA6ZAkEAwoBJnMRexvY8DstFjWqrJlp2VsfP9m9L2L03Uwgt3TYY0eVcKyPbScS+MSXpkLqTZubIcxUJGi8+xb4ufGEauw==" ;
        public static final String PARTNER_ID = "9" ;
        public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGm2jWbIHzTdPFkpRQ9d8oMESqNeqFQBtp/nqHQNIoXWLedfq+Xqpzyu1HwQKDLWyTvNAzSilk/+h81Hg2dzdy5WDbEH0NWc3yHIho0043fNpq5I2omQrZJjVN4gs/7J2cJvt+GvfnJdNG6zQHv8ObUmAC3AlFT8StmPrEVvMJKwIDAQAB" ;

       public static final Integer USER_LEVEL_15 = 1461000 ;  //15等级 公爵
       public static final Integer USER_LEVEL_14 = 1120700 ;  //14等级 侯爵
       public static final Integer USER_LEVEL_10 = 311500 ;  //10等级
       public static final Integer USER_LEVEL_8 = 135200 ;  //8等级
       public static final Integer USER_LEVEL_7 = 82600 ;  //7等级
       public static final Integer USER_LEVEL_4 = 11200 ;  //4等级
       public static final Integer USER_LEVEL_3 = 4200 ;  //3等级
       public static final Integer USER_LEVEL_2 = 1100 ;  //2等级
      //白名单   1代表 发布广播权限
      public static final Integer BROADCAST_PRIVATE = 1 ;     //1代表 发布广播权限
      public static final Integer PAYPAL_PRIVATE = 2 ;        //2代表允许 paypal 支付
      public static final Integer KICK_PRIVATE = 3 ;        //3代表踢人白名单
      public static final Integer BROKER_PRIVATE = 4 ;        //4特别经济人名单 属于此种经纪人下面主播不可兑换星币
   // Medal
    public final static Integer MEDAL_USER_TYPE = 1 ;
    public final static Integer MEDAL_STAR_TYPE = 2;

    //主播最高等级
    public static final Integer STAR_MAX_LEVEL = 60 ;


}
