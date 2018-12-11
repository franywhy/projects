/**
 * 订单弹窗js
 */
var mallorderLay = {
	init : function(){
		var that = this;
		$("#mallorderLay_jqGrid").jqGrid({
            url: '../mall/order/list?userplanType=1',
            datatype: "local",
            colModel: [
                { label: '订单ID', name: 'orderId', hidden: true,width: 50, key: true },
                { label: '订单号', name: 'orderNo', width: 80 },
                { label: '学员昵称', name: 'nickName', width: 80 },
                { label: '手机号', name: 'mobile', width: 90 },
                { label: '学员规划', name: 'userplanCount', width: 60 , formatter(value, options, row){
                	return value === 0 ? '<span class="label label-danger">未生成</span>' : '<span class="label label-success">已生成</span>';}
                	},
                { label: '专业', name: 'professionName', width: 60 },
                { label: '商品ID', name: 'commodityId', width: 50 },
                { label: '商品名称', name: 'commodityName', width: 80 },
                { label: '省份', name: 'areaName', width: 50 },
                { label: '层次', name: 'levelName', width: 40 },
                { label: '报名表号', name: 'ncCode', width: 80 },
                { label: '班型', name: 'classtypeName', width: 80 },
                { label: '班级', name: 'className', width: 80 },
                // { label: '产品线', name: 'productName', width: 80 },
                // { label: '部门', name: 'deptName', width: 50 },
            ],
		   viewrecords: true,
		   height: 380,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#mallorderLay_jqGridPager",
		   jsonReader : {
			   root: "page.list",
			   page: "page.currPage",
			   total: "page.totalPage",
			   records: "page.totalCount"
		   },
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "订单列表",
	layerHeigh : '600px',
	layerWeigh : '900px',
	show : function(scallback){
		var that = this;
		// that.reload();
		that.scallback = scallback;
		that.layerIndex = layer.open({
			type : 1,//
			// area : 'auto',
			area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#mallorderLayDiv"),
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

        var q_order_orderNo = $("#q_order_orderNo").val();
        var q_order_mobile = $("#q_order_mobile").val();
        var q_order_nickName = $("#q_order_nickName").val();

		$("#mallorderLay_jqGrid").jqGrid('setGridParam',{
            postData : {
                orderNo : q_order_orderNo , //课程ID
                mobile : q_order_mobile, //课程ID
                nickName : q_order_nickName //课程ID
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("mallorderLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#mallorderLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	}
}
mallorderLay.init();
