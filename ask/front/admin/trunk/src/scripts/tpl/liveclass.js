/*
 * demo:mongodb例子
 */
$.module("Izb.controller.liveclasstab",function(){

    return new TabListController({
        pty:{
            name : '考前冲刺',
            itemName : '考前冲刺',
            key : 'liveclasstab',
            menu:"liveclass"
        },
        //接口
        action:{
            item:'/liveclass/item',
            add:'/liveclass/additem',
            edit:'/liveclass/edititem',
            del:'/liveclass/delitem'
        },
        //模板Id

        tpl:{
            header:'tpl-liveclasstab-header',
            item:'tpl-liveclasstab-item',
            input:'tpl-liveclasstab-iteminput'
        },
        event:{
            onBeforeList: function (data) {
            },
            onAfterList: function (data) {
            }
        }
    }, {
    });

}(Izb));

$.module("Izb.controller.liveclass",function(){

    return new Controller({
        pty:{
            name : '考前冲刺',
            key : 'liveclass',
            itemName : '考前冲刺'
        },
        //接口
        action:{
            list:'/liveclass/list',
            add:'/liveclass/add',
            edit:'/liveclass/edit',
            del:'/liveclass/del',
            submit:"/liveclass/submit",
            unsubmit:"/liveclass/unsubmit",
            audit:"/liveclass/audit",
            unaudit:"/liveclass/unaudit"
        },
        //模板Id

        tpl:{
            header:'tpl-liveclass-header',
            input:'tpl-liveclass-input',
            content:'tpl-liveclass-list'
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