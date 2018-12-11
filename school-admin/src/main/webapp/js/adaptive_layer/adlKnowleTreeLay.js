/**
 * 知识点
 * 2017-12-5
 * shihongjie
 * setting.data.key.name
 * @see https://tieba.baidu.com/p/2334961259
 */
var adlKnowleTreeLay = {
	init : function(){
		var that = this;
		$("#adlKnowleTreeLay_jqGridX").jqGrid({
			url: "../adaptive/knowledge/list",
			datatype: "local",
			colModel: [
				{ label: '主键', name: 'knowledgeId', index: 'knowledge_id',hidden: true, width: 50, key: true },
	        	{ label: '编码', name: 'knowledgeNo', index: 'knowledge_no', width: 120 }, 			
				{ label: '名称', name: 'knowledgeName', index: 'knowledge_name', width: 90 }, 			
				{ label: '所属节', name: 'sectionId', index: 'section_id', width: 220 ,formatter: function(v, options, row){
					return row.sectionNo + row.sectionName;
				}}, 			
				{ label: '难度', name: 'levelName', index: 'level_id', width: 70 }, 			
				{ label: '考点', name: 'keyPointName', index: 'key_point', width: 50 }, 
				{ label: '题型', name: 'questiontypeList', index: 'knowledge_id', width: 80 ,formatter: function(v, options, row){
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
				{ label: '包含知识点', name: 'childList', index: 'knowledge_no', width: 200 ,formatter: function(v, options, row){
					var text = "";
					var childList = row.childList;
					if($.isNotNull(childList) && childList.length > 0){
						$.each(childList , function(index,element){
							text += element.childName;
							if(index < childList.length - 1) text += ",";
						});
					}
					return text;
				}},
				{ label: '状态', name: 'status', index: 'status', width: 50 ,formatter: function(v, options, row){
					return row.status == 1 ? '启用' : '禁用';
				}}, 
		           ],
		   viewrecords: true,
		   height: 400,
		   width : 600,
		   rowNum: 10,
		   rowList : [10,30,50,100],
		   rownumbers: true, 
		   rownumWidth: 45, 
		   autowidth:false,
		   multiselect: false,
		   pager: "#adlKnowleTreeLay_jqGridPagerX",
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
		   ondblClickRow : function(rowid,iRow,iCol,e){
			   that.select();
		   },
		   gridComplete:function(){
			   //隐藏grid底部滚动条
			   $("#adlKnowleTreeLay_jqGridPagerX").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
		   }
		});
	},
	courseId :"",//课程ID
	chapterId :"",//章ID
	sectionId :"",//节ID
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerTitle : "知识点列表",
	layerHeigh : '500px',
	layerWeigh : '1100px',
	show : function(courseId , scallback){
		var that = this;
		that.courseId = courseId;
		that.scallback = scallback;
		//树
		that.reloadCouresTree();
		
		that.layerIndex = layer.open({
			type : 1,//
//			area : "auto",
			area : [that.layerWeigh,that.layerHeigh],
			title :that.layerTitle,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : $("#adlKnowleTreeLayDiv"),
			scrollbar : true,//是否允许浏览器出现滚动条
			fixed : false,//固定
			shadeClose : false,// 是否点击遮罩关闭
			resize : true,//是否允许拉伸
			maxmin: true, //开启最大化最小化按钮
			zIndex : 19891014,
			btn : ['确认','取消' ],
			btn1: function (index) {
				that.select();
			}
		});
	},
	reload:function(){//加载
		var that = this;
		//知识点名称
		var adlKnowleTreeLay_knowleName = $("#adlKnowleTreeLay_knowleName").val()
		//知识点编号
		var adlKnowleTreeLay_knowleNo = $("#adlKnowleTreeLay_knowleNo").val()
		$("#adlKnowleTreeLay_jqGridX").jqGrid('setGridParam',{ 
			postData : {
				knowledgeName : adlKnowleTreeLay_knowleName,
				knowledgeNo : adlKnowleTreeLay_knowleNo,
				courseId : that.courseId,
				chapterId : that.chapterId, 
				sectionId : that.sectionId
			},
			page : 1,
			datatype : 'json'
		}).trigger("reloadGrid");
	},
	select:function(){//选择
		var selectDetail = getJqGridSelectedRow("adlKnowleTreeLay_jqGridX");
		if(selectDetail == null){
			return ;
		}
		//行数据
		var rowData = $("#adlKnowleTreeLay_jqGridX").jqGrid('getRowData',selectDetail);
		if($.isFunction(this.scallback)){
			this.scallback(selectDetail , rowData);
		}
		//关闭浮层
		layer.close(this.layerIndex);
	},
	//获取菜单树
	reloadCouresTree: function() {
		var that = this;
		//加载菜单树
		hq.ajax({
			type: "GET",
		    url: "../adaptive/layer/courseTree/" + that.courseId,
		    success: function(r){
				if(r.code == 0){
					that.menu_setting.callback.onClick = that.zTreeOnClick;
					that.menu_ztree = $.fn.zTree.init($("#adlKnowleTreeLay_Tree"), that.menu_setting, r.data);
					that.menu_ztree.expandAll(true);//展开
					that.reload();
				}else{
					alert(r.msg);
				}
			}
		});
    },
	menu_ztree : null,//树
	menu_setting: {//树配置
		data: {
		},
		check:{
			enable:false,//设置 zTree 的节点上是否显示 checkbox / radio
			nocheckInherit:false//当父节点设置 nocheck = true 时，设置子节点是否自动继承 nocheck = true 
		},
		callback:{
			onClick: null
		}
	},
	zTreeOnClick :function (event, treeId, treeNode) {//视频分类树 点击事件
		var that = adlKnowleTreeLay;
		//加载视频列表
		that.chapterId = null;
		that.sectionId = null;
		if(treeNode.level == 1){//章
			that.chapterId = treeNode.id;
		}else if(treeNode.level == 2){
			that.sectionId = treeNode.id;
		}
		that.reload();
	}
}
adlKnowleTreeLay.init();