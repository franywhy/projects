$(function () {
    $("#jqGrid").jqGrid({
        url: '../courses/courseabnormalclassplan/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true , hidden:true },
			{ label: '异常类型(0-正常 1-休学 2-失联)', name: 'abnormalType', index: 'abnormal_type', width: 80 ,hidden:true },
            { label: '订单PK', name: 'orderId', index: 'order_id', width: 80 ,hidden:true },
			{ label: '排课PK', name: 'classplanId', index: 'classplan_id', width: 80 ,hidden:true },
            { label: '审核状态(0-审核中 1-取消  2-通过)', name: 'auditStatus', index: 'audit_status', width: 80 ,hidden:true },
            { label: '单号', name: 'abnormalClassplanNo', index: 'abnormal_classplan_no', width: 80 ,},
            { label: '排课计划', name: 'classplanName', index: 'classplan_id', width: 120 ,},
            { label: '课程ID', name: 'courseId', index: 'course_id', width: 30 ,},
            { label: '课程', name: 'courseName', index: 'course_id', width: 80 ,},
            { label: '学员ID', name: 'studentId', index: 'student_id', width: 50 },
            { label: '学员名称', name: 'studentName', index: 'student_id', width: 80 },
            { label: '手机号码', name: 'studentMobile', index: 'student_id', width: 50 },
            { label: '异常类型', name: 'abnormalTypeName', index: 'abnormal_type', width: 40 ,},
			{ label: '审核状态', name: 'auditStatusName', index: 'audit_status', width: 40 ,},
            { label: '异常原因', name: 'abnormalReason', index: 'abnormal_reason', width: 80 },
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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
	el:'#rrapp',
	data:{
		showList: true,
		showedit:false,
		title: null,
        q:{//课程搜索条件
            abnormalClassplanNo : null,
            studentId : null,
            studentMobile : null,
            abnormalType : "",
            courseId:null,
            classplanId : null,
            classplanName : null,
            auditStatus : ""
        },
		courseAbnormalClassplan: {
            id:null,
            abnormalClassplanNo:null,
            studentId:null,
            studentName:null,
            studentMobile:null,
            abnormalType:3,
            abnormalTypeName:"弃考",
            courseId:null,
            courseName:null,
            classplanId:null,
            classplanName:null,
            auditStatus:0,
            abnormalReason:null,
            remark:null,
		},

	},
	methods: {
		showTab : function(index){//显示列表
			switch (index){
				case 1 :
                    vm.showList = true;
                    vm.showedit = false;
                    break;
				case 2 :
                    vm.showList = false;
                    vm.showedit = true;
                    break;
				default :
                    vm.showList = true;
                    vm.showedit = false;
                    break;
			}
		},
        clearQuery : function(){//清空查询条件
			vm.q = {//课程搜索条件
                abnormalClassplanNo : null,
                    studentId : null,
                    studentMobile : null,
                    abnormalType : "",
                    courseId : null,
                    classplanId : null,
                    classplanName : null,
                    auditStatus : ""
            };
		},
		clearCourseAbnormalClassplan : function(){//清空编辑对象
            vm.courseAbnormalClassplan = {
                id:null,
				abnormalClassplanNo:null,
				studentId:null,
				studentName:null,
				studentMobile:null,
				abnormalType:3,
				abnormalTypeName:"弃考",
				orderId:null,
				orderNo:null,
				orderName:null,
				courseId:null,
				courseName:null,
				classplanId:null,
				classplanName : null,
				auditStatus : 2,
				/*auditStatusName : "待审核",*/
				abnormalReason : null,
				remark:null
            };
		},
		query: function () {//查询
			vm.reload(null , 1);
		},
		add: function(){//新增按钮
            vm.showTab(2);
			vm.title = "新增";
			vm.clearCourseAbnormalClassplan();
		},

		update: function (event) {//修改按钮
            var selectId = getSelectedRow();
            if(selectId == null){
                return ;
            }
            vm.showTab(2);
            vm.title = "修改";
            
            vm.getInfo(selectId)
		},
		saveOrUpdate: function (event) {//新增或修改
			// console.log(vm.courseAbnormalClassplan);
			var url = vm.courseAbnormalClassplan.id == null ? "../courses/courseabnormalclassplan/save" : "../courses/courseabnormalclassplan/update";
			$.ajax({
				type: "POST",
			    url: url,
                contentType: "application/json",
			    data: JSON.stringify(vm.courseAbnormalClassplan),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
        /////////////////////////////////////排课计划
        showQueryClassplanLay : function(){//排课计划弹窗
            classplanLay.show(function(selectDetail , rowData){
                vm.q.classplanId = selectDetail;
                vm.q.classplanName = rowData.classplanName;
            });
        },
        clearQueryClassplanLay : function(){//清空选择的排课计划内容
            vm.q.classplanId = "";
            vm.q.classplanName = "";
        },
		/////////////////////////////////////排课计划
        showEditClassplanLay : function(){//排课计划弹窗
            classplanLay.show(function(selectDetail , rowData){
            	vm.courseAbnormalClassplan.classplanId = selectDetail;
            	vm.courseAbnormalClassplan.classplanName = rowData.classplanName;
            	// vm.courseAbnormalClassplan.courseId = rowData.courseId;
            	// vm.courseAbnormalClassplan.courseName = rowData.courseName;
			});
        },
        clearEditClassplanLay : function(){//清空选择的排课计划内容
            vm.courseAbnormalClassplan.classplanId = "";
            vm.courseAbnormalClassplan.classplanName = "";
            vm.courseAbnormalClassplan.studentId = "";
            vm.courseAbnormalClassplan.studentName = "";
            vm.courseAbnormalClassplan.studentMobile = "";
        },
		////////////////////////////////学员
		showEditUserLay : function(){//学员弹窗
			if($.isNull(vm.courseAbnormalClassplan.classplanId)){
                confirm('请先选择排课计划，是否现在选择排课计划？', function(){
                	vm.showEditClassplanLay();
                	return;
				});
                return;
			}
            usersByclassplanLay.show(vm.courseAbnormalClassplan.classplanId , function(selectDetail , rowData){
				vm.courseAbnormalClassplan.studentId = rowData.userId;
				vm.courseAbnormalClassplan.studentName = rowData.userName;
				vm.courseAbnormalClassplan.studentMobile = rowData.mobile;
			});
		},
        clearEditUser : function(){//清除学员信息
            vm.courseAbnormalClassplan.studentId = "";
            vm.courseAbnormalClassplan.studentName = "";
            vm.courseAbnormalClassplan.studentMobile = "";
        },
        updateCancel: function (event) {//取消
            var selectId = getSelectedRow();
            if(selectId == null){
                return ;
            }
            var selectDetail = getJqGridSelectedRow("jqGrid");
            if(selectDetail == null){
                return ;
            }
            if(selectDetail.auditStatus == 1){
                alert("改单已经处于取消状态！");
                return;
            }

            confirm('确定要取消选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../courses/courseabnormalclassplan/updateCancel",
                    contentType: "application/json",
                    data: JSON.stringify(selectId),
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
        updateAdopt: function (event) {//审核
            var selectId = getSelectedRow();
            if(selectId == null){
                return ;
            }
            var selectDetail = getJqGridSelectedRow("jqGrid");
            if(selectDetail == null){
                return ;
            }
            if(selectDetail.auditStatus == 2){
                alert("改单已经处于审核通过状态！");
                return;
            }

            confirm('确定审核通过选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../courses/courseabnormalclassplan/updateAdopt",
                    contentType: "application/json",
                    data: JSON.stringify(selectId),
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
		getInfo: function(id){//获取详情
			$.get("../courses/courseabnormalclassplan/info/"+id, function(r){
                vm.courseAbnormalClassplan = r.data;
            });
		},
		reload: function (event , page) {//单据列表刷新
			vm.showTab(1);
			var page = page || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData : vm.q,
                page:page
            }).trigger("reloadGrid");
		},
        downExcelTemplateCourse : function(){//下载模板
            var urlstr = "../courses/courseabnormalclassplan/downExcelTemplateCourse";
            window.location.href = urlstr;
        },
        importExcelTemplateCourse : function(){//批量上传
            $("#fileUploadLayer").val("");
            $("#file_data").val("");
            layer.open({
                type: 1,//
                area: ['350px', '200px'],
                title: "批量上传",
                closeBtn: 1,
                skin: "layui-layer-lan",
                content: $("#fileUploadLayer"),
                scrollbar: true,//是否允许浏览器出现滚动条
                fixed: false,//固定
                shadeClose: false,// 是否点击遮罩关闭
                resize: true,//是否允许拉伸
                maxmin: true, //开启最大化最小化按钮
                zIndex: 19891014,
                btn: ['确认', '取消'],
                btn1: function (index) {
                    var file = $('#file_data').val();
                    if (file) {
                        var fileType = file.substring(file.indexOf('.'), file.length);
                        if (fileType != '.xls') {
                            alert('文件类型必须为.xls');
                            return;
                        }
                    } else {
                        alert('请选择文件');
                        return;
                    }
                    $.ajaxFileUpload({
                        url: '../courses/courseabnormalclassplan/importExcelTemplateCourse',
                        secureuri: true,
                        fileElementId: 'file_data',
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == 0) {
                                alert(data.data || "批量导入成功", function (alertIndex) {
                                    vm.reload();
                                    layer.close(index);
                                });
                            } else {
                                alert(data.msg);
                            }
                        }

                    });
                }
            });
        },
	}
});