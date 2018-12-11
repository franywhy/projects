/**
 * 公开课权限弹窗js
 */
var courseOliveAuthorityLay = {
    scallback : null,//回调方法
    layerIndex : null,//浮层index
    layerTitle : "权限列表",//标题
    layerHeigh : '450px',//高度
    layerWeigh : '300px',//宽度
    url : "../layerdata/courseOliveAuthorityList",//地址
    // moduleName : "authority",
    jqGridLay : "courseOliveAuthorityLay_jqGrid",
    _jqGridDiv : "#courseOliveAuthorityLayDiv",
    _jqGridLay : "#courseOliveAuthorityLay_jqGrid",
    init : function(){
        var that = this;
        $(that._jqGridLay).jqGrid({
            url: that.url,
            datatype: "local",
            colModel: [
                { label: "ID", name: "id",width: 80 ,hidden: true },
                { label: "权限ID", name: "authorityId",width: 80 },
                { label: "权限名称", name: "authorityName",width: 120}
            ],
            viewrecords: true,
            height: 250,
            rowNum: 100,
            rowList : [10,30,50],
            rownumbers: true,
            rownumWidth: 25,
            autowidth:true,
            multiselect: false,
            //pager: "#areaLay_jqGridPager",
            jsonReader : {
                root: "data.list",
                page: "data.currPage",
                total: "data.totalPage",
                records: "data.totalCount"
            },
            ondblClickRow : function(rowid,iRow,iCol,e){
                that.select();
            }
        });
    },
    show : function(scallback){
        var that = this;
        that.scallback = scallback;
        that.reload();
        that.layerIndex = layer.open({
            type : 1,//
            // area : [that.layerWeigh,that.layerHeigh],
            title :that.layerTitle,
            closeBtn : 1,
            skin:"layui-layer-lan",
            content : $(that._jqGridDiv),
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
        $(that._jqGridLay).jqGrid('setGridParam',{
            page : 1,
            datatype : 'json'
        }).trigger("reloadGrid");
    },
    select:function(){
        var that = this;
        var selectDetail = getJqGridSelectedRow(that.jqGridLay);
        if(selectDetail == null){
            return ;
        }
        //行数据
        var rowData = $(that._jqGridLay).jqGrid('getRowData',selectDetail);
        if($.isFunction(that.scallback)){
            that.scallback(selectDetail , rowData);
        }
        //关闭浮层
        layer.close(that.layerIndex);
    }
}
courseOliveAuthorityLay.init();
