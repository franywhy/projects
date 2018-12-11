/*
 * demo:mongodb例子
 */
$.module("Izb.controller.classplan",function(){

    return new Controller({
        pty:{
            name : '排课计划',
            key : 'classplan',
            itemName : '排课计划'
        },
        //接口
        action:{
            list:'/classplan/list',
            course:'/classplan/course',
            student:'/classplan/student'
        },
        //模板Id

        tpl:{
            header:'tpl-classplan-header',
            courselist:'tpl-classplan-courselist',
            studentlist:'tpl-classplan-studentlist',
            content:'tpl-classplan-list'
        },
        event:{
            onBeforeList: function (data) {

                Izb.common.getResult({
                    action: "/classplan/getarea",
                    data: data,
                    success: function (result) {
                        if(result != null) {
                            area = result.data;
                            $("#nc_area_id").html('<option value ="">请选择校区</option>')
                            for(var index in area) {
                                var html = $("#nc_area_id").html();
                                $("#nc_area_id").html(html + '<option value ="'+area[index].code+'">'+area[index].name+'</option>');
                            }
                        }
                    },
                    error: function (xhr, status, result) {
                    }
                });
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
        //课程信息
        course: function (nc_id) {
            if (!this.action.course || !this.tpl.courselist) {
                return;
            }
            var that = this,
                title = "课程信息";
            if (!nc_id) {
                return false;
            }
            var data,$form ;
            Izb.common.getResult({
                action: that.action.course,
                async: false,
                data: { nc_id: nc_id,page:1,size:100},
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

            var dialog = Izb.ui.showDialogByTpl(this.tpl.courselist, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

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
        },
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