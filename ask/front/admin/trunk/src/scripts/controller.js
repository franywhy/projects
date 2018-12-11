/*
 * opt:初始化Controller的属性
 * extra:新对象附加的属性和方法
 * new Controller(opt, extra);
 */
var Controller = $.Class({
    page: 1,

    //临时列表
    tmpList: null,
    //优先级1
    searchParams: null,
    //优先级2
    hashParams: null,
    init: function (opt, extra) {
        //面板属性
        this.pty = opt.pty;
        //面板Id
        this.pnlId = this.pty.key + 'Panel';
        //数据接口的映射
        this.action = opt.action;
        //模板Ids
        this.tpl = opt.tpl;
        //表单名称
        this.formName = opt.formName || "inputForm";
        //表单名称
        this.searchFormName = opt.searchFormName || "searchForm";
        //提示标题
        this.tipTitle = $.extend({
            add: "添加" + this.pty.itemName,
            edit: "编辑" + this.pty.itemName,
            del: "确定要删除" + this.pty.itemName + "吗?",
            show: '查看' + this.pty.itemName,
            freeze: '上下架' + this.pty.itemName,
            submit: '提交' + this.pty.itemName,
            unsubmit: '反提交' + this.pty.itemName,
            examine: '审核' + this.pty.itemName,
            unexamine: '反审核' + this.pty.itemName
        }, opt.tipTitle || {});
        this.extraListData = opt.extraListData || {};
        this.event = opt.event || {};
        this.size = opt.size || Izb.config.size;
        if (!$.isEmptyObject(extra)) {
            $.extend(this, extra);
        }
    },
    //首页
    index: function (params) {
        if (!this.checkListLimits("查看" + this.pty.name)) {
            return;
        }

        Izb.ui.renderHeader(this.tpl.header, { pty: this.pty, params: params });
        this.page = 1;
        this.searchParams = null;
        this.hashParams = params;
        this.tmpList = null;
        this.pty["_autoLoad"] = false;
        this.list(1);
        this.bindEvent();
        Izb.ui.closeDialog('inputDialog');
    },
    bindEvent: function () {
        var that = this;
        var $main = $('#main');
        // console.log($main);
        $main.undelegate('.J_search', 'click');
        $main.delegate('.J_search', 'click', function () {
            that.search();
        });


        $main.on( 'click','.J_count', function () {
            $('#uuuu').onclick(
                Izb.controller.statement.count(that.searchFormName)
            );
        });

        $main.undelegate('.J_add', 'click');
        $main.delegate('.J_add', 'click', function () {
            that.add();
        });

        $main.undelegate('.J_export', 'click');
        $main.delegate('.J_export', 'click', function () {
            that.exportData();
        });

        $main.undelegate('.J_back', 'click');
        $main.delegate('.J_back', 'click', function () {
            history.back();
        });
        $main.undelegate('form[name=searchForm]', 'keypress keydown');
        $main.delegate("form[name=searchForm]", "keypress keydown", function (e) {
            if (e.which == 13) {
                that.search();
                return false;
            }
        });
    },
    //列表
    list: function (page, size) {
        if (!this.action.list || !this.tpl.content) {
            return;
        }
        if (!this.checkListLimits("查看" + this.pty.name)) {
            return;
        }
        var that = this;
        this.page = page;
        if ($.isNumeric(size)) {
            this.size = size;
        }

        //优先级:hashParams<searchParams<extraListData
        var data = $.extend({
            page: page || 1,
            size: this.size
        }, this.hashParams || {});
        $.extend(data, this.searchParams || {});
        $.extend(data, this.extraListData || {});
        var flag = true;
        if ($.isFunction(that.event.onBeforeList)) {
            flag = that.event.onBeforeList.call(that,data);
        }
        Izb.ui.setFormData(that.searchFormName, data);
        if (flag === false) {
            return;
        }

        if (this.pty.autoLoad == false && !this.pty["_autoLoad"]) {
            this.pty["_autoLoad"] = true;
            $("#appContent").html('<div class="listEmpty">点击查询获取数据。</div>');
            return;
        }

        Izb.ui.renderContent('<div class="appLoading">数据正在努力的加载中，请稍后...</div>');

        Izb.common.getResult({
            action: that.action.list,
            data: data,
            success: function (result) {

                if ($.isFunction(that.event.onAfterList)) {
                    that.event.onAfterList.call(that, result);
                }
                that.tmpList = result.data;

                result.pty = that.pty;
                Izb.ui.renderContent(that.tpl.content, result);
                $(".tablesorter").tablesorter();

                //List数据展示完后回掉数据
                if ($.isFunction(that.event.onListTmpAfter)) {
                    that.event.onListTmpAfter.call(that, result);
                }
                //List数据展示完后绑定click函数(credit used)
                if ($.isFunction(that.event.onClassBindClick)) {
                    that.event.onClassBindClick.call();
                }
            },
            error: function (xhr, status, result) {
                Izb.ui.renderContent(result.msg || '网络异常，请刷新后重试！');
                //Izb.ui.renderContent('网络异常，请刷新后重试！');
                //Izb.ui.renderContent($("#"+that.tpl.content).html());
            }
        });
    },
    //获取model,id=='model'取对象的信息
    get: function (id) {
        var data = null;
        if ($.isEmptyObject(this.tmpList)) {
            return data;
        }

        if ($.isArray(this.tmpList)) {
            $.each(this.tmpList, function (i, item) {
                if (item._id == id) {
//                if (item._id == id) {
                    data = item;
                    return false;
                }
                if (item.id == id) {
                    data = item;
                    return false;
                }
            });
        } else {
            if (id == 'model') {
                data = this.tmpList;
            }
        }
        return $.extend({}, data);
    },
    //查看详细
    show: function (id) {
        if (!this.action.show || !this.tpl.show) {
            return;
        }
        var that = this,
            title = this.tipTitle.show;
        if (!id) {
            return false;
        }

        var data = { _id: id };
        if ($.isFunction(that.event.onBeforeShow)) {
            that.event.onBeforeShow(data);
        }

        Izb.common.getResult({
            action: that.action.show,
            data: data,
            success: function (result) {
                result.pty = that.pty;
                Izb.ui.showDialogByTpl(that.tpl.show, title, result, function () {
                    Izb.ui.closeDialog('inputDialog');
                });
                if ($.isFunction(that.event.onAfterShow)) {
                    that.event.onAfterShow(result);
                }
            }
        });
    },
    //添加
    add: function () {
        if (!this.action.add || !this.tpl.input) {
            return;
        }

        if (!this.checkInputLimits(this.tipTitle.add)) {
            return;
        }
        var that = this,
            title = this.tipTitle.add,
            $form;

        var dialog = Izb.ui.showDialogByTpl(this.tpl.input, title, { params: that.hashParams, data: {} }, function () {

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

            Izb.common.getResult({
                type: 'POST',
                action: that.action.add,
                data: data,
                success: function (result) {
                    Izb.main.refresh();
                    if ($.isFunction(that.event.onAfterAdd)) {
                        that.event.onAfterAdd(result,data);
                    }
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        });

        if ($.isFunction(that.event.onAddRender)) {
            that.event.onAddRender();
        }

        $form = $('form[name=' + that.formName + ']');

        //if(!$.isFunction(that.event.onBindValidate)){
        //	that.event.onBindValidate($form);
        //} else {
        //	$form.validate();
        //}

    },
    //编辑
    edit: function (id) {
        if (!this.action.edit || !this.tpl.input) {
            return;
        }
        if (!this.checkInputLimits(this.tipTitle.edit)) {
            return;
        }
        var that = this,
            title = this.tipTitle.edit;
        if (!id) {
            return false;
        }
        var data = this.get(id), $form;
        Izb.core.out(data);

        var dialog = Izb.ui.showDialogByTpl(this.tpl.input, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

            //验证
            if (!$form.valid()) {
                return false;
            }

            var data = Izb.ui.getFormData(that.formName);

            if ($.isFunction(that.event.onBeforeSaveEdit)) {
                that.event.onBeforeSaveEdit(data);
            }


            Izb.core.out(data);
            that.update(data);
        });

        if ($.isFunction(that.event.onBeforeEdit)) {
            that.event.onBeforeEdit(data, dialog);
        }

        Izb.ui.setFormData(that.formName, data);

        if ($.isFunction(that.event.onEditRender)) {
            that.event.onEditRender(data);
        }

        //初始化验证
        $form = $('form[name=' + that.formName + ']');

        //if (!$.isFunction(that.event.onBindValidate)) {
        //	that.event.onBindValidate($form);
        //} else {
        //	$form.validate();
        //}
    },
    //更新
    update: function (data) {
        if (!this.action.edit || (!data._id && !data.id)) {
            return;
        }

        if (!this.checkInputLimits()) {
            return;
        }

        var that = this;

        Izb.common.getResult({
            type: 'POST',
            action: this.action.edit,
            data: data,
            success: function (result) {
                Izb.main.refresh();
                if ($.isFunction(that.event.onAfterEdit)) {
                    that.event.onAfterEdit(result,data);
                }
            },
            error: function (xhr, status, result) {
                Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
            }
        });

    },
    //删除
    del: function (_id) {
        if (!this.action.del || !_id) {
            return;
        }
        if (!this.checkInputLimits()) {
            return;
        }
        var that = this,
            title = this.tipTitle.del;
        Izb.ui.confirm(title, function () {
            Izb.common.getResult({
                type: 'POST',
                action: that.action.del,
                data: { _id: _id },
                success: function (result) {
                    Izb.main.refresh();
                    if ($.isFunction(that.event.onAfterDel)) {
                        that.event.onAfterDel(result);
                    }
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        });
    },
    //搜索
    search: function (page) {

        var searchParams = Izb.ui.getFormData(this.searchFormName);

        for (var key in searchParams) {
            if (searchParams[key] == undefined || searchParams[key] == null || searchParams[key] == "") {
                delete searchParams[key];
            }
        }

        if ($.isFunction(this.event.onSearch)) {
            this.event.onSearch(searchParams);
        }
        this.searchParams = searchParams;
        this.list(1);
        //$.history.setActions({page:1,Izb:new Date().getTime()});
    },
    checkListLimits: function (text) {
        var flag = Izb.user.checkLimits(this.pty.menu, Izb.enumList.menuType.list);
        //列表权限校验
        if (!flag) {
            Izb.ui.tips('您暂无' + (text || '') + '权限,请勿非法操作！', 'error');
        }
        return flag;
    },
    checkInputLimits: function () {
        var flag = Izb.user.checkLimits(this.pty.menu, Izb.enumList.menuType.all);
        //列表权限校验
        if (!flag) {
            Izb.ui.tips('您暂无' + (text || '') + '权限,请勿非法操作！', 'error');
            return;
        }
        return flag;
    },
    //导出数据
    exportData: function () {
        Izb.ui.exportData();
    },
    gotoUrl: function (url) {
        location.hash = url;
    },
    //审查
    examine: function (id) {
        if (!this.action.edit || !this.tpl.input) {
            return;
        }
        if (!this.checkInputLimits(this.tipTitle.edit)) {
            return;
        }
        var that = this,
            title = this.tipTitle.examine;
        if (!id) {
            return false;
        }
        var data = this.get(id), $form;
        Izb.core.out(data);

        $(this).find("input")
        var dialog = Izb.ui.showDialogByTpl(this.tpl.input, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

            that.audit(id);
        });
        if ($.isFunction(that.event.onBeforeEdit)) {
            that.event.onBeforeEdit(data, dialog);
        }
        Izb.ui.setFormData(that.formName, data);
        if ($.isFunction(that.event.onEditRender)) {
            that.event.onEditRender();
        }
        $(".aui_border").find("input").attr({"disabled":true});
        $(".aui_border").find("textarea").attr({"disabled":true});
        $(".aui_border").find("select").attr("disabled","disabled");
        $(".aui_border").find("a").attr({"disabled":true});

    },
    //编辑

    refund: function (id) {
        if (!this.action.edit || !this.tpl.input) {
            return;
        }
        if (!this.checkInputLimits(this.tipTitle.edit)) {
            return;
        }
        var that = this,
            title = this.tipTitle.examine;
        if (!id) {
            return false;
        }
        var data = this.get(id), $form;
        Izb.core.out(data);

        $(this).find("input")
        var dialog = Izb.ui.showDialogByTpl(this.tpl.input, '退单申请', { params: that.hashParams, pty: that.pty, data: data }, function () {
            var data_input = Izb.ui.getFormData(that.formName);
            data.money =data_input.money;
            data.refund_instructions=data_input.refund_instructions;
            console.log(data);
            that.add_refund(data);
        });
        if ($.isFunction(that.event.onBeforeEdit)) {
            that.event.onBeforeEdit(data, dialog);
        }
        Izb.ui.setFormData(that.formName, data);
        if ($.isFunction(that.event.onEditRender)) {
            that.event.onEditRender();
        }

        if ($.isFunction(that.event.onBeforeShow)) {
            that.event.onBeforeShow(data, 2);
        }

//        $(".aui_border").find("input").attr({"disabled":true});
        //        $(".aui_border").find("textarea").attr({"disabled":true});
        $(".aui_border").find("select").attr("disabled","disabled");
//        $(".aui_border").find("a").attr({"disabled":true});

    },
//冻结/解冻
    freeze: function (_id,status) {
        if (!this.action.freeze || !_id) {
            return;
        }
        if (!this.checkInputLimits()) {
            return;
        }
        var that = this,
            title = this.tipTitle.freeze;
        if(status == null || status == ""){
            status = "0";
        }
        Izb.ui.confirm(title, function () {
            Izb.common.getResult({
                type: 'POST',
                action: that.action.freeze,
                data: { _id: _id ,status:status},
                success: function (result) {
                    if(result){
                        if(result.fail && result.fail == 1){
                            Izb.ui.alert("该商品没有价格，无法上架。", "信息提示")
                        }else{
                            Izb.main.refresh();
                        }
                    }
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        });
    },
    //上/下架
    stand: function (_id,status) {
        if (!this.action.stand || !_id) {
            return;
        }
        if (!this.checkInputLimits()) {
            return;
        }
        var that = this,
            title;

        if(status == null || status == "" || status == "0" || status == 0){
            status = "0";
            title = "确定要下架该直播？";
        }else if(status == 1 || status == "1"){
            title = "确定要上架该直播？";
        }

        Izb.ui.confirm(title, function () {
            Izb.common.getResult({
                type: 'POST',
                action: that.action.stand,
                data: { _id: _id ,status:status},
                success: function (result) {
                    Izb.main.refresh();
                    Izb.ui.alert(result.msg, "信息提示", "")
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        });
    },
    //提交
    submit: function (_id,status) {
        if (!this.action.submit || !_id) {
            return;
        }
        if (!this.checkInputLimits()) {
            return;
        }
        var that = this,
            title = this.tipTitle.submit;
        if(status == null || status == ""){
            status = "0";
        }
        Izb.ui.confirm(title, function () {
            Izb.common.getResult({
                type: 'POST',
                action: that.action.submit,
                data: { _id: _id ,upload_flag:status},
                success: function (result) {
                    Izb.main.refresh();
                    if(result && result.code==500){
                        Izb.ui.alert(result.msg, "信息提示", "")
                    }
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        });
    },
    //反提交
    unsubmit: function (_id,status) {
        if (!this.action.unsubmit || !_id) {
            return;
        }
        if (!this.checkInputLimits()) {
            return;
        }
        var that = this,
            title = this.tipTitle.unsubmit;
        if(status == null || status == ""){
            status = "1";
        }
        Izb.ui.confirm(title, function () {
            Izb.common.getResult({
                type: 'POST',
                action: that.action.unsubmit,
                data: { _id: _id ,upload_flag:status},
                success: function (result) {
                    Izb.main.refresh();
                    if(result && result.code==500){
                        Izb.ui.alert(result.msg, "信息提示", "")
                    }
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        });
    },
    //审核
    audit: function (_id,status) {
        if (!this.action.audit || !_id) {
            return;
        }
        if (!this.checkInputLimits()) {
            return;
        }
        var that = this,
            title = this.tipTitle.examine;
        if(status == null || status == ""){
            status = "0";
        }
        Izb.ui.confirm(title, function () {
            Izb.common.getResult({
                type: 'POST',
                action: that.action.audit,
                data: { _id: _id ,audit_flag:status},
                success: function (result) {
                    Izb.main.refresh();
                    if(result && result.code==500){
                        Izb.ui.alert(result.msg, "信息提示", "")
                    }
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        });
    },
    //反审核
    unaudit: function (_id,status) {
        if (!this.action.unaudit || !_id) {
            return;
        }
        if (!this.checkInputLimits()) {
            return;
        }
        var that = this,
            title = this.tipTitle.unexamine;
        if(status == null || status == ""){
            status = "1";
        }
        Izb.ui.confirm(title, function () {
            Izb.common.getResult({
                type: 'POST',
                action: that.action.unaudit,
                data: { _id: _id ,audit_flag:status},
                success: function (result) {
                    Izb.main.refresh();
                    if(result && result.code==500){
                        Izb.ui.alert(result.msg, "信息提示", "")
                    }
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        });
    },
    detail: function (_id) {
        var that = this,
            title = this.tipTitle.show;
        if (!_id) {
            return false;
        }
        var data = this.get(_id), $form;
        Izb.core.out(data);
        Izb.ui.setFormData("detailForm", data);
        console.log(that);
        $(".aui_border").find("input").attr({"disabled":true});
        $(".aui_border").find("textarea").attr({"disabled":true});
        $(".aui_border").find("select").attr("disabled","disabled");
        $(".aui_border").find("a").attr({"disabled":true});
    },
    //课程列表
    courselist: function (page, size) {
        if (!this.action.courselist || !this.tpl.content) {
            return;
        }
        var that = this;
        this.page = page;
        if ($.isNumeric(size)) {
            this.size = size;
        }

        //优先级:hashParams<searchParams<extraListData
        var data = $.extend({
            page: page || 1,
            size: this.size
        }, this.hashParams || {});
        $.extend(data, this.searchParams || {});
        $.extend(data, this.extraListData || {});
        var flag = true;
        if ($.isFunction(that.event.onBeforeList)) {
            flag = that.event.onBeforeList.call(that,data);
        }
        Izb.ui.setFormData(that.searchFormName, data);
        if (flag === false) {
            return;
        }

        if (this.pty.autoLoad == false && !this.pty["_autoLoad"]) {
            this.pty["_autoLoad"] = true;
            $("#appContent").html('<div class="listEmpty">点击查询获取数据。</div>');
            return;
        }

        Izb.ui.renderContent('<div class="appLoading">数据正在努力的加载中，请稍后...</div>');

        Izb.common.getResult({
            action: that.action.courselist,
            data: data,
            success: function (result) {

                if ($.isFunction(that.event.onAfterList)) {
                    that.event.onAfterList.call(that, result);
                }
                that.tmpList = result.data;

                result.pty = that.pty;
                Izb.ui.renderContent(that.tpl.content, result);
                $(".tablesorter").tablesorter();

                //List数据展示完后回掉数据
                if ($.isFunction(that.event.onListTmpAfter)) {
                    that.event.onListTmpAfter.call(that, result);
                }
            },
            error: function (xhr, status, result) {
                Izb.ui.renderContent(result.msg || '网络异常，请刷新后重试！');
                //Izb.ui.renderContent('网络异常，请刷新后重试！');
                //Izb.ui.renderContent($("#"+that.tpl.content).html());
            }
        });
    },
    //价格列表
    pricelist: function (page, size) {
        if (!this.action.pricelist || !this.tpl.content) {
            return;
        }
        var that = this;
        this.page = page;
        if ($.isNumeric(size)) {
            this.size = size;
        }

        //优先级:hashParams<searchParams<extraListData
        var data = $.extend({
            page: page || 1,
            size: this.size
        }, this.hashParams || {});
        $.extend(data, this.searchParams || {});
        $.extend(data, this.extraListData || {});
        var flag = true;
        if ($.isFunction(that.event.onBeforeList)) {
            flag = that.event.onBeforeList.call(that,data);
        }
        Izb.ui.setFormData(that.searchFormName, data);
        if (flag === false) {
            return;
        }

        if (this.pty.autoLoad == false && !this.pty["_autoLoad"]) {
            this.pty["_autoLoad"] = true;
            $("#appContent").html('<div class="listEmpty">点击查询获取数据。</div>');
            return;
        }

        Izb.ui.renderContent('<div class="appLoading">数据正在努力的加载中，请稍后...</div>');

        Izb.common.getResult({
            action: that.action.pricelist,
            data: data,
            success: function (result) {

                if ($.isFunction(that.event.onAfterList)) {
                    that.event.onAfterList.call(that, result);
                }
                that.tmpList = result.data;

                result.pty = that.pty;
                Izb.ui.renderContent(that.tpl.content, result);
                $(".tablesorter").tablesorter();

                //List数据展示完后回掉数据
                if ($.isFunction(that.event.onListTmpAfter)) {
                    that.event.onListTmpAfter.call(that, result);
                }
            },
            error: function (xhr, status, result) {
                Izb.ui.renderContent(result.msg || '网络异常，请刷新后重试！');
                //Izb.ui.renderContent('网络异常，请刷新后重试！');
                //Izb.ui.renderContent($("#"+that.tpl.content).html());
            }
        });
    },
    //查看
    showDetail: function (id) {
        var that = this,
            title = this.tipTitle.show;
        if (!id) {
            return false;
        }
        var data = this.get(id), $form;
        Izb.core.out(data);
        var dialog = Izb.ui.showDialogByTpl(this.tpl.input, title, { params: that.hashParams, pty: that.pty, data: data }, function () {
        });
        if ($.isFunction(that.event.onBeforeEdit)) {
            that.event.onBeforeEdit(data, dialog);
        }
        Izb.ui.setFormData(that.formName, data);
        if ($.isFunction(that.event.onEditRender)) {
            that.event.onEditRender();
        }
        if ($.isFunction(that.event.onBeforeShow)) {
            that.event.onBeforeShow(data, dialog);
        }
        console.log(that);
        $(".aui_border").find("input").attr({"disabled":true});
        $(".aui_border").find("textarea").attr({"disabled":true});
        $(".aui_border").find("select").attr("disabled","disabled");
        $(".aui_border").find("a").attr({"disabled":true});
    }
});


/*
 * opt:初始化Controller的属性
 * extra:新对象附加的属性和方法
 * new TabListController(opt, extra);
 */
var TabListController = $.Class(
    {
        page: 1,
        //临时列表
        tmpList: null,
        //优先级1
        searchParams: null,
        //优先级2
        hashParams: null,
        init: function (opt, extra) {
            //面板属性
            this.pty = opt.pty;
            //面板Id
            this.pnlId = this.pty.key + 'Panel';
            //数据接口的映射
            this.action = opt.action;
            //模板Id
            this.tpl = opt.tpl;
            //表单名称
            this.formName = opt.formName || "inputForm";
            //表单名称
            this.searchFormName = opt.searchFormName || "searchForm";
            //提示标题
            this.tipTitle = $.extend({
                add: "添加" + this.pty.itemName,
                edit: "编辑" + this.pty.itemName,
                del: "确定要删除" + this.pty.itemName + "吗?",
                show: '查看' + this.pty.itemName,
                submit: '提交' + this.pty.itemName,
                unsubmit: '反提交' + this.pty.itemName,
                examine: '审核' + this.pty.itemName,
                unexamine: '反审核' + this.pty.itemName
            }, opt.tipTitle || {});
            this.extraListData = opt.extraListData || {};
            this.event = opt.event || {};
            this.size = opt.size;
            if (!$.isEmptyObject(extra)) {
                $.extend(this, extra);
            }
        },
        //首页
        index: function (params) {

            this.page = 1;
            this.searchParams = null;
            this.hashParams = params;
            this.tmpList = null;

            this.curHeader = this.tpl[params.a + '-header'] || this.tpl['header'];
            this.curAction = this.action[params.tab] || this.action[params.a];
            this.curTpl = this.tpl[params.tab] || this.tpl[params.a];

            Izb.ui.renderHeader(this.curHeader, { pty: this.pty, params: params });
            this.list(1);
            this.bindEvent();
            Izb.ui.closeDialog('inputDialog');
        },
        bindEvent: function () {
            var that = this;
            var $main = $('#main');
            $main.undelegate('.J_newclass', 'click');
            $main.delegate('.J_newclass', 'click', function () {
                that.newclass();
            });

            $main.undelegate('.J_search', 'click');
            $main.delegate('.J_search', 'click', function () {
                that.search();
            });

            $main.undelegate('.J_export', 'click');
            $main.delegate('.J_export', 'click', function () {
                that.exportData();
            });

            $main.undelegate('.J_back', 'click');
            $main.delegate('.J_back', 'click', function () {
                history.back();
            });

            $main.delegate("form[name=searchForm]", "keypress keydown", function (e) {
                if (e.which == 13) {
                    that.search();
                    return false;
                }
            });

        },
        //获取model,id=='model'取对象的信息
        get: function (id) {
            var data = null;
            if ($.isEmptyObject(this.tmpList)) {
                return data;
            }

            if ($.isArray(this.tmpList)) {
                $.each(this.tmpList, function (i, item) {
                    if (item._id == id) {
//                if (item._id == id) {
                        data = item;
                        return false;
                    }
                });
            } else {
                if (id == 'model') {
                    data = this.tmpList;
                }
            }
            return $.extend({}, data);
        },
        //列表
        list: function (page, size) {
            if (!this.checkListLimits("查看" + this.pty.name)) {
                return;
            }
            if (this.curAction == "/commoditys/school") {
                size = 1000;
            }
            var that = this;
            this.page = page;
            if ($.isNumeric(size)) {
                this.size = size;
            }
            Izb.ui.renderContent('<div class="appLoading">数据加载中...</div>');

            //优先级:hashParams<searchParams<extraListData
            var data = $.extend({
                page: page || 1,
                size: this.size || Izb.config.size
            }, this.hashParams || {});
            $.extend(data, this.searchParams || {});
            $.extend(data, this.extraListData || {});

            if ($.isFunction(that.event.onBeforeList)) {
                that.event.onBeforeList(data);
            }

            Izb.ui.setFormData(that.searchFormName, data);

            Izb.common.getResult({
                action: this.curAction,
                data: data,
                success: function (result) {
                    that.tmpList = result.data || result;
                    result.pty = that.pty;
                    Izb.ui.renderContent(that.curTpl, result);
                    console.log("=="+that.curTpl);
                    if ($.isFunction(that.event.onAfterList)) {
                        that.event.onAfterList(result);
                    }
                    $(".tablesorter").tablesorter();
                },
                error: function (xhr, status, result) {
                    Izb.ui.renderContent(result.msg || '网络异常，请刷新后重试！');
                    //Izb.ui.renderContent('网络异常，请刷新后重试！');
                }
            });
        },
        //添加
        add: function () {
            if (!this.action.add || !this.tpl.input) {
                return;
            }

            if (!this.checkInputLimits(this.tipTitle.add)) {
                return;
            }
            var that = this,
                title = this.tipTitle.add,
                $form;

            var dialog = Izb.ui.showDialogByTpl(this.tpl.input, title, { params: that.hashParams, data: {} }, function () {

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

                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.add,
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

            if ($.isFunction(that.event.onAddRender)) {
                that.event.onAddRender();
            }

            $form = $('form[name=' + that.formName + ']');

            //if(!$.isFunction(that.event.onBindValidate)){
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}

        },
        //编辑
        edit: function (id) {
            if (!this.action.edit || !this.tpl.input) {
                return;
            }
            if (!this.checkInputLimits(this.tipTitle.edit)) {
                return;
            }
            var that = this,
                title = this.tipTitle.edit;
            if (!id) {
                return false;
            }
            var data = this.get(id), $form;
            Izb.core.out(data);

            var dialog = Izb.ui.showDialogByTpl(this.tpl.input, title, { params: that.hashParams, pty: that.pty, data: data }, function () {

                //验证
                if (!$form.valid()) {
                    return false;
                }

                var data = Izb.ui.getFormData(that.formName);

                if ($.isFunction(that.event.onBeforeSaveEdit)) {
                    that.event.onBeforeSaveEdit(data);
                }


                Izb.core.out(data);
                that.update(data);
            });

            if ($.isFunction(that.event.onBeforeEdit)) {
                that.event.onBeforeEdit(data, dialog);
            }

            Izb.ui.setFormData(that.formName, data);

            if ($.isFunction(that.event.onEditRender)) {
                that.event.onEditRender(data);
            }

            //初始化验证
            $form = $('form[name=' + that.formName + ']');

            //if (!$.isFunction(that.event.onBindValidate)) {
            //	that.event.onBindValidate($form);
            //} else {
            //	$form.validate();
            //}
        },
        //更新
        update: function (data) {
            var that = this;

            if (!this.action.edit || (!data._id && !data.id)) {
                return;
            }

            if (!this.checkInputLimits()) {
                return;
            }


            Izb.common.getResult({
                type: 'POST',
                action: this.action.edit,
                data: data,
                success: function (result) {
                    Izb.main.refresh();
                    if ($.isFunction(that.event.onAfterEdit)) {
                        that.event.onAfterEdit(result);
                    }
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });

        },
        //删除
        del: function (_id) {
            if (!this.action.del || !_id) {
                return;
            }
            if (!this.checkInputLimits()) {
                return;
            }
            var that = this,
                title = this.tipTitle.del;
            Izb.ui.confirm(title, function () {
                Izb.common.getResult({
                    type: 'POST',
                    action: that.action.del,
                    data: { _id: _id },
                    success: function (result) {
                        Izb.main.refresh();
                        if ($.isFunction(that.event.onAfterDel)) {
                            that.event.onAfterDel(result);
                        }
                    },
                    error: function (xhr, status, result) {
                        Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                    }
                });
            });
        },
        //搜索
        search: function (page) {

            var searchParams = Izb.ui.getFormData(this.searchFormName);

            for (var key in searchParams) {
                if (searchParams[key] == undefined || searchParams[key] == null || searchParams[key] == "") {
                    delete searchParams[key];
                }
            }

            if ($.isFunction(this.event.onSearch)) {
                this.event.onSearch(searchParams);
            }

            this.searchParams = searchParams;
            this.list(1);
        },
        checkListLimits: function (text) {
            var flag = Izb.user.checkLimits(this.pty.menu, Izb.enumList.menuType.list);
            //列表权限校验
            if (!flag) {
                Izb.ui.tips('您暂无' + (text || '') + '权限,请勿非法操作！', 'error');
            }
            return flag;
        },
        checkInputLimits: function () {
            var flag = Izb.user.checkLimits(this.pty.menu, Izb.enumList.menuType.all);
            //列表权限校验
            if (!flag) {
                Izb.ui.tips('您暂无' + (text || '') + '权限,请勿非法操作！', 'error');
                return;
            }
            return flag;
        },
        //导出数据
        exportData: function () {
            Izb.ui.exportData();
        },
        //保存
        saveproduct: function (_id) {
            var that = this;
            if (!_id) {
                return false;
            }
            var data , $form;

            data = Izb.ui.getFormData("productForm");

            Izb.core.out(data);
            if (!this.action.saveproduct || (!data._id && !data.id)) {
//        if (!this.action.edit || !data._id ) {
                return;
            }

            /* if (!this.checkInputLimits()) {
             return;
             }*/


            Izb.common.getResult({
                type: 'POST',
                action: this.action.saveproduct,
                data: data,
                success: function (result) {
                    Izb.ui.alert("保存成功!", "信息提示", "")
                },
                error: function (xhr, status, result) {
                    Izb.ui.alert(Izb.resultMsg[result.code] || result.msg, "信息提示", "error")
                }
            });
        },
        //新增分类
        newclass: function () {
            var that = this;
            var dialog = Izb.ui.showDialogByTpl(this.tpl.newclass, "新增分类", { params: this.hashParams, data: {} }, function () {

                var tb_class_id = that.uuid();
                var class_id = that.uuid();
                var name = $("#name").val();
                $("#coursetable").append('<table id="'+tb_class_id+'"><tr>'
                    +'<td class="auto" style="width:200px;" id="'+class_id+'">'+name+'</td>'
                    +'<td class="auto"><button class="d-button" name="newcourse" value="newcourse" onclick="Izb.controller.commoditystab.newcourse('+tb_class_id+')">添加课程</button>'
                    +'<button class="d-button" name="delate" value="delate" onclick="Izb.controller.commoditystab.moveTableUp(this)">上移</button>'
                    +'<button class="d-button" name="delate" value="delate" onclick="Izb.controller.commoditystab.moveTableDown(this)">下移</button>'
                    +'<button class="d-button" name="editclass" value="editclass" onclick="Izb.controller.commoditystab.editclass('+class_id+')">编辑</button>'
                    +'<button class="d-button" name="deleteclass" value="deleteclass" onclick="Izb.controller.commoditystab.deleteclass('+tb_class_id+')">删除</button></td></tr></table>')

            });
        },
        //修改分类
        editclass: function (class_id) {
            var that = this;
            var name = $("#"+class_id).html();
            var dialog = Izb.ui.showDialogByTpl(this.tpl.newclass, "编辑分类", { params: this.hashParams, data: {} }, function () {

                name = $("#name").val();
                $("#"+class_id).html(name);

            });
            $("#name").val(name);
        },
        //删除分类
        deleteclass: function (tb_class_id) {
            $("#"+tb_class_id).remove();
        },
        //新增课程
        newcourse: function (tb_class_id) {
            var that = this;
            var data;
            var dialog = Izb.ui.showDialogByTpl(this.tpl.newcourse, "添加课程", { params: this.hashParams, data: {} }, function () {

                var course_id = that.uuid();
                var tr_course_id = that.uuid();
                var course = $("#course").val();
                var coursename = $("#course").find("option:selected").text();
                $("#"+tb_class_id).append('<tr id="'+tr_course_id+'">'
                    +'<td class="auto" style="width:200px;" data-course="'+course+'" id="'+course_id+'">'+coursename+'</td>'
                    +'<td class="auto">'
                    +'<button class="d-button" name="delate" value="delate" onclick="Izb.controller.commoditystab.moveUp(this,'+tb_class_id+')">上移</button>'
                    +'<button class="d-button" name="delate" value="delate" onclick="Izb.controller.commoditystab.moveDown(this,'+tb_class_id+')">下移</button>'
                    +'<button class="d-button" name="deletecourse" value="deletecourse" onclick="Izb.controller.commoditystab.deletecourse('+tr_course_id+')">删除</button></td></tr>')

            });
            var arr=new Array();
            var href = window.location.href;
            arr=href.split('=');
            var _id = arr[2].split('&')[0];
            var testJson = '{ "_id": "'+_id+'" }';
            testJson = eval("(" + testJson + ")");
            Izb.common.getResult({
                action: "/commoditys/courselist",
                data: testJson,
                success: function (result) {
                    var data = result.data;
                    for(var i=0;i<data.length;i++){
                        $("#course").append('<option value="'+data[i]._id+'">'+data[i].course_code+' '+data[i].show_name+'</option>');
                    }
                },
                error: function (xhr, status, result) {
                }
            });
        },
        //删除课程
        deletecourse: function (tr_course_id) {
            $("#"+tr_course_id).remove();
        },
        //保存分类
        saveclassify: function (_id) {
            var classsort = 0;
            var classifystring = '[';
            $("#coursetable").find("table").each(function() {
                var coursesort = 0;
                var coursetableid = $(this).attr("id");
                $("#"+coursetableid).find("tr").each(function() {
                    var classname;
                    var classuuid;
                    var courseid;
                    var courseuuid;
                    if($(this).index() == "0"){
                        $(this).find("td").each(function() {
                            if($(this).index() == "0"){
                                classname = $(this).text();
                                classuuid = $(this).attr("id");
                                classifystring = classifystring.concat('{"_id":"',classuuid,'","name":"',classname,'","sort":"',classsort,'","course_list":[');
                                classsort++;
                            }
                        });
                    }else{
                        $(this).find("td").each(function() {
                            if($(this).index() == "0"){
                                courseid = $(this).data("course");
                                courseuuid = $(this).attr("id");
                                classifystring = classifystring.concat('{"_id":"',courseuuid,'","commodity_courses_id":"',courseid,'","sort":"',coursesort,'"},');
                                coursesort++;
                            }
                        });
                    }
                });
                classifystring = classifystring.slice(0,classifystring.length-1);
                classifystring = classifystring.concat(']},')
            });
            classifystring = classifystring.slice(0,classifystring.length-1);
            classifystring = classifystring.concat(']');
            classifystring = eval("(" + classifystring + ")");
            var classstring = '{"classify":"","_id":"'+_id+'"}';
            classstring = eval("(" + classstring + ")");
            classstring.classify = JSON.stringify(classifystring)
            Izb.common.getResult({
                action: "/commoditys/saveclassify",
                data: classstring,
                success: function (result) {
                    Izb.main.refresh();
                },
                error: function (xhr, status, result) {
                }
            });
        },
        moveUp: function (_a,tb_class_id) {
            //通过链接对象获取表格行的引用
            var _row=_a.parentNode.parentNode;
            //如果不是第一行 交换顺序
            if(_row.previousSibling && _row.previousSibling.id != ""){
                var previousSibling = _row.previousSibling
                this.swapNode(_row,previousSibling);
            }
        },
        moveTableUp: function (_a) {
            //通过链接对象获取表格行的引用
            var _row=_a.parentNode.parentNode.parentNode.parentNode;
            //如果不是第一行 交换顺序
            if(_row.previousSibling)this.swapNode(_row,_row.previousSibling);
        },
        moveDown: function (_a,tb_class_id) {
            //通过链接对象获取表格行的引用
            var _row=_a.parentNode.parentNode;
            //如果不是最后一行 则与下一行交换顺序
            if(_row.nextSibling && _row.nextSibling.id != ""){
                var nextSibling = _row.nextSibling
                this.swapNode (_row,nextSibling);
            }
        },
        moveTableDown: function (_a) {
            //通过链接对象获取表格行的引用
            var _row=_a.parentNode.parentNode.parentNode.parentNode;
            //如果不是最后一行 则与下一行交换顺序s
            if(_row.nextSibling) this.swapNode (_row,_row.nextSibling);
        },
        //定义通用的函数交换两个节点的位置
        swapNode: function (node1,node2) {
            //获取父节点
                var _parent=node1.parentNode;
            //获取两个节点的相应位置
                var _t1=node1.nextSibling;
                var _t2=node2.nextSibling;
            //将node2插入到原来node1的位置
                if(_t1)_parent.insertBefore(node2,_t1);
                else _parent.appendChild(node2);
            //将node1插入到原来ndoe2的位置
                if(_t2)_parent.insertBefore(node1,_t2);
                else _parent.appendChild(node1);
        },
        savehtml: function (_id) {
            var htmlcontent = $("#htmlcontent").val();
            var testJson = { "_id": _id,"htmlcontent":htmlcontent };
//            testJson = eval("(" + testJson + ")");
            Izb.common.getResult({
                type: 'POST',
                action: "/commoditys/savehtml",
                data: testJson,
                success: function (result) {
                    Izb.main.refresh();
                },
                error: function (xhr, status, result) {
                }
            });
        },
        saveapphtml: function (_id) {
            var htmlcontent = $("#apphtmlcontent").val();
            var testJson = { "_id": _id,"htmlcontent":htmlcontent };
//            testJson = eval("(" + testJson + ")");
            Izb.common.getResult({
                type: 'POST',
                action: "/commoditys/appsavehtml",
                data: testJson,
                success: function (result) {
                    Izb.main.refresh();
                },
                error: function (xhr, status, result) {
                }
            });
        },
        saveschool: function (_id) {
            var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
            var nodes = treeObj.getCheckedNodes(true);
            var schoolarray = new Array();
            for(var index in nodes){
                if(nodes[index].code != null && nodes[index].code.length == 11 && nodes[index].is_school == "1"){
                    schoolarray.push(nodes[index].nc_id);
                }
            }
            var testJson = '{ "_id": "'+_id+'","schools":"" }';
            testJson = eval("(" + testJson + ")");
            testJson.schools = JSON.stringify(schoolarray);
            Izb.common.getResult({
                action: "/commoditys/saveschool",
                type: "post",
                data: testJson,
                success: function (result) {
                    Izb.main.refresh();
                },
                error: function (xhr, status, result) {
                }
            });
        },
        refreshthis: function () {
            window.location.reload();
        },
        uuid: function () {
            var s = [];
            var hexDigits = "0123456789012345";
            for (var i = 0; i < 12; i++) {
                s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
            }
            s[0] = "4"; // bits 12-15 of the time_hi_and_version field to 0010
            s[10] = hexDigits.substr((s[10] & 0x3) | 0x8, 1); // bits 6-7 of the clock_seq_hi_and_reserved to 01
            s[9] = s[6] = s[3] = s[1] ;

            var uuid = s.join("");
            return uuid;
        }
    });