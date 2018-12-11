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
        url: '../activityjoininfo/list',
        datatype: "json",
        colModel: [		
			{ label: 'id', name: 'id', hidden: true, key: true },
            { label: '活动名称', name: 'activityName', width: 80 },
			{ label: '姓名', name: 'name', width: 80 },
            { label: '手机号码', name: 'mobile', width: 80 },
            { label: '图片', name: 'pic', width: 30 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}},
			{ label: '文字', name: 'description', width: 80 },
			{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
				var text = '';
				if(value == 0)text='<span class="label label-danger">下架</span>';
				else if(value == 1)text='<span class="label label-success">上架</span>';
				return text; }
			},
            { label: '创建时间', name: 'createTime', width: 80 }
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
            content: "",
            nickName: "",
            mobile: "",
            activityCode: ""
        },
        showList: true,
        title: null,
        activityJoinInfo: {}
    },
    methods: {
        query: function () {
            vm.reload(null, 1);
        },
        add: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.activityJoinInfo = {};
        },
        updateStatus: function (status, statusText) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }
            confirm('确定要'+statusText+'选中的记录？',function(){
                $.ajax({
                    type: "POST",
                    url: "../activityjoininfo/updateStatus?status="+status,
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
            if (vm.title == "新增") {
                url = "../activityjoininfo/save";
            }
            else if (vm.title == "查看") {
                url = "../activityjoininfo/update";
            } else {
                url = "";
            }
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.activityJoinInfo),
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
                    url: "../activityjoininfo/delete",
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
        getInfo: function (id) {
            $.ajax({
                type: "POST",
                url: "/activityjoininfo/info/" + id,
                success: function (r) {
                    if (r.code === 0) {
                        vm.activityJoinInfo = r.data;
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        reload: function (event, p) {
            vm.showList = true;
            vm.q.startTime = $(".datetimepicker_start").val();
            vm.q.endTime = $(".datetimepicker_end").val();
            var page = p || $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: {
                    "mobile": vm.q.mobile,
                    "status": vm.q.status,
                    "startTime": vm.q.startTime,
                    "endTime": vm.q.endTime,
                    "activityCode": vm.q.activityCode
                },
                page: page
            }).trigger("reloadGrid");
        },
        clearQuery : function(){//清空查询条件
            var vm = this;
            vm.q.mobile = "";
            vm.q.status = "";
            vm.q.activityCode = "";
            $(".datetimepicker_start").val("");
            $(".datetimepicker_end").val("");
        }
    }
});