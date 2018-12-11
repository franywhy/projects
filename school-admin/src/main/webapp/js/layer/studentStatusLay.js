/**
 * 学员状态弹窗js
 */
var studentStatusLay = {
	item:{
		id:null,
		name:null
	},
	init : function(){
		var that = this;
		$("#studentStatusLay_jqGrid").jqGrid({
			colModel: [
			           { label: "主键", name: "id",width: 30 , key: true , hidden: true},
			           { label: "状态", name: "name", width: 150}
		           ],
		   viewrecords: true,
		   height: 250,
		   rowNum: 100,
		   rowList : [200],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: true,
		   /*ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },*/
		});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "学员状态",
	layerHeigh : '400px',
	layerWeigh : '300px',
	show : function(scallback){
		var that = this;
		that.reload();
		that.scallback = scallback;
		that.layerIndex = layer.open({
			type : 1,//
			area : 'auto',
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#studentStatusLayDiv"),
			scrollbar : false,//是否允许浏览器出现滚动条
			fixed : false,//固定
			shadeClose : false,// 是否点击遮罩关闭
			resize : false,//是否允许拉伸
			maxmin: true, //开启最大化最小化按钮
			zIndex : 19891014,
			btn : ['确认','取消' ],
			btn1: function (index) {
			 that.select();
			}
		});
		
	},
	reload:function(){
		$("#studentStatusLay_jqGrid").clearGridData();
		studentStatusLay.item.id = 0;studentStatusLay.item.name = '在读';
		$("#studentStatusLay_jqGrid").addRowData(studentStatusLay.item.id, studentStatusLay.item, "last");
		studentStatusLay.item.id = 1;studentStatusLay.item.name = '休学';
		$("#studentStatusLay_jqGrid").addRowData(studentStatusLay.item.id, studentStatusLay.item, "last");
	},
	select:function(){
		var ids = getJqGridSelectedRows("studentStatusLay_jqGrid");
		if(ids == null){
			return ;
		}
		var idString = '';
		var nameString = '';
		for(var i=0;i<ids.length;i++){
			var rowData = $("#studentStatusLay_jqGrid").jqGrid('getRowData',ids[i]);
			idString = idString+rowData.id+',';
			nameString = nameString+rowData.name+' ';
		}
		if($.isFunction(this.scallback)){
			this.scallback(idString , nameString);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	},
	
	
}
studentStatusLay.init();
