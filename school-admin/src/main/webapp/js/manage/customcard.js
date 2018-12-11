$(function () {
    $(".datetimepicker").datetimepicker({
        format: 'yyyy-mm-dd hh:ii:00',
        zIndex : 999999999,
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
    });
    $(".datetimepicker_start").datetimepicker({
        format: 'yyyy-mm-dd 00:00:00',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
    });
    $(".datetimepicker_end").datetimepicker({
        format: 'yyyy-mm-dd 23:59:59',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
    });
    $("#jqGrid").jqGrid({
        url: '../customcard/list',
        datatype: "json",
        colModel: [		
			{ label: 'cardId', name: 'cardId', hidden: true, key: true },
            { label: 'pushMsgId', name: 'pushMsgId', hidden: true },
			{ label: '标题', name: 'title', width: 80 },
            { label: '副标题', name: 'subtitle', width: 80 },
            { label: '卡片地址', name: 'cardUrl', width: 80 },
            { label: '封面', name: 'cardBannerUrl', width: 30 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}},
            { label: '产品线', name: 'productName', width: 80 },
            { label: '推送状态', name: 'pushStatus', width: 80, formatter: function(value, options, row){
                var text = '';
                if(value == 0)text='<span class="label label-default">未推送</span>';
                else if(value == 1) {
                    if(new Date().getTime() > new Date(row.pushTime).getTime()) {
                        text='<span class="label label-success">已推送</span>';
                    } else {
                        text='<span class="label label-primary">预推送</span>';
                    }
                }
                else if(value == 2)text='<span class="label label-danger">推送失败</span>';
                return text; }
            },
            { label: '推送时间', name: 'pushTime', width: 80 },
            { label: '是否可分享', name: 'canShare', width: 80, formatter: function(value, options, row){
                var text = '';
                if(value == 0)text='<span class="label label-danger">否</span>';
                else if(value == 1)text='<span class="label label-success">是</span>';
                return text; }
            },
            { label: '创建用户', name: 'creationName', width: 80 },
            { label: '创建时间', name: 'creationTime', width: 80 },
            { label: '最近修改用户', name: 'modifiedName', width: 80 },
            { label: '最近修改日期', name: 'modifiedTime', width: 80 }
        ],
		viewrecords: true,
        height: 495,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            title:null
        },
        showList: true,
        title: null,
        customCard: {
            title:"",
            subtitle:"",
            cardUrl:"",
            cardBannerUrl:"",
            productId:"",
            productName:"",
            canShare:""
        }
    },
    methods: {
        query: function () {
            vm.reload(null, 1);
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.customCard = {
                title:"",
                subtitle:"",
                cardUrl:"",
                cardBannerUrl:"",
                productId:"",
                productName:"",
                canShare:""
            };
        },
        updateStatus: function (status, statusText) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            confirm('确定要'+statusText+'选中的记录？',function(){
                $.ajax({
                    type: "POST",
                    url: "../customcard/updateStatus?status="+status,
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code === 0) {
                            alert('操作成功', function (index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "查看";

            vm.getInfo(id)
        },
        saveOrUpdate: function (event) {
            if($.isNull(vm.customCard.title)){
                alert("请输入标题！！！")
                return;
            }
            if($.isNull(vm.customCard.subtitle)){
                alert("请输入副标题！！！")
                return;
            }
            if($.isNull(vm.customCard.cardUrl)){
                alert("请输入卡片地址！！！")
                return;
            }
            if($.isNull(vm.customCard.cardBannerUrl)){
                alert("请选择封面！！！")
                return;
            }
            if($.isNull(vm.customCard.productId)){
                alert("请选择产品线！！！")
                return;
            }
            if($.isNull(vm.customCard.canShare)){
                alert("请选择是否可分享！！！")
                return;
            }
            if(!$.isNull(vm.customCard.oliveTitle) && vm.customCard.oliveTitle.length > 100){
                alert("标题信息不得超过100个字！！！")
                return;
            }
            if (vm.title == "新增") {
                url = "../customcard/save";
            }
            else if (vm.title == "查看") {
                url = "../customcard/update";
            } else {
                url = "";
            }
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.customCard),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: "../customcard/delete",
                    data: JSON.stringify(ids),
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (cardId) {
            $.ajax({
                type: "POST",
                url: "/customcard/info/" + cardId,
                success: function (r) {
                    if (r.code === 0) {
                        vm.customCard = r.data;
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        reload: function (event, p) {
            vm.showList = true;
            var page = p || $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "title": vm.q.title
                },
                page: page
            }).trigger("reloadGrid");
        },
        resume:function(event){
            var cardIds = getSelectedRows();
            if(cardIds == null){
                return ;
            }
            confirm('确定要上架选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../customcard/resume",
                    data: JSON.stringify(cardIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        pause:function(event){
            var cardIds = getSelectedRows();
            if(cardIds == null){
                return ;
            }
            confirm('确定要下架选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../customcard/pause",
                    data: JSON.stringify(cardIds),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        selectProduct : function(){
            productLay.show(function(index,opt){
                vm.customCard.productId = opt.productId;
                vm.customCard.productName = opt.productName;
            });
        },
        clearQuery : function(){//清空查询条件
            var vm = this;
            vm.q.mobile = "";
            vm.q.status = "";
            $(".datetimepicker_start").val("");
            $(".datetimepicker_end").val("");
        },
        pushRulesLayerShow : function(){//推送规则浮层
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            //行详情
            var row = $("#jqGrid").jqGrid('getRowData',id);
            /*消息类型 3：公开课 4：自定义卡片 9：系统消息*/
            pushRulesLay.show(4, id, row.pushTime, function(index,opt){
            });
        },
        delMessage: function () {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            /*消息类型 3：公开课 4：自定义卡片 9：系统消息*/
            //行详情
            var row = $("#jqGrid").jqGrid('getRowData',id);
            confirm('确定要取消推送选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../pushrules/delMessage",
                    data: JSON.stringify({
                        "messageType":4,
                        "messageId":id,
                        "pushMsgId":row.pushMsgId
                    }),
                    success: function(r){
                        if(r.code == 0){
                            alert(r.msg, function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        }
    }
});