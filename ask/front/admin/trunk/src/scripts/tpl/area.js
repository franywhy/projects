/*
 * demo:mongodb例子
 */
$.module("Izb.controller.area",function(){

    return new Controller({
        pty:{
            name : '校区列表',
            key : 'area',
            itemName : '校区列表'
        },
        //接口
        action:{
            list:'/area/list'
        },
        //模板Id

        tpl:{
            header:'tpl-area-header',
            content:'tpl-area-list',
            input:'tpl-area-input'
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
            }
        }
    }, {
    });

}(Izb));