$(function () {
    $("#jqGrid").jqGrid({
        url: '../coursematerialtype/list',
        datatype: "json",
        colModel: [		
        	
											{ label: 'materialTypeId', name: 'materialTypeId', key: true },
														{ label: '资料库PK', name: 'materialId', width: 80 }, 						
														{ label: '名称', name: 'materialTypeName', width: 80 }, 						
														{ label: '创建用户', name: 'createPerson', width: 80 }, 						
														{ label: '创建时间', name: 'creationTime', width: 80 }, 						
														{ label: '最近修改用户', name: 'modifyPerson', width: 80 }, 						
														{ label: '最近修改日期', name: 'modifiedTime', width: 80 }, 						
														{ label: '平台PK', name: 'schoolId', width: 80 }, 						
														{ label: '排序', name: 'orderNum', width: 80 }						
							
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		courseMaterialType: {}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseMaterialType = {};
		},
		update: function (event) {
			var materialTypeId = getSelectedRow();
			if(materialTypeId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(materialTypeId)
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../coursematerialtype/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../coursematerialtype/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseMaterialType),
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
			var materialTypeIds = getSelectedRows();
			if(materialTypeIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../coursematerialtype/delete",
				    data: JSON.stringify(materialTypeIds),
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
		getInfo: function(materialTypeId){
            $.ajax({
				type: "POST",
			    url: "/coursematerialtype/info/" + materialTypeId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseMaterialType = r.data;
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