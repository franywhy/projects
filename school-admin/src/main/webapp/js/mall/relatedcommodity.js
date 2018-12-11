$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/relatedcommodity/list',
        datatype: "json",
        colModel: [			
			{ label: '商品关联Id', name: 'relatedCommodityId', width: 50, key: true },
			{ label: '关联名称', name: 'relatedName', width: 80 }, 			
			{ label: '备注', name: 'remake', width: 80 }, 			
			{ label: '建档人', name: 'creationName', width: 80 }, 			
			{ label: '创建时间', name: 'creationTime', width: 80 }, 			
			{ label: '修改人', name: 'modifiedName', width: 80 }, 			
			{ label: '修改日期', name: 'modifiedTime', width: 80 }, 			
			{ label: '是否禁用', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},		
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
    
 /*--------------------------------------------detailGrid------------------------------------------*/    
    $("#detailGrid").jqGrid({
    	datatype: "json",
    	colModel: [			
			{ label: '子表ID', name: 'id', hidden : true, width: 50, key: true },
			{ label: '关联商品ID', name: 'relatedCommodityId', hidden : true, width: 80 }, 
			/*{ label: '商品id', name: 'commodityId', editable : true, width: 80 }, */	
			{ label: '商品', name: 'commodity', editable : true, width: 80 }, 			
			{ label: '别名', name: 'alias', editable : true, width: 80 }			
        ],
        height : 'auto',
        rownumbers : true, //如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。此列名为'rn'.
        viewrecords : true,//定义是否要显示总记录数
        multiselect: true,//定义是否可以多选
        autowidth:false,//自动宽度
		/*pager : "#paddtree",
		ExpandColumn : 'index',
		editurl : 'clientArray',// 保存数据到客户端上
		sortorder : "asc",
		caption : "子表",*/
	});
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			relatedName: null
		},
		showList: true,
		title: null,
		relatedCommodity: {},
		commodityList:[],
		commodityListMap:{},
		detail: {
			title:null,
			obj:{}
		}
	},
	methods: {
		commodityChange : function(event){
//			console.log($(event.target).val());
			var selVal = $(event.target).val()
			vm.detail.obj.alias = vm.commodityListMap[selVal].alias;
			vm.detail.obj.commodity = vm.commodityListMap[selVal].name;
			$("#input_alias").val(vm.detail.obj.alias);
		},
		initData : function (){
			//加载商品数据
            $.getJSON("../mall/mallgoodsinfo/selectList/", function(r){
                if(r.code === 0){
                	vm.commodityList = r.data;
                	if( null != vm.commodityList && vm.commodityList.length > 0){
                		$.each(vm.commodityList , function(i , item){
                			vm.commodityListMap[item.id] = item;
                		});
                	}
				}else{
					alert(r.msg);
				}
            });
		},
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.relatedCommodity = {status : 1};
			jQuery("#detailGrid").jqGrid("clearGridData");
		},
		update: function (event) {
			var relatedCommodityId = getSelectedRow();
			if(relatedCommodityId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            jQuery("#detailGrid").jqGrid("clearGridData");
            vm.getInfo(relatedCommodityId)
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../mall/relatedcommodity/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/relatedcommodity/update";
		    }else
		    {
		       url = "";
		    }
		    
		    //子表所有数据
			var details = [] ;
			var ids = $("#detailGrid").jqGrid('getDataIDs');
			for(var i = 0;i<ids.length;i++){
				var row = $('#detailGrid').jqGrid('getRowData',ids[i]);
				if(isNaN(row.id)){
    				row.id = null;
    			}
			    details.push(row);
			}
			vm.relatedCommodity.detailList = details;
			
		    
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.relatedCommodity),
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
			var relatedCommodityIds = getSelectedRows();
			if(relatedCommodityIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/relatedcommodity/delete",
				    data: JSON.stringify(relatedCommodityIds),
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
		getInfo: function(relatedCommodityId){
			$.get("../mall/relatedcommodity/info/"+relatedCommodityId, function(r){
                vm.relatedCommodity = r.relatedCommodity;
                var detailList = r.detailList;
                if(null != detailList && detailList.length > 0){
                	$.each(detailList , function(i , v){
                		//添加行
						$("#detailGrid").addRowData(v.id, v, "last");
                	});
                }
            });
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p|| $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"relatedName" : vm.q.relatedName},
                page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var relatedCommodityIds = getSelectedRows();
			if(relatedCommodityIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/relatedcommodity/resume",
				    data: JSON.stringify(relatedCommodityIds),
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
		    var relatedCommodityIds = getSelectedRows();
			if(relatedCommodityIds == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/relatedcommodity/pause",
				    data: JSON.stringify(relatedCommodityIds),
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
		
		iadd: function(){
			//弹框标题
			vm.detail.title = "新增";
			//初始化弹窗数据
			vm.detail.obj = {};
			vm.ishow();
		},
		
		iupdate:function(){
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("detailGrid");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#detailGrid").jqGrid('getRowData',selectDetail);
			//str=>array
			/*rowData.relatedCommodityIds = rowData.relatedCommodityIds.split(",");*/
			rowData.relatedCommodityIds = rowData.relatedCommodityIds;
			//
			vm.detail.obj = rowData;
			
			//弹框标题
			vm.detail.title = "修改";
			
			//弹框
			vm.ishow();
		},
		
		ishow : function(){//弹出新增或者修改窗口
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.detail.title,
				area: ['550px', '270px'],
				shadeClose: false,
				content: jQuery("#liveLayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
					if("新增" == vm.detail.title){
						//行ID
						var rowId = new Date().getTime();
						//添加行
						$("#detailGrid").addRowData(rowId, vm.detail.obj, "last");  
					}else if("修改" == vm.detail.title){
						//修改
						$("#detailGrid").setRowData(getJqGridSelectedRow("detailGrid"),vm.detail.obj);
					}
					layer.close(index);
					
	            }
			});
		},
		idel:function(){
			//获取选中行ID
			var selectDetails = getJqGridSelectedRows("detailGrid");
			if(selectDetails == null){
				return ;
			}
			console.log(selectDetails);
			confirm('确定要删除选中的记录？', function(){
				$.each(selectDetails , function(i , val){
					console.log(val);
					$("#detailGrid").jqGrid("delRowData", val);  
				});
				return true;
			});
		},
		
	},
	created : function(){
		this.initData();
	}
});