$(function () {
    $("#jqGrid").jqGrid({
        url: '../appcoursebanner/list',
        datatype: "json",
        colModel: [		
				{ label: 'id', name: 'id', hidden:true, key: true },
				{ label: '标题', name: 'title', width: 80 }, 						
				{ label: '图片', name: 'pic', width: 80 , align : "center" ,formatter : function(value, options, row){return '<img height="50px" width="50px"  src="'+value+'"/>';}}, 
				{ label: '链接', name: 'url', width: 80 }, 						
				{ label: '创建人', name: 'createName', width: 80 }, 						
				{ label: '创建时间', name: 'createTime', width: 80 }, 						
				{ label: '修改人', name: 'modifyName', width: 80 }, 						
				{ label: '修改时间', name: 'modifyTime', width: 80 }, 						
				{ label: '产品线', name: 'productName', width: 80 }, 						
				{ label: '排序', name: 'orderNum', width: 80 } 						
        ],
		viewrecords: true,
        height: 500,
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
			titleName:null,
			productName: null
		},
		showList: true,
		title: null,
		appCourseBanner: {
			title:"",
			pic:"",
			productId:null,
			productName:null
		}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.appCourseBanner = {
					title:"",
					pic:"",
					productId:null,
					productName:null
			};
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
			if($.isNull(vm.appCourseBanner.title)){
				alert("请输入标题！！！");
				return;
			}
			if($.isNull(vm.appCourseBanner.productId)){
				alert("请选择产品线！！！");
				return;
			}
			if(vm.title == "新增")
		    {
		       url = "../appcoursebanner/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../appcoursebanner/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.appCourseBanner),
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
				    url: "../appcoursebanner/delete",
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
			    url: "/appcoursebanner/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.appCourseBanner = r.data;
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
                postData : {
                	"titleName" : vm.q.titleName,
                	"productName" : vm.q.productName
                },
				page:page
            }).trigger("reloadGrid");
		},
		selectProduct : function(){
			productLay.show(function(index,opt){
				vm.appCourseBanner.productId = opt.productId;
				vm.appCourseBanner.productName = opt.productName;
			});
		}
		
	}
});