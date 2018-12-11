$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/classtype/list',
        datatype: "json",
        colModel: [			
			{ label: '班型Id', name: 'classtypeId', hidden: true, key: true },
			{ label: '班型名称', name: 'classtypeName', width: 80 }, 	
			{ label: '产品线', name: 'productName', width: 80 }, 	
			{ label: '创建人', name: 'creationName', width: 80 }, 			
			{ label: '创建时间', name: 'creationTime', width: 80 }, 			
			{ label: '修改人', name: 'modifiedName', width: 80 }, 			
			{ label: '修改时间', name: 'modifiedTime', width: 80 }, 			
			{ label: '是否禁用', name: 'status', width: 80, formatter: function(value, options, row){
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
			classtypeName: null,
			productName: null
		},
		showList: true,
		title: null,
		mallClassType: {
			status:1,
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
			vm.mallClassType = {
					status:1,
					productId:null,
					productName:null
			};
		},
		update: function (event) {
			var classtypeId = getSelectedRow();
			if(classtypeId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(classtypeId)
		},
		saveOrUpdate: function (event) {
			if($.isNull(vm.mallClassType.classtypeName)){
		    	alert("请输入班型名称！！！");
		    	return;
		    }
			if($.isNull(vm.mallClassType.productId)){
				alert("请选择产品线！！！");
				return;
			}
			if(!$.isNull(vm.mallClassType.remake) && vm.mallClassType.remake.length > 50){
				alert("备注信息不得超过50个字！！！");
		    	return;
			}
			if(vm.title == "新增")
		    {
		       url = "../mall/classtype/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/classtype/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallClassType),
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
			var classtypeId = getSelectedRow();
			if(classtypeId == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "GET",
				    url: "../mall/classtype/delete/"+classtypeId,
				    /*data: JSON.stringify(classtypeIds),*/
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
		getInfo: function(classtypeId){
			$.get("../mall/classtype/info/"+classtypeId, function(r){
				vm.mallClassType = r.mallClassType;
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
		   var classtypeIds = getSelectedRows();
			if(classtypeIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/classtype/resume",
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
				    url: "../mall/classtype/pause",
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
		selectProduct : function(){
			productLay.show(function(index,opt){
				vm.mallClassType.productId = opt.productId;
				vm.mallClassType.productName = opt.productName;
			});
		},
		
	}
});