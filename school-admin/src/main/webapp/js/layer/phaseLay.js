/**
 * 阶段弹窗js
 */
var phaseLay = {
	init : function(){
		$("#qphaseName").val("");
		
		var that = this;
		$("#phaseLay_jqGrid").jqGrid({
			url: "../layerdata/phaseList",
			datatype: "local",
			colModel: [
						{ label: '阶段Id', name: 'phaseId', hidden:true , key: true },
						{ label: '阶段名称', name: 'phaseName', width: 240 },
						{ label: '阶段编号', name: 'phaseNo', width: 100 }, 			
						{ label: '创建时间', name: 'createTime', width: 170 } 			
		           ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#phaseLay_jqGridPager",
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
			   $("#phaseLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	classPlanId : null,//classPlanId
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "阶段列表",
	layerHeigh : '650px',
	layerWeigh : '750px',
	show : function(classPlanId , scallback){
		var that = this;
		//console.log(classPlanId);
		that.classPlanId = classPlanId;
		that.scallback = scallback;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#phaseLayDiv"),
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
		$("#phaseLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				phaseName : $("#qphaseName").val() , //阶段名称
				classPlanId : that.classPlanId, //课程FK
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("phaseLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#phaseLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qphaseName").val("");
	}
}
phaseLay.init();
