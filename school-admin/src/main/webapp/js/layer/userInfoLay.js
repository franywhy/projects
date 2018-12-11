/**
 * 商品弹窗js
 */
var userInfoLay = {
	init : function(){
		var that = this;
		$("#userInfoLay_jqGrid").jqGrid({
			url: "../layerdata/userInfoList",
			datatype: "local",
			colModel: [
			           { label: "ID", name: "userId",width: 270 , key: true,hidden:true},
			           { label: "用户名字", name: "nickName", width: 500	 },
			           { label: "手机号码", name: "mobile",width: 500}
		           ],
		   viewrecords: true,
		   height: 300,
		   rowNum: 100,
		   rowList : [200],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#userInfoLay_jqGridPager",
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#userInfoLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		var title = "用户列表";
		that.layerIndex = layer.open({
			type : 1,//
			//area : 'auto',
			area : ['800px', '500px'],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#userInfoLayDiv"),
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
		/*var page = $("#userInfoLay_jqGrid").jqGrid('getGridParam','page');*/
		$("#userInfoLay_jqGrid").jqGrid('setGridParam',{ postData : {
			mobile : $("#qmobile").val() , //课程名称
    	    },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("userInfoLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#userInfoLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	}
}
userInfoLay.init();
