/**
 * 课程弹窗js
 */
var courseLay = {
	init : function(){
		$("#qcourseName").val("");
		var that = this;
		$("#courseLay_jqGrid").jqGrid({
			url: "../layerdata/courseList",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "courseId",width: 80 , key: true , hidden: true},
			           
			           { label: '排课可冲突', name: 'courseEq', width: 100 ,hidden: true},
			           
			           { label: "课程名称", name: "courseName", width: 120 },
			           { label: '课程类别', name: 'courseLb', width: 100 },
			           { label: '课程类型', name: 'courseType', width: 100 },
			           { label: '考试方式', name: 'examType', width: 100 },
			           { label: '学分', name: 'courseCredit', width: 100 },
			           { label: '排课可冲突', name: 'courseEq', width: 100, formatter: function(value, options, row){
							return value === 0 ? 
								'<span class="label label-danger">不可冲突</span>' : 
								'<span class="label label-success">可以冲突</span>';
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
		   pager: "#courseLay_jqGridPager",
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
			   $("#courseLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
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
		var title = "课程列表";
		that.layerIndex = layer.open({
				type : 1,//
				//area : "auto",
				area: ['701px', '650px'],
				title :title,
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#courseLayDiv"),
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
	reload:function(){//刷新课程列表
		$("#courseLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
            	courseName : $("#qcourseName").val() , //课程名称
            	},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("courseLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#courseLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qcourseName").val("");
	}
}
courseLay.init();
