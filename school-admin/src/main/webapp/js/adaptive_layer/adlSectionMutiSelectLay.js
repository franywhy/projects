/**
 * 节列表js
 */
var adlSectionMutiSelectLay = {
    init : function(){
        var that = this;
        that.courseId = null;
        $("#adlSectionMutiSelectLay_jqGrid").jqGrid({
            url: "../adaptive/layer/sectionList",
            datatype: "local",
            colModel: [
                { label: "主键", name: "sectionId" , index: 'section_id',key: true , hidden: true},
                { label: "编码", name: "sectionNo" , index: 'section_no',hidden: false , width: 250},
                { label: "名称", name: "sectionName" , index: 'section_name',hidden: false , width: 270},
            ],
            viewrecords: true,
            height: 320,
            rowNum: 100,
            rowList : [10,30,50,100],
            rownumbers: true,
//		   rownumWidth: 35,
            width:500,
            autowidth:true,
            multiselect: true,
            pager: "#adlSectionMutiSelectLay_jqGridPager",
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
                $("#adlSectionMutiSelectLay_jqGridPager").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
                if(null != that.selectIds && that.selectIds.length > 0){
                    $.each(that.selectIds , function(index , element){
                        $("#adlSectionMutiSelectLay_jqGrid").jqGrid('setSelection',element);
                    });
                }
            }
        });
    },
    scallback : null,//回调方法
    layerIndex : null,//浮层index
    courseId : null,
    selectIds : null,//默认选中ID
    show : function(courseId,selectIds , scallback){
        var that = this;
        that.courseId = courseId;
        that.selectIds = selectIds || new Array();
        that.scallback = scallback;
        that.reload();
        var title = "节列表";
        that.layerIndex = layer.open({
            type : 1,//
            area : ['600px','550px'],
            title :title,
            closeBtn : 1,
            skin:"layui-layer-lan",
            content : $("#adlSectionMutiSelectLayDiv"),
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

        var adlSectionMutiSelectLay_SectionName = $("#adlSectionMutiSelectLay_SectionName").val();
        var adlSectionMutiSelectLay_SectionNo = $("#adlSectionMutiSelectLay_SectionNo").val();

        $("#adlSectionMutiSelectLay_jqGrid").jqGrid('setGridParam',{
            postData : {
                courseId : that.courseId,//课程ID
                sectionName : adlSectionMutiSelectLay_SectionName , //名称
                sectionNo : adlSectionMutiSelectLay_SectionNo //编码
            },
            page : 1,
            datatype : 'json'
        }).trigger("reloadGrid");
    },
    select:function(){
        var rowIdList = getJqGridSelectedRows("adlSectionMutiSelectLay_jqGrid");
        if(rowIdList == null){
            return ;
        }
        //行数据
        var rowDataList = new Array();
        $.each(rowIdList , function(index,element){
            var rowData = $("#adlSectionMutiSelectLay_jqGrid").jqGrid('getRowData',element);
            rowDataList.push(rowData);
        });
        if($.isFunction(this.scallback)){
            this.scallback(rowIdList , rowDataList);
        }
        //关闭浮层
        layer.close(this.layerIndex);
        this.courseId = null;//课程ID
        this.selectIds = null;//
    }
}
adlSectionMutiSelectLay.init();
