$(function () {
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
	$("#jqGrid").jqGrid({
        url: '../courseexamschedule/list',
        datatype: "json",
        colModel: [		
			{ label: 'courseExamId', name: 'courseExamId', key: true, width: 80, hidden : true },
			{ label: '课程pk', name: 'courseId', width: 80, hidden : true },
			{ label: '省份pk', name: 'areaId', width: 80, hidden : true },
			{ label: '考试时间段pk', name: 'examScheduleId', width: 80, hidden : true },
			
			{ label: '课程', name: 'courseName', width: 80 },
			{ label: '省份', name: 'areaName', width: 80 }, 	
			{ label: '考试时间段', name: 'examScheduleName', width: 80 }, 
			{ label: '考试日期', name: 'examDate', width: 80 }, 	
			{ label: '考试时段', name: 'examBucket', width: 80, hidden : true },	
			{ label: '考试时段', name: 'examBucketName', width: 80 ,formatter: function(v, options, row){
				var value = row.examBucket;
				var text = '';
				if(value == 0)text= '上午';
				else if(value == 1)text= '下午';
				else if(value == 2)text= '晚上';
				return text;
			}},
			/*{ label: '创建人', name: 'creationName', width: 80 }, 						
			{ label: '创建时间', name: 'createTime', width: 80 }, */						
			{ label: '修改人', name: 'modifiedName', width: 80 }, 						
			{ label: '修改时间', name: 'modifyTime', width: 80 }, 						
			{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}}, 						
			{ label: '备注', name: 'remark', width: 80 }					
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
			courseName: null
		},
		showList: true,
		title: null,
		courseExamSchedule: {
			status:1,
			courseId : "",//课程id
			areaId : "",//省份id
			courseName:"",//课程名称
			examScheduleId:"",//考试时间段id
			examScheduleName:"",//考试时间段名称
			examDate:""//考试日期
		},
		selData : {
			areaList : [{value : "" , name : "数据加载中。。。"}],
			examTimeList : [{id : "" , scheduleName : "数据加载中。。。"}]
		},
		options: [  
		      { text: '上午', value: '0' },  
		      { text: '下午', value: '1' },  
		      { text: '晚上', value: '2' }  
		    ]
	},
	methods: {
		seleteCourse: function(){
			courseLay.show(function(index,opt){
				//console.log(opt);
				//课程ID
				vm.courseExamSchedule.courseId = opt.courseId;
				//课程名称
				vm.courseExamSchedule.courseName = opt.courseName;
			});
		},
		seleteExamSchedule: function(){
			examScheduleLay.show(function(index,opt){
				console.log(opt);
				//考试时段id
				vm.courseExamSchedule.examScheduleId = opt.id;
				//考试时段名称
				vm.courseExamSchedule.examScheduleName = opt.scheduleName;
			});
		},
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseExamSchedule = {
					status:1,
					courseId : "",
					areaId : "",//省份id
					courseName:"",//课程名称
					examScheduleId:"",//考试时段id
					examScheduleName:"",//考试时段名称
					examDate:""//考试日期
			};
			$("#date").val("");//新增时让开课日期置空
		},
		update: function (event) {
			var courseExamId = getSelectedRow();
			if(courseExamId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(courseExamId)
		},
		saveOrUpdate: function (event) {
			//做新增或保存时获取开课日期的值
			vm.courseExamSchedule.examDate = $("#date").val();
			if($.isNull(vm.courseExamSchedule.courseId)){
		    	alert("请选择课程！！！");
		    	return;
		    }
			if($.isNull(vm.courseExamSchedule.areaId)){
		    	alert("请选择省份！！！");
		    	return;
		    }
			if($.isNull(vm.courseExamSchedule.examScheduleId)){
		    	alert("请选择考试时间段！！！");
		    	return;
		    }
			if($.isNull(vm.courseExamSchedule.examDate)){
		    	alert("请选择考试日期！！！");
		    	return;
		    }
			if($.isNull(vm.courseExamSchedule.examBucket)){
		    	alert("请选择考试时段！！！");
		    	return;
		    }
			if(!$.isNull(vm.courseExamSchedule.remark) && vm.courseExamSchedule.remark.length > 50){
				alert("备注信息不得超过50个字！！！");
		    	return;
			}
			if(vm.title == "新增")
		    {
		       url = "../courseexamschedule/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../courseexamschedule/update";
		    }else
		    {
		       url = "";
		    }
		    
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseExamSchedule),
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
			var courseExamIds = getSelectedRows();
			if(courseExamIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseexamschedule/delete",
				    data: JSON.stringify(courseExamIds),
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
		getInfo: function(courseExamId){
			$.get("../courseexamschedule/info/"+courseExamId, function(r){
				vm.courseExamSchedule = r.courseExamSchedule;
				//查询信息时获取并显示考试日期的值
                $("#date").val(vm.courseExamSchedule.examDate);
            });
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"courseName" : vm.q.courseName},
				page:page
            }).trigger("reloadGrid");
		},
		selCourse : function(classplanId){//选择课程
			//清空查询条件
//			vm.clearQueryOrderParams();
			//初始化表格
			vm.initJqGridCourse();
			//显示浮层
			vm.showCourseLayer();
			//加载课程数据
			vm.reloadJqGridCourse();
		},
		selExamSchedule : function(id){//选择考试时间段
			//清空查询条件
//			vm.clearQueryOrderParams();
			//初始化表格
			vm.initJqGridExamSchedule();
			//显示浮层
			vm.showExamScheduleLayer();
			//加载考试时间段数据
			vm.reloadJqGridExamSchedule();
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
		        gridComplete:function(){
		        	//隐藏grid底部滚动条
		        	$("#jqGridCourse").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		        }
		    });
		},
		initJqGridExamSchedule : function(){//加载考试时间段列表
			$("#jqGridExamSchedule").jqGrid({
		        url: '../mall/mallexamschedule/listGrid',
		        datatype: "local",
//		        datatype: "json",
		        colModel: [			
		        	{ label: 'id', name: 'id', key: true, width: 80  },
					{ label: '考试时段名称', name: 'scheduleName', width: 80 },					
					{ label: '考试年月', name: 'scheduleDate', width: 80 },					
					{ label: '创建人', name: 'creationName', width: 80 }, 
					{ label: '创建时间', name: 'createTime', width: 80 }, 
					{ label: '修改人', name: 'modifiedName', width: 80 }, 
					{ label: '修改时间', name: 'modifyTime', width: 80 }, 
					{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
						return value === 0 ? 
							'<span class="label label-danger">禁用</span>' : 
							'<span class="label label-success">正常</span>';
					}},
					{ label: '备注', name: 'comments', width: 80 }	
		        ],
				viewrecords: true,
		        height: 300,
		        rowNum: 10,
				rowList : [10,30,50],
		        rownumbers: true, 
		        rownumWidth: 25, 
		        autowidth : true,
		        multiselect: false,
		        pager: "#jqGridPagerExamSchedule",
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
		        	$("#jqGridExamSchedule").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		        }
		    });
		},
		showCourseLayer : function(){//显示课程浮层
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
					vm.courseExamSchedule.courseId = rowData.courseId;
//					//课程名称
					vm.courseExamSchedule.courseName = rowData.courseName;
					layer.close(index);
	            }
			});
		},
		showExamScheduleLayer : function(){//显示考试时间段浮层
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '考试时间段列表',
				area: ['891px', '500px'],
				shadeClose: false,
				content: jQuery("#examSchedulelayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					//获取选中行ID
					var selectDetail = getJqGridSelectedRow("jqGridExamSchedule");
					if(selectDetail == null){
						return ;
					}
					//行数据
					var rowData = $("#jqGridExamSchedule").jqGrid('getRowData',selectDetail);
//					//考试时间段ID
					vm.courseExamSchedule.examScheduleId = rowData.id;
//					//考试时间段名称
					vm.courseExamSchedule.examScheduleName = rowData.scheduleName;
					layer.close(index);
	            }
			});
		},
		reloadJqGridExamSchedule : function(){//刷新考试时间段列表
			$("#jqGridExamSchedule").jqGrid('setGridParam',{ 
                page : 1,
                datatype : 'json'
            }).trigger("reloadGrid");
		},
		reloadJqGridCourse : function(){//刷新课程列表
			$("#jqGridCourse").jqGrid('setGridParam',{ 
                page : 1,
                datatype : 'json'
            }).trigger("reloadGrid");
		},
		//初始化省份列表
		initArea : function (){
			var that = this;
            $.getJSON("../common/areaList", function(r){
                if(r.code === 0){
                	that.selData.areaList = r.data;
				}else{
					alert(r.msg);
				}
            });
		},
		//初始化考试时间段列表
		initExamTime : function (){
			var that = this;
            $.getJSON("../common/examTimeList", function(r){
                if(r.code === 0){
                	that.selData.examTimeList = r.data;
				}else{
					alert(r.msg);
				}
            });
		},
	},
	created : function(){
		this.initArea(),
		this.initExamTime()
	}
});