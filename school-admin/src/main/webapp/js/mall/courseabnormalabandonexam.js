$(function () {
    $("#jqGrid").jqGrid({
        url: '../courseabnormalabandonexam/list',
        datatype: "local",
        colModel: [		
				{ label: '序', name: 'id', key: true ,width: 60},						
				{ label: '单据号', name: 'invoicesNumber', width: 80 }, 			 										
				{ label: '订单编号', name: 'orderNo', width: 80 }, 						
				{ label: '班型名称', name: 'classTypeName', width: 80 }, 						
				{ label: '课程名称', name: 'courseName', width: 80 }, 						
				{ label: '报读省份', name: 'applyProvince', width: 80 }, 						
				{ label: '产品线id', name: 'productId', width: 80,hidden:true }, 						
				{ label: '产品线名称', name: 'productName', width: 80 }, 						
				{ label: '报考单号', name: 'registrationNo', width: 80 },
				{ label: '报考省份', name: 'registerProvince', width: 80 },
				{ label: '专业', name: 'professionName', width: 80 },
				{ label: '层次', name: 'levelName', width: 80 },
				{ label: '班级名称', name: 'className', width: 80 },
				{ label: '班主任', name: 'teacherName', width: 80 },
				{ label: '学员姓名', name: 'studentName', width: 80 },
				{ label: '手机号', name: 'mobile', width: 80 },
				//{ label: '考试时段', name: 'scheduleName', width: 80 },			
				{ label: '考试时间', name: 'scheduleDate', width: 80 },			
				
				{ label: '状态 ', name: 'status', width: 80,formatter: function(value, options, row){
					var value = row.status;
					var text = '';
						if(value == 0)text='待审核';
		                else if(value == 1) text='作废';
		                else if(value == 2)text='通过';
		                return text; 
						/*if(value == 0)text='<span class="label label-default">待审核</span>';
		                else if(value == 1) text='<span class="label label-danger">作废</span>';
		                else if(value == 2)text='<span class="label label-success">通过</span>';
		                return text;*/ 
				} }, 							
				{ label: '原因', name: 'reason', width: 80 }, 						
				{ label: '备注', name: 'remark', width: 80 }, 						
										
							
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
		courseAbnormalAbandonExam: {},
		//查询条件
		q:{			
			registrationNo:"",
			//班型名称
			classTypeId:"",
			classTypeName:"",
			//班级名称
			classId:"",
			className:"",
			//班主任名称
			userId:"",
			teacherName:"",
			//课程名称
			courseId:"",
			courseName:"",
			//报考省份
			areaId:"",
			registerProvince:"",
			applyProvince:"",
			//专业
			professionId:"",
			professionName:"",
			//考试时间段
			scheduleId:"",
			scheduleName:"",
			//学员名称
			id:"",
			studentName:"",
			//手机号
			mobile:"",
			//层次
			levelId:"",
			levelName:"",
			//状态
			status:-1
		},
		
        
        //状态
        statusOptionsSelect: [
 			{ text: '全部',value: -1 },
 			{ text: '待审核', value: 0 },
 			{ text: '作废', value: 1 },
 			{ text: '通过', value: 2 }
 		]
 		
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		//重置查询条件
		clearQuery: function (event) {
	            vm.q = {	            		
	            		//班型名称
	        			classTypeId:"",
	        			classTypeName:"",
	        			//班级名称
	        			classId:"",
	        			className:"",
	        			//班主任名称
	        			userId:"",
	        			teacherName:"",
	        			//课程名称
	        			courseId:"",
	        			courseName:"",
	        			//报考省份
	        			areaId:"",
	        			registerProvince:"",
	        			applyProvince:"",
	        			//专业
	        			professionId:"",
	        			professionName:"",
	        			//考试时间段
	        			scheduleId:"",
	        			scheduleName:"",
	        			//学员名称
	        			id:"",
	        			studentName:"",
	        			//手机号
	        			mobile:"",
	        			//层次
	        			levelId:"",
	        			levelName:"",
	        			status:-1
	        			
	            }
	    },
	    
	    
	    //导入模板
	    downTemplate: function (event) {
            var urlstr = "../courseabnormalabandonexam/downTemplate";
            window.location.href = urlstr;
        },
		add: function(){
			vm.showList = false;			
			vm.title = "新增";
			vm.courseAbnormalAbandonExam = {				
					//报考单号					
					registrationNo:"",
					orderNo:"",
					//课程名称
					courseId:"",
					courseName:"",
					//报考省份
					bkAreaId:"",
					registerProvince:"",					
					//考试时间段
					scheduleId:"",
					scheduleName:"",
					scheduleDate:"",
					remark:"",			//备注
					reason:"", 			//原因
					registrationId:"",
					productId:""
			};
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
		    if(vm.title == "新增")
		    {
		       //vm.courseAbnormalAbandonExam.status = 0;//默认待审核
		       url = "../courseabnormalabandonexam/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../courseabnormalabandonexam/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseAbnormalAbandonExam),
			    success: function(r){
			    	if(r.code === 0){
						alert(r.data ||'操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		//作废
		cancel: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			 	var errorMsg = '以下审核通过订单不允许作废:';
	            var errorFlag = false;
	            $.each(ids,function (index,id) {	               
	            	var rowData = $("#jqGrid").jqGrid('getRowData',id);	        
	                if(rowData.status == '通过'){
	                    errorMsg += rowData.invoicesNumber+";";
	                    errorFlag = true;
	                }
	            })
	            if(errorFlag){
	                alert(errorMsg);	                
	                return;
	            }
	        
	        confirm('确定要作废选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseabnormalabandonexam/cancel",
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
		//反审核
		antiAudit: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			 var errorMsg = '以下作废的订单不允许进行处理：';
	            var errorFlag = false;
	            $.each(ids,function (index,id) {
	                var rowData = $("#jqGrid").jqGrid('getRowData',id);               
	                if(rowData.status == '作废'){
	                    errorMsg += rowData.invoicesNumber+";";
	                    errorFlag = true;
	                }
	            })
	            if(errorFlag){
	                alert(errorMsg);
	                return;
	            } 
	            
			
			$.ajax({
				type: "POST",
				url: "../courseabnormalabandonexam/antiAudit",
				data: JSON.stringify(ids),
				contentType: "application/json",
				success: function(r){
					if(r.code == 0){
            			if(r.data){
            				 var re = new RegExp(";","g"); //定义正则表达式
                             r.data = r.data.replace(re, "<br>");
            			}
            			alert(r.data || '操作成功！');
            				$("#jqGrid").trigger("reloadGrid");           			
            		}else{
            			alert(r.msg);
            		}
				}
			});			
		},
		//审核
		pass: function (event) {
			var ids = getSelectedRows();
            if(ids == null){
                return ;
            }                               
            var errorMsg = '已作废的订单不允许进行处理：';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);               
                if(rowData.status == '作废'){
                    errorMsg += rowData.invoicesNumber+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }            
           
            $.ajax({
            	type: "POST",
            	url: "../courseabnormalabandonexam/pass",
            	data: JSON.stringify(ids),
            	contentType: "application/json",
            	success: function(r){
            		if(r.code == 0){
            			if(r.data){
            				 var re = new RegExp(";","g"); //定义正则表达式
                             r.data = r.data.replace(re, "<br>");
            			}
            			alert(r.data || '操作成功！');
            				$("#jqGrid").trigger("reloadGrid");           			
            		}else{
            			alert(r.msg);
            		}
            	}
            });
            
        },
        
		getInfo: function (id) {
            $.get("../courseabnormalabandonexam/info/" + id, function (r) {
            	 vm.courseAbnormalAbandonExam = r.courseAbnormalAbandonExam;            	            	  	 
            });
		},
		
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData: {
                    "classTypeName": encodeURIComponent(vm.q.classTypeName),
                    "className": encodeURIComponent(vm.q.className),
                    "teacherName": encodeURIComponent(vm.q.teacherName),
                    "courseName": encodeURIComponent(vm.q.courseName),
                    "applyProvince": encodeURIComponent(vm.q.applyProvince),
                    "registerProvince": encodeURIComponent(vm.q.registerProvince),
                    "professionName": encodeURIComponent(vm.q.professionName),
                    "scheduleName": vm.q.scheduleName,
                    "studentName": encodeURIComponent(vm.q.studentName),
                    "mobile": vm.q.mobile,
                    "levelName": vm.q.levelName,
                    "status": vm.q.status
                },
                datatype: "json",
                page:page
            }).trigger("reloadGrid");
		},
		//导入数据		     
	    importExcel : function () {
	            $("#fileUploadLayer").val("");
	            layer.open({
	                type : 1,//
	                area : ['350px','200px'],
	                title :"批量导入",
	                closeBtn : 1,
	                skin:"layui-layer-lan",
	                content : $("#fileUploadLayer"),
	                scrollbar : true,//是否允许浏览器出现滚动条
	                fixed : false,//固定
	                shadeClose : false,// 是否点击遮罩关闭
	                resize : true,//是否允许拉伸
	                maxmin: true, //开启最大化最小化按钮
	                zIndex : 19891014,
	                btn : ['确认','取消' ],
	                btn1: function (index) {
	                    var file = $('#file_data').val();
	                    if(file){
	                        var fileType = file.substring(file.indexOf('.'),file.length);
	                        if(fileType != '.xls'){
	                            alert('文件类型必须为.xls');
	                            return;
	                        }
	                    }else{
	                        alert('请选择文件');
	                        return;
	                    }
	                    $.ajaxFileUpload({
	                        url:'../courseabnormalabandonexam/importExcel',
	                        secureuri:true,
	                        fileElementId:'file_data',
	                        // dataType:'json',
	                        success:function(data){
	                            console.log(data);
	                            if(data.code == 0) {
	                                alert(data.data ||"批量导入成功", function(alertIndex){
	                                    vm.reload();
	                                    layer.close(index);
	                                });
	                            }else{
	                                alert(data.msg);
	                            }
	                        }
	                    });
	                }
	            });
	        },
		//查询条件 层次	     
	    levelLayShow : function(){
	    	levelLay.show(function(index,opt){	    		
	    		//vm.q.levelId = opt.levelId;
	    		vm.q.levelName = opt.levelName;
	    	});
	    },
	    //查询条件 报考时间段	     
	    examScheduleLayShow : function(){
	    	examScheduleLay.show(function(index,opt){	    		
	    		//vm.q.scheduleId = opt.id;
	 	    	vm.q.scheduleName = opt.scheduleName;
	 	    	});
	 	    },
	    //查询条件 专业	     
	    professionLayerShow : function(){
	    	professionLay.show(function(index,opt){	    		
	 	    	//vm.q.professionId = opt.porfessionId
 	    		vm.q.professionName = opt.professionName;
	 	    	});
	 	    },
	    //查询条件 报读省份	     
	    areaLayerShowA : function(){
	    	 areaLay.show(function(index,opt){	    		 
	    		//vm.q.areaId = opt.areaId;	 
	    		 console.log(opt);
	    		 vm.q.applyProvince = opt.areaName;
	 	    	});
	 	    },
	 	//查询条件 报考省份	     
	 	areaLayerShowB : function(){
	 	    	areaLay.show(function(index,opt){	    		 
	 	    	//vm.q.areaId = opt.areaId;	 	    		
	 	    	 vm.q.registerProvince = opt.areaName;
	 	    	});
	 	    },
	 	    
	    //查询条件 班型名称	     
	    classTypeLayerShow : function(){
	    	classTypeLay.show(function(index,opt){		    		
	    		//vm.q.classTypeId = opt.classtypeId;
	 	    	vm.q.classTypeName = opt.classtypeName;
	 	    	});
	 	 },
	    //查询条件 班级名称
	    classLayerShow : function(){
	    	classLay.show(function(index,opt){
	    		//vm.q.classId = opt.classId;
	    		vm.q.className = opt.className;
	    	});
	    },
	    //查询条件 课程
	    courseLayerShow : function(){
            courseLay.show(function(index, opt) {        
            	//vm.q.courseId = opt.courseId
            	vm.q.courseName = opt.courseName;
            });
        },
	    //查询条件老师
	    teacherLayerShow : function(){
            teacherLay.show(function(index,opt){           	
                vm.q.teacherName = opt.nickName;
            });
        },
		
		//选择报考单号
		selRegister:function(){
			registrationLay.show(function(index,opt){
	            console.log(opt);
	         	  	 	                   
	        //报考省份
			vm.courseAbnormalAbandonExam.bkAreaId = opt.bkAreaId;		
			vm.courseAbnormalAbandonExam.registerProvince = opt.bkAreaName;								
			//订单号
			vm.courseAbnormalAbandonExam.orderNo = opt.orderNo;		
			//报考单号
			vm.courseAbnormalAbandonExam.registrationNo = opt.registrationNo;
			//课程名称
			vm.courseAbnormalAbandonExam.courseId = opt.courseId;
			vm.courseAbnormalAbandonExam.courseName = opt.courseName;
			//报考时间段
			vm.courseAbnormalAbandonExam.scheduleId = opt.scheduleId;									
			vm.courseAbnormalAbandonExam.scheduleName = opt.scheduleName;
			vm.courseAbnormalAbandonExam.scheduleDate = opt.scheduleDate;
	        //报考id
			vm.courseAbnormalAbandonExam.registrationId = opt.id;
			vm.courseAbnormalAbandonExam.productId = opt.productId;
			});
		}
	
	}
});