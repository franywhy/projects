
$(function () {
	//时间空间初始化
	$(".datetimepicker_date").datetimepicker({
		format: 'yyyy-mm-dd',
	    weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
	    todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
	    autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
	    minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
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
	$(".datetimepicker_date").val(new Date().format("yyyy-MM-dd"));
	$(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
	$(".datetimepicker_end").val(new Date().format("yyyy-MM-dd 23:59:59"));
	
	$("#jqGrid").jqGrid({
        url: '../logStudentAtten/list',
        datatype: "local",
        colModel: [	
        		{ label: '学员规划ID', name: 'userplanId' , width: 30,hidden: true},
                { label: '学员ID', name: 'userId' , width: 30 },
                { label: '姓名', name: 'userName', width: 30 },
                { label: '学员状态', name: 'userStatus', width: 30 },
                { label: '开课时间', name: 'startClassTime', width: 40},
                { label: '手机号', name: 'mobile', width: 40 },
                { label: '地区', name: 'areaName', width: 30 },
                { label: '班级', name: 'className', width: 60 },
                { label: '班主任', name: 'teacherName', width: 40 },
                { label: '直播出勤率', name: 'livePerTxt', width: 30 },
                { label: '总出勤率', name: 'attendPerTxt', width: 30 },
                { label: '作业达标率', name: 'liveNum', width: 30 },
        ],
		viewrecords: true,
        height: 495,
        rowNum: 10000,
        rownumbers: true,
        rownumWidth: 35, 
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

	$("#jqGridLogDetails").jqGrid({
        url: '../logStudentAtten/logDetails',
        datatype: "local",
        colModel: [
                { label: '学员名称', name: 'userName', width: 80 ,formatter:function(value, options, row){
                    if($.isNotNull(row.userName)){        //如果学员名称为空就显示学员手机
                        return value;
                    }else {
                        return row.mobile;
                    }
                }},
                { label: '学员手机', name: 'moblie', width: 80 ,hidden: true },
                { label: '课程名称', name: 'courseName', width: 160 },
                { label: '讲师', name: 'teacherName', width: 80 },
                { label: '开课时间', name: 'startTime', width: 160 },
                { label: '结束时间', name: 'endTime', width: 160 },
                { label: '观看设备', name: 'devices', width: 80 ,formatter:function(value, options, row){
                    if ($.isNotNull(row.devices) && ((row.devices).indexOf("app") >= 0)){
                        return "APP";
                    }else {
                        return "PC";
                    }
                }},
                { label: '类型', name: 'type', width: 80 ,formatter: function(value, options, row) {
                    if ($.isNotNull(row.type) && row.type == 1) {
                        return "直播";
                    } else if($.isNotNull(row.type) && row.type == 2){
                        return "回放";
                    }else {
                        return value;
                    }
                }},
                { label: '进入时间', name: 'inTime', width: 160 }
        ],
		viewrecords: true,
        height: 495,
        rowNum: 10000,
        rownumbers: true,
        rownumWidth: 40,
        autowidth:false,
        multiselect: true,
        pager: "#jqGridPagerLogDetails",
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
        	$("#jqGridLogDetails").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			areaId:null,//地区
		    areaName:null,
			classId:null,//班级
			className:null,
			classTeacherId:null,//班主任
			classTeacherName:null,
			deptId:null,//部门
			deptName:null,
			statusId:'0',
			statusName:'在读',
			date:null,
			startDate:null,
			endDate:null,
			mobile:null
		},
		showList: true,
		showList2: false,
		showLogList:false,
		title: null,
		//报表详情
		qd:{
			userplanId:"",//学员规划ID
			classplanId:"",//排课
			classplanName:"",
			attenType:0 //出勤状态-1.已出勤 2.未出勤 其余的没有该查询条件-非必填
		},
	},
	methods: {
		query: function () {
			var startDate = $(".datetimepicker_start").val();
			var endDate = $(".datetimepicker_end").val();
			if($.isNull(startDate) || $.isNull(endDate)){
				alert("请选择时间范围");
				return;
			}
			vm.q.startDate = startDate;
			vm.q.endDate = endDate;
			vm.qd.startDate = startDate;
            vm.qd.endDate = endDate;
			if($.isNull(vm.q.classTeacherId) && $.isNull(vm.q.classId)){
				alert("请选择班主任或班级");
				return;
			} else if(!$.isNull(vm.q.classTeacherId) && !$.isNull(vm.q.classId)){
				alert("班主任或班级只能选择其一");
				return;
			}
			if(!$.isNull(vm.q.areaId) && !$.isNull(vm.q.deptId)){
				alert("省份或校区只能选择其一");
				return;
			}
			vm.reload('listByRequirement',1);
		},
		reload: function (m , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData : vm.q,
				url : '../logStudentAtten/' + m,
				page:page,
                datatype: "json"
            }).trigger("reloadGrid");
		},
		reloadLog: function (e , p) {
			vm.showList = false;
			vm.showList2 = false;
			vm.showLogList = true;
			var page = p || $("#jqGridLogDetails").jqGrid('getGridParam','page');
			$("#jqGridLogDetails").jqGrid('setGridParam',{
				postData : vm.qd,
				page:page,
                datatype: "json"
            }).trigger("reloadGrid");
		},
		classTeacherLayerShow : function(){//班主任
			teacherLay.show(function(index,opt){
				vm.q.classTeacherId = opt.userId;
				vm.q.classTeacherName = opt.nickName;
			} , 2);
		},
		areaLayerShow : function(){//省份浮层
			areaLay.show(function(index,opt){
				vm.q.areaId = opt.areaId;
				vm.q.areaName = opt.areaName;
			});
		},
		classLayerShow : function(){//班级浮层
			classLay.show(function(index,opt){
				vm.q.classId = opt.classId;
				vm.q.className = opt.className;
			});
		},
		studentStatusLayerShow : function(){//状态浮层
			studentStatusLay.show(function(id,name){
				vm.q.statusId = id.substring(0,id.length-1);
				vm.q.statusName = name.substring(0,name.length-1);;
			});
		},
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q = {
				areaId:null,//地区
			    areaName:null,
				classId:null,//班级
				className:null,
				classTeacherId:null,//班主任
				classTeacherName:null,
				deptId:null,
				deptName:null,
				statusId:'0',
				statusName:'在读',
				date:null,
				startDate:null,
				endDate:null,
				mobile:null,
			};
			//重置
			$(".datetimepicker_date").val(new Date().format("yyyy-MM-dd"));
			$(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
			$(".datetimepicker_end").val(new Date().format("yyyy-MM-dd 23:59:59"));
		},
		////////////////////////////报表详情
		detailsList : function(){//报表详情
			var userPlanId = getSelectedRow();
			if(userPlanId == null){
				return ;
			}
			vm.showList = false;
			vm.showList2 = true;
			vm.qd.userplanId = userPlanId;//学员规划ID
			//初始化时间组件值
			$(".datetimepicker_detail_start").val($(".datetimepicker_start").val());
			$(".datetimepicker_detail_end").val($(".datetimepicker_end").val());
			//查询
			vm.queryDetail();
		},
		logDetails : function(){  //查看学员直播日志
            var rowId = getSelectedRow();
            var rowData = $("#jqGrid").jqGrid('getRowData',rowId);
            if($.isNull(rowData.userplanId)){
            	alert("请选择有效行");
                return;
            }
            vm.showList = false;
            vm.showLogList = true;
            vm.qd.userplanId = rowData.userplanId;//学员规划ID
            vm.reloadLog();
        },
		clearQueryDetail : function(){//清空子查询条件
			vm.qd.classplanId="";//排课
			vm.qd.classplanName="";//排课名称
			vm.qd.attenType=0; //出勤状态-1.已出勤 2.未出勤 其余的没有该查询条件-非必填
			//初始化时间组件值
			$(".datetimepicker_detail_start").val($(".datetimepicker_start").val());
			$(".datetimepicker_detail_end").val($(".datetimepicker_end").val());
		},
		deptLayerShowQuery : function(){//校区查询条件
			deptLay.show(function(id , name ,opt){
				vm.q.deptId = id;
				vm.q.deptName = name;
			});
		},
		queryDetail:function(fu,p){//查询
			vm.qd.startDate = $(".datetimepicker_detail_start").val();
			vm.qd.endDate = $(".datetimepicker_detail_end").val();
			if($.isNull(vm.qd.startDate) || $.isNull(vm.qd.endDate)){
				alert("请选择时间范围！");
				return ;
			}
			var page = p || $("#jqGridDetail").jqGrid('getGridParam','page');
			$("#jqGridDetail").jqGrid('setGridParam',{ 
				postData : vm.qd,
				page:page,
                datatype: "json"
            }).trigger("reloadGrid");
		},
		backMain : function(){//返回
			vm.showList = true;
			vm.showList2 = false;
			vm.showLogList = false;
		}
	},
	created : function(){
	}
});