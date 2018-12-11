/**
 * 报考单弹窗js
 */
var registrationLay = {
	init : function(){
		var that = this;
		$("#registrationLay_jqGrid").jqGrid({
			url: "../layerdata/registrationList",
			datatype: "local",
			colModel: [

				   { label: "报考单ID", name: "id",width: 180 ,hidden: true },
				   { label: "报考订单", name: "registrationNo",width: 180 },
		           { label: "订单号", name: "orderNo",width: 180},
		           { label: "课程", name: "courseName",width: 180},
		           { label: "报考省份", name: "bkAreaName",width: 100},
		           { label: "报考时间段名称", name: "scheduleName",width: 100,hidden: true},
		           { label: "报考时间", name: "scheduleDate",width: 150},
		           { label: "产品线", name: "productName",width: 100 ,hidden: true},
		           { label: "产品线Id", name: "productId",width: 100 ,hidden: true}
		           
		           ],
		   viewrecords: true,
		   height: 300,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 35, 
		   autowidth:true,
		   multiselect: false,

		   pager: "#registrationLay_jqGridPager",
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
			   $("#registrationLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "报考列表",
	layerHeigh : '400px',
	layerWeigh : '300px',
	show : function(scallback){
		var that = this;
		that.reload();
		that.scallback = scallback;
		that.layerIndex = layer.open({
			type : 1,//
			area : 'auto',
			area : ['850px','700px'],
//			area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#registrationLayDiv"),
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
		var that = this;
		$("#registrationLay_jqGrid").jqGrid('setGridParam',{
			postData : {
				registrationNo : $("#registrationNo").val() //授课老师名称
//				teacher : that.teacher , 
//				classTeacher : that.classTeacher  
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("registrationLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#registrationLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#registrationNo").val("");
	}
}
registrationLay.init();
