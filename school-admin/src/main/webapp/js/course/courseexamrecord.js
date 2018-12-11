$(function () {
    $("#jqGrid").jqGrid({
        url: '../course/courseexamrecord/list',
        datatype: "json",
        colModel: [		
        	
				{ label: '报考登记Id', name: 'examRecordId', key: true ,hidden:true},
				{ label: '单号', name: 'examRecordNo', width: 80 }, 						
				{ label: '备注', name: 'remark', width: 80 }, 							
				{ label: '创建时间', name: 'createTime', width: 80 }, 						
				{ label: '修改时间', name: 'modifyTime', width: 80 }, 						
				{ label: '创建人', name: 'createdName', width: 80 }, 						
				{ label: '修改人', name: 'modifiedName', width: 80 }, 
				{ label: '产品线', name: 'productName', width: 80 }, 
				{ label: '审核状态', name: 'audited', width: 80, formatter: function(value, options, row){
      				return value === 0 ? '<span class="label label-danger">未通过</span>' : '<span class="label label-success">已通过</span>';
       		   	}},			
				{ label: '审核状态vaule', name: 'audited', width: 80, hidden: true }		
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
    
    // ------------------------------------------------------------detailGrid------------------------------------
    $("#detailGrid").jqGrid({
		datatype : "json",
        url: '../course/courseexamrecord/queryExamRecordDetail',
		colModel : [
			{ label: 'examRecordDetailId', name: 'examRecordDetailId', key: true, hidden: true },
			{ label: '报考登记PK', name: 'examRecordId', width: 80, hidden:true }, 
			{ label: '学员规划子表ID', name: 'userplanDetailId', width: 80, hidden:true }, 
			{ label: '学员ID', name: 'userId', width: 80 },
			{ label: '学员昵称', name: 'userName', width: 120 },
			{ label: '手机', name: 'userMobile', width: 120 },
			{ label: '课程ID', name: 'courseId', width: 80, hidden: true },
			{ label: '课程', name: 'courseName', width: 120 },
			{ label: '报名省份ID', name: 'areaId', width: 80, hidden:true },
			{ label: '报名省份', name: 'areaName', width: 120 },
			{ label: '报考省份ID', name: 'examareaId', width: 80, hidden:true },
			{ label: '报考省份', name: 'examareaName', width: 120,formatter: function(value, options, row){
		        if($.isNotNull(row.examareaName)){
                    return value;
                }else {
		            return row.areaName;
                }
            }},
			{ label: '备注', name: 'remark', width: 150 }
		],
		viewrecords: true,
        height: 'auto',
        rowNum: 30,
		//rowList : [10,30,50],
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
    $("#detailGrid2").jqGrid({
		datatype : "json",
        url: '../course/courseexamrecord/queryExamRecordDetail',
		colModel : [
			{ label: 'examRecordDetailId', name: 'examRecordDetailId', key: true, hidden: true },
			{ label: '报考登记PK', name: 'examRecordId', width: 80, hidden:true },
			{ label: '学员规划子表ID', name: 'userplanDetailId', width: 80, hidden:true },
			{ label: '学员ID', name: 'userId', width: 80 },
			{ label: '学员昵称', name: 'userName', width: 120 },
			{ label: '手机', name: 'userMobile', width: 120 },
			{ label: '课程ID', name: 'courseId', width: 80, hidden: true },
			{ label: '课程', name: 'courseName', width: 120 },
			{ label: '报名省份ID', name: 'areaId', width: 80, hidden:true },
			{ label: '报名省份', name: 'areaName', width: 120 },
			{ label: '报考省份ID', name: 'examareaId', width: 80, hidden:true },
			{ label: '报考省份', name: 'examareaName', width: 120,formatter: function(value, options, row){
		        if($.isNotNull(row.examareaName)){
                    return value;
                }else {
		            return row.areaName;
                }
            }},
			{ label: '备注', name: 'remark', width: 150 }
		],
		viewrecords: true,
        height: 'auto',
        rowNum: 30,
		//rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 25,
        autowidth:true,
        multiselect: true,
	    pager: "#detailGridPager2",
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
		   $("#detailGrid2").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
	    }
	});

    // ------------------------------------------------------------学员规划弹窗------------------------------------
    $("#userplanGrid").jqGrid({
		url: "../course/courseexamrecord/userplanList",
		datatype: "local",
		colModel: [
			{ label: '主键', name: 'userPlanId',width: 30 , key: true , hidden: true},
            { label: '班型PK', name: 'classTypeId', width: 80 , hidden: true },
            { label: '订单ID', name: 'orderId', width: 80 , hidden : true}, 
            { label: '学员PK', name: 'userId', width: 30 , hidden : true}, 
            { label: '最近考试时段', name: 'examScheduleId', width: 80, hidden : true },
            
            { label: '学员', name: 'userName', width: 100 }, 
            { label: '学员手机', name: 'userMobile', width: 100 }, 
            { label: '商品', name: 'commodityName', width: 100 }, 
            { label: '专业', name: 'professionName', width: 80 },
            { label: '省份', name: 'areaName', width: 60 },
            { label: '班级', name: 'className', width: 80 }, 
            { label: '班型', name: 'classTypeName', width: 80 }, 			
            { label: '层次', name: 'levelName', width: 60 }, 
            { label: '班主任', name: 'teacherName', width: 80 }, 
            { label: '可代替课程', name: 'isRep', width: 80 , formatter: function(value, options, row){
  				return value === 0 ? '<span class="label label-success">学习</span>' : '<span class="label label-danger">不学习</span>';
  		   	}}, 			
  		   	{ label: '专业对口', name: 'isMatch', width: 80 , formatter: function(value, options, row){
  				return value === 0 ? '<span class="label label-success">对口</span>' : '<span class="label label-danger">不对口</span>';
  		   	}},
            { label: '可代替课程value', name: 'isRep', width: 80, hidden: true }, 			
            { label: '专业对口value', name: 'isMatch', width: 80, hidden: true },
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
            { label: '毕业时间', name: 'graduateTime', width: 80 }, 			
            { label: '最近考试时段', name: 'examScheduleName', width: 100 }	
	           ],
	   viewrecords: true,
	   height: 430,
	   rowNum: 10,
	   rowList : [10,30,50],
	   rownumbers: true, 
	   rownumWidth: 25, 
	   autowidth:true,
	   multiselect: true,
	   pager: "#userplanGridPager",
	   jsonReader : {
		   root: "data.list",
		   page: "data.currPage",
		   total: "data.totalPage",
		   records: "data.totalCount"
	   },
	   gridComplete:function(){
		   //隐藏grid底部滚动条
		   //$("#userplanGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
	   }
	});
    
 // ------------------------------------------------------------学员规划-子表弹窗------------------------------------
    
    $("#upDetailGrid").jqGrid({
		url : '../course/userplan/courseListByUserPlanId',
		datatype: "local",
        colModel: [			
                   { label: 'id', name: 'id', key: true ,hidden: true},
                   { label: 'areaId', name: 'areaId',hidden: true},
                   { label: 'courseId', name: 'courseId',hidden: true},
                   { label: 'classId', name: 'classId',hidden: true},
                   { label: 'userId', name: 'userId',hidden: true},
                   
                   { label: '省份', name: 'areaName', width: 80 }, 			
                   { label: '课程', name: 'courseName', width: 120 }, 			
                   { label: '课程编号', name: 'courseNo', width: 80 },			
                   { label: '是否通过', name: 'isPass', width: 80 , formatter: function(value, options, row){
       				return value === 1 ? '<span class="label label-success">通过</span>' : '<span class="label label-danger">未通过</span>';
       				}}, 			
                   { label: '可代替课程', name: 'isSubstituted', width: 100 , formatter: booleanFormat}, 			
                   { label: '代替课程', name: 'isSubstitute', width: 100 , formatter: booleanFormat}, 			
                   { label: '专业不对口课程', name: 'isSuitable', width: 100 , formatter: booleanFormat}, 			
                   { label: '全国统考', name: 'isUnitedExam', width: 100 , formatter: booleanFormat }, 			
                   ],
		viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#upDetailGridPager",
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
        	//$("#upDetailGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
	});
});

function booleanFormat(value, options, row){
	return value === 1 ? '<span class="label label-success">是</span>' : '<span class="label label-danger">否</span>';
}

$(function () {
    $("#buttonUpload").click(function () {
        ajaxFileUpload();
    })
})
function ajaxFileUpload(){
	$.ajaxFileUpload(
		{
			url:'/course/courseexamrecord/getExcelCourseExamRecordData',
			secureuri:true,
			fileElementId:'file_data',
			dataType:'json',
			success:function(data){
				if(data.code == 0) {
					alert("文件上传成功", function(index){
						$("#jqGrid").trigger("reloadGrid");
					});
					}
				else if(data.code == 1) { 
					alert(data.msg);
					}
			    else{
			    	
					 }
					
			}
		}
	);
	/*return true;*/
}
/*function exportFile(){
	var urlstr = '../course/courseexamrecord/exportExcelCourseExamRecordTemplate';
	window.location.href = urlstr;
	return true;
//	window.open("../course/courseexamrecord/exportExcelCourseExamRecordTemplate",'new4','left=220,top=40,toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes');
};*/
var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		updateshowList: false,
		onlookshowList: false,
		title: null,
		courseExamRecord: {
			productId : "",
			productName : "",
            detailList:{}
		},
		courseProductLine: {
			productId : "",
			productName : ""
		},
		q:{//查询条件
			examRecordNo: null,
            userId : "",//学员ID
            userName : "",//学员名称
            userMobile : "",//学员手机号
            examareaId:"", //报考省份名称
            examareaName:"" //报考省份名称
		},
        selectData:{
            areaList :[],
        },

		detail:{
			title:null,
			//ids: [],
			userplan:{
				userPlanId : "",//学员规划id
				userplanDetailId : "",//学员规划子表id
				userId : "",//学员ID
				userName : "",//学员名称
				userMobile : "",//学员手机号
				courseId : "",//课程id
				courseName : "",//课程名称
				areaId : "",//省份id
				areaName : "",//省份
				isRep : "",//是否替换,查询条件
				isMatch : "",//是否专业对口,查询条件
				remark : ""	,//报考登记子表备注
				productId:"",
                examareaId : "",//报考省份id
                examareaName:"" //报考省份名称
			}
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.updateshowList = true;
			vm.onlookshowList = false;
			vm.title = "新增";
			vm.courseExamRecord = {};
			$.ajax({
				type: "POST",
			    url: "/course/courseexamrecord/getNo",
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseExamRecord = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
			vm.inintUserplan();
			jQuery("#detailGrid").jqGrid("clearGridData");
			$("#detailBtn").attr("style", "display: block");
            $("#remark").removeAttr("readonly");
            $("#okBtn").show();
            $("#acceptBtn").hide();
            $("#reacceptBtn").hide();
		},
		update: function (event) {
			var examRecordId = getSelectedRow();
			if(examRecordId == null){
				return ;
			}
            vm.showList = false;
            vm.updateshowList = true;
            vm.onlookshowList = false;
            vm.title = "修改";
            vm.inintUserplan();
            jQuery("#detailGrid").jqGrid("clearGridData");
            vm.getInfo(examRecordId);
            $("#detailBtn").attr("style", "display: block");
            $("#remark").removeAttr("readonly");
            $("#okBtn").show();
            $("#acceptBtn").hide();
            $("#reacceptBtn").hide();
		},
		onlook:function (event) {
            var examRecordId = getSelectedRow();
            if(examRecordId == null){
                return ;
            }
            vm.showList = false;
            vm.updateshowList = false;
            vm.onlookshowList = true;
            vm.title = "查看";
            vm.inintUserplan();
            jQuery("#detailGrid2").jqGrid("clearGridData");
            vm.getInfo(examRecordId);
            //vm.iquery();
            $("#detailBtn").attr("style", "display: block");
            $("#remark").removeAttr("readonly");
            $("#okBtn").show();
            $("#acceptBtn").hide();
            $("#reacceptBtn").hide();
        },
		audit: function (event) {
			var examRecordId = getSelectedRow();
			var rowData = $("#jqGrid").jqGrid('getRowData',examRecordId);
			if(examRecordId == null){
				return ;
			}
            vm.showList = false;
            vm.updateshowList = true;
            vm.onlookshowList = false;
            vm.title = "审核";
            jQuery("#detailGrid").jqGrid("clearGridData");
            vm.getInfo(examRecordId);
            $("#detailBtn").attr("style", "display: none");
            $("#remark").attr("readonly", "readonly");
            $("#okBtn").hide();
            if(rowData.audited == 0){
            	$("#acceptBtn").show();
            	$("#reacceptBtn").hide();
            }else{
            	$("#acceptBtn").hide();
            	$("#reacceptBtn").show();
            }
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../course/courseexamrecord/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../course/courseexamrecord/update";
		    }
		    else
		    {
		       url = "";
		    }
		    //获取子表数据
		    var details = [] ;
			var ids = $("#detailGrid").jqGrid('getDataIDs');
			for(var i = 0;i<ids.length;i++){
				var row = $('#detailGrid').jqGrid('getRowData',ids[i]);
				if(isNaN(row.id)){
    				row.id = null;
    			}
			    details.push(row);
			}
			vm.courseExamRecord.detailList = details;
			vm.courseExamRecord.productId=vm.courseProductLine.productId;
			vm.courseExamRecord.productName=vm.courseProductLine.productName;
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseExamRecord),
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
		exportTemplate: function (event) {
			var urlstr = "../course/courseexamrecord/exportExcelCourseExamRecordTemplate";
			window.location.href = urlstr;
		},
		exportScoreTemplate: function (event) {
			var examRecordId = getSelectedRow();
			if(examRecordId == null){
				return ;
			}
			var urlstr = "../course/coursescorerecord/exportExcelCourseScoreRecordTemplate?examRecordId="+examRecordId;
			window.location.href = urlstr;
		},
		accept: function (event) {
			confirm('确定要通过此登记吗？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/courseexamrecord/accept",
				    data: JSON.stringify(vm.courseExamRecord),
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
			})
		},
		reaccept: function (event) {
			confirm('确定要取消通过此登记吗？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/courseexamrecord/reaccept",
				    data: JSON.stringify(vm.courseExamRecord),
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
			})
		},
		del: function (event) {
			var examRecordIds = getSelectedRows();
			if(examRecordIds == null){
				return ;
			}
			console.log(examRecordIds);
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/courseexamrecord/delete",
				    data: JSON.stringify(examRecordIds),
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
		getInfo: function(examRecordId){
            $.ajax({
				type: "POST",
			    url: "../course/courseexamrecord/info/"+examRecordId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseExamRecord = r.examRecord;
						 vm.courseProductLine.productName=vm.courseExamRecord.productName;
						 vm.iquery();
						/* var detailList = r.examRecordDetail;
			                //报考登记子表
			                if(null != detailList && detailList.length > 0){
			                	$.each(detailList , function(i , v){
			                		//添加行
									$("#detailGrid").addRowData(v.examRecordDetailId, v, "last");
									//$("#detailGrid2").addRowData(v.examRecordDetailId, v, "last");
			                	});
			                }*/
					}else{
						alert(r.msg);
					}
				}
			});
		},
		
		reload: function (event , p) {
			vm.showList = true;
            vm.updateshowList = false;
            vm.onlookshowList = false;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"examRecordNo" : vm.q.examRecordNo},
				page:page
            }).trigger("reloadGrid");
		},
		inintUserplan:function(){
			vm.detail.userplan.userPlanId = "";
			vm.detail.userplan.userplanDetailId = "";
			vm.detail.userplan.userId = "";
			vm.detail.userplan.userName = "";
			vm.detail.userplan.userMobile = "";
			vm.detail.userplan.courseId = "";
			vm.detail.userplan.courseName = "";
			vm.detail.userplan.areaId = "";
			vm.detail.userplan.areaName = "";
			vm.detail.userplan.examareaId = "";
			vm.detail.userplan.examareaName = "";
			vm.detail.userplan.isRep = "";
			vm.detail.userplan.isMatch = "";
			vm.detail.userplan.remark = "";
			vm.courseProductLine.productId = "";
			vm.courseProductLine.productName = "";
			vm.q.examareaId = "";
			vm.q.examareaName = "";
			vm.q.userName = "";
			vm.q.userMobile = "";
		},
		inintUserplanDetail:function(){
			vm.detail.userplan.courseId = "";
			vm.detail.userplan.courseName = "";
			vm.detail.userplan.areaId = "";
			vm.detail.userplan.areaName = "";
			vm.detail.userplan.remark = "";
			vm.detail.userplan.productId = "";
            vm.detail.userplan.userName = "";
            vm.detail.userplan.examareaName = "";
		},
		iadd:function(){
			if(vm.courseProductLine.productName.trim().length==0){
				alert("请先选择产品线");
				return;
			}
			//弹框标题
			vm.detail.title = "新增";
			//初始化弹窗数据
			vm.inintUserplanDetail();
			vm.detail.userplan.productId=vm.courseProductLine.productId;
			//新增子表弹窗
			vm.ishow();
		},
        iupdate:function () {
		    var detailId = getJqGridSelectedRow("detailGrid");
		    if(detailId == null){
		        return;
            }

            vm.detail.title = "修改报考省份"
            vm.iishow(detailId);
        },

		iishow : function(detailId){//弹出新增或者修改窗口
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.detail.title,
				area: ['600px', '150px'],
				shadeClose: false,
				content: jQuery("#detailUpdateLayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
                   $("#detailGrid").setRowData(detailId,{"examareaName":vm.detail.userplan.examareaName,"examareaId":vm.detail.userplan.examareaId});
					layer.close(index);
					
	            }
			});
		},
		ishow : function(){//弹出新增或者修改窗口
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.detail.title,
				area: ['600px', '300px'],
				shadeClose: false,
				content: jQuery("#detailLayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
					//非空校验
					//课次名称
					var userPlanId = vm.detail.userplan.userPlanId;
					if(null == userPlanId || userPlanId == ""){
						alert("请选择学员规划");
						return;
					}
					var courseId = vm.detail.userplan.courseId;
					if(null == courseId || courseId == ""){
						alert("请选择规划课程");
						return;
					}

					if("新增" == vm.detail.title){
						//行ID
						var rowId = new Date().getTime();
						//添加行
						$("#detailGrid").addRowData(rowId, vm.detail.userplan, "last");
					}
					layer.close(index);

	            }
			});
		},
		idel:function(){
			//获取选中行ID
			var selectDetails = getJqGridSelectedRows("detailGrid");
			if(selectDetails == null){
				return;
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
        iquery:function(){  //详情子表查询
            vm.ireload(null,1);
		},
        ireload: function (event , p) {
            if(vm.title == "新增"){
                alert("新增还没有订单信息,查询不可用!");
                return;
            }
            var examRecordId = getSelectedRow();
            var page1 = p || $("#detailGrid").jqGrid('getGridParam','page');
            $("#detailGrid").jqGrid('setGridParam',{
                postData : {"examRecordId":examRecordId,"userName" : vm.q.userName,"mobile":vm.q.userMobile,"examareaId":vm.q.examareaId},
                page:page1
            }).trigger("reloadGrid");
            var page = p || $("#detailGrid2").jqGrid('getGridParam','page');
            $("#detailGrid2").jqGrid('setGridParam',{
                postData : {"examRecordId":examRecordId,"userName" : vm.q.userName,"mobile":vm.q.userMobile,"examareaId":vm.q.examareaId},
                page:page
            }).trigger("reloadGrid");
        },
		useplanShow : function(){//学员规划弹窗
			userplanLay.show(function(index,opt){
				console.log(opt);
				//学员计划ID
				vm.detail.userplan.userPlanId = opt.userPlanId;
				//学员id
				vm.detail.userplan.userId = opt.userId;
				//学员名称
				vm.detail.userplan.userName = opt.userName;
				//学员手机号
				vm.detail.userplan.userMobile = opt.userMobile;
				//报名省份名称
				vm.detail.userplan.areaName = opt.areaName;
				//报考省份名称
				vm.detail.userplan.examareaName = opt.areaName;

			});
		
		},
		useplanDetailShow : function(){//学员规划详情弹窗
			var userPlanId = vm.detail.userplan.userPlanId;
			if(null == userPlanId || userPlanId == ""){
				alert("请先选择学员规划");
				return;
			}
			userplanDetailLay.show(function(index,opt){
				console.log(opt);
				//学员规划详情id
				vm.detail.userplan.userplanDetailId = opt.userplanDetailId;
				//课程id
				vm.detail.userplan.courseId = opt.courseId;
				//课程名称
				vm.detail.userplan.courseName = opt.courseName;
				//省份id
				vm.detail.userplan.areaId = opt.areaId;
			});

		},
        examareaShow : function(){//报考省份弹窗
            if (vm.detail.title == "新增"){
			    var userPlanId = vm.detail.userplan.userPlanId;
			    if(null == userPlanId || userPlanId == ""){
				alert("请先选择学员规划");
				return;
			    }
            }
            areaLay.show(function(index,opt){
                vm.detail.userplan.examareaId = opt.areaId;
                vm.detail.userplan.examareaName = opt.areaName;
            });
		},
        queryExamareaShow : function(){//查询报考省份弹窗
            areaLay.show(function(index,opt){
                vm.q.examareaId = opt.areaId;
                vm.q.examareaName = opt.areaName;
                vm.detail.userplan.examareaId = opt.areaId;
                vm.detail.userplan.examareaName = opt.areaName;
            });
		},

		selUserplan : function(callblack){//班级弹窗
			//显示浮层
			vm.showUserplanLay(callblack);
			//加载数据
			vm.reloadUserplanJqGrid();
		},
		
		showUserplanLay : function(callblack){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '学员规划',
				area: ['1300px', '633px'],
				shadeClose: false,
				content: jQuery("#userplanlayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					//获取选中行ID
					var selectDetail = getJqGridSelectedRow("userplanGrid");
					if(selectDetail == null){
						return ;
					}
					//行数据
					var rowData = $("#userplanGrid").jqGrid('getRowData',selectDetail);
					if($.isFunction(callblack)){
						if(callblack(selectDetail , rowData)){
							//关闭浮层
							layer.close(index);
						}
					}
	            }
			});
		},
		
		reloadUserplanJqGrid : function(){//刷新学员规划列表
			$("#userplanGrid").jqGrid('setGridParam',{
                page : 1,
                //url : "../course/courseexamrecord/userplanList",
                datatype : 'json',
                postData:vm.detail.userplan.productId,
                postData : {
                	productId : vm.detail.userplan.productId
                	}
            }).trigger("reloadGrid")
		},
		
		userplanDetailShow : function(){
			var userPlanId = vm.detail.userplan.userPlanId;
			if(null == userPlanId || userPlanId == ""){
				alert("请先选择学员规划");
				return;
			}
			vm.selupDetail(function(selectDetail , rowData){
				//学员规划detail_id
				vm.detail.userplan.userplanDetailId = rowData.id;
				//学员规划-课程id
				vm.detail.userplan.courseId = rowData.courseId;
				//学员规划-课程名称
				vm.detail.userplan.courseName = rowData.courseName;
				//省份id
				vm.detail.userplan.areaId = rowData.areaId;
				//省份
				vm.detail.userplan.areaName = rowData.areaName;
				console.log(vm.detail.userplan);
				return true;
			});
		},
		
		selupDetail : function(callblack){//班级弹窗
			//显示浮层
			vm.showUserplanDetailLay(callblack);
			
			//加载数据
			vm.reloadupDetailJqGridClass();
		},
		
		showUserplanDetailLay : function(callblack){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '学员规划-课程列表',
				area: ['825px', '633px'],
				shadeClose: false,
				content: jQuery("#userplanDetaillayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					//获取选中行ID
					var selectDetail = getJqGridSelectedRow("upDetailGrid");
					if(selectDetail == null){
						return ;
					}
					//行数据
					var rowData = $("#upDetailGrid").jqGrid('getRowData',selectDetail);
					if($.isFunction(callblack)){
						if(callblack(selectDetail , rowData)){
							//关闭浮层
							layer.close(index);
						}
					}
	            }
			});
		},
		
		reloadupDetailJqGridClass : function(){//刷新班级列表
			
			var ids = $("#detailGrid").jqGrid('getDataIDs');
			var userplanDetailIds = "";
			for(var i = 0;i<ids.length;i++){
				var row = $('#detailGrid').jqGrid('getRowData',ids[i]);
				userplanDetailIds+=row.userplanDetailId + ",";
			}
			userplanDetailIds = userplanDetailIds.length > 0 ? userplanDetailIds.substring(0,userplanDetailIds.length-1) : userplanDetailIds;
			$("#upDetailGrid").jqGrid('setGridParam',{ 
                postData : {
                	userPlanId : vm.detail.userplan.userPlanId,//学员规划ID
                	isRep : vm.detail.userplan.isRep,		//是否代替课程
                	isMatch : vm.detail.userplan.isMatch,		//是否专业不对口
                	userplanDetailIds : userplanDetailIds //已被选中的学员规划子表ID
                	},
                page : 1,
                url : '../course/courseexamrecord/courseListByUserPlanId',
                datatype : 'json'
            }).trigger("reloadGrid")
		},
		selectProduct : function(){
			productLay.show(function(index,opt){
				vm.courseProductLine.productId = opt.productId;
				vm.courseProductLine.productName = opt.productName;
			});
		},
        initAreaList: function(){//初始化省份列表
            var that = this;
            $.ajax({
                type: "GET",
                url: "../common/areaList",
                success: function(r){
                    if(r.code == 0){
                        that.selectData.areaList = r.data;
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
		uploadExcelMethod : function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '导入Excel',
				area: ['300px', '200px'],
				shadeClose: false,
				content: jQuery("#uploadExcel"),
				btn: ['确定','取消'],
				btn1: function (index) {
					$.ajaxFileUpload({
						url:'../course/courseexamrecord/getExcelCourseExamRecordData',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						success:function(data){
							if(data.code == 0 && data.msg != null) {
								alert("文件上传失败！！！"+"<br/>"+data.msg, function(index){
									$("#jqGrid").trigger("reloadGrid");
								});
								layer.close(index);
							}else if(data.code == 0 && data.msg == null){
                                alert("文件上传成功！！！", function(index){
                                    $("#jqGrid").trigger("reloadGrid");
                                });
                                layer.close(index);
                            }else if (data.code == 1) {
								alert(data.msg);
							}
						}
					});
	            }
			});
		}
	},
    created : function(){
        var vm = this;
        //初始化省份列表
        vm.initAreaList();
    }
});
