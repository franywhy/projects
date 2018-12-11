/*
 * demo:mongodb例子
 */
$.module("Izb.controller.ncteacher",function(){

    return new Controller({
        pty:{
            name : '教师列表',
            key : 'ncteacher',
            itemName : '教师列表'
        },
        //接口
        action:{
            list:'/ncteacher/list',
            edit:"/ncteacher/edit",
            freeze:"/ncteacher/freeze"
        },
        //模板Id

        tpl:{
            header:'tpl-ncteacher-header',
            content:'tpl-ncteacher-list',
            input:'tpl-ncteacher-input'
        },
        event:{
            onBeforeList: function (data) {
            },
            onBeforeAdd:function(data){
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            },
            onAfterEdit:function(data,dialog){
                $("#name").attr({"disabled":true});
                $("#nc_id").attr({"disabled":true});
                $("#sex").attr({"disabled":true});
                $("#telephone").attr({"disabled":true});
                $("#email").attr({"disabled":true});
                $("#school_code").attr({"disabled":true});
                $("#status").attr({"disabled":true});
                $("#create_time").attr({"disabled":true});
                $("#last_login").attr({"disabled":true});
                $("#syn_time").attr({"disabled":true});
            }
        }
    }, {
    });

}(Izb));