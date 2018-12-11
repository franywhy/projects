/**
 * 考试时间弹窗js
 */
var examScheduleLay = {
	init : function(){
		$("#qscheduleName").val("");
		var that = this;
		$("#examScheduleLay_jqGrid").jqGrid({
			url: "../layerdata/examScheduleList",
			datatype: "local",
			colModel: [
			           	{ label: '主键', name: 'id',hidden:true, key: true },
						{ label: '考试时间段名称', name: 'scheduleName', width: 150 },					
						{ label: '考试年月日', name: 'scheduleDate', width: 150 },					
						{ label: '创建人', name: 'creationName', width: 80 }, 
						{ label: '创建时间', name: 'createTime', width: 80 }, 
						{ label: '修改人', name: 'modifiedName', width: 80 }, 
						{ label: '修改时间', name: 'modifyTime', width: 80 }, 
						{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
							return value === 0 ? 
								'<span class="label label-danger">禁用</span>' : 
								'<span class="label label-success">正常</span>';
						}},
						{ label: '备注', name: 'comments', width: 150 }
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#examScheduleLay_jqGridPager",
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
			   $("#examScheduleLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		this.reload();
		var title = "考试时间列表";
		that.layerIndex = layer.open({
			type : 1,//
			area : ['900px', '650px'],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#examScheduleLayDiv"),
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
		$("#examScheduleLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				scheduleName : $("#qscheduleName").val() , //考试 时间段名称
            	},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("examScheduleLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#examScheduleLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qscheduleName").val("");
	}
}
examScheduleLay.init();
