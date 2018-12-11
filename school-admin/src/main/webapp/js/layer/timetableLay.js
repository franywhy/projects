/**
 * 上课时点弹窗js
 */
var timetableLay = {
	init : function(){
		$("#qName").val("");
		var that = this;
		$("#timetableLay_jqGrid").jqGrid({
			url: "../layerdata/timetableList",
			datatype: "local",
			colModel: [
						{ label: '上课时点Id', name: 'number', hidden : true, width: 50, key: true },
						{ label: '上课时点名称', name: 'name', width: 180 },
						{ label: '创建人', name: 'creationName', width: 80 },
						{ label: '创建时间', name: 'createTime', width: 80 }, 			
						{ label: '修改人', name: 'modifiedName', width: 80 }, 	
						{ label: '修改时间', name: 'updateTime', width: 80 }, 
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
		   pager: "#timetableLay_jqGridPager",
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
			   $("#timetableLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "上课时点列表",
	layerHeigh : '650px',
	layerWeigh : '780px',
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
			content : $("#timetableLayDiv"),
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
	reload:function(){//刷新上课时点列表
		$("#timetableLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				name : $("#qName").val() , //上课时点名称
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("timetableLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#timetableLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qName").val("");
	}
}
timetableLay.init();
