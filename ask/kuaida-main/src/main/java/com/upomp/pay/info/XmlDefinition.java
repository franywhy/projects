package com.upomp.pay.info;


/*
 * author: guoqiang
 * 该类主要用于生成，服务器相应操作的报文。
 */
public class XmlDefinition {

    //订单提交
    static String SubmitOrder = "0";
    //订单验证
    static String lanchpay = "1";
    //订单查询
    static String QueryOrder = "2";
    //订单撤销
    static String Cancel = "3";
    //订单退货
    static String Refund = "4";
    //预授权
    static String PreAuthCancel = "5";
    //预授权完成
    static String PreAuthPay = "6";
    //预授权完成撤销
    static String PreAuthPayCancel = "7";

    public static String [] application_chose  = {"SubmitOrder","lanchpay","QueryOrder","Cancel","Refund","PreAuthCancel","PreAuthPay","PreAuthPayCancel"};

    public static String ReturnXml(String sign,int args,String application){


        switch (args){

            case 7:
			/*
			 * SubmitOrder：订单提交报文
			 */
                SubmitOrder = "<?xml version="+"'1.0' "
                        + "encoding=" + "'utf-8' " +"standalone='yes'"+ "?>"+"<upomp  application="+"'"+application+".Req' " +"version="+ "'1.0.0'"+">"

                        + "<merchantName>"
                        + Upomp_Pay_Info.merchantName
                        + "</merchantName>"

                        + "<merchantId>"
                        + Upomp_Pay_Info.merchantId
                        + "</merchantId>"

                        + "<merchantOrderId>"
                        + Upomp_Pay_Info.merchantOrderId
                        + "</merchantOrderId>"

                        + "<merchantOrderTime>"
                        + Upomp_Pay_Info.merchantOrderTime
                        + "</merchantOrderTime>"

                        + "<merchantOrderAmt>"
                        + Upomp_Pay_Info.merchantOrderAmt
                        + "</merchantOrderAmt>"

                        + "<merchantOrderDesc>"
                        + Upomp_Pay_Info.merchantOrderDesc
                        + "</merchantOrderDesc>"

                        + "<transTimeout>"
                        + Upomp_Pay_Info.transTimeout
                        + "</transTimeout>"

                        + "<backEndUrl>"
                        + Upomp_Pay_Info.backEndUrl
                        + "</backEndUrl>"

                        + "<sign>"
                        + sign
                        + "</sign>"

                        + "<merchantPublicCert>"
                        + Upomp_Pay_Info.merchant_public_cer
                        + "</merchantPublicCert>"+ "</upomp>";

                return SubmitOrder;

            case 3:
			/*
			 * LanchPay：订单验证	
			 */
                lanchpay ="<?xml version=" + "'1.0' "
                        + "encoding=" + "'UTF-8' " + "?>" + "<upomp  application="+"'"+application+".Req' "+"version="+ "'1.0.0' "+">"
                        + "<merchantId>"
                        + Upomp_Pay_Info.merchantId
                        + "</merchantId>"

                        + "<merchantOrderId>"
                        + Upomp_Pay_Info.merchantOrderId
                        + "</merchantOrderId>"

                        + "<merchantOrderTime>"
                        + Upomp_Pay_Info.merchantOrderTime
                        + "</merchantOrderTime>"

                        + "<sign>"
                        + sign
                        + "</sign>"
                        + "</upomp>";

                return lanchpay;

            case 4:
			/*
			 * QueryOrder:订单查询报文
			 */
                QueryOrder = "<?xml version="+"'1.0' "
                        + "encoding=" + "'utf-8' " +"standalone='yes'"+ "?>"+"<upomp  application="+"'"+application+".Req' " +"version="+ "'1.0.0'"+">"
                        +"<transType>"
                        +Upomp_Pay_Info.type[1]
                        +"</transType>"
                        +"<merchantId>"
                        +Upomp_Pay_Info.merchantId
                        +"</merchantId>"
                        +"<merchantOrderId>"
                        +Upomp_Pay_Info.merchantOrderId
                        +"</merchantOrderId>"
                        +"<merchantOrderTime>"
                        +Upomp_Pay_Info.merchantOrderTime
                        +"</merchantOrderTime>"
                        + "<sign>"
                        + sign
                        + "</sign>"
                        + "<merchantPublicCert>"
                        + Upomp_Pay_Info.merchant_public_cer
                        + "</merchantPublicCert>"
                        + "</upomp>";

                return QueryOrder;

            case 6:
			/*
			 * Cancel：订单撤销报文
			 */
                Cancel = "<?xml version="+"'1.0' "
                        + "encoding=" + "'utf-8' " +"standalone='yes'"+ "?>"+"<upomp  application="+"'"+application+".Req' " +"version="+ "'1.0.0'"+">"

                        + "<merchantId>"
                        + Upomp_Pay_Info.merchantId
                        + "</merchantId>"

                        + "<merchantOrderId>"
                        + Upomp_Pay_Info.merchantOrderId
                        + "</merchantOrderId>"

                        + "<merchantOrderTime>"
                        + Upomp_Pay_Info.merchantOrderTime
                        + "</merchantOrderTime>"

                        + "<merchantOrderAmt>"
                        + Upomp_Pay_Info.merchantOrderAmt
                        + "</merchantOrderAmt>"

                        +"<cupsQid>"
                        +Upomp_Pay_Info.cupsQid
                        +"</cupsQid>"

                        + "<backEndUrl>"
                        + Upomp_Pay_Info.backEndUrl
                        + "</backEndUrl>"

                        + "<sign>"
                        + sign
                        + "</sign>"
                        + "<merchantPublicCert>"
                        + Upomp_Pay_Info.merchant_public_cer
                        + "</merchantPublicCert>"

                        + "</upomp>";

                return Cancel;

            case 5:
			/*
			 * Refund：订单退货报文
			 */
                Refund = "<?xml version="+"'1.0' "
                        + "encoding=" + "'utf-8' " +"standalone='yes'"+ "?>"+"<upomp  application="+"'"+application+".Req' " +"version="+ "'1.0.0'"+">"
                        + "<merchantId>"
                        + Upomp_Pay_Info.merchantId
                        + "</merchantId>"

                        + "<merchantOrderId>"
                        + Upomp_Pay_Info.merchantOrderId
                        + "</merchantOrderId>"

                        + "<merchantOrderTime>"
                        + Upomp_Pay_Info.merchantOrderTime
                        + "</merchantOrderTime>"

                        + "<merchantOrderAmt>"
                        + Upomp_Pay_Info.merchantOrderAmt
                        + "</merchantOrderAmt>"

                        +"<cupsQid>"
                        +Upomp_Pay_Info.cupsQid
                        +"</cupsQid>"

                        + "<backEndUrl>"
                        + Upomp_Pay_Info.backEndUrl
                        + "</backEndUrl>"

                        + "<sign>"
                        + sign
                        + "</sign>"
                        + "<merchantPublicCert>"
                        + Upomp_Pay_Info.merchant_public_cer
                        + "</merchantPublicCert>"

                        + "</upomp>";

                return Refund;

            case 9:
			/*
			 * 预授权撤销
			 */
                PreAuthCancel = "<?xml version="+"'1.0' "
                        + "encoding=" + "'utf-8' " +"standalone='yes'"+ "?>"+"<upomp  application="+"'"+application+".Req' " +"version="+ "'1.0.0'"+">"
                        + "<merchantId>"
                        + Upomp_Pay_Info.merchantId
                        + "</merchantId>"

                        + "<merchantOrderId>"
                        + Upomp_Pay_Info.merchantOrderId
                        + "</merchantOrderId>"

                        + "<merchantOrderTime>"
                        + Upomp_Pay_Info.merchantOrderTime
                        + "</merchantOrderTime>"

                        + "<merchantOrderAmt>"
                        + Upomp_Pay_Info.merchantOrderAmt
                        + "</merchantOrderAmt>"

                        +"<cupsQid>"
                        +Upomp_Pay_Info.cupsQid
                        +"</cupsQid>"

                        + "<backEndUrl>"
                        + Upomp_Pay_Info.backEndUrl
                        + "</backEndUrl>"

                        + "<sign>"
                        + sign
                        + "</sign>"
                        + "<merchantPublicCert>"
                        + Upomp_Pay_Info.merchant_public_cer
                        + "</merchantPublicCert>"

                        + "</upomp>";

                return PreAuthCancel;

            case 10:
			/*
			 * 预授权完成
			 */
                PreAuthPay = "<?xml version="+"'1.0' "
                        + "encoding=" + "'utf-8' " +"standalone='yes'"+ "?>"+"<upomp  application="+"'"+application+".Req' " +"version="+ "'1.0.0'"+">"
                        + "<merchantId>"
                        + Upomp_Pay_Info.merchantId
                        + "</merchantId>"

                        + "<merchantOrderId>"
                        + Upomp_Pay_Info.merchantOrderId
                        + "</merchantOrderId>"

                        + "<merchantOrderTime>"
                        + Upomp_Pay_Info.merchantOrderTime
                        + "</merchantOrderTime>"

                        + "<merchantOrderAmt>"
                        + Upomp_Pay_Info.merchantOrderAmt
                        + "</merchantOrderAmt>"

                        +"<cupsQid>"
                        +Upomp_Pay_Info.cupsQid
                        +"</cupsQid>"

                        + "<backEndUrl>"
                        + Upomp_Pay_Info.backEndUrl
                        + "</backEndUrl>"

                        + "<sign>"
                        + sign
                        + "</sign>"
                        + "<merchantPublicCert>"
                        + Upomp_Pay_Info.merchant_public_cer
                        + "</merchantPublicCert>"

                        + "</upomp>";

                return PreAuthPay;

            case 11:
			/*
			 * 预授权完成撤销
			 */
                PreAuthPayCancel = "<?xml version="+"'1.0' "
                        + "encoding=" + "'utf-8' " +"standalone='yes'"+ "?>"+"<upomp  application="+"'"+application+".Req'" +"version="+ "'1.0.0'"+">"
                        + "<merchantId>"
                        + Upomp_Pay_Info.merchantId
                        + "</merchantId>"

                        + "<merchantOrderId>"
                        + Upomp_Pay_Info.merchantOrderId
                        + "</merchantOrderId>"

                        + "<merchantOrderTime>"
                        + Upomp_Pay_Info.merchantOrderTime
                        + "</merchantOrderTime>"

                        + "<merchantOrderAmt>"
                        + Upomp_Pay_Info.merchantOrderAmt
                        + "</merchantOrderAmt>"

                        +"<cupsQid>"
                        +Upomp_Pay_Info.cupsQid
                        +"</cupsQid>"

                        + "<backEndUrl>"
                        + Upomp_Pay_Info.backEndUrl
                        + "</backEndUrl>"

                        + "<sign>"
                        + sign
                        + "</sign>"
                        + "<merchantPublicCert>"
                        + Upomp_Pay_Info.merchant_public_cer
                        + "</merchantPublicCert>"

                        + "</upomp>";

                return PreAuthPayCancel;


        }
        return null;
    }

}