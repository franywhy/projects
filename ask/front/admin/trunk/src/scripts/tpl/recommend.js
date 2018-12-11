/*
 * demo:mongodb例子
 */
$.module("Izb.controller.recommend",function(){

    return new Controller({
        pty:{
            name : '内容管理',
            key : 'recommend',
            itemName : '内容管理'
        },
        //接口
        action:{
            list:'/recommend/list',
            del:'/recommend/del',
            addarticle:'/recommend/addarticle',
            addtkarticle:'/recommend/addtkarticle',
            addvoice:'/recommend/addvoice',
            editarticle:'/recommend/editarticle',
            edittkarticle:'/recommend/edittkarticle',
            editvoice:'/recommend/editvoice',
            editvideo:'/recommend/editvideo',
            addvideo:'/recommend/addvideo',
            recommend:'/recommend/recommend',
            tk_article_type:'/recommend/tk_article_type'
        },
        //模板Id

        tpl:{
            header:'tpl-recommend-header',
            articleinput:'tpl-recommend-articleinput',
            tkarticleinput:'tpl-recommend-tkarticleinput',
            voiceinput:'tpl-recommend-voiceinput',
            videoinput:'tpl-recommend-videoinput',
            content:'tpl-recommend-list'
        },
        event:{
            onBeforeList: function (data) {
            },
            onAfterList: function (data) {
            },
            onBeforeAdd:function(data){
            },
            onAdd:function(){
            },
            onAddRender:function(){
            },
            onBeforeSaveEdit:function(data){
            },
            onBeforeEdit:function(data,dialog){
            },
            onEditRender:function(){
            },
            onAfterEdit:function(data,dialog){
            }
        }
    }, {
        //关联类型
        inittkarticelType:function( tk_article_type_name){
            Izb.common.getResult({
                type: 'GET',
                action:this.action.tk_article_type,
                data: {"page" : 1 , "size" : 100},
                success: function (result) {
                    var $liveRoomSelect = $($("select[name='tk_article_type_name']")[0]);
                    //清空
                    $liveRoomSelect.empty();
                    var option;
                    if(tk_article_type_name)
                    {
                        option = $("<option data-live-id=''>").val(tk_article_type_name).text(tk_article_type_name);
                    }
                    else
                    {
                        option = $("<option data-live-id=''>").val("").text("请选择");
                    }

                    $liveRoomSelect.append(option);
                    //
                    var data = result.data;
                    if(data != null && data.length > 0){
                        $.each(data , function(i , d){
                            var option = $("<option data-live-id='" + d._id + "'>").val(d.type_name).text(d.type_name );
                            $liveRoomSelect.append(option);
                        });
                    }

                }
            });
//            //初始化数据
//            var data = that.get(_id);
//            Izb.ui.setFormData("live_inputForm", data);


        },
        //添加文章
        addarticle: function () {
            if (!this.action.addarticle || !this.tpl.articleinput) {
                return;
            }

            var that = this,
                title = "添加文章",
                $form,editor;

            var dialog = Izb.ui.showDialogByTpl(this.tpl.articleinput, title, { params: that.hashParams, data: {} }, function () {

                //表单校验
                if (!$form.valid()) {
                    return false;
                }

                /*//验证
                 if ($.fn.formValidate && !$('form[name=' + that.formName + ']').formValidate()) {
                 return false;
                 }*/

                var data = Izb.ui.getFormData("inputForm");
                if ($.isFunction(that.event.onBeforeAdd)) {
                    that.event.onBeforeAdd(data);
                }
                var htmlcontent = editor.html();
                data.recommend_info = htmlcontent;


                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.addarticle,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                        if ($.isFunction(that.event.onAfterAdd)) {
                            that.event.onAfterAdd(result);
                        }
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });

            editor = KindEditor.create('#recommend_info',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                this.loadPlugin('autoheight');
            }});

            $form = $('form[name=' + that.formName + ']');

            //if(!$.isFunction(that.event.onBindValidate)){
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}

        },
        //添加题库文章
        addtkarticle: function () {
            if (!this.action.addtkarticle || !this.tpl.tkarticleinput) {
                return;
            }
            Izb.controller.recommend.inittkarticelType();
            var that = this,
                title = "添加题库知识点文章",
                $form,editor;

            var dialog = Izb.ui.showDialogByTpl(this.tpl.tkarticleinput, title, { params: that.hashParams, data: {} }, function () {

                //表单校验
                if (!$form.valid()) {
                    return false;
                }

                /*//验证
                 if ($.fn.formValidate && !$('form[name=' + that.formName + ']').formValidate()) {
                 return false;
                 }*/

                var data = Izb.ui.getFormData("inputForm");
                if ($.isFunction(that.event.onBeforeAdd)) {
                    that.event.onBeforeAdd(data);
                }
                var htmlcontent = editor.html();
                data.recommend_info = htmlcontent;


                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.addtkarticle,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                        if ($.isFunction(that.event.onAfterAdd)) {
                            that.event.onAfterAdd(result);
                        }
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });

            editor = KindEditor.create('#recommend_info',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                this.loadPlugin('autoheight');
            }});

            $form = $('form[name=' + that.formName + ']');

            //if(!$.isFunction(that.event.onBindValidate)){
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}

        },
        //添加语音
        addvoice: function () {
            if (!this.action.addvoice || !this.tpl.voiceinput) {
                return;
            }

            var that = this,
                title = "添加语音",
                $form , editor;

            var dialog = Izb.ui.showDialogByTpl(this.tpl.voiceinput, title, { params: that.hashParams, data: {} }, function () {

                //表单校验
                if (!$form.valid()) {
                    return false;
                }

                /*//验证
                 if ($.fn.formValidate && !$('form[name=' + that.formName + ']').formValidate()) {
                 return false;
                 }*/

                var data = Izb.ui.getFormData("inputForm");
                if ($.isFunction(that.event.onBeforeAdd)) {
                    that.event.onBeforeAdd(data);
                }

                var htmlcontent = editor.html();
                data.recommend_html = htmlcontent;

                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.addvoice,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                        if ($.isFunction(that.event.onAfterAdd)) {
                            that.event.onAfterAdd(result);
                        }
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });

            editor = KindEditor.create('#recommend_html',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                this.loadPlugin('autoheight');
            }});

            $form = $('form[name=' + that.formName + ']');

            //if(!$.isFunction(that.event.onBindValidate)){
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}

        },
        //编辑文章
        editarticle: function (id) {
            if (!this.action.editarticle || !this.tpl.articleinput) {
                return;
            }
            var that = this,
                title = "修改文章",editor;
            if (!id) {
                return false;
            }
            var data = this.get(id), $form;
            Izb.core.out(data);

            var dialog = Izb.ui.showDialogByTpl(this.tpl.articleinput, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

                //验证
                if (!$form.valid()) {
                    return false;
                }

                var data = Izb.ui.getFormData(that.formName);

                if ($.isFunction(that.event.onBeforeSaveEdit)) {
                    that.event.onBeforeSaveEdit(data);
                }
                var htmlcontent = editor.html();
                data.recommend_info = htmlcontent;

                Izb.core.out(data);
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.editarticle,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });

            var currentEditCallback = data.recommend_info;
            editor = KindEditor.create('#recommend_info',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                this.loadPlugin('autoheight');
            }});
            editor.html(currentEditCallback);

            Izb.ui.setFormData(that.formName, data);

            if ($.isFunction(that.event.onEditRender)) {
                that.event.onEditRender();
            }

            //初始化验证
            $form = $('form[name=' + that.formName + ']');

            //if (!$.isFunction(that.event.onBindValidate)) {
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}
        },
        //编辑题库文章
        edittkarticle: function (id) {
            if (!this.action.edittkarticle || !this.tpl.tkarticleinput) {
                return;
            }
            var that = this,
                title = "修改题库知识点文章",editor;
            if (!id) {
                return false;
            }

            var data = this.get(id), $form;
            Izb.core.out(data);
            Izb.controller.recommend.inittkarticelType(data.tk_article_type_name);
            var dialog = Izb.ui.showDialogByTpl(this.tpl.tkarticleinput, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

                //验证
                if (!$form.valid()) {
                    return false;
                }

                var data = Izb.ui.getFormData(that.formName);

                if ($.isFunction(that.event.onBeforeSaveEdit)) {
                    that.event.onBeforeSaveEdit(data);
                }
                var htmlcontent = editor.html();
                data.recommend_info = htmlcontent;

                Izb.core.out(data);
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.edittkarticle,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });

            var currentEditCallback = data.recommend_info;
            editor = KindEditor.create('#recommend_info',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                this.loadPlugin('autoheight');
            }});
            editor.html(currentEditCallback);

            Izb.ui.setFormData(that.formName, data);

            if ($.isFunction(that.event.onEditRender)) {
                that.event.onEditRender();
            }

            //初始化验证
            $form = $('form[name=' + that.formName + ']');

            //if (!$.isFunction(that.event.onBindValidate)) {
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}
        },
        //编辑语音
        editvoice: function (id) {
            if (!this.action.editvoice || !this.tpl.voiceinput) {
                return;
            }
            var that = this,
                title = "修改语音" , editor;
            if (!id) {
                return false;
            }
            var data = this.get(id), $form;
            Izb.core.out(data);

            var dialog = Izb.ui.showDialogByTpl(this.tpl.voiceinput, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

                //验证
                if (!$form.valid()) {
                    return false;
                }

                var data = Izb.ui.getFormData(that.formName);



                if ($.isFunction(that.event.onBeforeSaveEdit)) {
                    that.event.onBeforeSaveEdit(data);
                }
                var htmlcontent = editor.html();
                data.recommend_html = htmlcontent;

                Izb.core.out(data);
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.editvoice,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });


            });


            var currentEditCallback = data.recommend_html;
            editor = KindEditor.create('#recommend_html',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                this.loadPlugin('autoheight');
            }});
            editor.html(currentEditCallback);

            Izb.ui.setFormData(that.formName, data);

            if ($.isFunction(that.event.onEditRender)) {
                that.event.onEditRender();
            }

            //初始化验证
            $form = $('form[name=' + that.formName + ']');

            //if (!$.isFunction(that.event.onBindValidate)) {
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}
        },
        //添加语音
        addvideo: function () {
            if (!this.action.addvideo || !this.tpl.videoinput) {
                return;
            }

            var that = this,
                title = "添加微课视频",
                $form;

            var dialog = Izb.ui.showDialogByTpl(this.tpl.videoinput, title, { params: that.hashParams, data: {} }, function () {

                //表单校验
                if (!$form.valid()) {
                    return false;
                }

                /*//验证
                 if ($.fn.formValidate && !$('form[name=' + that.formName + ']').formValidate()) {
                 return false;
                 }*/

                var data = Izb.ui.getFormData("inputForm");
                if ($.isFunction(that.event.onBeforeAdd)) {
                    that.event.onBeforeAdd(data);
                }

                var currentEditCallback = data.recommend_html;
                editor = KindEditor.create('#recommend_html',{allowFileManager:true,uploadJson : '/upload.json',afterCreate : function() {
                    this.loadPlugin('autoheight');
                }});



                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.addvideo,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                        if ($.isFunction(that.event.onAfterAdd)) {
                            that.event.onAfterAdd(result);
                        }
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });


            $form = $('form[name=' + that.formName + ']');

            //if(!$.isFunction(that.event.onBindValidate)){
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}

        },
        //编辑语音
        editvideo: function (id) {
            if (!this.action.editvideo || !this.tpl.videoinput) {
                return;
            }
            var that = this,
                title = "修改微课视频";
            if (!id) {
                return false;
            }
            var data = this.get(id), $form;
            Izb.core.out(data);

            var dialog = Izb.ui.showDialogByTpl(this.tpl.videoinput, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

                //验证
                if (!$form.valid()) {
                    return false;
                }

                var data = Izb.ui.getFormData(that.formName);

                if ($.isFunction(that.event.onBeforeSaveEdit)) {
                    that.event.onBeforeSaveEdit(data);
                }


                Izb.core.out(data);
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.editvideo,
                    data: data,
                    success: function (result) {
                        Izb.main.refresh();
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });

            Izb.ui.setFormData(that.formName, data);

            if ($.isFunction(that.event.onEditRender)) {
                that.event.onEditRender();
            }

            //初始化验证
            $form = $('form[name=' + that.formName + ']');

            //if (!$.isFunction(that.event.onBindValidate)) {
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}
        },
        //推荐
        recommend: function (_id,status) {
            if (!this.action.recommend || !_id) {
                return;
            }
            if (!this.checkInputLimits()) {
                return;
            }
            var that = this,
                title;
            if(status == null || status == "" || status == "0" || status == 0){
                status = "0";
                title = "确定要推荐该项目？";
            }else if(status == 1 || status == "1"){
                title = "确定不要推荐该项目？";
            }
            Izb.ui.confirm(title, function () {
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.recommend,
                    data: { _id: _id ,status:status},
                    success: function (result) {
                        if(result.code == 0){
                            Izb.ui.alert("已经推荐的数量已经大于等于5条！", "信息提示")
                        }
                        Izb.main.refresh();

                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert("已经推荐的数量已经大于等于5条！", "信息提示")
                    }
                });
            });
        }
    });

}(Izb));