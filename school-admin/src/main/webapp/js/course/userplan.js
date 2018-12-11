var userPlanId;
$(function () {
	//初始化组件
	$("#userplanServiceTime").datetimepicker({
		format: 'yyyy-mm-dd hh:ii:00',
		zIndex : 999999999,
		todayBtn:  true,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
		autoclose: true//当选择一个日期之后是否立即关闭此日期时间选择器。
	});
	$("#nextTime").datetimepicker({
		format: 'yyyy-mm-dd',
		startDate:new Date() ,
		minView: "month",//设置只显示到月份
		clearBtn:true,  
		zIndex : 999999999,
		todayBtn:'linked',//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
		autoclose: true//当选择一个日期之后是否立即关闭此日期时间选择器。
	});
	//初始化当前日期
	$("#userplanServiceTime").val(new Date().format("yyyy-MM-dd hh:mm:00"));
	$("#nextTime").val("");
	//初始化表格
        $("#jqGrid").jqGrid({
        url: '../course/userplan/list',
        datatype: "local",
        colModel: [
                   { label: '主键', name: 'userPlanId',width: 30 , key: true , hidden: false},
                   { label: '班型PK', name: 'classTypeId', width: 80 , hidden: true },
                   { label: '订单ID', name: 'orderId', width: 80 , hidden : true}, 
                   { label: '学员PK', name: 'userId', width: 30 , hidden : true}, 
                   { label: '最近考试时段PK', name: 'examScheduleId', width: 80, hidden : true },
                   
                   { label: '学员', name: 'userName', width: 50 }, 
                   { label: '学员手机', name: 'userMobile', width: 70 }, 
                   { label: '商品', name: 'commodityName', width: 100 }, 
                   { label: '专业', name: 'professionName', width: 40 },
                   { label: '省份', name: 'areaName', width: 40 },
                   { label: '班级', name: 'className', width: 50 }, 
                   { label: '班型', name: 'classTypeName', width: 50 }, 			
                   { label: '层次', name: 'levelName', width: 30 }, 
                   { label: '班主任', name: 'teacherName', width: 50 }, 
                   
                   { label: '可代替课程', name: 'isRep', width: 50 , formatter: function(value, options, row){
          				return value === 0 ? '<span class="label label-success">学习</span>' : '<span class="label label-danger">不学习</span>';
          		   }}, 			
                   { label: '专业对口', name: 'isMatch', width: 50 , formatter: function(value, options, row){
          				return value === 0 ? '<span class="label label-success">对口</span>' : '<span class="label label-danger">不对口</span>';
          		   }}, 			
                   { label: '订单编号', name: 'orderNo', width: 80 }, 			
                   { label: '学员规划状态', name: 'userplanStatus', width: 80 , formatter: function(value, options, row){
                	   var str = '';
                	   if(value === 0){
                		   str = '<span class="label label-success">正常</span>';
                	   }else if(value === 1){
                		   str = '<span class="label label-danger">毕业</span>';
                	   }else if(value === 2){
                		   str = '<span class="label label-danger">休学</span>';
                	   }
                	   return str;
          		   }},
          		   { label: '部门', name: 'deptName', width: 50 }, 
//                   { label: '毕业时间', name: 'graduateTime', width: 80 }, 			
//                   { label: '最近考试时段', name: 'examScheduleName', width: 80 }		
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
    // ===================================================初始化学员规划课程子表=========================================
    $("#detailGrid").jqGrid({
        url: '../course/userplan/list',
        datatype: "local",
        colModel: [			
                   { label: 'id', name: 'id', key: true ,hidden: true},
                   { label: 'areaId', name: 'areaId',hidden: true},
                   { label: 'classId', name: 'classId',hidden: true},
                   { label: 'userId', name: 'userId',hidden: true},
                   { label: 'sourceType', name: 'sourceType',hidden: true},
                   { label: '省份', name: 'areaName', width: 80 }, 			
                   { label: '课程', name: 'courseName', width: 200 }, 			
                   { label: '课程编号', name: 'courseNo', width: 200 },			
                   { label: '是否通过', name: 'isPass', width: 100 , formatter: function(value, options, row){
       				return value === 1 ? '<span class="label label-success">通过</span>' : '<span class="label label-danger">未通过</span>';
       				}}, 			
                   { label: '可代替课程', name: 'isSubstituted', width: 100 , formatter: booleanFormat}, 			
                   { label: '代替课程', name: 'isSubstitute', width: 100 , formatter: booleanFormat}, 			
                   { label: '专业不对口课程', name: 'isSuitable', width: 100 , formatter: booleanFormat}, 			
                   { label: '全国统考', name: 'isUnitedExam', width: 100 , formatter: booleanFormat },
                   { label: '最新排课ID', name: 'userplanClassDetailId', width: 200 ,hidden:true},
                   { label: '学习计划明细ID', name: 'userplanClassId', width: 200 ,hidden:true},
                  /* { label: '最新排课名称', name: 'classPlanName', width: 80,formatter: function(value, options, row){
                	    classPlanName=value
          				return classPlanName;
      				}},*/
      			   { label: '最新排课名称', name: 'classPlanName', width: 200},
                   { label: '最新入课时间', name: 'timeStamps', width: 200 }, 			
                   { label: '最新学习计划单号', name: 'userplanClassNo', width: 200 }
                   ],
		viewrecords: true,
        height : 500,
        rowNum: 100,
		rowList : [100,300,500],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
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
    
    function booleanFormat(value, options, row){
    	return value === 1 ? '<span class="label label-success">是</span>' : '<span class="label label-danger">否</span>';
    }
    //======================================================================初始化订单列表=======================================================
    $("#jqGridOrder").jqGrid({
        url: '../mall/order/listGrid',
        datatype: "local",
//        datatype: "json",
        colModel: [			
			{ label: 'ID', name: 'orderId', key: true ,hidden: true},
			{ label: 'commodityId', name: 'commodityId',hidden: true},
			{ label: 'areaId', name: 'areaId',hidden: true},
			{ label: 'classId', name: 'classId',hidden: true},
			 
			{ label: '订单号', name: 'orderNo' , width: 150 },
			{ label: '手机', name: 'userMobile' , width: 100 },
			{ label: '学员名称', name: 'userName' , width: 80},
			{ label: '地区', name: 'areaName' , width: 80},
			{ label: '班级', name: 'className' , width: 100 },
			{ label: '班型', name: 'classTypeName' , width: 60 },
			{ label: '层次', name: 'levelName' , width: 60 },
			{ label: '来源', name: 'sourceType' , width: 50 , formatter: function(value, options, row){
				var str = '';
				if(value === 0){
					str = '<span class="label label-danger">线上</span>';
				}else if(value === 1){
					str = '<span class="label label-success">NC</span>';
				}else if(value === 2){
					str = '<span class="label label-success">后台</span>';
				}else{
					str = '<span class="label label-danger">其他</span>';
				}
				return str;
			}},
			{ label: '支付状态', name: 'payStatus', width: 80 , formatter(value, options, row){if(0==value)return "未支付";else if(1==value)return "发起支付";else if(2==value){return "支付成功";}} }, 
			{ label: '学员规划', name: 'userplanCount', width: 80 , formatter(value, options, row){return value === 0 ? '<span class="label label-danger">未生成</span>'
					: '<span class="label label-success">已生成</span>';} },
			{ label: '备注', name: 'remark' , width: 300 }
        ],
		viewrecords: true,
        height: 400,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth : true,
        multiselect: false,
        pager: "#jqGridPagerOrder",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
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
        	$("#jqGridOrder").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
	//======================================================================初始化班级列表=======================================================
	$("#jqGridClass").jqGrid({
		url : '../mall/class/list',
		datatype : "local",
		colModel : [
			{ label : '班级Id', name : 'classId', width : 50, key : true, hidden: true },
			{ label : '省份', name : 'areaName', width : 80 },
			{ label : '专业', name : 'professionName', width : 80 },
			{ label : '学历', name : 'levelName', width : 80 },
			{ label : '班级名称' , name : 'className', width : 200 },
			{ label : '班主任', name : 'classTeacherName', width : 80}
		],
		viewrecords : true,
		height : 300,
		rowNum : 10,
		rowList : [ 10, 30, 50 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		multiselect : false,
		pager : "#jqGridPagerClass",
		jsonReader : {
			root : "page.list",
			page : "page.currPage",
			total : "page.totalPage",
			records : "page.totalCount"
		},
		prmNames : {
			page : "page",
			rows : "limit",
			order : "order"
		},
		gridComplete : function() {
			//隐藏grid底部滚动条
			$("#jqGridClass").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	});

	//======================================================================初始化服务记录列表=======================================================
	
	
	$("#jqGridService").jqGrid({
        url: '../courseuserplanservice/list',
        datatype: "local",
        colModel: [			
                   { label: '主键', name: 'userplanServiceId' , key: true , hidden: true},
                   { label: '学员规划PK', name: 'userplanId' , hidden: true },
                   
                   { label: '服务时间', name: 'userplanServiceTime', width: 160 }, 
                   { label: '服务名称', name: 'serviceTypeName', width: 100 }, 
                   { label: '服务ID', name: 'serviceTypeId', width: 100 , hidden: true}, 
                   { label: '服务内容', name: 'serviceTypeContent', width: 120 },
                   { label: '服务反馈名称', name: 'serviceFbName', width: 100 },
                   { label: '服务反馈ID', name: 'serviceFbId', width: 100 , hidden: true},
                   { label: '服务反馈内容', name: 'serviceFbContent', width: 160 }, 
                   { label: '下次跟进', name: 'nextTime', width: 160 }, 
                   
                   { label: '创建人', name: 'createPersonName', width: 80 }, 			
                   { label: '创建时间', name: 'createTime', width: 160 },
                   { label: '修改人', name: 'modifyPersonName', width: 80 },
                   { label: '修改时间', name: 'modifyTime', width: 160 }
//                   
//                   { label: '类型', name: 'type', width: 80 , formatter: function(value, options, row){
//          				return value === 0 ? '<span class="label label-success">手工</span>' : '<span class="label label-danger">系统</span>';
//          		   }}		
                   ],
		viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPagerService",
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
        	$("#jqGridService").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });

});

var vm = new Vue({
	el:'#rrapp',
	data:{
		show1: true,
		show2: false,
		show3: false,
		show4: false,
		showOrderBtn : false, //订单选择按钮
		showClassBtn : false, //班级选择按钮
		title: null,//修改操作标题
		index: null,
		courseUserplan: {},//修改/行数据
		courseUserplanOldChange: {},//修改/转省转专业
		courseUserplanNewChange: {
			commodityId : "",
			commodityName:"",
			areaId:"",
			areaName:"",
			classTypeId : "",
			classTypeName:"",
			levelId : "",
			levelName:"",
			professionId : "",
			professionName:""
		},//修改/转省转专业
		detailItem : {
			userplanId : "",
			areaId : "",
			areaName : "",
			courseId : "",
			courseName : "",
			isPass:0,
			isSubstituted : 0,
			isSubstitute : 0,
			isSuitable : 0,
			courseType : 0,
			title:"新增",
			classPlanName:""
		},
		qUserplan : {//查询条件
			orderNo : "",
			mobile : "",
			nickName : "",
			userMobile : "",
			areaId : "",
			levelId : "",
			professionId : "",
			isRep:"",
			isMatch:"",
			classTypeId : "",
			userplanStatus:0,
			classTeacherId:"",
			deptId:"",
			deptName:"",
			deptIdList:"",
			deptNameList:""
		},
		qorder:{//选择订单查询条件
			orderNo : "",
			sourceType : "",
			userMobile : ""
		},
		qclass : {//班级查询条件
			levelId : "",
			areaId : "",
			professionId : "",
			classTeacherId : "",
			className : ""
		},
		userplanDetail : {
			url : ""//学员规划课程子表加载路径
		},
		selectData : {//下拉初始化列表
			classTeacherList :[],//班主任
			areaList :[],//地区
			professionList :[],//专业
			levelList :[],//层次
			classTypeList :[],//班型
			classTeacherList :[],//班主任
			//可代替课程数据
			repList : [{value:0,name:'学习'} , {value:1,name:'不学习'}],
			//专业对口
			matchList : [{value:0,name:'对口'} , {value:1,name:'不对口'}],
			//学员规划状态
			userplanStatus : [{value:0,name:'正常'} , {value:1,name:'毕业'} , {value:1,name:'休学'}],
			commodityList :[],//商品列表
			commodityAreaList :[],//
			serviceRecordList : [],//服务类型
			FeedbackRecordList : [],//反馈类型文档
		},
		change : {//转省转专业初始化参数
			orderChangeList : [],//转省转专业对应的订单
			areaChangeList : []
		},
		serviceLayerTitle:"",
		serviceUrl:"",
		serviceData:{
			userplanServiceId:"",
			userplanId:"",
			userplanServiceTime:"",
			nextTime:"",
			serviceTypeId:"",
			serviceTypeContent:"",
			serviceFbId:"",
			serviceFbContent:"",
			remark:""
		},
		//商品
		mallOrderBatch : {
			commodityId : "",//商品ID
			commodityName : "",//商品名称
		},
		//班级
		mallClassBatch : {
			classId : null,//班级ID
			className : "",//班级名称
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
//			vm.showType(1);
		},
		showType:function(type){
			switch (type) {
			case 1:	/*-*/
				vm.show1 = true;
				vm.show2 = false;
				vm.show3 = false;
				vm.show4 = false;
				break;
			case 2:	/*-*/
				vm.show1 = false;
				vm.show2 = true;
				vm.show3 = false;
				vm.show4 = false;
				break;
			case 3:	/*-*/
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = true;
				vm.show4 = false;
				break;
			case 4:	/*-*/
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = false;
				vm.show4 = true;
				break;

			default:
				vm.show1 = true;
			vm.show2 = false;
			vm.show3 = false;
			vm.show4 = false;
				break;
			}
		},
		add: function(){
			vm.showType(2);
			vm.showOrderBtn = true;
			vm.showClassBtn = true;
			vm.title = "新增";
			vm.courseUserplan = {
				isRep : 0 , //是否有代替课程
				isMatch : 0,//专业对口
				areaName :"",//省份名称
				className :"",//班级名称
				classId :"",//班级名称
				classTypeName :"",//班型名称
				levelName :"",//层次名称
				orderId :"",//订单ID
				orderNo :"",//订单号
				userMobile :"",//学员手机号码
				userName :"",//学员名称
				classTypeId :"",//班型
				classTypeName :""//班型
			};
			 $("#userplanDetails").hide();//隐藏添加和删除子表的按钮
			jQuery("#detailGrid").jqGrid("clearGridData");//新增的时候清空Grid子表
			//学员规划课程子表加载路径
			vm.userplanDetail.url = "../course/userplan/courseListByCommodityId";
		},
		update: function (event) {
			vm.showOrderBtn = false;
			vm.showClassBtn = false;
			userPlanId = getSelectedRow();
			if(userPlanId == null){
				return ;
			}
			jQuery("#detailGrid").jqGrid("clearGridData");//新增的时候清空Grid子表
			vm.showType(2);
            vm.title = "修改";
            vm.getInfo(userPlanId);
            $("#userplanDetails").show();
            //学员规划课程子表加载路径
            vm.userplanDetail.url = "../course/userplan/courseListByUserPlanId";
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../course/userplan/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../course/userplan/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseUserplan),
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
			var userPlanIds = getSelectedRows();
			if(userPlanIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/userplan/delete",
				    data: JSON.stringify(userPlanIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.reload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(userPlanId){
			$.get("../course/userplan/info/"+userPlanId, function(r){
                vm.courseUserplan = r.courseUserplan;
                vm.reloadUserPlanDetail();
            });
		},
		reload: function (event , p) {
			vm.showType(1);
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.qUserplan,
				datatype: "json",
                page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var userPlanIds = getSelectedRows();
			if(userPlanIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseuserplan/resume",
				    data: JSON.stringify(userPlanIds),
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
		pause:function(event){
		    var userPlanIds = getSelectedRows();
			if(userPlanIds == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../userplan/pause",
				    data: JSON.stringify(userPlanIds),
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
		selOrder : function(userPlanId){
			//清空查询条件
			vm.clearQueryOrderParams();
			//显示浮层
			vm.showOrderLayer();
			//加载订单数据
			vm.reloadJqGridOrder();
		},
		showOrderLayer : function(){//显示订单列表浮层
			vm.index = layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '订单列表',
				area: ['1211px', '633px'],
				shadeClose: false,
				content: jQuery("#orderlayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					vm.select(vm.index);
	            }
			});
		},
		select:function(index){//单选
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("jqGridOrder");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGridOrder").jqGrid('getRowData',selectDetail);
			//订单基础信息
//			vm.courseUserplan = rowData;
			//省份名称
			vm.courseUserplan.areaName = rowData.areaName;
			//班级名称
			vm.courseUserplan.className = rowData.className;
			//班级名称
			vm.courseUserplan.classId = rowData.classId;
			//班型名称
			vm.courseUserplan.classTypeName = rowData.classTypeName;
			//层次名称
			vm.courseUserplan.levelName = rowData.levelName;
			
			//订单ID
			vm.courseUserplan.orderId = rowData.orderId;
			//订单号
			vm.courseUserplan.orderNo = rowData.orderNo;
			//学员手机号码
			vm.courseUserplan.userMobile = rowData.userMobile;
			//学员名称
			vm.courseUserplan.userName = rowData.userName;
			//商品ID
			vm.courseUserplan.commodityId = rowData.commodityId;
			//省份ID
			vm.courseUserplan.areaId = rowData.areaId;
			//加载子表
			vm.reloadUserPlanDetail();
			//关闭浮层
			layer.close(index);
		},
		reloadJqGridOrder : function(){//刷新订单列表
			$("#jqGridOrder").jqGrid('setGridParam',{ 
                postData : {
                	orderNo : vm.qorder.orderNo , //单号
                	userMobile : vm.qorder.userMobile ,//手机号码 
                	sourceType : vm.qorder.sourceType//来源
                	},
                page : 1,
                datatype : 'json'
            }).trigger("reloadGrid");
		},
		clearQueryOrderParams : function(){//重置订单浮层的查询条件
			vm.qorder = {
				orderNo : "",
				sourceType : "",
				userMobile : ""
			};
		},
		reloadUserPlanDetail : function(){//加载学员规划课程子表
			if(vm.title == "新增"){
				if(null == vm.courseUserplan.orderId || vm.courseUserplan.orderId ==""){
					return;
				}
				
				$("#detailGrid").jqGrid('setGridParam',{ 
	                postData : {
	                	//新增需要的参数
	                	orderId : vm.courseUserplan.orderId,//订单ID
	                	commodityId : vm.courseUserplan.commodityId , //商品ID
	                	areaId : vm.courseUserplan.areaId , 		//省份ID
	                	isRep : vm.courseUserplan.isRep,		//是否代替课程
	                	isMatch : vm.courseUserplan.isMatch		//是否专业不对口
	                	},
	                page : 1,
	                url : vm.userplanDetail.url,
	                datatype : 'json'
	            }).trigger("reloadGrid");
			}else if(vm.title == "修改"){
//				console.log(vm.courseUserplan);
				if(null == vm.courseUserplan.userPlanId || vm.courseUserplan.userPlanId == ""){
					return;
				}
				$("#detailGrid").jqGrid('setGridParam',{ 
	                postData : {
	                	//修改需要的参数
	                	userPlanId : vm.courseUserplan.userPlanId,//学员规划ID
	                	isRep : vm.courseUserplan.isRep,		//是否代替课程
	                	isMatch : vm.courseUserplan.isMatch		//是否专业不对口
	                	},
	                page : 1,
	                url : vm.userplanDetail.url,
	                datatype : 'json'
	            }).trigger("reloadGrid");
			}
		},
		updateSelClass : function(){
			vm.selClass(function(selectDetail , rowData){
				//班级名称
				vm.courseUserplan.className = rowData.className;
				//班级ID
				vm.courseUserplan.classId = rowData.classId;
				return true;
			});
		},
		updateChangeSelClass : function(){
			vm.selClass(function(selectDetail , rowData){
				//班级名称
				vm.courseUserplanNewChange.className = rowData.className;
				//班级ID
				vm.courseUserplanNewChange.classId = rowData.classId;
				return true;
			});
		},
		selClass : function(callblack){//班级弹窗
			//清空查询条件
			vm.clearQueryClassParams();
			//显示浮层
			vm.showClassLayer(callblack);
			//加载数据
			vm.reloadJqGridClass();
		},
		showClassLayer : function(callblack){//弹出班级选择窗口
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '班级列表',
				area: ['600px', '633px'],
				shadeClose: false,
				content: jQuery("#classlayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					//获取选中行ID
					var selectDetail = getJqGridSelectedRow("jqGridClass");
					if(selectDetail == null){
						return ;
					}
					//行数据
					var rowData = $("#jqGridClass").jqGrid('getRowData',selectDetail);
					if($.isFunction(callblack)){
						if(callblack(selectDetail , rowData)){
							//关闭浮层
							layer.close(index);
						}
					}
	            }
			});
		},
		reloadJqGridClass : function(){//刷新班级列表
			$("#jqGridClass").jqGrid('setGridParam',{ 
                postData : {
                	levelId : vm.qclass.levelId , //层次
                	areaId : vm.qclass.areaId,//省份 
                	professionId : vm.qclass.professionId,//专业
                	userId : vm.qclass.classTeacherId,//班主任
                	className : vm.qclass.className//班级名称
                	},
                page : 1,
                datatype : 'json'
            }).trigger("reloadGrid")
		},
		clearQueryClassParams : function(){//重置班级浮层的查询条件
			vm.qclass.levelId = "";
			vm.qclass.areaId = "";
			vm.qclass.professionId = "";
			vm.qclass.classTeacherId = "";
			vm.qclass.className = "";
		},
		initClassTeacherList : function(){//初始化班主任列表
			var that = this;
			$.ajax({
				type: "GET",
			    url: "../common/classTeacherList",
//			    data: JSON.stringify(userPlanIds),
			    success: function(r){
					if(r.code == 0){
						that.selectData.classTeacherList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		initAreaList: function(){//初始化省份列表
			var that = this;
			$.ajax({
				type: "GET",
			    url: "../common/areaList",
//			    data: JSON.stringify(userPlanIds),
			    success: function(r){
					if(r.code == 0){
						that.selectData.areaList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		initProfessionList: function(){//初始化专业列表
			var that = this;
			$.ajax({
				type: "GET",
			    url: "../common/professionList",
//			    data: JSON.stringify(userPlanIds),
			    success: function(r){
					if(r.code == 0){
						that.selectData.professionList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		initLevelList: function(){//初始化层次列表
			var that = this;
			$.ajax({
				type: "GET",
				url: "../common/levelList",
//			    data: JSON.stringify(userPlanIds),
				success: function(r){
					if(r.code == 0){
						that.selectData.levelList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		classTypeList: function(){//初始化班型列表
			var that = this;
			$.ajax({
				type: "GET",
				url: "../common/classTypeList",
//			    data: JSON.stringify(userPlanIds),
				success: function(r){
					if(r.code == 0){
						that.selectData.classTypeList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		clearQueryUserplanParams : function(){//清空查询条件
			var vm = this;
			vm.qUserplan.orderNo = "";
			vm.qUserplan.mobile = "";
			vm.qUserplan.nickName = "";
			vm.qUserplan.userMobile = "";
			vm.qUserplan.areaId = "";
			vm.qUserplan.levelId = "";
			vm.qUserplan.professionId = "";
			vm.qUserplan.isRep = "";
			vm.qUserplan.isMatch = "";
			vm.qUserplan.classTypeId = "";
			vm.qUserplan.userplanStatus = 0;
			vm.qUserplan.classTeacherId = "";
			vm.qUserplan.deptId = "";
			vm.qUserplan.deptName = "";
			vm.qUserplan.deptIdList = "";
			vm.qUserplan.deptNameList = "";
		},
		updateChangeWin : function(){//转省转专业
			vm.courseUserplanNewChange = {
					commodityId : "",
					commodityName:"",
					areaId:"",
					areaName:"",
					classTypeId : "",
					classTypeName:"",
					levelId : "",
					levelName:"",
					professionId : "",
					professionName:""
			};
			//选择
			var userPlanId = getSelectedRow();
			if(userPlanId == null){
				return ;
			}
			vm.courseUserplanOldChange.userPlanId = userPlanId;
			//转省转专业数据
			$.get("../course/userplan/info/"+userPlanId, function(r){
				vm.courseUserplanOldChange = r.courseUserplan;
			});
			vm.showChangeLayer();
		},
		selectCommodity : function(){//商品弹窗
			goodsInfoLay.show(function(index, opt) {
				vm.courseUserplanNewChange.commodityId = opt.id;
				vm.courseUserplanNewChange.commodityName = opt.name;//商品名
				
				vm.courseUserplanNewChange.classTypeId = opt.classTypeId;
				vm.courseUserplanNewChange.classTypeName = opt.classTypeName;//班型
				
				vm.courseUserplanNewChange.levelId = opt.levelId;
				vm.courseUserplanNewChange.levelName = opt.levelName;//层次
				
				vm.courseUserplanNewChange.professionId = opt.professionId;
				vm.courseUserplanNewChange.professionName = opt.professionName;//专业
			});
		},
		selectArea : function(){//选择省份
			var id = vm.courseUserplanNewChange.commodityId;
			if ($.isNull(id)) {
				alert("请先选择商品！！！");
				return;
			}
			areaGoodsLay.show(id,function(index,opt){
				vm.courseUserplanNewChange.areaId = opt.areaId;
				vm.courseUserplanNewChange.areaName = opt.areaName;
			});
		},
		showChangeLayer : function(){//转省转专业弹窗
			var vm = this;
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '转省转专业',
				area: ['880px', '333px'],
				shadeClose: false,
				content: jQuery("#changelayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					var userPlanId = vm.courseUserplanOldChange.userPlanId;
					var commodityId = vm.courseUserplanNewChange.commodityId;
					var areaId = vm.courseUserplanNewChange.areaId;
					var classTypeId = vm.courseUserplanNewChange.classTypeId;
					var levelId = vm.courseUserplanNewChange.levelId;
					var professionId = vm.courseUserplanNewChange.professionId;
					if(userPlanId == ""){
						alert("请选择一条记录")
						return;
					}
					if($.isNull(commodityId)){
						alert("请选择商品")
						return;
					}
					if($.isNull(areaId)){
						alert("请选择省份")
						return;
					}
					var postData = {
							userPlanId : userPlanId,
							commodityId : commodityId,
							areaId : areaId,
							levelId : levelId,
							classTypeId : classTypeId,
							professionId : professionId
					};
					$.ajax({
						type: "GET",
						url: "../course/userplan/updateChange",
//					    data:  JSON.stringify(postData) ,
						data: postData ,
						success: function(r){
							if(r.code == 0){
								alert('操作成功', function(alertIndex){
									vm.reload();
									//关闭浮层
									layer.close(index);
								});
							}else{
								alert(r.msg);
							}
						}
					});
				}
			});
		},
		initOrderChangeList : function(){//初始化转省转专业可变更订单数据
			$.ajax({
				type: "GET",
				url: "../course/userplan/orderChangeList",
			    data: {userPlanId : vm.courseUserplanOldChange.userPlanId},
				success: function(r){
					if(r.code == 0){
						vm.change.orderChangeList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		initGoodList : function(){//商品
			var that = this;
			$.ajax({
				type: "GET",
				url: "../course/userplan/goodList",
//			    data: JSON.stringify(userPlanIds),
				success: function(r){
					if(r.code == 0){
						that.selectData.commodityList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		queryAreaByCommodityId : function(val, oldVal){//查询商品销售的省份
			var vm = this;
			$.ajax({
				type: "GET",
				url: "../course/userplan/queryAreaByCommodityId",
//			    data: JSON.stringify(userPlanIds),
				data:{commodityId : vm.courseUserplanNewChange.commodityId},
				success: function(r){
					if(r.code == 0){
						vm.selectData.commodityAreaList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		
		
//		服务记录
		showService : function(){
			var userPlanId = getSelectedRow();
			if(userPlanId == null){
				return ;
			}
			vm.showType(3);
			vm.reloadjqGridService();
		},
		reloadjqGridService : function(){//刷新班级列表
			$("#jqGridService").jqGrid('setGridParam',{ 
				postData : {
					userplanId : getSelectedRow()
				},
				page : 1,
				datatype : 'json'
			}).trigger("reloadGrid")
		},
		/////////////////服务记录操作/////////////////////
		addService:function(){//新增服务记录
			//服务记录编辑弹窗标题
			vm.serviceLayerTitle = "新增服务记录";
			//初始化服务记录录入内容
			vm.serviceData = {
				userplanServiceId:"",
				userplanId : getSelectedRow(),
				userplanServiceTime:"",
				nextTime:"",
				serviceTypeId:"",
				serviceTypeContent:"",
				serviceFbId:"",
				serviceFbContent:"",
				remark:""
			};
			//初始化当前日期
			$("#userplanServiceTime").val(new Date().format("yyyy-MM-dd hh:mm:00"));
			$("#nextTime").val("");
			//弹窗
			vm.showServiceLayer();
			//服务记录新增地址
			vm.serviceUrl="../courseuserplanservice/save";
		},
		updateService:function(){//修改服务记录
			//服务记录编辑弹窗标题
			vm.serviceLayerTitle = "修改服务记录";
			var serviceId = getJqGridSelectedRow("jqGridService");
			if(serviceId == null){
				return ;
			}
			//从服务端获取服务记录详情
			vm.getServiceInfo(serviceId);
			//修改地址
			vm.serviceUrl = "../courseuserplanservice/update";
			vm.showServiceLayer();
		},
		getServiceInfo : function(serviceId){//获取服务记录详情
			$.ajax({
				type: "GET",
				url: "../courseuserplanservice/info/"+serviceId,
//				data:{userplanServiceId : serviceId},
				success: function(r){
					if(r.code == 0){
						vm.serviceData = r.data;
						 $("#userplanServiceTime").val(vm.serviceData.userplanServiceTime);
						 $("#nextTime").val(vm.serviceData.nextTime);
					}else{
						alert(r.msg);
					}
				}
			});
		},
		showServiceLayer:function(){//服务记录弹窗
			var vm = this;
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.serviceLayerTitle,
				area: ['600px', '450px'],
//				area: ['100px', '150px'],
				shadeClose: false,
				content: jQuery("#serviceLayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					
					vm.serviceData.userplanServiceTime = $("#userplanServiceTime").val();
					vm.serviceData.nextTime = $("#nextTime").val();
					
					//服务记录内容提交
					$.ajax({
						type: "POST",
					    url: vm.serviceUrl,//地址
					    data: JSON.stringify(vm.serviceData),//data数据
					    success: function(r){
					    	if(r.code === 0){
								alert('操作成功', function(index1){
									//重新加载服务记录
									vm.reloadjqGridService();
									//关闭浮层
									layer.close(index);
								});
							}else{
								alert(r.msg);
							}
						}
					});
					
	            }
			});
		},
		delService : function(){//删除服务记录
			var ids = getJqGridSelectedRows("jqGridService");
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseuserplanservice/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								//重新加载服务记录
								vm.reloadjqGridService();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		serviceTypeChange : function(){//服务类型select改变 修改服务内容
			var info = vm.getServiceRecordInfo(vm.serviceData.serviceTypeId);
			if(info){
				vm.serviceData.serviceTypeContent = info.serviceContent;
			}
		},
		//////////////学员规划详情操作//////////////////
		iadd: function(){
			vm.detailItem ={
				userplanId : "",
				areaId : "",
				areaName : "",
				courseId : "",
				courseName : "",
				isPass:0,
				isSubstituted : 0,
				isSubstitute : 0,
				isSuitable : 0,
				courseType : 0,
				title:"新增",
				classPlanName:""
			};
			vm.ishow();
		},
		ishow : function(){//弹出新增或者修改窗口
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.detailItem.title,
				area: ['550px', '350px'],
				shadeClose: false,
				content: jQuery("#materialUserPlanLayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
					if($.isNull(vm.detailItem.areaId)){
						alert("请选择省份！");
						return
					}
					if($.isNull(vm.detailItem.courseId)){
						alert("请选择课程！");
						return;
					}
					if($.isNull(vm.detailItem.courseType)){
						alert("请选择课程类型！");
						return;
					}
					
					if( 0 == vm.detailItem.courseType){
						vm.detailItem.isSubstituted = 0;//被代替
						vm.detailItem.isSubstitute = 0;//代替
						vm.detailItem.isSuitable = 0;//专业不对口
					} else if(1 == vm.detailItem.courseType){
						vm.detailItem.isSubstituted = 1;//被代替
						vm.detailItem.isSubstitute = 0;//代替
						vm.detailItem.isSuitable = 0; //专业不对口
					} else if (2 == vm.detailItem.courseType) {
						vm.detailItem.isSubstituted = 0; //被代替
						vm.detailItem.isSubstitute = 1; //代替
						vm.detailItem.isSuitable = 0; //专业不对口
					} else if (3 == vm.detailItem.courseType) {
						vm.detailItem.isSubstituted = 0; //被代替
						vm.detailItem.isSubstitute = 0; //代替
						vm.detailItem.isSuitable = 1; //专业不对口
					}
					//全国统考 默认统考
					vm.detailItem.isUnitedExam = 1;
					vm.detailItem.userplanId=vm.courseUserplan.userPlanId;
					$.ajax({
						type: "POST",
					    url: "../course/userplan/saveDetail",
					    data: JSON.stringify(vm.detailItem),
					    success: function(r){
					    	if(r.code === 0){
								alert('操作成功', function(index){
									vm.reloadUserPlanDetail();
								});
							}else{
								alert(r.msg);
							}
						}
					});
					//行ID
					  /*var rowId = new Date().getTime();
					   $("#detailGrid").addRowData(rowId, vm.detailItem, "last");*/  
					layer.close(index);
					
	            }
			});
		},
		idel:function(){
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("detailGrid");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#detailGrid").jqGrid('getRowData',selectDetail);
			//str=>array
			rowData.numbers = rowData.numbers;
			//
			vm.detailItem = rowData;
			
			/*if(vm.detailItem.classPlanName!=""){
				alert("不能被删除，此处有被引用");
			}*/
			/*else{*/
				confirm('确定要删除选中的记录？', function(){/*
					$.each(selectDetail , function(i , val){
						$("#detailGrid").jqGrid("delRowData", val);  
					});
					$("#detailGrid").jqGrid("delRowData", selectDetail);
					return true;
				*/
					$.ajax({
						type: "POST",
					    url: "../course/userplan/deleteDetail/"+selectDetail,
					    success: function(r){
							if(r.code == 0){
								alert('操作成功', function(index){
									vm.reloadUserPlanDetail();
								});
							}else{
								alert(r.msg);
							}
						}
					});
				
				});
			/*}*/
			
		},
/////////////编辑子表浮层//////////////////
		areaLayerShowDetail : function(){//省份
			areaLay.show(function(index, opt) {
				vm.detailItem.areaId = opt.areaId;
				vm.detailItem.areaName = opt.areaName;
			});
		},
		courseLayerShowDetail : function(){//课程
			courseLay.show(function(index, opt) {
				vm.detailItem.courseId = opt.courseId;
				vm.detailItem.courseName = opt.courseName;
			});
		},
		feedbackRecordChange : function(){//反馈类型改变 修改反馈内容
			var info = vm.getFeedbackRecordInfo(vm.serviceData.serviceFbId);
			if(info){
				vm.serviceData.serviceFbContent = info.feedbackContent;
			}
		},
		initServiceRecordList : function(){//初始化服务类型列表数据
			var that = this;
			$.ajax({
				type: "GET",
			    url: "../common/serviceRecordList",
//			    data: JSON.stringify(userPlanIds),
			    success: function(r){
					if(r.code == 0){
						that.selectData.serviceRecordList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		initFeedbackRecordList : function(){//初始化反馈类型列表数据
			var that = this;
			$.ajax({
				type: "GET",
				url: "../common/feedbackRecordList",
//			    data: JSON.stringify(userPlanIds),
				success: function(r){
					if(r.code == 0){
						that.selectData.feedbackRecordList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		getServiceRecordInfo:function(id){//选中的服务类型详情
			var info = null;
			if($.isNotNull(id) && vm.selectData.serviceRecordList.length > 0){
				for(var i = 0 ; i < vm.selectData.serviceRecordList.length ; i++){
					var item = vm.selectData.serviceRecordList[i];
					if(item.id == id){
						info = item;
						break;
					}
				}
			}
			return info;
		},
		getFeedbackRecordInfo:function(id){//选中的服务反馈类型详情
			var info = null;
			if($.isNotNull(id) && vm.selectData.feedbackRecordList.length > 0){
				for(var i = 0 ; i < vm.selectData.feedbackRecordList.length ; i++){
					var item = vm.selectData.feedbackRecordList[i];
					if(item.id == id){
						info = item;
						break;
					}
				}
			}
			return info;
		},
		deptLayerShowQuery : function(){//部门查询条件
			/*deptLay.show(function(id , name ,opt){
             vm.qUserplan.deptId = id;
             vm.qUserplan.deptName = name;
             });*/
            deptQueryLay.show(function(opt,deptIdList,deptNameList){
                vm.qUserplan.deptIdList = deptIdList.join(",");
                vm.qUserplan.deptNameList = deptNameList;
            });
		},
		showAddOrderBatch : function(){
			vm.clearMallOrderBatch();
			vm.showType(4);
		},
		clearMallOrderBatch : function(){
			vm.mallOrderBatch = {
				commodityId : "",//商品ID
				commodityName : "",//商品名称
			},
			vm.mallClassBatch = {
					classId : 0,//班级ID
					className : "",//班级名称
			}
		},
		goodsInfoLayerShow : function(){//商品信息浮层
			goodsInfoLay.show(function(index,opt){
				//商品ID
				vm.mallOrderBatch.commodityId = opt.id;
				//商品名称
				vm.mallOrderBatch.commodityName = opt.name;
			});
		},
		classLayerShow : function(){//班级浮层
			classLay.show(function(index,opt){
				vm.mallClassBatch.classId = opt.classId;
				vm.mallClassBatch.className = opt.className;
			});
		},
		addOrderBatch : function(){
			if($.isNull(vm.mallOrderBatch.commodityId)){//非空
				alert("请选择商品!");
				return;
				vm.goodsInfoLayerShow();
			}
			confirm('您确定要批量生成学员规划吗？', function(){
				$.ajax({
					beforeSend : function(r){
						hq.load();
					},
					type: "POST",
				    url: "../course/userplan/saveBatch/" + vm.mallOrderBatch.commodityId+"/"+vm.mallClassBatch.classId,
//				    data: JSON.stringify(userPlanIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作完成'+(r.data || ''));
						}else{
							alert(r.msg);
						}
					},
					complete : function(r){
						hq.hide();
					}
				});
			});
		},
	},created : function(){
		var vm = this;
		//初始化班主任列表
		vm.initClassTeacherList();
		//初始化省份列表
		vm.initAreaList();
		//初始化专业列表
		vm.initProfessionList();
		//初始化层次列表
		vm.initLevelList();
		//初始化班型列表
		vm.classTypeList();
		//初始化班型列表
		vm.initGoodList();
		//服务类型
		vm.initServiceRecordList();
		//反馈类型文档
		vm.initFeedbackRecordList();
	}
});