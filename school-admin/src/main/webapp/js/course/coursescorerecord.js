$(function () {
    $("#jqGrid").jqGrid({
        url: '../course/coursescorerecord/list',
        datatype: "json",
        colModel: [		
        	
			{ label: '分数登记ID', name: 'scoreRecordId', key: true, hidden: true },
			{ label: '分数登记单号', name: 'scoreRecordNo', width: 80 }, 						
			{ label: '报考登记ID', name: 'examRecordId', width: 80, hidden: true }, 
			{ label: '报考登记单号', name: 'examRecordNo', width: 80 }, 
			{ label: '备注', name: 'remark', width: 80 }, 
			{ label: '产品线ID', name: 'productId', width: 160 ,hidden : true}, 
			{ label: '产品线', name: 'productName', width: 160 }, 
			{ label: '审核状态', name: 'audited', width: 80, formatter: function(value, options, row){
  				return value === 0 ? '<span class="label label-danger">未通过</span>' : '<span class="label label-success">已通过</span>';
   		   	}},	
			{ label: '审核状态value', name: 'audited', width: 80, hidden: true },					
			{ label: '创建时间', name: 'createTime', width: 80 }, 						
			{ label: '创建人', name: 'createPerson', width: 80 }, 
			{ label: '修改时间', name: 'modifyTime', width: 80 }, 
			{ label: '修改人', name: 'modifyPerson', width: 80 }, 
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
        url: '../coursescorerecorddetail/list',
        datatype: "local",
        colModel: [		
			{ label: 'scoreRecordDetailId', name: 'scoreRecordDetailId', key: true, hidden: true },
			{ label: 'scoreRecordId', name: 'scoreRecordId', hidden: true },
			{ label: '学员PK', name: 'userId', width: 80, hidden : true }, 	
			{ label: '学员昵称', name: 'userName', width: 150 }, 
			{ label: '课程PK', name: 'courseId', width: 80, hidden : true }, 	
			{ label: '课程名字', name: 'courseName', width: 200 }, 
			{ label: '分数', name: 'score', width: 150 }, 						
			{ label: '状态', name: 'passed', width: 150, formatter: function(value, options, row){
  				return value === 0 ? '<span class="label label-danger">未通过</span>' : '<span class="label label-success">已通过</span>';
   		   	}}	,
   		   	{ label: '状态value', name: 'passed', width: 150, hidden: true },
							
        ],
		viewrecords: true,
        height: 'auto',
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
        //pager: "#detailGridPager",
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
    // ------------------------------------------------------------报考登记主表------------------------------------
    $("#examRecordGrid").jqGrid({
        url: '../course/coursescorerecord/examrecordlist',
        datatype: "local",
        colModel: [		
				{ label: '报考登记Id', name: 'examRecordId', key: true, hidden : true },
				{ label: '单号', name: 'examRecordNo', width: 100 }, 						
				{ label: '备注', name: 'remark', width: 120 }, 	
				{ label: '创建人', name: 'createPerson', width: 80 },
				{ label: '创建时间', name: 'createTime', width: 160 },
				{ label: '修改人', name: 'modifyPerson', width: 80 },
				{ label: '修改时间', name: 'modifyTime', width: 160 }, 	
				{ label: '产品线ID', name: 'productId', width: 160 ,hidden : true}, 
				{ label: '产品线', name: 'productName', width: 160 }, 
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
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
        pager: "#examRecordGridPager",
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
        	$("#examRecordGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		courseScoreRecord: { 
			productId:"",
			productName:""
		},
		q:{//查询条件
			scoreRecordNo: null
		},
		detail:{
			title:null,
			obj:{},
		},
		examRecordNo : "",
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseScoreRecord = { }; 
			$.ajax({
				type: "POST",
			    url: "../course/coursescorerecord/getNo",
			    success: function(r){
			    	if(r.code === 0){
						vm.courseScoreRecord = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
			jQuery("#detailGrid").jqGrid("clearGridData");
			$("#examRecordShow").removeAttr('disabled');
			$("#addScore").attr("style", "display: block");
			$("#updateScore").attr("style", "display: none");
			$("#detailBtn").attr("style", "display: block");
            $("#examRecordNo").removeAttr("readonly");
            $("#remark").removeAttr("readonly");
            $("#okBtn").show();
            $("#acceptBtn").hide();
            $("#reacceptBtn").hide();
		},
		examRecordShow: function (event) {
				vm.selExamRecord(function(selectDetail , rowData){
					//清空子表
					jQuery("#detailGrid").jqGrid("clearGridData");
					//获取分数登记id
					vm.courseScoreRecord.examRecordId = rowData.examRecordId;
					vm.courseScoreRecord.examRecordNo = rowData.examRecordNo;
					vm.courseScoreRecord.productName = rowData.productName;
					vm.courseScoreRecord.productId = rowData.productId;
					vm.examRecordNo = rowData.examRecordNo;
					//查询分数登记子表
					$.ajax({
						type: "POST",
					    url: "../course/coursescorerecord/examRecordDetail/" + vm.courseScoreRecord.examRecordId,
					    success: function(r){
					    	if(r.code === 0){
					    		var detailList = r.data;
				                //报考登记子表
				                if(null != detailList && detailList.length > 0){
				                	$.each(detailList , function(i , v){
				                		//添加行
				                		v.score = 0;
				                		v.passed = 0;
										$("#detailGrid").addRowData(v.examRecordDetailId, v, "last");
				                	});
				                }
							}else{
								alert(r.msg);
							}
						}
					});
				return true;
			});
		},
		selExamRecord : function(callblack){//班级弹窗
			//显示浮层
			vm.showExamRecordLay(callblack);
			//加载数据
			vm.reloadExamRecordJqGrid();
		},
		showExamRecordLay : function(callblack){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '报考登记',
				area: ['850px', '600px'],
				shadeClose: false,
				content: jQuery("#examRecordlayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					//获取选中行ID
					var selectDetail = getJqGridSelectedRow("examRecordGrid");
					if(selectDetail == null){
						return ;
					}
					//行数据
					var rowData = $("#examRecordGrid").jqGrid('getRowData',selectDetail);
					if($.isFunction(callblack)){
						if(callblack(selectDetail , rowData)){
							//关闭浮层
							layer.close(index);
						}
					}
	            }
			});
		},
		reloadExamRecordJqGrid : function(){//刷新班级列表
			$("#examRecordGrid").jqGrid('setGridParam',{
                page : 1,
                datatype : 'json'
            }).trigger("reloadGrid")
		},
		update: function (event) {
			var scoreRecordId = getSelectedRow();
			if(scoreRecordId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            jQuery("#detailGrid").jqGrid("clearGridData");
            $("#examRecordShow").attr('disabled',"true");
            $("#addScore").attr("style", "display: none");
			$("#updateScore").attr("style", "display: block");
            $("#detailBtn").attr("style", "display: block");
            $("#examRecordNo").removeAttr("readonly");
            $("#remark").removeAttr("readonly");
            $("#okBtn").show();
            $("#acceptBtn").hide();
            $("#reacceptBtn").hide();
            vm.getInfo(scoreRecordId)
		},
		audit: function (event) {
			var scoreRecordId = getSelectedRow();
			var rowData = $("#jqGrid").jqGrid('getRowData',scoreRecordId);
			if(scoreRecordId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "审核";
            jQuery("#detailGrid").jqGrid("clearGridData");
            vm.getInfo(scoreRecordId);
            $("#detailBtn").attr("style", "display: none");
            $("#examRecordNo").attr("readonly", "readonly");
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
		accept: function (event) {
			confirm('确定要通过此登记吗？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/coursescorerecord/accept",
				    data: JSON.stringify(vm.courseScoreRecord),
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
				    url: "../course/coursescorerecord/reaccept",
				    data: JSON.stringify(vm.courseScoreRecord),
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
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../course/coursescorerecord/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../course/coursescorerecord/update";
		    }else
		    {
		       url = "";
		    }
		    if(null == vm.courseScoreRecord.examRecordId || vm.courseScoreRecord.examRecordId == "")
	    	{
		    	alert("请选择报考登记!");
		    	return;
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
			vm.courseScoreRecord.detailList = details;
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseScoreRecord),
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
			var scoreRecordIds = getSelectedRows();
			if(scoreRecordIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/coursescorerecord/delete",
				    data: JSON.stringify(scoreRecordIds),
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
		getInfo: function(scoreRecordId){
            $.ajax({
				type: "POST",
			    url: "../course/coursescorerecord/info/" + scoreRecordId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseScoreRecord = r.scoreRecord;
						 vm.examRecordNo = r.scoreRecord.examRecordNo;
						 var detailList = r.scoreRecordDetail;
			                //报考登记子表
			                if(null != detailList && detailList.length > 0){
			                	$.each(detailList , function(i , v){
			                		//添加行
									$("#detailGrid").addRowData(v.scoreRecordDetailId, v, "last");
			                	});
			                }
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"scoreRecordNo" : vm.q.scoreRecordNo},
                page:page
            }).trigger("reloadGrid");
		},
		scoreAdd: function(){
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("detailGrid");
			if(selectDetail == null){
				return ;
			}
			
			vm.detail.obj = {};
			//弹框标题
			vm.detail.title = "录入分数";
			
			vm.scoreShow();
		},
		scoreUpdate: function(){
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("detailGrid");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#detailGrid").jqGrid('getRowData',selectDetail);
			//
			vm.detail.obj = rowData;
			//弹框标题
			vm.detail.title = "录入修改";
			
			vm.scoreShow();
		},
		uploadExcelMethod : function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '导入Excel',
				area: ['300px', '200px'],
				shadeClose: false,
				content: jQuery("#uploadScoreExcel"),
				btn: ['确定','取消'],
				btn1: function (index) {
					$.ajaxFileUpload({
						url:'../course/coursescorerecord/getExcelCourseScoreRecordData',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						success:function(data){
							if(data.code == 0) {
								alert("文件上传成功", function(index){
									$("#jqGrid").trigger("reloadGrid");
								});
								layer.close(index);
							}else if(data.code == 1) { 
								alert(data.msg);
								layer.close(index);
							}
						}
					});
	            }
			});
		},
		scoreShow: function (){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.detail.title,
				area: ['600px', '180px'],
				shadeClose: false,
				content: jQuery("#scorelayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
					if(vm.detail.obj.score >= 0 && vm.detail.obj.score <=100){
						vm.detail.obj.passed = vm.detail.obj.score < 60 ? 0 : 1;
						$("#detailGrid").setRowData(getJqGridSelectedRow("detailGrid"),vm.detail.obj);
						layer.close(index);
					}else{
						alert("请输入0到100的数字");
					}
	            }
			});
		}
	}
});