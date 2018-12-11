$(function () {
    $("#jqGrid").jqGrid({
        url: '../coldstarting/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true ,hidden: true},
			{ label: '标题', name: 'title', index: 'title', width: 80 }, 			
			{ label: '图片', name: 'pic', index: 'pic', width: 50,align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';} },
			{ label: '跳转地址', name: 'url', index: 'url', width: 80,
                formatter : function (value, options, row) {
                    return '<a href="'+value+'" target="_blank">'+value+'</a>'
                }
            },
			{ label: '倒计时/秒', name: 'countdown', index: 'countdown', width: 80 },
			{ label: '创建时间', name: 'createTime', index: 'create_time', width: 80 }, 			
			{ label: '修改时间', name: 'updateTime', index: 'update_time', width: 80 }, 			
			/*{ label: '版本号', name: 'version', index: 'version', width: 50 },*/
			{ label: '是否显示', name: 'isShow', index: 'is_show', width: 50,
                formatter : function(value, options, row) {
                    return value === 1 ? '<span class="label label-success">上架</span>'
                        : '<span class="label label-danger">下架</span>';
                }
            },
			{ label: '状态', name: 'dr', index: 'dr', width: 50,
                formatter : function(value, options, row) {
                    return value === 0 ? '<span class="label label-success">正常</span>'
                        : '<span class="label label-danger">停用</span>';
                }
            }
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
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
		coldStarting: {
		    title:"",
            pic:"",
            url:"",
            countdown:"",
        }
	},
	methods: {
		query: function () {
			vm.reload();
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.coldStarting = {
                title:"",
                pic:"",
                url:"",
                countdown:"",
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
			var url = vm.coldStarting.id == null ? "../coldstarting/save" : "../coldstarting/update";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.coldStarting),
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
				    url: "../coldstarting/delete",
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
			$.get("../coldstarting/info/"+id, function(r){
                vm.coldStarting = r.coldStarting;
            });
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page
            }).trigger("reloadGrid");
		},
        resume : function(event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }

            confirm('确定要上架选中的记录？', function() {
                $.ajax({
                    type : "POST",
                    url : "../coldstarting/resume",
                    data : JSON.stringify(ids),
                    success : function(r) {
                        if (r.code == 0) {
                            alert('操作成功', function(index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        pause : function(event) {
            var ids = getSelectedRows();
            if (ids == null) {
                return;
            }

            confirm('确定要下架选中的记录？', function() {
                $.ajax({
                    type : "POST",
                    url : "../coldstarting/pause",
                    data : JSON.stringify(ids),
                    success : function(r) {
                        if (r.code == 0) {
                            alert('操作成功', function(index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
	}
});