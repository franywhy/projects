/*
 * demo:mongodb例子
 */
$.module("Izb.controller.mysql",function(){

    return new Controller({
        pty:{
            name : 'mysql例子',
            key : 'mysql',
            itemName : 'mysql例子'
        },
        //接口
        action:{
            list:'/mysql/list',
            add:"/mysql/add",
            edit:"/mysql/edit",
            del:"/mysql/del"
        },
        //模板Id

        tpl:{
            header:'tpl-mysql-header',
            content:'tpl-mysql-list',
            input:'tpl-mysql-input'
        },
        event:{

        }
    });
}(Izb));