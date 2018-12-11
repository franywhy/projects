/**
 * 班级js
 */
var freeAssessmentCoursesLay = {
    init : function(){
        $("#freeAssessmentCoursesLayClassName").val("");
        var that = this;
        $("#freeAssessmentCoursesLay_jqGrid").jqGrid({
            url: "../courseAbnormalFreeAssessment/courseListByOrderId",
            datatype: "local",
            colModel: [
                { label: 'id', name: 'id', key: true ,hidden: true},
                { label: 'areaId', name: 'areaId',hidden: true},
                { label: 'courseId', name: 'courseId',hidden: true},
                { label: 'classId', name: 'classId',hidden: true},
                { label: 'userId', name: 'userId',hidden: true},
                { label: 'sourceType', name: 'sourceType',hidden: true},
                { label: '省份', name: 'areaName', width: 80 },
                { label: '课程', name: 'courseName', width: 300 },
                { label: '课程编号', name: 'courseNo', width: 300 },
            ],
            viewrecords: true,
            height: 380,
            rowNum: 10,
            rowList : [10,30,50],
            rownumbers: true,
            rownumWidth: 25,
            autowidth:true,
            multiselect: false,
            pager: "#freeAssessmentCoursesLay_jqGridPager",
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
            ondblClickRow : function(rowid,iRow,iCol,e){
                that.select();
            },
            gridComplete:function(){
                //隐藏grid底部滚动条
                $("#freeAssessmentCoursesLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
            }
        });
    },
    scallback : null,//回调方法
    layerIndex : null,//浮层index
    show : function(scallback,orderId){
        var that = this;
        that.scallback = scallback;
        that.reload(orderId);
        var that = this;
        var title = "课程列表";
        that.layerIndex = layer.open({
            type : 1,//
            area : ['800px','700px'],
            title :title,
            closeBtn : 1,
            skin:"layui-layer-lan",
            content : $("#freeAssessmentCoursesLayDiv"),
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
    reload:function(orderId){
        $("#freeAssessmentCoursesLay_jqGrid").jqGrid('setGridParam',{
            postData : {
                className : $("#freeAssessmentCoursesLayClassName").val() , //班级名称
                teacherName : $("#teacherName").val() , //班主任名称
                orderId:orderId
            },
            page : 1,
            datatype : 'json'
        }).trigger("reloadGrid");
    },
    select:function(){
        var selectDetail = getJqGridSelectedRow("freeAssessmentCoursesLay_jqGrid");
        if(selectDetail == null){
            return ;
        }
        //行数据
        var rowData = $("#freeAssessmentCoursesLay_jqGrid").jqGrid('getRowData',selectDetail);
        if($.isFunction(this.scallback)){
            this.scallback(selectDetail , rowData);
        }
        //关闭浮层
        layer.close(this.layerIndex);
        $("#freeAssessmentCoursesLayClassName").val("");
    }
}
freeAssessmentCoursesLay.init();
