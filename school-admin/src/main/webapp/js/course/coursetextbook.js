$(function () {
    $("#jqGrid").jqGrid({
        url: '../coursetextbook/list',
        datatype: "json",
        colModel: [		
        	
			                                           { label: 'textbookId', name: 'textbookId', key: true , hidden: true},
														{ label: '教材名称', name: 'textbookName', width: 80 }, 						
														{ label: '课程代号', name: 'courseNo', width: 80 }, 		
														{ label: '课程', name: 'courseName', width: 80 }, 		
														{ label: '状态', name: 'status', width: 30, formatter: function(value, options, row){
															return value === 0 ? 
																'<span class="label label-danger">禁用</span>' : 
																'<span class="label label-success">正常</span>';
														}}, 						
														{ label: '创建人', name: 'createPerson', width: 80 }, 						
														{ label: '创建时间', name: 'creationTime', width: 80 }, 						
														{ label: '修改人', name: 'modifyPerson', width: 80 }, 						
														{ label: '修改时间', name: 'modifiedTime', width: 80 }			
							
        ],
		viewrecords: true,
        height : 500,
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
		q:{
			classplanName: null
		},
		showList: true,
		title: null,
		courseName:null,
		courseTextbook: {
			textbookName: null,
			courseId:null,
			courseName : ""
		},
		selectData : {//下拉初始化列表
			courseNameList :[],//课程
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseTextbook = {
					textbookName: null,
					courseId:null,
					courseName : ""
			};
		},
		update: function (event) {
			var textbookId = getSelectedRow();
			if(textbookId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(textbookId)
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../coursetextbook/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../coursetextbook/update";
		    }else
		    {
		       url = "";
		    }
		    //非空
		   /* if($.isNull(vm.courseTextbook.courseId)){
		    	alert();
		    	return;
		    }*/
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseTextbook),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.courseTextbook.courseName=null;
							vm.courseTextbook.textbookName=null;
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var textbookIds = getSelectedRows();
			//debugger;
			if(textbookIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../coursetextbook/delete",
				    data: JSON.stringify(textbookIds),
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
		resume:function(event){
			   var professionIds = getSelectedRows();
				if(professionIds == null){
					return ;
				}
				
				confirm('确定要启用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../coursetextbook/resume",
					    data: JSON.stringify(professionIds),
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
			pause:function(event){
			    var professionIds = getSelectedRows();
				if(professionIds == null){
					return ;
				}
				
				confirm('确定要禁用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../coursetextbook/pause",
					    data: JSON.stringify(professionIds),
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
			selectCourse: function(){
				courseLay.show(function(index,opt){
					vm.courseTextbook.courseId = opt.courseId;
					//debugger;
					vm.courseTextbook.courseName = opt.courseName;
				});
			},
			search:function(event){
			    var professionIds = getSelectedRows();
				if(professionIds == null){
					return ;
				}
				
				confirm('确定要禁用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../coursetextbook/pause",
					    data: JSON.stringify(professionIds),
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
		initcourseNameList : function(){//初始化课程列表
			var that = this;
			$.ajax({
				type: "GET",
			    url: "../common/courseList",
			    success: function(r){
					if(r.code == 0){
						that.selectData.courseNameList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		getInfo: function(textbookId){
            $.ajax({
				type: "POST",
			    url: "../coursetextbook/info/" + textbookId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseTextbook = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"textbookName" : vm.courseTextbook.textbookName,"courseName":vm.courseTextbook.courseName},
                page:page
            }).trigger("reloadGrid");
		}
		
	},
	created : function(){
		var vm = this;
		//初始化班主任列表
		vm.initcourseNameList();
	}
});