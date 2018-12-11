$(function () {
    $("#jqGrid").jqGrid({
        url: '../abandonedassessment/list',        	  
        datatype: "json",
        colModel: [		        	
        			{ label: 'id', name: 'id', key: true,hidden: true},
					{ label: '学员名称', name: 'studentName', width: 80 }, 						
					{ label: '手机号', name: 'studentMobile', width: 80 }, 						
					{ label: '班级名称', name: 'className', width: 80 }, 						
					{ label: '班主任', name: 'teacherName', width: 80 }, 						
					{ label: '弃考课程', name: 'course', width: 80 }, 						
					{ label: '申请时间', name: 'applicationTime', width: 80 }, 						
					{ label: '截止时间', name: 'deadlineTime', width: 80 }, 						
					{ label: '申请原因', name: 'reason', width: 80 }, 						
					{ label: '申请状态', name: 'applicationStatus', width: 80, formatter: function(value, options, row){
			                var text = '';
			                if(value == 0)text='<span class="label label-default">审核中</span>';
			                else if(value == 1) text='<span class="label label-success">通过</span>';
			                else if(value == 2)text='<span class="label label-danger">取消</span>';
			                return text; 
			                }
			            },
			        { label: '到期时间', width: 80, formatter: function(value, options, row){
			                var text = '';
			                if(row.applicationStatus == 1) {//状态为审核的时候才计算到期时间
			                    var date = new Date(row.deadlineTime).getTime() - new Date().getTime();
			                    var day = Math.floor(date / (24*3600*1000));
			                    if(day < 0) day = 0;
			                    if(day > 10) {
			                        text = day + '天';
			                    } else {
			                        text='<i style="color: red">' + day + '天</i>';
			                    }
			                }
			                return text; }
			            }

			        ],
		viewrecords: true,
        height: 385,
        rowNum: 10, //在grid上显示记录条数，这个参数是要被传递到后台
		rowList : [10,30,50],//一个下拉选择框，用来改变显示记录数，当选择时会覆盖rowNum参数传递到后台
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager", //定义翻页用的导航栏，必须是有效的html元素。翻页工具栏可以放置在html页面任意位置
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
        q: {
            teacherName: "",
            course: "",
            studentMobile: "",
            applicationStatus: ""
        },
		showList: true,
		title: null,
		abandonedAssessment: {}
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.abandonedAssessment = {};
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
			var url = vm.abandonedAssessment.id == null ? "../abandonedassessment/save" : "../abandonedassessment/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.abandonedAssessment),
			   // dataType:'json',
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
				    url: "../abandonedassessment/delete",
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
		getInfo: function(id){
			$.get("../abandonedassessment/info/"+id, function(r){
                vm.abandonedAssessment = r.abandonedAssessment;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                postData: {
                    "teacherName": encodeURIComponent(vm.q.teacherName),
                    "course": encodeURIComponent(vm.q.course),
                    "studentMobile": vm.q.studentMobile,
                    "applicationStatus": vm.q.applicationStatus
                },
                page:page
            }).trigger("reloadGrid");
		},
        clearQuery : function(){//清空查询条件
            var vm = this;
            vm.q.teacherName = "";
            vm.q.course = "";
            vm.q.studentMobile = "";
            vm.q.applicationStatus = "";
        },
        courseLayerShowDetail : function(){//课程
            courseLay.show(function(index, opt) {
                vm.q.course = opt.courseName;
            });
        },
        teacherLayerShow : function(){//老师
            teacherLay.show(function(index,opt){
                vm.q.teacherName = opt.nickName;
            });
        },
        pass: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            confirm('确定要通过选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../abandonedassessment/pass",
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
        cancel: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            confirm('确定要取消选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../abandonedassessment/cancel",
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
        downTemplate: function () {
            var urlstr = "../abandonedassessment/downTemplate";
            window.location.href = urlstr;
        },
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
                        url: '../abandonedassessment/batchImport',
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