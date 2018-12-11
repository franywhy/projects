$(function() {
	$("#jqGrid").jqGrid({//主表数据初始化
		url : '../mall/mallgoodsinfo/list',
		datatype : "json",
		colModel : [
			{label : 'id', name : 'id', key : true , width:38 },
			
			{label : 'professionId', name : 'professionId', hidden: true },
			{label : 'levelId', name : 'levelId', hidden: true },
			{label : 'templateId', name : 'templateId', hidden: true },
			{label : 'classTypeId', name : 'classTypeId', hidden: true },
			
			{label : '商品名称', name : 'name', width : 80 },
			{label : '班型', name : 'classTypeName', width : 80 },
			{label : '专业', name : 'professionName', width : 40 },
			{label : '层次', name : 'levelName', width : 50 },
			{label : 'NCID', name : 'ncId', width : 70 },
			{label : '售价', name : 'presentPrice', width : 50 },
			{label : '原价', name : 'originalPrice', width : 50 }, 
			{label : '小图', name : 'thumbPath', width: 50 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}},
			{label : '大图', name : 'originPath', width: 50 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}},
			
//			{label : '适用对象', name : 'suitableUser', width : 80 },
//			{label : '学习周期', name : 'learningTime', width : 80 },
//			{label : '上课方式', name : 'pattern', width : 80},
			{label : '产品线', name : 'productName', width : 80},
			
			/*{label : '修改人', name : 'modifyPersonName', width : 80 },
			{label : '修改时间', name : 'modifyTime', width : 100 },*/
			{label : '上架状态', name : 'status', width : 80,
				formatter : function(value, options, row) {
					return value === 1 ? '<span class="label label-success">上架</span>'
							: '<span class="label label-danger">下架</span>';
				}
			},
			{label : '审核状态', name : 'isAudited', width : 80,
				formatter : function(value, options, row) {
					return value === 1 ? '<span class="label label-success">通过</span>'
							: '<span class="label label-danger">驳回</span>';
				}
			}
		],
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
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x" : "hidden"});
		}
	});
	/*--------------------------------------------初始化商品档案子表------------------------------------------*/
	$("#jqGridDetail").jqGrid({
		colModel : [ 
	         { label : "id", name : 'id', editable : false, hidden : true, key : true }, 
	        /* { label : "省份", name : 'areaName', index : 'areaName', width : 60 }, */
	         { label : "省份", name : 'mallAreaId', width : 80 , hidden : true}, 
	         { label : "课程", name : 'courseId', width : 80 , hidden : true }, 
	         { label : "isSubstituted", name : 'isSubstituted', width : 80 , hidden : true }, 
	         { label : "isSubstitute", name : 'isSubstitute', width : 80 , hidden : true }, 
	         { label : "isUnitedExam", name : 'isUnitedExam', width : 80 , hidden : true }, 
	         { label : "isSuitable", name : 'isSuitable', width : 80 , hidden : true }, 
	         
	         { label : "省份", name : 'areaName', width : 80 }, 
	         { label : "课程", name : 'courseName', width : 80 }, 
	         { label : "被替代课程", name : 'isSubstitutedName' , width : 50, formatter: function(value, options, row){return row.isSubstituted==1 ? "是" : "否"} }, 
	         { label : "替代课程", name: 'isSubstituteName', width : 40, formatter: function(value, options, row){return row.isSubstitute==1 ? "是" : "否"} },
	         { label : "统考", name : 'isUnitedExamName', width : 40, formatter: function(value, options, row){return row.isUnitedExam==1 ? "是" : "否"} },
	         { label : "专业不对口", name : 'isSuitableName', width : 40, formatter: function(value, options, row){return row.isSuitable==1 ? "是" : "否"} },
	         { label: '排课可冲突', name: 'courseEq', width: 40, formatter: function(value, options, row){
					return value === 0 ? 
						'<span class="label label-danger">不可冲突</span>' : 
						'<span class="label label-success">可以冲突</span>';
				}}
		],
		height : 'auto',
		pager : "#jqGridDetail",
		rowNum:1000, //每页显示记录数   
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
        	$("#jqGridDetail").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
	});

});

var vm = new Vue(
{
	el : '#rrapp',
	data : {
		showList : true,
		title : null,
		q : { //商品查询条件
			id : "",
			name : "",
			suitableUser : "",
            ncId:"",
			status : "",
			levelId : "",
			levelName : "",
			professionId : "",
			professionName : "",
			classTypeId : "",
			classTypeName : "",
			productName : "",//产品线Name
			productId:"",
		},
		mallGoodsInfo : {
			id:"",
			levelId : "",
			levelName : "",
			professionId : "",
			professionName : "",
			classTypeId : "",
			classTypeName : "",
			thumbPath:"",
			originPath:"",
			dayValidity : 1825,//有效期-默认五年
			productName : "",//产品线Name
			productId:"",//产品线ID
            onlyOne:"", //是否开通题库权限
            ncId:"",
            tkCourseCode:"",
            goodRecomment:"" //商品介绍
			
		},
		detailTitle : "",
		detailItem : {
			id : "",
			mallAreaId : "",
			areaName : "",
			courseId : "",
			courseName : "",
			isSubstituted : 0,
			isSubstitute : 0,
			isSuitable : 0,
			courseType : 0
		},
		copyArea : {//拷贝省份
			areaId1 : "",
			areaName1 : "",
			areaId2 : "",
			areaName2 : ""
		},
		levelSelections : [],
		professionSelections : [],
		classTypeSelections : [],
		areaSelections : [],
		courseSelections : []
			},
			methods : {
				query : function() {
					vm.reload(null , 1);
				},
				add : function() {
					vm.showList = false;
					vm.title = "新增";
					vm.mallGoodsInfo = {
							levelId : "",
							levelName : "",
							professionId : "",
							professionName : "",
							classTypeId : "",
							classTypeName : "",
							templateName:"",
							thumbPath:"",
							originPath:"",
							dayValidity : 1825,//有效期-默认五年
							productName : "",//产品线Name
							productId:"",//产品线ID
                            onlyOne:1, //是否开通题库权限
                            ncId:"",
                            tkCourseCode:"",
                            goodRecomment:"" //商品介绍
						};
					
					$("#auditButton").attr("style", "display: none");
					$("#commitButton").attr("style", "display: block");
				},
				update : function(event) {
					var id = getSelectedRow();
					if (id == null) {
						return;
					}
					vm.showList = false;
					vm.title = "修改";
					vm.getInfo(id);  
					$("#pjqGridDetail").attr("style", "display:block");
					
					$("#auditButton").attr("style", "display: none");
					$("#commitButton").attr("style", "display: block");
					$("#createPersonText").attr("readonly", "readonly");
					$("#createTimeText").attr("readonly", "readonly");
					$("#modifyPersonText").attr("readonly", "readonly");
					$("#modifyTimeText").attr("readonly", "readonly");
				},	
				saveOrUpdate : function(event) {
					if($.isNull(vm.mallGoodsInfo.ncId)){
						alert("请输入NCID！！！");
						return;
					}
					if(vm.mallGoodsInfo.onlyOne == 1){
						if($.isNull(vm.mallGoodsInfo.tkCourseCode)){
							alert("请输入题库课程编号！！！");
							return;
						}
					}
                    if(!$.isNull(vm.mallGoodsInfo.goodRecomment) && vm.mallGoodsInfo.goodRecomment.length > 100){
                        alert("商品介绍请控制在100字以内");
                        return;
                    }
					if(vm.title == "新增")
				    {
				       url = "../mall/mallgoodsinfo/save";
				    }
				    else if(vm.title == "修改")
				    {
				       url = "../mall/mallgoodsinfo/update";
				    }else
				    {
				       url = "";
				    }
				    
				    //子表所有数据
					var details = [] ;
					var ids = $("#jqGridDetail").jqGrid('getDataIDs');
					for(var i = 0;i<ids.length;i++){
						var row = $('#jqGridDetail').jqGrid('getRowData',ids[i]);
						if(isNaN(row.id)){
		    				row.id = null;
		    			}
					    details.push(row);
					}
					vm.mallGoodsInfo.detailList = details;
					
				    
					$.ajax({
						type: "POST",
					    url: url,
					    data: JSON.stringify(vm.mallGoodsInfo),
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
				del : function(event) {
					var id = getSelectedRow();
					if (id == null) {
						return;
					}

					confirm('确定要删除选中的记录？', function() {
						$.ajax({
							type : "POST",
							url : "../mall/mallgoodsinfo/delete/"+id,
							/*data : JSON.stringify(ids),*/
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
					$.get("../mall/mallgoodsinfo/info/" + id,function(r) {
						if(!$.isNull(r.mallGoodsInfo)){
							if(!$.isNull(r.mallGoodsInfo.details)){
								for (var i = 0; i<r.mallGoodsInfo.details.length; i++) {
									for (var j = 0; j < vm.areaSelections.length; j++) {
										if (vm.areaSelections[j].value == r.mallGoodsInfo.details[i].mallAreaName) {
											r.mallGoodsInfo.details[i].mallAreaName = vm.areaSelections[j].name
										}
									}
									for (var j = 0; j < vm.courseSelections.length; j++) {
										if (vm.courseSelections[j].value == r.mallGoodsInfo.details[i].courseName) {
											r.mallGoodsInfo.details[i].courseName = vm.courseSelections[j].name
										}
									}
								}
							}
							
							vm.mallGoodsInfo = r.mallGoodsInfo;  
 
							if('templateName' in vm.mallGoodsInfo == false){
								Vue.set(vm.mallGoodsInfo,'templateName',''); 
 							} 
							$('#jqGridDetail').setGridParam({
											datastr : JSON.stringify(vm.mallGoodsInfo.details),
											datatype : 'jsonstring'
										}).trigger('reloadGrid');
							

							 
						}
					});

				},
				reload : function(event , p) {
					vm.showList = true;
					//TODO 商品查询数据清空问题?
					$('#jqGridDetail').clearGridData(true);
					var page = p||$("#jqGrid").jqGrid('getGridParam', 'page');
					$("#jqGrid").jqGrid('setGridParam', {
						postData : vm.q,
						datatype: "json",
						page : page
					}).trigger("reloadGrid");
				},
				resume : function(event) {//启用
					var ids = getSelectedRows();
					if (ids == null) {
						return;
					}

					confirm('确定要启用选中的记录？', function() {
						$.ajax({
							type : "POST",
							url : "../mall/mallgoodsinfo/resume",
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
				pause : function(event) {//禁用
					var ids = getSelectedRows();
					if (ids == null) {
						return;
					}

					confirm('确定要禁用选中的记录？', function() {
						$.ajax({
							type : "POST",
							url : "../mall/mallgoodsinfo/pause",
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
				clearQuery : function(event) {//重置查询条件
					vm.q = {//商品查询条件
						id : "",
						name : "",
						suitableUser : "",
						ncId:"",
						status : "",
						levelId : "",
						levelName : "",
						professionId : "",
						professionName : "",
						classTypeId : "",
						classTypeName : ""
					}
				},
				audit : function(event) {//审核
					var id = getSelectedRow();
					if (id == null) {
						return;
					}
					vm.showList = false;
					vm.title = "审核";
					vm.getInfo(id);
					
					$("#pjqGridDetail").attr("style", "display:none");
					$("#auditButton").attr("style", "display: block");
					$("#commitButton").attr("style", "display: none");
					
					$("#createPersonText").attr("readonly", "readonly");
					$("#createTimeText").attr("readonly", "readonly");
					$("#modifyPersonText").attr("readonly", "readonly");
					$("#modifyTimeText").attr("readonly", "readonly");
					
					$("#levelContent").attr("readonly", "readonly");
					$("#levelContent").attr("disabled", "disabled");
					$("#professionContent").attr("readonly", "readonly");
					$("#professionContent").attr("disabled", "disabled");
					$("#classContent").attr("readonly", "readonly");
					$("#classContent").attr("disabled", "disabled");
					
					$("#patternTextContent").attr("readonly", "readonly");
					$("#learningTimeTextContent").attr("readonly", "readonly");
					$("#suitableUserTextContent").attr("readonly", "readonly");
					$("#originalPriceTextContent").attr("readonly", "readonly");
					$("#presentPriceTextContent").attr("readonly", "readonly");
					$("#nameTextContent").attr("readonly", "readonly");
				},
				accept : function(event) {//审核通过
					$.ajax({
						type : "POST",
						url : "../mall/mallgoodsinfo/accept",
						data : JSON.stringify(vm.mallGoodsInfo.id),
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
				reject : function(event) {//审核未通过
					$.ajax({
						type : "POST",
						url : "../mall/mallgoodsinfo/reject",
						data : JSON.stringify(vm.mallGoodsInfo.id),
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
				iadd:function(){//子表新增
//					courseLayer
					vm.detailTitle = "新增课程";
					vm.detailItem = {
						id : "",//id
						mallAreaId : "",//地区
						areaName : "",//地区
						courseId : "",//课程
						courseName : "",//课程名称
						isSubstituted : 0,//被代替
						isSubstitute : 0,//代替
						isSuitable : 0,//专业不对口
						courseType:0
					};
					//弹窗
					vm.detailLayer();
				},
				iupdate:function(){//子表修改
					var vm = this;
					//获取选中行ID
					var selectDetail = getJqGridSelectedRow("jqGridDetail");
					if(selectDetail == null){
						return ;
					}
					//行数据
					var rowData = $("#jqGridDetail").jqGrid('getRowData',selectDetail);
					//
					vm.detailItem = rowData;
					console.log(vm.detailItem);
					if(vm.detailItem.isSubstituted == 1){//被代替
						vm.detailItem.courseType = 1;
					} else if (vm.detailItem.isSubstitute == 1) {
						vm.detailItem.courseType = 2;
					} else if (vm.detailItem.isSuitable == 1) {
						vm.detailItem.courseType = 3;
					}else{
						vm.detailItem.courseType = 0;
					}
					vm.detailTitle = "修改课程";
					//弹框
					vm.detailLayer();
				},
				detailLayer : function(){
					var vm = this;
					layer.open({
						type: 1,
						skin: 'layui-layer-molv',
						title: vm.detailTitle,
						area: ['600px', '450px'],
						shadeClose: false,
						content: jQuery("#courseMaterialTypeLayer"),
						btn: ['确定','取消'],
						btn1: function (index) {
							if($.isNull(vm.detailItem.mallAreaId)){
								alert("请选择省份！");
								return
							}
							if($.isNull(vm.detailItem.courseId)){
								alert("请选择课程！");
								return;
							}
							if($.isNull(vm.detailItem.courseType)){
								alert("请选择课程类型！");
								return;
							}
							
							if( 0 == vm.detailItem.courseType){
								vm.detailItem.isSubstituted = 0;//被代替
								vm.detailItem.isSubstitute = 0;//代替
								vm.detailItem.isSuitable = 0;//专业不对口
							} else if(1 == vm.detailItem.courseType){
								vm.detailItem.isSubstituted = 1;//被代替
								vm.detailItem.isSubstitute = 0;//代替
								vm.detailItem.isSuitable = 0; //专业不对口
							} else if (2 == vm.detailItem.courseType) {
								vm.detailItem.isSubstituted = 0; //被代替
								vm.detailItem.isSubstitute = 1; //代替
								vm.detailItem.isSuitable = 0; //专业不对口
							} else if (3 == vm.detailItem.courseType) {
								vm.detailItem.isSubstituted = 0; //被代替
								vm.detailItem.isSubstitute = 0; //代替
								vm.detailItem.isSuitable = 1; //专业不对口
							}
							//全国统考 默认统考
							vm.detailItem.isUnitedExam = 1;

						if("新增课程" == vm.detailTitle){
								//行ID
								var rowId = new Date().getTime();
								//添加行
								$("#jqGridDetail").addRowData(rowId, vm.detailItem, "last");  
							}else if("修改课程" == vm.detailTitle){
								//修改
								$("#jqGridDetail").setRowData(getJqGridSelectedRow("jqGridDetail"),vm.detailItem);
							}
							layer.close(index);
			            }
					});
				},
				idelete : function(){//子表删除
					var vm = this;
					//获取选中行ID
					var selectDetails = getJqGridSelectedRows("jqGridDetail");
					if(selectDetails == null){
						return ;
					}
					confirm('确定要删除选中的记录？', function(){
						$.each(selectDetails , function(i , val){
							$("#jqGridDetail").jqGrid("delRowData", val);  
						});
						return true;
					});
				},
				getLevelSelections : function(event) {
					$.get("../mall/level/getSelectionList", function(r) {
						vm.levelSelections = r.levelSelections;
					});
				},
				getProfessionSelections : function(event) {
					$.get("../common/professionList", function(r) {
						vm.professionSelections = r.professionSelections;
					});
				},
				getClassTypeSelections : function(event) {
					$.get("../mall/classtype/select", function(r) {
						vm.classTypeSelections = r.data;
					});
				},
				getAreaSelections : function(event) {
					var str = "";
					$.ajax({
						type : "get",
						url : "../mall/mallarea/getSelectionList",
						async : false,
						success : function(r) {
							vm.areaSelections = r.areaSelections;
							for (var i = 0; i < vm.areaSelections.length; i++) {
								str += vm.areaSelections[i].value + ":"
										+ vm.areaSelections[i].name;
								if (i != vm.areaSelections.length - 1) {
									str += ";"
								}
							}
						}
					});
					return str;
				},
			getCourseSelections : function(event) {
				var str = "";
				$.ajax({
					type : "get",
					url : "../mall/courses/getSelectionList",
					async : false,
					success : function(r) {
						vm.courseSelections = r.courseSelections;
						for (var i = 0; i < vm.courseSelections.length; i++) {
							str += vm.courseSelections[i].value + ":" + vm.courseSelections[i].name;
							if (i != vm.courseSelections.length - 1) {
								str += ";"
							}
						}
					}
				});
				return str;
			},
			////////////////////////////////查询条件浮层////////////////////////////////////
			classTypeLayerShowQuery : function() { //班型浮层
				classTypeLay.show(function(index, opt) {
					vm.q.classTypeId = opt.classtypeId;
					vm.q.classTypeName = opt.classtypeName;
				});
			},
			levelLayerShowQuery : function() { //层次浮层
				levelLay.show(function(index, opt) {
					vm.q.levelId = opt.levelId;
					vm.q.levelName = opt.levelName;
				});
			},
			professionLayerShowQuery : function() { //专业浮层
				professionLay.show(function(index, opt) {
					vm.q.professionId = opt.professionId;
					vm.q.professionName = opt.professionName;
				});
			},
			productLayerShowQuery : function(){
				productLay.show(function(index, opt) {
					vm.q.productId = opt.productId;
					vm.q.productName = opt.productName;
				});
				
			},
			////////////////////////编辑主表浮层//////////////////////////////////
			classTypeLayerShow : function() { //班型浮层
				classTypeLay.show(function(index, opt) {
					vm.mallGoodsInfo.classTypeId = opt.classtypeId;
					vm.mallGoodsInfo.classTypeName = opt.classtypeName;
				});
			},
			levelLayerShow : function() { //层次浮层
				levelLay.show(function(index, opt) {
					vm.mallGoodsInfo.levelId = opt.levelId;
					vm.mallGoodsInfo.levelName = opt.levelName; 
				});
			},
			professionLayerShow : function() { //专业浮层
				professionLay.show(function(index, opt) {
					vm.mallGoodsInfo.professionId = opt.professionId;
					vm.mallGoodsInfo.professionName = opt.professionName;
				});
			},
			templateLayerShow : function() { //模板浮层
				templateLay.show(function(index, opt) {
					vm.mallGoodsInfo.contractTemplateId = opt.id;
					vm.mallGoodsInfo.templateId = opt.templateId;
					vm.mallGoodsInfo.templateName = opt.templateName;  
				});
			},
			/////////////编辑子表浮层//////////////////
			areaLayerShowDetail : function(){//省份
				areaLay.show(function(index, opt) {
					vm.detailItem.mallAreaId = opt.areaId;
					vm.detailItem.areaName = opt.areaName;
				});
			},
			courseLayerShowDetail : function(){//课程
				courseLay.show(function(index, opt) {
					vm.detailItem.courseId = opt.courseId;
					vm.detailItem.courseName = opt.courseName;
				});
			},
			productLayerShow : function(){//产品线
				productLay.show(function(index, opt) {
					vm.mallGoodsInfo.productId = opt.productId;
					vm.mallGoodsInfo.productName = opt.productName;
				});
				
			},
			//////////////////////////////////////  拷贝省份
			copyGoodAreaLayerShow : function(){//省份
				var id = getSelectedRow();
				if (id == null) {
					return;
				}
				var vm = this;
				var lindex = layer.open({
					type: 1,
					skin: 'layui-layer-molv',
					title: '拷贝省份',
					area: ['400px', '350px'],
					shadeClose: false,
					content: jQuery("#copyAreaLayer"),
					btn: ['确定','取消'],
					btn1: function (index) {
						if($.isNull(vm.copyArea.areaId1)){
							alert("请选择该商品的省份");
							return;
						}
						if($.isNull(vm.copyArea.areaId2)){
							alert("请选择该省份");
							return;
						}
						if(vm.copyArea.areaId1 == vm.copyArea.areaId2){
							alert("省份不能相同");
							return;
						}
						vm.copySave(lindex);
//						//关闭浮层
//						layer.close(index);
		            }
				});
			},
			copyArea1LayerShow : function(){//根據商品查詢省份浮层
				var id = getSelectedRow();
				if (id == null) {
					return;
				}
				areaGoodsLay.show(id,function(index,opt){
					vm.copyArea.areaId1 = opt.areaId;
					vm.copyArea.areaName1 = opt.areaName;
				});
			},
			copyArea2LayerShow : function(){//省份
				areaLay.show(function(index, opt) {
					vm.copyArea.areaId2 = opt.areaId;
					vm.copyArea.areaName2 = opt.areaName;
				});
			},
			copySave:function(index){//copy保存方法
				var id = getSelectedRow();
				if (id == null) {
					return;
				}
				var pdata =	vm.copyArea;
				pdata.id = id;
//				console.log(pdata);
				hq.ajax({
					type: "POST",
				    url: "../mall/mallgoodsinfo/copyArea/",
				    data: pdata,
				    success: function(r){
						if(r.code == 0){
							alert('操作成功');
							//关闭浮层
							layer.close(index);
						}else{
							alert(r.msg);
						}
					}
				});
			}
		},
		created : function() {
//				this.getLevelSelections();
//				this.getProfessionSelections();
//				this.getClassTypeSelections();
			}
});

