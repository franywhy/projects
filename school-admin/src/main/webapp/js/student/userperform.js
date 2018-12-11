$(function () {

    //
    $(".exam-Date").datetimepicker({
        format: 'yyyy-mm-dd',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn: 1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        todayHighlight: 1,//如果为true, 高亮当前日期。
        startView: 2,//日期时间选择器打开之后首先显示的视图。
        forceParse: 0,//当选择器关闭的时候，是否强制解析输入框中的值。
        minView: 2,//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
        showMeridian: 1
    });
    //
    $("#jqGrid").jqGrid({
        url: '../userPerform/list',
        datatype: "json",
        colModel: [
            {label: 'id', name: 'id', key: true, hidden: true},
            {label: '商品', name: 'commodityName', width: 80},
            {label: '学员ID', name: 'userId', width: 80},
            {label: '姓名', name: 'userNickname', width: 80},
            {label: '时间', name: 'creationTime', width: 80,formatter : function(value, options, row){
            	if(value != null && value != ""){ 
            		var time = value.split(" ");
            		if( time.length > 0 ){
            			return time[0];
            		}
            	}
            	return null;
            }},
            {label: '奖学金名称', name: 'scholarshipName', width: 80},
            {label: '奖学金金额', name: 'scholarship', width: 80},
            {label: '评价', name: 'perform', width: 80},
            {label: '班主任', name: 'teacher', width: 80}

        ],
        viewrecords: true,
        height: 385,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 35,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
});


var vm = new Vue({
    el: '#rrapp',
    data: {
        showList: true,
        title: null,
        q: {
            userNickname: '',
            teacher:''
        },
        userPerform: {}

    },
    methods: {
        query: function () {
            vm.reload();
        },
        //清空查询条件
        queryClear: function () {
            vm.q.userNickname = '';
            vm.q.teacher = '';
        },

        reload: function (e, p) {
            vm.showList = true;
            var page = p || $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                page: page,
                postData: {
                    'userNickname': encodeURIComponent(vm.q.userNickname),
                    'teacher' : encodeURIComponent(vm.q.teacher)
                }
            }).trigger("reloadGrid");
        },
        //删除
        del: function (event) {
            var ids = getSelectedRows();
            console.log(ids);
            if (ids == null) {
                return;
            }
            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: "../userPerform/delete",
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
        //下载模板
        downTemplate: function () {
            var urlstr = "../userPerform/downTemplate";
            window.location.href = urlstr;
        },
        //导出数据
        batchImport: function () {
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: '导入Excel',
                area: ['300px', '200px'],
                shadeClose: false,
                content: jQuery("#uploadExcel"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    $.ajaxFileUpload({
                        url: '../userPerform/batchImport',
                        secureuri: true,
                        fileElementId: 'file_data',
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == 0) {
                                alert("文件上传成功！！！" + "<br/>" + data.msg, function (index) {
                                    $("#jqGrid").trigger("reloadGrid");
                                });
                                layer.close(index);
                            } else if (data.code == 1) {
                                alert(data.msg);
                            }
                        }
                    });
                }
            });
        },
        //修改
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        //新增
        save: function () {
            vm.showList = false;
            vm.title = "新增";
            vm.userPerform = {
                //商品名称
                commodityName: "",
                //学员ID
                userId: "",
                //学员姓名
                userNickname: "",
                //时间
                creationTime: "",
                //奖学金名称
                scholarshipName: "",
                //奖学金金额
                scholarship: "",
                //评价
                perform: ""
            };
            $("#creationTime").val("");//新增时让奖学金获取日期置空
        },

        saveOrUpdate: function (event) {
            //做新增或保存时获取开课日期的值
            vm.userPerform.creationTime = $("#creationTime").val();

            if (vm.title == "新增") {
                url = "../userPerform/save";
            }
            else if (vm.title == "修改") {
                url = "../userPerform/update";
            } else {
                url = "";
            }
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.userPerform),
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
        //学员名字信息浮层
        userInfoLayerShow1: function () {
            userInfoLay.show(function (index, opt) {
                //console.log(opt);
                vm.userPerform.userId = opt.userId;
                vm.userPerform.userNickname = opt.nickName;
                $("#userId").val(opt.userId);
                $("#userNickname").val(opt.nickName);
            });
        },
        //班主任
       /* teacherLayerShow : function(){
            teacherLay.show(function(index,opt){
                vm.q.teacher = opt.nickName;
            });
        },*/
        classTeacherLayerShow : function(){
            teacherLay.show(function(index,opt){
                vm.q.sysUserId = opt.userId;
                vm.q.teacher = opt.nickName;
            } , 2);
        },
        /*//编辑-班主任
        selectTeacher : function(){
            teacherLay.show(function(index,opt){
                //console.log(opt);
                vm.userPerform.userId = opt.userId;
                vm.userPerform.teacher = opt.nickName;

            });
        },*/

        //获取用户信息
        getInfo: function (id) {
            $.get("../userPerform/info/" + id, function (r) {
                vm.userPerform = r.userPerform;
                //查询信息时获取并显示奖学金获取日期
                $("#creationTime").val(vm.userPerform.creationTime);
            });
        }
    }
});