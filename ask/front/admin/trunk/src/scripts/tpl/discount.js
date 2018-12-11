/*
 * demo:mongodb例子
 */
$.module("Izb.controller.discount",function(){

    return new Controller({
        pty:{
            name : '优惠券管理',
            key : 'discount',
            itemName : '优惠券管理'
        },
        //接口
        action:{
            list:'/discount/list',
            add:'/discount/add',
            edit:'/discount/edit',
            del:'/discount/del',
            stop:'/discount/stop'
        },
        //模板Id

        tpl:{
            header:'tpl-discount-header',
            input:'tpl-discount-input',
            content:'tpl-discount-list'
        },
        event:{
            onBeforeList: function (data) {
            },
            onBeforeAdd:function(data){
            },
            onAddRender:function(){
                $("#workType").attr({"disabled":true});
                $("#workType").css("color","#ccc");
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            },
            onEditRender:function(data){
                $("#workType").attr({"disabled":true});
                $("#workType").css("color","#ccc");
                //1.限制平台不可选2.设置时间类型的联动性
                var timeType = data.timeType;
                var type = data.type;
                var validDays = data.validDays;
                var workType = data.workType;
                var startTime = data.startTime;
                var endTime = data.endTime;
                if(startTime){
                    startTime = new Date(startTime).toLocaleString();
                }
                if(endTime){
                    endTime = new Date(endTime).toLocaleString();
                }
                $("#startTime").val(startTime);
                $("#endTime").val(endTime);
                if(timeType == 1){
                    $("input:radio[name='timeType']").eq(1).attr("checked",true);
                }else{
                    $("input:radio[name='timeType']").eq(0).attr("checked",true);
                }
                if(type == 1){
                    $("input:radio[name='type']").eq(1).attr("checked",true);
                }else{
                    $("input:radio[name='type']").eq(0).attr("checked",true);
                }
                if(validDays != null){
                    switch(validDays)
                    {
                        case 30:
                            $("#validDays").find("option[text='30天']").attr("selected",true);
                            break;
                        case 90:
                            $("#validDays").find("option[text='90天']").attr("selected",true);
                            break;
                        case 180:
                            $("#validDays").find("option[text='1800天']").attr("selected",true);
                            break;
                        case 365:
                            $("#validDays").find("option[text='1年']").attr("selected",true);
                            break;
                        default:
                            $("#validDays").find("option[text='无']").attr("selected",true);
                    }
                }
                if(workType != null){
                    switch(workType)
                    {
                        case 0:
                            $("#workType").find("option[text='不限制']").attr("selected",true);
                            break;
                        case 1:
                            $("#workType").find("option[text='恒企在线']").attr("selected",true);
                            break;
                        case 1:
                            $("#workType").find("option[text='会答APP']").attr("selected",true);
                            break;
                        default:
                            $("#workType").find("option[text='不限制']").attr("selected",true);
                    }
                }
            },
            onAfterAdd:function(data){
            },
            onAfterEdit:function(data){
            }
        }
    }, {
        //改变时间类型
        ChangeRadio: function (that) {
            var time_type=$("input:radio[name='timeType']:checked").val() ;
            if(time_type == 0){
                $("#validDays").val("");
                $("#validDays").attr({"disabled":true});
                $("#startTime").removeAttr('disabled');
                $("#endTime").removeAttr('disabled');
            }else{
                $("#startTime").val("");
                $("#endTime").val("");
                $("#startTime").attr({"disabled":true});
                $("#endTime").attr({"disabled":true});
                $("#validDays").removeAttr('disabled');
            }
        },
        //冻结
        stop: function (_id,status) {
            if (!this.action.stop || !_id) {
                return;
            }
            if (!this.checkInputLimits()) {
                return;
            }
            var that = this,
                title = "确定要停用该优惠券？";
            if(status == null || status == "" || status == "null"){
                status = "0";
                title = "确定要停用该优惠券？";
            }else if(status == 1 || status == "1"){
                title = "确定要恢复该优惠券？";
            }
            Izb.ui.confirm(title, function () {
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.stop,
                    data: { id: _id ,status:status},
                    success: function (result) {
                        Izb.main.refresh();

                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });
        }
        });

}(Izb));