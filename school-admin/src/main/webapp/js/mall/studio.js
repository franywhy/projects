$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/studio/list',
        datatype: "json",
        colModel: [
            { label: '直播室Id', name: 'studioId', width: 50, key: true },       
			{ label: '直播室名称', name: 'studioName', width: 80 }, 			
			{ label: '产品线', name: 'productName', width: 80 }, 			
			{ label: '是否禁用', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '备注', name: 'remake', width: 80 }, 			
			{ label: '创建人', name: 'creationName', width: 80 }, 			
			{ label: '创建时间', name: 'creationTime', width: 80 }, 			
			{ label: '修改人', name: 'modifiedName', width: 80 }, 			
			{ label: '修改时间', name: 'modifiedTime', width: 80 }			
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
			studioName: null,
			productName: null
		},
		showList: true,
		title: null,
		mallStudio: {
			productId:null,
			productName:null
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.mallStudio = {
					status:1,
					productId:null,
					productName:null
			};
		},
		update: function (event) {
			var studioId = getSelectedRow();
			if(studioId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(studioId)
		},
		saveOrUpdate: function (event) {
			if($.isNull(vm.mallStudio.studioName)){
		    	alert("请输入直播室名称！！！");
		    	return;
		    }
			if($.isNull(vm.mallStudio.productId)){
				alert("请选择产品线！！！");
				return;
			}
			if(!$.isNull(vm.mallStudio.remake) && vm.mallStudio.remake.length > 50){
				alert("直播室描述不得超过50个字！！！");
		    	return;
			}
			if(vm.title == "新增")
		    {
		       url = "../mall/studio/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/studio/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallStudio),
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
			var studioIds = getSelectedRows();
			if(studioIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/studio/delete",
				    data: JSON.stringify(studioIds),
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
		getInfo: function(studioId){
			$.get("../mall/studio/info/"+studioId, function(r){
                vm.mallStudio = r.mallStudio;
            });
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
                page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var studioIds = getSelectedRows();
			if(studioIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/studio/resume",
				    data: JSON.stringify(studioIds),
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
		    var studioIds = getSelectedRows();
			if(studioIds == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/studio/pause",
				    data: JSON.stringify(studioIds),
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
				vm.mallStudio.productId = opt.productId;
				vm.mallStudio.productName = opt.productName;
			});
		},
	}
});