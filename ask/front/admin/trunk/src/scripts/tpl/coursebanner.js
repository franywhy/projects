/*
 * demo:mongodb例子
 */
$.module("Izb.controller.coursebanner",function(){

    return new Controller({
        pty:{
            name : '课程列表banner管理',
            key : 'coursebanner',
            itemName : '课程列表banner管理'
        },
        //接口
        action:{
            list:'/coursebanner/list',
            add:'/coursebanner/add',
            edit:'/coursebanner/edit',
            del:'/coursebanner/del'
        },
        //模板Id

        tpl:{
            header:'tpl-coursebanner-header',
            input:'tpl-coursebanner-input',
            content:'tpl-coursebanner-list'
        },
        event:{
            onBeforeList: function (data) {
            },
            onBeforeAdd:function(data){
            },
            onAddRender:function(){
                $("#is_default_0").attr({"checked":true});
                $("#create_user_id").attr({"disabled":true});
                $("#create_time").attr({"disabled":true});
                $("#update_user_id").attr({"disabled":true});
                $("#update_time").attr({"disabled":true});
            },
            onAfterAdd:function(result,data){
                if(result.fail == 1){
                    Izb.ui.confirm("已经有banner处于默认状态了，是否以当前banner作为当前默认状态？", function (){
                        data.is_default=2;
                        Izb.common.getResult({
                            type: 'POST',
                            action: '/coursebanner/add',
                            data: data,
                            success: function (result) {
                                Izb.main.refresh();
                            },
                            error: function (xhr, status, result) {
                                Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                            }
                        });
                    },"信息提示")
                }
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            },
            onEditRender:function(data){
                $("#pc_is_show").val(data.pc_is_show);
                $("#app_is_show").val(data.app_is_show);
                $("#create_user_id").attr({"disabled":true});
                $("#create_time").attr({"disabled":true});
                $("#update_user_id").attr({"disabled":true});
                $("#update_time").attr({"disabled":true});
                if(data){
                    var is_default=data.is_default;
                    if(is_default == 1){
                        $("#is_default_1").attr({"checked":true});
                    }
                }
            },
            onAfterEdit:function(result,data){
                if(result.fail == 1){
                    Izb.ui.confirm("已经有banner处于默认状态了，是否以当前banner作为当前默认状态？", function (){
                        data.is_default=2;
                        Izb.common.getResult({
                            type: 'POST',
                            action: '/coursebanner/edit',
                            data: data,
                            success: function (result) {
                                Izb.main.refresh();
                            },
                            error: function (xhr, status, result) {
                                Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                            }
                        });
                    },"信息提示")
                }
            }
        }
    }, {
        });

}(Izb));