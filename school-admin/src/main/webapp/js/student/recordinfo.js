$(function () {
    $("#jqGrid").jqGrid({
        url: '../record/recordInfo/list',
        datatype: "json",
        contentType: "application/x-www-form-urlencoded; charset=utf-8", 
        colModel: [		
        	
					{ label: 'id', name: 'recordId', key: true, hidden: true },

					{ label: 'ncStudentId', name: 'ncStudentId',   hidden: true },
					{ label: '学员ID', name: 'userId', width: 80 }, 						
					{ label: '姓名', name: 'name', width: 80 }, 						
					 	
					
					{ label: '性别', name: 'sex', align : "center",width: 80 ,formatter : function(value, options, row){
						if(value == 0){
							return '<span class="label label-success">女</span>';
						}else if(value == 1){
							return '<span class="label label-success">男</span>';
						} else if(value == 2){
							return '<span class="label label-success">保密</span>';
						}  else {
							return '<span class="label label-success">保密</span>';
						}
					}
				},
					{ label: '身份证', name: 'idCard', width: 80 ,formatter : function(value, options, row){
						if(value == null){
							return '--';
						}else  {
							return value;
						}
					}},						
					{ label: '年龄', name: 'ageStr', width: 80,formatter : function(value, options, row){
						if(value == null){
							return '--';
						}else  {
							return value;
						}
					} }, 						
					{ label: '学历', name: 'record', width: 80 ,formatter : function(value, options, row){
						if(value == null){
							return '--';
						}else  {
							return value;
						}
					}}, 						
					{ label: 'QQ', name: 'qq', width: 80 }, 	
					{ label: '手机号码', name: 'mobile', width: 80 }, 	
				 
					{ label: '是否结婚', name: 'marriageStatus', align : "center",width: 80 ,formatter : function(value, options, row){
							if(value == 1){
								return '<span class="label label-success">是</span>';
							}else if(value == 0){
								return '<span class="label label-success">否</span>';
							} 
						}
					},
					{ label: '是否生育', name: 'fertilityStatus', align : "center",width: 80 ,formatter : function(value, options, row){
						if(value == 1){
							return '<span class="label label-success">是</span>';
						}else if(value == 0){
							return '<span class="label label-success">否</span>';
						} 
					}
				},
				{ label: '每天可学习时间(min)', name: 'studyTimeOfDay', width: 80 }, 	
				{ label: '现工作岗位', name: 'postName', width: 80 }, 	
				{ label: '会计类证书', name: 'accountingCertificates', width: 80 }, 	
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
			teacherId:'',
			teacherName:'',
 			 
		}, 
		info:{
			ncStudentId:'',
			userId:'',
			recordId:'',
			name:'',
			sex:'',
			idCard:'',
			age:'',
			qq:'',
			marriageStatus:'',
			fertilityStatus:'',
			postName:'',
			accountingCertificates:'',
			studyTimeOfDay:'',
	 
		}

	},
	methods: {
		query: function () {
			vm.reload(null,1);
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
		getInfo: function(id){
            $.ajax({
				type: "POST",
			    url: "../record/recordInfo/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.info = r.recordInfo;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		saveOrUpdate: function (event) {
			console.log(vm.info.recordId);
		    if(vm.info.recordId == null || vm.info.recordId == "")
		    {
		       url = "../record/recordInfo/save";
		    }
		    else if(vm.info.recordId != "")
		    {
		       url = "../record/recordInfo/update";
		    }else
		    {
		       url = "";
		    }
		    
		    var param = {};
		    param.recordId = vm.info.recordId;
		    param.userId=vm.info.userId;
		    param.ncStudentId=vm.info.ncStudentId;
		    param.name = vm.info.name;
		    param.sex = vm.info.sex;
		    param.idCard = vm.info.idCard;
		    param.age = vm.info.age;
		    param.qq= vm.info.qq;
		    param.marriageStatus = vm.info.marriageStatus;
		    param.fertilityStatus = vm.info.fertilityStatus;
		    param.postName = vm.info.postName;
		    param.accountingCertificates = vm.info.accountingCertificates; 
		    param.studyTimeOfDay=vm.info.studyTimeOfDay;
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(param),
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
                vm.q.teacherId = opt.userId;
                vm.q.teacherName=opt.nickName;
                $('#query-classTeacherName').val(opt.nickName);
            } , 2);
        },
        
		exportTemplate: function (event) {  //下载模板
			/*var urlstr = "../record/recordInfo/exportExcelRecordInfoTemplate";
			window.location.href = urlstr;
			*/
			var urlstr = "../../download/template/RecordInfoTemplate.xls";
			window.location.href = urlstr;
			
		},
		query: function(){
			
		
			vm.reload(null , 1);
		},
		queryClear: function(){
			vm.q.userId = ''; 
			vm.q.name = ''; 
			vm.q.mobile = '';
			vm.q.teacherId= '';
			vm.q.teacherName= '';
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
                'teacherId': q.teacherId,
                'teacherName':q.teacherName
                }
            }).trigger("reloadGrid");
		}
		
	}
});