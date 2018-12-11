$(function () {
    $("#jqGrid").jqGrid({
        url: '../courses/cscourse/list',
        datatype: "json",
        colModel: [			
        	{ label: 'ID', name: 'courseId', index: 'course_id', width: 20, key: true },
        	{ label: 'status', name: 'status', index: 'status', width: 20 , hidden: true},
        	{ label: 'productId', name: 'productId', index: 'product_id', width: 20 , hidden: true },
        	{ label: 'deptId', name: 'deptId', index: 'dept_id', width: 20 , hidden: true },
			{ label: '课程名称', name: 'courseName', index: 'course_name', width: 80 },
			{ label: '课程编码', name: 'courseNo', index: 'course_no', width: 80 }, 
			{ label: '章节', name: 'createType', index: 'course_no', width: 30 , formatter: function(v, options, row){
				return "章" + row.chapterNum + "|节" + row.sectionNum;
			}},
			// { label: '状态', name: 'statusName', index: 'status', width: 30 , formatter: function(v, options, row){
			// 	return row.status == 1 ? "启用" : "禁用";
			// }},
			{ label: '产品线', name: 'productName', index: 'product_id', width: 80 },
			{ label: '所属公司', name: 'deptName', index: 'dept_id', width: 80 },
			{ label: '关联ID', name: 'ncId', index: 'course_id', width: 80 , formatter: function(v, options, row){
				var text = "";
				if($.isNotNull(row.ncId))text += "NCID(" + row.ncId + ")";
				if($.isNotNull(row.ljId))text += "蓝鲸ID(" + row.ljId + ")";
				if($.isNotNull(row.tkId))text += "题库ID(" + row.tkId + ")";
				return text;
			}},
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
    
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{//课程搜索条件
			courseId : null,
			courseName : null,
			courseNo : null,
			productId : null,
			productName : null,
			status:"",
			deptId : null,
			deptName : null
		},
		courseObj:{
			courseId : null,//自增id
			courseName : null,//课程名称
			courseNo : null,//课程编码
			productId : null,//产品线pk1.自考 : null,2.会计学历 : null,3学来学网 : null,5.会计猎才 : null,6.会计培训
			productName : null,
			deptId : null,//部门ID
			deptName : null,
			remark : null,//备注
			ncId : null,//NCID
			ljId : null,//蓝鲸ID
			tkId : null//题库ID
		},
		selectCourse:{
			courseId : null,
			courseName : null,
			courseNo : null,
			courseNameNo : null
		},
		zIndexOf:"Z_",
		jIndexOf:"J_",
		show1: true,//1.课程列表 2.章节列表
		show1_1: false,//1.课程列表 2.章节列表
		show2: false,
		show3: false,
		show5: false,
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
		//章-编辑
		zObj:{
			chapterId : null,//章ID
			chapterName : "",//章名称
			chapterNo : "",//章编码
			courseId : null,//课程ID
			orderNum : 1//排序
		},
		jObj:{//节-编辑
			chapterId : null,//章ID
			sectionId : null,//节ID
			chapterNameNo : "",//章编码名称
			sectionName : "",//节名称
			sectionNo : "",//节编码
			orderNum : 1//排序
		},
	},
	methods: {
		clearQuery : function(){//重置课程所搜条件
			//课程搜索条件
			vm.q = {
					courseId: null,//课程ID
					courseName: null,//课程名称
					courseNo: null,//课程编号
					productId : null,
					productName : null,
					deptId : null,
					deptName : null
				};
		},
		query: function () {//刷新课程列表
			vm.reload(null ,1);
		},
		clearCourseObj:function(){
            vm.courseObj = {
                	courseId : null,//自增id
                    courseName : null,//课程名称
                    courseNo : null,//课程编码
                    productId : null,//产品线pk1.自考 : null,2.会计学历 : null,3学来学网 : null,5.会计猎才 : null,6.会计培训
                    productName : null,
                    deptId : null,//部门ID
                    deptName : null,
                    remark : null,//备注
                    ncId : null,//NCID
                    ljId : null,//蓝鲸ID
                    tkId : null//题库ID
            };
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
				vm.show1_1 = false;
				vm.show2 = false;
				vm.show3 = false;
				vm.show5 = false;
				break;
			case 2:	/*-*/
				vm.title = "章节列表";
				vm.show1 = false;
				vm.show1_1 = false;
				vm.show2 = true;
				vm.show3 = false;
				vm.show5 = false;
				break;
			case 3:	/*-*/
				vm.title = "新增章";
				vm.show1 = false;
				vm.show1_1 = false;
				vm.show2 = false;
				vm.show3 = true;
				vm.show5 = false;
				break;
			case 4:	/*-*/
				vm.title = "修改章";
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = true;
				vm.show5 = false;
				break;
			case 5:	/*-*/
				vm.title = "新增节";
				vm.show1 = false;
				vm.show1_1 = false;
				vm.show2 = false;
				vm.show3 = false;
				vm.show5 = true;
				break;
			case 6:	/*-*/
				vm.title = "修改节";
				vm.show1 = false;
				vm.show2 = false;
				vm.show3 = false;
				vm.show5 = true;
				break;
			case 11:	/*-*/
				vm.clearSelectCourse();
				vm.show1 = false;
				vm.show1_1 = true;
				vm.show2 = false;
				vm.show3 = false;
				vm.show5 = false;
				break;
			default:
				vm.show1 = true;
				vm.show1_1 = false;
				vm.show2 = false;
				vm.show3 = false;
				vm.show5 = false;
				break;
			}
		},
		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 课程  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		save : function(){//新增按钮
			vm.title="新增课程";
			vm.clearCourseObj();
			vm.showType(11);
		},
		edit : function(){//修改按钮
			var selectId = getSelectedRow();
			if(selectId == null){
				return ;
			}
			vm.title="编辑课程";
            vm.clearCourseObj();
			vm.info();
			vm.showType(11);
		},
		info : function(){//信息
			var selectId = getSelectedRow();
			$.ajax({
				type: "GET",
			    url: "../courses/cscourse/info/"+selectId,
			    success: function(r){
			    	if(r.code === 0){
			    		vm.courseObj = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		saveOrModify : function(){//保存
			if($.isNull(vm.courseObj.courseName)){
				alert("请输入课程名称!");return;
			}
			if($.isNull(vm.courseObj.courseNo)){
				alert("请输入课程编号!");return;
			}else{
				vm.courseObj.courseNo = vm.courseObj.courseNo.trim();
			}
			if($.isNull(vm.courseObj.productId)){
				alert("请选择产品线!");return;
			}
			if($.isNull(vm.courseObj.deptId)){
				alert("请选择公司!");return;
			}
			var url = "";
			if(vm.title=="新增课程"){
				url = "../courses/cscourse/save";
			}else if(vm.title=="编辑课程"){
				url = "../courses/cscourse/edit";
			}
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseObj),
			    success: function(r){
			    	if(r.code === 0){
			    		alert("操作成功!" , function(){
			    			vm.reload();
			    		});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		del : function(){//删除
            confirm('确定删除吗？', function() {
                var selectId = getSelectedRow();
                if (selectId == null) {
                    return;
                }
                $.ajax({
                    type: "POST",
                    url: "../courses/cscourse/del",
                    data: JSON.stringify(selectId),
                    success: function(r){
                        if(r.code === 0){
                            alert("操作成功!" , function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
		},
		enable : function(){//启用
			confirm('确定启用吗？', function(){
				var selectId = getSelectedRow();
				if(selectId == null){
					return ;
				}
				//行数据
				var rowData = $("#jqGrid").jqGrid('getRowData',selectId);
				console.log(rowData.status);	
				if(rowData.status == 1){
					alert("课程已启用!");
					return;
				}
				$.ajax({
					type: "POST",
				    url: "../courses/cscourse/enable",
				    data: JSON.stringify(selectId),
				    success: function(r){
				    	if(r.code === 0){
				    		alert("操作成功!" , function(){
				    			vm.reload();
				    		});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		unenable : function(){//禁用
			confirm('确定禁用吗？', function(){
				var selectId = getSelectedRow();
				if(selectId == null){
					return ;
				}
				//行数据
				var rowData = $("#jqGrid").jqGrid('getRowData',selectId);
				console.log(rowData.status);	
				if(rowData.status == 0){
					alert("课程已禁用!");
					return;
				}
				$.ajax({
					type: "POST",
				    url: "../courses/cscourse/unenable",
				    data: JSON.stringify(selectId),
				    success: function(r){
				    	if(r.code === 0){
				    		alert("操作成功!" , function(){
				    			vm.reload();
				    		});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		checkCourseNo : function(){//编码重复性校验
			if($.isNull(vm.courseObj.courseNo)){
				alert("请输入课程编号!");return;
			}else{
				vm.courseObj.courseNo = vm.courseObj.courseNo.trim();
			}
			hq.ajax({
				type: "POST",
			    url: "../courses/cscourse/checkCourseNo",
			    data: {courseId : vm.courseObj.courseId , courseNo : vm.courseObj.courseNo},
			    success: function(r){
			    	if(r.code === 0){
			    		alert("课程编号可用!");
					}else{
						alert(r.msg);
					}
				}
			});
		},
		checkChapterNo : function(){//编码重复性校验
			if($.isNull(vm.zObj.chapterNo)){
				alert("请输入章编号!");return;
			}else{
				vm.zObj.chapterNo = $('#chapter_parent').text() + vm.zObj.chapterNo.trim();
			}
			hq.ajax({
				type: "POST",
			    url: "../courses/zj/checkChapterNo",
			    data: { chapterNo : vm.zObj.chapterNo,chapterId:vm.zObj.chapterId},
			    success: function(r){
                    var chapterNo = vm.zObj.chapterNo;
                    vm.zObj.chapterNo = chapterNo.substring(chapterNo.lastIndexOf("Z")+1,chapterNo.length);
			    	if(r.code === 0){
			    		alert("章编号可用!");
					}else{
						alert(r.msg);
					}
				}
			});
		},
		checkSectionNo : function(){//编码重复性校验
			if($.isNull(vm.jObj.sectionNo)){
				alert("请输入节编号!");return;
			}else{
				vm.jObj.sectionNo = $('#section_parent').text() + vm.jObj.sectionNo.trim();
			}
			hq.ajax({
				type: "POST",
			    url: "../courses/zj/checkSectionNo",
			    data: { sectionNo : vm.jObj.sectionNo,sectionId:vm.jObj.sectionId},
			    success: function(r){
                    var sectionNo = vm.jObj.sectionNo;
                    vm.jObj.sectionNo = sectionNo.substring(sectionNo.lastIndexOf("J")+1,sectionNo.length);
			    	if(r.code === 0){
			    		alert("节编号可用!");
					}else{
						alert(r.msg);
					}
				}
			});
		},
		showProductLay : function(){//选择产品线
			productLay.show(function(index,opt){
				vm.courseObj.productId = opt.productId;
				vm.courseObj.productName = opt.productName;
			});
		},
		showDeptLay : function(){//选择公司
			deptLay.show(function(id , name ,opt){
				vm.courseObj.deptId = id;
				vm.courseObj.deptName = name;
			});
		},
		showQueryProductLay : function(){//选择产品线
			productLay.show(function(index,opt){
				vm.q.productId = opt.productId;
				vm.q.productName = opt.productName;
			});
		},
		showQueryDeptLay : function(){//选择公司
			deptLay.show(function(id , name ,opt){
				vm.q.deptId = id;
				vm.q.deptName = name;
			});
		},
		clearQueryProduct : function(){//清空查询条件-产品线
			vm.q.productId = null;
			vm.q.productName = null;
		},
		clearQueryDept : function(){//清空查询条件-公司
			vm.q.deptId = null;
			vm.q.deptName = null;
		},
		clearProduct : function(){//清空编辑-产品线
			vm.courseObj.productId = null;
			vm.courseObj.productName = null;
		},
		clearDept : function(){//清空编辑-产品线
			vm.courseObj.deptId = null;
			vm.courseObj.deptName = null;
		},
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 课程  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
		
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
	        var table = new TreeTable(vm.zjTable.id, "../courses/zj/zjList/"+selectId, colunms);
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
		zAdd:function(){//新增章页面
			//初始化参数
			vm.zObj.chapterId = null;
			vm.zObj.chapterName = null;
			vm.zObj.chapterNo = null;
			vm.zObj.courseId = vm.selectCourse.courseId;
			vm.zObj.orderNum = 1;
			vm.showType(3);//显示加载界面
            $('#chapter_parent').text(vm.selectCourse.courseNo+'Z');
//			vm.zgetCode();//加载章编码
		},
		zgetCode : function(){//获取章编码
			$.get("../courses/zj/getChapterNo/" + vm.selectCourse.courseId, function(r) {
				if(r.code == 0){
					vm.zObj.chapterNo = r.data;
				}else{
					alert(r.msg);
				}
			});
		},
		zSaveOrModify : function(){//章新增\编辑 保存
			if($.isNull(vm.zObj.chapterName)){
				alert("请输入章名称!");
				return;
			}
			if($.isNull(vm.zObj.chapterNo)){
				alert("请输入章编码!");
				return;
			}else{
				vm.zObj.chapterNo = $('#chapter_parent').text()+vm.zObj.chapterNo.trim();
			}
			//保存地址
			var url = "";
			if(vm.title == "新增章") url = "../courses/zj/cschapter/save/";
			else if(vm.title == "修改章") url = "../courses/zj/cschapter/update/";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.zObj),
			    success: function(r){
                    var chapterNo = vm.zObj.chapterNo;
                    vm.zObj.chapterNo = chapterNo.substring(chapterNo.lastIndexOf("Z")+1,chapterNo.length);
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.zjReload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
			
		},
		jAdd:function(){//新增节
			var zjId = vm.getZJSelectId();
			if (zjId == null || !zjId) {
				return;
			}
			//初始化节变量
			vm.jObj.sectionId = null;//节ID
			vm.jObj.chapterNameNo = null;//章编码名称
			vm.jObj.chapterId = null;//章ID
			vm.jObj.sectionName = "";//节名称
			vm.jObj.sectionNo = "";//节编码
			vm.jObj.orderNum = 1;//排序

			//新增节时获取章id、章code、章name、节编码
			vm.jAddBeforeGetChapterInfo(zjId);
			//展示节编辑界面
			vm.showType(5);

		},
		jAddBeforeGetChapterInfo : function(zjId){//新增节时获取章id、章code、章name、节编码
			$.ajax({
				type: "POST",
			    url: "../courses/zj/jAddBeforeGetChapterInfo",
			    data: JSON.stringify(zjId),
			    success: function(r){
			    	if(r.code === 0){
			    		vm.jObj.chapterNameNo = r.data.chapterNo + r.data.chapterName;//章编码名称
						vm.jObj.chapterId = r.data.chapterId;//章ID
                        vm.zObj.chapterNo = r.data.chapterNo;
//						vm.jObj.sectionNo = r.data.sectionNo;//节编码
                        $('#section_parent').text(r.data.chapterNo+'J');
					}else{
						alert(r.msg);
					}
				}
			});
		},
		jSaveOrModify:function(){
			if($.isNull(vm.jObj.sectionName)){
				alert("请输入节名称!");
				return;
			}
			if($.isNull(vm.jObj.sectionNo)){
				alert("请输入节编码!");
				return;
			}else{
				vm.jObj.sectionNo = $('#section_parent').text()+vm.jObj.sectionNo.trim();
			}
			//保存地址
			var url = "";
			if(vm.title == "新增节") url = "../courses/zj/cssection/save/";
			else url = "../courses/zj/cssection/update/";
			
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.jObj),
			    success: function(r){
                    var sectionNo = vm.jObj.sectionNo;
                    vm.jObj.sectionNo = sectionNo.substring(sectionNo.lastIndexOf("J")+1,sectionNo.length);
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.zjReload();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		zjUpdate:function(){//章节修改页面
			var zjId = vm.getZJSelectId();
			if (zjId == null || !zjId) {
				return;
			}
			$.get("../courses/zj/zjInfo/" + zjId, function(r) {
				if(r.code == 0){
					if(r.zjType == 1){//章
						vm.zObj.chapterId = r.data.chapterId;
						vm.zObj.chapterName = r.data.chapterName;
						vm.zObj.chapterNo = r.data.chapterNo;
						vm.zObj.courseId = r.data.courseId;
						vm.zObj.orderNum = r.data.orderNum;
						vm.showType(4);
                        var chapterNo = vm.zObj.chapterNo;
                        vm.zObj.chapterNo = chapterNo.substring(chapterNo.lastIndexOf("Z")+1,chapterNo.length);
                        $('#chapter_parent').text(vm.selectCourse.courseNo+'Z');
					}else if(r.zjType == 2){//节
						vm.jObj.sectionId = r.data.sectionId;//节ID
						vm.jObj.chapterNameNo = r.data.chapterNo + r.data.chapterName;//章编码名称
						vm.jObj.chapterId = r.data.chapterId;//章ID
						vm.jObj.sectionName = r.data.sectionName;//节名称
						vm.jObj.sectionNo = r.data.sectionNo;//节编码
						vm.jObj.orderNum = r.data.orderNum;//排序
						vm.showType(6);
                        var sectionNo = vm.jObj.sectionNo;
                        vm.jObj.sectionNo = sectionNo.substring(sectionNo.lastIndexOf("J")+1,sectionNo.length);
                        $('#section_parent').text(r.data.chapterNo+'J');
					}
				}else{
					alert(r.msg);
				}
			});
		},
		zjDelete:function(){//章节删除
			var zjId = vm.getZJSelectId();
			if (zjId == null || !zjId) {
				return;
			}
			confirm('确定删除章节吗？', function(){
				$.ajax({
					type: "POST",
				    url: "../courses/zj/zjRemove",
				    data: JSON.stringify(zjId),
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.zjReload();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		getZJSelectId :function() {//获取选中的章节ID
			var selected = $('#zjTable').bootstrapTreeTable('getSelections');
			if (selected.length == 0) {
				alert("请选择一条记录");
				return false;
			} else {
				return selected[0].id;
			}
		},
		jUpdateParent : function(){//节修改上一级
			var that = this;
			var zjId = vm.getZJSelectId();
			if (zjId == null || !zjId) {
				return;
			}
			if(0 == zjId.indexOf(vm.zIndexOf)){
				alert("请选择节！");
				return;
			}
			chapterListLay.show(vm.selectCourse.courseId , function(chapterId , rowData){
				
				var chapterName = rowData.chapterName;
				var chapterNo = rowData.chapterNo;
				confirm('确认修改上级为（' + chapterNo + '）'+chapterName+'吗?', function(){
					var jobj = {
						chapterId : chapterId,
						sectionId : zjId.substring(that.jIndexOf.length , zjId.length)
					};
					$.ajax({
						type: "POST",
					    url: "../courses/zj/cssection/updateSectionParent",
					    data: JSON.stringify(jobj),
					    success: function(r){
							if(r.code == 0){
								alert('操作成功', function(index){
									vm.zjReload();
								});
							}else{
								alert(r.msg);
							}
						}
					});
				});
			});
		},
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 章节
		
		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 阶段  ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		showJD : function(){
			var selectId = getSelectedRow();
			if(selectId == null){
				return ;
			}
//			选中某一个课程 初始化选中课程后的信息
			vm.initSelectCourse();
		},
		
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 阶段  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
		checkKnowledge : function(){
            var selectId = getSelectedRow();
            if(selectId == null){
                return ;
            }
            $.ajax({
                type: "GET",
                url: "../courses/knowledge/checkKnowledge/"+selectId,
                success: function(r){
                    if(r.code === 0){
                        alert("操作成功");
                    }else{
                        alert(r.msg);
                    }
                }
            });
		},
		// ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓ 数据初始化导入 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
		importExcelCourse : function(){//批量导入课程
			var that = vm;
			$("#fileUploadLayer").val("");
			layer.open({
				type : 1,//
				area : ['350px','200px'],
				title :"批量上传",
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#fileUploadLayer"),
				scrollbar : true,//是否允许浏览器出现滚动条
				fixed : false,//固定
				shadeClose : false,// 是否点击遮罩关闭
				resize : true,//是否允许拉伸
				maxmin: true, //开启最大化最小化按钮
				zIndex : 19891014,
				btn : ['确认','取消' ],
				btn1: function (index) {
					$.ajaxFileUpload({
						url:'../courses/cscourse/importCourseExcelInitialization',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						success:function(data){
							if(data.code == 0) {
								alert(data.data ||"批量导入成功", function(alertIndex){
									vm.reload();
									layer.close(index);
								});
								}
							else if(data.code == 1) { 
								alert(data.msg);
							}
						}
					});
				}
			});
		},
		importExcelKnowledge : function(){//批量导入章节知识点
			var that = vm;
			$("#fileUploadLayer").val("");
			layer.open({
				type : 1,//
				area : ['350px','200px'],
				title :"批量上传",
				closeBtn : 1,
				skin:"layui-layer-lan",
				content : $("#fileUploadLayer"),
				scrollbar : true,//是否允许浏览器出现滚动条
				fixed : false,//固定
				shadeClose : false,// 是否点击遮罩关闭
				resize : true,//是否允许拉伸
				maxmin: true, //开启最大化最小化按钮
				zIndex : 19891014,
				btn : ['确认','取消' ],
				btn1: function (index) {
					$.ajaxFileUpload({
						url:'../courses/knowledge/importKnowledgeExcelInitialization',
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						success:function(data){
							if(data.code == 0) {
								alert(data.data ||"批量导入成功", function(alertIndex){
									vm.reload();
									layer.close(index);
								});
								}
							else if(data.code == 1) { 
								alert(data.msg);
							}
						}
					});
				}
			});
		}
		// ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑ 数据初始化导入  ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	}
});