/**
 * 课程弹窗js
 */
var kpmCourseLay = {
	init : function(){
		var that = this;
		$("#kpmCourseLay_jqGrid").jqGrid({
			url: "../layerdata/coursesList",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "nc_id",hidden:true , key: true },
			           { label: "課程名称", name: "name", width: 280	  },
			           { label: "同步时间", name: "syn_time", width: 280	, 
			        	   formatter : function(cellvalue, options, rowObject) {  
		                        return vm.dateFormat(cellvalue);  
		                    }
			           }
			          
		           ],
		   viewrecords: true,
		   height: 300,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25,
		   width:680,
		   autowidth:false,
		   multiselect: false,
		   pager: "#kpmCourseLay_jqGridPager",
		   jsonReader : {
			   root: "data.datas",
			   page: "data.pageNo",
			   total: "data.totalPage",
			   records: "data.totalCount"
		   },prmNames : {
				page : "page",
				rows : "limit",
				order : "order"
			},
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   gridComplete:function(){
//			   //隐藏grid底部滚动条
			   $("#levelLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "課程列表",
	layerHeigh : '350px',
	layerWeigh : '500px',
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : ['700px','600px'],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#kpmCourseLayDiv"),
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
		$("#kpmCourseLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
            	courseName : $("#kpmcourseName").val() , //课程名称
            	},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
		
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("kpmCourseLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#kpmCourseLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		
		//
		
	}
}
kpmCourseLay.init();
