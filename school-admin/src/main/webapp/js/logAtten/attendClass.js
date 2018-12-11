
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
                { label: '班级ID', name: 'classId' , width: 30 },
                { label: '班级名称', name: 'className', width: 40 },
                { label: '报表类型', name: 'reportType', width: 40 },
                { label: '生成时间', name: 'reportCreateTime', width: 40},
                { label: '地区', name: 'areaName', width: 40 },
                { label: '专业', name: 'professionName', width: 40 },
                { label: '层次', name: 'levelName', width: 40 },
                { label: '班主任', name: 'classTeacherName', width: 40 },
                { label: '班级总人数', name: 'totalStudent', width: 40 },
                { label: '考核总人数', name: 'checkTotalStudent', width: 40 },
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
			areaId:null,//地区
		    areaName:null,
			classId:null,//班级
			className:null,
			classTeacherId:null,//班主任
			classTeacherName:null,
			professionId:null,//专业
			professionName:null,
			levelId:null,//层次
			levelName:null,
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
			if($.isNull(vm.q.classTeacherId) && $.isNull(vm.q.classId)){
				alert("请选择班主任或班级");
				return;
			} else if(!$.isNull(vm.q.classTeacherId) && !$.isNull(vm.q.classId)){
				alert("班主任或班级只能选择其一");
				return;
			}
			vm.reload('queryGroupByClass',1);
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
		levelLayerShow : function(){//层次浮层
			levelLay.show(function(index,opt){
				vm.q.levelId = opt.levelId;
				vm.q.levelName = opt.levelName;
			});
		},
		professionLayerShow : function(){//专业浮层
			professionLay.show(function(index,opt){
				vm.q.professionId = opt.professionId;
				vm.q.professionName = opt.professionName;
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
				professionId:null,//专业
				professionName:null,
				levelId:null,//层次
				levelName:null,
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