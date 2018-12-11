/**
 * 课程列表js
 */
var adlCourseLay = {
	init : function(){
		var that = this;
		that.courseId = null;
		$("#adlCourseLay_jqGrid").jqGrid({
			url: "../adaptive/layer/courseList",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "courseId" , index: 'course_id',key: true , hidden: true},
			           { label: "编码", name: "courseNo" , index: 'course_no',hidden: false , width: 250},
			           { label: "名称", name: "courseName" , index: 'course_name',hidden: false , width: 270},
			           { label: "产品线", name: "productId" , index: 'product_id',hidden: true }
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
		   pager: "#adlCourseLay_jqGridPager",
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
			   $("#adlCourseLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
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
			content : $("#adlCourseLayDiv"),
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
		
		var adlCourseLay_CourseName = $("#adlCourseLay_CourseName").val();
		var adlCourseLay_CourseNo = $("#adlCourseLay_CourseNo").val();
		
		$("#adlCourseLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				courseName : adlCourseLay_CourseName , //课程ID
				courseNo : adlCourseLay_CourseNo //课程ID
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("adlCourseLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#adlCourseLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
        //关闭浮层
		layer.close(this.layerIndex);
		this.courseId = null;//课程ID
	}
}
adlCourseLay.init();
