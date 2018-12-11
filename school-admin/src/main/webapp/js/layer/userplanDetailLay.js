/**
 * 学员规划详情弹窗js
 */
var userplanDetailLay = {
	init : function(){
		$("#qcourseNo").val("");//课程编号
		$("#qcourseName").val("");//课程名称
		var that = this;
		$("#userplanDetailLay_jqGrid").jqGrid({
			url: "../course/userplan/userplanDetailList",
			datatype: "local",
			colModel: [
				{ label: 'id', name: 'userplanDetailId', key: true, width: 80 },
                { label: 'areaId', name: 'areaId',hidden: true},
                { label: 'classId', name: 'classId',hidden: true},
                { label: 'userId', name: 'userId',hidden: true},
                { label: 'sourceType', name: 'sourceType',hidden: true},
                { label: 'courseId', name: 'courseId',hidden: true},
                { label: '课程编号', name: 'courseNo', width: 120 },
                { label: '课程名称', name: 'courseName', width: 250 }, 			
                { label: '是否通过', name: 'isPass', width: 120 , formatter: function(value, options, row){
    				return value === 1 ? '<span class="label label-success">通过</span>' : '<span class="label label-danger">未通过</span>';
    				}}, 			
                { label: '可代替课程', name: 'isSubstituted', width: 120 , formatter: booleanFormat}, 			
                { label: '代替课程', name: 'isSubstitute', width: 120 , formatter: booleanFormat}, 			
                { label: '专业不对口课程', name: 'isSuitable', width: 120 , formatter: booleanFormat}, 			
                { label: '全国统考', name: 'isUnitedExam', width: 120 , formatter: booleanFormat }
					  ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 35, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#userplanDetailLay_jqGridPager",
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
			   $("#userplanDetailLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
		});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		this.reload();
		var title = "学员规划详情列表";
		that.layerIndex = layer.open({
				type : 1,//
				//area : "auto",
				area: ['1141px', '650px'],
				title :title,
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#userplanDetailLayDiv"),
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
		$("#userplanDetailLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				courseNo : $("#qcourseNo").val() , //课程编号
				courseName : $("#qcourseName").val() , //课程名称
				userPlanId : vm.detail.userplan.userPlanId
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("userplanDetailLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#userplanDetailLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qcourseNo").val("");//课程编号
		$("#qcourseName").val("");//课程名称
	},
	clearQuery : function(){//清空查询条件
		$("#qcourseNo").val("");//课程编号
		$("#qcourseName").val("");//课程名称
	}
}
userplanDetailLay.init();
