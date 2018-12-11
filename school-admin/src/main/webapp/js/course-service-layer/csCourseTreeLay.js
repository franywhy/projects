/**
 *课程树js
 */
var csCourseTreeLay = {
	init : function(){
		var that = this;
	},
	
	setting : {
	    data: {
	        simpleData: {
	            enable: true,
//	            idKey: "deptId",
//	            pIdKey: "parentId",
	            rootPId: -1
	        },
//	        key: {
//	            url:"nourl"
//	        }
	    },
	    callback : {
	    	onDblClick : function(event, treeId, treeNode) {
	    		csCourseTreeLay.select();
	    	}
	    }
	},
    ztree:null,
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerHeigh : '450px',
	layerWeigh : '300px',
	courseId : null,//课程ID
	isCourse : false,//是否可选课程
	isChapter : false,//是否可选章
	isSection : false,//是否可选节
	show : function(courseId , isCourse , isChapter , isSection , scallback){
		var that = this;
		that.courseId = courseId;
		that.isCourse = isCourse || false;
		that.isChapter = isChapter || false;
		that.isSection = isSection || false;
		that.scallback = scallback;
		that.reload();
		var title = "课程章节";
		
        that.layerIndex = layer.open({
			type : 1,//
			//area : 'auto',
			area : [that.layerWeigh,that.layerHeigh],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : jQuery("#csCourseTreeLayDiv"),
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
	reload:function(){
		var that = this;
		//加载部门树
		$.get("../courses/layer/courseTree/"+ that.courseId, function(r){
			ztree = $.fn.zTree.init($("#csCourseTreeLayTree"), that.setting, r.data);
			// 展开所有节点
			ztree.expandAll(true);
		});
	},
	select:function(){
		var that = this;
		var node = ztree.getSelectedNodes();
		if(node == null){
			return ;
		}
		//选择
		var _id = node[0].id;
		var _name = node[0].name;
		var _level = node[0].level;
		if(_level == 0 && !that.isCourse){
			alert("不能选择第一级课程!");
			return;
		}
		if(_level == 1 && !that.isChapter){
			alert("不能选择第二级章!");
			return;
		}
		if(_level == 1 && !that.isSection){
			alert("不能选择第三级节!");
			return;
		}
		console.log(node[0]);
		if($.isFunction(this.scallback)){
			this.scallback(_id, _name, node[0]);
		}
		layer.close(that.layerIndex);
	}
}
csCourseTreeLay.init();