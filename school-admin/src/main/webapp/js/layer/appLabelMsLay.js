/**
 * 标签弹窗js
 */
var appLabelMsLay = {
	init : function(){
        $("#qproductId").val(-1);
        $("#qlabelName").val("");
		var that = this;
		$("#appLabelMsLay_jqGrid").jqGrid({
			url: "../layerdata/appLabelList",
			datatype: "local",
			//mtype: "post",
			colModel: [
			           { label: "主键", name: "id",width: 100 , key: true , hidden: true},
			           { label: "标签", name: "labelName", width: 900	  }
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 80,
            rowList : [10,50,100],
		   rownumbers: true, 
		   rownumWidth: 50,
		   autowidth:true,
		   multiselect: true,
		   pager: "#appLabelMsLay_jqGridPager",
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
			   $("#appLabelMsLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
		});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "标签列表",
	layerHeigh : '700px',
	layerWeigh : '1000px',
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
			content : $("#appLabelMsLayDiv"),
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
		$("#appLabelMsLay_jqGrid").jqGrid('setGridParam',{
            postData : {
                productId : encodeURIComponent($("#qproductId").val()) , //产品线
                labelName : encodeURIComponent($("#qlabelName").val())  //标签名称
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
        var selectIds = getJqGridSelectedRows("appLabelMsLay_jqGrid");
        if(selectIds == null){
            return ;
        }
        var selectDetails = [];
        selectIds.forEach(function(value, index, array) {
            selectDetails.push($("#appLabelMsLay_jqGrid").jqGrid('getRowData',value));
        });
        if($.isFunction(this.scallback)){
            this.scallback(selectIds , selectDetails);
        }

		//关闭浮层
		layer.close(this.layerIndex);
        $("#qproductId").val(-1);
        $("#qlabelName").val("");
	}
}
appLabelMsLay.init();
