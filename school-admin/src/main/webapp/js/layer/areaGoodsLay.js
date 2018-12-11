/**
 *根據商品ID查找省份弹窗js
 */
var areaGoodsLay = {
	init : function(){
		var that = this;
		$("#areaGoodsLay_jqGrid").jqGrid({
			url: "../layerdata/areaGoodsList",
			datatype: "local",
			colModel: [
			           { label: "ID", name: "areaId",width: 100 , key: true},
			           { label: "省份", name: "areaName",width: 100}
		           ],
		   viewrecords: true,
		   height: 300,
		   rowNum: 100,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		  // pager: "#areaGoodsLay_jqGridPager",
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		  /* gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#areaLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }*/
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerHeigh : '400px',
	layerWeigh : '230px',
	goodsId : null,
	show : function(id,scallback){
		var that = this;
		that.goodsId = id;
		//debugger;
		that.scallback = scallback;
		that.reload();
		var title = "省份列表";
		that.layerIndex = layer.open({
			type : 1,//
			//area : 'auto',
			area : [that.layerWeigh,that.layerHeigh],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#areaGoodsLayDiv"),
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
		$("#areaGoodsLay_jqGrid").jqGrid('setGridParam',{ 
			postData:{
				goodsId:this.goodsId,
			},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("areaGoodsLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#areaGoodsLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	}
}
areaGoodsLay.init();
