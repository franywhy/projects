$(function () {
    $("#jqGrid").jqGrid({
        url: '../sysproduct/list',
        datatype: "json",
        colModel: [		
			{ label: 'productId', name: 'productId', key: true , width: 80},
			{ label: '名称', name: 'productName', width: 80 }, 						
			{ label: '备注', name: 'remark', width: 80 }, 						
//			{ label: 'ts', name: 'ts', width: 80 }, 						
			{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}}, 
			{ label: '点播userId', name: 'polyvuserid', width: 80 }, 						
			{ label: '点播secretKey', name: 'polyvsecretkey', width: 80 }						
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
			product_name: null
		},
		showList: true,
		title: null,
		sysProduct: {
			productName:"",
			remark:"",
			status:1
		}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.sysProduct = {
				productName:"",
				remark:"",
				status:1
			};
		},
		update: function (event) {
			var productId = getSelectedRow();
			if(productId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(productId)
		},
		saveOrUpdate: function (event) {
			if($.isNull(vm.sysProduct.productName)){
				alert("请输入名称！！！");
				return;
			}
			if(vm.title == "新增")
		    {
		       url = "../sysproduct/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../sysproduct/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.sysProduct),
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
			var productIds = getSelectedRows();
			if(productIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sysproduct/delete",
				    data: JSON.stringify(productIds),
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
		getInfo: function(productId){
            $.ajax({
				type: "POST",
			    url: "/sysproduct/info/" + productId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.sysProduct = r.data;
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
		},
		resume:function(event){
		   var productIds = getSelectedRows();
			if(productIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sysproduct/resume",
				    data: JSON.stringify(productIds),
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
		    var productIds = getSelectedRows();
			if(productIds == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sysproduct/pause",
				    data: JSON.stringify(productIds),
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
		
	}
});