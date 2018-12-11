/**
 * 点播视频弹窗js
 */
var polyvVideoLay = {
	init : function(){
		var that = this;
		$("#polyvVideoLay_jqGrid").jqGrid({
			url: "../layerdata/polyvVideo",
			datatype: "local",
			colModel: [
					{ label: '主键', name: 'vid', key: true, width: 80, hidden:true }, 
	        		{ label: '录播课ID', name: 'vid', width: 80, hidden:true }, 
					{ label: '录播课标题', name: 'title', width: 300 },
					{ label: '录播课时长', name: 'duration', width: 100 }, 
					{ label: '描述', name: 'context', width: 400 }, 
					{ label: '上传日期', name: 'ptime', width: 150 }, 
					{ label: '首截图', name: 'first_image', width: 80, hidden: true }, 
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "视频列表",
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : ['1000px','600px'],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#polyvVideoLayDiv"),
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
		$("#polyvVideoLay_jqGrid").jqGrid('setGridParam',{ 
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("polyvVideoLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#polyvVideoLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	}
}
polyvVideoLay.init();
