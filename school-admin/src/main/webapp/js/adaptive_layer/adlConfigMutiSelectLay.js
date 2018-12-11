/**
 * 知识点难度列表js
 */
var adlConfigMutiSelectLay = {
	init : function(){
		var that = this;
		$("#adlConfigMutiSelectLay_jqGrid").jqGrid({
			url: "../adaptive/layer/knowledgeLevel",
			datatype: "local",
			colModel: [
			           { label: "主键", name: "cvalue" , index: 'cvalue',key: true , hidden: false},
			           { label: "名称", name: "cname" , index: 'cname' , width: 250},
			           { label: "备注", name: "remark" , index: 'remark', hidden: true}
		           ],
		   viewrecords: true,
		   height: 320,
		   rowNum: 100,
		   rowList : [10,30,50,100],
		   rownumbers: true, 
//		   rownumWidth: 35, 
		   width:500,
		   autowidth:true,
		   multiselect: true,
		   pager: "#adlConfigMutiSelectLay_jqGridPager",
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
//			   that.select();
		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#adlConfigMutiSelectLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
			   if(null != that.selectIds && that.selectIds.length > 0){
				   $.each(that.selectIds , function(index , element){
					   $("#adlConfigMutiSelectLay_jqGrid").jqGrid('setSelection',element);
				   });
			   }
		   }
		});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	title:null,//标题
	selectIds : null,//默认选中ID
	knowldgeLevelShow : function(selectIds , scallback){
		var that = this;
		that.selectIds = selectIds || new Array();
		that.scallback = scallback || null;
		that.url = "../adaptive/layer/knowledgeLevel";
		that.title = "知识点难度列表";
		that.show();
	},
	knowledgeQuestionTypeShow : function(selectIds,scallback){
		var that = this;
		that.selectIds = selectIds || new Array();
		that.scallback = scallback || null;
		that.url = "../adaptive/layer/knowledgeQuestionType";
		that.title = "知识点题型列表";
		that.show();
	},
	knowledgeKeyPointShow : function(selectIds , scallback){
		var that = this;
		that.selectIds = selectIds || new Array();
		that.scallback = scallback || null;
		that.url = "../adaptive/layer/knowledgeKeyPoint";
		that.title = "知识点考点类型列表";
		that.show();
	},
	show : function(){
		var that = this;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : ['600px','550px'],
			title :that.title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#adlConfigMutiSelectLayDiv"),
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
		console.log(that.url);
		var adlConfigMutiSelectLay_cname = $("#adlConfigMutiSelectLay_cname").val();
		$("#adlConfigMutiSelectLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				cname : adlConfigMutiSelectLay_cname 
            },
            url : that.url,
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var rowIdList = getJqGridSelectedRows("adlConfigMutiSelectLay_jqGrid");
		if(rowIdList == null || rowIdList.length == 0){
			return ;
		}
		//行数据
		var rowDataList = new Array();
		$.each(rowIdList , function(index,element){
			var rowData = $("#adlConfigMutiSelectLay_jqGrid").jqGrid('getRowData',element);
			rowDataList.push(rowData);
		});
		if($.isFunction(this.scallback)){
			this.scallback(rowIdList , rowDataList);
		}
        //关闭浮层
		layer.close(this.layerIndex);
	}
}
adlConfigMutiSelectLay.init();