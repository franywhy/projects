
$(function () {
    //时间空间初始化
    $(".datetimepicker_start").datetimepicker({
        format: 'yyyy-mm-dd 00:00:00',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
    });
    $(".datetimepicker_end").datetimepicker({
        format: 'yyyy-mm-dd 23:59:59',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
    });
    $(".datetimepicker_date").datetimepicker({
        format: 'yyyy-mm-dd',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
    });
    $(".datetimepicker_date").val(new Date().format("yyyy-MM-dd"));
    $(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
    $(".datetimepicker_end").val(new Date().format("yyyy-MM-dd 23:59:59"));

    $("#jqGrid").jqGrid({
        url: '../schoolReport/list',
        datatype: "json",
        colModel: [
            { label: '班主任ID', name: 'classTeacherId' , width: 10 ,formatter: function(value, options, row){
                var value = row.classTeacherId;
                if(value == null)value= '--';
                return value;
            }},
            { label: '班主任名称', name: 'classTeacherName', width: 40 },
            { label: '在读人数', name: 'learningNum', width: 20 },
            { label: '休学人数', name: 'pauseNum', width: 20},
            { label: '弃考人数', name: 'abandonNum', width: 20 },
            { label: '免考人数', name: 'exemptNum', width: 20 },
            { label: '失联人数', name: 'nonContactNum', width: 20 },
            { label: '其他', name: 'othersNum', width: 20 },
            { label: '实际总人数', name: 'totalNums', width: 20 },
            { label: '换算管理人数', name: 'manageNum', width: 20 },
            { label: '应出勤时间', name: 'fullDuration', width: 30,hidden:true },
            { label: '实际出勤时间', name: 'watchDuration', width: 30,hidden:true },
            { label: '出勤率', name: 'livePerTxt', width: 30 },
            { label: '创建时间', name: 'createTime', width: 30 ,formatter: function(value, options, row){
                var value = row.createTime;
                if(value == null)value= '--';
                return value;
            }},
            { label: '开始时间', name: 'startDate', width: 30 ,formatter: function(value, options, row){
                var value = row.startDate;
                if(value == null)value= '--';
                return value;
            }},
            { label: '结束时间', name: 'endDate', width: 30 ,formatter: function(value, options, row){
                var value = row.endDate;
                if(value == null)value= '--';
                return value;
            }},
        ],
        /*footerrow:true,*/
        viewrecords: true,
       /* rowList : [10,300,500,1000],*/
        height: 495,
        rowNum: 10000,
        rownumbers: true,
        rownumWidth: 35,
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
            //统计总数
            var learningNum = $(this).getCol('learningNum', false, 'sum');
            var pauseNum = $(this).getCol('pauseNum', false, 'sum');
            var abandonNum = $(this).getCol('abandonNum', false, 'sum');
            var exemptNum = $(this).getCol('exemptNum', false, 'sum');
            var nonContactNum = $(this).getCol('nonContactNum', false, 'sum');
            var othersNum = $(this).getCol('othersNum', false, 'sum');
            var totalNums = $(this).getCol('totalNums', false, 'sum');
            var manageNum = $(this).getCol('manageNum', false, 'sum');
            var livePerTxt = $(this).getCol('livePerTxt', false, 'avg');
            var fullDuration = $(this).getCol('fullDuration', false, 'sum');
            var watchDuration = $(this).getCol('watchDuration', false, 'sum');
            var totalLivePerText = isNaN(watchDuration/fullDuration) ? 0.00 : watchDuration/fullDuration;
            $(this).footerData('set',
                {"classTeacherId": "小计", "classTeacherName": "--","learningNum": learningNum,
                    "pauseNum": pauseNum,
                    "abandonNum": abandonNum,
                    "exemptNum": exemptNum,
                    "nonContactNum": nonContactNum,
                    "othersNum": othersNum,
                    "totalNums": totalNums,
                    "manageNum": manageNum,
                    "livePerTxt":  (totalLivePerText*100).toFixed(2)+"%",
                    "createTime": "--",
                    "startDate": "--",
                    "endDate": "--",
                });
        }
    });
});

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
            classTeacherId:null,//班主任
            classTeacherName:null,
            startDate:null,
            endDate:null,
            reportType:-1,
            createDate:null,
        },
        showList: true,
        title: null,
    },
    methods: {
        query: function () {
            var startDate = $(".datetimepicker_start").val();
            var endDate = $(".datetimepicker_end").val();
            var createDate = $(".datetimepicker_date").val();
           /* if($.isNull(startDate) || $.isNull(endDate)){
                alert("请选择时间范围");
                return;
            }*/
            if($.isNull(createDate)){
                alert("请选择生成时间");
                return;
            }
            vm.q.startDate = startDate;
            vm.q.endDate = endDate;
            vm.q.createDate = createDate;
            /*if($.isNull(vm.q.classTeacherId) && $.isNull(vm.q.classId)){
                alert("请选择班主任或班级");
                return;
            } else if(!$.isNull(vm.q.classTeacherId) && !$.isNull(vm.q.classId)){
                alert("班主任或班级只能选择其一");
                return;
            }*/
            vm.reload();
        },
        reload: function (m , p) {
            vm.showList = true;
            var page = p || $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData : vm.q,
                page:page,
                datatype: "json"
            }).trigger("reloadGrid");
        },

        classTeacherLayerShow : function(){//班主任
            teacherLay.show(function(index,opt){
                vm.q.classTeacherId = opt.userId;
                vm.q.classTeacherName = opt.nickName;
            } , 2);
        },

        clearQuery : function(){//清空查询条件
            var vm = this;
            vm.q = {
                classTeacherId:null,//班主任
                classTeacherName:null,
                startDate:null,
                endDate:null,
                reportType:-1,
                createDate:null,
            };
            //重置
            $(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
            $(".datetimepicker_end").val(new Date().format("yyyy-MM-dd 23:59:59"));
            $(".datetimepicker_date").val(new Date().format("yyyy-MM-dd"));
        },
    },
});