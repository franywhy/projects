/**
 * 考勤详情弹窗js
 */
var attendanceDetailsLay = {
	init : function(){
		var that = this;
		$("#attendanceDetailsLay_jqGrid").jqGrid({
			url: "../attendanceStatistics/detailsList",
			datatype: "local",
			colModel: [
					{ label: '报名时间', name: 'registrationTime', width: 80 },
					{ label: '省份', name: 'areaName', width: 80 },
					{ label: '学员姓名', name: 'userName', width: 80 },
					{ label: '手机号码', name: 'mobile', width: 80 }, 	
					
					{ label: '学员层次', name: 'levelName', width: 80 }, 			
					{ label: '报读专业', name: 'professionName', width: 80 }, 			
					{ label: '报读班型', name: 'classTypeName', width: 80 }, 			
					{ label: '课程', name: 'courseName', width: 80 }, 
					{ label: '课次', name: 'classplanLiveName', width: 150 },
					
					{ label: '上课时间', name: 'startTime', width: 150 }, 			
					{ label: '班主任', name: 'teacherName', width: 80 }, 			
					{ label: '任课老师', name: 'teacher', width: 80 }, 			
					{ label: '直播出勤', name: 'liveAttendance', width: 80, formatter: function(value, options, row){
						return value > 0 ? 
							'已出勤' : '未出勤';
					}}, 
					{ label: '回放出勤', name: 'backAttendance', width: 80, formatter: function(value, options, row){
						return value > 0 ? 
							'已出勤' : '未出勤';
					}},
					{ label: '是否出勤', name: 'isAttendance', width: 80, formatter: function(value, options, row){
						return value > 0 ? 
							'已出勤' : '未出勤';
					}},
		     ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 30,
		   rowList : [30,50,100],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#attendanceDetailsLay_jqGridPager",
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
			   $("#attendanceDetailsLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "考勤详情列表",
	layerHeigh : '650px',
	layerWeigh : '1400px',
	classplanId:  null,
	userPlanId :  null,
	userId :  null,
	classTypeId :  null,
	show : function(classTypeId , userId , userPlanId , classplanId , scallback){
		var that = this;
		that.scallback = scallback;
		that.classplanId= classplanId;
		that.userPlanId= userPlanId;
		that.userId= userId;
		that.classTypeId= classTypeId;
		that.reload();
		that.layerIndex = layer.open({
			type : 1,//
			area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#attendanceDetailsLayDiv"),
			scrollbar : true,//是否允许浏览器出现滚动条
			fixed : false,//固定
			shadeClose : false,// 是否点击遮罩关闭
			resize : true,//是否允许拉伸
			maxmin: true, //开启最大化最小化按钮
			zIndex : 19891014,
			btn : ['返回'],
//			btn1: function (index) {
//				that.select();
//			}
		});
		
	},
	reload:function(){
		var that = this;
		$("#attendanceDetailsLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				classplanId : that.classplanId ,
				userPlanId : that.userPlanId ,
				userId : that.userId ,
				classTypeId : that.classTypeId ,
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("attendanceDetailsLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#attendanceDetailsLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		this.classplanId = null;
		this.userPlanId = null;
		this.userId = null;
		this.classTypeId = null;
	}
}
attendanceDetailsLay.init();
