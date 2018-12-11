$(function() {
	$("#jqGrid")
			.jqGrid(
					{
						url : '../mall/mallfeedbackrecord/list',
						datatype : "json",
						colModel : [
								{
									label : '序号',
									name : 'id',
									width : 50,
									key : true
								},
								{
									label : '反馈类型',
									name : 'feedbackType',
									width : 80
								},
								{
									label : '反馈内容',
									name : 'feedbackContent',
									width : 80
								},
								{
									label : '创建人',
									name : 'creationName',
									width : 80
								},
								{
									label : '修改人',
									name : 'modifiedName',
									width : 80
								},
								{
									label : '创建时间',
									name : 'createTime',
									width : 80
								},
								{
									label : '修改时间',
									name : 'modifyTime',
									width : 80
								},
								{
									label : '状态',
									name : 'status',
									width : 80,
									formatter : function(value, options, row) {
										return value === 1 ? '<span class="label label-success">启用</span>'
												: '<span class="label label-danger">停用</span>';
									}
								} ],
						viewrecords : true,
						height : 385,
						rowNum : 10,
						rowList : [ 10, 30, 50 ],
						rownumbers : true,
						rownumWidth : 35,
						autowidth : true,
						multiselect : true,
						pager : "#jqGridPager",
						jsonReader : {
							root : "page.list",
							page : "page.currPage",
							total : "page.totalPage",
							records : "page.totalCount"
						},
						prmNames : {
							page : "page",
							rows : "limit",
							order : "order"
						},
						gridComplete : function() {
							// 隐藏grid底部滚动条
							$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
								"overflow-x" : "hidden"
							});
						}
					});
});

var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		q:{feedbackType : ""},
		mallFeedbackRecord : {}
	},
	methods : {
		query : function() {
			vm.reload(null , 1);
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.mallFeedbackRecord = {};
			$("#createPerson").attr("style","display: none");
			$("#createTime").attr("style","display: none");
			$("#modifyPerson").attr("style","display: none");
			$("#modifyTime").attr("style","display: none");
			$("#createPersonText").removeAttr("readonly");
			$("#createTimeText").removeAttr("readonly");
			$("#modifyPersonText").removeAttr("readonly");
			$("#modifyTimeText").removeAttr("readonly");
		},
		update : function(event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";
			vm.getInfo(id);
			$("#createPerson").attr("style","display: block");
			$("#createTime").attr("style","display: block");
			$("#modifyPerson").attr("style","display: block");
			$("#modifyTime").attr("style","display: block");
			$("#createPersonText").attr("readonly","readonly");
			$("#createTimeText").attr("readonly","readonly");
			$("#modifyPersonText").attr("readonly","readonly");
			$("#modifyTimeText").attr("readonly","readonly");
		},
		saveOrUpdate : function(event) {
			if (vm.title == "新增") {
				url = "../mall/mallfeedbackrecord/save";
			} else if (vm.title == "修改") {
				url = "../mall/mallfeedbackrecord/update";
			} else {
				url = "";
			}
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.mallFeedbackRecord),
				success : function(r) {
					if (r.code === 0) {
						alert('操作成功', function(index) {
							vm.reload();
						});
					} else {
						alert(r.msg);
					}
				}
			});
		},
		del : function(event) {
			var ids = getSelectedRows();
			if (ids == null) {
				return;
			}

			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../mall/mallfeedbackrecord/delete",
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
		getInfo : function(id) {
			$.get("../mall/mallfeedbackrecord/info/" + id, function(r) {
				vm.mallFeedbackRecord = r.mallFeedbackRecord;
			});
		},
		reload : function(event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				postData: vm.q,
				page : page
			}).trigger("reloadGrid");
		},
		resume : function(event) {
			var ids = getSelectedRows();
			if (ids == null) {
				return;
			}

			confirm('确定要启用选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../mall/mallfeedbackrecord/resume",
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

			confirm('确定要禁用选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../mall/mallfeedbackrecord/pause",
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
		}
	}
});