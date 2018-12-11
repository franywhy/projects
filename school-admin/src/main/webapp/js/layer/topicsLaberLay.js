/**
 *标签js
 */
var topicsLaberLay = {
    init : function(){
        var that = this;
    },

    setting : {
        data: {
            simpleData: {
                enable: true,
                idKey: "_id",
                pIdKey: "parent_tip_id",
                rootPId: -100
            },
            key: {
                url:"nourl",
                name : "tip_name",
            },

        },
        check: {
            enable: true
        },
        view: {
            showIcon: true,
        }
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
        var title = "选择标签";

        that.layerIndex = layer.open({
            type : 1,//
            //area : 'auto',
            area : [that.layerWeigh,that.layerHeigh],
            title :title,
            closeBtn : 1,
            skin:"layui-layer-lan",
            content : jQuery("#topicsLaberLayer"),
            scrollbar : true,//是否允许浏览器出现滚动条
            fixed : false,//固定
            shadeClose : false,// 是否点击遮罩关闭
            resize : true,//是否允许拉伸
            maxmin: true, //开启最大化最小化按钮
            zIndex : 19891015,

            btn : ['确认','取消' ],
            btn1: function (index) {
                that.select();
            }
        });
    },
    reload:function(){
        var that = this;
        //加载标签树
        $.get("../topics/topicsLaber/allLaberList", function(r){
            ztree = $.fn.zTree.init($("#topicsLaberTree1"), that.setting, r.data);
            //会答报表--回显勾选的标签
            var tipIds = vm.q.childTipIds;
            if (tipIds !== null && tipIds !== undefined && tipIds !== '') {
                var tipIdArray = tipIds.split(',');
                for(var i=0; i<tipIdArray.length; i++) {
                    var node1 = ztree.getNodeByParam("_id", tipIdArray[i]);
                    if(null != node1) {
                        ztree.checkNode(node1, true, true);
                    }
                }
            }
        });
    },
    select:function(){
        var that = this;
        var nodes = ztree.getCheckedNodes(true);
        if(nodes == null){
            return ;
        }
        var parentLaberIdList = new Array();
        var laberIdList = new Array();
        var laberNameList = new Array();
        for(var i=0; i<nodes.length; i++) {
            if(0 < nodes[i]["parent_tip_id"]) {
                laberIdList.push(nodes[i]["_id"]);
                laberNameList.push(nodes[i].tip_name);
            }
        }
        if($.isFunction(this.scallback)){
            this.scallback(nodes, parentLaberIdList, laberIdList, laberNameList);
        }
        layer.close(that.layerIndex);
    }
}
topicsLaberLay.init();
