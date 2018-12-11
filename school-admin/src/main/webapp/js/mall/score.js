$(function () {
   
	//
	$(".exam-Date").datetimepicker({
	 	format: 'yyyy-mm-dd',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        todayHighlight: 1,//如果为true, 高亮当前日期。
        startView: 2,//日期时间选择器打开之后首先显示的视图。
        forceParse: 0,//当选择器关闭的时候，是否强制解析输入框中的值。
        minView: 2,//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
        showMeridian: 1
	});
	//
	
	$("#jqGrid").jqGrid({
        url: '../score/list',
        datatype: "json",
        colModel: [		
        	
			{ label: 'id', name: 'id', key: true ,hidden:true},
			{ label: '学员名称', name: 'studentName', width: 80 }, 						
			{ label: '手机号', name: 'studentMobile', width: 80 }, 						
			{ label: '班级名称', name: 'className', width: 80 }, 						
			{ label: '班主任', name: 'teacherName', width: 80 },
			{ label: '课程', name: 'course', width: 80 }, 						
			{ label: '考试时间', name: 'examinationTime', width: 80 }, 						
			{ label: '录入时间', name: 'registrationTime', width: 80 }, 						
			{ label: '成绩', name: 'score', width: 80 }, 						
			{ label: '状态:0.通过,1.未通过', name: 'status', width: 80,formatter:function(value, options, row){
				var text = '';
                if(value == 0)text='<span class="label label-success">通过</span>';
                else if(value == 1) text='<span class="label label-danger">未通过</span>';	               
                return text;
			}}, 						
			{ label: '备注', name: 'remark', width: 80 }, 						
//			{ label: '创建用户', name: 'creator', width: 80 }, 						
//			{ label: '创建时间', name: 'creationTime', width: 80 }, 						
//			{ label: '修改用户', name: 'modifier', width: 80 }, 						
//			{ label: '修改时间', name: 'modifiedTime', width: 80 }, 						
//			{ label: '删除标志', name: 'dr', width: 80 }						
							
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
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
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
		//查询条件	
		q: {
            teacherName: "",
            course: "",
            studentMobile: "",
            className: "",
            status:""
        },	
		showList: true,
		title: null,
		score: {
//			userId:"",		//老师id
//			teacherName: "", //老师名称
//			courseId: "",    //课程id       
//			course: "",      //课程名称    
//			studentMobile:"",//手机号
//			studentName:"",//学员名称
//            examinationTime:"",//考试时间
//            registrationTime:"",//录入时间
            // classId:"",//班级id
			//className:""//班级名称
		}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.score = {
				courseId:"",//课程id
				course:"",  //课程名称
				userId:"",		//老师id
				teacherName: "", //老师名称
				studentMobile:"",//手机号
				studentName:"",//学员名称
				examinationTime:"",//考试时间
		        registrationTime:"",//录入时间
		        classId:"",//班级id
				className:""//班级名称
			  };
			$("#examinationtime").val("");//新增时让开课日期置空
			$("#registrationtime").val("");//新增时让开课日期置空
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {		    
			//做新增或保存时获取开课日期的值
            vm.score.examinationTime = $("#examinationtime").val();
            vm.score.registrationTime = $("#registrationtime").val();
			
			if(vm.title == "新增")
		    {
		       url = "../score/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../score/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.score),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.score.course = null;
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../score/delete",
				    data: JSON.stringify(ids),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		/*getInfo: function(id){
            $.ajax({
				type: "GET",
			    url: "/score/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.score = r.score;
					}else{
						alert(r.msg);
					}
				}
			});
		},*/
		
		getInfo: function (id) {
            $.get("../score/info/" + id, function (r) {
            	 vm.score = r.score;            	 
            	//查询信息时获取并显示考试时间和录入时间的值
            	 $("#examinationtime").val(vm.score.examinationTime); 
            	 $("#registrationtime").val(vm.score.registrationTime); 
            	 
            });
		},
		
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData: {
                    "teacherName": encodeURIComponent(vm.q.teacherName),
                    "course": encodeURIComponent(vm.q.course),
                    "studentMobile": vm.q.studentMobile,
                    "className": vm.q.className,
                    "status": vm.q.status
                },
				page:page
            }).trigger("reloadGrid");
		},
		//清空查询条件
		clearQuery : function(){
            var vm = this;
            vm.q.teacherName = "";
            vm.q.course = "";
            vm.q.studentMobile = "";
            vm.q.className = "";
            vm.q.status = "";
        },
        //课程
        courseLayerShowDetail : function(){
            courseLay.show(function(index, opt) {
                vm.q.course = opt.courseName;
            });
        },      
        //编辑-课程
		selectCourse : function(){
			courseLay.show(function(index,opt){	
				//console.log(opt);
				vm.score.courseId = opt.courseId;
				vm.score.course = opt.courseName;				
			})
		},
		//班主任
        teacherLayerShow : function(){
            teacherLay.show(function(index,opt){
                vm.q.teacherName = opt.nickName;
            });
        },
        //编辑-班主任
        selectTeacher : function(){
			teacherLay.show(function(index,opt){
				//console.log(opt);
				vm.score.userId = opt.userId;
				vm.score.teacherName = opt.nickName;
				
			});
		},
		 //学员名称信息浮层
        userInfoLayerShow1: function () {
        	userInfoLay.show(function (index, opt) {
        		//console.log(opt);
        		vm.score.userId = opt.userId;
        		vm.score.studentName = opt.nickName;
        		$("#userId").val(opt.userId);
        		$("#studentName").val(opt.nickName);
        	});
        },
		 //手机号信息浮层
		userInfoLayerShow2: function () {
            userInfoLay.show(function (index, opt) {
            	//console.log(opt);
                vm.score.userId = opt.userId;
                vm.score.studentMobile = opt.mobile;
                $("#userId").val(opt.userId);
                $("#studentMobile").val(opt.mobile);
            });
        }, 
        //编辑—班级名称
        classLayShow:function (){
        	classLay.show(function(index,opt){
        		vm.score.classId = opt.classId;
        		vm.score.className = opt.className;       		
        	})
        },
		 //下载报考模版
        downTemplate: function () {
            var urlstr = "../score/downTemplate";      
            window.location.href = urlstr;
        },
        
        //导出数据
        batchImport: function () {
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: '导入Excel',
                area: ['300px', '200px'],
                shadeClose: false,
                content: jQuery("#uploadExcel"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    $.ajaxFileUpload({
                        url: '../score/batchImport',
                        secureuri: true,
                        fileElementId: 'file_data',
                        dataType: 'json',
                        success: function (data) {
                            if (data.code == 0) {
                                alert("文件上传成功！！！" + "<br/>" + data.msg, function (index) {
                                    $("#jqGrid").trigger("reloadGrid");
                                });
                                layer.close(index);
                            } else if (data.code == 1) {
                                alert(data.msg);
                            }
                        }
                    });
                }
            });
        }
		
	}
});