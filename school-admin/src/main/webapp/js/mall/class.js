$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/class/list',
        datatype: "json",
        colModel: [			
			{ label: '班级Id', name: 'classId', width: 30, key: true },
			{ label: '省份Id', name: 'areaId', hidden:true},
			{ label: '专业Id', name: 'professionId', hidden:true},
			{ label: '学历Id', name: 'levelId', hidden:true},
			{ label: '后台用户Id', name: 'userId', hidden:true},
			
			{ label: '班级名称', name: 'className', width: 80 }, 
			{ label: '省份', name: 'areaName', width: 40 }, 			
			{ label: '专业', name: 'professionName', width: 80 }, 			
			{ label: '学历', name: 'levelName', width: 40 }, 			
			{ label: '产品线', name: 'productName', width: 40 },
			{ label: '组织', name: 'deptName', width: 80 },
			{ label: '默认班级', name: 'defaultClassName', width: 80 ,formatter:function(value, options, row){
				return row.defaultClass === 0 ? 
						'<span class="label label-danger">正常</span>' : 
						'<span class="label label-success">默认班级</span>';
			}}, 			
			{ label: '班主任', name: 'classTeacherName', width: 80 }, 
			{ label: '备注', name: 'remake', width: 80 }, 			
        ],
		viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: true,
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
        }
    });

    $("#jqGridOrder").jqGrid({
        url: '../mall/order/list',
       datatype: "local",
//        datatype: "json",
        colModel: [			
			{ label: 'orderId', name: 'orderId', hidden: true,width: 50, key: true },
			{ label: '订单号', name: 'orderNo', width: 80 }, 			
//			{ label: '用户ID', name: 'userId', width: 80 }, 	
			{ label: '学员昵称', name: 'nickName', width: 80 }, 
			{ label: '手机号', name: 'mobile', width: 90 }, 
//			{ label: '来源', name: 'sourceType', width: 80 }, 
//			{ label: '订单名称', name: 'orderName', width: 80 }, 	
//			{ label: '订单描述', name: 'orderDescribe', width: 80 }, 
			{ label: '学员规划', name: 'userplanCount', width: 80 , formatter(value, options, row){return value === 0 ? '<span class="label label-danger">未生成</span>'
					: '<span class="label label-success">已生成</span>';} }, 		
			{ label: '专业', name: 'professionName', width: 60 }, 			
			{ label: '商品ID', name: 'commodityId', width: 60 }, 			
			{ label: '商品名称', name: 'commodityName', width: 80 }, 			
			/*{ label: '订单总额', name: 'totalMoney', width: 80 }, 			
			{ label: '支付金额', name: 'payMoney', width: 80 }, 
			{ label: '优惠金额', name: 'favorableMoney', width: 80 }, */
			//0.未支付 1.发起支付 ,2.支付成功
			{ label: '支付状态', name: 'payStatus', width: 80 , formatter(value, options, row){if(0==value)return "未支付";else if(1==value)return "发起支付";else if(2==value){return "支付成功";}} }, 		
//			{ label: '支付方式', name: 'payType', width: 80 },
//			{ label: '支付宝交易号', name: 'alipayTradeNo', width: 80 }, 
			{ label: '支付时间', name: 'payTime', width: 80 }, 
			{ label: '省份', name: 'areaName', width: 80 }, 
			{ label: '层次', name: 'levelName', width: 80 },
			{ label: 'NCID', name: 'ncId', width: 80 },
			{ label: 'NCCODE', name: 'ncCode', width: 80 },
			{ label: '创建时间', name: 'creationTime', hidden: true, width: 80 },
			{ label: '班型', name: 'classtypeName', width: 80 },
			{ label: '班级', name: 'className', width: 80 },
//			{ label: '状态', name: 'status', width: 80 },	
			{ label: '备注', name: 'remark', hidden: true, width: 80 }
        ],
		viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPagerOrder",
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
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGridOrder").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });

    
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			className: null,
			productId : "",
			productName : "",
			teacherName: null
		},
		showList: true,
		updateshowList: false,
		flag:false,
		showListOrder:false,
		title: null,
		mallClass: {
			areaId:"",//省份
			areaName:"",//省份
			professionId:"",//专业
			professionName:"",//专业
			levelId:"",//层次
			levelName:"",//层次
			userId:"",//班主任
			classTeacherName:"",//班主任
			classId:"",
            className: "", //班级名称
			productId : "",//产品线
			productName : "",//产品线
			deptId:"",//部门
			deptName:"",//部门
            orderList:[], //要转移班级的订单列表
			
		},
		selectData : {//下拉初始化列表
			professionList :[],//专业
			areaList :[],//省份
			classTeacherList :[],//班主任
            levelList :[],//层次
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
            vm.showList = false;
            vm.flag=false;
            vm.showListOrder = false;
            vm.updateshowList=true;
			vm.title = "新增";
			vm.mallClass = {
				areaId:"",//省份
				areaName:"",//省份
				professionId:"",//专业
				professionName:"",//专业
				levelId:"",//层次
				levelName:"",//层次
				userId:"",//班主任
				classTeacherName:"",//班主任
				classId:"",
				productId : "",//产品线
				productName : "",//产品线
				deptId:"",//部门
				deptName:""//部门
			};
		},
		update: function (event) {
			var classId = getSelectedRow();
			if(classId == null){
				return ;
			}
            vm.showList = false;
            vm.flag=false;
            vm.showListOrder = false;
            vm.updateshowList=true;
            vm.title = "修改";

            vm.getInfo(classId)
		},
		queryOrder: function (event) {
			/*var classId = getSelectedRow();
			if(classId == null){
				return ;
			}*/
			var selectDetail = getJqGridSelectedRow("jqGrid");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGrid").jqGrid('getRowData',selectDetail);
			vm.mallClass.className=rowData.className;
			vm.mallClass.classId=selectDetail;
            vm.showList = false;
            vm.flag=false;
            vm.showListOrder = true;
            vm.updateshowList=false;
			//加载订单数据
			vm.reloadJqGridOrder();
		},
		resetView: function (event) {
            vm.showList = true;
            vm.flag=false;
            vm.showListOrder = false;
            vm.updateshowList=false;
		},
		reloadJqGridOrder : function(){//刷新订单列表
			$("#jqGridOrder").jqGrid('setGridParam',{ 
                postData : {
                	"classId" : vm.mallClass.classId , //班级名称
                	},
                page : 1,
                datatype : 'json'
            }).trigger("reloadGrid");
		},
		saveOrUpdate: function (event) {
		    
			if($.isNull(vm.mallClass.areaId)){
				alert("请选择省份！！！");
				return;
			}
			if($.isNull(vm.mallClass.professionId)){
				alert("请选择专业！！！");
				return;
			}
			if($.isNull(vm.mallClass.levelId)){
				alert("请选择学历！！！");
				return;
			}
			if($.isNull(vm.mallClass.className)){
				alert("请输入班级名称！！！");
				return;
			}
			if($.isNull(vm.mallClass.userId)){
				alert("请选择班主任！！！");
				return;
			}
			if(!$.isNull(vm.mallClass.remake) && vm.mallClass.remake.length > 50){
				alert("备注信息不得超过50个字！！！");
				return;
			}
			if($.isNull(vm.mallClass.productId)){
				alert("请选择产品线！！！");
				return;
			}
			if($.isNull(vm.mallClass.deptId)){
				alert("请选择部门组织！！！");
				return;
			}
			
			if(vm.title == "新增"){
		       url = "../mall/class/save";
		    }
		    else if(vm.title == "修改"){
		       url = "../mall/class/update";
		    }else{
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallClass),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del: function (event) {
			var classId = getSelectedRow();
			if(classId == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/class/delete/"+classId,
				   /* data: JSON.stringify(classIds),*/
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getInfo: function(classId){
			$.get("../mall/class/info/"+classId, function(r){
                vm.mallClass = r.mallClass;
               // alert(r.mallClass.productId);
            });
		},
		reload: function (event , p) {
            vm.showList = true;
            vm.flag=false;
            vm.showListOrder = false;
            vm.updateshowList=false;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"className" : vm.q.className,"teacherName" : vm.q.teacherName},
                page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var classIds = getSelectedRows();
			if(classIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/class/resume",
				    data: JSON.stringify(classIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");

							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		pause:function(event){
		    var classIds = getSelectedRows();
			if(classIds == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/class/pause",
				    data: JSON.stringify(classIds),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		updateDefaultClass : function(){//指定为默认班级
			var classId = getSelectedRow();
			if(classId == null){
				return ;
			}
			var data = 	{classId : classId};
			confirm('确定要指定为默认班级吗？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/class/updateDefaultClass",
				    data: data,
//				    data: JSON.stringify(data),
				    contentType: "application/x-www-form-urlencoded",
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								$("#jqGrid").trigger("reloadGrid");
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
        selectClass: function () {
           var orderIds =  getJqGridSelectedRows("jqGridOrder");
           if (orderIds == null){
                return ;
           }
            vm.mallClass.orderList = orderIds;
           vm.title = "转移班级"
            vm.showList = false;
            vm.flag=true;
            vm.showListOrder = false;
            vm.updateshowList=false;
        },
        classLayerShow: function () {
            classLay.show(function(index,opt){
                vm.mallClass.classId = opt.classId;
                vm.mallClass.className = opt.className;
                vm.mallClass.productId = opt.productId;
            });
        },
        updateBatchClass:function () {
            confirm('确定要转移到新班级吗？', function(){
                //alert(JSON.stringify(vm.mallClass.classId))
               // alert(vm.mallClass.classId+"-----------"+vm.mallClass.orderList)
               // var data = 	{"orderIds":vm.mallClass.orderList,"classId":vm.mallClass.classId};
               // alert(JSON.stringify(data));
                $.ajax({
                    type: "POST",
                    url: "../mall/order/update",
                    data: JSON.stringify(vm.mallClass),
                    dataType:"json",
                    // contentType: "application/json",
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                $("#jqGrid").trigger("reloadGrid");
                                vm.resetView();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
		selectProduct : function(){//编辑-产品线
			productLay.show(function(index,opt){
				vm.mallClass.productId = opt.productId;
				vm.mallClass.productName = opt.productName;
			});
		},
		selectDept : function(){//编辑-部门
			deptLay.show(function(id , name ,opt){
				vm.mallClass.deptId = id;
				vm.mallClass.deptName = name;
			});
		},
		selectArea : function(){//编辑-省份
			areaLay.show(function(index,opt){
				vm.mallClass.areaId = opt.areaId;
				vm.mallClass.areaName = opt.areaName;
			});
		},
		selectProfession : function(){//编辑-专业
			professionLay.show(function(index,opt){
				vm.mallClass.professionId = opt.professionId;
				vm.mallClass.professionName = opt.professionName;
			});
		},
		selectLevel : function(){//编辑-学历
			levelLay.show(function(index,opt){
				vm.mallClass.levelId = opt.levelId;
				vm.mallClass.levelName = opt.levelName;
			});
		},
		selectTeacher : function(){//编辑-班主任
			teacherLay.show(function(index,opt){
				vm.mallClass.userId = opt.userId;
				vm.mallClass.classTeacherName = opt.nickName;
			} , 2);
		},
		classTeacherLayerShow : function(){//选择班主任
			teacherLay.show(function(index,opt){
				vm.q.classTeacherId = opt.userId;
				vm.q.classTeacherName = opt.nickName;
			} , 2);
		},
	},
	created : function(){
	}
});