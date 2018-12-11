/*
 * demo:mongodb例子
 */
$.module("Izb.controller.schoolpic",function(){

    return new Controller({
        pty:{
            name : '校区图片管理',
            key : 'schoolpic',
            itemName : '校区图片管理'
        },
        //接口
        action:{
            list:'/schoolpic/list',
            add:'/schoolpic/add',
            edit:'/schoolpic/edit',
            del:'/schoolpic/del'
        },
        //模板Id

        tpl:{
            header:'tpl-schoolpic-header',
            input:'tpl-schoolpic-input',
            content:'tpl-schoolpic-list'
        },
        event:{
            onBeforeList: function (data) {
                Izb.common.getResult({
                    action: "/schoolpic/getprovince",
                    data: data,
                    success: function (result) {
                        if(result != null) {
                            province = result.data;
                            for(var index in province) {
                                var provincehtml = $("#province").html();
                                $("#province").html(provincehtml + '<option value="'+province[index].code+'">'+province[index].name+'</option>');
                            }
                        }
                    },
                    error: function (xhr, status, result) {
                    }
                });
            },
            onBeforeAdd:function(data){


            },
            onAddRender:function(){
                Izb.ui.initschoolselect();
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
                Izb.ui.initschoolselect_edit(data.school_code);
            },
            onAfterAdd:function(data){
                if(data.msg){
                    Izb.ui.alert(data.msg, "信息提示")
                }
            },
            onAfterEdit:function(data){
                if(data.msg){
                    Izb.ui.alert(data.msg, "信息提示")
                }
            }
        }
    }, {

    });

}(Izb));