$(function () {
    $("#jqGrid").jqGrid({
        url: '../applabel/list',
        datatype: "local",
        colModel: [		
			{ label: '标签id', name: 'id', key: true, width: 20 },
			{ label: '标签名称', name: 'labelName', width: 80 }, 						
			{ label: '父级标签id', name: 'parentId', width: 80 }, 						
			{ label: '父级名称', name: 'parentName', width: 80 }, 						
			{ label: '类型', name: 'parentId', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
						'<span class="label label-danger">专业</span>' : 
						'<span class="label label-success">标签</span>';
			}}, 						
			{ label: '产品线', name: 'productId', width: 80, formatter: function(value, options, row){
				return value === 0 ? '会计' : '自考';
			}},
			{ label: '状态', name: 'dr', width: 80, formatter: function(value, options, row){
				return value === 1 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '课程代码', name: 'courseCode', width: 80 },
            { label: '公共标签', name: 'isCommon', width: 80, formatter: function(value, options, row){
                return value === 0 ? '否' : '是';
            }}
        ],
		viewrecords: true,
        height: 395,
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
			labelName: null,
			productId: "",
			dr: ""
		},
		
		showList0: false,//按钮
		showList1: true,//修改页面
		showList2: true,//新增页面
		title: null,
		appLabel: {
			labelName: "",
			dr: 0,//0:正常 1:禁用
			type: 1,//1:标签  0:专业
			productId: 0,//0:会计 1:自考
			parentId: 0,
			parentName: null,
			smallPicUrl: null,//小图标地址
			bigPicUrl: null,//大图标地址
		}
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList0 = true;//按钮
			vm.showList2 = false;
			vm.title = "新增";
			vm.appLabel = {
					labelName: "",
					dr: 0,//0:正常 1:禁用
					type: 1,//1:标签  0:专业
					productId: 0,//0:会计 1:自考
                	isCommon: 0,//公共标签：0否,1是
					parentId: 0,
					parentName: null,
					smallPicUrl: null,//小图标地址
					bigPicUrl: null,//大图标地址
			};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList0 = true;//按钮
			vm.showList1 = false;
			vm.title = "修改";
			
            vm.getInfo(id);
            
		},
		saveOrUpdate: function (event) {
			if($.isNull(vm.appLabel.labelName)){
				alert("[名称]不能为空！！！");
				return;
			}
			if(vm.title == "新增")
		    {
			   if(vm.appLabel.type == 1){
				   if(vm.appLabel.parentId == 0){
					   alert("[父级]不能为空！！！");
					   return;
				   }
			   }
			   url = "../applabel/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../applabel/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.appLabel),
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
				    url: "../applabel/delete",
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
		resume: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
					url: "../applabel/resume",
					data: JSON.stringify(id),
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
		pause: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
					url: "../applabel/pause",
					data: JSON.stringify(id),
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
            $.ajax({
				type: "POST",
			    url: "../applabel/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.appLabel = r.data;
						 if(vm.appLabel.parentId == 0){
			            	 vm.appLabel.type = 0;
			             }else{
			            	 vm.appLabel.type = 1;
			             }
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (e , p) {
			vm.showList0 = false;
			vm.showList1 = true;
			vm.showList2 = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
                datatype: "json",
				postData : vm.q,
				page:page
            }).trigger("reloadGrid");
		},
		clearQuery : function(){//清空查询条件
            var vm = this;
            vm.q.labelName = "";
            vm.q.productId = "";
            vm.q.dr = "";
            vm.appLabel.parentId = 0;
            vm.appLabel.parentName = null;
        },
        downKJExcelTemplate : function () {
            var url = "../applabel/downKJExcelTemplate";
            window.location.href = url;
        },
        downZKExcelTemplate : function () {
        	var url = "../applabel/downZKExcelTemplate";
        	window.location.href = url;
        },
        importKJExcel : function () {
            $("#fileUploadLayer").val("");
            layer.open({
                type : 1,//
                area : ['350px','200px'],
                title :"会计标签批量导入",
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
                        url:'../applabel/importKJExcel',
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
        importZKExcel : function () {
        	$("#fileUploadLayer").val("");
        	layer.open({
        		type : 1,//
        		area : ['350px','200px'],
        		title :"自考标签批量导入",
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
        				url:'../applabel/importZKExcel',
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
        selectParent : function(){//父级标签
			labelParentLay.show(function(index,opt){
//				vm.appLabel.productId = opt.productId;
				vm.appLabel.parentId = opt.id;
				vm.appLabel.parentName = opt.labelName;
			});
		},
	}
});