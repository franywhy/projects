$(function () {
	$(".examtime").datetimepicker({
	 	format: 'yyyy/mm',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        todayHighlight: 1,//如果为true, 高亮当前日期。
        startView: 3,//日期时间选择器打开之后首先显示的视图。
        forceParse: 0,//当选择器关闭的时候，是否强制解析输入框中的值。
        minView: 3,//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
        showMeridian: 1
    });
	$("#jqGrid").jqGrid({
        url: '../mall/mallexamschedule/list',
        datatype: "json",
        colModel: [		
				{ label: 'ID', name: 'id', key: true, width: 30  },
				{ label: '考试时段名称', name: 'scheduleName', width: 80 },					
				{ label: '考试年月', name: 'scheduleDate', width: 80 },					
				{ label: '产品线', name: 'productName', width: 80 },					
//				{ label: '创建人', name: 'creationName', width: 80 }, 
//				{ label: '创建时间', name: 'createTime', width: 80 }, 
//				{ label: '修改人', name: 'modifiedName', width: 80 }, 
//				{ label: '修改时间', name: 'modifyTime', width: 80 }, 
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			scheduleName: null,
			productName:"",//产品线
			productId:null
		},
		showList: true,
		title: null,
		mallExamSchedule: {
			id:null,//主键
			scheduleName:"",//名称
			comments:"",//备注
			status:1,//状态
			productName:"",//产品线
			productId:null
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.mallExamSchedule = {
				id:null,//主键
				scheduleName:"",//名称
				comments:"",//备注
				status:1,//状态
				productName:"",//产品线
				productId:null
			};
			$("#date").val("");//新增时让开课日期置空
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			//做新增或保存时获取开课日期的值
			vm.mallExamSchedule.scheduleDate = $("#date").val();
			if($.isNull(vm.mallExamSchedule.scheduleName)){
		    	alert("请输入考试时段名称！！！");
		    	return;
		    }
			if($.isNull(vm.mallExamSchedule.scheduleDate)){
		    	alert("请选择考试年月！！！");
		    	return;
		    }
			if(!$.isNull(vm.mallExamSchedule.comments) && vm.mallExamSchedule.comments.length > 50){
				alert("备注信息不得超过50个字！！！");
		    	return;
			}
			if(vm.title == "新增")
		    {
		       url = "../mall/mallexamschedule/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/mallexamschedule/update";
		    }else
		    {
		       url = "";
		    }
		    
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallExamSchedule),
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
				    url: "../mall/mallexamschedule/delete",
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
			$.get("../mall/mallexamschedule/info/" + id, function(r){
				vm.mallExamSchedule = r.mallExamSchedule;
				//查询信息时获取并显示开课日期的值
                $("#date").val(vm.mallExamSchedule.scheduleDate);
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
			   var classtypeIds = getSelectedRows();
				if(classtypeIds == null){
					return ;
				}
				
				confirm('确定要启用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../mall/mallexamschedule/resume",
					    data: JSON.stringify(classtypeIds),
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
			    var classtypeIds = getSelectedRows();
				if(classtypeIds == null){
					return ;
				}
				
				confirm('确定要禁用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../mall/mallexamschedule/pause",
					    data: JSON.stringify(classtypeIds),
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
			productLayerShow : function(){//产品线
				productLay.show(function(index, opt) {
					vm.mallExamSchedule.productId = opt.productId;
					vm.mallExamSchedule.productName = opt.productName;
				});
				
			},
		
	}
});