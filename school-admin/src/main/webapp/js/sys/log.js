$(function () {
    $("#jqGrid").jqGrid({
        url: '../syslog/list',
        datatype: "json",
        colModel: [		
			{ label: 'id', name: 'id', key: true, width: 40 },
			{ label: '用户名', name: 'username', width: 50 }, 						
			{ label: '用户操作', name: 'operation', width: 80 }, 						
			{ label: '请求方法', name: 'method', width: 200 }, 						
			{ label: '请求参数', name: 'params', width: 80 }, 						
			{ label: '执行时长(毫秒)', name: 'executionTime', width: 80 }, 						
			{ label: 'IP地址', name: 'ip', width: 80 }, 						
			{ label: '创建时间', name: 'createTime', width: 80 }						
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
		q:{
			username:null,
			operation:null
		},
		showList: true,
		title: null,
		sysLog: {}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysLog = {};
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
		    if(vm.title == "新增")
		    {
		       url = "../syslog/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../syslog/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.sysLog),
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
				    url: "../syslog/delete",
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
			    url: "/syslog/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.sysLog = r.data;
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
				postData : vm.q,
				page:page
            }).trigger("reloadGrid");
		}
		
	}
});