/**
 *部门js
 */
var deptLay = {
	init : function(){
		var that = this;
	},
	
	setting : {
	    data: {
	        simpleData: {
	            enable: true,
	            idKey: "deptId",
	            pIdKey: "parentId",
	            rootPId: -1
	        },
	        key: {
	            url:"nourl"
	        }
	    }
	},
	sysDept:{
        parentName:null,
        parentId:0,
        orderNum:0
    },
    ztree:null,
    
	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerHeigh : '650px',
	layerWeigh : '450px',
	show : function(scallback){
		var that = this;
		that.scallback = scallback;
		that.reload();
		var title = "选择部门";
		
        that.layerIndex = layer.open({
			type : 1,//
			//area : 'auto',
			area : [that.layerWeigh,that.layerHeigh],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : jQuery("#deptLayer"),
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
		$.get("../layerdata/deptList", function(r){
	        ztree = $.fn.zTree.init($("#deptTree"), that.setting, r.data);
		 });
	},
	select:function(){
		var that = this;
		var node = ztree.getSelectedNodes();
		if(node == null){
			return ;
		}
		//选择部门
		var deptId = node[0].deptId;
		var deptName = node[0].name;
		if($.isFunction(this.scallback)){
			this.scallback(deptId, deptName, node[0]);
		}
		layer.close(that.layerIndex);
	}
}
deptLay.init();
