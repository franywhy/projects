/**
 * 班型js
 */
var classTypeLay = {
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "班型列表",//标题
	layerHeigh : '450px',//高度
	layerWeigh : '300px',//宽度
	url : "../layerdata/classTypeList",//地址
	init : function(){
		var that = this;
		$("#classTypeLay_jqGrid").jqGrid({
			url: that.url,
			datatype: "local",
			colModel: [
			           { label: "主键", name: "classtypeId" , key: true , hidden: true},
			           { label: "班型", name: "classtypeName", width: 200	  }
		           ],
		   viewrecords: true,
		   height: 250,
		   rowNum: 100,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   //pager: "#areaLay_jqGridPager",
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   }
		   /*gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#areaLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }*/
			});
	},
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
			content : $("#classTypeLayDiv"),
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
		$("#classTypeLay_jqGrid").jqGrid('setGridParam',{ 
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("classTypeLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#classTypeLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
//					//关闭浮层
		layer.close(this.layerIndex);
	}
}
classTypeLay.init();
