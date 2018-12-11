$(function () {
	$(".datetimepicker").datetimepicker({
	 	format: 'yyyy-mm-dd',
	 	zIndex : 999999999,
	 	weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        todayHighlight: 1,//如果为true, 高亮当前日期。
        startView: 2,//日期时间选择器打开之后首先显示的视图。
        forceParse: 0,//当选择器关闭的时候，是否强制解析输入框中的值。
        minView: 2,//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
        showMeridian: 1
    });
	$(".datetimepicker_start").datetimepicker({
		format: 'yyyy-mm-dd 00:00:00',
		zIndex : 999999999,
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
	});
	$(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
	$("#jqGrid").jqGrid({
        url: '../courseuserplanclass/list',
        datatype: "json",
        colModel: [		
				{ label: '主键', name: 'userplanClassId',hidden:true, key: true },
				{ label: '考试时段PK', name: 'examScheduleId', hidden:true }, 	
				{ label: '学员规划PK', name: 'userplanId', hidden:true }, 	
				
				{ label: '学习计划单号', name: 'userplanClassNo', width: 80 }, 
				{ label: '考试时段', name: 'examScheduleName', width: 80 },
				{ label: '学员姓名', name: 'userName', width: 80 },
				{ label: '手机号', name: 'mobile', width: 80 },
//				{ label: '创建人', name: 'creationName', width: 80 },
//				{ label: '创建时间', name: 'createTime', width: 80 }, 
//				{ label: '修改人', name: 'modifiedName', width: 80 }, 
				{ label: '审核状态', name: 'status', width: 80, formatter: function(value, options, row){
					return value === 0 ? 
						'<span class="label label-danger">未通过</span>' : 
						'<span class="label label-success">通过</span>';
				}},	
				{ label: '部门', name: 'deptName', width: 50 }
//				{ label: '备注', name: 'remark', width: 80 }
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
//========================================学习计划子表==========================================
    $("#detailGrid").jqGrid({
//        datatype: "json",
        datatype: "local",
        colModel: [		
				{ label: '主键', name: 'userplanClassDetailId', key: true , hidden:true },
				{ label: '主表PK', name: 'userplanClassId', hidden:true }, 	
				
				{ label: '学员规划子表PK', name: 'userplanDetailId',hidden:true },
				{ label: '课程', name: 'courseName', width: 200 }, 
				
				{ label: '排课计划PK', name: 'classplanId', hidden:true }, 
				{ label: '排课计划', name: 'classplanName', width: 200},
				
				{ label: '入课时间', name: 'timestamp', width: 200 },
				{ label: '备注', name: 'remark', width: 150 , hidden:true }
        ],
		viewrecords: true,
        height: 285,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#detailGridPager",
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
        	$("#detailGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    }); 
    
 // ------------------------------------------------------------排课计划------------------------------------
    $("#userplanDetailGrid").jqGrid({
        //url : '../course/userplan/userplanDetailList/' + vm.courseUserplanClass.userplanId,
        datatype: "local",
        colModel: [		
        		{ label: '排课计划id', name: 'classplanId', key: true, hidden : true },
        		{ label: '学员规划子表id', name: 'userplanDetailId', hidden : true },
				{ label: '课程Id', name: 'courseId', hidden : true },
				{ label: '课程', name: 'courseName', width: 300 }, 	
				{ label: '排课计划', name: 'classplanName', width: 300 }	
        ],
		viewrecords: true,
        height : 350,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#userplanDetailGridPager",
        jsonReader : {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        ondblClickRow : function(rowid,iRow,iCol,e){
			vm.select(vm.index);
		},
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#userplanDetailGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			userplanClassNo: null,
			userName:null,
			mobile:null,
			classplanName:null,
			deptId:"",
			deptName:"",
            deptIdList:"",
            deptNameList:""
		},
//		showList: true,
		showModule1:true,
		showModule2:false,
		showModule3:false,
		showModule4:false,
		title: null,
		index: null,
		courseUserplanClass: {
			userplanClassId:"",
			userplanId:"",
			orderNo:"",
			userName:"",
			examScheduleId:"",
			examScheduleName:"",
			userplanClassNo:""
		},
		detail: {
			title:null,
			obj : {
				userplanDetailId : "",
				courseName : "",
				classplanId : "",
				classplanName : "",
				timestamp : "",
				remark : ""
			},
		},
		selData : {
			userplanDetailList : [{id : "" , courseId : "", courseName : "数据加载中。。。"}],
			classplanList : [{classplanId : "" , classplanName : ""}],
		},
		addBatch:{
			classplanId:"",
			classplanName:"",
			classId:"",
			className:"",
			examScheduleId:"",
			examScheduleName:""
		},
	},
	methods: {
		showType : function(type){
			switch (type) {
			case 1:	/*-*/
				vm.showModule1 = true;
				vm.showModule2 = false;
				vm.showModule3 = false;
				vm.showModule4 = false;
				break;
			case 2:	/*-*/
				vm.showModule1 = false;
				vm.showModule2 = true;
				vm.showModule3 = false;
				vm.showModule4 = false;
				break;
			case 3:	/*-*/
				vm.showModule1 = false;
				vm.showModule2 = false;
				vm.showModule3 = true;
				vm.showModule4 = false;
				break;
			case 4:	/*-*/
				vm.showModule1 = false;
				vm.showModule2 = false;
				vm.showModule3 = false;
				vm.showModule4 = true;
				break;
			default:
				vm.showModule1 = true;
				vm.showModule2 = false;
				vm.showModule3 = false;
				vm.showModule4 = false;
				break;
}
		},
		selectUserplan: function({}){
			userplanLay.show(function(index,opt){
				console.log(opt);
				//学员计划ID
				vm.courseUserplanClass.userplanId = opt.userPlanId;
				//订单编号
				vm.courseUserplanClass.orderNo = opt.orderNo;
				//学员名称
				vm.courseUserplanClass.userName = opt.userName;
			});
		},
		selectExamSchedule: function(){
			examScheduleLay.show(function(index,opt){
				//console.log(opt);
				//考试时段id
				vm.courseUserplanClass.examScheduleId = opt.id;
				//考试时段名称
				vm.courseUserplanClass.examScheduleName = opt.scheduleName; 
				//给学员规划弹窗的考试时段id赋值
				$("#qexamScheduleId").val(opt.id);
				//给学员规划弹窗的考试时段名称赋值
				$("#qexamScheduleName").val(opt.scheduleName);
 			});
		},
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
//			vm.showList = false;
			vm.showType(2);
			vm.title = "新增";
			vm.courseUserplanClass = {
					userplanId:"",
					orderNo:"",
					userName:"",
					examScheduleId:"",
					examScheduleName:"",
					userplanClassNo:""
			};
			$("#auditButton").attr("style", "display: none");
			$("#commitButton").attr("style", "display: block");
			
			$("#itemButton").attr("style", "display: block");
			
			jQuery("#detailGrid").jqGrid("clearGridData");//新增的时候清空Grid子表
			vm.getUserplanClassNo();
		},
		update: function (event) {
			var userplanClassId = getSelectedRow();
			if(userplanClassId == null){
				return ;
			}
//			vm.showList = false;
			vm.showType(2);
            vm.title = "修改";
            jQuery("#detailGrid").jqGrid("clearGridData");//修改时先清除子表数据
            
            $("#auditButton").attr("style", "display: none");
			$("#commitButton").attr("style", "display: block");
			
			$("#itemButton").attr("style", "display: block");
            
            vm.getInfo(userplanClassId);
            
		},
		audit: function (event) {
			var userplanClassId = getSelectedRow();
			if(userplanClassId == null){
				return ;
			}
//			vm.showList = false;
			vm.showType(2);
            vm.title = "审核";
            jQuery("#detailGrid").jqGrid("clearGridData");
            
            $("#auditButton").attr("style", "display: block");
			$("#commitButton").attr("style", "display: none");
			
			$("#itemButton").attr("style", "display: none");
            
			vm.getInfo(userplanClassId);
		},
		accept : function(event) {//审核通过
			$.ajax({
				type : "POST",
				url : "../courseuserplanclass/accept",
				data : JSON.stringify(vm.courseUserplanClass.userplanClassId),
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
		},
		reject : function(event) {//审核未通过
			$.ajax({
				type : "POST",
				url : "../courseuserplanclass/reject",
				data : JSON.stringify(vm.courseUserplanClass.userplanClassId),
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
		},
		saveOrUpdate: function (event) {
		    if($.isNull(vm.courseUserplanClass.examScheduleId)){
		    	alert("请选择考试时间段！！！");
		    	return;
		    }
		    if($.isNull(vm.courseUserplanClass.userplanId)){
		    	alert("请选择订单编号！！！");
		    	return;
		    }
		    if(!$.isNull(vm.courseUserplanClass.remark) &&　vm.courseUserplanClass.remark.length > 50){
		    	alert("备注信息不能超过50个字！！！");
		    	return;
		    }
			if(vm.title == "新增")
		    {
		       url = "../courseuserplanclass/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../courseuserplanclass/update";
		    }else
		    {
		       url = "";
		    }
		    //子表所有数据
			var details = [] ;
			var ids = $("#detailGrid").jqGrid('getDataIDs');
			for(var i = 0;i<ids.length;i++){
				var row = $('#detailGrid').jqGrid('getRowData',ids[i]);
				if(isNaN(row.id)){
    				row.id = null;
    			}
			    details.push(row);
			}
			vm.courseUserplanClass.detailList = details;
		    
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseUserplanClass),
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
			var userplanClassIds = getSelectedRows();
			if(userplanClassIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseuserplanclass/delete",
				    data: JSON.stringify(userplanClassIds),
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
		getInfo: function(userplanClassId){
			$.get("../courseuserplanclass/info/"+userplanClassId, function(r){
				vm.courseUserplanClass = r.courseUserplanClass;
				
				var detailList = r.detailList;
                if(null != detailList && detailList.length > 0){
                	$.each(detailList , function(i , v){
                		//添加行
						$("#detailGrid").addRowData(v.userplanClassDetailId, v, "last");
                	});
                }

            });
		},
		getUserplanClassNo: function(){
			$.get("../courseuserplanclass/getCode/", function(r){
				vm.courseUserplanClass.userplanClassNo = r.data;
			});
		},
		reload: function (event , p) {
//			vm.showList = true;
			vm.showType(1);
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData : vm.q,
				page:page
            }).trigger("reloadGrid");
		},
		
		//新方案--by shanyaofeng
		userplanDetailShow: function (event) {
			
			if($.isNull(vm.courseUserplanClass.userplanId)){
				//ajax加载下拉数据
				alert("请选择订单编号！");
				return;
			}
			//弹框标题
			vm.detail.title = "新增";
			//初始化弹窗数据
			vm.detail.obj = {
					userplanDetailId:"",
					courseName:"",
					classplanId : "",
					classplanName:"",
					timestamp : "",
					remark : ""
			};
			
			vm.selUserplanDetail();
		},
//		userplanDetailOneShow: function (event) {
//			jQuery("#userplanDetailGrid").jqGrid('setGridParam',{multiselect: false});  
//			vm.userplanDetailShow();
//		},
		userplanDetailMultShow: function (event) {
//			jQuery("#userplanDetailGrid").jqGrid('setGridParam',{multiselect: true});  
			vm.userplanDetailShow();
		},
		selUserplanDetail : function(){//学员规划详情弹窗
			//显示浮层
			vm.showUserplanDetailLay();
			//加载数据
			vm.reloadUserplanDetailJqGrid();
		},
		showUserplanDetailLay : function(){
			vm.index = layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '新增',
				area: ['670px', '600px'],
				shadeClose: false,
				content: jQuery("#userplanDetaillayer"),
				btn: ['确定','取消'],
				btn1: function () {
					// vm.select(vm.index);
					vm.selectRows(vm.index);
	            }
			});
		},
		reloadUserplanDetailJqGrid : function(){
			var ids = $("#detailGrid").jqGrid('getDataIDs');
			var classplanIds = "";
			for(var i = 0;i<ids.length;i++){
				var row = $('#detailGrid').jqGrid('getRowData',ids[i]);
				classplanIds+=row.classplanId + ",";
			}
			classplanIds = classplanIds.length > 0 ? classplanIds.substring(0,classplanIds.length-1) : classplanIds;
			
			$("#userplanDetailGrid").jqGrid('setGridParam',{
				url : '../course/userplan/userplanDetailList',
				postData : {
					userPlanId : vm.courseUserplanClass.userplanId,
					classplanIds : classplanIds,
					classplanName : vm.q.classplanName,
					},
				page : 1,
                datatype : 'json'
            }).trigger("reloadGrid")
		},
		queryUserplanDetailByName:function(){
			vm.reloadUserplanDetailJqGrid();
		},
		select:function(index){//单选
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("userplanDetailGrid");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#userplanDetailGrid").jqGrid('getRowData',selectDetail);
			vm.detail.obj.userplanDetailId = rowData.userplanDetailId;
			vm.detail.obj.courseName = rowData.courseName;
			vm.detail.obj.classplanId = rowData.classplanId;
			vm.detail.obj.classplanName = rowData.classplanName;
			vm.detail.obj.timestamp = new Date().format("yyyy-MM-dd hh:mm:ss");
			//行ID
			var rowId = new Date().getTime();
			//添加行
			$("#detailGrid").addRowData(rowId, vm.detail.obj, "last"); 
			
			layer.close(index);
		},
		selectRows : function(index){//多选
			//获取选中行ID
			var selectArrays = getJqGridSelectedRows("userplanDetailGrid");
			if($.isNull(selectArrays)){
				return ;
			}
			for(var i = 0 ; i< selectArrays.length ; i++){
				var selectId = selectArrays[i];
				//行数据
				var rowData = $("#userplanDetailGrid").jqGrid('getRowData',selectId);
				vm.detail.obj.userplanDetailId = rowData.userplanDetailId;
				vm.detail.obj.courseName = rowData.courseName;
				vm.detail.obj.classplanId = rowData.classplanId;
				vm.detail.obj.classplanName = rowData.classplanName;
                    vm.detail.obj.timestamp = new Date().format("yyyy-MM-dd hh:mm:ss");
				//行ID
				var rowId = new Date().getTime();
				//添加行
				$("#detailGrid").addRowData(rowId, vm.detail.obj, "last");
			}
			layer.close(index);
		},
		updateDetail:function(){
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("detailGrid");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#detailGrid").jqGrid('getRowData',selectDetail);
			vm.detail.obj = rowData;
			vm.dateShow();
		},
		dateShow: function (){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '修改',
				area: ['600px', '180px'],
				shadeClose: false,
				content: jQuery("#dateLayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
					vm.detail.obj.timestamp = $("#detailStartTime").val();
					$("#detailGrid").setRowData(getJqGridSelectedRow("detailGrid"),vm.detail.obj);
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
		courseSelectChange : function(){
			vm.initClassplanList();
		},
		getClassplanName : function(id){
			var vm = this;
			console.log(id);
			console.log(vm.selData.studioList);
			var str = "";
			if(id != null && id!=""){
				for(var i = 0 ; i < vm.selData.classplanList.length ; i++){
					var item = vm.selData.classplanList[i];
					if(id == item.classplanId){
						str = item.classplanName
						break;
					}
					
				}
			}
			return str;
		},
		getCourseName : function(id){
			var vm = this;
			console.log(id);
			console.log(vm.selData.userplanDetailList);
			var str = "";
			if(id != null && id!=""){
				for(var i = 0 ; i < vm.selData.userplanDetailList.length ; i++){
					var item = vm.selData.userplanDetailList[i];
					if(id == item.id){
						str = item.courseName
						break;
					}
					
				}
			}
			return str;
		},
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q.userplanClassNo = "";
			vm.q.userName = "";
			vm.q.mobile = "";
            vm.q.deptIdList = "" ;
            vm.q.deptNameList = "";
		},
		deptLayerShowQuery : function(){//部门查询条件
			/*deptLay.show(function(id , name ,opt){
				vm.q.deptId = id;
				vm.q.deptName = name;
			});*/
            deptQueryLay.show(function(opt,deptIdList,deptNameList){
                vm.q.deptIdList = deptIdList.join(",");
                vm.q.deptNameList = deptNameList;
            });
		},
		
		classplanLayerShow : function(){//排课浮层
			classplanLay.show(function(index,opt){
				vm.addBatch.classplanId = opt.classplanId;
				vm.addBatch.classplanName = opt.classplanName;
			});
		},
		classLayerShow : function(){//班级浮层
			/*classLay.show(function(index,opt){
                vm.addBatch.classId = opt.classId;
                vm.addBatch.className = opt.className;
            });*/
            classLay.showRows(function(classIdList,classNameList){
                vm.addBatch.classId = classIdList.join(",");
                vm.addBatch.className = classNameList;
            });
		},
		examScheduleLayerShow: function(){//考试时段浮层
			examScheduleLay.show(function(index,opt){
				//考试时段id
				vm.addBatch.examScheduleId = opt.id;
				//考试时段名称
				vm.addBatch.examScheduleName = opt.scheduleName; 
 			});
		},
		addBatchShow : function(){//批量新增窗口-会计
			vm.showType(3);
			vm.addBatch = {classplanId:"",classplanName:"",classId:"",className:""};
		},
		addBatchShowZK : function(){//批量新增窗口-自考
			vm.showType(4);
			vm.addBatch = {
					classplanId:"",
					classplanName:"",
					classId:"",
					className:"",
					examScheduleId:"",
					examScheduleName:""
			};
		},
		addBatchCom : function(){//批量新增方法-会计
			if($.isNull(vm.addBatch.classplanId)){
				alert("请选择排课!");
				return;
				vm.classplanLayerShow();//排课浮层
			}
            if(!$.isNull(vm.addBatch.classId) && vm.addBatch.classId.split(",").length > 10){
                alert("最多只能选择10个班级!");
                return;
                vm.classLayerShow();//班级浮层
            }
			confirm('确定要批量排课吗？', function(){
				$.ajax({
					beforeSend : function(r){
						hq.load();
					},
					type : "POST",
					url : "../courseuserplanclass/addBatchByClassplanId?classplanId="+vm.addBatch.classplanId+"&classId="+vm.addBatch.classId,
//					data : JSON.stringify(vm.courseUserplanClass.userplanClassId),
					success : function(r) {
						if (r.code === 0) {
							alert(r.data);
						} else {
							alert(r.msg);
						}
					},
					complete : function(r){
						hq.hide();
					}
				});
			});
		},
		addBatchComZK : function(){//批量新增方法-自考
			if($.isNull(vm.addBatch.classplanId)){
				alert("请选择排课!");
				return;
				vm.classplanLayerShow();//排课浮层
			}
			if($.isNull(vm.addBatch.classId)){
				alert("请选择班级!");
				return;
				vm.classLayerShow();//班级浮层
			}
            if(vm.addBatch.classId.split(",").length > 10){
                alert("最多只能选择10个班级!");
                return;
                vm.classLayerShow();//班级浮层
            }
			if($.isNull(vm.addBatch.examScheduleId)){
				alert("请选择考试时段!");
				return;
				vm.examScheduleLayerShow();//考试时段浮层
			}
			confirm('确定要批量排课吗？', function(){
				$.ajax({
					beforeSend : function(r){
						hq.load();
					},
					type : "POST",
					url : "../courseuserplanclass/addBatchByClassplanIdAndClassId/"+vm.addBatch.classplanId+"/"+vm.addBatch.classId+"/"+vm.addBatch.examScheduleId,
//					data : JSON.stringify(vm.addBatch),
					success : function(r) {
						if (r.code === 0) {
							alert(r.data);
						} else {
							alert(r.msg);
						}
					},
					complete : function(r){
						hq.hide();
					}
				});
				
			});
		},
	}
});