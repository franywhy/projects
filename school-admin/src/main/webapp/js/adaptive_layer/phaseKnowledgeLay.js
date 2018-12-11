/**
 *阶段知识点树js
 */
var phaseKnowledgeLay = {
	init : function(){
		var that = this;
	},
	setting : {
        check:{
            enable:true
        },
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                rootPId: 0
            },
            key: {
                url:"nourl"
            }
        }
	},
    ztree:null,

	scallback : null,//回调方法
	layerIndex : null,//浮层index
	layerHeigh : '650px',
	layerWeigh : '450px',
	show : function(scallback,defaultArr){
		var that = this;
		that.scallback = scallback;
		that.reload(defaultArr);
		var title = "选择知识点";

        that.layerIndex = layer.open({
			type : 1,//
			//area : 'auto',
			area : [that.layerWeigh,that.layerHeigh],
			title :title,
			closeBtn : 1,
			skin:"layui-layer-lan",
			content : jQuery("#phaseKnowledgeLay"),
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
	reload:function(defaultArr){
		var that = this;
        vm.ztree = $.fn.zTree.init($("#courseTreeAll"), that.setting,[] );
        if(vm.selectCourse.courseTreeAll && vm.selectCourse.courseTreeAll[0].id==vm.selectCourse.courseId){
            vm.ztree = $.fn.zTree.init($("#courseTreeAll"), that.setting,vm.selectCourse.courseTreeAll );
            if(defaultArr != null && defaultArr.length > 0){
                defaultArr.forEach(function(value, index){
                    var node = vm.ztree.getNodeByParam("knowledgeId",value);
                    if(node){
                        vm.ztree.checkNode(node, true, true);
                    }else{
                        console.log("知识点id:"+value+"已被删除！！");
                    }
                });
            }
		}else{
            $.get("../adaptive/layer/courseTreeAll/"+ vm.selectCourse.courseId, function(r) {
                if(r.code == 0){
                    vm.ztree = $.fn.zTree.init($("#courseTreeAll"), that.setting,r.data );
                    if(defaultArr != null && defaultArr.length > 0){
                        defaultArr.forEach(function(value, index){
                            var node = vm.ztree.getNodeByParam("knowledgeId",value);
                            if(node){
                                vm.ztree.checkNode(node, true, true);
                            }else{
                                console.log("知识点id:"+value+"已被删除！！");
                            }
                        });
                    }
                }
            });
		}
	},
	select:function(){
		var that = this;
        var nodes = vm.ztree.getCheckedNodes(true);
        if(nodes == null){
        	return;
		}
        var knowledgeIds = [];
        nodes.forEach(function(value, index, array) {
            if(!value.isParent  && value.level==3) {
                knowledgeIds.push(value.id);
            }
        });
        if(knowledgeIds.length==0){
            alert("阶段必须包含知识点，请选择知识点！");
        }
        if($.isFunction(this.scallback)){
            this.scallback(knowledgeIds);
        }
		layer.close(that.layerIndex);
	}
}
phaseKnowledgeLay.init();
