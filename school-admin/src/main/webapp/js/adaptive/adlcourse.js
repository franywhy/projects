$(function () {
    $("#jqGrid").jqGrid({
        url: '../adaptive/adlcourse/list',
        datatype: "json",
        colModel: [			
        	{ label: 'ID', name: 'courseId', index: 'course_id', width: 50, key: true },
			{ label: '课程名称', name: 'courseName', index: 'course_name', width: 80 },
			{ label: '课程编码', name: 'courseNo', index: 'course_no', width: 80 },
			{ label: '产品线', name: 'productId', index: 'product_id', width: 80,hidden: true },
			{ label: '部门', name: 'deptId', index: 'deptId_id', width: 80,hidden: true },
            { label: '同步时间', name: 'syncTime', index: 'sync_time', width: 80}
        ],
		viewrecords: true,
		height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
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
        }
    });
    
    
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 章节  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	var zj_setting = {
		    data: {
		        simpleData: {
		            enable: true,
		            idKey: "id",
		            pIdKey: "parentId",
		            rootPId: 0
		        },
		        key: {
		            url:"nourl"
		        }
		    }
		};
	var zj_ztree;
	
	function getRecordId () {
	    var selected = $('#zjTable').bootstrapTreeTable('getSelections');
	    if (selected.length == 0) {
	        alert("请选择一条记录");
	        return false;
	    } else {
	    	return selected[0].id;
	    }
	}
	
	// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 章节  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    // ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 阶段  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
    $("#phaseGrid").jqGrid({
        datatype : "local",
        url: '../adaptive/adlphase/list',
        colModel : [
            {label : 'ID',name : 'phaseId', index: 'phase_id', key: true,hidden : true},
            {label : 'status',name : 'status', index: 'status', hidden : true},
            {label : '编号',name : 'phaseNo', index: 'phase_no', width : 150},
            {label : '名称',name : 'phaseName', index: 'phase_name', width : 150},
            {label : '知识点',name : 'knowledgeCount', width : 60},
            {label : '考点',name : 'keyPointName', index: 'key_point', width : 100},
            {label : '状态',name : 'statusName', index: 'status', width : 100 ,formatter: function(v, options, row){return row.status == 1 ? "正常" : "禁用";}},
            {label : '版本',name : 'versionNo', index: 'version_no', width : 100}
        ],
        viewrecords: true,
        height : 500,
        rowNum: 10,
        rowList : [10,30,50],
        rownumbers: true,
        rownumWidth: 35,
        autowidth:true,
        multiselect: false,
        pager: "#phaseGridPager",
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
            $("#phaseGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" });
        }
    });
    // ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 阶段  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{//课程搜索条件
			courseId: null,
			courseName: null,
			courseNo: null,
			sourceId:null
		},
		selectCourse:{
			courseId : null,
			courseName : null,
			courseNo : null,
			courseNameNo : null
		},
		queryPhase : {
            courseId : null ,
			keyPoint : null,
            keyPointName : null,
            phaseName : null,
            phaseNo : null,
		},
		phaseObj : {
            phaseId : null,
            courseId : null,
            phaseName : null,
            phaseNo : null,
            keyPointName : "正常",
            keyPoint : 1,
            versionNo : null,
            versionHash : null,
            status : 1,
			sectionList : [],
            knowledgeIdList:[],
            sectionListName : null,
            //阶段预习-资料
            phaseBeforeFile : {
                id : null,//自增id
                phaseId : null,//阶段
                fileName : null,//名称
                fileUrl : null,//资料文件下载地址
                phaseType:null
            },
            //阶段课后-资料
            phaseAfterFile : {
                id : null,//自增id
                phaseId : null,//阶段
                fileName : null,//名称
                fileUrl : null,//资料文件下载地址
                phaseType:null
            }
		},
		show1: true,//1.课程列表 2.章节列表
		show2: false,
		show3: false,
		show4: false,
		title: null,

		//章节课树列表参数
		zjTable : {
		    id: "zjTable",
		    table: null,
		    layerIndex: -1,

		    initColumn: function () {
			    var columns = [
	   		        {field: 'selectItem', radio: true},
	   		        {title: 'ID', field: 'id', visible: false, align: 'center', valign: 'middle', width: '20px'},
	   		        {title: '编号', field: 'code', align: 'center', valign: 'middle', sortable: true, width: '100px'},
	   		        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '100px'},
	   		        {title: '知识点数量', field: 'knowledgeNum', align: 'center', valign: 'middle', sortable: true, width: '20px'},
	   		        {title: '章节', field: 'zj', align: 'center', valign: 'middle', sortable: true, width: '20px' , formatter: function(value,row,index){return value.zj == 1 ? "章" : "节";}},
	   		        {title: '排序', field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '20px'}]
			   	 return columns;
			}
		},
	},
	methods: {
		clearQuery : function(){//重置课程所搜条件
			//课程搜索条件
			vm.q = {
					courseId: null,//课程ID
					courseName: null,//课程名称
					courseNo: null,//课程编号
					sourceId:null//课程来源
				};
		},
		query: function () {//刷新课程列表
			vm.reload(null ,1);
		},
		reload: function (event , p) {//刷新课程列表
			var that = vm;
			vm.showType(1);
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{
				postData : that.q,
                page:page
            }).trigger("reloadGrid");
		},
		initSelectCourse : function(){//选中某一个课程 初始化选中课程后的信息
			var selectId = getSelectedRow();
			if(selectId == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGrid").jqGrid('getRowData',selectId);
			//课程ID
			vm.selectCourse.courseId = rowData.courseId;
			vm.selectCourse.courseName = rowData.courseName;
			vm.selectCourse.courseNo = rowData.courseNo;
			vm.selectCourse.courseNameNo = vm.selectCourse.courseNo + vm.selectCourse.courseName;
		},
		clearSelectCourse:function(){//清空选中某一个课程信息
			vm.selectCourse.courseId = null;
			vm.selectCourse.courseName = "";
			vm.selectCourse.courseNo = "";
			vm.selectCourse.courseNameNo = "";
		},
		showType:function(type){//显示
			switch (type) {
			case 1:	/*-*/
				vm.clearSelectCourse();
				vm.title = "课程列表";
				vm.show1 = true;
				vm.show2 = false;
                vm.show3 = false;
                vm.show4 = false;
				break;
			case 2:	/*-*/
				vm.title = "章节列表";
				vm.show1 = false;
				vm.show2 = true;
				vm.show3 = false;
				vm.show4 = false;
				break;
			case 3:	/*-*/
				vm.title = "阶段列表";
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = true;
                vm.show4 = false;
                break;
			case 4:	/*-*/
				vm.title = "阶段编辑";
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = false;
                vm.show4 = true;
                break;

			default:
				vm.show1 = true;
				vm.show2 = false;
				break;
			}
		},
		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 章节  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

		//章节
		showZJ : function(){
			var selectId = getSelectedRow();
			if(selectId == null){
				return ;
			}
//			选中某一个课程 初始化选中课程后的信息
			vm.initSelectCourse();
			vm.showType(2);
			var colunms = vm.zjTable.initColumn();
	        var table = new TreeTable(vm.zjTable.id, "../adaptive/zj/zjList/"+selectId, colunms);
	        table.setRootCodeValue(0);//设置根节点code的值
	        table.setExpandColumn(2);//设置在哪一列上面显示展开按钮，从0开始
	        table.setIdField("id");//设置记录返回的id值
	        table.setCodeField("id");//设置记录分级的字段
	        table.setParentCodeField("parentId");//设置记录分级的父级字段
	        table.setExpandAll(true);//设置是否默认全部展开
	        table.init();
	        vm.zjTable.table = table
		},
		zjReload:function(){//刷新章节列表
			vm.showType(2);
			vm.zjTable.table.refresh();
		},
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 章节

		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 阶段  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
        showPhase : function(){//显示章节
			var selectId = getSelectedRow();
			if(selectId == null){
				return ;
			}

//			选中某一个课程 初始化选中课程后的信息
			vm.initSelectCourse();
			//清空查询条件
            vm.cleanQueryPhase();
            //课程ID
            vm.queryPhase.courseId = selectId;
            //刷新
            vm.phaseReload();
		},
		phaseReload : function(){//刷新阶段
            vm.showType(3);
            //清空表格数据
            jQuery("#phaseGrid").jqGrid("clearGridData");
            var page = $("#phaseGrid").jqGrid('getGridParam','page');
            $("#phaseGrid").jqGrid('setGridParam',{
                datatype: "json",
                postData : vm.queryPhase,
                page:page
            }).trigger("reloadGrid");
            vm.getcourseTreeAllTree();
		},
        getcourseTreeAllTree:function(){//获取课程树包含知识点
            $.get("../adaptive/layer/courseTreeAll/"+ vm.selectCourse.courseId, function(r) {
                if(r.code == 0){
                    vm.selectCourse.courseTreeAll = r.data;
                }
            });
        },
		cleanQueryPhase : function(){//清空阶段查询条件
            vm.queryPhase = {
                courseId : null ,
                keyPoint : null,
                keyPointName : null,
                phaseName : null,
                phaseNo : null,
			}
		},
		phaseGetInfo : function (phaseId) {//获取阶段内容
            $.ajax({
                type: "GET",
                url: "../adaptive/adlphase/info/"+phaseId,
                success: function(r){
                    if(r.code === 0){
                        vm.phaseObj = r.data;
                        vm.phaseObj.keyPointName = vm.phaseObj.keyPoint == 1 ? "正常" : "考点";
                        $("#knowledgeListName").val('共'+r.data.knowledgeIdList.length+'个知识点');
                        vm.phaseObj.knowledgeIdList = vm.phaseObj.knowledgeIdList;
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        phaseEdit : function(){//修改按钮点击事件
            var selectId = getJqGridSelectedRow("phaseGrid");
            if(selectId == null){
                return ;
            }
            vm.title = "阶段编辑";
            //初始化阶段参数
            vm.cleanPhaseObj();
            //获取阶段内容
            vm.phaseGetInfo(selectId);
            //展示
            vm.showType(4);
		},
        phaseSave : function(){//保存按钮点击事件
			vm.cleanPhaseObj();
			//所选课程ID
            vm.phaseObj.courseId = vm.selectCourse.courseId;
            vm.showType(4);
            vm.title = "新增阶段";
            $("#knowledgeListName").val("");
        },
        phaseSaveOrModify : function () {//保存阶段
			if($.isNull(vm.phaseObj.phaseName)){
				alert("请输入名称!");
				return;
			}
			if($.isNull(vm.phaseObj.phaseNo)){
				alert("请输入编号!");
				return;
			}
			if($.isNull(vm.phaseObj.courseId)){
				alert("请选择编号!");
				return;
			}
			if($.isNull(vm.phaseObj.phaseNo)){
				alert("请选择考点类型!");
				return;
			}
            // if($.isNull(vm.phaseObj.phaseBeforeFile.fileName) || $.isNull(vm.phaseObj.phaseBeforeFile.fileUrl)){
            //     vm.phaseObj.phaseBeforeFile = null;
            // }
            // if($.isNull(vm.phaseObj.phaseAfterFile.fileName) || $.isNull(vm.phaseObj.phaseAfterFile.fileUrl)){
            //     vm.phaseObj.phaseAfterFile = null;
            // }
			//保存地址
            var url = "";
            if(vm.title == "新增阶段") url = "../adaptive/adlphase/save/";
            else if(vm.title == "阶段编辑") url = "../adaptive/adlphase/edit/";

            $.ajax({
                type: "POST",
                url: url,
                data: JSON.stringify(vm.phaseObj),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(index){
                            vm.phaseReload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        phaseDel : function(){//删除阶段
            var selectId = getJqGridSelectedRow("phaseGrid");
            if(selectId == null){
                return ;
            }
            confirm('确定删除阶段吗？', function(){
                $.ajax({
                    type: "POST",
                    url: "../adaptive/adlphase/delete/"+selectId,
                    // data: JSON.stringify(zjId),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                vm.phaseReload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        phaseEnable : function(){//启用阶段
            var selectId = getJqGridSelectedRow("phaseGrid");
            if(selectId == null){
                return ;
            }
            //行数据
            var rowData = $("#phaseGrid").jqGrid('getRowData',selectId);
            if(rowData.status == 1){
                alert("已经处于启用状态");
                return;
            }
            confirm('确定启用阶段吗？', function(){
                $.ajax({
                    type: "POST",
                    url: "../adaptive/adlphase/enablePhase/"+selectId,
                    // data: JSON.stringify(zjId),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                vm.phaseReload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        phaseDisable : function(){//禁用阶段
            var selectId = getJqGridSelectedRow("phaseGrid");
            if(selectId == null){
                return ;
            }
            //行数据
            var rowData = $("#phaseGrid").jqGrid('getRowData',selectId);
            if(rowData.status != 1){
                alert("已经处于禁用状态");
                return;
            }
            confirm('确定禁用阶段吗？', function(){
                $.ajax({
                    type: "POST",
                    url: "../adaptive/adlphase/disablePhase/"+selectId,
                    // data: JSON.stringify(zjId),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(index){
                                vm.phaseReload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
		cleanPhaseObj : function(){//初始化对象
			vm.phaseObj = {
                phaseId : null,
                    courseId : null,
                    phaseName : null,
                    phaseNo : null,
					keyPointName : "正常",
					keyPoint : 1,
                    versionNo : null,
                    versionHash : null,
                    status : 1,
                	sectionList : [],
                	sectionListName : null,
                    //阶段预习-资料
                    phaseBeforeFile : {
        				id : null,//自增id
        				phaseId : null,//阶段
        				fileName : null,//名称
        				fileUrl : null,//资料文件下载地址
                        phaseType:null
        			},
                    //阶段课后-资料
                    phaseAfterFile : {
        				id : null,//自增id
        				phaseId : null,//阶段
        				fileName : null,//名称
        				fileUrl : null,//资料文件下载地址
                        phaseType:null
        			}
            };
		},
        checkPhaseName : function () {//校验名称重复性
            hq.ajax({
                type: "POST",
                url: "../adaptive/adlphase/checkPhaseName/",
                data: {courseId : vm.selectCourse.courseId , phaseId : vm.phaseObj.phaseId , phaseName : vm.phaseObj.phaseName},
                success: function(r) {
                    if (r.code == 0) {
                        if (r.data == true) {
                            alert("同一课程下的阶段名称出现重复，请修改！");
                        }
                        else {
                            alert("阶段名称可以使用！");
                        }
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        checkPhaseNo : function () {//校验编码重复性
            hq.ajax({
                type: "POST",
                url: "../adaptive/adlphase/checkPhaseNo/",
                data: {phaseId : vm.phaseObj.phaseId , phaseNo : vm.phaseObj.phaseNo},
                success: function(r) {
                    if (r.code == 0) {
                        if (r.data == true) {
                            alert("同一课程下的阶段编码出现重复，请修改！");
                        }
                        else {
                            alert("阶段编码可以使用！");
                        }
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        KeyPointShow : function () {//阶段编辑时 考点展示
            adlConfigLay.knowledgeKeyPointShow(function(rowId , rowData){
                vm.phaseObj.keyPointName = rowData.cname;
                vm.phaseObj.keyPoint = rowData.cvalue;
            });
        },
        queryKeyPointShow : function () {//查询时 考点展示
            adlConfigLay.knowledgeKeyPointShow(function(rowId , rowData){
                vm.queryPhase.keyPointName = rowData.cname;
                vm.queryPhase.keyPoint = rowData.cvalue;
            });
        },
        clearKeyPointName : function(){//清除考点
            vm.phaseObj.keyPointName = null;
            vm.phaseObj.keyPoint = null;
        },
        queryClearKeyPointName : function(){//清除考点
            vm.queryPhase.keyPointName = null;
            vm.queryPhase.keyPoint = null;
        },
        clearBeforeFile : function(){//清除预习资料
			vm.phaseObj.phaseBeforeFile.fileUrl = "";//资料地址
		},
		clearAfterFile : function(){//清除课后资料
			vm.phaseObj.phaseAfterFile.fileUrl = "";//资料地址
		},
        sectionMutiSelectShow : function(){//节列表
            //弹窗选中
            var selectIds = new Array();
            if(null != vm.phaseObj.sectionList && vm.phaseObj.sectionList.length > 0){
                $.each(vm.phaseObj.sectionList , function(index , item){
                    selectIds.push(item.sectionId);
                });
            }
            //弹出弹窗
            adlSectionMutiSelectLay.show(vm.selectCourse.courseId , selectIds,function(rowIdList , rowDataList){
                //rowIdList->多选ID
                //rowDataList->多选Obj
                if(null != rowDataList && rowDataList.length){
                    //支持多选
                    $.each(rowDataList , function(index , item){
                        vm.addSectionList(item.sectionId , item.sectionName);
                    });
                    //选中名称
                    vm.phaseObj.sectionListName = vm.initSectiontListName();
                }
            });
        },
        addSectionList : function(value , name){//节列表
            if(vm.phaseObj.sectionList == null){
                //选中节数组
                vm.phaseObj.sectionList = new Array();
            }
            var isHave = false;
            for(var i = 0 ; i < vm.phaseObj.sectionList.length ; i++){
            	var item = vm.phaseObj.sectionList[i];
            	if(item.sectionId == value){
                    isHave = true;
                    break;
				}
			}
			if(!isHave){
                vm.phaseObj.sectionList.push({'sectionId' : value , "sectionName" : name , courseId : vm.selectCourse.courseId});
            }
        },
        initSectiontListName : function(){//节名称
            var text = "";
            if($.isNotNull(vm.phaseObj.sectionList) && vm.phaseObj.sectionList.length > 0){
                $.each(vm.phaseObj.sectionList , function(index , item){
                    text += item.sectionName;
                    if(index < vm.phaseObj.sectionList.length - 1){
                        text += ",";
                    }
                });
            }
            return text;
        },
        clearSectionMutiSelect : function () {//清空节
            vm.phaseObj.sectionList = new Array();
            vm.phaseObj.sectionListName = vm.initSectiontListName();
        },
        updatePhaceKnowledgeVersion : function() {//阶段更新知识点空间图谱
            vm.updatePhaceKnowledgeVersionBase(false);
        },
        updatePhaceKnowledgeVersionUpdate : function(){//阶段更新知识点空间图谱(强制更新)
            vm.updatePhaceKnowledgeVersionBase(true);
        },
        updatePhaceKnowledgeVersionBase : function(update){//阶段更新知识点空间图谱
            var selectId = getJqGridSelectedRow("phaseGrid");
            if(selectId == null){
                return ;
            }
            hq.ajax({
                type: "POST",
                url: "../adaptive/adlphase/updatePhaceKnowledgeVersion/"+selectId,
                data : {update : update},
                success: function(r){
                    if(r.code == 0){
                        alert('操作成功', function(index){
                            vm.phaseReload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        showPhaseKnowledgeLay : function(){//选择阶段知识点
            phaseKnowledgeLay.show(function(ids){
                vm.phaseObj.knowledgeIdList = ids;
                $("#knowledgeListName").val('共'+ids.length+'个知识点');
            },vm.phaseObj.knowledgeIdList);
        },
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 阶段  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	}
});