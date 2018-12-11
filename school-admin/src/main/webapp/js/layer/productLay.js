/**
 * 产品线弹窗js
 */
var productLay = {
	init : function(){
		$("#qproductName").val("");
		var that = this;
		$("#productLay_jqGrid").jqGrid({
			url: "../layerdata/productList", 
			datatype: "local",
			colModel: [
					{ label: 'productId', name: 'productId', key: true , width: 80},
					{ label: '名称', name: 'productName', width: 120 }, 						
					{ label: '备注', name: 'remark', width: 120 }, 						
					{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
						return value === 0 ? 
							'<span class="label label-danger">禁用</span>' : 
							'<span class="label label-success">正常</span>';
					}}, 
					{ label: '点播userId', name: 'polyvuserid', width: 120 }, 						
					{ label: '点播secretKey', name: 'polyvsecretkey', width: 120 }	
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#productLay_jqGridPager",
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
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#productLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "产品线列表",
	layerHeigh : '650px',
	layerWeigh : '750px',
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#productLayDiv"),
			scrollbar : true,//是否允许浏览器出现滚动条
			fixed : false,//固定
			shadeClose : false,// 是否点击遮罩关闭
			resize : true,//是否允许拉伸
			maxmin: true, //开启最大化最小化按钮
			zIndex : 19891014,
			btn : ['确认','取消' ],
			btn1: function (index) {
				that.select();
			}
		});
		
	},
	reload:function(){
		$("#productLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				productName : $("#qproductName").val() , //产品线名称
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("productLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#productLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qproductName").val("");
	}
}
productLay.init();