$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/order/list',
        datatype: "json",
        colModel: [			
			{ label: 'orderId', name: 'orderId', hidden: true,width: 50, key: true },
			{ label: '订单号', name: 'orderNo', width: 80 }, 			
			{ label: '用户ID', name: 'userId', width: 80 }, 	
			{ label: '学员昵称', name: 'nickName', width: 80 }, 
			{ label: '手机号', name: 'mobile', width: 80 }, 
			{ label: '来源', name: 'sourceType', width: 80 }, 
			{ label: '订单名称', name: 'orderName', width: 80 }, 	
			{ label: '订单描述', name: 'orderDescribe', width: 80 }, 
			{ label: '商品ID', name: 'commodityId', width: 80 }, 			
			{ label: '商品名称', name: 'commodityName', width: 80 }, 			
			{ label: '订单总额', name: 'totalMoney', width: 80 }, 			
			{ label: '支付金额', name: 'payMoney', width: 80 }, 
			{ label: '优惠金额', name: 'favorableMoney', width: 80 }, 
			{ label: '支付状态', name: 'payStatus', width: 80 }, 		
			{ label: '支付方式', name: 'payType', width: 80 },
			{ label: '支付宝交易号', name: 'alipayTradeNo', width: 80 }, 
			{ label: '支付时间', name: 'payTime', width: 80 }, 
			{ label: '省份', name: 'areaName', width: 80 }, 
			{ label: '层次', name: 'levelName', width: 80 },
			{ label: '创建时间', name: 'creationTime', hidden: true, width: 80 },
			{ label: '班型', name: 'classtypeName', width: 80 },
			{ label: '班级', name: 'className', width: 80 },
			{ label: '状态', name: 'status', width: 80 },	
			{ label: '备注', name: 'remark', hidden: true, width: 80 }
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
    /*--------------------------------------------------*/
    //班级格式化
    function classNameFormatter(cellvalue, options, rowObject){
		if(null != rowObject && null != rowObject.classIds && rowObject.classIds.length > 0){
			 return vm.getClassNames(rowObject.classIds);
		}
		return "";
	}
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			orderNo: null
		},
		showList: true,
		title: null,
		mallOrder: {},
		orderList:[],
		orderListMap:{},
		detail: {
			title: null,
			ojb: {
				classIds:[]
			}
		}
	},
	methods: {
		/*initData : function (){
			//加载班级数据
            $.getJSON("../mall/class/selectList/", function(r){
                if(r.code === 0){
                	var selData = r.data;
                	if(null != selData && selData.length > 0){
                		var str = "";
                		$.each(selData , function(i,v){
                			str+='<option value="'+v.classId+'">'+v.className+'</option>';
                		});
                		$("#selClass").empty().html(str);
                	}
				}else{
					alert(r.msg);
				}
            
            });
            
		},*/
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.mallOrder = {};
		},
		update: function (event) {
			var orderId = getSelectedRow();
			if(orderId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(orderId)
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../mall/order/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/order/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallOrder),
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
			var orderIds = getSelectedRows();
			if(orderIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/order/delete",
				    data: JSON.stringify(orderIds),
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
		getInfo: function(orderId){
			$.get("../mall/order/info/"+orderId, function(r){
                vm.mallOrder = r.mallOrder;
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
		   var orderIds = getSelectedRows();
			if(orderIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/order/resume",
				    data: JSON.stringify(orderIds),
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
		    var orderIds = getSelectedRows();
			if(orderIds == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/order/pause",
				    data: JSON.stringify(orderIds),
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
		/*iadd:function(){
			//弹框标题
			vm.detail.title = "指定班级";
			
			//初始化弹窗数据
			vm.detail.obj = {classIds:[] , "className" : ""};
			vm.ishow();
		},*/
		iupdate:function(){
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("detailGrid");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#detailGrid").jqGrid('getRowData',selectDetail);
			//str=>array
			rowData.classIds = rowData.classIds.split(",");
			//
			vm.detail.obj = rowData;
			
			//弹框标题
			vm.detail.title = "指定班级";
			
			//弹框
			vm.ishow();
		},
		ishow : function(){//弹出新增或者修改窗口
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.detail.title,
				area: ['600px', '300px'],
				shadeClose: false,
				content: jQuery("#liveLayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
					//非空校验
					//课次名称
					var liveName = vm.detail.obj.liveName;
					if(null == liveName || liveName == ""){
						alert("课次名称不能为空");
						return;
					}
					var liveClassTypeIds = vm.detail.obj.liveClassTypeIds;
					if(null == liveClassTypeIds || liveClassTypeIds.length == 0){
						alert("请选择班型权限");
						return;
					}
					/*if("新增" == vm.detail.title){
						//行ID
						var rowId = new Date().getTime();
						//添加行
						$("#detailGrid").addRowData(rowId, vm.detail.obj, "last");  
					}else if("修改" == vm.detail.title){
						//修改
						$("#detailGrid").setRowData(getJqGridSelectedRow("detailGrid"),vm.detail.obj);
					}*/
					if("指定班级" == vm.detail.title){
						$("#detailGrid").setRowData(getJqGridSelectedRow("detailGrid"),vm.detail.obj);
					}
					layer.close(index);
					
	            }
			});
		},
		getClassList: function(){//获取班型列表
			$.get("../mall/class/select", function(data){
				vm.classList = data.data;
				$.each(vm.classList , function(i , v){
					vm.classMap[v.value] = v.name;
				});
			});
		},
		getClassNames : function(ids){
			var str = "";
			if(null == ids || ids.length == 0){
				return str;
			}
			if(!$.isArray(ids)){
				ids = ids.split(",");
			}
			$.each(ids , function(i , val){
				str += vm.classMap[val] + ";";
			});
			return str.length > 0 ? str.substring(0 , str.length-1) : "";
		}
	},
	created : function(){
		this.getClassList();
	}
});