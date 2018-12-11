$(function () {
	$(".datetimepicker_detailTime").datetimepicker({
	 		format: 'yyyy-mm-dd hh:ii:00',
	 		zIndex : 999999999,
	 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
	 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
	});
	$("#jqGrid").jqGrid({
        url: '../pushclassplanremind/list',
        datatype: "json",
        colModel: [		
				{ label: 'id', name: 'id', key: true, width: 20  },
				{ label: '排课计划ID', name: 'courseClassplanId', hidden:true, width: 80 },
				{ label: '排课计划名称', name: 'courseClassplanName', width: 80 },
				{ label: '通知模板ID', name: 'pushTimeTemplateId', hidden:true, width: 80 },
				{ label: '通知模板名称', name: 'pushTimeTemplateName', width: 80 },
				{ label: '课程名称', name: 'courseName', width: 80 },
				{ label: '开课日期', name: 'startTime', width: 80 },
				{ label: '授课老师', name: 'teacherName', width: 80 },
				{ label: '产品线', name: 'productName', width: 80 },
				/*{ label: '最近更新人', name: 'updaterName', width: 80 }, 						
				{ label: '最近更新时间', name: 'updateTime', width: 80 },*/ 						
        ],
		viewrecords: true,
        height: 385,
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
    
    /***********************推送课次详情列表*******************************/
    $("#jqGrid1").jqGrid({
        url: '../pushclassplandetailremind/list',
        datatype: "local",
        colModel: [		
				{ label: 'detailId', name: 'detailId', key: true, width: 50  },
				{ label: '课次通知提醒id', name: 'pushClassplanRemindId', hidden: true, width: 50  },
				{ label: '排课计划id', name: 'classplanId', hidden: true, width: 50  },
				{ label: '直播课次名称', name: 'classplanLiveName', width: 200 },
				{ label: '开始时间', name: 'startTime', width: 200 },
				{ label: '结束时间', name: 'endTime', width: 200 },
				{ label: '授课老师', name: 'teacherName', width: 100 },
				{ label: '推送模板名称', name: 'templateName', width: 200 },
				{ label: '推送时间', name: 'pushTime', width: 200 },
				{ label: '推送内容', name: 'pushContent', width: 500 },
				{ label: '审核状态', name: 'auditStatus', width: 100 ,formatter: function(v, options, row){
					var text = '';
					if(v == 0)text= '不通过';
					else if(v == 1)text= '通过';
					else if(v == 2)text= '待审核';
					return text;
				}},
				{ label: '最近审核人', name: 'auditorName', width: 100 }, 						
				{ label: '最近审核时间', name: 'auditTime', width: 100 }, 						
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager1",
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
        	$("#jqGrid1").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q: {
			name: null,
			templateName: null
		},
		qd: {
			liveName: null
		},
		showList: true,
		showSaveOrUpdate: false,
		showDetailList: false,
		title: null,
		pushClassplanRemind: {
			id: "",
			pushTimeTemplateId: "",
			pushTimeTemplateName: "",
			courseClassplanId: "",
			courseClassplanName: "",
			type: ""
		},
		pushClassplanDetailRemind: {
			detailId: "",
			pushClassplanRemindId: "",
			pushTime: "",
			pushContent: "",
			auditStatus: "",
			classplanId: ""
			
		}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.showSaveOrUpdate = true;
			vm.showDetailList = false;
			vm.title = "新增";
			vm.pushClassplanRemind = {
					id: "",
					pushTimeTemplateId: "",
					pushTimeTemplateName: "",
					courseClassplanId: "",
					courseClassplanName: ""
			};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
			vm.showSaveOrUpdate = true;
			vm.showDetailList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		//推送课次详情修改
		detailUpdate:function(event){
			
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("jqGrid1");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGrid1").jqGrid('getRowData',selectDetail);
			//赋值
			vm.pushClassplanDetailRemind = rowData;
			if('待审核' != vm.pushClassplanDetailRemind.auditStatus){
				alert("该内容已审核，若要修改，请先进行反审核后再修改");
				return;
			}
			vm.pushClassplanDetailRemind.auditStatus = 2;

			$("#detailPushTime").val(vm.pushClassplanDetailRemind.pushTime);
			
			vm.detailLayShow();
		},
		//推送课次详情编辑弹窗
		detailLayShow : function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: "修改",
				area: ['600px', '440px'],
				shadeClose: false,
				content: jQuery("#detailLayer"),
				btn: ['提交','取消'],
				btn1: function (layerIndex) {
					//非空校验
					//推送时间
					vm.pushClassplanDetailRemind.pushTime = $("#detailPushTime").val();//推送时间
					if($.isNull(vm.pushClassplanDetailRemind.pushTime)){
						alert("推送时间不能为空");
						return;
					}
					//推送内容
					if($.isNull(vm.pushClassplanDetailRemind.pushContent)){
						alert("推送内容不能为空");
						return;
					}
					if(vm.pushClassplanDetailRemind.pushContent.length > 200){
						alert("推送内容不能超过200个字符");
						return;
					}
					$.ajax({
						type: "POST",
					    url: "../pushclassplandetailremind/update",
					    data: JSON.stringify(vm.pushClassplanDetailRemind),
					    success: function(r){
					    	if(r.code === 0){
								alert('操作成功', function(index){
									layer.close(layerIndex);
									$("#jqGrid1").trigger("reloadGrid");
								});
							}else{
								alert(r.msg);
							}
						}
					});
	            }
			});
			
		},
		//审核
		audit: function(event){
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("jqGrid1");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGrid1").jqGrid('getRowData',selectDetail);
			//赋值
			vm.pushClassplanDetailRemind = rowData;
			$("#detailPushTime").val(vm.pushClassplanDetailRemind.pushTime);
			
			vm.auditLayShow(selectDetail);
		},
		//审核弹窗
		auditLayShow : function(id){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: "审核",
				area: ['600px', '440px'],
				shadeClose: false,
				content: jQuery("#detailLayer"),
				btn: ['通过','不通过','取消'],
				btn1: function (layerIndex) {
					$.ajax({
						type: "POST",
					    url: "../pushclassplandetailremind/auditPass",
					    data: JSON.stringify(id),
					    success: function(r){
					    	if(r.code === 0){
					    		alert('操作成功', function(index){
									$("#jqGrid1").trigger("reloadGrid");
									layer.close(layerIndex);
								});
							}else{
								alert(r.msg);
							}
						}
					});
	            },
	            btn2: function (layerIndex) {
					$.ajax({
						type: "POST",
					    url: "../pushclassplandetailremind/auditFail",
					    data: JSON.stringify(id),
					    success: function(r){
					    	if(r.code === 0){
					    		alert('操作成功', function(index){
									$("#jqGrid1").trigger("reloadGrid");
									layer.close(layerIndex);
								});
							}else{
								alert(r.msg);
							}
						}
					});
	            }
			});
		},
		//查看推送课次详情
		showDetail: function (event){
			//获取选中行ID
			var id = getJqGridSelectedRow("jqGrid");
			if(id == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGrid").jqGrid('getRowData',id);
			
			vm.pushClassplanDetailRemind.classplanId = rowData.courseClassplanId;
			vm.pushClassplanDetailRemind.pushClassplanRemindId = id;
			
			vm.showList = false;
			vm.showSaveOrUpdate = false;
			vm.showDetailList = true;
            vm.title = "查看推送课次详情";
            
            vm.detailRload(id, 1);
		},
		//返回推送课次列表
		goList : function(){
			vm.reload();
		},
		//推送课次详情加载
		detailRload : function(id , p){
			vm.pushClassplanDetailRemind.pushClassplanRemindId = id;
			vm.qd.liveName = null;
			vm.showList = false;
			vm.showSaveOrUpdate = false;
			vm.showDetailList = true;
			//清空表格数据
			jQuery("#jqGrid1").jqGrid("clearGridData");
			var page = p||$("#jqGrid1").jqGrid('getGridParam','page');
			$("#jqGrid1").jqGrid('setGridParam',{
				datatype: "json",
				postData : {
					"pushClassplanRemindId" :vm.pushClassplanDetailRemind.pushClassplanRemindId,
					"liveName" : null
				},
                page:page
            }).trigger("reloadGrid");
		},
		//条件查询推送课次详情
		queryDetail : function(){
			//清空表格数据
			jQuery("#jqGrid1").jqGrid("clearGridData");
			var page = $("#jqGrid1").jqGrid('getGridParam','page');
			$("#jqGrid1").jqGrid('setGridParam',{
				datatype: "json",
				postData : {
					"pushClassplanRemindId" :vm.pushClassplanDetailRemind.pushClassplanRemindId,
					"liveName" : vm.qd.liveName
				},
                page:page
            }).trigger("reloadGrid");
		},
		//刷新
		refresh : function(event){
			var pushClassplanRemindId = vm.pushClassplanDetailRemind.pushClassplanRemindId;
			var classplanId = vm.pushClassplanDetailRemind.classplanId;
			confirm('确定要刷新？', function(){
				$.ajax({
					type: "GET",
				    url: "../pushclassplandetailremind/refresh?pushClassplanRemindId="+pushClassplanRemindId+"&classplanId="+classplanId,
				    //data: JSON.stringify(id),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid1").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
			
		},
		//查看模板
		showTemplate: function (event){
			
            pushTemplateLay.show(function(index,opt){
				/*vm.pushClassplanRemind.pushTimeTemplateId = opt.id;
				vm.pushClassplanRemind.pushTimeTemplateName = opt.name;*/
			});
            
            
		},
		//反审核
		theAudit: function (event) {
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("jqGrid1");
			if(selectDetail == null){
				return ;
			}
			var id = selectDetail;
			
			confirm('确定要反审核选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../pushclassplandetailremind/theAudit",
				    data: JSON.stringify(id),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid1").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		//删除推送课次详情
		delDetail: function (event) {
			//获取选中行ID
			var ids = getJqGridSelectedRows("jqGrid1");
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../pushclassplandetailremind/delDetail",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid1").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdate: function (event) {
			//非空校验
			if($.isNull(vm.pushClassplanRemind.courseClassplanId)){
				alert("请选择一个排课计划");
				return;
			}
			if($.isNull(vm.pushClassplanRemind.pushTimeTemplateId)){
				alert("请选择一个推送模板");
				return;
			}
		    if(vm.title == "新增")
		    {
		       url = "../pushclassplanremind/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../pushclassplanremind/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.pushClassplanRemind),
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
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../pushclassplanremind/delete",
				    data: JSON.stringify(ids),
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
		getInfo: function(id){
            $.ajax({
				type: "POST",
			    url: "/pushclassplanremind/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.pushClassplanRemind = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (e , p) {
			vm.showList = true;
			vm.showSaveOrUpdate = false;
			vm.showDetailList = false;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				page:page
            }).trigger("reloadGrid");
		},
		
		selectClassplan : function(){//排课浮层
			classplanLay.show(function(index,opt){
				vm.pushClassplanRemind.courseClassplanId = opt.classplanId;
				vm.pushClassplanRemind.courseClassplanName = opt.classplanName;
			});
		},
		
		selectPushTemplate : function(){//推送模板浮层
			pushTemplateLay.show(function(index,opt){
				vm.pushClassplanRemind.pushTimeTemplateId = opt.id;
				vm.pushClassplanRemind.pushTimeTemplateName = opt.name;
				vm.pushClassplanRemind.type = opt.type;
			});
		},
		
	}
});