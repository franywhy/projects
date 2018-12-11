/**
 * 学员规划弹窗js
 */
var userplanLay = {
	init : function(){
		$("#qorderNo").val("");//订单编号
		$("#quserMobile").val("");//学员手机号
		$("#qclassName").val("");//班级名称
		$("#qteacherName").val("");//班主任
		$("#qexamScheduleId").val("");//考试时段id
		$("#qexamScheduleName").val("");//考试时段名称
		var that = this;
		$("#userplanLay_jqGrid").jqGrid({
			url: "../layerdata/userplanList",
			datatype: "local",
			colModel: [
				{ label: "学员规划pk", name: "userPlanId",hidden:true, key: true},
                { label: '班型PK', name: 'classTypeId', hidden: true },
                { label: '订单ID', name: 'orderId', hidden : true}, 
                { label: "学员pk", name: "userId",hidden:true, width: 280  },
                { label: '最近考试时段PK', name: 'examScheduleId', hidden : true },
                
                { label: '订单编号', name: 'orderNo', width: 150 }, 
                { label: '学员', name: 'userName', width: 130 }, 
                { label: '学员手机', name: 'userMobile', width: 150 }, 
                { label: '商品', name: 'commodityName', width: 150 }, 
                { label: '专业', name: 'professionName', width: 80 },
                { label: '省份', name: 'areaName', width: 80 },
                { label: '班级', name: 'className', width: 80 }, 
                { label: '班型', name: 'classTypeName', width: 80 }, 			
                { label: '层次', name: 'levelName', width: 80 }, 
                { label: '班主任', name: 'teacherName', width: 80 }, 
                
                { label: '可代替课程', name: 'isRep', width: 80 , formatter: function(value, options, row){
       				return value === 0 ? '<span class="label label-success">学习</span>' : '<span class="label label-danger">不学习</span>';
       		   }}, 			
                { label: '专业对口', name: 'isMatch', width: 80 , formatter: function(value, options, row){
       				return value === 0 ? '<span class="label label-success">对口</span>' : '<span class="label label-danger">不对口</span>';
       		   }}, 			
                { label: '学员规划状态', name: 'userplanStatus', width: 80 , formatter: function(value, options, row){
             	   var str = '';
             	   if(value === 0){
             		   str = '<span class="label label-success">正常</span>';
             	   }else if(value === 1){
             		   str = '<span class="label label-danger">毕业</span>';
             	   }else if(value === 2){
             		   str = '<span class="label label-danger">休学</span>';
             	   }
             	   return str;
       		   }}, 			
                { label: '毕业时间', name: 'graduateTime', hidden:true }, 			
                { label: '最近考试时段', name: 'examScheduleName', hidden:true }
						],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 35, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#userplanLay_jqGridPager",
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
			   $("#userplanLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
		});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		this.reload();
		var title = "学员规划列表";
		that.layerIndex = layer.open({
				type : 1,//
				//area : "auto",
				area: ['1341px', '650px'],
				title :title,
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#userplanLayDiv"),
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
		$("#userplanLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				orderNo : $("#qorderNo").val() , //订单编号
				userMobile : $("#quserMobile").val() , //学员手机号
				className : $("#qclassName").val() , //班级名称
				teacherName : $("#qteacherName").val() , //班主任
				examScheduleId : $("#qexamScheduleId").val()//考试时段id
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("userplanLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#userplanLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qorderNo").val("");//订单编号
		$("#quserMobile").val("");//学员手机号
		$("#qclassName").val("");//班级名称
		$("#qteacherName").val("");//班主任
		$("#qexamScheduleId").val("");//考试时段id
		$("#qexamScheduleName").val("");//考试时段名称
	},
	selExamSchedule:function(){//选择考试时段
		examScheduleLay.show(function(index,opt){
			console.log(opt);
			//考试时段id
			$("#qexamScheduleId").val(opt.id);
//			//考试时段名称
			$("#qexamScheduleName").val(opt.scheduleName);
		});
	},
	clearQuery : function(){//清空查询条件
		$("#qorderNo").val("");//订单编号
		$("#quserMobile").val("");//学员手机号
		$("#qclassName").val("");//班级名称
		$("#qteacherName").val("");//班主任
		$("#qexamScheduleId").val("");//考试时段id
		$("#qexamScheduleName").val("");//考试时段名称
	}
}
userplanLay.init();
