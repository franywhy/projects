/**
 *一级标签js
 */
var topicsFirstLaberLay = {
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
            enable: true,
            chkStyle: "checkbox",
            chkboxType: { "Y": "", "N": "" }
        },
        view: {
            showIcon: true,
        }
    },
   /* sysDept:{
        parentName:null,
        parentId:0,
        orderNum:0,
    },*/

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
            content : jQuery("#topicsFirstLaberLayer"),
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
        $.get("../topics/topicsLaber/allFirstLaberList", function(r){
            ztree = $.fn.zTree.init($("#topicsLaberTree2"), that.setting, r.data);
        });
    },
    select:function(){
        var that = this;
        var nodes = ztree.getCheckedNodes(true);
        if(nodes == null){
            return ;
        }
        var laberIdList = new Array();
        var laberNameList = new Array();
        for(var i=0; i<nodes.length; i++) {
            laberIdList.push(nodes[i]["_id"]);
            laberNameList.push(nodes[i].tip_name);
        }
        if($.isFunction(this.scallback)){
            this.scallback(nodes, laberIdList,laberNameList);
        }
        layer.close(that.layerIndex);
    }
}
topicsFirstLaberLay.init();
