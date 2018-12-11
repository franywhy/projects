/**
 * 资料库弹窗js
 */
var materialLay = {
	init : function(){
		var that = this;
		$("#materialLay_jqGrid").jqGrid({
			url: "../layerdata/materialList",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "materialId",width: 30 , key: true , hidden: true},
			           { label: "资料库名称", name: "materialName", width: 260	  }
		           ],
		   viewrecords: true,
		   height: 250,
		   rowNum: 100,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   //pager: "#materialLay_jqGridPager",
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   /*gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#materialLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }*/
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "资料库列表",
	layerHeigh : '400px',
	layerWeigh : '300px',
	show : function(scallback){
		var that = this;
		that.reload();
		that.scallback = scallback;
		that.layerIndex = layer.open({
			type : 1,//
			material : 'auto',
//			material : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#materialLayDiv"),
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
		$("#materialLay_jqGrid").jqGrid('setGridParam',{ 
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("materialLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#materialLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	}
}
materialLay.init();
