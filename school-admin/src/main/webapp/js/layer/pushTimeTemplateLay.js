/**
 * 推送模板弹窗js
 */
var pushTemplateLay = {
	init : function(){
		var that = this;
		$("#pushTemplateLay_jqGrid").jqGrid({
			url: "../layerdata/pushTemplateList",
			datatype: "local",
			colModel: [
					{ label: '推送模板Id', name: 'id', width: 100, key: true },
					{ label: '推送模板名称', name: 'name', width: 200 },
					{ label: '推送日期备注', name: 'remark', width: 200 },
					{ label: '推送日期', name: 'pushDay', width: 100 },
					{ label: '推送时点', name: 'pushTime', width: 100 },
					{ label: '推送类型', name: 'type', width: 100 ,formatter: function(value, options, row){
						return value === 0 ? '定点' : '定时';
					}},
					{ label: '推送内容', name: 'pushContentTemplate', width: 600 }
		    ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#pushTemplateLay_jqGridPager",
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
			   $("#pushTemplateLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	layerIndex : null,
	scallback : null,
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		var that = this;
		var title = "推送模板列表";
		that.layerIndex = layer.open({
				type : 1,//
				//area : "auto",
				area: ['1331px', '620px'],
				title :title,
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#pushTemplateLayDiv"),
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
	reload:function(){//刷新推送模板列表
		$("#pushTemplateLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
            	pushTemplateName : $("#qpushTemplateName").val() , //排课名称
            	},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("pushTemplateLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#pushTemplateLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	}
}
pushTemplateLay.init();
