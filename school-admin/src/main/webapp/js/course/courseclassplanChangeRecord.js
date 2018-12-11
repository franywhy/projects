$(function () {
	$(".datetimepicker").datetimepicker({
	 		format: 'yyyy-mm-dd hh:ii:00',
	 		zIndex : 999999999,
	 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
	 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
    });
	$(".courseClassplan-startTime").datetimepicker({
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
    $("#jqGrid").jqGrid({//主表数据初始化
        url: '../course/classplanChangeRecord/list',
        datatype: "local",
        colModel: [			
			{ label: '排课计划Id', name: 'classplanId', hidden: true,  },
			{ label: '变更记录ID', name: 'changeId', hidden: true, key: true },
			{ label: '上课时点ID', name: 'timetableId', hidden: true },
			{ label: '直播室ID', name: 'studioId', hidden: true},
			{ label: '直播间ID', name: 'liveRoomId', hidden: true },
			{ label: '授课老师ID', name: 'teacherId', hidden: true },
			{ label: '课程ID', name: 'courseId', hidden: true },
			{ label: '资料库ID', name: 'Id', hidden: true },
			
			{ label: '排课计划名称', name: 'classplanName', width: 120 }, 			
			{ label: '产品线', name: 'productName', width: 80 },
			{ label: '课程名称', name: 'courseName', width: 80 },
			{ label: '资料库名', name: 'materialName'},
			{ label: '时间说明', name: 'classplanLiveDetail', width: 80 }, 			
			{ label: '开课日期', name: 'startTime', width: 80 },
			{ label: '上课时点', name: 'timetableName', width: 80 },
			
			{ label: '直播室', name: 'studioName', width: 80 },
			{ label: '直播间', name: 'liveRoomName', width: 80 },
			{ label: '授课老师', name: 'teacherName', width: 80 },
//			{ label: '创建用户', name: 'creationName', width: 80 }, 			
//			{ label: '创建时间', name: 'creationTime', width: 80 }, 			
			
			{ label: '最近修改用户', name: 'modifiedName', width: 80 }, 			
			{ label: '最近修改日期', name: 'modifiedTime', width: 80 }, 			
			{ label: '状态', name: 'status', hidden : true},
			{ label: '状态', name: 'statusName', width: 80 ,formatter: function(v, options, row){
				var value = row.status;
				var text = '';
				if(value == 0)text= '<span class="label label-danger">作废</span>';
				else if(value == 1)text= '<span class="label label-success">正常</span>';
				else if(value == 2)text= '<span class="label label-danger">结课</span>';
				return text;
			}},
			{ label: '审核状态', name: 'isAudited', width: 80, formatter: function(value, options, row){
                var value = row.isAudited;
                var text = '';
                if(value == 0)text= '<span class="label label-danger">未通过</span>';
                else if(value == 1)text= '<span class="label label-success">通过</span>';
                else if(value == 2)text= '<span class="label label-primary">待审核</span>';
                return text;
			}},	
//			{ label: '是否公开课', name: 'isOpen', width: 80, formatter: function(value, options, row){
//				return value === 1 ? 
//					'<span class="label label-danger">否</span>' : 
//					'<span class="label label-success">是</span>';
//			}},	
			{ label: '直播课数量', name: 'liveNumber', width: 80 }, 
			{ label: '学生数量', name: 'studentNumber', width: 80 }, 
			{ label: '审核人', name: 'auditorName', width: 80 },
			{ label: '审核时间', name: 'auditTime', width: 80 },
			{ label: '审核版本', name: 'versionNo', width: 80 },
//			{ label: '备注', name: 'remark', hidden: true, width: 80 },
        ],
		viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
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
    /*--------------------------------------------初始化排课计划子表------------------------------------------*/    
    $("#detailGrid").jqGrid({
    	url: '../course/classplan/initItem',
//    	datatype: "json",
        datatype: "local",
    	colModel: [			
			{ label: '子表ID', name: 'classplanLiveId', hidden : true, width: 50, key: true },
            { label: '变更记录子表ID', name: 'changeId', hidden: true },
			{ label: '主表ID', name: 'classplanId', hidden : true, width: 80 }, 
			{ label: '课程ID', name: 'courseId', hidden : true, width: 80 }, 
			{ label: '直播课名称', name: 'classplanLiveName', width: 80 },
			{ label: '直播课ID', name: 'courseLiveDetailId', hidden : true },
//			{ label: '直播时间说明', name: 'classplanLiveTimeDetail', width: 200 }, 
			{ label: '即将开始时间', name: 'readyTime', width: 80 },	
			{ label: '开始时间', name: 'startTime', width: 80 },	
			{ label: '结束时间', name: 'endTime', width: 80 },
			{ label: '进入直播间结束时间', name: 'closeTime', width: 80 },
			{ label: '上课时段', name: 'timeBucket', hidden : true},
			{ label: '上课时段', name: 'timeBucketName', width: 80 ,formatter: function(v, options, row){
				var value = row.timeBucket;
				var text = '';
				if(value == 0)text= '上午';
				else if(value == 1)text= '下午';
				else if(value == 2)text= '晚上';
				return text;
			}},
            { label: '考勤', name: 'attendance', hidden : true},
            { label: '考勤', name: 'attendanceName', width: 80 ,formatter: function(v, options1, row){
                    var value = row.attendance;
                    var text = '';
                    if(value == -1)text= '否';
                    else text = '是';
                    return text;
                }},
			{ label: '直播室', name: 'studioName', width: 80 },//formatter: studioNameFormatter},
			{ label: '直播室ID', name: 'studioId', hidden : true },
			{ label: '直播间', name: 'liveRoomName', width: 80 },//formatter: liveRoomNameFormatter},
			/*{ label: '直播间频道号', name: 'liveRoomChannelId', width: 80 },
			{ label: '直播间频道密码', name: 'liveRoomChannelPassword', width: 80 },*/
			{ label: '直播间ID', name: 'liveroomId', hidden : true},
			{ label: '展示互动直播ID', name: 'genseeLiveId', width: 80 },
			{ label: '展示互动直播房间号', name: 'genseeLiveNum', width: 80 },
			{ label: '回放地址', name: 'backUrl', width: 80 },
			{ label: '回放ID', name: 'backId', hidden : true, width: 80 },
			{ label: '回放类型', name: 'backType', hidden : true, width: 80 },
			{ label: '授课老师', name: 'teacherName', width: 80 },//formatter: teacherNameFormatter},
			{ label: '授课老师ID', name: 'teacherId', hidden : true },
			{ label: '班型权限', name: 'liveClassTypeIds', hidden : true},
			{label : '班型权限',name : 'liveClassTypeNames',width : 80, formatter : classTypeNameFormatter},
			{ label: '文件上传', name: 'fileUrl', width: 80 },
			/*courseId : vm.courseClassplan.courseId,//课程id
        	startTime : $("#datetimepicker").val(),//开课日期
        	timetableId : vm.courseClassplan.timetableId,//上课时段id
	    	studioId : vm.courseClassplan.studioId,//直播间id
	    	liveRoomId : vm.courseClassplan.liveRoomId,//直播室id
			
	    	teacherId : vm.courseClassplan.teacherId //授课老师id*/
			//app4.0.1字段
			{label : '上期复习',name : 'review',width : 60},
            {label : '本期预习',name : 'prepare',width : 60},
            {label : '课堂资料',name : 'courseware',width : 120},
             {label : '阶段ID',name : 'examStageId',width : 120},
             {label : '阶段名称',name : 'phaseName',width : 120},
             /*{label : '变更版本',name : 'versionNo',width : 80},*/


            /*{label : '练习阶段ID',name : 'practiceStageId',width : 120,hidden : true},
            {label : '练习阶段名称',name : 'practiceStageName',width : 120,hidden : true},
            {label : '考试阶段ID',name : 'examStageId',width : 120,hidden : true},
            {label : '考试阶段名称',name : 'examStageName',width : 120,hidden : true},
            */

		],
        viewrecords: true, //定义是否要显示总记录数
        height: 285,
        rowNum: 1000,
		rowList : [10,30,50],
        rownumbers: true, //如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
        rownumWidth: 25,
        autowidth:true, //自动宽度
        multiselect: true, //定义是否可以多选
        pager: "#detailGridPager",
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
        	$("#detailGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });

    //班型权限格式化
	function classTypeNameFormatter(cellvalue, options, rowObject){
		if(null != rowObject && null != rowObject.liveClassTypeIds && rowObject.liveClassTypeIds.length > 0){
			 return vm.getLiveClassTypeNames(rowObject.liveClassTypeIds);
		}
		return "";
	};
	//直播间格式化
	function liveRoomNameFormatter(cellvalue, options, rowObject){
		return vm.getLiveRoomName(rowObject.liveroomId);
	};
	//直播室格式化
	function studioNameFormatter(cellvalue, options, rowObject){
		return vm.getStudioName(rowObject.studioId);
	};
	//授课老师格式化
	function teacherNameFormatter(cellvalue, options, rowObject){
		return vm.getTeacherName(rowObject.teacherId);
	};
	//阶段格式化
	function phaseNameFormatter(cellvalue, options, rowObject){
		return vm.getPhaseName(rowObject.phaseId);
	}

});

var vm = new Vue({
	el:'#rrapp',
	data:{// set    get
		q:{
			classplanName: "",
		},
		showList: true,
		title: null,
		classTypeList : [],
		classTypeMap : {},
		courseClassplan: {
			classplanId:"",
			classplanName:"",
			classplanLiveDetail:"",
			status:1,
			isOpen:1,
			courseId : "",
			courseName:"",//课程名称
			liveRoomId:"",
			checkType:true,
			liveRoomName:"",
			studioId:"",
			studioName:"",
			timetableId:"",
			timetableName:"",
			teacherId:"",
			teacherName:"",
			startTime:"",
			materialId:"",
			materialName:"",
			productId:"",
			productName:"",
			readyTime:30,//即将开始时间单位min
			closeTime:30//进入直播间结束时间单位min
		},
		detail: {
			title:null,
			obj : {
				liveClassTypeIds:[],
				classplanLiveName:'',
//				classplanLiveTimeDetail:'',
				timeBucket:'',
				studioId:'',
				studioName:"",
				liveroomId:'',
				liveRoomName:'',
				backUrl:'',
				teacherId:'',
				teacherName:"",
				startTime:"",
				endTime:"",
				fileUrl:"",
				readyTime:"",//即将开始时间
				closeTime:"",//进入直播间结束时间
				//liveRoomId:{}
				review:"",//上期复习
				prepare:"",//本期预习
				courseware:"",//课堂资料
				/*practiceStageId:"",//练习阶段ID
				practiceStageName:"",//练习阶段名称*/
				examStageId:"",//考试阶段ID
				phaseName:"",//阶段ID名称
                attendance:''//是否考勤
			},
		},
		selData : {
			timetableList : [{number : "" , name:"数据加载中。。。"}],
			timetableDetailList : [{id : "" , timeBucket:"数据加载中。。。"}],
			teacherList : [{userId : "" , userName:"数据加载中。。。"}],
			studioList : [{studioId : "" , studioName:"数据加载中。。。"}],
			liveRoomList : [{liveRoomId : "" , liveRoomName:"数据加载中。。。"}]
		},
		options: [
            { text: '上午', value: '0' },
            { text: '下午', value: '1' },
            { text: '晚上', value: '2' }
        ],
        options1: [
            { text: '是', value: '0' },
            { text: '否', value: '-1' }
		]
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},

		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseClassplan = {
					classplanId:"",
					classplanName:"",
					classplanLiveDetail:"",
					status:1,
					isOpen:1,
					courseId : "",
					courseName:"",//课程名称
					liveRoomId:"",
					liveRoomName:"",//直播间名字
					studioId:"",
					checkType:true,
					studioName:"",
					timetableId:"",
					timetableName:"",
					teacherId:"",
					teacherName:"",
					timetableId:"",
				    attendance:"",//是否考勤
					startTime:"",
					materialId:"",
					materialName:"",
					productId:"",
					productName:"",
					readyTime:30,//即将开始时间单位min
					closeTime:30//进入直播间结束时间单位min

		    };

			$("#auditButton").attr("style", "display: none");
            $("#unAuditButton").attr("style", "display: none");
			$("#commitButton").attr("style", "display: block");
            $("#lookButton").attr("style", "display: none");
			$("#initButton").attr("style","");
			$("#itemButton").attr("style", "display: block");
			$("#ready_time").attr("style", "");
			$("#close_time").attr("style", "");

            $("#classplanName").attr("disabled",false);
            $("#productName").attr("disabled",false);
            $("#courseName").attr("disabled",false);
            $("#classplanLiveDetail").attr("disabled",false);
            $("#meteriaName").attr("disabled",false);
            $("#courseClassplanStartTime").attr("disabled",false);
            $("#timetableName").attr("disabled",false);
            $("#studioName").attr("disabled",false);
            $("#liveRoomName").attr("disabled",false);
            $("#teacherName").attr("disabled",false);
            $("#status1").attr("disabled",false);
            $("#status2").attr("disabled",false);
            $("#status3").attr("disabled",false);
            $("#readyTime").attr("disabled",false);
            $("#closeTime").attr("disabled",false);
            $("#checkType").attr("disabled",false);
            $("#remark").attr("disabled",false);

			$("#courseClassplanStartTime").val("");//新增时让开课日期置空
			jQuery("#detailGrid").jqGrid("clearGridData");//新增的时候清空Grid子表
		},
		update: function (event) {
			var classplanId = getSelectedRow();
			if(classplanId == null){
				return ;
			}
            var id = $("#jqGrid").jqGrid('getGridParam','selrow');
            var rowData = $('#jqGrid').jqGrid('getRowData',id);
            var startTime = rowData.startTime;
            var isAudited = rowData.isAudited;
            if (new Date(startTime).getTime() < new Date().getTime()){
                alert("排课已经开课,不允许修改!")
                return;
            }
            if (isAudited.indexOf("待审核") < 0){
                alert("排课已经审核,不允许再次修改!")
                return;
            }
			vm.showList = false;
            vm.title = "修改";
            jQuery("#detailGrid").jqGrid("clearGridData");

            $("#auditButton").attr("style", "display: none");
			$("#commitButton").attr("style", "display: block");
            $("#unAuditButton").attr("style", "display: none");

			$("#itemButton").attr("style", "display: block");
            $("#lookButton").attr("style", "display: none");
			$("#initButton").attr("style", "display: none");
			$("#ready_time").attr("style", "display: none");
			$("#close_time").attr("style", "display: none");

            $("#classplanName").attr("disabled",false);
            $("#productName").attr("disabled",false);
            $("#courseName").attr("disabled",false);
            $("#classplanLiveDetail").attr("disabled",false);
            $("#meteriaName").attr("disabled",false);
            $("#courseClassplanStartTime").attr("disabled",false);
            $("#timetableName").attr("disabled",false);
            $("#studioName").attr("disabled",false);
            $("#liveRoomName").attr("disabled",false);
            $("#teacherName").attr("disabled",false);
            $("#status1").attr("disabled",false);
            $("#status2").attr("disabled",false);
            $("#status3").attr("disabled",false);
            $("#readyTime").attr("disabled",false);
            $("#closeTime").attr("disabled",false);
            $("#checkType").attr("disabled",false);
            $("#remark").attr("disabled",false);

			vm.getInfo(classplanId);
		},
        updateApply: function (event) {
            var classplanId = getSelectedRow();
            if(classplanId == null){
                return ;
            }
            var rowData = $("#jqGrid").jqGrid('getRowData',classplanId);
            var startTime = new Date(rowData.startTime);
            var date = new Date();
            if(startTime > date){
            	alert("未开课，无需填写变更申请单，请联系审核人反审核！！！");
				return;
			}
            vm.showList = false;
            vm.title = "变更申请";
            jQuery("#detailGrid").jqGrid("clearGridData");

            $("#auditButton").attr("style", "display: none");
            $("#commitButton").attr("style", "display: block");

            $("#itemButton").attr("style", "display: block");

            $("#initButton").attr("style", "display: none");
            $("#ready_time").attr("style", "display: none");
            $("#close_time").attr("style", "display: none");

            vm.getInfo(classplanId);
        },

        audit: function (event) {
            var changeId = getSelectedRow();
            if(changeId == null){
                return ;
            }

            var id = $("#jqGrid").jqGrid('getGridParam','selrow');
            var rowData = $('#jqGrid').jqGrid('getRowData',id);
            console.log(rowData);
            var classplanId = rowData.classplanId;
            var versionNo = rowData.versionNo;
            if (rowData.isAudited.indexOf('通过')>0 ){
                alert("该排课变更已审核！！！")
                return;
            }
            vm.showList = false;
            vm.title = "审核";
            jQuery("#detailGrid").jqGrid("clearGridData");

            $("#auditButton").attr("style", "display: block");
            $("#unAuditButton").attr("style", "display: none");
            $("#commitButton").attr("style", "display: none");

            $("#itemButton").attr("style", "display: none");
            $("#lookButton").attr("style", "display: none");


            $("#classplanName").attr("disabled",true);
            $("#productName").attr("disabled",true);
            $("#courseName").attr("disabled",true);
            $("#classplanLiveDetail").attr("disabled",true);
            $("#meteriaName").attr("disabled",true);
            $("#courseClassplanStartTime").attr("disabled",true);
            $("#timetableName").attr("disabled",true);
            $("#studioName").attr("disabled",true);
            $("#liveRoomName").attr("disabled",true);
            $("#teacherName").attr("disabled",true);
            $("#status1").attr("disabled",true);
            $("#status2").attr("disabled",true);
            $("#status3").attr("disabled",true);
            $("#readyTime").attr("disabled",true);
            $("#closeTime").attr("disabled",true);
            $("#checkType").attr("disabled",true);
            $("#remark").attr("disabled",true);

            vm.getInfo(classplanId,versionNo);
        },
        changeRecodLook: function (event) {
            var changeId = getSelectedRow();
            if(changeId == null){
                return ;
            }

            var id = $("#jqGrid").jqGrid('getGridParam','selrow');
            var rowData = $('#jqGrid').jqGrid('getRowData',id);
            console.log(rowData);
            var classplanId = rowData.classplanId;
            var versionNo = rowData.versionNo;
            vm.showList = false;
            vm.title = "查看";
            jQuery("#detailGrid").jqGrid("clearGridData");

            $("#auditButton").attr("style", "display: none");
            $("#unAuditButton").attr("style", "display: none");
            $("#commitButton").attr("style", "display: none");

            $("#itemButton").attr("style", "display: none");
            $("#lookButton").attr("style", "display: block");


            $("#classplanName").attr("disabled",true);
            $("#productName").attr("disabled",true);
            $("#courseName").attr("disabled",true);
            $("#classplanLiveDetail").attr("disabled",true);
            $("#meteriaName").attr("disabled",true);
            $("#courseClassplanStartTime").attr("disabled",true);
            $("#timetableName").attr("disabled",true);
            $("#studioName").attr("disabled",true);
            $("#liveRoomName").attr("disabled",true);
            $("#teacherName").attr("disabled",true);
            $("#status1").attr("disabled",true);
            $("#status2").attr("disabled",true);
            $("#status3").attr("disabled",true);
            $("#readyTime").attr("disabled",true);
            $("#closeTime").attr("disabled",true);
            $("#checkType").attr("disabled",true);
            $("#remark").attr("disabled",true);

            vm.getInfo(classplanId,versionNo);
        },
		unAudit: function (event) {
			var classplanId = getSelectedRow();
			if(classplanId == null){
				return ;
			}
            var id = $("#jqGrid").jqGrid('getGridParam','selrow');
            var rowData = $('#jqGrid').jqGrid('getRowData',id);
            var startTime = rowData.startTime;
            var isAudited = rowData.isAudited;
            if (new Date(startTime).getTime() < new Date().getTime()){
                alert("排课已经开课,不允许反审核!")
                return;
            }
            if (isAudited.indexOf("待审核") > 0 ){
                alert("排课处于待审核状态,请先审核!")
                return;
            }
			vm.showList = false;
            vm.title = "反审核";
            jQuery("#detailGrid").jqGrid("clearGridData");

            $("#auditButton").attr("style", "display: none");
			$("#commitButton").attr("style", "display: none");
            $("#unAuditButton").attr("style", "display: block");
			$("#itemButton").attr("style", "display: none");
            $("#lookButton").attr("style", "display: none");

            $("#classplanName").attr("disabled",true);
            $("#productName").attr("disabled",true);
            $("#courseName").attr("disabled",true);
            $("#classplanLiveDetail").attr("disabled",true);
            $("#meteriaName").attr("disabled",true);
            $("#courseClassplanStartTime").attr("disabled",true);
            $("#timetableName").attr("disabled",true);
            $("#studioName").attr("disabled",true);
            $("#liveRoomName").attr("disabled",true);
            $("#teacherName").attr("disabled",true);
            $("#status1").attr("disabled",true);
            $("#status2").attr("disabled",true);
            $("#status3").attr("disabled",true);
            $("#readyTime").attr("disabled",true);
            $("#closeTime").attr("disabled",true);
            $("#checkType").attr("disabled",true);
            $("#remark").attr("disabled",true);

            vm.getInfo(classplanId);
		},
		look: function (event) {
			var classplanId = getSelectedRow();
			if(classplanId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "查看详情";
            jQuery("#detailGrid").jqGrid("clearGridData");

            $("#auditButton").attr("style", "display: none");
			$("#commitButton").attr("style", "display: none");
            $("#unAuditButton").attr("style", "display: none");
			$("#itemButton").attr("style", "display: none");
			$("#lookButton").attr("style", "display: block");


            $("#classplanName").attr("disabled",true);
            $("#productName").attr("disabled",true);
            $("#courseName").attr("disabled",true);
            $("#classplanLiveDetail").attr("disabled",true);
            $("#meteriaName").attr("disabled",true);
            $("#courseClassplanStartTime").attr("disabled",true);
            $("#timetableName").attr("disabled",true);
            $("#studioName").attr("disabled",true);
            $("#liveRoomName").attr("disabled",true);
            $("#teacherName").attr("disabled",true);
            $("#status1").attr("disabled",true);
            $("#status2").attr("disabled",true);
            $("#status3").attr("disabled",true);
            $("#readyTime").attr("disabled",true);
            $("#closeTime").attr("disabled",true);
            $("#checkType").attr("disabled",true);
            $("#remark").attr("disabled",true);

            vm.getInfo(classplanId);
		},
		userList: function (event){//排课下的学员列表
			var classplanId = getSelectedRow();
			if(classplanId == null){
				return ;
			}
			//vm.showList = false;
            vm.title = "学员列表";

            vm.getUserListInfo(classplanId);
		},

		getUserListInfo: function(classplanId){//获取学员列表信息
			usersByclassplanLay.show(classplanId , null);
		},
        accept : function(event) {//审核通过
            confirm('您确定通过审核吗', function(){
                var classplanId = vm.courseClassplan.classplanId;
                var versionNo = vm.courseClassplan.versionNo;
                $.ajax({
                    type : "POST",
                    url : "../course/classplanChangeRecord/accept/"+classplanId+"/"+versionNo,
                    success : function(r) {
                        if (r.code === 0) {
                            alert('操作成功', function(index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        reject : function(event) {//审核未通过
            confirm('您确定不通过审核吗', function(){
                var classplanId = vm.courseClassplan.classplanId;
                var versionNo = vm.courseClassplan.versionNo;
                $.ajax({
                    type : "POST",
                    url : "../course/classplanChangeRecord/reject/"+classplanId+"/"+versionNo,
                    success : function(r) {
                        if (r.code === 0) {
                            alert('操作成功', function(index) {
                                vm.reload();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
		saveOrUpdate: function (event) {
			//做新增或保存时获取开课日期的值
			vm.courseClassplan.startTime = $("#courseClassplanStartTime").val();
			//前台校验
			if($.isNull(vm.courseClassplan.classplanName)){
		    	alert("排课计划名称不能为空！！！");
		    	return;
		    }
			console.log(vm.courseClassplan.productId);
			if($.isNull(vm.courseClassplan.productId)){
				alert("请选择产品线！！！");
				return;
			}
		    if($.isNull(vm.courseClassplan.courseId)){
		    	alert("请选择课程！！！");
		    	return;
		    }
		    /*if($.isNull(vm.courseClassplan.classplanLiveDetail)){
		    	alert("时间说明不能为空！！！");
		    	return;
		    }*/
		    if(!$.isNull(vm.courseClassplan.classplanLiveDetail) && vm.courseClassplan.classplanLiveDetail.length > 100){
		    	alert("时间说明不能超过100个字！！！");
		    	return;
		    }
//		    if($.isNull(vm.courseClassplan.materialId)){
//		    	alert("请选择资料库！！！");
//		    	return;
//		    }
		    if($.isNull(vm.courseClassplan.startTime)){
		    	alert("请输入开课日期！！！");
		    	return;
		    }
//		    if($.isNull(vm.courseClassplan.timetableId)){
//		    	alert("请选择上课时点！！！");
//		    	return;
//		    }
//		    if($.isNull(vm.courseClassplan.studioId)){
//		    	alert("请选择直播室！！！");
//		    	return;
//		    }
//		    if($.isNull(vm.courseClassplan.liveRoomId)){
//		    	alert("请选择直播间！！！");
//		    	return;
//		    }
		    if($.isNull(vm.courseClassplan.teacherId)){
		    	alert("请选择授课老师！！！");
		    	return;
		    }
		    if(!$.isNull(vm.courseClassplan.remark) && vm.courseClassplan.remark.length > 50){
		    	alert("备注信息不能超过50个字！！！");
		    	return;
		    }
			if(vm.title == "新增")
		    {
		       url = "../course/classplan/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../course/classplan/update";
		    }else if(vm.title == "变更申请")
            {
                url = "../course/classplan/updateApply";
            }else
		    {
		       url = "";
		    }
		    //子表所有数据
			var details = [] ;
			var ids = $("#detailGrid").jqGrid('getDataIDs');
			for(var i = 0;i<ids.length;i++){
				var row = $('#detailGrid').jqGrid('getRowData',ids[i]);
			    details.push(row);
			}
			vm.courseClassplan.detailList = details;
//			console.log(vm.courseClassplan.detailList);
            console.log(vm.courseClassplan);
            console.log(JSON.stringify(vm.courseClassplan));
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseClassplan),
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
		checkItem: function (event) {
			/*//子表所有数据
			var details = [] ;
			var ids = $("#detailGrid").jqGrid('getDataIDs');
			for(var i = 0;i<ids.length;i++){
				var row = $('#detailGrid').jqGrid('getRowData',ids[i]);
				if(isNaN(row.id)){
					row.id = null;
				}
				details.push(row);
			}
			vm.courseClassplan.detailList = details;*/

			//子表所有数据
			var details = [] ;
			var ids = $("#detailGrid").jqGrid('getDataIDs');
			for(var i = 0;i<ids.length;i++){
				var row = $('#detailGrid').jqGrid('getRowData',ids[i]);
			    details.push(row);
			}
			vm.courseClassplan.detailList = details;
			//做新增或保存时获取开课日期的值
			vm.courseClassplan.startTime = $("#courseClassplanStartTime").val();

			$.ajax({
				type: "POST",
				url: "../course/classplan/checkItem",
				data: JSON.stringify(vm.courseClassplan),
				success: function(r){
					if(r.code === 0){
						alert('校验成功');
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var classplanIds = getSelectedRows();
			if(classplanIds == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/classplan/delete",
				    data: JSON.stringify(classplanIds),
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
		getInfo: function(classplanId,versionNo){
            var productId = vm.courseClassplan.productId;
            this.getclassTypeList(productId);//班型权限初始化
			$.get("../course/classplanChangeRecord/info/"+classplanId+"/"+versionNo, function(r){
				if(r.code == 0){
					vm.courseClassplan = r.courseClassplan;
	                vm.courseClassplan.checkType=true;
	                //查询信息时获取并显示开课日期的值
	                $("#courseClassplanStartTime").val(vm.courseClassplan.startTime);
	                $("#ready_time").val(30);
	                $("#close_time").val(30);
	                var detailList = r.detailList;
	                //开课日期
	                if(null != detailList && detailList.length > 0){
	                	$.each(detailList , function(i , v){
	                		//添加行
							$("#detailGrid").addRowData(v.classplanLiveId, v, "last");
	                	});
	                }
				}

            });
		},

		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData : {"classplanName" : vm.q.classplanName},
                datatype: "json",
				page:page
            }).trigger("reloadGrid");
		},
		//正常
		resume:function(event){
		   var classplanIds = getSelectedRows();
			if(classplanIds == null){
				return ;
			}
			confirm('确定要正常选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/classplan/resume",
				    data: JSON.stringify(classplanIds),
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
		//作废
		pause:function(event){
		    var classplanIds = getSelectedRows();
			if(classplanIds == null){
				return ;
			}
			confirm('确定要作废选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/classplan/pause",
				    data: JSON.stringify(classplanIds),
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
		//结课
		end:function(event){
		    var classplanIds = getSelectedRows();
			if(classplanIds == null){
				return ;
			}
			confirm('确定要结课选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/classplan/end",
				    data: JSON.stringify(classplanIds),
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
		selCourse : function(classplanId){
			//清空查询条件
//			vm.clearQueryOrderParams();
			//初始化表格
			vm.initJqGridCourse();
			//显示浮层
			vm.showCourseLayer();
			//加载课程数据
			vm.reloadJqGridCourse();
		},
		initJqGridCourse : function(){//加载课程列表
			$("#jqGridCourse").jqGrid({
		        url: '../mall/courses/listGrid',
		        datatype: "local",
//		        datatype: "json",
		        colModel: [
					{ label: '课程ID', name: 'courseId', width: 60, key: true },
					{ label: '课程编号', name: 'courseNo', width: 100 },
					{ label: '课程名称', name: 'courseName', width: 100 },
					{ label: '课程类别', name: 'courseLb', width: 100 },
					{ label: '课程类型', name: 'courseType', width: 100 },
					{ label: '考试方式', name: 'examType', width: 100 },
					{ label: '学分', name: 'courseCredit', width: 100 },
					{ label: '排课可冲突', name: 'courseEq', width: 100, formatter: function(value, options, row){
						return value === 0 ?
							'<span class="label label-danger">不可冲突</span>' :
							'<span class="label label-success">可以冲突</span>';
					}},
					/*{ label: '试听地址', name: 'listenUrl', width: 80 },
					{ label: '创建用户', name: 'creationName', width: 80 },
					{ label: '创建时间', name: 'creationTime', width: 80 },
					{ label: '修改用户', name: 'modifiedName', width: 80 },
					{ label: '修改时间', name: 'modifiedTime', width: 80 }*/
		        ],
				viewrecords: true,
		        height: 300,
		        rowNum: 10,
				rowList : [10,30,50],
		        rownumbers: true,
		        rownumWidth: 25,
		        autowidth : true,
		        multiselect: false,
		        pager: "#jqGridPagerCourse",
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
		        ondblClickRow : function(rowid,iRow,iCol,e){
					   that.select();
				},
		        gridComplete:function(){
		        	//隐藏grid底部滚动条
		        	$("#jqGridCourse").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
		        }
		    });
		},
		showCourseLayer : function(){//显示课程列表
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '课程列表',
				area: ['891px', '633px'],
				shadeClose: false,
				content: jQuery("#courselayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					//获取选中行ID
					var selectDetail = getJqGridSelectedRow("jqGridCourse");
					if(selectDetail == null){
						return ;
					}
					//行数据
					var rowData = $("#jqGridCourse").jqGrid('getRowData',selectDetail);
//					//课程ID
					vm.courseClassplan.courseId = rowData.courseId;
//					//课程名称
					vm.courseClassplan.courseName = rowData.courseName;
					layer.close(index);
	            }
			});
		},
		reloadJqGridCourse : function(){//刷新课程列表
			$("#jqGridCourse").jqGrid('setGridParam',{
                page : 1,
                datatype : 'json'
            }).trigger("reloadGrid");
		},

		iadd: function(){
			//弹框标题
			vm.detail.title = "新增";
			//初始化弹窗数据
			vm.detail.obj = {
				liveClassTypeIds:[],
				classplanLiveName:'',
//				classplanLiveTimeDetail:'',
				timeBucket:'',
				attendance:'',
				studioId:'',
				studioName:"",
				liveroomId:'',
				liveRoomName:'',
				backUrl:'',
				teacherId:'',
				teacherName:"",
				startTime:"",
				endTime:"",
				fileUrl:"",
				readyTime:"",
				closeTime:"",
				review:"",//上期复习
				prepare:"",//本期预习
				courseware:"",//课堂资料
				/*practiceStageId:"",//练习阶段ID
				practiceStageName:"",//练习阶段名称*/
				examStageId:"",//考试阶段ID
				phaseName:""//阶段名称
			};
			$("#detailReadyTime").val("");
			$("#detailCloseTime").val("");
			$("#detailStartTime").val("");
			$("#detailEndTime").val("");
			vm.ishow();
		},
		iupdate:function(){
            var productId = vm.courseClassplan.productId;
            this.getclassTypeList(productId);//班型权限初始化
			//获取选中行ID
			//var classplanId = getSelectedRow();
			var selectDetail = getJqGridSelectedRow("detailGrid");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#detailGrid").jqGrid('getRowData',selectDetail);
			//str=>array
			rowData.liveClassTypeIds = rowData.liveClassTypeIds.split(",");
			vm.detail.obj = rowData;
			//弹框标题
			vm.detail.title = "修改";
			
			$("#detailReadyTime").val(vm.detail.obj.readyTime);
			$("#detailCloseTime").val(vm.detail.obj.closeTime);
			$("#detailStartTime").val(vm.detail.obj.startTime);
			$("#detailEndTime").val(vm.detail.obj.endTime);
//			弹框
			vm.ishow();
			
		},
		
		ishow : function(){//弹出新增或者修改窗口
            if($.isNull(vm.courseClassplan.productId)){
                alert("请选择产品线！！！");
                return;
            }
            if("新增" == vm.detail.title){
                var productId = vm.courseClassplan.productId
                this.getclassTypeList(productId);//班型权限初始化
            }
			layer.open({
				//非空校验
				//课次名称
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.detail.title,
				area: ['600px', '699px'],
				shadeClose: false,
				content: jQuery("#liveLayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
					
					vm.detail.obj.readyTime = $("#detailReadyTime").val();//即将开始时间
					vm.detail.obj.closeTime = $("#detailCloseTime").val();//进入直播间结束时间
					vm.detail.obj.startTime = $("#detailStartTime").val();//开始时间
					vm.detail.obj.endTime = $("#detailEndTime").val();//结束时间
					//非空校验
					if($.isNull(vm.detail.obj.classplanLiveName)){
						alert("直播课名称不能为空！！！");
						return;
					}
					if($.isNull(vm.detail.obj.readyTime)){
						alert("请输入即将开始时间！！！");
						return;
					}
					if($.isNull(vm.detail.obj.startTime)){
						alert("请输入开始时间！！！");
						return;
					}
					if($.isNull(vm.detail.obj.endTime)){
						alert("请输入结束时间！！！");
						return;
					}
					if(vm.detail.obj.startTime > vm.detail.obj.endTime){
						alert("结束时间不能小于开始时间！！！");
						return;
					}
					if(vm.detail.obj.readyTime > vm.detail.obj.startTime){
						alert("开始时间不能小于即将开始时间！！！");
						return;
					}
					if($.isNull(vm.detail.obj.timeBucket)){
						
						alert("请选择上课时段！！！");
						return;
					}
					if($.isNull(vm.detail.obj.attendance)){

                        alert("请选择是否考勤！！！");
                        return;
                    }
//					if($.isNull(vm.detail.obj.studioId)){
//						alert("请选择直播室！！！");
//						return;
//					}
					if($.isNull(vm.detail.obj.liveroomId)){
						alert("请选择直播间！！！");
						return;
					}
					if($.isNull(vm.detail.obj.teacherId)){
						alert("请选择授课老师！！！");
						return;
					}
					
					var liveClassTypeIds = vm.detail.obj.liveClassTypeIds;
					if(null == liveClassTypeIds || liveClassTypeIds.length == 0){
						alert("请选择班型权限");
						return;
					}

					if("新增" == vm.detail.title){
						//行ID
						var rowId = new Date().getTime();
						//添加行
						console.log(vm.detail.obj);
						$("#detailGrid").addRowData(rowId, vm.detail.obj, "last");  
					}else if("修改" == vm.detail.title){
						//修改
						$("#detailGrid").setRowData(getJqGridSelectedRow("detailGrid"),vm.detail.obj);
					}
					layer.close(index);
					
	            }
			});
		},
		idel:function(){
			//获取选中行ID
			var selectDetails = getJqGridSelectedRows("detailGrid");
			if(selectDetails == null){
				return ;
			}
			var len = selectDetails.length; 
			confirm('确定要删除选中的记录？', function(){
				/*$.each(selectDetails , function(i , val){
					$("#detailGrid").jqGrid("delRowData", val);  
				});*/
				for(var i = 0;i < len ;i ++) {
					$("#detailGrid").jqGrid("delRowData", selectDetails[0]);
				} 
				return true;
			});
		},
		//初始化授课老师列表
		initTeacher : function (){
			var that = this;
            $.getJSON("../common/teacherList", function(r){
                if(r.code === 0){
                	that.selData.teacherList = r.data;
                	//console.log(r.data);
				}else{
					alert(r.msg);
				}
            });
		},
		//初始化上课时点列表
		initTimetable : function (){
			var that = this;
            $.getJSON("../common/timetableList", function(r){
                if(r.code === 0){
                	that.selData.timetableList = r.data;
                	//console.log(r.data);
				}else{
					alert(r.msg);
				}
            });
		},
		//初始化直播室列表
		initStudio : function (){
			var that = this;
            $.getJSON("../common/studioList", function(r){
                if(r.code === 0){
                	that.selData.studioList = r.data;
                	//console.log(r.data);
				}else{
					alert(r.msg);
				}
            });
		},
		//初始化直播间列表
		initLiveRoom : function (){
			var that = this;
            $.getJSON("../common/liveRoomList", function(r){
                if(r.code === 0){
                	that.selData.liveRoomList = r.data;
				}else{
					alert(r.msg);
				}
            });
		},
		//获取班型列表
		getclassTypeList: function(productId){
			$.get("../mall/classtype/select?productId="+productId, function(data){
				vm.classTypeList = data.data;
				$.each(vm.classTypeList , function(i , v){
					vm.classTypeMap[v.value] = v.name;
				});
			});
		},
		getLiveClassTypeNames : function(ids){
			var str = "";
			if(null == ids || ids.length == 0){
				return str;
			}
			if(!$.isArray(ids)){
				ids = ids.split(",");
			}
			$.each(ids , function(i , val){
				str += vm.classTypeMap[val] + ";";
			});
			return str.length > 0 ? str.substring(0 , str.length-1) : "";
		},
		initItem : function(){//加载子表
			var vm = this;
			//加载时将开课日期赋值
			vm.courseClassplan.startTime = $("#courseClassplanStartTime").val();
			//check
			if($.isNull(vm.courseClassplan.courseId)){
				alert("请选择课程");
				return;
			};
			if($.isNull(vm.courseClassplan.startTime)){
				alert("请选择开课日期");
				return;
			};
			if($.isNull(vm.courseClassplan.timetableId)){
				alert("请选择上课时点");
				return;
			};
//			if($.isNull(vm.courseClassplan.studioId)){
//				alert("请选择直播室");
//				return;
//			};
			if($.isNull(vm.courseClassplan.liveRoomId)){
				alert("请选择直播间");
				return;
			};
			if($.isNull(vm.courseClassplan.teacherId)){
				alert("请选择授课老师");
				return;
			};
			$("#detailGrid").jqGrid('setGridParam',{ 
                postData : {
                	//加载生成列表需要的参数（传到后台）
                	courseId : vm.courseClassplan.courseId,//课程id
                	startTime :vm.courseClassplan.startTime,//开课日期
                	timetableId : vm.courseClassplan.timetableId,//上课时段id
					attendance:vm.courseClassplan.attendance,//考勤id
			    	studioId : vm.courseClassplan.studioId,//直播间id
			    	liveRoomId : vm.courseClassplan.liveRoomId,//直播室id
			    	teacherId : vm.courseClassplan.teacherId, //授课老师id
			    	readyTime : vm.courseClassplan.readyTime, //即将开始时间
			    	closeTime : vm.courseClassplan.closeTime //进入直播间结束时间
                	},
//                page : 1,
//                url : vm.userplanDetail.url,
                datatype : 'json'
            }).trigger("reloadGrid");
		},
		checkout: function(){//排课校验
			
		},
		getLiveRoomName : function(id){
			var vm = this;
			/*console.log(id);
			console.log(vm.selData.liveRoomList);*/
			var str = "";
			if(id != null && id!=""){
				for(var i = 0 ; i < vm.selData.liveRoomList.length ; i++){
					var item = vm.selData.liveRoomList[i];
					if(id == item.liveRoomId){
						str = item.liveRoomName
						break;
					}
					
				}
			}
			return str;
		},
		getStudioName : function(id){
			var vm = this;
			var str = "";
			if(id != null && id!=""){
				for(var i = 0 ; i < vm.selData.phaseList.length ; i++){
					var item = vm.selData.phaseList[i];
					if(id == item.phaseId){
						str = item.phaseName
						break;
					}
					
				}
			}
			return str;
		},
		getPhaseName : function(id){
			var vm = this;
			var str = "";
			if(id != null && id!=""){
				for(var i = 0 ; i < vm.selData.studioList.length ; i++){
					var item = vm.selData.studioList[i];
					if(id == item.studioId){
						str = item.studioName
						break;
					}
					
				}
			}
			return str;
		},
		getTeacherName : function(id){
			var vm = this;
			/*console.log(id);
			console.log(vm.selData.teacherList);*/
			var str = "";
			if(id != null && id!=""){
				for(var i = 0 ; i < vm.selData.teacherList.length ; i++){
					var item = vm.selData.teacherList[i];
					if(id == item.userId){
						str = item.username
						break;
					}
					
				}
			}
			return str;
		},
		selectCourse: function(){
			courseLay.show(function(index,opt){
				vm.courseClassplan.courseId = opt.courseId;
				vm.courseClassplan.courseName = opt.courseName;
			});
		},
		selectTeacher: function(){
			teacherLay.show(function(index,opt){
				vm.courseClassplan.teacherId = opt.userId;
				vm.courseClassplan.teacherName = opt.nickName;
			});
		},
		selectTeacherForDetail: function(){
			teacherLay.show(function(index,opt){
				vm.detail.obj.teacherId = opt.userId;
				vm.detail.obj.teacherName = opt.nickName;
			});
		},
		materialLayerShow : function() { //资料库浮层
			materialLay.show(function(index, opt) {
				vm.courseClassplan.materialId = opt.materialId;
				vm.courseClassplan.materialName = opt.materialName;
			});
		},
		selLiveroom : function(){
			liveroomLay.show(function(index,opt){
				vm.courseClassplan.liveRoomId = opt.liveRoomId;
				vm.courseClassplan.liveRoomName = opt.liveRoomName;
			});
		},
		selectStudio : function(){
			studioLay.show(function(index,opt){
				vm.courseClassplan.studioId = opt.studioId;
				vm.courseClassplan.studioName = opt.studioName;
			});
		},
		selectProduct : function(){
			productLay.show(function(index,opt){
				vm.courseClassplan.productId = opt.productId;
				vm.courseClassplan.productName = opt.productName;
			});
		},
		selectStudioForDetail : function(){
			studioLay.show(function(index,opt){
				vm.detail.obj.studioId = opt.studioId;
				vm.detail.obj.studioName = opt.studioName;
			});
		},
		selectPhaseForDetail : function(){
			phaseLay.show(getSelectedRow(),function(index,opt){
				/*alert(opt.phaseId);*/
				vm.detail.obj.examStageId = opt.phaseId;
				vm.detail.obj.phaseName = opt.phaseName;
			});
		},
		selectTimetable : function(){
			timetableLay.show(function(index,opt){
				vm.courseClassplan.timetableId = opt.number;
				vm.courseClassplan.timetableName = opt.name;
			});
		},
		selLiveroomForDetail : function(){
			liveroomLay.show(function(index,opt){
				console.log(opt);
				vm.detail.obj.liveroomId = opt.liveRoomId;
				vm.detail.obj.liveRoomName = opt.liveRoomName;
				vm.detail.obj.liveRoomChannelId = opt.liveRoomChannelId;
				vm.detail.obj.liveRoomChannelPassword = opt.liveRoomChannelPassword;
				vm.detail.obj.genseeLiveId = opt.genseeLiveId;
				vm.detail.obj.genseeLiveNum = opt.genseeLiveNum;
			});
		},
		selectVideo : function(){
			polyvVideoLay.show(function(index,opt){
				console.log(opt);
			});
		},
		updateMaterialShow : function(){//修改资料库
			var vm = this;
			var classplanId = getSelectedRow();
			if(classplanId == null){
				return ;
			}
			vm.courseClassplan = {
					classplanId:"",
					classplanName:"",
					classplanLiveDetail:"",
					status:1,
					isOpen:1,
					courseId : "",
					courseName:"",//课程名称
					liveRoomId:"",
					liveRoomName:"",//直播间名字
					studioId:"",
					studioName:"",
					timetableId:"",
					timetableName:"",
					teacherId:"",
					teacherName:"",
					timetableId:"",
					startTime:"",
					materialId:"",
					materialName:"",
				    attendance:""
		    };
			vm.getInfo(classplanId);
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '资料库关联',
				area: ['491px', '333px'],
				shadeClose: false,
				content: jQuery("#courseMaterialDiv"),
				btn: ['确定','取消'],
				btn1: function (index) {
					$.ajax({
						type: "POST",
						url : "../course/classplan/updateMaterial",
					    data: JSON.stringify(vm.courseClassplan),
					    success: function(r){
					    	if(r.code === 0){
								alert('操作成功', function(index){
									vm.reload();
								});
								layer.close(index);
							}else{
								alert(r.msg);
							}
						}
					});
//					//获取选中行ID
//					var selectDetail = getJqGridSelectedRow("jqGridCourse");
//					if(selectDetail == null){
//						return ;
//					}
//					//行数据
//					var rowData = $("#jqGridCourse").jqGrid('getRowData',selectDetail);
////					//课程ID
//					vm.courseClassplan.courseId = rowData.courseId;
////					//课程名称
//					vm.courseClassplan.courseName = rowData.courseName;
//					layer.close(index);
	            }
			});
		}
	},
	created : function(){
		this.initTeacher();
		this.initTimetable();
		this.initStudio();
		this.initLiveRoom()//改为使用弹窗
		//this.getclassTypeList(-1)//班型权限初始化
	}
});
