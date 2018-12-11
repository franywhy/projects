$(function () {
	//
	$(".exam-Date").datetimepicker({
	 	format: 'yyyy-mm-dd',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        todayHighlight: 1,//如果为true, 高亮当前日期。
        startView: 2,//日期时间选择器打开之后首先显示的视图。
        forceParse: 0,//当选择器关闭的时候，是否强制解析输入框中的值。
        minView: 2,//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
        showMeridian: 1
	});
	
	//
    $("#jqGrid").jqGrid({
        url: '../mall/mallexpcertificate/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', width: 50, key: true },
			{ label: '姓名', name: 'name', width: 80 }, 			
			{ label: '身份证号', name: 'card', width: 80 }, 	
			{ label: '手机号', name: 'mobile', width: 80 }, 	
			{ label: '报读课程', name: 'course', width: 80 }, 			
			
			{ label: '报读日期', name: 'readDate', width: 80 }, 			
			{ label: '结课日期', name: 'endDate', width: 80 }, 			
			{ label: '经验等级', name: 'exp', width: 80,hidden : true }, 
			{ label: '经验等级', name: 'expText', width: 80, formatter: function(value, options, row){
				var value = row.exp;
				var text = '';
				if(value == 1)text= '一级';
				else if(value == 2)text= '二级';
				else if(value == 3)text= '三级';
				else if(value == 4)text= '四级';
				else if(value == 5)text= '五级';
				else if(value == 6)text= '六级';
				else if(value == 7)text= '七级';
				else if(value == 8)text= '八级';
				else if(value == 9)text= '九级';
				else if(value == 10)text= '十级';
				else if(value == 11)text= '十一级';
				else if(value == 12)text= '十二级';
				else if(value == 13)text= '十三级';
				return text;
			}},
			{ label: '所属校区', name: 'schoolName', width: 80 }, 	
			{ label: '颁发日期', name: 'sendDate', width: 80 }, 		
			{ label: '发放状态 ', name: 'sendStatus', width: 80 ,hidden : true },
			{ label: '状态', name: 'sendstatus', width: 80, formatter: function(value, options, row){
				var value = row.sendStatus;
				var text = '';//0:未发放 1:发放中 2:已发放
				if(value == 0)text= '未发放';
				else if(value == 1)text= '发放中';
				else if(value == 2)text= '已发放';
				return text;
			}},
			{ label: '证书类型', name: 'typeName', width: 80, formatter: function(value, options, row){
				var value = row.type;
				var text = '';//0:未发放 1:发放中 2:已发放
				if(value == 1)text= '恒企经验';
				else if(value == 2)text= '中央财大';
				
				return text;
			}}
						
			/*{ label: '状态:0.禁用;1.启用', name: 'status', width: 80 }, 			
			{ label: '平台ID', name: 'schoolId', width: 80 }, 			
			{ label: '创建用户', name: 'createPerson', width: 80 }, 			
			{ label: '创建时间', name: 'creationTime', width: 80 }, 			
			{ label: '最近修改用户', name: 'modifyPerson', width: 80 }, 			
			{ label: '最近修改日期', name: 'modifiedTime', width: 80 }	*/		
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
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
	el:'#rrapp',
	data:{
		showList: true,
		showView: false,
		showType: false,
		title: null,
		mallExpCertificate: {

			school:0,
			schoolName:""
		},
		q : { //查询条件
			name : "",
			schoolName : "",
			school:0,
			course : "",
			card : "",

			sendStatus : -1,
			type: -1,

            schoolIdList:"",
            schoolNameList:""

		},
        exportTemplate: function (event) {
            var urlstr = "../mall/mallexpcertificate/exportExcelMallExpCertificateTemplate";
            window.location.href = urlstr;
        },
		statusOptionsSelect: [
 			{ text: '全部',value: -1 },
 			{ text: '未发放', value: 0 },
 			{ text: '发放中', value: 1 },
 			{ text: '已发放', value: 2 }

 		],
 		typeOptionsSelect: [
 			{ text: '全部', value: -1 },
 			{ text: '恒企经验', value: 1 },
 			{ text: '中央财大', value: 2 }
 		],
 		//1:恒企经验,2:中央财大
 		typeOptions: [
 			{ text: '恒企经验', value: 1 },
 			{ text: '中央财大', value: 2 }
 		],

		//0:未发放 1:发放中 2:已发放
		statusOptions: [
 			{ text: '未发放', value: 0 },
 			{ text: '发放中', value: 1 },
 			{ text: '已发放', value: 2 }
 		],

 		expOptions: [
 			{ text: '一级', value: 1 },
 			{ text: '二级', value: 2 },
 			{ text: '三级', value: 3 },
 			{ text: '四级', value: 4 },
 			{ text: '五级', value: 5 },
 			{ text: '六级', value: 6 },
 			{ text: '七级', value: 7 },
 			{ text: '八级', value: 8 },
 			{ text: '九级', value: 9 },
 			{ text: '十级', value: 10 },
 			{ text: '十一级', value: 11 },
 			{ text: '十二级', value: 12 },
 			{ text: '十三级', value: 13 }
 		]

	},
	methods: {
        query: function () {
            vm.reload();
        },
        clearQuery: function (event) {//重置查询条件
            vm.q = {//商品查询条件
                name: "",
                schoolName: "",
                school: 0,
                course: "",
                card: "",
                sendStatus: -1,
                type: -1,
                schoolIdList: "",
                schoolNameList: ""
            }
        },
        deptLayerShow: function () {//部门
            deptLay.show(function (id, name, opt) {
            	//console.log(opt);
                vm.mallExpCertificate.school = id;
                vm.mallExpCertificate.schoolName = name;
                $("#schoolText").val(name);
            });
        },
        deptLayerShowSelect: function () {//部门
            /*deptLay.show(function(id , name ,opt){
             vm.q.school = id;
             vm.q.schoolName = name;
             $("#schoolTextSelect").val(name);
             });*/
            deptQueryLay.show(function (opt, deptIdList, deptNameList) {
                vm.q.schoolIdList = deptIdList.join(",");
                vm.q.schoolNameList = deptNameList;
            });
        },
        userInfoLayerShow: function () {//用户信息浮层
            userInfoLay.show(function (index, opt) {
                vm.mallExpCertificate.userId = opt.userId;
                vm.mallExpCertificate.mobile = opt.mobile;
                $("#userId").val(opt.userId);
                $("#mobile").val(opt.mobile);
            });
        },
        add: function () {
            vm.showList = false;
            vm.showView = false;
            vm.title = "新增";
            vm.mallExpCertificate = {};
        },
        update: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.showView = false;
            vm.title = "修改";

            vm.getInfo(id)
        },
        showViews: function (event) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            vm.showList = false;
            vm.showView = true;
            vm.showType = false;
			vm.showType = false;
            vm.title = "预揽";

            vm.getInfo(id);
            vm.showType = (vm.mallExpCertificate.type == 1 );

        },
        saveOrUpdate: function (event) {

            //做新增或保存时获取开课日期的值
            vm.mallExpCertificate.readDate = $("#readdate").val();
            vm.mallExpCertificate.endDate = $("#enddate").val();
            vm.mallExpCertificate.sendDate = $("#senddate").val();
            vm.mallExpCertificate.userId = $("#userId").val();

            if ($.isNull(vm.mallExpCertificate.name)) {
                alert("姓名不能为空，请输入！！！");
                return;
            }
            if ($.isNull(vm.mallExpCertificate.card)) {
                alert("身份证不能为空，请输入！！！");
                return;
            }
           /* if ($.isNull(vm.mallExpCertificate.mobile)) {
                alert("手机号不能为空，请输入！！！");
                return;
            }*/
            if ($.isNull(vm.mallExpCertificate.readDate)) {
                alert("报读日期不能为空，请输入！！！");
                return;
            }
            if ($.isNull(vm.mallExpCertificate.endDate)) {
                alert("结课日期不能为空，请输入！！！");
                return;
            }
            if ($.isNull(vm.mallExpCertificate.course)) {
                alert("报读课程不能为空，请输入！！！");
                return;
            }
           /*if ($.isNull(vm.mallExpCertificate.exp)) {
                alert("经验等级不能为空，请输入！！！");
                return;
            }
            if ($.isNull(vm.mallExpCertificate.certNo)) {
                alert("证书编号不能为空，请输入！！！");
                return;
            }
            if ($.isNull(vm.mallExpCertificate.school)) {
                alert("校区不能为空，请输入！！！");
                return;
            }*/
            if ($.isNull(vm.mallExpCertificate.sendDate)) {
                alert("颁发日期不能为空，请输入！！！");
                return;
            }
            if ($.isNull(vm.mallExpCertificate.sendStatus)) {
                alert("发放状态不能为空，请输入！！！");
                return;
            }
            /*if ($.isNull(vm.mallExpCertificate.courseRemark)) {
                alert("主要课程不能为空，请输入！！！");
                return;
            }*/
            if ($.isNull(vm.mallExpCertificate.type)) {
                alert("证书类型不能为空，请输入！！！");
                return;
            }
            var url = vm.mallExpCertificate.id == null ? "../mall/mallexpcertificate/save" : "../mall/mallexpcertificate/update";
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.mallExpCertificate),
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

            confirm('是否删除已选数据，删除后将无法查看?', function () {
                $.ajax({
                    type: "POST",
                    url: "../mall/mallexpcertificate/delete",
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

        updateStatus: function (event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }


            var isExists = false;
            for (var i = 0; i < ids.length; i++) {
                var rowdata = $("#jqGrid").jqGrid('getRowData', ids[i]);
                if (rowdata["sendStatus"] == 2) {
                    isExists = true;
                }
            }
            var str = '确定要更新状态选中的记录？';
            if (isExists && ids.length > 1) {
                str = '你选中的内容存在“已发放”状态，是否继续修改？';
            } else {
                //
                var rowdata = $("#jqGrid").jqGrid('getRowData', ids[0]);

                if (rowdata["sendStatus"] == 2) {
                    str = '该条信息已经是“已发放”状态，是否继续修改？';
                }

            }

            confirm(str, function () {
                $.ajax({
                    type: "POST",
                    url: "../mall/mallexpcertificate/updateStatus",
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
            $.get("../mall/mallexpcertificate/info/" + id, 
            		function (r) {
                vm.mallExpCertificate = r.mallExpCertificate;
            });
        },
        reload: function (event) {
            vm.showList = true;
            //TODO 商品查询数据清空问题?
            $('#jqGridDetail').clearGridData(true);
            /*var page = p||$("#jqGrid").jqGrid('getGridParam', 'page');
             $("#jqGrid").jqGrid('setGridParam', {
             postData : vm.q,
             datatype: "json",
             page : page
             }).trigger("reloadGrid");*/
            var page = $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                postData: vm.q,
                datatype: "json",
                page: page
            }).trigger("reloadGrid");
        },
        uploadExcelMethod: function () {
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
                        url: '../mall/mallexpcertificate/getExcelMallExpCertificateData',
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
        }
    }
});