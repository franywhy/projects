/*
 * demo:mongodb例子
 */
$.module("Izb.controller.signs",function(){

    return new Controller({
        pty:{
            name : '报名表',
            key : 'signs',
            itemName : '报名表'
        },
        //接口
        action:{
            list:'/signs/list'
        },
        //模板Id

        tpl:{
            header:'tpl-signs-header',
            input:'tpl-signs-input',
            content:'tpl-signs-list'
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