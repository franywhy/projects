$(function () {
    $("#jqGrid").jqGrid({
    	url: '../courseabnormalregistration/queryRegistrationList',
        datatype: "local",
        colModel: [			
			{ label: '序', name: 'id', index: 'id', width: 50, key: true },
			{ label: '单据号', name: 'registrationNo', index: 'registrationNo', width: 80 }, 			
			{ label: '订单编号', name: 'orderNo', index: 'orderNo', width: 80 }, 			
			{ label: '班型名称', name: 'classtypeName', index: 'classtypeName', width: 80 }, 			
			{ label: '课程科目', name: 'courseName', index: 'courseName', width: 80 }, 			
			{ label: '报读省份', name: 'bdAreaName', index: 'bdAreaName', width: 80 }, 			
			{ label: '报考省份', name: 'bkAreaName', index: 'bkAreaName', width: 80 }, 			
			{ label: '专业', name: 'professionName', index: 'professionName', width: 80 }, 			
			{ label: '层次', name: 'levelName', index: 'levelName', width: 80 }, 			
			{ label: '班级名称', name: 'className', index: 'className', width: 80 }, 			
			{ label: '班主任', name: 'sysUserName', index: 'sysUserName', width: 80 }, 			
			{ label: '昵称', name: 'nickName', index: 'nickName', width: 80 }, 			
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 }, 			
			{ label: '报考时间段名称', name: 'scheduleName', index: 'scheduleName', width: 80 }, 			
			{ label: '报考时间', name: 'scheduleDate', index: 'scheduleDate', width: 80 }, 			
			{ label: '产品线', name: 'productName', index: 'productName', width: 80 }, 			
			{ label: '报考登记号', name: 'registrationNumber', index: 'registrationNumber', width: 80 },		
			{ label: '提交时间', name: 'registrationTime', index: 'registrationTime', width: 80 },
//			{ label: '状态', name: 'status', index: 'status', width: 80 }	
			{ label: '状态', name: 'statusName', width: 80 ,formatter: function(v, options, row){
				var value = row.status;
				var text = '';
				if(value == 0)text= '待审核';
				else if(value == 1)text= '作废';
				else if(value == 2)text= '通过';
				return text;
			}}
			
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
		q:{
			orderNo:"",
			orderId:"",
			classtypeName:"",
			classtypeId:"",
			scheduleDate:"",
			scheduleDateId:"",
			examScheduleId:"",
			sysUserName:"",
			sysUserId:"",
			nickName:"",
			className:"",
			classId:"",
			mobile:"",
			courseName:"",
			courseId:"",
			status:-1,
			professionName:"",
			professionId:"",
			levelName:"",
			levelId:"",
			bdAreaName:"",
			bdAreaId:"",
			bkAreaName:"",
			bkAreaId:""

		},
		a:{
			orderId:"",
			orderNo:"",
			courseId:"",
			courseName:"",
			bkAreaName:"",
			bkAreaId:"",
			scheduleDate:"",
			examScheduleId:"",
			registrationNumber:"",
		},
		
		
		showList: true,
		title: null,
		flag:true,
		flagDvalidity:true,
		showOrderList:true,
		showOrderAttempt:true,
		showAddOrUpdate:false,
		statusOptions: [
			{ text: '全部', value: -1 },
			{ text: '待审核', value: 0 },
			{ text: '作废', value: 1 },
			{ text: '通过', value: 2 }
		]
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseAbnormalRegistration = {};
			vm.showAddOrUpdate = true;
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            vm.showAddOrUpdate = true;
            vm.getInfo(id)
		},
		//作废
		cancellation: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var errorMsg = '以下审核通过的单不允许进行作废：';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.statusName == '通过'){
                    errorMsg += rowData.registrationNo+";";
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
				    url: "../courseabnormalregistration/cancellation",
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
		audit: function(event){
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            
            var errorMsg = '已作废的单据不允许审核：';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.statusName == "作废"){
                    errorMsg += rowData.registrationNo+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }
            
            $.ajax({
                type: "POST",
                url: "../courseabnormalregistration/audit",
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function(r){
                    if(r.code === 0){
                        if(r.data){
                            var re = new RegExp(";","g"); //定义正则表达式
                            r.data = r.data.replace(re, "<br>");
                        }
                        alert(r.data || '操作成功！');
                        $("#jqGrid").trigger("reloadGrid");
                        /*vm.reload(null,1);*/
                    }else{
                        alert(r.msg);
                    }
                }
            
            });
        },
		opposeAudit:function(event){
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            
            var errorMsg = '已作废的单据不允许反审核：';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.statusName == "作废"){
                    errorMsg += rowData.registrationNo+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }
            $.ajax({
                type: "POST",
                url: "../courseabnormalregistration/opposeAudit",
                contentType: "application/json",
                data: JSON.stringify(ids),
                success: function(r){
                    if(r.code === 0){
                        if(r.data){
                            var re = new RegExp(";","g"); //定义正则表达式
                            r.data = r.data.replace(re, "<br>");
                        }
                        alert(r.data || '操作成功！');
                        $("#jqGrid").trigger("reloadGrid");
                        /*vm.reload(null,1);*/
                    }else{
                        alert(r.msg);
                    }
                }
            });
		
        },
		saveOrUpdate: function (event) {
			if(vm.title == "新增")
		    {
				vm.courseAbnormalRegistration.status = 0;//默认审核中
		       url = "../courseabnormalregistration/save";
		    }
		    else if(vm.title == "修改")
		    {
		    	if(vm.showAddOrUpdate){
		    		vm.courseAbnormalRegistration.status = 0;
				}
		       url = "../courseabnormalregistration/update";
		    }else
		    {
		       url = "";
		    }
			
			if(!vm.a.orderNo){
                alert('请选择订单');
                return;
            }
            if(!vm.a.courseId){
                alert('请选择课程');
                return;
            }
            if(!vm.a.bkAreaId){
                alert('请选择报考省份');
                return;
            }
            if(!vm.a.scheduleDateId){
                alert('请选择报考时间');
                return;
            }
            if(vm.a.registrationNumber){
            	alert('请选择输入登记号');
            	
            }
            $.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.a),
			    success: function(r){
			    	if(r.code === 0){
						alert(r.data ||'操作成功', function(index){
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
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "courseabnormalregistration/delete",
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
			/*alert(id);*/
			$.get("../courseabnormalregistration/info/"+id, function(r){
                vm.courseAbnormalRegistration = r.courseAbnormalRegistration;
            });
		},
		reload: function (event , p) {
			var vm = this;
			vm.showList = true;
			vm.showAddOrUpdate = false;
			vm.flag=true;
			vm.flagDvalidity=true;
			vm.showOrderList=true;
			vm.showOrderAttempt=true;
			vm.a={};
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                datatype: "json",
                postData : {
                    "classtypeId" : vm.q.classtypeId,
                    "scheduleDateId" : vm.q.scheduleDateId,
                    "sysUserId" : vm.q.sysUserId,
                    "nickName" : vm.q.nickName,
                    "className" : vm.q.className,
                    "classId" : vm.q.classId,
                    "mobile" : vm.q.mobile,
                    "courseName" : vm.q.courseName,
                    "courseId" : vm.q.courseId,
                    "status" : vm.q.status,
                    "professionName" : vm.q.professionName,
                    "professionId" : vm.q.professionId,
                    "levelName" : vm.q.levelName,
                    "levelId" : vm.q.levelId,
                    "bdAreaName" : vm.q.bdAreaName,
                    "bdAreaId" : vm.q.bdAreaId,
                    "bkAreaName" : vm.q.bkAreaName,
                    "bkAreaId" : vm.q.bkAreaId
				},
				page:page
            }).trigger("reloadGrid");
		},
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q.orderNo = "";
			vm.q.orderId = "";
			vm.q.classtypeName = "";
			vm.q.classtypeId = "";
			vm.q.scheduleDate = "";
			vm.q.scheduleDateId = "";
			vm.q.examScheduleId = "",
			vm.q.sysUserName = "";
			vm.q.sysUserId = "";
			vm.q.nickName = "";
			vm.q.className = "";
			vm.q.classId = "";
			vm.q.mobile = "";
			vm.q.courseName = "";
			vm.q.courseId = "";
			vm.q.status = -1;
			vm.q.professionName = "";
			vm.q.professionId = "";
			vm.q.levelName = "";
			vm.q.levelId = "";
			vm.q.bdAreaName = "";
			vm.q.bdAreaId = "";
			vm.q.bkAreaName = "";
			vm.q.bkAreaId = "";
		},
		areaLayerBkShowQ : function(){//报考省份浮层
			areaLay.show(function(index,opt){
				vm.q.bkAreaId = opt.areaId;
				vm.q.bkAreaName = opt.areaName;
				$('#query-bkAreaName').val(opt.areaName);
			});
		},
		areaLayerBkShowA : function(){//报考省份浮层
			areaLay.show(function(index,opt){
				vm.a.bkAreaId = opt.areaId;
				vm.a.bkAreaName = opt.areaName;
				$('#add-bkAreaName').val(opt.areaName);
			});
		},
		areaLayerBdShow : function(){//报读省份浮层
			areaLay.show(function(index,opt){
				vm.q.bdAreaId = opt.areaId;
				vm.q.bdAreaName = opt.areaName;
			});
		},
		professionLayerShow : function(){//专业浮层
			professionLay.show(function(index,opt){
				vm.q.professionId = opt.professionId;
				vm.q.professionName = opt.professionName;
			});
		},
		classTypeLayerShow : function(){//班型浮层
			classTypeLay.show(function(index,opt){
				vm.q.classtypeId = opt.classtypeId;
				vm.q.classtypeName = opt.classtypeName;
			});
		},
		classLayerShow : function(){//班级浮层
			classLay.show(function(index,opt){
				vm.q.classId = opt.classId;
				vm.q.className = opt.className;
			});
		},
		//班主任
		selectTeacher : function(){
			teacherLay.show(function(index,opt){
				vm.q.sysUserId = opt.userId;
				vm.q.sysUserName = opt.nickName;
			} , 2);
		},
		//层次-学历
		selectLevel : function(){
			levelLay.show(function(index,opt){
				vm.q.levelId = opt.levelId;
				vm.q.levelName = opt.levelName;
			});
		},
		//考试时间段档案
		selectExamScheduleQ : function(){
			examScheduleLay.show(function(index,opt){
				vm.q.scheduleDateId = opt.id;
				/*vm.q.scheduleDate = opt.scheduleDate;*/
				vm.q.examScheduleId = opt.id;
				vm.q.scheduleDate = opt.scheduleDate;
				$('#query-scheduleDate').val(opt.scheduleDate);
			});
		},
		//考试时间段档案
		selectExamScheduleA : function(){
			examScheduleLay.show(function(index,opt){
				vm.a.scheduleDateId = opt.id;
				/*vm.q.scheduleDate = opt.scheduleDate;*/
				vm.a.examScheduleId = opt.id;
				vm.a.scheduleDate = opt.scheduleDate;
				$('#add-scheduleDate').val(opt.scheduleDate);
			});
		},
		//课程弹窗
		selectCourse : function(){
			courseLay.show(function(index,opt){	
				vm.q.courseId = opt.courseId;
				vm.q.courseName = opt.courseName;				
			})
		},
		//订单信息浮层
		orderInfoLayerShow : function(){
            mallorderLay.show(function(index,opt){
            	vm.a.orderId = opt.orderId;
				vm.a.orderNo = opt.orderNo;
				$('#add-orderName').val(opt.orderNo);
				/*vm.q.classtypeName = opt.orderNo;*/
				/*$('#add-courseName').val(opt.orderNo);*/
			});
		},
		//关联课程弹窗
		courseInfoLayerShow : function () {
		    var orderId = vm.a.orderId;
		    if(orderId){
		    	freeAssessmentCoursesLay.show(function(index,opt){
                    vm.a.courseId = opt.courseId;
                    vm.a.courseName = opt.courseName;
                    $('#add-courseName').val(opt.courseName);
                },orderId);
            }else{
		        alert("请先选择订单！");
            }

        },

        //模板下载
        downExcelTemplate : function () {
            var urlstr = "/courseabnormalregistration/downExcelTemplate";
            window.location.href = urlstr;
        },
        //批量导入
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
                        url:'../courseabnormalregistration/importExcel',
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
		//自己弹窗
		selectRegistration : function(){
			registrationLay.show(function(index,opt){	
				vm.q.courseId = opt.courseId;
				vm.q.courseName = opt.courseName;				
			})
		}
	}
});