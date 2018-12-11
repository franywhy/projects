/*
 * demo:mongodb例子
 */
$.module("Izb.controller.orders",function(){

    return new Controller({
        pty:{
            name : '订单列表',
            key : 'orders',
            itemName : '订单列表'
        },
        //接口
        action:{
            list:'/orders/list'
        },
        //模板Id

        tpl:{
            header:'tpl-orders-header',
            input:'tpl-orders-input',
            content:'tpl-orders-list'
        },
        event:{
            onBeforeList: function (data) {
            },
            onBeforeAdd:function(data){
            },
            onAdd:function(){
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            },
            onAfterEdit:function(data,dialog){
            },
            onBeforeShow:function(data,dialog){
                if(data){
                    var payOnLineType = data.payOnLineType;
                    var isCancel = data.isCancel;
                    var dr = data.dr;
                    var payStatus = data.payStatus;
                    var status = data.status;
                    var payType = data.payType;
                    var preferenceScheme = data.preferenceScheme;
                    var productType = data.productType;
                    var createTime = data.createTime;
                    var modifiedtime = data.modifiedtime;
                    var payCallblackTime = data.payCallblackTime;
                    var payTime = data.payTime;
                    var validity = data.validity;
                    if(createTime){
                        createTime = new Date(createTime).toLocaleString();
                        $("#createTime").val(createTime);
                    }
                    if(modifiedtime){
                        modifiedtime = new Date(modifiedtime).toLocaleString();
                        $("#modifiedtime").val(modifiedtime);
                    }
                    if(payCallblackTime){
                        payCallblackTime = new Date(payCallblackTime).toLocaleString();
                        $("#createTime").val(payCallblackTime);
                    }
                    if(payTime){
                        payTime = new Date(payTime).toLocaleString();
                        $("#createTime").val(payTime);
                    }
                    if(validity){
                        validity = new Date(validity).toLocaleString();
                        $("#validity").val(validity);
                    }
                    if(isCancel == 0){
                        $("#isCancel").val("否");
                    }else if(isCancel == 1){
                        $("#isCancel").val("是");
                    }
                    if(payOnLineType == 0){
                        $("#payOnLineType").val("无");
                    }else if(payOnLineType == 1){
                        $("#payOnLineType").val("支付宝PC");
                    }else if(payOnLineType == 2){
                        $("#payOnLineType").val("微信PC");
                    }else if(payOnLineType == 3){
                        $("#payOnLineType").val("快钱PC");
                    }else if(payOnLineType == 4){
                        $("#payOnLineType").val("支付宝网银PC");
                    }else if(payOnLineType == 11){
                        $("#payOnLineType").val("支付宝H5");
                    }else if(payOnLineType == 12){
                        $("#payOnLineType").val("微信H5");
                    }else if(payOnLineType == 13){
                        $("#payOnLineType").val("快钱H5");
                    }
                    if(payStatus == 0){
                        $("#payStatus").val("未支付");
                    }else if(payStatus == 1){
                        $("#payStatus").val("待支付");
                    }else if(payStatus == 2){
                        $("#payStatus").val("支付成功");
                    }
                    if(payType == 0){
                        $("#payType").val("在线支付");
                    }else if(payType == 1){
                        $("#payType").val("分期付款");
                    }
                    if(preferenceScheme == 0){
                        $("#preferenceScheme").val("不使用优惠  ");
                    }else if(preferenceScheme == 1){
                        $("#preferenceScheme").val("积分");
                    }else if(preferenceScheme == 2){
                        $("#preferenceScheme").val("优惠券");
                    }
                    if(productType == 0){
                        $("#productType").val("vip");
                    }else if(productType == 1){
                        $("#productType").val("商品");
                    }
                    if(status == 0){
                        $("#status").val("有效");
                    }else if(status == 1){
                        $("#status").val("取消");
                    }
                    if(dr == 0){
                        $("#dr").val("否");
                    }else if(dr == 1){
                        $("#dr").val("是");
                    }
                }
            }
        }
    }, {
        });

}(Izb));