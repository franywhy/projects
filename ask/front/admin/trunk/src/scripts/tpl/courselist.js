/*
 * demo:mongodb例子
 */
$.module("Izb.controller.courselist",function(){

    return new Controller({
        pty:{
            name : '课程列表',
            key : 'courselist',
            itemName : '课程列表'
        },
        //接口
        action:{
            list:'/courselist/list',
            edit:'/courselist/edit',
            del:'/courselist/del'
       },
        //模板Id

        tpl:{
            header:'tpl-courselist-header',
            input:'tpl-courselist-input',
            content:'tpl-courselist-list'
        },
        event:{
            onBeforeList: function (data) {
            },
            onBeforeAdd:function(data){
            },
            onAddRender:function(){

           },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            },
            onEditRender:function(data){
                $("#nc_id").attr({"disabled":true});
                $("#nc_name").attr({"disabled":true});
                $("#sort").attr({"disabled":true});
                $("#course_code").attr({"disabled":true});
                $("#update_user_id").attr({"disabled":true});
                $("#update_time").attr({"disabled":true});
           },
            onAfterEdit:function(data,dialog){
            }
        }
    }, {

        });

}(Izb));