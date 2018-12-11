$(function () {
    $("#jqGrid").jqGrid({
        url: '../sys/userlaber/list',
        datatype: "json",
        colModel: [			
			{ label: 'id', name: 'id', index: 'id', width: 50, key: true ,hidden:true},
			{ label: '申请时间', name: 'createTime', index: 'create_time', width: 80 ,hidden:true}, 
			{ label: '系统管理员id', name: 'sysUserId', index: 'sys_user_id', width: 80 ,hidden:true},
			{ label: '老师名称', name: 'nickName', index: 'nickName', width: 80 },
			{ label: '手机号', name: 'mobile', index: 'mobile', width: 80 },
			{ label: '标签ids', name: 'laberIds', index: 'laber_ids', width: 80 ,hidden:true}, 			
			{ label: '标签', name: 'laberNames', index: 'laberNames', width: 80 }, 			
			/*{ label: '权限状态', name: 'statusName', width: 80 ,formatter: function(v, options, row){
				var value = row.status;
				var text = '';
				if(value == 0)text= '所有';
				else if(value == 1)text= '正常';
				else if(value == 2)text= '冻结';
				return text;
			}},*/
			{ label: '创建人', name: 'creationName', index: 'creationName', width: 80 }, 
			{ label: '操作时间', name: 'modifyTime', index: 'modifyTime', width: 80 }		
			/*{ label: '备注', name: 'remark', index: 'remark', width: 80 }*/		
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
			nickName:"",
			mobile:"",
			status:-1
		},
		a:{
			sysUserId:"",
			sysUserName:"",
			mobile:"",
			laberIds:"",
			laberNameList:""
		},
		showList: true,
		title: null,
//		sysUserLaber: {},
		statusOptions: [
			{ text: '全部', value: -1 },
			{ text: '正常', value: 1 },
			{ text: '冻结', value: 2 }
		]
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			document.getElementById("id.sysUserName").disabled = false;
			document.getElementById("id.mobile").disabled = false;
			vm.title = "新增";
			vm.sysUserLaber = {};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			document.getElementById("id.sysUserName").disabled = true;
			document.getElementById("id.mobile").disabled = true;
			vm.showList = false;
            vm.title = "修改";
            vm.showAddOrUpdate = true;
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			var url = vm.a.id == null ? "../sys/userlaber/save" : "../sys/userlaber/update";
			if(!vm.a.sysUserId){
                alert('请选择老师');
                return;
            }
			if(!vm.a.laberIds){
                alert('请选择标签');
                return;
            }
			$.ajax({
				type: "POST",
			    url: url,
                contentType: "application/json",
			    data: JSON.stringify(vm.a),
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
		del: function (event) {
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: baseURL + "sys/userlaber/delete",
                    contentType: "application/json",
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
			$.get("../sys/userlaber/info/"+id, function(r){
//				alert(r);
                vm.a = r.sysUserLaber;
                vm.a.sysUserName = r.sysUserLaber.nickName;
            });
		},
		reload: function (event,p) {
			var vm = this;
			vm.showList = true;
			vm.showAddOrUpdate = false;
			vm.flag=true;
			vm.flagDvalidity=true;
			vm.showOrderList=true;
			vm.showOrderAttempt=true;
			vm.a = {
				sysUserId : "",
				sysUserName : "",
				mobile : "",
				laberIds : "",
				laberNameList : ""
			};
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				datatype: "json",
                postData : {
                    "nickName" : vm.q.nickName,
                    "mobile" : vm.q.mobile,
                    "status" : vm.q.status
				},
                page:page
            }).trigger("reloadGrid");
		},
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q.nickName = "";
			vm.q.mobile = "";
			vm.q.status = -1;
		},
		//班主任
		selectTeacher : function(){
			teacherLay.show(function(index,opt){
				vm.a.sysUserId = opt.userId;
				vm.a.sysUserName = opt.nickName;
				vm.a.mobile = opt.mobile;
			} , 2);
		},
		//标签弹窗
		topicsFirstLaberLayerShowQuery : function(){
			topicsFirstLaberLay.show(function(opt,laberIdList,laberNameList){
//				alert(laberIdList+"=========="+laberNameList)
					for(var i= 0;i<laberIdList.length;i++){
						if(laberIdList[i]<0){
							alert("只能选择三级标签！");
							return;
						}
					}
				vm.a.laberIds = laberIdList.join(",");
				vm.a.laberNames = laberNameList.join(",");
			});
			
		},
		//冻结
		stopUse: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var errorMsg = '操作成功！';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.statusName == '通过'){
                    errorMsg += rowData.id+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }
			confirm('确定要停用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/userlaber/stopUse",
                    contentType: "application/json",
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
		//启用
		startUse: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var errorMsg = '操作成功！';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.statusName == '通过'){
                    errorMsg += rowData.id+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/userlaber/startUse",
                    contentType: "application/json",
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
		//删除
		del: function (event) {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }
            var errorMsg = '操作成功！';
            var errorFlag = false;
            $.each(ids,function (index,id) {
                var rowData = $("#jqGrid").jqGrid('getRowData',id);
                if(rowData.statusName == '通过'){
                    errorMsg += rowData.id+";";
                    errorFlag = true;
                }
            })
            if(errorFlag){
                alert(errorMsg);
                return;
            }
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../sys/userlaber/del",
                    contentType: "application/json",
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
		}
	}
});