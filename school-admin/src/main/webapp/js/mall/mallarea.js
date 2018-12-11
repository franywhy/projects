$(function() {
	$("#jqGrid")
			.jqGrid(
					{
						url : '../mall/mallarea/list',
						datatype : "json",
						colModel : [
								{
									label : 'areaId',
									name : 'areaId',
									width : 50,
									key : true
								},
								{
									label : '地区名称',
									name : 'areaName',
									width : 80
								},
								{
									label : 'NCID',
									name : 'ncId',
									width : 80
								},
								{
									label : 'NCCODE',
									name : 'ncCode',
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
									label : '创建人',
									name : 'createPerson',
									width : 80
								},
								{
									label : '修改人',
									name : 'modifyPerson',
									width : 80
								},
								{
									label : '是否停用',
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
		q : {
			areaName : null,
			ncId : null
		},
		showList : true,
		title : null,
		mallArea : {
			areaName : null,
			ncId : null
		}
	},
	methods : {
		query : function() {
			vm.reload(null, 1);
		},
		add : function() {
			vm.showList = false;
			vm.title = "新增";
			vm.mallArea = {};
			$("#createPerson").attr("style", "display: none");
			$("#createTime").attr("style", "display: none");
			$("#modifyPerson").attr("style", "display: none");
			$("#modifyTime").attr("style", "display: none");
			$("#createPersonText").removeAttr("readonly");
			$("#createTimeText").removeAttr("readonly");
			$("#modifyPersonText").removeAttr("readonly");
			$("#modifyTimeText").removeAttr("readonly");
		},
		update : function(event) {
			var areaId = getSelectedRow();
			if (areaId == null) {
				return;
			}
			vm.showList = false;
			vm.title = "修改";

			vm.getInfo(areaId)
			$("#createPerson").attr("style", "display: block");
			$("#createTime").attr("style", "display: block");
			$("#modifyPerson").attr("style", "display: block");
			$("#modifyTime").attr("style", "display: block");
			$("#createPersonText").attr("readonly", "readonly");
			$("#createTimeText").attr("readonly", "readonly");
			$("#modifyPersonText").attr("readonly", "readonly");
			$("#modifyTimeText").attr("readonly", "readonly");
		},
		saveOrUpdate : function(event) {
			if (vm.title == "新增") {
				url = "../mall/mallarea/save";
			} else if (vm.title == "修改") {
				url = "../mall/mallarea/update";
			} else {
				url = "";
			}
			if ($.isNull(vm.mallArea.areaName)) {
				alert("请输入省份名称!");
				return;
			}
			if ($.isNull(vm.mallArea.ncId)) {
				alert("请输入NCID!");
				return;
			}
			if ($.isNull(vm.mallArea.ncCode)) {
				alert("请输入NC_CODE!");
				return;
			}

			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.mallArea),
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
			var areaIds = getSelectedRows();
			if (areaIds == null) {
				return;
			}
			confirm('确定要删除选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../mall/mallarea/delete",
					data : JSON.stringify(areaIds),
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
		getInfo : function(areaId) {
			$.get("../mall/mallarea/info/" + areaId, function(r) {
				vm.mallArea = r.mallArea;
			});
		},
		reload : function(event, p) {
			vm.showList = true;
			// vm.mallArea = {ncId:""};
			var page = p || $("#jqGrid").jqGrid('getGridParam', 'page');
			$("#jqGrid").jqGrid('setGridParam', {
				/*
				 * postData:{ "areaName":vm.mallArea.areaName,
				 * "ncId":vm.mallArea.ncId },
				 */
				postData : vm.q,
				page : page
			}).trigger("reloadGrid");
		},
		resume : function(event) {
			var areaIds = getSelectedRows();
			if (areaIds == null) {
				return;
			}

			confirm('确定要启用选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../mall/mallarea/resume",
					data : JSON.stringify(areaIds),
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
			var areaIds = getSelectedRows();
			if (areaIds == null) {
				return;
			}

			confirm('确定要禁用选中的记录？', function() {
				$.ajax({
					type : "POST",
					url : "../mall/mallarea/pause",
					data : JSON.stringify(areaIds),
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
		search : function(event) {
			vm.reload();
		},
		clearQuery : function() {// 清空查询条件
			var vm = this;
			vm.q.areaName = "";
			vm.q.ncId = "";
		},
	}
});