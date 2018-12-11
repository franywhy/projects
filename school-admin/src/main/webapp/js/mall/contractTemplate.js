$(function() {
	$("#jqGrid").jqGrid({//主表数据初始化
		url : '../contractTemplate/list',
		datatype : "json",
		colModel : [
			{label : 'id', name : 'id', key : true , width:38 },
  
			{label : '模板Id', name : 'templateId', width : 80 },
			{label : '模板名称', name : 'templateName', width : 80 },
			{label : '公司名称', name : 'companyName', width : 80 },
			{label : '公司Id', name : 'companyId', width : 80 },
	 
		],
		viewrecords : true,
		height : 385,
		rowNum : 10,
		rowList : [ 10, 30, 50 ],
		rownumbers : true,
		rownumWidth : 35,
		autowidth : true,
		multiselect : true,
		pager : "#jqGridPager",
		jsonReader : {
			root : "page.list",
			page : "page.currPage",
			total : "page.totalPage",
			records : "page.totalCount"
		},
		prmNames : {
			page : "page",
			rows : "limit",
			order : "order"
		},
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "hidden"});
		}
	});  
});

var vm = new Vue(
{
	el : '#rrapp',
	data : {
				showList : true,
				idBtn : 'true',
				title : null,
				q : { //商品查询条件
					id : "",
					name : "",
					suitableUser : "",
		            ncId:"",
					status : "",
					levelId : "",
					levelName : "",
					professionId : "",
					professionName : "",
					classTypeId : "",
					classTypeName : "",
					productName : "",//产品线Name
					productId:""
				},
				contractTemplateInfo : {
					id:"",
					templateId:"",
					templateName: "",
					companyName:"",
					companyId: "",
				}, 
		},
	methods : {
		query : function() {
			vm.reload(null , 1);
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.contractTemplateInfo = {
					id:"",
					templateId:"",
					templateName: "",
					companyName:"",
					companyId: "",
				};
			 
			$("#commitButton").attr("style", "display: block");
		},
		update : function(event) { 
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改"; 
            vm.idBtn = false;
			$.ajax({
				type : "GET",
				url : "../contractTemplate/info/"+id,
				success : function(data) {
					if (data.code == 0) {
						 vm.contractTemplateInfo.id = data.data.id;
						 vm.contractTemplateInfo.templateId = data.data.templateId;
						 vm.contractTemplateInfo.templateName = data.data.templateName;
						 vm.contractTemplateInfo.companyName = data.data.companyName;
						 vm.contractTemplateInfo.companyId = data.data.companyId;
						 
					} else {
						alert(data.msg);
					}
				}
			});
			
			$("#commitButton").attr("style", "display: block");
		},	
		del : function(event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}  
	         
			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "GET",
					url : "../contractTemplate/delete/"+id,
					/*data : JSON.stringify(ids),*/
					success : function(r) {
						if (r.code == 0) {
							alert('操作成功', function(index) {
								var rows = $("#jqGrid").jqGrid('getRowData');
								var page = $("#jqGrid").getGridParam('page');
								console.log(rows,page)
								if(rows.length == 0 && page > 1){
									page--;
								} 
								
								$("#jqGrid").jqGrid('setGridParam', {
							        page:page,
							    }).trigger("reloadGrid");
							});
						} else {
							alert(r.msg);
						}
					}
				});
			});
		},
		saveOrUpdate : function(event) {
		    if(vm.title == "新增")
		    {
		       url = "../contractTemplate/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../contractTemplate/update";
		    }else
		    {
		       url = "";
		    } 
		    
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.contractTemplateInfo),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							$("#jqGrid").trigger("reloadGrid");
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		
		},
		reload : function(event , p) {
			vm.showList = true;
			vm.idBtn = true;
		},
	},
	created : function() {
//				this.getLevelSelections();
//				this.getProfessionSelections();
//				this.getClassTypeSelections();
	}
});

