$(function() {
	$("#jqGrid").jqGrid({
		url : '../kpmanagement/list',
		datatype : "local",
		postData : {
			course : vm.knowledgePoint.course,// 课程名称
			chapter : vm.knowledgePoint.chapter,
			verse : vm.knowledgePoint.verse,
			name : vm.knowledgePoint.name

		},
		colModel : [ {
			label : 'id',
			name : 'id',
			key : true,
			hidden : true,
			
		}, {
			label : '知识点名称',
			name : 'name',
			width : 140
		}, {
			label : '题库',
			name : 'questionBank',
			width : 130
		}, {
			label : '实操',
			name : 'sem',
			width : 130
		}, {
			label : '讲义',
			name : 'handout',
			width : 130
		}, {
			label : '视频',
			name : 'video',
			width : 130
		}, {
			label : '创建人',
			name : 'createBy',
			width : 130
		}/*
			 * , { label : '创建日期', name : 'createTime', width : 80 }
			 */, {
			label : '更新人',
			name : 'updateBy',
			width : 130
		} /*
			 * , { label : '更新日期', name : 'updateTime', width : 80 }, { label :
			 * '所属课程', name : 'course', width : 80 }, { label : '所属章 ', name :
			 * 'chapter', width : 80 }, { label : '所属节', name : 'verse', width :
			 * 80 }
			 */

		],
		viewrecords : true,
		height : 385,
		rowNum : 10,
		rowList : [10,30,50],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		multiselect : true,
		pager : "#jqGridPager",
		jsonReader : {
			root : "data.list",
			page : "data.currPage",
			total : "data.totalPage",
			records : "data.totalCount"
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

	/*--------------------------------------------初始化资料子表------------------------------------------*/
	$("#detailGrid")
			.jqGrid(
					{
						url : '../kpmanagement/detailInfo',
						datatype : "local",
						/*
						 * postData : {
						 * 
						 * id : vm.knowledgePoint.id
						 *  },
						 */
						// datatype: "json",
						colModel : [
								{
									label : '名称',
									name : 'name',
									width : vm.autoWidth
								},
								{
									label : '类型',
									name : 'type',
									width : vm.autoWidth,
									formatter : "select",
									editoptions : {
										value : "VIDEO:视频;LIVE:直播;QUESTIONBANK:题库;TRAINING:实训;FILE:文件;FACETOFACE:面授;OTHER:其他"
									}
								},
								{
									label : '属性',
									name : 'property',
									width : vm.autoWidth,
									formatter : "select",
									editoptions : {
										value : "BEFORECLASS:课前;MIDDLECLASS:课中;AFTERCLASS:课后"
									}
								}, /*{
									label : '创建时间',
									name : 'createTime',
									width : vm.autoWidth
								}, {
									label : '更新时间',
									name : 'updateTime',
									width : vm.autoWidth
								},

								{
									label : '创建人',
									name : 'createBy',
									width : vm.autoWidth
								}, {
									label : '最后修改人',
									name : 'updateBy',
									width : vm.autoWidth
								},*/

						],
						viewrecords : true, // 定义是否要显示总记录数
						height : 285,
						// width:800,
						// rowNum: 9,
						rowList : [ 10, 30, 50 ],
						rownumbers : true, // 如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。
						rownumWidth : 35,
						autowidth : true, // 自动宽度
						multiselect : true, // 定义是否可以多选
						pager : "#detailGridPager",
						jsonReader : {
							root : "data.list",
							page : "data.currPage",
							total : "data.totalPage",
							records : "data.totalCount"
						},
						prmNames : {
							page : "page",
							rows : "limit",
							order : "order"
						},
						gridComplete : function() {
							// 隐藏grid底部滚动条
							$("#detailGrid").closest(".ui-jqgrid-bdiv").css({
								"overflow-x" : "hidden"
							});
						},
						onPaging : function(pgButton){
							   
							  
							$("#detailGrid").jqGrid('setGridParam', {
								postData : {

									id : vm.knowledgePoint.id,
									limit : $("#detailGridPager").find(".ui-pg-selbox").val()
								},
								page : 1,
								datatype : 'json'
							}).trigger("reloadGrid");
						   }
					});

	vm.coursesLayerShow();

	$("#chapterSelect").change(function() {

		var p1 = $(this).children('option:selected').val();// 这就是selected的值
		vm.knowledgePoint.chapter = p1;
		vm.chapterSelected();
	});
});

var ztree;
var setting = {
	data : {
		simpleData : {
			enable : true,
			idKey : "menuId",
			pIdKey : "parentId",
			rootPId : -1
		},
		key : {
			url : "url"
		}
	},
	check : {
		enable : false,
		nocheckInherit : true
	},
	callback : {

		onClick : function(treeId, name, treeNode) {

			// init
			vm.knowledgePoint.course = "";
			vm.knowledgePoint.chapter = "";
			vm.knowledgePoint.verse = "";

			vm.verseName = "";
			vm.chapterName = "";

			vm.courseName = "";

			if (treeNode.level == 0) {
				var url = treeNode.perms;

				vm.knowledgePoint.course = url;
				vm.courseName = treeNode.name;

				if (!vm.showList && !vm.title == "修改") {
					vm.courseSelected();
				}

			}
			if (treeNode.level == 1) {
				var url = treeNode.perms;

				var string = url.split("#");
				vm.knowledgePoint.course = string[0];
				vm.knowledgePoint.chapter = string[1];

				var parentNode = treeNode.getParentNode();
				vm.chapterName = treeNode.name;
				vm.courseName = parentNode.name;

				if (!vm.showList && !vm.title == "修改") {
					vm.courseSelected();
					// $("#chapterSelect
					// option[value='"+vm.knowledgePoint.chapter+"']").attr("selected","true");
					$("#chapterSelect")
							.find(
									"option[value='"
											+ vm.knowledgePoint.chapter + "']")
							.attr("selected", "true");
					$("#chapterSelect").trigger("change");
				}

			}
			if (treeNode.level == 2) {
				var url = treeNode.perms;
				var string = url.split("#");
				vm.knowledgePoint.course = string[0];
				vm.knowledgePoint.chapter = string[1];
				vm.knowledgePoint.verse = string[2];

				//
				var parentNode = treeNode.getParentNode();
				// 所属节
				//				
				vm.verseName = treeNode.name;
				vm.chapterName = parentNode.name;
				parentNode = parentNode.getParentNode();
				vm.courseName = parentNode.name;

				if (!vm.showList && !vm.title == "修改") {
					vm.courseSelected();
				}

			}

			if (!vm.showList && vm.title == "修改") {
				return;
			}

			vm.isSelect = true;
			if (vm.showList) {
				vm.reload();
			}

		}
	}
};

var vm = new Vue({
	el : '#rrapp',
	data : {
		showList : true,
		showAll : false,
		showEdit : false,
		title : null,
		knowledgePoint : {},

		verseName : "",
		courseName : "",
		chapterName : "",
		name : "", //查询条件
		sTime : "",//查询排序
		isSelect : false,
		autoWidth : '300px'

	},
	methods : {
		query : function() {
			vm.reload();
		},
		add : function() {
			vm.title = "新增";

			vm.showList = false;

			vm.initData();
		},
		update : function(event) {
			var id = getSelectedRow();
			if (id == null) {
				return;
			}
			vm.title = "修改";
			vm.showList = false;
			vm.getInfo(id);
			vm.showEdit = true;

			var page = $("#detailGrid").jqGrid('getGridParam', 'page');

			$("#detailGrid").jqGrid('setGridParam', {
				postData : {

					id : id

				},
				page : page,
				datatype : 'json'
			}).trigger("reloadGrid");

		},
		initData : function() {
			var course = vm.knowledgePoint.course;
			var chapter = vm.knowledgePoint.chapter;
			var verse = vm.knowledgePoint.verse;
			vm.knowledgePoint = {};
			vm.knowledgePoint.course = course;
			vm.knowledgePoint.chapter = chapter;
			vm.knowledgePoint.verse = verse;
			$("#detailGrid").jqGrid("clearGridData");

			vm.courseSelected();
			vm.showEdit = false;

		},
		saveOrUpdate : function(event) {

			if (vm.title == "新增") {
				url = "../kpmanagement/save";
			} else if (vm.title == "修改") {
				url = "../kpmanagement/update";
			} else {
				url = "";
			}

			vm.knowledgePoint.verse = $("#verseSelect").val();
			vm.knowledgePoint.chapter = $("#chapterSelect").val();

			vm.knowledgePoint.verseName = $("#verseSelect").find(
					"option:selected").text();
			vm.knowledgePoint.chapterName = $("#chapterSelect").find(
					"option:selected").text();
			vm.knowledgePoint.courseName = vm.courseName;
			var selectDetails = $("#detailGrid").getDataIDs();
			vm.knowledgePoint.ids = selectDetails;
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(vm.knowledgePoint),

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
					url : "../kpmanagement/delete",
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

			$.ajax({
				type : "POST",
				url : "../kpmanagement/info/" + id,
				success : function(r) {
					if (r.code === 0) {
						vm.knowledgePoint = r.data;
						vm.verseName = vm.knowledgePoint.verseName;
						vm.chapterName = vm.knowledgePoint.chapterName;
						vm.courseName = vm.knowledgePoint.courseName;
						vm.courseSelected();
					} else {
						alert(r.msg);
					}
				}
			});
		},
		reload : function(event) {
			vm.showList = true;
			// var page = $("#jqGrid").jqGrid('getGridParam', 'page');

			$("#jqGrid").jqGrid('setGridParam', {
				postData : {

					course : vm.knowledgePoint.course,
					chapter : vm.knowledgePoint.chapter,
					verse : vm.knowledgePoint.verse,
					name : vm.name,
					sTime : vm.sTime

				},
				page : 1,
				datatype : 'json'
			}).trigger("reloadGrid");
			vm.initData();
		},
		returnBack : function() {

			if (!vm.showList && vm.title == "修改" && !vm.isSelect) {
				vm.knowledgePoint.chapter = "";
				vm.knowledgePoint.verse = "";
				vm.chapterName = "";
				vm.verseName = "";
			}
			vm.reload();
		},
		coursesLayerShow : function() {
			kpmCourseLay.show(function(index, opt) {

				vm.knowledgePoint.course = opt.nc_id;
				vm.courseName = opt.name;
				vm.reload();

				$.get("../layerdata/perms?id=" + opt.nc_id, function(r) {
					ztree = $.fn.zTree.init($("#menuTree"), setting, r.data);
					// 展开所有节点
					ztree.expandAll(true);

				});
				vm.showAll = true;

			});
		},
		addItem : function() {

			kpmMaterialDetailLay.show(vm.knowledgePoint.id, function(details) {
				for (var i = 0; i < details.length; i++) {
					// 行ID
					var rowId = details[i].id;
					if (!vm.checkItem(rowId)) {
						// 添加行
						$("#detailGrid").addRowData(rowId, details[i], "last");
					}

				}

			});
		},
		checkItem : function(rowId) {
			var rowIds = $("#detailGrid").getDataIDs();
			for (var i = 0; i < rowIds.length; i++) {
				if (rowId == rowIds[i]) {
					return true;
				}
			}
			return false;
		},
		delItem : function() {
			// 获取选中行ID
			// var selectedIds = $("#detailGrid").jqGrid("getGridParam",
			// "selarrrow");
			var selectDetails = getJqGridSelectedRows("detailGrid");
			if (selectDetails == null) {
				return;
			}
			confirm('确定要删除选中的记录？', function() {
				for (var i = selectDetails.length - 1; i >= 0; i--) {
					$("#detailGrid").jqGrid("delRowData", selectDetails[i]);
				}

				return true;
			});

		},
		courseSelected : function() {
			var datas = {
				"courseId" : vm.knowledgePoint.course
			};
			$.ajax({
				type : 'POST',
				url : '../kpmanagement/courseSelect',
				data : JSON.stringify(datas),
				success : function(r) {
					if (r.code === 0) {
						var result = r.data;
						$("#chapterSelect").empty();
						for (var i = 0; i < result.length; i++) {
							var chapter = result[i];
							$("#chapterSelect").append(
									"<option value='" + chapter.nc_id + "'>"
											+ chapter.name + "</option>");
						}
						// $("#chapterSelect").find("option[text='']").remove();
						if ($.trim(String(vm.knowledgePoint.chapter)) == '') {
							$("#chapterSelect option").eq(0).attr('selected',
									'true');
						} else {
							$("#chapterSelect").find(
									"option[value='"
											+ vm.knowledgePoint.chapter + "']")
									.attr("selected", "true");
							$("#chapterSelect").trigger("change");
						}

					} else {
						alert(r.msg);
					}
				}
			});

		},
		chapterSelected : function() {
			var datas = {
				"courseId" : vm.knowledgePoint.course,
				"chapterId" : vm.knowledgePoint.chapter
			};
			$.ajax({
				type : 'POST',
				url : '../kpmanagement/chapterSelect',
				data : JSON.stringify(datas),
				success : function(r) {
					if (r.code === 0) {
						var result = r.data;
						$("#verseSelect").empty();
						if (null != result) {
							for (var i = 0; i < result.length; i++) {
								var verse = result[i];
								$("#verseSelect").append(
										"<option value='" + verse.nc_id + "'>"
												+ verse.name + "</option>");
							}
							//$("#verseSelect").find("option[text='']").remove();
							if ($.trim(String(vm.knowledgePoint.verse)) == '') {
								$("#verseSelect option").eq(0).attr('selected',
										'true');
							} else {
								$("#verseSelect").find(
										"option[value='"
												+ vm.knowledgePoint.verse
												+ "']")
										.attr("selected", "true");

							}

						}

					} else {
						alert(r.msg);
					}
				}
			});

		},
		dateFormat : function( time, fmt) { // author: meizz  
            if(null == fmt || typeof fmt == "undefined" || $.trim(fmt).length == 0){  
                fmt = "yyyy-MM-dd HH:mm:ss";  
            }  
              
            if(typeof time == "number"){  
                time = new Date(time);  
            }  
              
            var o = {  
                "M+" : time.getMonth() + 1, // 月份  
                "d+" : time.getDate(), // 日  
                "h+" : time.getHours(), // 小时  
                "H+" : time.getHours(), // 小时  
                "m+" : time.getMinutes(), // 分  
                "s+" : time.getSeconds(), // 秒  
                "q+" : Math.floor((time.getMonth() + 3) / 3), // 季度  
                "S" : time.getMilliseconds() // 毫秒  
            };  
            if (/(y+)/.test(fmt)) {  
                fmt = fmt.replace(RegExp.$1, (time.getFullYear() + "").substr(4 - RegExp.$1.length));  
            }  
                  
            for ( var k in o) {  
                if (new RegExp("(" + k + ")").test(fmt)) {  
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));  
                }  
            }  
            return fmt;  
        }  

	}
});
