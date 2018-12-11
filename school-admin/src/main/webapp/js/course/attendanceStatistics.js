$(function () {
	$("#jqGrid").jqGrid({
        url: '../attendanceStatistics/attendance',
        datatype: "local",
//        datatype: "json",
        colModel: [		
        		{ label: '学员id', name: 'userId', hidden:true }, 
        		{ label: '排课计划id', name: 'classplanId', hidden:true }, 
        		{ label: '班型id', name: 'classTypeId', hidden:true }, 
        		{ label: '学员规划id', name: 'userPlanId', hidden:true }, 
        		
        		{ label: '报名时间', name: 'registrationTime', width: 80 }, 
				{ label: '省份', name: 'areaName', width: 80 },
				{ label: '学员名称', name: 'userName', width: 80 },
				{ label: '手机号', name: 'mobile', width: 80 },
				{ label: '学员层次', name: 'levelName', width: 80 },
				{ label: '上课时间', name: 'TIMESTAMP', width: 80 },
				{ label: '班级', name: 'className', width: 80 },
				{ label: '班主任', name: 'teacherName', width: 80 },
				{ label: '班型', name: 'classTypeName', width: 80 },
				
				{ label: '报读专业', name: 'professionName', width: 80 },
				{ label: '商品名称', name: 'commodityName', width: 80 },
				{ label: '排课计划', name: 'classplanName', width: 80 },	
				
				{ label: '应出勤数', name: 'mustNum', width: 80 },
				{ label: '直播出勤数', name: 'liveNum', width: 80 },
				{ label: '回放出勤数', name: 'backNum', width: 80 },
				{ label: '缺勤数', name: 'absenteeismNum', width: 80 },
				{ label: '出勤率', name: 'attendanceRate', width: 80 },
        ],
		viewrecords: true,
        height: 495,
        rowNum: 300,
		rowList : [300,500,1000],
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
		q:{
			classplanId:"",
			classplanName:"",
			areaId:"",
		    areaName:"",
		    professionId:"",
			professionName:"",
			classTypeId:"",
			classTypeName:"",
			classId:"",
			className:"",
			classTeacherId:"",
			mobile:"",
		},
		showList: true,
		title: null,
		object: {
			userPlanId:"",
			classplanId:"",
			classplanLiveId:"",
			commodityId:"",
			orderId:"",
			areaId:"",
			userId:"",
			levelId:"",
			classId:"",
			professionId:"",
			
			registrationTime:"",//报名时间
			areaName:"",//省份
			userName:"",//学员名称
			mobile:"",//学员账号
			levelName:"",//层次
			enrollmentTime:"",//报读时间
			professionName:"",//报读专业
			commodityName:"",//商品名称
			classTime:"",//上课时间
			classplanName:"",//排课计划
			mustNum:"",//应出勤课次数
			liveNum:"",//直播出勤次数
			backNum:"",//回放出勤次数
			absenteeismNum:"",//缺勤次数
			attendanceRate:""//出勤率
		},
		selectData : {//下拉初始化列表
			classTeacherList :[],//班主任
		}
	},
	methods: {
		query: function () {
			if($.isNull(vm.object.classplanId)){
				alert("请先选择一个排课计划！！！");
				return;
			}
			vm.reload(null,1);
		},
		
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				page:page,
                datatype: "json"
            }).trigger("reloadGrid");
		},
		classplanLayerShow : function(){//排课浮层
			classplanLay.show(function(index,opt){
				vm.q.classplanId = opt.classplanId;
				vm.object.classplanId = opt.classplanId;
				vm.q.classplanName = opt.classplanName;
			});
		},
		areaLayerShow : function(){//省份浮层
			areaLay.show(function(index,opt){
				vm.q.areaId = opt.areaId;
				vm.q.areaName = opt.areaName;
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
				vm.q.classTypeId = opt.classtypeId;
				vm.q.classTypeName = opt.classtypeName;
			});
		},
		classLayerShow : function(){//班级浮层
			classLay.show(function(index,opt){
				vm.q.classId = opt.classId;
				vm.q.className = opt.className;
			});
		},
		initClassTeacherList : function(){//初始化班主任列表
			var that = this;
			$.ajax({
				type: "GET",
			    url: "../common/classTeacherList",
			    success: function(r){
					if(r.code == 0){
						that.selectData.classTeacherList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		detailsList: function (event){//单个学员的考勤详情列表
			var keyId = getSelectedRow();
			if(keyId == null){
				return ;
			}else{
				var rowData = $("#jqGrid").jqGrid('getRowData',keyId);
				var classplanId = rowData.classplanId;
				var userId = rowData.userId;
				var userPlanId = rowData.userPlanId;
				var classTypeId = rowData.classTypeId;
				vm.title = "详情列表";
				
	            vm.getDetailsInfo(classTypeId , userId , userPlanId , classplanId);
			}
		},
		getDetailsInfo: function(classTypeId , userId , userPlanId , classplanId){//获取学员列表信息
			attendanceDetailsLay.show(classTypeId , userId , userPlanId , classplanId , null);
		},
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q.classplanName = "";
			vm.object.classplanId = "";
			vm.q.areaId = "";
			vm.q.areaName = "";
			vm.q.professionId = "";
			vm.q.professionName = "";
			vm.q.classTypeId = "";
			vm.q.classTypeName = "";
			vm.q.classId = "";
			vm.q.className = "";
			vm.q.classTeacherId = "";
			vm.q.mobile = "";
		},
	},
	created : function(){
		var vm = this;
		vm.initClassTeacherList();//初始化班主任列表
	}
});