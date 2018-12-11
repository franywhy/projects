$(function () {
    $(".dateValidity").datetimepicker({
        format: 'yyyy-mm-dd',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn: 1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        todayHighlight: 1,//如果为true, 高亮当前日期。
        startView: 3,//日期时间选择器打开之后首先显示的视图。
        forceParse: 0,//当选择器关闭的时候，是否强制解析输入框中的值。
        minView: 2,//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
        showMeridian: 1
    });
    $("#jqGrid").jqGrid({
        url: '../mall/order/list',
        datatype: "local",
        colModel: [
            {label: '订单ID', name: 'orderId', hidden: true, width: 50, key: true},
            {label: '订单号', name: 'orderNo', width: 80},
//			{ label: '用户ID', name: 'userId', width: 80 }, 	
            {label: '学员昵称', name: 'nickName', width: 80},
            {label: '手机号', name: 'mobile', width: 90},
//			{ label: '来源', name: 'sourceType', width: 80 }, 
//			{ label: '订单名称', name: 'orderName', width: 80 }, 	
//			{ label: '订单描述', name: 'orderDescribe', width: 80 }, 
            {
                label: '学员规划', name: 'userplanCount', width: 60, formatter(value, options, row) {
                    return value === 0 ? '<span class="label label-danger">未生成</span>'
                        : '<span class="label label-success">已生成</span>';
                }
            },
            {label: '专业', name: 'professionName', width: 60},
            {label: '商品ID', name: 'commodityId', width: 50},
            {label: '商品名称', name: 'commodityName', width: 80},
//			{ label: '订单总额', name: 'totalMoney', width: 80 }, 			
            {label: '支付金额', name: 'payMoney', width: 80},
//			{ label: '优惠金额', name: 'favorableMoney', width: 80 }, 
            //0.未支付 1.发起支付 ,2.支付成功
            {
                label: '支付状态', name: 'payStatus', width: 60, formatter(value, options, row) {
                    if (0 == value) return "未支付";
                    else if (1 == value) return "发起支付";
                    else if (2 == value) {
                        return "支付成功";
                    }
                }
            },
//			{ label: '支付方式', name: 'payType', width: 80 },
//			{ label: '支付宝交易号', name: 'alipayTradeNo', width: 80 }, 
            {label: '支付时间', name: 'payTime', width: 80},
            {label: '省份', name: 'areaName', width: 50},
            {label: '层次', name: 'levelName', width: 40},
//			{ label: 'NCID', name: 'ncId', width: 80 },
            {label: '报名表号', name: 'ncCode', width: 80},
//			{ label: '创建时间', name: 'creationTime', hidden: true, width: 80 },
            {label: '班型', name: 'classtypeName', width: 80},
            {label: '班级', name: 'className', width: 80},
            {label: '产品线', name: 'productName', width: 80},
            {label: '部门', name: 'deptName', width: 50},
            {label: '教师角色', name: 'isTeacher', hidden: true},
            {label: '角色', name: 'isTeacherStr', width: 50},
            {
                label: '状态', name: 'commodityId', width: 60, formatter(value, options, row) {
                    if (1522 == value || 1523 == value || 1524 == value) {
                        return '<span class="label label-danger">关闭</span>';
                    }
                    return '<span class="label label-success">正常</span>';
                }
            },
            {
                label: '有效期', name: 'dateValidity', width: 80, formatter: function (v, options, row) {
                    if (!$.isNull(v)) {
                        var d1 = new Date(v.replace(/\-/g, "\/"));
                        var d2 = new Date();
                        var text = "";
//					计算相差天数
                        if (d1 > d2) {
                            var dt1 = d1.getTime();
                            var dt2 = d2.getTime();
                            var dayNum = parseInt(Math.abs(dt1 - dt2) / 1000 / 60 / 60 / 24);
                            text = '<span  class="label label-success" style="font-weight:bold;font-size:10px;">' + v.substring(0, 10) + " (" + dayNum + ')</span>';
                        } else {
                            text = '<span class="label label-danger">' + v.substring(0, 10) + ' (0)</span>';
                        }
                        return text;
                    } else {
                        return "-";
                    }
                }
            },
            {
                label: '签署状态', name: 'signStatus', width: 60, formatter(value, options, row) {
                    if (value == 2) {
                        return '<span class="label label-danger">已驳回</span>';
                    }
                    if (value == 1) {
                        return '<span class="label label-success">已签署</span>';
                    }
                    return '<span class="label label-danger">未签署</span>';
                }
            },
            {
                label: '在线签约时间', name: 'contractTs', width: 80, formatter(value, options, row) {
                    if (row.signStatus == 1) {
                        return value;
                    } else {
                        return "";
                    }
                }
            },
            {
                label: '协议详情', name: 'viewDetail', width: 120, formatter: function (value, options, row) {
                    return row.contractId == null || row.signStatus != 1 ? '' : '<button style="width:50px;height:25px" onclick="viewDetail(' + row.contractId + ',' + row.companyId + ' )">查看</button> <button style="width:50px;height:25px" onclick="refuseSign(' + row.contractId + ')">驳回</button>';
                }
            }
//			{ label: '状态', name: 'status', width: 80 },	
//			{ label: '备注', name: 'remark', hidden: true, width: 80 }
        ], 
        viewrecords: true,
        height: 500,
        rowNum: 10,
        rowList: [10, 30, 50, 500, 5000],
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
});

function refuseSign(contractId) {
    var params = {"contractId": contractId, "status": 2};
    console.log(params)
    confirm('驳回后学员需重签，确认驳回？', function () {
        $.ajax({
            type: "POST",
            url: "../contract/updateByContract",
            data: JSON.stringify(params),
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

//	layer.confirm("驳回后学员需重签，确认驳回？",{
//		  btn: ['确认','取消'] //按钮
//	 },function(index){
//		 console.log("666");
//		 layer.closeAll('dialog');
//		 vm.reload();
//	 }); 


}

function viewDetail(contractId, companyId) {
    var netTab = window.open('about:blank');
    var params = {"companyId": companyId};
    var tokenUnableListener = function (obj) {//当token不合法时，SDK会回调此方法
        console.log(JSON.stringify(companyId));
        $.ajax({
            type: 'POST',
            url: '/contract/getToken',
            cache: false,
            dataType: 'json',
            data: JSON.stringify(params), //第三方获取token需要的参数
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
            },
            success: function (data, textStatus, request) {
                YHT.setToken(data.msg); //重新设置token，从请求头获取
                YHT.do(obj);//调用此方法，会继续执行上次未完成的操作
            },
            error: function (data) {
                alert(data);
            }
        });
    }

    YHT.init("AppID", tokenUnableListener); //必须初始化YHT

    console.log(contractId);
    //获取预览合同的URL
    var url = 'www.baidu.com'
    console.log(YHT)
    YHT.queryContract(
        function successFun(url) {
            netTab.location.href = url;
            YHT.setToken('eyJhbGciOiJIUzUxMiJ9.eyJqdGkiOiJiR0JLbGM2Z0pSczRnUXFsNkpIc2xRQ2lwZkswSjRVOVRNRHZqOHBYdCs4VlczSW5mK1B0U2I4aVgwdC9ZdlBPdWNQYzNVKzdFUkR3bFlaZld4aVNIZz09IiwiZXhwIjoxNTQzMzk1OTY3fQ.p-tx3pzVxjUjjcmXq07GE7bZSxqgZ2ilT-KpFbRDp_KhABFB2KhXgbn5SwNUnUmnTRz3oycmL8eXiy-Pepx1-w');
        },
        function failFun(data) {
            console.log(data);
        },
        contractId
    );

}

var vm = new Vue({
    el: '#rrapp',
    data: {
        q: {
            orderNo: "",
            orderId: "",
            name: "",
            id: "",
            mobile: "",
            nickName: "",
            userplanType: "",
            classId: "",
            className: "",
            classTypeId: "",
            classTypeName: "",
            levelId: "",
            levelName: "",
            areaId: "",
            areaName: "",
            professionId: "",
            professionName: "",
            originalPrice: "",
            originPath: "",
            thumbPath: "",
            ncId: "",
            ncCode: "",
            productId: "",
            productName: "",
            deptId: "",
            deptName: "",
            deptIdList: "",
            deptNameList: "",
            signStatus: "",
        },
        showList: true,
        flag: true,
        flagDvalidity: true,
        showOrderList: true,
        showOrderAttempt: true,
        chooseRole: true,
        title: null,
        mallOrder: {
            orderId: "",
            //用户信息
            nickName: "",
            userId: "",
            //商品ID
            commodityId: "",
            //商品名称
            commodityName: "",
            //订单名称
            orderName: "",
            //订单描述
            orderDescribe: "",
            //订单总额
            totalMoney: "",
            payMoney: "",
            //商品图片
            pic: "",
            //商品小图
            spic: "",
            //层次
            levelId: "",
            levelName: "",
            //班型
            classTypeId: "",
            classTypeName: "",
            //专业
            professionId: "",
            professionName: "",
            //省份
            areaId: "",
            areaName: "",
            //班级
            classId: "",
            className: "",
            //产品线
            productId: "",
            productName: "",
            dateValidity: "",
            oldDateValidity: "",
            orderList: [],  //订单id集合
            onlyOne: "", //是否开通题库权限
            isTeacher: ""
        },
        courseUserplanNewChange: {
            orderId: "",
            commodityId: "",
            commodityName: "",
            areaId: "",
            areaName: "",
            classTypeId: "",
            classTypeName: "",
            levelId: "",
            levelName: "",
            professionId: "",
            professionName: ""
        },
        areaNewChange: {
            orderId: "",
            areaId: "",
            areaName: "",
        }
    },
    methods: {
        initData: function () {
            //加载班级数据
            $.getJSON("../mall/class/selectList/", function (r) {
                if (r.code === 0) {
                    var selData = r.data;
                    if (null != selData && selData.length > 0) {
                        var str = "";
                        $.each(selData, function (i, v) {
                            str += '<option value="' + v.classId + '">' + v.className + '</option>';
                        });
                        $("#selClass").empty().html(str);
                    }
                } else {
                    alert(r.msg);
                }

            });
        },
        clearQuery: function () {//清空查询条件
            vm.q = {
                orderNo: "",
                name: "",
                mobile: "",
                nickName: "",
                userId: "",
                userplanType: "",
                classId: "",
                className: "",
                classTypeId: "",
                classTypeName: "",
                levelId: "",
                levelName: "",
                areaId: "",
                areaName: "",
                professionId: "",
                professionName: "",
                originalPrice: "",
                id: "",
                originPath: "",
                thumbPath: "",
                ncId: "",
                ncCode: "",
                productName: "",
                productId: "",
                deptId: "",
                deptName: "",
                deptIdList: "",
                deptNameList: "",
                signStatus: "",
            };
        },
        query: function () {
            vm.reload(null, 1);
        },
        add: function () {
            vm.showList = false;
            vm.flag = true;
            vm.flagDvalidity = true;
            vm.showOrderList = false;
            vm.showOrderAttempt = true;
            vm.chooseRole = true,
                vm.title = "新增";
            vm.mallOrder = {
                //用户信息
                nickName: "",
                userId: "",
                //商品ID
                commodityId: "",
                //商品名称
                commodityName: "",
                //订单名称
                orderName: "",
                //订单描述
                orderDescribe: "",
                //订单总额
                totalMoney: "",
                payMoney: "",
                //商品图片
                pic: "",
                //商品小图
                spic: "",
                //层次
                levelId: "",
                levelName: "",
                //班型
                classTypeId: "",
                classTypeName: "",
                //专业
                professionId: "",
                professionName: "",
                //省份
                areaId: "",
                areaName: "",
                //班级
                classId: "",
                className: "",
                deptId: "",
                deptName: "",
                productId: ""

            };
        },
        update: function (event) {
            var orderIds = getSelectedRows();
            if (orderIds == null) {
                return;
            }
            vm.mallOrder.orderList = orderIds;
            vm.showList = false;
            vm.flag = false;
            vm.flagDvalidity = true;
            vm.showOrderList = true;
            vm.showOrderAttempt = true;
            vm.chooseRole = true;
            vm.title = "修改";

            //vm.getInfo(orderId);
        },
//		addAttempt: function (event) {
//			vm.showList =false;
//			vm.flag=true;
//			vm.showOrderAttempt =false;
//			vm.showOrderList=true;
//			vm.title = "订单体验";
//			vm.mallOrder = {};
//		},
        updateDateValidity: function (event) {
            var orderId = getSelectedRow();
            if (orderId == null) {
                return;
            }
            vm.showList = false;
            vm.flag = true;
            vm.flagDvalidity = false;
            vm.showOrderList = true;
            vm.showOrderAttempt = true;
            vm.chooseRole = true;
            vm.title = "修改订单有效期";

            vm.getInfo(orderId)
        },
//		addAttempt: function (event) {
//			vm.showList =false;
//			vm.flag=true;
//			vm.showOrderAttempt =false;
//			vm.showOrderList=true;
//			vm.title = "订单体验";
//			vm.mallOrder = {};
//		},
        saveOrUpdate: function (event) {


            if (vm.title == "新增") {
                url = "../mall/order/save";
            }
            else if (vm.title == "修改") {
                url = "../mall/order/update";
            }
            else if (vm.title == "修改订单有效期") {
                vm.mallOrder.dateValidity = $("#dateValidity").val();
                url = "../mall/order/updateDvalidate";
            }
            else {
                url = "";
            }
            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.mallOrder),
                success: function (r) {
                    if (r.code === 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                            window.location.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        },
        del: function (event) {
            var orderIds = getSelectedRows();
            if (orderIds == null) {
                return;
            }

            confirm('确定要删除选中的记录？', function () {
                $.ajax({
                    type: "POST",
                    url: "../mall/order/delete",
                    data: JSON.stringify(orderIds),
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
        getInfo: function (orderId) {
            $.get("../mall/order/info/" + orderId, function (r) {
                vm.mallOrder = r.mallOrder;
                vm.mallOrder.orderId = orderId;
                //vm.mallOrder.orderList=orderId;
                vm.mallOrder.oldDateValidity = r.mallOrder.dateValidity;
                $("#dateValidity").val(vm.mallOrder.dateValidity.substring(0, 10));
            });
        },
        reload: function (event, p) {
            var vm = this;
            vm.showList = true;
            vm.flag = true;
            vm.flagDvalidity = true;
            vm.showOrderList = true;
            vm.showOrderAttempt = true;
            vm.chooseRole = true;
            var page = p || $("#jqGrid").jqGrid('getGridParam', 'page');
            $("#jqGrid").jqGrid('setGridParam', {
                datatype: "json",
                postData: {
                    "orderNo": vm.q.orderNo,
                    "mobile": vm.q.mobile,
                    "nickName": vm.q.nickName,
                    "classId": vm.q.classId,
                    "classTypeId": vm.q.classTypeId,
                    "levelId": vm.q.levelId,
                    "areaId": vm.q.areaId,
                    "professionId": vm.q.professionId,
                    "userplanType": vm.q.userplanType,
                    "id": vm.q.id,
                    "ncId": vm.q.ncId,
                    "ncCode": vm.q.ncCode,
                    "productId": vm.q.productId,
                    "deptId": vm.q.deptId,
                    "deptIdList": vm.q.deptIdList,
                    "signStatus": vm.q.signStatus,
                },
                page: page
            }).trigger("reloadGrid");
        },
        classLayerShow: function () {//班级浮层
            classLay.show(function (index, opt) {
                vm.mallOrder.classId = opt.classId;
                vm.mallOrder.className = opt.className;
                vm.mallOrder.productId = opt.productId;
            });
        },
        classLayerShowQuery: function () {//班级浮层
            classLay.show(function (index, opt) {
                vm.q.classId = opt.classId;
                vm.q.className = opt.className;
            });
        },
        classTypeLayerShow: function () {//班型浮层
            classTypeLay.show(function (index, opt) {
                vm.q.classTypeId = opt.classtypeId;
                vm.mallOrder.classTypeId = opt.classtypeId;
                vm.q.classTypeName = opt.classtypeName;
            });
        },
        levelLayerShow: function () {//层次浮层
            levelLay.show(function (index, opt) {
                vm.q.levelId = opt.levelId;
                vm.q.levelName = opt.levelName;
                vm.mallOrder.levelId = opt.levelId;
            });
        },
        areaLayerShow: function () {//省份浮层
            areaLay.show(function (index, opt) {
                vm.q.areaId = opt.areaId;
                vm.q.areaName = opt.areaName;
            });
        },
        areaLayerShow2: function () {//转省份浮层
            areaLay.show(function (index, opt) {
                vm.areaNewChange.areaId = opt.areaId;
                vm.areaNewChange.areaName = opt.areaName;
            });
        },
        userInfoLayerShow: function () {//用户信息浮层
            userInfoLay.show(function (index, opt) {
                vm.mallOrder.userId = opt.userId;
                vm.mallOrder.nickName = opt.nickName;
            });
        },
//		productLayerShowQuery : function(){//产品线信息浮层
//			productLay.show(function(index,opt){
//				vm.q.productId = opt.productId;
//				vm.q.productName = opt.productName;
//			});
//		},
        goodsInfoLayerShow: function () {//商品信息浮层
            goodsInfoLay.show(function (index, opt) {
                //商品ID
                vm.mallOrder.commodityId = opt.id;
                //商品名称
                vm.mallOrder.commodityName = opt.name;
                //订单名称
                vm.mallOrder.orderName = opt.name;
                //订单描述
                vm.mallOrder.orderDescribe = opt.name;
                //订单总额
                vm.mallOrder.totalMoney = opt.originalPrice;
                vm.mallOrder.payMoney = opt.originalPrice;
                //商品图片
                vm.mallOrder.pic = opt.thumbPath;
                //商品小图
                vm.mallOrder.spic = opt.originPath;
                //层次
                vm.mallOrder.levelId = opt.levelId;
                vm.mallOrder.levelName = opt.levelName;
                //班型
                vm.mallOrder.classTypeId = opt.classTypeId;
                vm.mallOrder.classTypeName = opt.classTypeName;
                //专业
                vm.mallOrder.professionId = opt.professionId;
                vm.mallOrder.professionName = opt.professionName;
                //是否开通题库权限
                vm.mallOrder.onlyOne = opt.onlyOne;
            });
        },
        areaGoodsLayerShow: function () {//根據商品查詢省份浮层
            //非空
            if ($.isNull(vm.mallOrder.commodityId)) {
                alert("先选择商品再选择省份");
                return;
            }
            areaGoodsLay.show(vm.mallOrder.commodityId, function (index, opt) {
                vm.mallOrder.areaId = opt.areaId;
                vm.mallOrder.areaName = opt.areaName;
            });
        },
        professionLayerShow: function () {//专业浮层
            professionLay.show(function (index, opt) {
                vm.q.professionId = opt.professionId;
                vm.q.professionName = opt.professionName;
                vm.q.originalPrice = opt.originalPrice;
                vm.mallOrder.professionId = opt.professionId;

            });
        },
        productShowQuery: function () {//产品线查询条件
            productLay.show(function (index, opt) {
                vm.q.productId = opt.productId;
                vm.q.productName = opt.productName;
            });
        },
        deptLayerShowQuery: function () {//部门查询条件
            deptQueryLay.show(function (opt, deptIdList, deptNameList) {
                //vm.q.deptId = id;
                //vm.q.deptName = name;
                vm.q.deptIdList = deptIdList.join(",");
                vm.q.deptNameList = deptNameList;
                //alert(vm.q.deptIdList+"======")
            });
        },
        deptLayerShow: function () {//部门
            deptLay.show(function (id, name, opt) {
                vm.mallOrder.deptId = id;
                vm.mallOrder.deptName = name;
            });
        },
        updateChangeWin: function () {//转专业
            vm.courseUserplanNewChange = {
                commodityName: "",
                areaId: "",
                areaName: "",
                classTypeId: "",
                classTypeName: "",
                levelId: "",
                levelName: "",
                professionId: "",
                professionName: ""
            };
            //选择
            var orderId = getSelectedRow();
            if (orderId == null) {
                return;
            }
//			vm.courseUserplanOldChange.orderId = orderId;
            //转省转专业数据
            /*$.get("../course/userplan/info/"+userPlanId, function(r){
                vm.courseUserplanOldChange = r.courseUserplan;
            });*/
            vm.showChangeLayer(orderId);
        },
        updateChangeArea: function () {//转省份
            vm.areaNewChange = {
                orderId: "",
                areaId: "",
                areaName: "",
            };
            //选择
            var orderId = getSelectedRow();
            if (orderId == null) {
                return;
            }
            vm.showChangeAreaLayer(orderId);
        },
        showChangeLayer: function (orderId) {//转专业弹窗
            var vm = this;
            vm.courseUserplanNewChange.orderId = orderId;
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: '转专业',
                area: ['880px', '333px'],
                shadeClose: false,
                content: jQuery("#changelayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var orderId = orderId;
                    var commodityId = vm.courseUserplanNewChange.commodityId;
//					var areaId = vm.courseUserplanNewChange.areaId;
//					var classTypeId = vm.courseUserplanNewChange.classTypeId;
//					var levelId = vm.courseUserplanNewChange.levelId;
                    var professionId = vm.courseUserplanNewChange.professionId;
                    if (orderId == "") {
                        alert("请选择一条记录")
                        return;
                    }
                    if ($.isNull(commodityId)) {
                        alert("请选择商品")
                        return;
                    }
//					if($.isNull(areaId)){
//						alert("请选择省份")
//						return;
//					}
//					var postData = {
//							orderId : orderId,
//							commodityId : commodityId,
//							/*
//							areaId : areaId,
//							levelId : levelId,
//							classTypeId : classTypeId,
//							*/
//							professionId : professionId
//					};
                    $.ajax({
                        type: "GET",
                        url: "../mall/order/updateChange",
//					    data:  JSON.stringify(postData) ,
                        data: vm.courseUserplanNewChange,
                        success: function (r) {
                            if (r.code == 0) {
                                alert('操作成功', function (alertIndex) {
                                    vm.reload();
                                    //关闭浮层
                                    layer.close(index);
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        showChangeAreaLayer: function (orderId) {//转省份弹窗
            var vm = this;
            vm.areaNewChange.orderId = orderId;
            layer.open({
                type: 1,
                skin: 'layui-layer-molv',
                title: '转省份',
                area: ['333px', '333px'],
                shadeClose: false,
                content: jQuery("#changeArealayer"),
                btn: ['确定', '取消'],
                btn1: function (index) {
                    var orderId = orderId;
                    var areaId = vm.areaNewChange.areaId;
                    if (orderId == "") {
                        alert("请选择一条记录")
                        return;
                    }
//					if($.isNull(commodityId)){
//						alert("请选择商品")
//						return;
//					}
                    if ($.isNull(areaId)) {
                        alert("请选择省份")
                        return;
                    }
                    $.ajax({
                        type: "GET",
                        url: "../mall/order/updateChangeArea",
//					    data:  JSON.stringify(postData) ,
                        data: vm.areaNewChange,
                        success: function (r) {
                            if (r.code == 0) {
                                alert('操作成功', function (alertIndex) {
                                    vm.reload();
                                    //关闭浮层
                                    layer.close(index);
                                });
                            } else {
                                alert(r.msg);
                            }
                        }
                    });
                }
            });
        },
        selectCommodity: function () {//商品弹窗
            goodsInfoLay.show(function (index, opt) {
                vm.courseUserplanNewChange.commodityId = opt.id;
                vm.courseUserplanNewChange.commodityName = opt.name;//商品名
                /*
                vm.courseUserplanNewChange.classTypeId = opt.classTypeId;
                vm.courseUserplanNewChange.classTypeName = opt.classTypeName;//班型

                vm.courseUserplanNewChange.levelId = opt.levelId;
                vm.courseUserplanNewChange.levelName = opt.levelName;//层次
                */
                vm.courseUserplanNewChange.professionId = opt.professionId;
                vm.courseUserplanNewChange.professionName = opt.professionName;//专业
            });
        },
        chooseIsTeacher: function (event) {
            var orderId = getSelectedRow();
            if (orderId == null) {
                return;
            }
            var rowData = $("#jqGrid").jqGrid('getRowData', orderId);
            var isTeacher = rowData.isTeacher;
            console.log(isTeacher);
            $("input[name='roleRadiosinline'][value='" + isTeacher + "']").attr("checked", true);
            vm.mallOrder = rowData;
            vm.showList = false;
            vm.flag = true;
            vm.flagDvalidity = true;
            vm.showOrderList = true;
            vm.showOrderAttempt = true;
            vm.chooseRole = false;
            vm.title = "修改角色";

        },
        updateIsTeacher: function (event) {
            vm.mallOrder.isTeacher = $("input[name='roleRadiosinline']:checked").val();
            $.ajax({
                type: "GET",
                url: "../mall/order/updateIsTeacher",
                data: vm.mallOrder,
                success: function (r) {
                    if (r.code == 0) {
                        alert('操作成功', function (index) {
                            vm.reload();
                        });
                    } else {
                        alert(r.msg);
                    }
                }
            });
        }
    },
    created: function () {
        this.initData();
    }
});