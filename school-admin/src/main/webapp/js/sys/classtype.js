$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/classtype/list',
        datatype: "json",
        colModel: [			
			{ label: '班型ID', name: 'classtypeid', width: 50,key: true },
			{ label: '班型名称', name: 'classtypename', width: 80 }, 			
			{ label: '创建人', name: 'createName', width: 80 }, 			
			{ label: '创建时间', name: 'createTime', width: 80 }, 			
			{ label: '修改用户', name: 'updateName', width: 80 }, 			
			{ label: '更新时间', name: 'updateTime', width: 80 }, 			
			/*{ label: '状态', name: 'status', width: 80 }, */		
			{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '备注', name: 'remake', width: 80 }			
        ],
		viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
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
			classtypename: null
		},
		showList: true,
		title: null,
		sysClasstype: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysClasstype = {};
		},
		update: function (event) {
			var classtypeid = getSelectedRow();
			if(classtypeid == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(classtypeid)
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../sys/classtype/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../sys/classtype/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.sysClasstype),
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
			var classtypeids = getSelectedRows();
			if(classtypeids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/classtype/delete",
				    data: JSON.stringify(classtypeids),
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
		getInfo: function(classtypeid){
			$.get("../sys/classtype/info/"+classtypeid, function(r){
                vm.sysClasstype = r.sysClasstype;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var classtypeids = getSelectedRows();
			if(classtypeids == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/classtype/resume",
				    data: JSON.stringify(classtypeids),
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
		    var classtypeids = getSelectedRows();
			if(classtypeids == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/classtype/pause",
				    data: JSON.stringify(classtypeids),
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
		}
	}
});