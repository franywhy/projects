$(function () {
    $(".insertTime").datetimepicker({
        format: 'yyyy-mm-dd',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
//	        todayHighlight: 1,//如果为true, 高亮当前日期。
//	        startView: 2,//日期时间选择器打开之后首先显示的视图。
//	        forceParse: 0,//当选择器关闭的时候，是否强制解析输入框中的值。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
//	        showMeridian: 1
    });
    $("#jqGrid").jqGrid({
        url: '../courseabnormalorder/list',
        datatype: "json",
        colModel: [
            { label: '单号', name: 'orderno', width: 80 },
            { label: '类型', name: 'abnormalTypeStr', width: 40 },
            { label: '订单编号', name: 'mallOrderNo', width: 120 },
            { label: '班型名称', name: 'classtypeName', width: 80 },
            { label: '省份', name: 'areaName', width: 80 },
            { label: '专业', name: 'professionName', width: 80 },
            { label: '层次', name: 'levelName', width: 80 },
            { label: '班级名称', name: 'className', width: 120 },
            { label: '班主任', name: 'teacherName', width: 80 },
            { label: '学员姓名', name: 'nickName', width: 80 },
            { label: '手机号码', name: 'mobile', width: 80 },
            { label: '报名时间', name: 'creationTimeStr', width: 80 },
            { label: '学籍有效期', name: 'dateValidityStr', width: 80 },
            // { label: '学员id', name: 'studentId', width: 80 },
            { label: '预计开始时间', name: 'startTimeStr', width: 80 },
            { label: '预计结束时间', name: 'expectEndTimeStr', width: 80 },
            { label: '复课时间', name: 'endTimeStr', width: 80 },
            { label: '原因', name: 'abnormalReason', width: 80 },
            { label: '备注', name: 'remark', width: 80 },
            { label: '提交时间', name: 'createTimeStr', width: 80 },
            { label: '状态', name: 'auditStatusStr', width: 40 },
        ],
		viewrecords: true,
        height: 385,
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
	el:'#rrapp',
	data:{
		showList: true,
        showAddOrUpdate:false,
        showResumeClasses:false,
		title: null,
		courseAbnormalOrder: {},
        selectData : {//下拉初始化列表
            //异常类型状态
            abnormalTypeList : [{value:1,name:'休学'} , {value:2,name:'失联'}],
            auditStatusList : [{value:0,name:'待审核'} , {value:2,name:'通过'},{value:1,name:'作废'}],
            classTeacherList:[]
        },
        qCourseAbnormalOrder:{//查询条件
            mallOrderNo : "",
            orderNo:"",
            mobile : "",
            studentId:"",
            nickName:"",
            auditStatus:"",
			abnormalType:"",
            classTeacherId:"",
            classId:"",
            className:"",
            startTime:"",
            endTime:"",
            areaId:"",
            professionId:"",
            levelId:"",
            classTypeId:""
		}
	},
	methods: {
		query: function () {
            vm.qCourseAbnormalOrder.startTime = $("#qStartTime").val();
            vm.qCourseAbnormalOrder.endTime = $("#qEndTime").val();
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
            vm.showResumeClasses = false;
			vm.showAddOrUpdate = true;
			vm.title = "新增";
			vm.courseAbnormalOrder = {};
            vm.courseAbnormalOrder.abnormalType = 1;
            vm.courseAbnormalOrder.startTimeStr = new Date().format("yyyy-MM-dd");
        },
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.showResumeClasses = false;
            vm.showAddOrUpdate = true;
            vm.title = "修改";
            
            vm.getInfo(id);
		},
        resumeClasses : function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            var rowData = $("#jqGrid").jqGrid('getRowData',id);
            if(rowData.auditStatusStr != "通过"){
                alert("审核未通过，不允许复课！");
                return;
            }
            if(rowData.endTimeStr){
                var nowDate = new Date();
                var endTime = new Date(rowData.endTimeStr);
                if(nowDate >= endTime){
                    alert("当前日期已超过复课时间，不允许复课！");
                    return;
                }
            }
            vm.showList = false;
            vm.showAddOrUpdate = false;
            vm.showResumeClasses = true;
            vm.title = "复课";
            vm.getInfo(id);
        },
		saveOrUpdate: function (event) {
            vm.courseAbnormalOrder.startTime = $("#add-startTime").val();
            vm.courseAbnormalOrder.expectEndTime = $("#add-expectEndTime").val();
		    if(vm.title == "新增")
		    {
                vm.courseAbnormalOrder.auditStatus = 2;//默认审核通过
		       url = "../courseabnormalorder/save";
		    }
		    else if(vm.title == "修改")
		    {
		    	if(vm.showAddOrUpdate){
                    vm.courseAbnormalOrder.auditStatus = null;
				}
		       url = "../courseabnormalorder/update";
		    }else
		    {
		       url = "";
		    }
            // if(!vm.courseAbnormalOrder.studentId){
             //    alert('请选择学员');
		    	// return;
			// }
            if(!vm.courseAbnormalOrder.abnormalType){
                alert('请选择异常类型');
                return;
            }
            if(!vm.courseAbnormalOrder.orderId){
                alert('请选择订单');
                return;
            }
            if(!vm.courseAbnormalOrder.startTime){
                alert('请选择预计开始时间');
                return;
            }
            if(!vm.courseAbnormalOrder.expectEndTime){
                alert('请选择预计结束时间');
                return;
            }
            if(vm.courseAbnormalOrder.startTime>=vm.courseAbnormalOrder.expectEndTime){
                alert('预计开始时间不能大于等于预计结束时间');
                return;
            }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseAbnormalOrder),
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
		del: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var errorMsg = '以下审核通过的单不允许进行作废：';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.auditStatusStr == '通过'){
                    errorMsg += rowData.orderno+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }
			confirm('确定要作废选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseabnormalorder/updateCancel",
                    contentType: "application/json",
				    data: JSON.stringify(ids),
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
		getInfo: function(id){
            $.ajax({
				type: "POST",
			    url: "/courseabnormalorder/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseAbnormalOrder = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (e , p) {
			vm.showList = true;
			vm.showAddOrUpdate = false;
            vm.showResumeClasses = false;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData : vm.qCourseAbnormalOrder,
                datatype: "json",
                page:page
            }).trigger("reloadGrid");
		},
        areaLayerShow : function(){//省份浮层
            areaLay.show(function(index,opt){
                vm.qCourseAbnormalOrder.areaId = opt.areaId;
                $('#query-areaName').val( opt.areaName);
            });
        },
        levelLayerShow : function(){//层次浮层
            levelLay.show(function(index,opt){
                vm.qCourseAbnormalOrder.levelId = opt.levelId;
                $('#query-levelName').val( opt.levelName);
            });
        },
        professionLayerShow : function(){//专业浮层
            professionLay.show(function(index,opt){
                vm.qCourseAbnormalOrder.professionId = opt.professionId;
                $('#query-professionName').val( opt.professionName);
            },2);
        },
        classTypeLayerShow : function(){//班型浮层
            classTypeLay.show(function(index,opt){
                vm.qCourseAbnormalOrder.classTypeId = opt.classtypeId;
                $('#query-classTypeName').val(opt.classtypeName);
            });
        },
        classTeacherLayerShow : function(){//班主任
            teacherLay.show(function(index,opt){
                vm.qCourseAbnormalOrder.classTeacherId = opt.userId;
                $('#query-classTeacherName').val(opt.nickName);
            } , 2);
        },
        classLayerShow : function(){//班级浮层
            classLay.show(function(index,opt){
                vm.qCourseAbnormalOrder.classId = opt.classId;
                vm.qCourseAbnormalOrder.className = opt.className;
            });
        },
        userInfoLayerShow : function(){//用户信息浮层
            userInfoLay.show(function(index,opt){
                vm.courseAbnormalOrder.studentId = opt.userId;
                $('#add-studentName').val(opt.nickName);
                $('#add-mobile').val(opt.mobile);
            });
        },
        orderInfoLayerShow : function(){//订单信息浮层
            mallorderLay.show(function(index,opt){
				vm.courseAbnormalOrder.orderId = opt.orderId;
				$('#add-orderName').val(opt.orderNo);
			});
		},
		clearQcousrAbnormalOrder:function () {
            vm.qCourseAbnormalOrder = {//查询条件
                orderNo:"",
                mallOrderNo : "",
                    mobile : "",
                    studentId:"",
                    nickName:"",
                    auditStatus:"",
                    abnormalType:"",
                    classTeacherId:"",
                    classId:"",
                    className:"",
                    startTime:"",
                    endTime:"",
                    areaId:"",
                    professionId:"",
                    levelId:"",
                    classTypeId:""
            }
            vm.reload(null,1);
        },
        downExcelTemplate : function () {
            var urlstr = "/courseabnormalorder/downExcelTemplate";
            window.location.href = urlstr;
        },
        importExcel : function () {
            $("#fileUploadLayer").val("");
            layer.open({
                type : 1,//
                area : ['350px','200px'],
                title :"批量导入",
                closeBtn : 1,
                skin:"layui-layer-lan",
                content : $("#fileUploadLayer"),
                scrollbar : true,//是否允许浏览器出现滚动条
                fixed : false,//固定
                shadeClose : false,// 是否点击遮罩关闭
                resize : true,//是否允许拉伸
                maxmin: true, //开启最大化最小化按钮
                zIndex : 19891014,
                btn : ['确认','取消' ],
                btn1: function (index) {
                    var file = $('#file_data').val();
                    if(file){
                        var fileType = file.substring(file.indexOf('.'),file.length);
                        if(fileType != '.xls'){
                            alert('文件类型必须为.xls');
                            return;
                        }
                    }else{
                        alert('请选择文件');
                        return;
                    }
                    $.ajaxFileUpload({
                        url:'../courseabnormalorder/importExcel',
                        secureuri:true,
                        fileElementId:'file_data',
                        // dataType:'json',
                        success:function(data){
                            console.log(data);
                            if(data.code == 0) {
                                alert(data.data ||"批量导入成功", function(alertIndex){
                                    vm.reload();
                                    layer.close(index);
                                });
                            }else{
                                alert(data.msg);
                            }
                        }
                    });
                }
            });
        },
        auditPass: function(event){
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var errorMsg = '已作废的单据不允许审核通过：';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.auditStatusStr == '作废'){
                    errorMsg += rowData.orderno+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }
            $.ajax({
                type: "POST",
                url: "/courseabnormalorder/auditPass",
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function(r){
                    if(r.code === 0){
                        if(r.data){
                            var re = new RegExp(";","g"); //定义正则表达式
                            r.data = r.data.replace(re, "<br>");
                        }
                        alert(r.data || '操作成功！');
                        vm.reload(null,1);
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        auditCancel: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var errorMsg = '以下单据不允许反审核：';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.auditStatusStr == '作废' || rowData.endTimeStr){
                    errorMsg += rowData.orderno+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }
            $.ajax({
                type: "POST",
                url: "../courseabnormalorder/updateAuditCancel",
                contentType: "application/json",
                data: JSON.stringify(ids),
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
        },
        saveResumeClasses:function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }
            var rowData = $("#jqGrid").jqGrid('getRowData',id);
            vm.courseAbnormalOrder.endTime = $("#endTime").val();
            if(!vm.courseAbnormalOrder.endTime){
                alert('请选择结束时间');
                return;
            }
            if(rowData.startTimeStr >= vm.courseAbnormalOrder.endTime){
                alert('复课时间不能小于等于预计开始时间！');
                return;
            }
            $.ajax({
                type: "POST",
                url: '/courseabnormalorder/saveResumeClasses',
                data: JSON.stringify(vm.courseAbnormalOrder),
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
        }
	}
});