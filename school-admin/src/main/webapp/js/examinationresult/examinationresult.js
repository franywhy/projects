$(function () {
    $("#jqGrid").jqGrid({
        url: '../examinationresult/list',
        datatype: "json",
        contentType: "application/x-www-form-urlencoded; charset=utf-8", 
        colModel: [		
        	
					{ label: 'id', name: 'registrationId', key: true, hidden: true },
					{ label: '学员ID', name: 'userId', width: 80 }, 						
					{ label: '姓名', name: 'nickName', width: 80 }, 						
					{ label: '手机号码', name: 'mobile', width: 80 }, 						
					{ label: '报考单号', name: 'registrationNo', width: 80 }, 						
					{ label: '报考课程', name: 'courseName', width: 80 }, 						
					{ label: '报考省份', name: 'bkAreaName', width: 80 }, 						
					{ label: '报考时间', name: 'scheduleDate', width: 80 },
					{ label: '报考班型', name: 'classtypeName', width: 80 },
					{ label: '成绩', name: 'score', align : "center",width: 80 }	,
					{ label: '统考成绩截图',name: 'img',align : "center" ,formatter : function(value, options, row){
						if(value == null || value == ""){
							return ''
						}else{
							return '<img height="32px" width="32px"  src="'+value+'"/>';}
						}
					},
					{ label: '成绩类型', name: 'examType',align : "center" ,formatter : function(value, options, row){
						if(value == 0){
							return '<span class="label label-success">非补考</span>';
						}else if(value == 1){
							return '<span class="label label-success">补考</span>';
						}else{
							return '<span class="label label-warning">成绩未登记</span>';
						}
					}},	
					{ label: '班主任', name: 'sysUserName', width: 80 },	
							
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
			nickName:'',
			mobile:'', 
			examType:'',
			sysUserName:'',
		},
		examinationResult:{
			id:'',
			nickName:'',
			registrationId:'',
			examId:'',
			registrationNo:'',
			courseName:'',
			bkAreaName:'',
			scheduleDate:'',
			classtypeName:'',
			score:'',
			img:'',
		}

	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.examinationResult = {};
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
			console.log(vm.examinationResult.examId);
		    if(vm.examinationResult.examId == null || vm.examinationResult.examId == "")
		    {
		       url = "../examinationresult/save";
		    }
		    else if(vm.examinationResult.examId != "")
		    {
		       url = "../examinationresult/update";
		    }else
		    {
		       url = "";
		    }
		    
		    var param = {};
		    param.id = vm.examinationResult.examId;
		    param.userId = vm.examinationResult.userId;
		    param.registrationId = vm.examinationResult.registrationId;
		    param.score = vm.examinationResult.score;
		    param.img = vm.examinationResult.img;
		    param.examType = vm.examinationResult.examType;
		    
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
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../examinationresult/delete",
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
		importData: function(){
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
						url:'../examinationresult/importData',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						success:function(data){
							if(data.code == 0 && data.msg != null) {
								alert(data.msg, function(index){
									$("#jqGrid").trigger("reloadGrid");
								});
								layer.close(index);
							}else if(data.code == 0){
                                alert(data.msg, function(index){
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
		importModal: function(){
			var urlstr = "../../download/template/examinationResultTemplate.xls";
			window.location.href = urlstr;
		},
		exportData: function(){
			hq.ui.exportData(null);
		},
		query: function(){
			vm.reload(null , 1);
		},
		queryClear: function(){
			vm.q.userId = ''; 
			vm.q.nickName = ''; 
			vm.q.mobile = ''; 
			vm.q.examType = ''; 
			vm.q.sysUserId = '';
			vm.q.sysUserName = '';
		},
		//班主任
		selectTeacher : function(){
			teacherLay.show(function(index,opt){
				vm.q.sysUserId = opt.userId;
				vm.q.sysUserName = opt.nickName;
			} , 2);
		},
		getInfo: function(id){
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			} 
            $.ajax({
				type: "GET",
		        url: '../examinationresult/list?id=' + ids[0],
		        datatype: "json",
			    success: function(r){
			    	if(r.code == 0){
			    		console.log(r.data.list[0]);
						 vm.examinationResult = r.data.list[0];
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			var exam = vm.q; 
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page,
                postData: {
                'userId':exam.userId,
                'nickName':exam.nickName,
                'mobile':exam.mobile,
                'examType':exam.examType,
                'sysUserId': exam.sysUserId
                }
            }).trigger("reloadGrid");
		}
		
	}
});