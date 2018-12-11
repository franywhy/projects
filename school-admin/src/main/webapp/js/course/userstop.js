$(function () {
    $("#jqGrid").jqGrid({
        url: '../courseuserstop/list',
        datatype: "json",
        colModel: [		
				{ label: 'id', name: 'id', key: true, hidden:true, width: 80 },
				{ label: '申请学员ID', name: 'userId', hidden:true, width: 80 },
				{ label: '申请学员', name: 'userName', width: 80 },
				{ label: '学员规划ID', name: 'userplanId', hidden:true, width: 80 }, 						
				{ label: '手机号', name: 'mobile', width: 80 }, 						
				{ label: '商品', name: 'commodityName', width: 80 }, 						
				{ label: '休学时间', name: 'startTime', width: 80 }, 						
				{ label: '复课时间', name: 'endTime', width: 80 }, 						
				{ label: '休学原因', name: 'stopCause', width: 80 }, 						
				{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
					var text = '';
					if(value == 0)text='审核中';
					else if(value == 1)text='取消';
					else if(value == 2)text='未通过';
					else if(value == 3)text='通过';
					return text;
				}},
				{ label: '备注', name: 'remark', width: 80 }, 						
				/*{ label: '后台创建用户', name: 'createPerson', width: 80 }, 						
				{ label: '创建时间', name: 'creationTime', width: 80 }, */						
				{ label: '最近审核用户', name: 'modifyName', width: 80 }, 						
				{ label: '最近审核日期', name: 'modifiedTime', width: 80 }						
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
		q:{
			userName:"",
			mobile:""
		},
		courseUserstop: {
			commodityName:"",
			userName:"",
			status:""
		}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseUserstop = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			/*vm.showList = false;
            vm.title = "审核";*/
            vm.getInfo(id);
            
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../courseuserstop/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../courseuserstop/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseUserstop),
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
		accept : function(event) {//审核通过
			$.ajax({
				type : "POST",
				url : "../courseuserstop/accept",
				data : JSON.stringify(vm.courseUserstop),
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
				url : "../courseuserstop/reject",
				data : JSON.stringify(vm.courseUserstop),
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
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseuserstop/delete",
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
			    url: "../courseuserstop/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
			    		vm.courseUserstop = r.data;
			    		if(vm.courseUserstop.status == 1){
			            	alert("该学员已取消休学申请！！！");
			            	return ;
			            }
			            else
			            {
			            	vm.showList = false;
			            	vm.title = "审核";
			            }
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page,
                postData:vm.q
            }).trigger("reloadGrid");
		}
		
	}
});