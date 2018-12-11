$(function () {
    $("#jqGrid").jqGrid({
        url: '../mallmarketcourse/list',
        datatype: "json",
        colModel: [		
				{ label: 'id', name: 'id', hidden:true , key: true },
				{ label: '所属分类', name: 'parentName', width: 80 },
				{ label: '标题', name: 'name', width: 80 }, 						
				{ label: '图片', name: 'pic', width: 80 , align : "center" ,formatter : function(value, options, row){return '<img height="50px" width="50px"  src="'+value+'"/>';}},
				{ label: 'pcUrl', name: 'pcUrl', width: 80 }, 						
				{ label: 'appUrl', name: 'appUrl', width: 80 }, 						
				{ label: '上课方式', name: 'classWay', width: 80 }, 						
				{ label: '试用对象', name: 'suitableObject', width: 80 }, 						
				{ label: '产品线', name: 'productName', width: 80 },
				{ label: '可分享', name: 'isShare', width: 80, formatter: function(value, options, row){
					return value === 0 ?
						'<span class="label label-danger">否</span>' : 
						'<span class="label label-success">是</span>';
				}}, 
				{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
					return value === 0 ? 
						'<span class="label label-danger">禁用</span>' : 
						'<span class="label label-success">正常</span>';
				}}, 
				{ label: '排序', name: 'orderNum', width: 80 },
				{ label: '创建时间', name: 'createTime', width: 80 }, 						
				{ label: '创建人', name: 'createName', width: 80 }, 						
				{ label: '修改时间', name: 'modifyTime', width: 80 }, 						
				{ label: '修改人', name: 'modifyName', width: 80 }, 		
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
			parentName:null,
			titleName:null,
			productName: null
		},
		showList: true,
		title: null,
		mallMarketCourse: {
			pic:"",
			productId:null,
			productName:null,
			level : 2,
			status:1,
			isShare:1
		},
		selData : {
			classifyList : [{id : "" , name : "数据加载中。。。"}],
		},
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.mallMarketCourse = {
					pic:"",
					productId:null,
					productName:null,
					level : 2,
					status:1,
					isShare:1
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
			if($.isNull(vm.mallMarketCourse.parentId)){
				alert("请选择分类！！！");
				return;
			}
			if($.isNull(vm.mallMarketCourse.name)){
				alert("请输入标题！！！");
				return;
			}
			if($.isNull(vm.mallMarketCourse.productId)){
				alert("请选择产品线！！！");
				return;
			}
			if(vm.title == "新增")
		    {
		       url = "../mallmarketcourse/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mallmarketcourse/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallMarketCourse),
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
				    url: "../mallmarketcourse/delete",
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
			    url: "/mallmarketcourse/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.mallMarketCourse = r.data;
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
                	"parentName" : vm.q.parentName,
                	"titleName" : vm.q.titleName,
                	"productName" : vm.q.productName
                },
				page:page
            }).trigger("reloadGrid");
		},
		
		resume:function(event){
			   var ids = getSelectedRows();
				if(ids == null){
					return ;
				}
				
				confirm('确定要启用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../mallmarketcourse/resume",
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
		pause:function(event){
		    var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mallmarketcourse/pause",
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
		selectProduct : function(){
			productLay.show(function(index,opt){
				vm.mallMarketCourse.productId = opt.productId;
				vm.mallMarketCourse.productName = opt.productName;
			});
		},
		//初始化营销课程分类列表
		initMarkCourse : function (){
			var that = this;
            $.getJSON("../mallmarketcourse/classifyDownList", function(r){
                if(r.code === 0){
                	that.selData.classifyList = r.data;
				}else{
					alert(r.msg);
				}
            });
		},
	},
	created : function(){
		this.initMarkCourse()
	}
});