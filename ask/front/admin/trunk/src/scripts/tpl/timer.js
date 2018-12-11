/*
 * 定时器
 */
$.module("Izb.controller.timer",function(){

    return new Controller({
        pty:{
            name : '定时器',
            key : 'timer',
            itemName : '定时器'
        },
        //接口
        action:{
            list:'/timer/list'
        },
        //模板Id

        tpl:{
            header:'tpl-timer-header',
            content:'tpl-timer-list'
        }
    },{
        timerM : function(name , url , isAsyn){
            var tips_msg = "您确认调用【<font color='#FF0000' size='5'>" +name + "</font>】服务吗?";
            if(isAsyn){
                tips_msg += "<br/><br/><font color='#FF0000'>该服务为异步线程,启动后会有一定延时,请您耐心等待!</font>"
            }
            Izb.ui.confirm(tips_msg ,function(){
                Izb.common.getResult({
                    type: 'POST',
                    action:url,
                    success:function(result){
                        Izb.ui.tips("操作成功！",'succeed');
//                        Izb.main.refresh();
                    }
                });
            })
        }
    });

}(Izb));