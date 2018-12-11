/**
 * 资料弹窗js
 */
var kpmMaterialDetailLay = {
	init : function(){
		var that = this;
		
		 $("#kpmName").val("");
    	 $("#kpmProperty").val("");
    	$("#kpmType").val("");
    	$("#isRelevance").val("");
    	$("#kpmTime").val("");
		
		$("#kpmMaterialDetail_jqGrid").jqGrid({
			url: "../layerdata/materialDetailList",
			datatype: "local",
			/**
			 * 
			 */
			postData:{
				limits : $("#kpmMaterialDetail_jqGridPager").find(".ui-pg-selbox").val()
            	},
			colModel: [
				
				{ label: 'id', name: 'id', hidden:true ,key: true},
				{ label: '名称', name: 'name', width: 300 }, 						
				{ label: '类型', name: 'type', width: 200 ,formatter:"select", editoptions:{value:"VIDEO:视频;LIVE:直播;QUESTIONBANK:题库;TRAINING:实训;FILE:文件;FACETOFACE:面授;OTHER:其他"}}, 						
				{ label: '属性', name: 'property', width: 200 ,formatter:"select", editoptions:{value:"BEFORECLASS:课前;MIDDLECLASS:课中;AFTERCLASS:课后"}}, 						
				/*{ label: '创建时间', name: 'createTime', width: 80 }, 						
				{ label: '更新时间', name: 'updateTime', width: 80 }, 						
				{ label: '状态', name: 'status', width: 80 }, 						
				{ label: '创建人', name: 'createBy', width: 80 }, 					
				{ label: '最后修改人', name: 'updateBy', width: 80 },*/
				{ label: '是否关联', name: 'isRelevance', width: 200 },
			          
		           ],
		   viewrecords: true,
		   height: 380,
		   /*rowNum: 10,*/
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: true,
		   pager: "#kpmMaterialDetail_jqGridPager",
		   jsonReader : {
			   root: "data.list",
			   page: "data.currPage",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },
//		   ondblClickRow : function(rowid,iRow,iCol,e){
//			   that.select();
//		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#kpmMaterialDetail_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   },
		   onPaging : function(pgButton){
			   
			  
			   that.reload();
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "资料列表",
	layerHeigh : '350px',
	layerWeigh : '500px',
	point_id : null,
	show : function(point_id,scallback){
		var that = this;
		this.point_id = point_id;
		that.scallback = scallback;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : ['1000px', '600px'],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#kpmMaterialDetailLayDiv"),
			scrollbar : true,//是否允许浏览器出现滚动条
			fixed : false,//固定
			shadeClose : false,// 是否点击遮罩关闭
			resize : true,//是否允许拉伸
			maxmin: true, //开启最大化最小化按钮
			zIndex : 19891015,
			btn : ['确认','取消' ],
			btn1: function (index) {
				that.select();
			}
		});
		
	},
	reload:function(){
		//alert($("#kpmMaterialDetail_jqGridPager").find(".ui-pg-selbox").val());
		$("#kpmMaterialDetail_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				
				point_id : this.point_id,
            	name : $("#kpmName").val(),  //名称
            	property : $("#kpmProperty").val(),
            	type : $("#kpmType").val(),
            	isRelevance : $("#isRelevance").val(),
            	sTime : $("#kpmTime").val(),
            	limit : $("#kpmMaterialDetail_jqGridPager").find(".ui-pg-selbox").val()
            	},
			page : 1,
			
			/*pageSize : 10,*/
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		
		//var selectDetail = getJqGridSelectedRow("kpmMaterialDetail_jqGrid");
		//if(selectDetail == null){
		//	return ;
		//}
		//行数据
		//var rowData = $("#kpmMaterialDetail_jqGrid").jqGrid('getRowData',selectDetail);
		//if($.isFunction(this.scallback)){
		//	this.scallback( rowData);
		//}
		
		var selectedIds = $("#kpmMaterialDetail_jqGrid").jqGrid("getGridParam", "selarrrow");
		if(null==selectedIds){
			return ;
		}
		var details = new Array();
		
		 for(var i = 0; i< selectedIds.length;i++){
			var rowData = $("#kpmMaterialDetail_jqGrid").jqGrid('getRowData',parseInt(selectedIds[i]));
			details.push(rowData);
		}
		this.scallback(details);
		//关闭浮层
		layer.close(this.layerIndex);
		
		//
		
	}
}
kpmMaterialDetailLay.init();
