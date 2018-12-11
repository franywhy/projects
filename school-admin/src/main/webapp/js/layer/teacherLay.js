/**
 * 授课老师弹窗js
 */
var teacherLay = {
	init : function(){
		$("#qteacherName").val("");
		var that = this;
		$("#teacherLay_jqGrid").jqGrid({
			url: "../layerdata/teacherList",
			datatype: "local",
			colModel: [
	           { label: "主键", name: "userId" , key: true , hidden: true},
	           { label: "授课老师", name: "nickName" , width: 100	  },
	           
				{ label: '邮箱', name: 'email', width: 150 },
				{ label: '手机号', name: 'mobile', width: 150 },
				{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
					return value === 0 ? '<span class="label label-danger">禁用</span>' : '<span class="label label-success">正常</span>';
				}},
				{ label: '班主任', name: 'classTeacher', width: 80, formatter: function(value, options, row){
					return value === 1 ? '<span class="label label-success">是</span>' : '<span class="label label-danger">否</span>';
				}},
				{ label: '教学老师', name: 'teacher', width: 80, formatter: function(value, options, row){
					return value === 1 ? '<span class="label label-success">是</span>' : '<span class="label label-danger">否</span>';
				}},
				{ label: '创建时间', name: 'createTime', width: 80} 
		    ],
		   viewrecords: true,
		   height: 400,
		   rowNum: 10,
		   rowList : [10,30,50],
		   rownumbers: true, 
		   rownumWidth: 25, 
		   autowidth:true,
		   multiselect: false,
		   pager: "#teacherLay_jqGridPager",
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
			   $("#teacherLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
			});
	},
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	teacher:null,//授课老师
	classTeacher:null,//班主任
	show : function(scallback , type){
		var that = this;
		that.scallback = scallback;
		if(2 === type){
			that.classTeacher = 1;
		}else{
			that.teacher = 1;
		}
		that.reload();
		var that = this;
		var title = "授课老师";
		if(1 == that.classTeacher){
			title = '班主任'
		}
		
		that.layerIndex = layer.open({
			type : 1,//
			area: ['800px', '650px'],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#teacherLayDiv"),
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
		$("#teacherLay_jqGrid").jqGrid('setGridParam',{ 
			postData : {
				nickName : $("#qteacherName").val() , //授课老师名称
				teacher : that.teacher , 
				classTeacher : that.classTeacher  
            },
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){
		var selectDetail = getJqGridSelectedRow("teacherLay_jqGrid");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#teacherLay_jqGrid").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
		$("#qteacherName").val("");
	}
}
teacherLay.init();
