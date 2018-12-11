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
        url: '../registration/list',
        datatype: "json",
        colModel: [		        	
			{ label: 'id', name: 'id', key: true,hidden:true },
			{ label: '学员名称', name: 'studentName', width: 80 }, 						
			{ label: '手机号', name: 'studentMobile', width: 80 }, 						
			{ label: '班级名称', name: 'className', width: 80 }, 						
			{ label: '班主任', name: 'teacherName', width: 80 }, 						
			{ label: '课程', name: 'course', width: 80 }, 						
			{ label: '考试时间', name: 'examinationTime', width: 80 }, 						
			{ label: '登记时间', name: 'registrationTime', width: 80 }, 						
		    { label: '报名省份', name: 'applyProvince', width: 80 }, 						
			{ label: '报考省份', name: 'registrationProvince', width: 80 }, 						
			{ label: '备注', name: 'remark', width: 80 }						
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
		showList: true,
		title: null,
		registration: {
			//courseId:"",//课程id
            //course: "",  //课程名称         
//            userId:"",   //老师id
//            teacherName: "", //老师名称
//            examinationTime:"",//考试时间
//            registrationtime:"",//登记时间
//            areaId:"",		//省份id
//			applyProvince:"",//报名省份名称
//			registrationProvince:"", //报考省份名称
//			studentMobile:"",//手机号
//			studentName:"",//学员名称
//			examinationTime:"",//考试时间
//		    registrationTime:"",//登记时间
		    //classId:"",//班级id
		    //className:""//班级名称
		},
		q: {//查询条件
		    teacherName: "",
		    course: "",
		    studentMobile: "",
		    className: ""
		}
	},
		
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.registration = {
					courseId:"",//课程id
					course:"",   //课程名称
					userId:"",		//老师id
					teacherName: "", //老师名称
					examinationTime:"",//考试时间
		            registrationTime:"",//登记时间
					areaId:"",		//省份id
					applyProvince:"",//报名省份名称
					registrationProvince:"", //报考省份名称
					studentMobile:"",//手机号
					studentName:"",//学员名称
					examinationTime:"",//考试时间
				    registrationTime:"",//登记时间
				    classId:"",//班级id
				    className:""//班级名称
			};
			$("#examinationtime").val("");//新增时让考试时间置空
			$("#registrationtime").val("");//新增时让登记时间置空
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
			vm.registration.examinationTime = $("#examinationtime").val();
            vm.registration.registrationTime = $("#registrationtime").val();			
                     	                                    
			if(vm.title == "新增")
		    {
		       url = "../registration/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../registration/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.registration),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							
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
				    url: "../registration/delete",
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
				type: "POST",
			    url: "/registration/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.registration = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},*/
		
		getInfo: function (id) {
            $.get("../registration/info/" + id, function (r) {
            	 vm.registration = r.registration;            	 
            	//查询信息时获取并显示考试时间和登记时间的值
            	 $("#examinationtime").val(vm.registration.examinationTime); 
            	 $("#registrationtime").val(vm.registration.registrationTime);         	 
            });
		},
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				//postData: vm.q,
				postData: {
                    "teacherName": encodeURIComponent(vm.q.teacherName),
                    "course": encodeURIComponent(vm.q.course),
                    "studentMobile": vm.q.studentMobile,
                    "className": vm.q.className
                },
				page:page
            }).trigger("reloadGrid");
		},
		clearQuery : function(){//清空查询条件
            var vm = this;
            vm.q.teacherName = "";
            vm.q.course = "";
            vm.q.studentMobile = "";
            vm.q.className = "";
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
				vm.registration.courseId = opt.courseId;
				vm.registration.course = opt.courseName;				
			});
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
				vm.registration.userId = opt.userId;
				vm.registration.teacherName = opt.nickName;
				
			});
		},
        //选择报名省份
		selectArea1 : function(){
			areaAllLay.show(function(index,opt){
				vm.registration.areaId = opt.areaId;
				vm.registration.applyProvince = opt.areaName;   //报名省份				
			})
		},
		//选择报考省份
		selectArea2 : function(){
			areaAllLay.show(function(index,opt){
				vm.registration.areaId = opt.areaId;
				vm.registration.registrationProvince = opt.areaName;//报考省份
			})
		},
		
        //手机号信息浮层
		userInfoLayerShow2: function () {
            userInfoLay.show(function (index, opt) {
            	//console.log(opt);
                vm.registration.userId = opt.userId;
                vm.registration.studentMobile = opt.mobile;
                $("#userId").val(opt.userId);
                $("#studentMobile").val(opt.mobile);
            });
        },
        //学员名称信息浮层
        userInfoLayerShow1: function () {
        	userInfoLay.show(function (index, opt) {
        		//console.log(opt);
        		vm.registration.userId = opt.userId;
        		vm.registration.studentName = opt.nickName;
        		$("#userId").val(opt.userId);
        		$("#studentName").val(opt.nickName);
        	});
        },
        //编辑—班级名称
        classLayShow:function (){
        	classLay.show(function(index,opt){
        		console.log(opt);
        		vm.registration.classId = opt.classId;
        		vm.registration.className = opt.className;       		
        	})
        },
        //下载模版
        downTemplate: function () {
        	var urlstr = "../registration/downTemplate";      
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
                        url: '../registration/batchImport',
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