/**
 * 父级标签弹窗js
 */
var labelParentLay = {
	init : function(){
		$("#qlabelParentName").val("");
		this.productId = null;
		var that = this;
		$("#labelParentLay_jqGrid").jqGrid({
			url: "../layerdata/labelParentList", 
			datatype: "local",
			colModel: [
					{ label: 'id', name: 'id', key: true , width: 150},
					{ label: '名称', name: 'labelName', width: 150 }, 						
					{ label: '产品线', name: 'productId', width: 150, formatter: function(value, options, row){
						return value === 0 ? '会计' : '自考';
					}},
					{ label: '状态', name: 'dr', width: 150, formatter: function(value, options, row){
						return value === 1 ? 
							'<span class="label label-danger">禁用</span>' : 
							'<span class="label label-success">正常</span>';
					}}, 
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#labelParentLay_jqGridPager",
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
		   prmNames : {
	            page:"page", 
	            rows:"limit", 
	            order: "order"
	        },
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#labelParentLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "父级标签列表",
	layerHeigh : '650px',
	layerWeigh : '750px',
    productId : null,//产品线ID
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
			content : $("#labelParentLayDiv"),
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
        if(vm.title == "修改") {
            this.productId = vm.appLabel.productId;
		} else {
            this.productId = null;
		}
		$("#labelParentLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				labelParentName : $("#qlabelParentName").val() , //父级标签名称
                productId : this.productId
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("labelParentLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#labelParentLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qlabelParentName").val("");
        this.productId = null;
	}
}
labelParentLay.init();