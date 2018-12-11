/**
 * 节列表js
 */
var chapterListLay = {
	init : function(){
		var that = this;
		that.courseId = null;
		$("#chapterListLay_jqGrid").jqGrid({
			url: "../adaptive/layer/chapterList",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "chapterId" , key: true , hidden: true},
			           { label: "编码", name: "chapterNo" , hidden: false , width: 250},
			           { label: "名称", name: "chapterName" , hidden: false , width: 350},
			           { label: "排序", name: "orderNum" , hidden: true , width: 50}
		           ],
		   viewrecords: true,
		   height: 250,
		   rowNum: 100,
		   rowList : [10,30,50,100],
		   rownumbers: true, 
//		   rownumWidth: 35, 
		   width:500,
		   autowidth:true,
		   multiselect: false,
		   pager: "#chapterListLay_jqGridPager",
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
			   $("#chapterListLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	courseId : null,//课程ID
	show : function(courseId , scallback){
		var that = this;
		that.scallback = scallback;
		that.courseId = courseId;
		that.reload();
			var that = this;
			var title = "节列表";
			that.layerIndex = layer.open({
				type : 1,//
				area : ['650px','550px'],
				title :title,
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#chapterListLayDiv"),
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
		$("#chapterListLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				courseId : that.courseId , //课程ID
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("chapterListLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#chapterListLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
        //关闭浮层
		layer.close(this.layerIndex);
		this.courseId = null;//课程ID
	}
}
chapterListLay.init();
