/**
 * 课程列表js
 */
var csKnowleKeyPoinLay = {
	init : function(){
		var that = this;
		that.courseId = null;
		$("#csKnowleKeyPoinLay_jqGrid").jqGrid({
			url: "../courses/layer/courseList",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "courseId" , index: 'course_id',key: true , hidden: true},
			           { label: "编码", name: "courseNo" , index: 'course_no',hidden: false , width: 250},
			           { label: "名称", name: "courseName" , index: 'course_name',hidden: false , width: 270}
		           ],
		   viewrecords: true,
		   height: 320,
		   rowNum: 100,
		   rowList : [10,30,50,100],
		   rownumbers: true, 
//		   rownumWidth: 35, 
		   width:500,
		   autowidth:true,
		   multiselect: false,
		   pager: "#csKnowleKeyPoinLay_jqGridPager",
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
			   $("#csKnowleKeyPoinLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		var title = "课程列表";
		that.layerIndex = layer.open({
			type : 1,//
			area : ['600px','550px'],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#csKnowleKeyPoinLayDiv"),
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
		
		var csKnowleKeyPoinLay_CourseName = $("#csKnowleKeyPoinLay_CourseName").val();
		var csKnowleKeyPoinLay_CourseNo = $("#csKnowleKeyPoinLay_CourseNo").val();
		
		$("#csKnowleKeyPoinLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				courseName : csKnowleKeyPoinLay_CourseName , //课程ID
				courseNo : csKnowleKeyPoinLay_CourseNo //课程ID
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("csKnowleKeyPoinLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#csKnowleKeyPoinLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
        //关闭浮层
		layer.close(this.layerIndex);
		this.courseId = null;//课程ID
	}
}
csKnowleKeyPoinLay.init();
