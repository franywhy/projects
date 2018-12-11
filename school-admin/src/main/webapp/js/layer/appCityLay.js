/**
 * 标签弹窗js
 */
var appCityLay = {
	init : function(){
        $("#qcityId").val("");
        $("#qcityName").val("");
		var that = this;
		$("#appCityLay_jqGrid").jqGrid({
			url: "../layerdata/appCityList",
			datatype: "local",
			//mtype: "post",
			colModel: [
			           { city: "主键", name: "id",width: 50 , key: true , hidden: true},
			           { city: "标签", name: "cityName", width: 760	  }
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 50,
		   autowidth:true,
		   multiselect: false,
		   pager: "#appCityLay_jqGridPager",
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
            prmNames : {
                page : "page",
                rows : "limit",
                order : "order"
            },
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#appCityLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
		});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "标签列表",
	layerHeigh : '700px',
	layerWeigh : '900px',
	show : function(scallback){
		var that = this;
		that.reload();
		that.scallback = scallback;
		that.layerIndex = layer.open({
			type : 1,//
			//area : 'auto',
            area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#appCityLayDiv"),
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
		$("#appCityLay_jqGrid").jqGrid('setGridParam',{
            postData : {
                cityId : encodeURIComponent($("#qcityId").val()) , //产品线
                cityName : encodeURIComponent($("#qcityName").val()) , //标签名称
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("appCityLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#appCityLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
        $("#qcityId").val("");
        $("#qcityName").val("");
	}
}
appCityLay.init();
