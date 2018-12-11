/*
 * demo:mongodb例子
 */
$.module("Izb.controller.scoredetail",function(){

    return new Controller({
        pty:{
            name : '积分明细',
            key : 'scoredetail',
            itemName : '积分明细'
        },
        //接口
        action:{
            list:'/scoredetail/list'
        },
        //模板Id

        tpl:{
            header:'tpl-scoredetail-header',
            input:'tpl-scoredetail-input',
            content:'tpl-scoredetail-list'
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
                    var scoreGainType = data.scoreGainType;
                    var scoreType = data.scoreType;
                    var createTime = data.createTime;
                    if(createTime){
                        createTime = new Date(createTime).toLocaleString();
                        $("#createTime").val(createTime);
                    }
                    if(scoreGainType == 0){
                        $("#scoreGainType").val("默认");
                    }else if(scoreGainType == 1){
                        $("#scoreGainType").val("首次赠送");
                    }else if(scoreGainType == 2){
                        $("#scoreGainType").val("每天一次");
                    }else if(scoreGainType == 3){
                        $("#scoreGainType").val("消耗积分");
                    }
                    if(scoreType == 0){
                        $("#scoreType").val("上传头像");
                    }else if(scoreType == 1){
                        $("#scoreType").val("绑定邮箱");
                    }else if(scoreType == 2){
                        $("#scoreType").val("完成职业测试");
                    }else if(scoreType == 3){
                        $("#scoreType").val("完善所有信息");
                    }else if(scoreType == 4){
                        $("#scoreType").val("每日签到登录");
                    }else if(scoreType == 5){
                        $("#scoreType").val("分享链接");
                    }else if(scoreType == 6){
                        $("#scoreType").val("邀请朋友注册");
                    }else if(scoreType == 7){
                        $("#scoreType").val("购买商品消耗积分");
                    }else if(scoreType == 8){
                        $("#scoreType").val("在兑吧消费");
                    }else if(scoreType == 9){
                        $("#scoreType").val("兑吧消费失败积分退还");
                    }else if(scoreType == 10){
                        $("#scoreType").val("购买商品赠送积分");
                    }else if(scoreType == 11){
                        $("#scoreType").val("订单取消积分返还");
                    }else if(scoreType == 12){
                        $("#scoreType").val("会答提问");
                    }else if(scoreType == 13){
                        $("#scoreType").val("打卡考勤");
                    }else if(scoreType == 14){
                        $("#scoreType").val("课程试听");
                    }
                }
            }
        }
    }, {
        });

}(Izb));