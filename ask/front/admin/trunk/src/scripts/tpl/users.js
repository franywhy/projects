/*
 * demo:mongodb例子
 */
$.module("Izb.controller.users",function(){

    return new Controller({
        pty:{
            name : '用户列表',
            key : 'users',
            itemName : '用户列表'
        },
        //接口
        action:{
            list:'/users/list',
            add:"/users/add",
            edit:"/users/edit",
            freeze:"/users/freeze"
        },
        //模板Id

        tpl:{
            header:'tpl-users-header',
            content:'tpl-users-list',
            input:'tpl-users-input'
        },
        event:{
            onBeforeList: function (data) {
            },
            onBeforeAdd:function(data){
                $("#_id").attr({"disabled":true});
                $("#nc_id").attr({"disabled":true});
                $("#vip_flag").attr({"disabled":true});
                $("#vip_validity").attr({"disabled":true});
                $("#update_time").attr({"disabled":true});
                $("#create_time").attr({"disabled":true});
                $("#last_login").attr({"disabled":true});
                $("#syn_time").attr({"disabled":true});
                $("#status").attr({"disabled":true});
            },
            onAdd:function(){
                $("#_id").attr({"disabled":true});
                $("#nc_id").attr({"disabled":true});
                $("#vip_flag").attr({"disabled":true});
                $("#vip_validity").attr({"disabled":true});
                $("#update_time").attr({"disabled":true});
                $("#create_time").attr({"disabled":true});
                $("#last_login").attr({"disabled":true});
                $("#syn_time").attr({"disabled":true});
                $("#status").attr({"disabled":true});
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            },
            onAfterEdit:function(data,dialog){
                $("#_id").attr({"disabled":true});
                $("#nc_id").attr({"disabled":true});
                $("#sex").attr("disabled","disabled");
                $("#telephone").attr({"disabled":true});
                $("#email").attr({"disabled":true});
                $("#vip_flag").attr({"disabled":true});
                $("#vip_validity").attr({"disabled":true});
                $("#cardnumber").attr({"disabled":true});
                $("#qq_number").attr({"disabled":true});
                $("#school_code").attr({"disabled":true});
                $("#status").attr({"disabled":true});
                $("#birthday").attr({"disabled":true});
                $("#update_time").attr({"disabled":true});
                $("#create_time").attr({"disabled":true});
                $("#last_login").attr({"disabled":true});
                $("#syn_time").attr({"disabled":true});
            }
        }
    }, {
    });

}(Izb));