$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/courses/list',
        datatype: "local",
        colModel: [
            {label: 'ID', name: 'courseId', width: 30, key: true},
            {label: '课程编号', name: 'courseNo', width: 80},
            {label: '课程名称', name: 'courseName', width: 100},
            {label: '课程类别', name: 'courseLb', width: 60},
            {label: '课程类型', name: 'courseType', width: 60},
            {label: '考试方式', name: 'examType', width: 50},
            {label: '学分', name: 'courseCredit', width: 30},
            {label: '直播课', name: 'liveNum', width: 40},
            {
                label: '录播课', name: 'zRecordNum', width: 40, formatter: function (value, options, row) {
                    var zRecordNum = row.zRecordNum || 0;
                    var jRecordNum = row.jRecordNum || 0;
                    return zRecordNum + "章|" + jRecordNum + "节";
                }
            },
            {label: '产品线', name: 'productName', width: 80},
            {
                label: '排课可冲突', name: 'courseEq', width: 50, formatter: function (value, options, row) {
                    return value === 0 ? '<span class="label label-danger">不可冲突</span>' : '<span class="label label-success">可以冲突</span>';
                }
            },
            {
                label: '是否是双师课程', name: 'status', width: 50, formatter: function (value, options, row) {
                    return value === 0 ? '<span class="label label-danger">不是</span>' : '<span class="label label-success">是</span>';
                }
            }
            /*{ label: '试听地址', name: 'listenUrl', width: 80 },*/
//			{ label: '创建用户', name: 'creationName', width: 80 },
//			{ label: '创建时间', name: 'creationTime', width: 80 },
//			{ label: '修改用户', name: 'modifiedName', width: 80 },
//			{ label: '修改时间', name: 'modifiedTime', width: 80 }
        ],
        viewrecords: true,
        height: 500,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 35,
        autowidth: true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader: {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });
    // ------------------------------------------------------------直播课基础课次 livejqGrid------------------------------------
    $("#livejqGrid").jqGrid({
        datatype: "local",
        url: '../mall/courses/liveList',
        colModel: [
            {label: 'ID', name: 'liveId', key: true, hidden: true},
            {label: '班型权限ID', name: 'liveClassTypeIds', width: 100, hidden: true},
            {label: '课次名称', name: 'liveName', width: 300},
            {label: '班型权限', name: 'liveClassTypeNames', width: 600, formatter: classTypeNameFormatter},
            {label: '排序', name: 'orderNum', width: 60},
            //app4.0.1注释掉字段
            {label: '上期复习', name: 'reviewUrl', width: 60},
            {label: '上期复习文件名', name: 'reviewName', width: 60},
            {label: '本次预习', name: 'prepareUrl', width: 60},
            {label: '本次预习文件名', name: 'prepareName', width: 60},
            {label: '课堂资料', name: 'coursewareUrl', width: 120},
            {label: '课堂资料文件名', name: 'coursewareName', width: 60},
            /* {label : '练习阶段ID',name : 'practiceStageId',width : 120,hidden : true},*/
            {label: '阶段ID', name: 'examStageId', width: 50},
            {label: '阶段名称', name: 'examStageName', width: 120}
        ],
        viewrecords: true,
        height: 500,
        rowNum: 10,
        rowList: [10, 30, 50],
        rownumbers: true,
        rownumWidth: 35,
        autowidth: true,
        multiselect: false,
        pager: "#livejqGridPager",
        jsonReader: {
            root: "data.list",
            page: "data.currPage",
            total: "data.totalPage",
            records: "data.totalCount"
        },
        prmNames: {
            page: "page",
            rows: "limit",
            order: "order"
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#livejqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        }
    });

    //班型权限格式化
    function classTypeNameFormatter(cellvalue, options, rowObject) {
        if (null != rowObject && null != rowObject.liveClassTypeIds && rowObject.liveClassTypeIds.length > 0) {
            return vm.getLiveClassTypeNames(rowObject.liveClassTypeIds);
        }
        return "";
    }

    // ------------------------------------------------------------录播课 recordjqGrid------------------------------------
    var record_setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "parentId",
                rootPId: 0
            },
            key: {
                url: "nourl"
            }
        }
    };
    var record_ztree;


    function getRecordId() {
        var selected = $('#recordTable').bootstrapTreeTable('getSelections');
        if (selected.length == 0) {
            alert("请选择一条记录");
            return false;
        } else {
            return selected[0].id;
        }
    }

});


var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            courseName: "",
            courseNo: "",
            productName: "",
            productId: ""
        },
        showType: 1,//1 课程List 2.课程编辑  3.直播课基础课次  4.录播课
        title: null,
        classTypeList: [],
        classTypeMap: {},
        courses: {
            courseEq: 0,
            courseName: "",
            courseNo: "",
            productId: "",
            productName: "",
            status: 0
        },
        courseId: null,
        ///////////////////////////////////////////////////////////////////////直播课参数
        liveDetail: {
            obj: {
                liveId: "",//基础课次主键
                liveClassTypeIds: "",//班型
                liveClassTypeIdsArray: [],//班型
                liveName: "",//基础课次名称
                orderNum: "",//排序
                courseId: "",//课程主表外键
                examStageId: "",//阶段ID
                examStageName: ""//阶段名称
            },
            title: null
        },
        ////////////////////////////////////////////////////////////////////////录播课参数
        //录播课树列表参数
        Record: {
            id: "recordTable",
            table: null,
            layerIndex: -1,

            initColumn: function () {
                var columns = [
                    {field: 'selectItem', radio: true},
                    {
                        title: '录播课ID',
                        field: 'recordId',
                        visible: false,
                        align: 'center',
                        valign: 'middle',
                        width: '30px'
                    },
                    {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '100px'},
                    {title: '老师', field: 'userName', align: 'center', valign: 'middle', sortable: true, width: '70px'},
                    {title: '时长', field: 'duration', align: 'center', valign: 'middle', sortable: true, width: '40px'},
                    {
                        title: '章节',
                        field: 'type',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                        width: '30px',
                        formatter: function (value, row, index) {
                            return value.type == 0 ? "章" : "节";
                        }
                    },
                    {title: '视频ID', field: 'vid', align: 'center', valign: 'middle', sortable: true, width: '100px'},
                    {title: '排序', field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '30px'},
                    {title: '文件地址', field: 'fileUrl', align: 'center', valign: 'middle', sortable: true, width: '30px'},
                    {title: '文件名', field: 'fileName', align: 'center', valign: 'middle', sortable: true, width: '30px'}
                    ]
                return columns;
            }
        },
        //录播课编辑
        recordDetail: {
            title: "",//标题
            obj: {
                recordId: "",//主键
                courseId: "",//课程PK
                duration: "",//时长
                firstImage: "",//封面
                name: "",//名称
                orderNum: 1,//排序
                parentId: 0,//上级ID
                ptime: "",//上传时间
                type: 0,//类型
                userId: "",//教师ID
                userName: "",//教师名称
                vid: "",//视频ID
                polyvName: "",//视频名称
                fileUrl:"",//文件地址
                fileName:""//文件名
            }
        }
    },
    methods: {
        rest: function () {//重置
            vm.q = {
                courseName: "",
                courseNo: "",
                productName: "",
                productId: ""
            };
        },
        query: function () {
            vm.reload(null, 1);
        },
        add: function () {
            vm.showType = 2;
            vm.title = "新增";
            url = "../mall/courses/save";
            vm.courses = {
                courseEq: 0,
                productId: "",
                productName: "",
                status: 0
            };
            jQuery("#detailGrid").jqGrid("clearGridData");
        },
        update: function (event) {
            var courseId = getSelectedRow();
            if (courseId == null) {
                return;
            }
            vm.showType = 2;
            vm.title = "修改";
            url = "../mall/courses/update";
            jQuery("#detailGrid").jqGrid("clearGridData");
            vm.getInfo(courseId);
        },
        saveOrUpdate: function (event) {
            //参数校验
            if (null == vm.courses.courseName || vm.courses.courseName == "") {
                alert("请输入课程名称！！！");
                return;
            }
            if ($.isNull(vm.courses.courseNo)) {
                alert("请输入课程编号！！！");
                return;
            }
            if ($.isNull(vm.courses.productId)) {
                alert("请选择产品线！！！");
                return;
            }
            if (vm.title == "新增") {
                url = "../mall/courses/save";
            }
            else if (vm.title == "修改") {
                url = "../mall/courses/update";
            } else {
                url = "";
            }

            //子表所有数据
            var details = [];
            var ids = $("#detailGrid").jqGrid('getDataIDs');
            for (var i = 0; i < ids.length; i++) {
                var row = $('#detailGrid').jqGrid('getRowData', ids[i]);
                if (isNaN(row.id)) {
                    row.id = null;
                }
                details.push(row);
            }

            vm.courses.liveDetail = details;
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.courses),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var courseId = getSelectedRow();
            if (courseId == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "GET",
                    url: "../mall/courses/delete/" + courseId,
                    /* data: JSON.stringify(courseIds),*/
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getInfo: function (courseId) {
            $.get("../mall/courses/info/" + courseId, function (r) {
                vm.courses = r.courses;
                var detailList = r.detailList;
                if (null != detailList && detailList.length > 0) {
                    $.each(detailList, function (i, v) {
                        //添加行
                        $("#detailGrid").addRowData(v.liveId, v, "last");
                    });
                }
            });
        },
        reload: function (event, p) {
            vm.showType = 1;
            var page = p || $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                datatype: "json",
                postData: {
                    "courseName": vm.q.courseName,
                    "courseNo": vm.q.courseNo,
                    "productId": vm.q.productId
                },
                page: page
            }).trigger("reloadGrid");
        },
        getclassTypeList: function () {//获取班型列表
            $.get("../mall/classtype/select", function (data) {
                vm.classTypeList = data.data;
                $.each(vm.classTypeList, function (i, v) {
                    vm.classTypeMap[v.value] = v.name;
                });
            });
        },
        getLiveClassTypeNames: function (ids) {//
            var str = "";
            if (null == ids || ids.length == 0) {
                return str;
            }
            if (!$.isArray(ids)) {
                ids = ids.split(",");
            }
            $.each(ids, function (i, val) {
                str += vm.classTypeMap[val] + ";";
            });
            return str.length > 0 ? str.substring(0, str.length - 1) : "";
        },
        goList: function () {//返回到课程列表
            vm.reload();
        },
        selectProduct: function () {//编辑-产品线
            productLay.show(function (index, opt) {
                vm.courses.productId = opt.productId;
                vm.courses.productName = opt.productName;
            });
        },
        selectProductQuery: function () {//查询条件-产品线
            productLay.show(function (index, opt) {
                vm.q.productId = opt.productId;
                vm.q.productName = opt.productName;
            });
        },
        ////////////////////////////////////////////////////直播基础课次操作//////////////////////////////////////////////
        liveList: function () {//直播基础课次-查询
            vm.courseId = getSelectedRow();
            if (vm.courseId == null) {
                return;
            }
            vm.liveRload(vm.courseId);
        },
        liveAdd: function () {//新增直播基础课次
            //弹框标题
            vm.liveDetail.title = "新增";
            vm.liveDetail.url = "../mall/courses/liveSave";

            //初始化弹窗数据
            vm.liveDetail.obj = {
                liveId: "",//基础课次主键
                liveClassTypeIds: "",//班型
                liveClassTypeIdsArray: [],//班型
                liveName: "",//基础课次名称
                orderNum: 10,//排序
                courseId: "",//课程主表外键
                reviewUrl: "",//上期复习
                prepareUrl: "",//本期预习
                coursewareUrl: "",//课堂资料
                reviewUrlName:"",//上期复习文件名
                prepareName: "",//本期预习文件名
                coursewareName: "",//课堂资料文件名
                practiceStageId: "",//练习阶段ID
                examStageId: "",//阶段ID
                examStageName: ""//阶段名称
            };
            vm.liveShow();
        },
        liveUpdate: function () {//修改直播基础课次
            vm.liveDetail.title = "修改";
            vm.liveDetail.url = "../mall/courses/liveUpdate";

            //获取选中行ID
            var selectDetail = getJqGridSelectedRow("livejqGrid");
            if (selectDetail == null) {
                return;
            }
            //行数据
            var rowData = $("#livejqGrid").jqGrid('getRowData', selectDetail);
            //str=>array
            rowData.liveClassTypeIdsArray = rowData.liveClassTypeIds.split(",");
            //
            vm.liveDetail.obj = rowData;

            vm.liveShow();
        },
        liveShow: function () {//直播基础课次编辑弹窗
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: vm.liveDetail.title,
                area: ['600px', '440px'],
                shadeClose: false,
                content: jQuery("#liveLayer"),
                btn: ['提交', '取消'],
                btn1: function (layerIndex) {
                    //非空校验
                    //课次名称
                    var liveName = vm.liveDetail.obj.liveName;
                    if (null == liveName || liveName == "") {
                        alert("课次名称不能为空");
                        return;
                    }
                    var liveClassTypeIds = vm.liveDetail.obj.liveClassTypeIdsArray;
                    if (null == liveClassTypeIds || liveClassTypeIds.length == 0) {
                        alert("请选择班型权限");
                        return;
                    }
                    //[1,2,3,4]==>1,2,3,4
                    vm.liveDetail.obj.liveClassTypeIds = vm.liveDetail.obj.liveClassTypeIdsArray.toString();
                    //课程
                    vm.liveDetail.obj.courseId = vm.courseId;
                    $.ajax({
                        type: "POST",
                        url: vm.liveDetail.url,
                        data: JSON.stringify(vm.liveDetail.obj),
                        success: function (r) {
                            if (r.code === 0) {
                                alert('操作成功', function (index) {
                                    vm.liveRload();
                                    layer.close(layerIndex);
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        liveDelete: function () {//删除直播课基础课次
            var liveId = getJqGridSelectedRow("livejqGrid");
            if (liveId == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "GET",
                    url: "../mall/courses/liveDelete/" + liveId,
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                $("#livejqGrid").trigger("reloadGrid");
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        liveRload: function (courseId, p) {//课程基础课次查询
            vm.courseId = courseId || vm.courseId;
            vm.showType = 3;
            //清空表格数据
            jQuery("#livejqGrid").jqGrid("clearGridData");
            var page = p || $("#livejqGrid").jqGrid('getGridParam', 'page');
            $("#livejqGrid").jqGrid('setGridParam', {
                datatype: "json",
                postData: {
                    "courseId": vm.courseId
                },
                page: page
            }).trigger("reloadGrid");
        },
        liveQuery: function () {//刷新
            vm.liveRload(vm.courseId, 1);
        },
        //////////////////////////////////////////////////录播课操作//////////////////////////////////////////////
        recordList: function () {//录播课
            vm.courseId = getSelectedRow();
//			vm.recordQuery(vm.courseId);
            if (vm.courseId == null) {
                return;
            }

            vm.showType = 4;

            var colunms = vm.Record.initColumn();
            var table = new TreeTable(vm.Record.id, "../mall/courses/recordList/" + vm.courseId, colunms);
            table.setRootCodeValue(0);//设置根节点code的值
            table.setExpandColumn(2);//设置在哪一列上面显示展开按钮，从0开始
            table.setIdField("recordId");//设置记录返回的id值
            table.setCodeField("recordId");//设置记录分级的字段
            table.setParentCodeField("parentId");//设置记录分级的父级字段
            table.setExpandAll(true);//设置是否默认全部展开
            table.init();
            vm.Record.table = table
        },
        recordQuery: function (courseId) {//刷新
            vm.Record.table.refresh();
        },
        recordAddZ: function () {//新增章
            vm.recordDetail.title = "新增章";//标题
            vm.recordDetail.url = "../mall/courses/recordSave";//URL
            //录播课编辑
            vm.recordDetail.obj = {
                recordId: "",//主键
                courseId: vm.courseId,//课程PK
                duration: "",//时长
                firstImage: "",//封面
                name: "",//名称
                orderNum: 1,//排序
                parentId: 0,//上级ID
                ptime: "",//上传时间
                type: 0,//类型 0.章 1.节
                userId: "",//教师ID
                userName: "",//教师名称
                vid: "",//视频ID
                polyvName: "",//视频名称
                fileUrl:"",//文件地址
                fileName:""//文件名
            };
            vm.recordShow();
        },
        recordAddJ: function () {//新增节
            var recordId = vm.getRecordId();
            if (recordId == null || !recordId) {
                return;
            }
            $.get("../mall/courses/recordInfo/" + recordId, function (r) {
                if (r.code == 0) {
//					vm.recordDetail.obj = r.data;
                    vm.recordDetail.title = "新增节";//标题
                    vm.recordDetail.url = "../mall/courses/recordSave";//URL
                    //上级ID
                    var parentId = r.data.recordId;
                    if (r.data.type == 1) {
                        parentId = r.data.parentId;
                    }
                    //录播课编辑
                    vm.recordDetail.obj = {
                        recordId: "",//主键
                        courseId: vm.courseId,//课程PK
                        duration: "",//时长
                        firstImage: "",//封面
                        name: "",//名称
                        orderNum: 1,//排序
                        parentId: parentId,//上级ID
                        ptime: "",//上传时间
                        type: 1,//类型 0.章 1.节
                        userId: "",//教师ID
                        userName: "",//教师名称
                        vid: "",//视频ID
                        polyvName: "",//视频名称
                        fileUrl:"",//文件地址
                        fileName:""//文件名
                    };
                    vm.recordShow();

                } else {
                    alert(r.msg);
                }
            });

        },
        recordUpdate: function () {
            var recordId = vm.getRecordId();
            if (recordId == null || !recordId) {
                return;
            }

            $.get("../mall/courses/recordInfo/" + recordId, function (r) {
                if (r.code == 0) {
                    vm.recordDetail.obj = r.data;
                } else {
                    alert(r.msg);
                }
            });
            vm.recordDetail.title = "编辑章节"; //标题
            vm.recordDetail.url = "../mall/courses/recordUpdate";//URL
            vm.recordShow();

        },
        recordShow: function () {//弹出编辑或新增框
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: vm.recordDetail.title,//标题
                area: ['600px', '440px'],
                shadeClose: false,
                content: jQuery("#recordLayer"),
                btn: ['提交', '取消'],
                btn1: function (layerIndex) {
                    //非空校验
                    //名称
                    if ($.isNull(vm.recordDetail.obj.name)) {
                        alert("名称不能为空");
                        return;
                    }
                    if ($.isNull(vm.recordDetail.obj.orderNum)) {
                        alert("排序不能为空");
                        return;
                    }
                    $.ajax({
                        type: "POST",
                        url: vm.recordDetail.url,
                        data: JSON.stringify(vm.recordDetail.obj),
                        success: function (r) {
                            if (r.code === 0) {
                                alert('操作成功', function (index) {
                                    vm.recordQuery();
                                    layer.close(layerIndex);
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        recordDelete: function () {//删除录播课
            var recordId = vm.getRecordId();
            if (recordId == null || !recordId) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "GET",
                    url: "../mall/courses/recordDelete/" + recordId,
                    success: function (r) {
                        if (r.code == 0) {
                            alert('操作成功', function (index) {
                                vm.recordQuery();
                            });
                        } else {
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        getRecordId: function () {//获取选中的录播课
            var selected = $('#recordTable').bootstrapTreeTable('getSelections');

            if (selected.length == 0) {
                alert("请选择一条记录");
                return false;
            } else {
                return selected[0].id;
            }
        },
        selectTeacher: function () {//授课老师弹窗
            teacherLay.show(function (index, opt) {
                vm.recordDetail.obj.userId = opt.userId;
                vm.recordDetail.obj.userName = opt.nickName;
            });
        },
        selectVideo: function () {//录播视频弹窗
            polyVideoLay.show(vm.courseId, function (index, opt) {
                vm.recordDetail.obj.duration = opt.duration;//时长
                vm.recordDetail.obj.firstImage = opt.first_image;//封面
                vm.recordDetail.obj.name = vm.recordDetail.obj.name || opt.title;//章节名称
                vm.recordDetail.obj.ptime = opt.ptime;//上传时间
                vm.recordDetail.obj.vid = opt.vid;//vid
                vm.recordDetail.obj.polyvName = opt.title;//视频名称
            });
        },
        //根据课程获取阶段Id
        selectPhaseForDetail: function () {
            phaseLay.show(vm.courseId, function (index, opt) {
                /*alert(opt.phaseId);*/
                vm.liveDetail.obj.examStageId = opt.phaseId;
                vm.liveDetail.obj.examStageName = opt.phaseName;
            });
        },
        clearRecordDetail: function () {//清空录播课
            vm.recordDetail.obj.duration = "";//时长
            vm.recordDetail.obj.firstImage = "";//封面
//			vm.recordDetail.obj.name = "";//章节名称
            vm.recordDetail.obj.ptime = "";//上传时间
            vm.recordDetail.obj.vid = "";//vid
            vm.recordDetail.obj.polyvName = "";//视频名称
        },
        clearTeacher: function () {//清空教学老师
            vm.recordDetail.obj.userId = "";
            vm.recordDetail.obj.userName = "";
        }
    }
    , created: function () {
        this.getclassTypeList();
    }
});