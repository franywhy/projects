$(function () {
    $("#jqGrid").jqGrid({
        url: '../courses/knowledge/list',
        datatype: "local",
        colModel: [			
        	{ label: '主键', name: 'knowledgeId', index: 'knowledge_id',hidden: true, width: 50, key: true },
        	{ label: 'status', name: 'status', index: 'status',hidden: true, width: 50 },
        	{ label: '编码', name: 'knowledgeNo', index: 'knowledge_no', width: 120 }, 			
			{ label: '名称', name: 'knowledgeName', index: 'knowledge_name', width: 90 }, 			
			{ label: '所属节', name: 'sectionId', index: 'section_id', width: 220 ,formatter: function(v, options, row){
				return row.sectionNo + row.sectionName;
			}}, 			
			{ label: '难度', name: 'levelName', index: 'level_id', width: 70 }, 			
			{ label: '考点', name: 'keyPointName', index: 'key_point', width: 50 }, 
			{ label: '题型', name: 'questiontypeList', index: 'knowledge_id', width: 50 ,formatter: function(v, options, row){
				var text = "";
				var questiontypeList = row.questiontypeList
				if($.isNotNull(questiontypeList) && questiontypeList.length > 0){
					$.each(questiontypeList , function(index,element){
						text += element.cname;
						if(index < questiontypeList.length - 1) text += ",";
					});
				}
				return text;
			}},
			// { label: '包含知识点', name: 'childList', index: 'knowledge_no', width: 200 ,formatter: function(v, options, row){
			// 	var text = "";
			// 	var childList = row.childList;
			// 	if($.isNotNull(childList) && childList.length > 0){
			// 		$.each(childList , function(index,element){
			// 			text += element.childName;
			// 			if(index < childList.length - 1) text += ",";
			// 		});
			// 	}
			// 	return text;
			// }},
			// { label: '状态', name: 'statusName', index: 'status', width: 50 ,formatter: function(v, options, row){
			// 	return row.status == 1 ? '启用' : '禁用';
			// }},
        ],
		viewrecords: true,
		height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
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
    
 // ------------------------------------------------------------录播课 recordjqGrid------------------------------------
    
    vm.csCourseLayShow();
	
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		selectCourseId : null,//选择课程ID
		selectCourseName : "",//选择课程名称
		selectCourseProductId : null,//选择课程产品线ID
		q:{//搜索条件
			courseId: null,//课程ID
			chapterId: null,//章ID
			sectionId: null,//节ID
			knowledgeName: null,//名称
			knowledgeNo: null,//编码
			keyPoint: null,//重点:0.正常;1.重点
			keyPointName: null,//重点:0.正常;1.重点
			questionType:null,//题型PK,请参考静态变量表
			levelId:null//难度PK，请参考静态变量表
		},
		show1: true,
		show2: false,
		show3: false,
		show5: false,
		title: null,
		setting : {
			    data: {
			        simpleData: {
			            enable: true,
			            idKey: "id",
			            rootPId: 0
			        },
			    },
			    callback : {
			    	onClick : function(treeId, name, treeNode) {
			    		vm.initReload(treeNode.level,treeNode.id)
			    		vm.reload();
			    	},
			    	onNodeCreated: function(event, treeId, treeNode){
			    		vm.initReload(0,vm.q.courseId)
			    		vm.reload();
			    	}
			    }
		},
		ztree : null,
		knowledgeObj : {//知识点
			courseName : null,//课程名称
			knowledgeId : null,//知识点ID
			knowledgeName : null,//知识点名称
			knowledgeNo : null,//知识点编号
			keyPoint : null,//考点:0.正常;1.考点
			keyPointName : null,//考点:0.正常;1.考点
			levelId : null,//难度PK，请参考静态变量表
			levelName : null,//难度PK，请参考静态变量表
			sectionId : null,//节ID
			sectionName : null,//节名称
			questiontypeListName : null,
			questiontypeList : null,//题型
			childListName : null,
			childList : null,//包含知识点
		}
	},
	methods: {
		clearQuery : function(){//重置搜所条件
			//搜索条件
			vm.q.knowledgeName = null;//名称
			vm.q.knowledgeNo = null;//编码
			vm.q.keyPoint = null;//重点:0.正常;1.重点
			vm.q.keyPointName = null;//重点:0.正常;1.重点
			vm.q.questionType = null;//题型PK,请参考静态变量表
			vm.q.levelId = null;//难度PK，请参考静态
		},
		query: function () {//刷新列表
			vm.reload(null , 1);
		},
		reload: function (event , p) {//刷新列表
			var that = vm;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData : that.q,
                page:page,
                datatype : 'json'
            }).trigger("reloadGrid");
			vm.showType(2);
		},
		initReload : function(level , _id){//树形点击事件
			switch (level) {
			case 0:
				vm.q.courseId = _id;
				vm.q.chapterId = null;
				vm.q.sectionId = null;
				break;
			case 1:
				vm.q.courseId = null;
				vm.q.chapterId = _id;
				vm.q.sectionId = null;
				break;
			case 2:
				vm.q.courseId = null;
				vm.q.chapterId = null;
				vm.q.sectionId = _id;
				break;

			default:
				break;
			}
            $("#jqGrid").jqGrid('setGridParam',{
                postData : vm.q,
                page:1,
                datatype : 'json'
            }).trigger("reloadGrid");
		},
		showType:function(type){//显示
			switch (type) {
			case 1:	/*-*/
				vm.title = "知识点列表";
				vm.show1 = true;
				vm.show2 = false;
				vm.show3 = false;
				vm.show5 = false;
				break;
			case 2:	/*-*/
				vm.title = "知识点列表";
				vm.show1 = true;
				vm.show2 = true;
				vm.show3 = false;
				vm.show5 = false;
				break;
			case 3:	/*-*/
				vm.title = "新增知识点";
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = true;
				vm.show5 = false;
				break;
			case 4:	/*-*/
				vm.title = "修改知识点";
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = true;
				vm.show5 = false;
				break;
			case 5:	/*-*/
				vm.title = "新增节";
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = false;
				vm.show5 = true;
				break;

			default:
				vm.show1 = true;
				vm.show2 = false;
				vm.show3 = false;
				vm.show5 = false;
				break;
			}
		},
		csCourseLayShow : function(){//课程选择
			csCourseLay.show(function(rowId , rowData){
				vm.q.courseId = rowId;
				vm.selectCourseId = rowId;
				vm.selectCourseName = rowData.courseName;
				vm.initTree();
				vm.showType(2);
			});
		},
		initTree : function(){//初始化左侧树
			if($.isNull(vm.q.courseId)){
				alert("请选择课程!");
				return;
			}
			
			vm.reloadTree();
		},
		reloadTree : function(){
			$.get("../courses/layer/courseTree/" + vm.q.courseId, function(r) {
				if(r.code == 0){
					vm.ztree = $.fn.zTree.init($("#courseTree"), vm.setting, r.data);
					// 展开所有节点
					vm.ztree.expandAll(true);
				}
			});
		},
		add : function(){//新增按钮
			vm.initKnowledgeObj();
			vm.showType(3);
		},
		update : function(){//修改
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.initKnowledgeObj();
			vm.getInfo(id);
		},
		del : function(){
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courses/knowledge/delete/",
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
		enable : function(){//启用知识点
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGrid").jqGrid('getRowData',id);
			if(rowData.status == 1){
				alert("该知识点已经是启用状态!");
				return;
			}
			confirm('确定要启用选中的知识点嘛？', function(){
				$.ajax({
					type: "POST",
				    url: "../courses/knowledge/enable/",
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
		unenable : function(){//禁用知识点
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGrid").jqGrid('getRowData',id);
			if(rowData.status == 0){
				alert("该知识点已经是禁用状态!");
				return;
			}
			confirm('确定要禁用选中的知识点嘛？', function(){
				$.ajax({
					type: "POST",
				    url: "../courses/knowledge/unenable/",
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
		initKnowledgeObj : function(){//初始化knowledgeObj对象
			vm.knowledgeObj = {//知识点
					courseName : vm.selectCourseName,//课程名称
					knowledgeId : null,//知识点ID
					knowledgeName : null,//知识点名称
					knowledgeNo : null,//知识点编号
					keyPoint : 1,//考点:1.正常;2.考点
					keyPointName : "正常",//考点:1.正常;2.考点
					levelId : null,//难度PK，请参考静态变量表
					levelName : null,//难度PK，请参考静态变量表
					sectionId : null,//节ID
					sectionName : null,//节名称
					questiontypeListName : null,
					questiontypeList : null,//题型
					childListName : null,
					childList : null,//包含知识点
				};
		},
		getInfo : function(knowledgeId){
			$.ajax({
				type: "POST",
			    url: "../courses/knowledge/info/" + knowledgeId,
			    success: function(r){
			    	if(r.code === 0){
			    		var data = r.data;
			    		vm.knowledgeObj.courseName = vm.selectCourseName;//课程名称
	    				vm.knowledgeObj.knowledgeId = data.knowledgeId;//知识点ID
	    				vm.knowledgeObj.knowledgeName = data.knowledgeName;//知识点名称
	    				vm.knowledgeObj.knowledgeNo = data.knowledgeNo.substring(data.sectionNo.length+1,data.knowledgeNo.length);//知识点编号
	    				
	    				vm.knowledgeObj.keyPoint = data.keyPoint;//考点=1.正常;2.考点
	    				vm.knowledgeObj.keyPointName = data.keyPointName;//考点=1.正常;2.考点
	    				
	    				vm.knowledgeObj.levelId = data.levelId;//难度PK，请参考静态变量表
	    				vm.knowledgeObj.levelName = data.levelName;//难度PK，请参考静态变量表
	    				
	    				vm.knowledgeObj.sectionId = data.sectionId;//节ID
	    				vm.knowledgeObj.sectionName = data.sectionNo + data.sectionName;//节名称
	    				
	    				//题型
	    				if($.isNotNull(data.questiontypeList) && data.questiontypeList.length > 0){
	    					$.each(data.questiontypeList , function(index , item){
	    						vm.addQuestiontypeList(item.questiontypeId, item.cname);
	    					});
	    					vm.knowledgeObj.questiontypeListName = vm.initQuestiontypeListName(vm.knowledgeObj.questiontypeList);
	    				}
	    				
	    				//包含知识点
	    				if($.isNotNull(data.childList) && data.childList.length > 0){
	    					$.each(data.childList , function(index , item){
	    						vm.setChildList(item.selfId , data.knowledgeId , item.childName , item.childNo);
	    					});
	    				}
	    				$('#knowledge_parent').text(data.sectionNo+'K');
	    				vm.showType(4);

					}else{
						alert(r.msg);
					}
				}
			});
		},
		goBack : function(){// 返回
			vm.showType(2);
		},
		save : function(){//新增按钮点击
			vm.saveOrModify();
		},
		checkKnowledgeNo : function(){//编码重复性校验
			if($.isNull(vm.knowledgeObj.knowledgeNo)){
				alert("请输入知识点编号!");return;
			}else{
				vm.knowledgeObj.knowledgeNo = $('#knowledge_parent').text() + vm.knowledgeObj.knowledgeNo.trim();
			}
			hq.ajax({
				type: "POST",
			    url: "../courses/knowledge/checkKnowledgeNo",
			    data: {courseId: vm.selectCourseId, knowledgeNo : vm.knowledgeObj.knowledgeNo,knowledgeId:vm.knowledgeObj.knowledgeId},
			    success: function(r){
			    	if(r.code === 0){
			    		alert("知识点编号可用!");
					}else{
						alert(r.msg);
					}
                    var knowledgeNo = vm.knowledgeObj.knowledgeNo;
                    vm.knowledgeObj.knowledgeNo = knowledgeNo.substring(knowledgeNo.lastIndexOf("K")+1,knowledgeNo.length);
				}
			});
		},
		saveOrModify : function(){//新增\编辑 保存
			if($.isNull(vm.knowledgeObj.knowledgeNo)){
				alert("请输入知识点编码!");
				return;
			}else{
				vm.knowledgeObj.knowledgeNo = $('#knowledge_parent').text()+vm.knowledgeObj.knowledgeNo.trim();
			}
			if($.isNull(vm.knowledgeObj.knowledgeName)){
				alert("请输入知识点名称!");
				return;
			}
			if($.isNull(vm.knowledgeObj.keyPoint)){
				alert("请选择知识点考点!");
				return;
			}
			if($.isNull(vm.knowledgeObj.levelId)){
				alert("请输入知识点难度!");
				return;
			}
			if($.isNull(vm.knowledgeObj.sectionId)){
				alert("请选择节!");
				return;
			}
			//保存地址
			var url = "";
			if(vm.title == "新增知识点") url = "../courses/knowledge/save/";
			else if(vm.title == "修改知识点") url = "../courses/knowledge/update/";
			
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.knowledgeObj),
			    success: function(r){
                    var knowledgeNo = vm.knowledgeObj.knowledgeNo;
                    vm.knowledgeObj.knowledgeNo = knowledgeNo.substring(knowledgeNo.lastIndexOf("K")+1,knowledgeNo.length);
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
		knowldgeLevelShow : function(){//知识点难度列表
			csConfigLay.knowldgeLevelShow(function(rowId , rowData){
				vm.knowledgeObj.levelName = rowData.cname;
				vm.knowledgeObj.levelId = rowData.cvalue;
			});
		},
		knowledgeQuestionTypeShow : function(){//知识点题型列表
			//弹窗选中题型
			var selectIds = new Array();
			if(null != vm.knowledgeObj.questiontypeList && vm.knowledgeObj.questiontypeList.length > 0){
				$.each(vm.knowledgeObj.questiontypeList , function(index , item){
					selectIds.push(item.questiontypeId);
				});
			}
			//弹出题型弹窗
			csConfigMutiSelectLay.knowledgeQuestionTypeShow(selectIds,function(rowIdList , rowDataList){
				//rowIdList->多选题型ID
				//rowDataList->多选题型Obj
				if(null != rowDataList && rowDataList.length){
					//题型支持多选
					$.each(rowDataList , function(index , item){
						vm.addQuestiontypeList(item.cvalue , item.cname);
					});
					//选中题型名称
					vm.knowledgeObj.questiontypeListName = vm.initQuestiontypeListName(vm.knowledgeObj.questiontypeList);
				}
			});
		},
		addQuestiontypeList : function(value , name){
			if(vm.knowledgeObj.questiontypeList == null){
				//选中题型数组
				vm.knowledgeObj.questiontypeList = new Array();
			}
			vm.knowledgeObj.questiontypeList.push({'questiontypeId' : value , "cname" : name});
		},
		initQuestiontypeListName : function(questiontypeList){
			var text = "";
			if($.isNotNull(questiontypeList) && questiontypeList.length > 0){
				$.each(questiontypeList , function(index , item){
					text += item.cname;
					if(index < questiontypeList.length - 1){
						text += ",";
					}
				});
			}
			return text;
		},
		knowldgeKeyPointShow : function(){//考点列表
			csConfigLay.knowledgeKeyPointShow(function(rowId , rowData){
				vm.knowledgeObj.keyPointName = rowData.cname;
				vm.knowledgeObj.keyPoint = rowData.cvalue;
			});
		},
		knowldgeKeyPointQueryShow : function(){//考点列表
			csConfigLay.knowledgeKeyPointShow(function(rowId , rowData){
				vm.q.keyPointName = rowData.cname;
				vm.q.keyPoint = rowData.cvalue;
			});
		},
		sectionShow : function(){//考点列表
			csConfigLay.knowledgeKeyPointShow(function(rowId , rowData){
				vm.knowledgeObj.keyPointName = rowData.cname;
				vm.knowledgeObj.keyPoint = rowData.cvalue;
			});
		},
		csCourseLayShow : function(){//课程选择
			csCourseLay.show(function(rowId , rowData){
				vm.q.courseId = rowId;
				vm.selectCourseId = rowId;
				vm.selectCourseName = rowData.courseName;
				vm.selectCourseProductId = rowData.productId;
				vm.initTree();
				vm.showType(2);
			});
		},
		csSectionLayShow : function(){//节选择
			csCourseTreeLay.show(vm.selectCourseId , false , false , true , function(id,name,node){
				vm.knowledgeObj.sectionId = id;//节ID
				vm.knowledgeObj.sectionName = name;//节名称
				$('#knowledge_parent').text(name.substring(1,name.indexOf(')'))+'K');
			});
		},
		csKnowleTreeLayShow : function(){//包含知识点弹窗
			csKnowleTreeLay.show(vm.selectCourseId , function(selectDetail , rowData){
				vm.setChildList(vm.knowledgeObj.knowledgeId , rowData.knowledgeId , rowData.knowledgeName , rowData.knowledgeNo);
			},vm.knowledgeObj.knowledgeId);
		},
		setChildList : function(selfId , childId , childName , childNo){//增加包含知识点
			var childObj = {
					selfId : selfId,
					childId : childId,
					childName : childName,
					childNo : childNo
			};
			if(null == vm.knowledgeObj.childList){
				vm.knowledgeObj.childList = new Array();
			}
			//避免重复包含
			var childIsCheck = false;
			for(var i = 0 ; i < vm.knowledgeObj.childList.length ; i++){
				if(childObj.childId == vm.knowledgeObj.childList[i].childId){
					childIsCheck = true;
					break;
				}
			}
			if(!childIsCheck){//非重复包含添加到数组
				vm.knowledgeObj.childList.push(childObj);
			}
			//包含知识点显示方法
			vm.initChildListName();
		},
		initChildListName : function(childList){//包含知识点显示方法
			vm.knowledgeObj.childListName = vm.getChildListName(vm.knowledgeObj.childList);
		},
		getChildListName : function(childList){
			var text = "";
			if(null != childList && childList.length > 0){
				$.each(childList , function(index , item){
					text += item.childName;
					if(index < childList.length - 1){
						text += ",";
					}
				});
			}
			return text;
		},
		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 清楚操作  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		clearKnowledgeSectionName : function(){//清除节
			vm.knowledgeObj.sectionId = "";//节ID
			vm.knowledgeObj.sectionName = "";//节名称
		},
		clearKnowledgeLevelName : function(){//清除难度
			vm.knowledgeObj.levelName = "";
			vm.knowledgeObj.levelId = "";
		},
		clearKnowledgeKeyPointName : function(){//清除考点
			vm.knowledgeObj.keyPointName = "";
			vm.knowledgeObj.keyPoint = "";
		},
		clearKnowledgeQuestionType : function(){//清除题型
			vm.knowledgeObj.questiontypeList = new Array();
			vm.knowledgeObj.questiontypeListName = "";
		},
		clearKnowledgeChild : function(){//清除知识点包含关系
			vm.knowledgeObj.childList = new Array();
			vm.initChildListName(vm.knowledgeObj.childList);
		},
		importExcel : function(){//批量导入知识点
			var that = vm;
			if($.isNull(vm.selectCourseId)){
				alert("请选择课程！");
				return;
			}
			$("#fileUploadLayer").val("");
			layer.open({
				type : 1,//
				area : ['350px','200px'],
				title :"批量上传",
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
						url:'../courses/knowledge/importExcel',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						data: { courseId:  that.selectCourseId}, //此参数非常严谨，写错一个引号都不行
						success:function(data){
							if(data.code == 0) {
								alert(data.data ||"批量导入成功", function(alertIndex){
									vm.reloadTree();
									vm.reload();
									layer.close(index);
								});
							}else{
								alert(data.msg);
							}
						},
                        complete: function (xmlHttpRequest) {
                            console.log("complete")
                        },
                        error: function (data, status, e) {
                            console.log("error")
                        }

					});
				}
			});
		},
		downExcelTemplate : function(){//下载Excel模板
			var urlstr = "../courses/knowledge/downExcelTemplate";
			window.location.href = urlstr;
		},
		checkKnowledgeClose : function(){//校验知识点是否出现闭环
			var that = vm;
			if($.isNull(vm.selectCourseId)){
				alert("请选择课程！");
				return;
			}
			$.ajax({
				type: "POST",
			    url: "../courses/knowledge/checkKnowledge/"+vm.selectCourseId,
			    success: function(r){
			    	if(r.code === 0){
						alert('校验成功', function(index){
						});
					}else{
						alert(r.msg);
					}
				}
			});
		}
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 清楚操作  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
		
		
		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 阶段  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		
		
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 阶段  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	}
});