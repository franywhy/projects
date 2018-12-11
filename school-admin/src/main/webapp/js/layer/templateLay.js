/**
 * 模板弹窗js
 */
var templateLay = {
	init : function(){
		var that = this;
		$("#templateLay_jqGrid").jqGrid({
			url: "../layerdata/contractTemplateList",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "id",width: 30 , key: true , hidden: true},
			           { label: "模板id", name: "templateId",width: 30 , key: true , hidden: true},
			           { label: "模板名称", name: "templateName", width: 200	  }
		           ],
		   viewrecords: true,
		   height: 250,
		   rowNum: 100,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
//				   pager: "#levelLay_jqGridPager",
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
			   $("#areaLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }*/
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "合同模板选择",
	layerHeigh : '450px',
	layerWeigh : '300px',
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
 			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			area : [that.layerWeigh,that.layerHeigh],
			content : $("#templateLayDiv"),
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
		$("#templateLay_jqGrid").jqGrid('setGridParam',{ 
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("templateLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#templateLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	}
}
templateLay.init();
