$(function () {
/*	$(".form_datetime").datetimepicker({
 		format: 'hh:mm',
 		zIndex : 999999999,
 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
});	*/
	$("#startTimeShow").datetimepicker({
		    zIndex : 999999999,
		    format: 'hh:ii',
	        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
	        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
	        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
//	        todayHighlight: 1,//如果为true, 高亮当前日期。
	        startView: 1,//日期时间选择器打开之后首先显示的视图。
	        forceParse: true,//当选择器关闭的时候，是否强制解析输入框中的值。
//	        minView: 2,//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
//	        showMeridian: 1
//	        minView: 4
	        minuteStep:5
	 });
	$("#endTimeShow").datetimepicker({
		zIndex : 999999999,
		format: 'hh:ii',
	    weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
	    todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
	    autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
//	    todayHighlight: 1,//如果为true, 高亮当前日期。
	    startView: 1,//日期时间选择器打开之后首先显示的视图。
	    forceParse: true,//当选择器关闭的时候，是否强制解析输入框中的值。
//	    minView: 2,//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
//	    showMeridian: 1
//	    minView: 4
	    minuteStep:5
	});
	
    $("#jqGrid").jqGrid({
        url: '../mall/timetable/list',
        datatype: "json",
        colModel: [			
			{ label: 'number', name: 'number', hidden : true, width: 50, key: true },
			{ label: '名称', name: 'name', width: 80 },
			{ label: '产品线', name: 'productName', width: 80 },
			{ label: '创建人', name: 'creationName', width: 80 },
			{ label: '创建时间', name: 'createTime', width: 80 }, 			
			{ label: '修改人', name: 'modifiedName', width: 80 }, 	
			{ label: '修改时间', name: 'updateTime', width: 80 }, 
			{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '备注', name: 'comments', width: 80 } 			
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
    
    /*--------------------------------------------初始化子表------------------------------------------*/
    $("#detailGrid").jqGrid({
    	datatype: "json",
    	colModel: [		
			{ label: 'id', name: 'id', hidden : true, key: true },
			{ label: '编号，对应timetable主键', hidden : true, name: 'number', width: 80 }, 						
			{ label: '上课时间（星期）', name: 'week', hidden : true,width: 80 }, 
			{ label: '上课时间（星期）', name: 'weekName', width: 80 ,formatter: function(v, options, row){
				var value = row.week;
				var text = '';
				if(value == 0)text= '星期日';
				else if(value == 1)text= '星期一';
				else if(value == 2)text= '星期二';
				else if(value == 3)text= '星期三';
				else if(value == 4)text= '星期四';
				else if(value == 5)text= '星期五';
				else if(value == 6)text= '星期六';
				return text;
			}},
			{ label: '上课时段', name: 'timeBucket', width: 80, hidden : true}, 
			{ label: '上课时段', name: 'timeBucketName', width: 80 ,formatter: function(v, options, row){
				var value = row.timeBucket;
				var text = '';
				if(value == 0)text= '上午';
				else if(value == 1)text= '下午';
				else if(value == 2)text= '晚上';
				return text;
			}},
			{ label: '开始时间', name: 'startTime', width: 80 }, 						
			{ label: '结束时间', name: 'endTime', width: 80 }, 						
			{ label: '备注', name: 'comments', width: 80 }						
        ],
        height : 'auto',
        rownumbers : true, //如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。此列名为'rn'.
        viewrecords : true,//定义是否要显示总记录数
        multiselect: true,//定义是否可以多选
        autowidth:false,//自动宽度
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			name: null,
			productName: null
		},
		showList: true,
		title: null,
		timetable: {
			productId:null,
			productName:null
		},
		detail: {
			title:null,
			obj:{
				startTime:"",
				endTime:""
			}
		},
		options: [  
		      { text: '星期日', value: '0' },  
		      { text: '星期一', value: '1' },  
		      { text: '星期二', value: '2' },
		      { text: '星期三', value: '3' },
		      { text: '星期四', value: '4' },
		      { text: '星期五', value: '5' },
		      { text: '星期六', value: '6' }
		    ],
		options1: [  
			  { text: '上午', value: '0' },  
			  { text: '下午', value: '1' },  
			  { text: '晚上', value: '2' }  
			]
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		timeShow:function(){},
		defaultTime:function(rowItem){
			var value=rowItem.obj.timeBucket;
			if(0==value){
				$("#startTimeShow").val("09:00");
				$("#endTimeShow").val("12:00");
			}
			else if(1==value){
				$("#startTimeShow").val("14:00");
				$("#endTimeShow").val("17:00");
			}
			else if(2==value){
				$("#startTimeShow").val("19:30");
				$("#endTimeShow").val("21:30");
			}
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.timetable = {
					status:1,
					productId:null,
					productName:null
			};
			jQuery("#detailGrid").jqGrid("clearGridData");//新增的时候清空Grid子表
		},
		update: function (event) {
			var number = getSelectedRow();
			if(number == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            jQuery("#detailGrid").jqGrid("clearGridData");
            vm.getInfo(number)
		},
		/*querySub: function (event) {
			var number = getSelectedRow();
			if(number == null){
				return ;
			}
			vm.showList = false;
            vm.title = "查询子表信息";
            jQuery("#detailGrid").jqGrid("clearGridData");
            vm.getInfo(number)
		},*/
		saveOrUpdate: function (event) {
			/*vm.detail.obj.startTime = $("#startTimeShow").val();
			vm.detail.obj.endTime = $("#endTimeShow").val();*/
			if($.isNull(vm.timetable.name)){
		    	alert("请输入名称！！！");
		    	return;
		    }
			if($.isNull(vm.timetable.productId)){
				alert("请选择产品线！！！");
				return;
			}
			if(!$.isNull(vm.timetable.comments) && vm.timetable.comments.length > 50){
				alert("备注信息不能超过50个字！！！");
		    	return;
			}
			if(vm.title == "新增")
		    {
		       url = "../mall/timetable/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/timetable/update";
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
			vm.timetable.detailList = details;
		    
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.timetable),
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
			var numbers = getSelectedRows();
			if(numbers == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/timetable/delete",
				    data: JSON.stringify(numbers),
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
		getInfo: function(number){
			$.get("../mall/timetable/info/"+number, function(r){
                vm.timetable = r.timetable;
                var detailList = r.detailList;
                if(null != detailList && detailList.length > 0){
                	$.each(detailList , function(i , v){
                		//添加行
						$("#detailGrid").addRowData(v.liveId, v, "last");
                	});
                }
            });
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var numbers = getSelectedRows();
			if(numbers == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/timetable/resume",
				    data: JSON.stringify(numbers),
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
		    var numbers = getSelectedRows();
			if(numbers == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/timetable/pause",
				    data: JSON.stringify(numbers),
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
		selectProduct : function(){
			productLay.show(function(index,opt){
				vm.timetable.productId = opt.productId;
				vm.timetable.productName = opt.productName;
			});
		},
		
		iadd: function(){
			
			//弹框标题
			vm.detail.title = "新增";
			//初始化弹窗数据
			vm.detail.obj = {};
			vm.ishow();
//			加载时间插件
//			vm.timeShow();

		},
		iupdate:function(){
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
			vm.detail.obj = rowData;
			
			//弹框标题
			vm.detail.title = "修改";
			
			$("#startTimeShow").val(vm.detail.obj.startTime);
			$("#endTimeShow").val(vm.detail.obj.endTime);
			
			/*$('#startTimeShow').empty();
			 $("#endTimeShow").empty();*/
			//弹框
			vm.ishow();
//			加载时间插件
//			vm.timeShow();
			

		},
		ishow : function(){//弹出新增或者修改窗口
			
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.detail.title,
				area: ['550px', '550px'],
				shadeClose: false,
				content: jQuery("#liveLayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
				    if(vm.detail.obj.comments != null && vm.detail.obj.comments.length > 50 ){
                        alert("[备注信息]不能超过50个字！！！");
                        return;
                    }
					if("新增" == vm.detail.title){
						//添加行
						vm.detail.obj.startTime = $("#startTimeShow").val();
						vm.detail.obj.endTime = $("#endTimeShow").val();
						//行ID
						var rowId = new Date().getTime();
						$("#detailGrid").addRowData(rowId, vm.detail.obj, "last");  
					}else if("修改" == vm.detail.title){
//						var rowId = new Date().getTime();
						//修改
						vm.detail.obj.startTime = $("#startTimeShow").val();
						vm.detail.obj.endTime = $("#endTimeShow").val();
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
	}
});