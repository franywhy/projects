/**
 * 资料库类型弹窗js
 */
/*var detailId=getSelectedRow();*/
var materialTypeLay = {
	init : function(){
		var that = this;
		$("#materialTypeLay_jqGrid").jqGrid({
			url: "../layerdata/materialTypeList/",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "materialTypeId",width: 30 , key: true , hidden: true},
			           { label: "资料库类型", name: "materialTypeName", width: 260	  }
		           ],
		   viewrecords: true,
		   height: 250,
		   rowNum: 100,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   //pager: "#materialTypeLay_jqGridPager",
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
			   $("#materialTypeLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }*/
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "资料库类型列表",
	layerHeigh : '400px',
	layerWeigh : '300px',
	materialId : null,
	show : function(materialId , scallback){
		var that = this;
		/*if(that.materialId!=null){*/
		that.scallback = scallback;
		that.materialId = materialId;
		that.reload();
		
		that.layerIndex = layer.open({
			type : 1,//
			materialType : 'auto',
//			materialType : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#materialTypeLayDiv"),
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
		/*}*/
	},
	reload:function(){
		var that =this;
		$("#materialTypeLay_jqGrid").jqGrid('setGridParam',{
			postData:{materialId : that.materialId},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("materialTypeLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#materialTypeLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	}
}
materialTypeLay.init();
