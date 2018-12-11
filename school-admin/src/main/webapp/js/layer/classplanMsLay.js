/**
 * 排课计划js
 */
var classplanMsLay = {
	init : function(){
		var that = this;
		$("#classplanMsLay_jqGrid").jqGrid({
			url: "../layerdata/classplanList",
			datatype: "local",
			colModel: [
					{ label: '排课计划Id', name: 'classplanId', hidden: true, width: 50, key: true },
				           
					{ label: '排课计划名称', name: 'classplanName', width: 200 },
					{ label: '课程名称', name: 'courseName', width: 200 },
					{ label: '开课日期', name: 'startTime', width: 200 },
					{ label: '授课老师', name: 'teacherName', width: 150 },
					{ label: '状态', name: 'status', width: 150 ,formatter: function(v, options, row){
						var value = row.status;
						var text = '';
						if(value == 0)text= '<span class="label label-danger">作废</span>';
						else if(value == 1)text= '<span class="label label-success">正常</span>';
						else if(value == 2)text= '<span class="label label-danger">结课</span>';
						return text;
					}},
					{ label: '审核状态', name: 'isAudited', width: 150, formatter: function(value, options, row){
						return value === 0 ? 
							'<span class="label label-danger">未通过</span>' : 
							'<span class="label label-success">通过</span>';
					}},
		    ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: true,
		   pager: "#classplanMsLay_jqGridPager",
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
			   $("#classplanMsLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
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
		var title = "排课列表";
		that.layerIndex = layer.open({
				type : 1,//
				//area : "auto",
				area: ['1101px', '600px'],
				title :title,
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#classplanMsLayDiv"),
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
	reload:function(){//刷新排课列表
		$("#classplanMsLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
                classplanName : $("#qClassplanName").val() , //排课名称
            	},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
        var selectIds = getJqGridSelectedRows("classplanMsLay_jqGrid");
        if(selectIds == null){
            return ;
        }
        var selectDetails = [];
        selectIds.forEach(function(value, index, array) {
            selectDetails.push($("#classplanMsLay_jqGrid").jqGrid('getRowData',value));
        });
        if($.isFunction(this.scallback)){
            this.scallback(selectIds , selectDetails);
        }
        //关闭浮层
		layer.close(this.layerIndex);
	}
}
classplanMsLay.init();
