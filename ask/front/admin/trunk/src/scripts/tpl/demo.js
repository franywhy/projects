/*
 * demo:mongodb例子
 */
$.module("Izb.controller.demods",function(){

    return new TabListController({
        pty:{
            name : '用户详情',
            itemName : '用户',
            key : 'demods',
            menu:"demo"
        },
        //接口
        action:{
            teams:'/demo/teams',
            duiwu:"/demo/duiwu"
        },
        //模板Id

        tpl:{
            header:'tpl-demods-header',
            teams:'tpl-demods-teams',
            duiwu:'tpl-demods-duiwu'
        },
        event:{
        }
    }, {
    });

}(Izb));




$.module("Izb.controller.demo",function(){

    return new Controller({
        pty:{
            name : 'mongodb例子',
            key : 'demo',
            itemName : 'mongodb例子'
        },
        //接口
        action:{
            list:'/demo/list',
            add:"/demo/add",
            edit:"/demo/edit",
            del:"/demo/del"
        },
        //模板Id

        tpl:{
            header:'tpl-demo-header',
            list:'tpl-demo-list',
            content:'tpl-demo-list',
            input:'tpl-demo-input'
        },
        event:{
            onBeforeList: function (data) {

                if (!data.stime) {
                    data.stime = Izb.ui.getToday();
                    $("#stime").val(data.stime);
                }

                if (!data.etime) {
                    data.etime = Izb.ui.getTodayEnd();
                    $("#etime").val(data.etime);
                }
            },
            onBeforeAdd:function(data){
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            }
        }
    }, {
    });

}(Izb));