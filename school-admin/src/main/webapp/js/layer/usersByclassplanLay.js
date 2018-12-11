/**
 * 排课下的学员弹窗js
 */
var usersByclassplanLay = {
	init : function(){
		$("#quserName").val("");
		$("#qmobile").val("");
		$("#qclassName").val("");
		var that = this;
		$("#usersByclassplanLay_jqGrid").jqGrid({
			url: "../course/classplan/userList",
			datatype: "local",
			colModel: [
					{ label: '学员Id', name: 'userId', hidden:true , key: true },
					{ label: '学员名称', name: 'userName', width: 150 },
					{ label: '手机号码', name: 'mobile', width: 150 }, 			
					{ label: '学习计划单号', name: 'userplanClassNo', width: 200 }, 			
					{ label: '入课时间', name: 'timestamp', width: 150 }, 			
					{ label: '订单Id', name: 'orderId', hidden:true },
					{ label: '订单号', name: 'orderNo', width: 150 },
					{ label: '班级Id', name: 'classId', hidden:true },
					{ label: '班级名称', name: 'className', width: 150 },
					{ label: '班型Id', name: 'classTypeId', hidden:true },
					{ label: '班型名称', name: 'classTypeName', width: 150 }
		     ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#usersByclassplanLay_jqGridPager",
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
//		   ondblClickRow : function(rowid,iRow,iCol,e){
//			   that.select();
//		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#usersByclassplanLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "该排课下的学员列表",
	layerHeigh : '650px',
	layerWeigh : '990px',
	classplanId:  null,
	show : function(classplanId , scallback){
		var that = this;
		that.scallback = scallback;
		that.classplanId= classplanId;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#usersByclassplanLayDiv"),
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
		$("#usersByclassplanLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				userName : $("#quserName").val() , //学员名称
				mobile : $("#qmobile").val() , //学员手机号
				className : $("#qclassName").val() , //班级名称
				classplanId : that.classplanId 
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("usersByclassplanLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#usersByclassplanLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		this.classplanId = null;
		$("#quserName").val("");
		$("#qmobile").val("");
		$("#qclassName").val("");
	}
}
usersByclassplanLay.init();
