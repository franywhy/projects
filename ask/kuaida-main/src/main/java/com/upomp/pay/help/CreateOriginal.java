package com.upomp.pay.help;

import com.upomp.pay.info.Upomp_Pay_Info;


/*
 * 创建签名原串
 */
public class CreateOriginal {

    public static  String CreateOriginal_Sign(int args){

        StringBuilder os3,os7,os4,os6;

        switch(args){
            //LanchPay：订单验证
            case 3:

                os3 = new StringBuilder();
                os3.append("merchantId=")
                        .append(Upomp_Pay_Info.merchantId).append("&merchantOrderId=")
                        .append(Upomp_Pay_Info.merchantOrderId).append("&merchantOrderTime=")
                        .append(Upomp_Pay_Info.merchantOrderTime);

                return os3.toString();

            //SubmitOrder：订单提交
            case 7:
                os7 = new StringBuilder();
                os7.append("merchantName=").append(Upomp_Pay_Info.merchantName).append("&merchantId=")
                        .append(Upomp_Pay_Info.merchantId).append("&merchantOrderId=")
                        .append(Upomp_Pay_Info.merchantOrderId).append("&merchantOrderTime=")
                        .append(Upomp_Pay_Info.merchantOrderTime).append("&merchantOrderAmt=")
                        .append(Upomp_Pay_Info.merchantOrderAmt).append("&merchantOrderDesc=")
                        .append(Upomp_Pay_Info.merchantOrderDesc).append("&transTimeout=")
                        .append(Upomp_Pay_Info.transTimeout);
                return os7.toString();

            //QueryOrder:订单查询
            case 4:
                os4 = new StringBuilder();
                os4.append("transType=").append(Upomp_Pay_Info.type[1]);
                os4.append("&merchantId=")
                        .append(Upomp_Pay_Info.merchantId).append("&merchantOrderId=")
                        .append(Upomp_Pay_Info.merchantOrderId).append("&merchantOrderTime=")
                        .append(Upomp_Pay_Info.merchantOrderTime);

                return os4.toString();

            //Cancel: 订单退货
            case 6:
                os6 = new StringBuilder();

                os6.append("merchantId=")
                        .append(Upomp_Pay_Info.merchantId).append("&merchantOrderId=")
                        .append(Upomp_Pay_Info.merchantOrderId).append("&merchantOrderTime=")
                        .append(Upomp_Pay_Info.merchantOrderTime).append("&merchantOrderAmt=")
                        .append(Upomp_Pay_Info.merchantOrderAmt).append("&cupsQid=")
                        .append(Upomp_Pay_Info.cupsQid).append("&backEndUrl=").append(Upomp_Pay_Info.backEndUrl);

                return os6.toString();

            default:
                System.out.println("报文原串拼接错误");
        }

        return null;



    }


}
