
$(function () {
	//时间空间初始化
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
	$(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
	$(".datetimepicker_end").val(new Date().format("yyyy-MM-dd 23:59:59"));
	
	$("#jqGrid").jqGrid({
        datatype: "local",
        colModel: [	
                { label: '排课名称', name: 'classplanName', width: 80 },
                { label: '授课老师', name: 'teacherName', width: 40 },
                { label: '报表类型', name: 'reportType', width: 40 },
                { label: '生成时间', name: 'reportCreateTime', width: 30 },
                { label: '开课日期', name: 'startClassTime', width: 30 },
                { label: '直播课数', name: 'totalLive', width: 30 },
                { label: '班级总人数', name: 'totalStudent', width: 30 },
                { label: '考核总人数', name: 'checkTotalStudent', width: 30 },
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			teacherId:null,//授课老师
			teacherName:null,
			classplanId:null,//排课
			classplanName:null,
			statusId:'0',
			statusName:'在读',
			reportType:0,
			startDate:null,
			endDate:null
		},
		showList: true,
		title: null,
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
			if($.isNull(vm.q.teacherId) && $.isNull(vm.q.classplanId)){
				alert("请选择授课老师或排课");
				return;
			}
			vm.reload('queryGroupByClassplan',1);
		},
		reload: function (m , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				url : '../liveAttend/' + m,
				page:page,
                datatype: "json"
            }).trigger("reloadGrid");
		},
		teacherLayerShow : function(){//授课老师
			teacherLay.show(function(index,opt){
				vm.q.teacherId = opt.userId;
				vm.q.teacherName = opt.nickName;
			} , 1);
		},
		classplanLayerShow : function(){//排课
			classplanLay.show(function(index,opt){
				vm.q.classplanId = opt.classplanId;
				vm.q.classplanName = opt.classplanName;
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
				teacherId:null,//授课老师
				teacherName:null,
				classplanId:null,//排课
				classplanName:null,
				statusId:'0',
				statusName:'在读',
				reportType:0,
				startDate:null,
				endDate:null
			};
			//重置
			$(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
			$(".datetimepicker_end").val(new Date().format("yyyy-MM-dd 23:59:59"));
		},
	},
});