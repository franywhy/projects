
$(function () {
	$(".datetimepicker").datetimepicker({
 		format: 'yyyy-mm-dd ',
 		zIndex : 999999999,
 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
});
$(".courseExamTime").datetimepicker({
	 	format: 'yyyy-mm-dd',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
//        todayHighlight: 1,//如果为true, 高亮当前日期。
//        startView: 2,//日期时间选择器打开之后首先显示的视图。
//        forceParse: 0,//当选择器关闭的时候，是否强制解析输入框中的值。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
//        showMeridian: 1
 });
    $("#jqGrid").jqGrid({
        url: '../courseexamtime/list',
        datatype: "json",
        colModel: [		
        	
											{ label: 'id', name: 'id', width: 20,key: true ,hidden:true},
														{ label: '考试时间', name: 'examTime', width: 40, formatter: function(v, options, row){
															return v.substring(0,10);
														}},
														{ label: '倒计时', name: 'countDays', width: 80, formatter: function(v, options, row){
															var text="";
															if(v>0) text='<span  class="label label-success" style="font-weight:bold;font-size:40px;">'+'距离考试时间还剩'+v+'(天)'+'</span>';
															else text='<span class="label label-danger">'+0+'(天)'+'</span>';
															return text;
														}},
														{ label: '备注', name: 'remark', width: 40 },
														{ label: '创建用户', name: 'creationName', width: 80 ,hidden:true}, 						
														{ label: '创建时间', name: 'creationTime', width: 80 ,hidden:true}, 						
														{ label: '最近修改用户', name: 'modifiedName', width: 80 ,hidden:true}, 						
														{ label: '最近修改日期', name: 'modifiedTime', width: 80 ,hidden:true}, 						
														{ label: '平台PK', name: 'schoolId', width: 80 ,hidden:true}						
							
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		courseExamTime: {
			examTime:""
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseExamTime = {};
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
			vm.courseExamTime.examTime = $("#courseExamTime").val();
		    if(vm.title == "新增")
		    {
		       url = "../courseexamtime/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../courseexamtime/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseExamTime),
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
				    url: "../courseexamtime/delete",
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
			    url: "/courseexamtime/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseExamTime = r.data;
						 $("#courseExamTime").val(vm.courseExamTime.examTime.substring(0,10));
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
                page:page
            }).trigger("reloadGrid");
		}
		
	}
});