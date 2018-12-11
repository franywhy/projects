/*
 * demo:mongodb例子
 */
$.module("Izb.controller.attendances",function(){

    return new Controller({
        pty:{
            name : '考勤表',
            key : 'attendances',
            itemName : '考勤表'
        },
        //接口
        action:{
            list:'/attendances/list',
            student:'/attendances/student'
        },
        //模板Id

        tpl:{
            header:'tpl-attendances-header',
            input:'tpl-attendances-input',
            content:'tpl-attendances-list',
            studentlist:'tpl-attendances-studentlist'
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
        //学生信息
        student: function (nc_id) {
            if (!this.action.student || !this.tpl.studentlist) {
                return;
            }
            var that = this,
                title = "学生信息";
            if (!nc_id) {
                return false;
            }
            var data,$form ;
            Izb.common.getResult({
                action: that.action.student,
                async: false,
                data: { nc_id: nc_id,page:1,size:500},
                success: function (result) {
                    data = result.data;
                },
                error: function (xhr, status, result) {
                    Izb.ui.renderContent(result.msg || '网络异常，请刷新后重试！');
                    //Izb.ui.renderContent('网络异常，请刷新后重试！');
                    //Izb.ui.renderContent($("#"+that.tpl.content).html());
                }
            });
            Izb.core.out(data);

            var dialog = Izb.ui.showDialogByTpl(this.tpl.studentlist, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

                dialog.close();

            });

            if ($.isFunction(that.event.onBeforeEdit)) {
                that.event.onBeforeEdit(data, dialog);
            }

            Izb.ui.setFormData("inputForm", data);

            if ($.isFunction(that.event.onEditRender)) {
                that.event.onEditRender();
            }

            if ($.isFunction(that.event.onAfterEdit)) {
                that.event.onAfterEdit(data, dialog);
            }

            //初始化验证
            $form = $('form[name=courseinputForm]');

            //if (!$.isFunction(that.event.onBindValidate)) {
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}
        }
        });

}(Izb));