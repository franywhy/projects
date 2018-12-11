$(function () {
    $("#jqGrid").jqGrid({
        url: '../record/study/list',
        datatype: "json",
        contentType: "application/x-www-form-urlencoded; charset=utf-8", 
        colModel: [		
        	
        	
        	     { label: 'recordStudyId', name: '学习信息id', key: true, hidden: true }, 
				 { label: 'classplanId', name: '排课计划id', hidden: true }, 
				 { label: 'classTeacherId', name: '班主任id',hidden: true }, 
				 { label: 'commodityId', name: '商品id',   hidden: true }, 
				 { label: 'courseId', name: '课程id',   hidden: true }, 
				 { label: 'teacherId', name: '授课老师id',   hidden: true }, 
				 { label: 'userplanDetailId', name: '学习计划子表id',  hidden: true }, 
				 { label: 'userplanId', name: '学员规划id',  hidden: true }, 
				 { label: 'orderId', name: '订单id', key: true, hidden: true }, 
        	
        	 
        	
        	
        	
        	
        	
					{ label: '学员ID', name: 'userId', width: 80 }, 						
					{ label: '姓名', name: 'name', width: 80 }, 						
					 	
					
					
					{ label: '手机号码', name: 'mobile', width: 80 }, 	
					{ label: '商品', name: 'commodityName', width: 80 }, 		
					{ label: '订单号', name: 'orderNo', width: 80 }, 	
					{ label: '学习课程', name: 'courseName', width: 80 }, 	
					{ label: '课程开始时间', name: 'startTime', width: 80 }, 	
					{ label: '任课老师', name: 'teacherName', width: 80 }, 	
					{ label: '班主任', name: 'classTeacherName', width: 80 }, 	
					{ label: '听课完成率数字', name: 'classCompletionRate', width: 80 , hidden: true }, 
					{ label: '听课完成率', name: 'classCompletionRateStr', width: 80 }, 
					
				/*	{ label: '作业达标率', name: 'tikuHomeWorkCompletionRate', width: 80 }, 	
					{ label: '零基实战', name: 'zeroBasisPracticeScore', width: 80 }, 	
					
					{ label: '期中', name: 'midtermExamScore', width: 80 }, 	
					{ label: '期末', name: 'finalExamScore', width: 80 }, 	
					{ label: '考霸1', name: 'kaobaOneScore', width: 80 }, 	
					{ label: '考霸2', name: 'kaobaTwoScore', width: 80 }, 	*/
					{ label: '统考成绩', name: 'unExaminationScore', width: 80 }, 	
					{ label: '学段', name: 'studyPart', width: 80 }, 	
				/*	{ label: '成绩类型', name: 'scoreType', width: 80 }, 	 */
					
					{ label: '成绩类型', name: 'scoreType',align : "center" ,formatter : function(value, options, row){
						if(value == 0){
							return '<span class="label label-success">非补考</span>';
						}else if(value == 1){
							return '<span class="label label-success">补考</span>';
						}else{
							return '<span class="label label-warning">成绩未登记</span>';
						}
					}},
					
					 
		 
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
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
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q: {
			userId:'',
			name:'',
			mobile:'',
 			classTeacherName:'',
 			classTeacherId:'',
		}, 

	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},  
		exportData: function(){
			hq.ui.exportData(null);
		},
		importData: function(){    //导入数据

			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '导入Excel',
				area: ['300px', '200px'],
				shadeClose: false,
				content: jQuery("#uploadExcel"),
				btn: ['确定','取消'],
				btn1: function (index) {
					$.ajaxFileUpload({
						url:'../record/recordInfo/getExcelRecordInfoImportData',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						success:function(data){
							if(data.code == 0 && data.msg != null) {
								alert("文件上传失败！！！"+"<br/>"+data.msg, function(index){
									$("#jqGrid").trigger("reloadGrid");
								});
								layer.close(index);
							}else if(data.code == 0 && data.msg == null){
                                alert("文件上传成功！！！", function(index){
                                    $("#jqGrid").trigger("reloadGrid");
                                });
                                layer.close(index);
                            }else if (data.code == 1) {
								alert(data.msg);
							}
						}
					});
	            }
			});
		
		},
        classTeacherLayerShow : function(){//班主任
            teacherLay.show(function(index,opt){
                vm.q.classTeacherId = opt.userId;
                vm.q.classTeacherName=opt.nickName;
                $('#query-classTeacherName').val(opt.nickName);
            } , 2);
        },
		exportTemplate: function (event) {  //下载模板
			var urlstr = "../record/recordInfo/exportExcelRecordInfoTemplate";
			window.location.href = urlstr;
		},
		query: function(){
			vm.reload(null , 1);
		},
		queryClear: function(){
			vm.q.userId = ''; 
			vm.q.name = ''; 
			vm.q.mobile = '';
			vm.q.classTeacherName='';
			vm.q.classTeacherId='';
		},   
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			var q = vm.q; 
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page,
                postData: { 
                'name':q.name,
                'mobile':q.mobile,
                'classTeacherId': q.classTeacherId,
                'classTeacherName':q.classTeacherName
                }
            }).trigger("reloadGrid");
		}
		
	}
});