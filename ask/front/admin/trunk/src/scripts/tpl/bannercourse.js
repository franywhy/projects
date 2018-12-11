/*
 * bannercourse
 */
$.module("Izb.controller.bannercourse",function(){

    return new Controller({
        pty:{
            name : '首页课程banner管理',
            key : 'bannercourse',
            itemName : '首页课程banner管理'
        },
        //接口
        action:{
            list:'/bannercourse/list',
            add:'/bannercourse/add',
            edit:'/bannercourse/edit',
            del:'/bannercourse/del'
        },
        //模板Id

        tpl:{
            header:'tpl-bannercourse-header',
            input:'tpl-bannercourse-input',
            content:'tpl-bannercourse-list'
        },
        event:{
        }
    }, {});

}(Izb));