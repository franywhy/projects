$(function () {
    $("#jqGrid").jqGrid({
        url: '../adaptive/knowledge/list',
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
			{ label: '题型', name: 'questiontypeName', index: 'questiontypeName', width: 50 },
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
    
    vm.adlCourseLayShow();
	
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
            questiontypeName : null,//题型
			childListName : null,
			childList : null,//包含知识点
			//知识点-视频
			adlKnowledgeVideo : {
				videoId : null,//主键
				knowledgeId : null,//知识点PK
				polyvVid : null,//录播课id
				polyvName : null,//保利威视视频名称
				polyvDurationS : null,//播放时长:毫秒
				polyvDuration : null,//时长默认'00:00:00' 格式hh-mm-ss
				screenShot : null//首截图
			},
			//知识点-资料
			adlKnowledgeFile : {
				fileId : null,//自增id
				knowledgeId : null,//知识点PK
				fileName : null,//名称
				fileUrl : null//资料文件下载地址
			}
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
		adlCourseLayShow : function(){//课程选择
			adlCourseLay.show(function(rowId , rowData){
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
			$.get("../adaptive/layer/courseTree/" + vm.q.courseId, function(r) {
				if(r.code == 0){
					vm.ztree = $.fn.zTree.init($("#courseTree"), vm.setting, r.data);
					// 展开所有节点
					vm.ztree.expandAll(true);
				}
			});
		},
		update : function(){//修改
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.initKnowledgeObj();
			vm.getInfo(id);
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
                	questiontypeName : null,//题型
					childListName : null,
					childList : null,//包含知识点
					//知识点-视频
					adlKnowledgeVideo : {
						videoId : null,//主键
						knowledgeId : null,//知识点PK
						polyvVid : null,//录播课id
						polyvName : null,//保利威视视频名称
						polyvDurationS : null,//播放时长:毫秒
						polyvDuration : null,//时长默认'00:00:00' 格式hh-mm-ss
						screenShot : null//首截图
					},
					//知识点-资料
					adlKnowledgeFile : {
						fileId : null,//自增id
						knowledgeId : null,//知识点PK
						fileName : null,//名称
						fileUrl : null//资料文件下载地址
					}
				};
		},
		getInfo : function(knowledgeId){
			$.ajax({
				type: "POST",
			    url: "../adaptive/knowledge/info/" + knowledgeId,
			    success: function(r){
			    	if(r.code === 0){
			    		var data = r.data;
			    		vm.knowledgeObj.courseName = vm.selectCourseName;//课程名称
	    				vm.knowledgeObj.knowledgeId = data.knowledgeId;//知识点ID
	    				vm.knowledgeObj.knowledgeName = data.knowledgeName;//知识点名称
	    				vm.knowledgeObj.knowledgeNo = data.knowledgeNo;//知识点编号
	    				
	    				vm.knowledgeObj.keyPoint = data.keyPoint;//考点=1.正常;2.考点
	    				vm.knowledgeObj.keyPointName = data.keyPointName;//考点=1.正常;2.考点
	    				
	    				vm.knowledgeObj.levelId = data.levelId;//难度PK，请参考静态变量表
	    				vm.knowledgeObj.levelName = data.levelName;//难度PK，请参考静态变量表
	    				
	    				vm.knowledgeObj.sectionId = data.sectionId;//节ID
	    				vm.knowledgeObj.sectionName = data.sectionName;//节
	    				vm.knowledgeObj.sectionNo = data.sectionNo;//节
                        vm.knowledgeObj.questiontypeName = data.questiontypeName;//题型

	    				//包含知识点
	    				if($.isNotNull(data.childList) && data.childList.length > 0){
	    					$.each(data.childList , function(index , item){
	    						vm.setChildList(item.selfId , data.knowledgeId , item.childName , item.childNo);
	    					});
	    				}
	    				
						//知识点-视频
	    				if($.isNotNull(data.adlKnowledgeVideo)){
	    					vm.knowledgeObj.adlKnowledgeVideo.videoId = data.adlKnowledgeVideo.videoId;//主键
	    					vm.knowledgeObj.adlKnowledgeVideo.knowledgeId = data.adlKnowledgeVideo.knowledgeId;//知识点PK
	    					vm.knowledgeObj.adlKnowledgeVideo.polyvVid = data.adlKnowledgeVideo.polyvVid;//录播课id
	    					vm.knowledgeObj.adlKnowledgeVideo.polyvName = data.adlKnowledgeVideo.polyvName;//保利威视视频名称
	    					vm.knowledgeObj.adlKnowledgeVideo.polyvDurationS = data.adlKnowledgeVideo.polyvDurationS;//播放时长=毫秒
	    					vm.knowledgeObj.adlKnowledgeVideo.polyvDuration = data.adlKnowledgeVideo.polyvDuration;//时长默认'00=00=00' 格式hh-mm-ss
	    					vm.knowledgeObj.adlKnowledgeVideo.screenShot = data.adlKnowledgeVideo.screenShot//首截图
	    				}
						//知识点-资料
	    				if($.isNotNull(data.adlKnowledgeFile)){
	    					vm.knowledgeObj.adlKnowledgeFile.fileId = data.adlKnowledgeFile.fileId;//自增id
	    					vm.knowledgeObj.adlKnowledgeFile.knowledgeId = data.adlKnowledgeFile.knowledgeId;//知识点PK
	    					vm.knowledgeObj.adlKnowledgeFile.fileName = data.adlKnowledgeFile.fileName;//名称
	    					vm.knowledgeObj.adlKnowledgeFile.fileUrl = data.adlKnowledgeFile.fileUrl//资料文件下载地址
	    				}
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
		saveOrModify : function(){//新增\编辑 保存
			// if($.isNull(vm.knowledgeObj.knowledgeName)){
			// 	alert("请输入知识点名称!");
			// 	return;
			// }
			// if($.isNull(vm.knowledgeObj.keyPoint)){
			// 	alert("请选择知识点考点!");
			// 	return;
			// }
			// if($.isNull(vm.knowledgeObj.levelId)){
			// 	alert("请输入知识点难度!");
			// 	return;
			// }
			// if($.isNull(vm.knowledgeObj.sectionId)){
			// 	alert("请选择节!");
			// 	return;
			// }
			//保存地址
			var url = "";
			if(vm.title == "新增知识点") url = "../adaptive/knowledge/save/";
			else if(vm.title == "修改知识点") url = "../adaptive/knowledge/update/";
			
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.knowledgeObj),
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
		knowldgeLevelShow : function(){//知识点难度列表
			adlConfigLay.knowldgeLevelShow(function(rowId , rowData){
				vm.knowledgeObj.levelName = rowData.cname;
				vm.knowledgeObj.levelId = rowData.cvalue;
			});
		},
		addQuestiontypeList : function(value , name){
			if(vm.knowledgeObj.questiontypeList == null){
				//选中题型数组
				vm.knowledgeObj.questiontypeList = new Array();
			}
			vm.knowledgeObj.questiontypeList.push({'questiontypeId' : value , "cname" : name});
		},
		knowldgeKeyPointQueryShow : function(){//考点列表
			adlConfigLay.knowledgeKeyPointShow(function(rowId , rowData){
				vm.q.keyPointName = rowData.cname;
				vm.q.keyPoint = rowData.cvalue;
			});
		},
		adlCourseLayShow : function(){//课程选择
			adlCourseLay.show(function(rowId , rowData){
				vm.q.courseId = rowId;
				vm.selectCourseId = rowId;
				vm.selectCourseName = rowData.courseName;
				vm.selectCourseProductId = rowData.productId;
				vm.initTree();
				vm.showType(2);
			});
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
		selectVideo: function(){//录播视频弹窗
			polyVideoLay.show(null , function(index,opt){
				vm.knowledgeObj.adlKnowledgeVideo.polyvDuration = opt.duration;//时长
				vm.knowledgeObj.adlKnowledgeVideo.screenShot = opt.first_image;//封面
				vm.knowledgeObj.adlKnowledgeVideo.polyvName = opt.title;//章节名称
				vm.knowledgeObj.adlKnowledgeVideo.polyvVid = opt.vid;//vid
			},vm.selectCourseProductId);
		},
		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 清除操作  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		clearVideo : function(){//清除视频
			vm.knowledgeObj.adlKnowledgeVideo.screenShot = "";//封面
			vm.knowledgeObj.adlKnowledgeVideo.polyvVid = "";//vid
			vm.knowledgeObj.adlKnowledgeVideo.polyvDuration = "";//时长
		},
		clearPolyvName : function(){//清除视频名称
			vm.knowledgeObj.adlKnowledgeVideo.polyvName = "";//章节名称
		},
		clearFile : function(){//清除资料
			vm.knowledgeObj.adlKnowledgeFile.fileUrl = "";//资料地址
		},
        uploadKnowledgeFile : function(files,err_message,layer_index){//
            $.ajax({
                type: "POST",
                url: "../adaptive/knowledge/uploadKnowledgeFile/",
                data: JSON.stringify(files),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(r){
                    if(err_message){
                        err_message = err_message.replace(/,/g,'<br>');
                        alert(err_message);
                    }else{
                        hq.core.upload.close(layer_index);
                        alert("上传文件成功");
                    }
                }
            });
        },
       multiUpload : function(){
            document.getElementById("uploadForm").action=hq.config.mulit_upload_url;
            document.getElementById("file").value="";
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: '上传文件',
                area: ['291px', '233px'],
                shadeClose: false,
                content: jQuery("#div_iframe"),
                btn: ['确定','取消'],
                btn1: function (index) {
                    var file = $('#file').get(0).files;
                    var fileCount = file.length;
                    if(fileCount==0){
                    	alert("请选择文件！！");
                    	return;
					}
                    if(fileCount>50){
                        alert("上传文件一次性不能超出50个文件！！");
                        return;
                    }
                    var fileNameStr = '';
                    $.each(file , function(index , item){
                        fileNameStr += item.name;
                        if(index < file.length - 1){
                            fileNameStr += ",";
                        }
                    });
                    $.ajax({
                        type: "POST",
                        url: "../adaptive/knowledge/checkFile",
                        data: JSON.stringify(fileNameStr),
                        success: function(r){
                            //参数
                            var cb_params = "layer_index=" + index;
                            //当前站点回调页面地址
                            var redirectUrl= "http://" + window.location.host + "/adaptive/multiUpload_callback.html?"+cb_params;
                            console.log(redirectUrl);
                            $("#redirectUrl").val(redirectUrl);
                            $('#errorFile').val(r.msg);
                            //提交form表单
                            document.getElementById("uploadForm").submit();
                        }
                    });

                }
            });
        },
		importExcel : function(){//批量导入知识点视频
			var that = vm;
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
						url:'../adaptive/knowledge/importExcel',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						data: { courseId:  that.selectCourseId, productId : vm.selectCourseProductId}, //此参数非常严谨，写错一个引号都不行
						success:function(data){
							if(data.code == 0) {
								alert(data.data ||"批量导入成功", function(alertIndex){
									vm.reloadTree();
									vm.reload();
									layer.close(index);
								});
								}
							else if(data.code == 1) { 
								alert(data.msg);
							}
						}
					});
				}
			});
		}
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 清楚操作  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
		
		
		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 阶段  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		
		
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 阶段  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	}
});
