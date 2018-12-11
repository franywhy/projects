$(function () {
    $("#jqGrid").jqGrid({
        url: '../mallbussinessopp/list',
        datatype: "json",
        colModel: [		
			{ label: 'id', name: 'id', width: 80, hidden:true, key: true },
			{ label: '用户ID', name: 'userId', width: 80 }, 						
			{ label: '用户昵称', name: 'nickName', width: 80 }, 						
			{ label: '手机', name: 'userMobile', width: 80 }, 						
			{ label: '创建时间', name: 'createTime', width: 80 }, 						
			{ label: '平台ID', name: 'schoolId', width: 80 }						
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
			nickName:"",
			userMobile:""
		},
		showList: true,
		title: null,
		mallBussinessOpp: {}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.mallBussinessOpp = {};
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
		       url = "../mallbussinessopp/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mallbussinessopp/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallBussinessOpp),
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
				    url: "../mallbussinessopp/delete",
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
			    url: "/mallbussinessopp/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.mallBussinessOpp = r.data;
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
                postData: vm.q,
				page:page
            }).trigger("reloadGrid");
		},
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q.nickName = "";
			vm.q.userMobile = "";
		},
	}
});