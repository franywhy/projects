/*
 * banner
 */
$.module("Izb.controller.banner",function(){

    return new Controller({
        pty:{
            name : '首页banner管理',
            key : 'banner',
            itemName : '首页banner管理'
        },
        //接口
        action:{
            list:'/banner/list',
            add:'/banner/add',
            edit:'/banner/edit',
            del:'/banner/del'
        },
        //模板Id

        tpl:{
            header:'tpl-banner-header',
            input:'tpl-banner-input',
            content:'tpl-banner-list'
        },
        event:{
        }
    }, {});

}(Izb));