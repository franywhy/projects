$(function () {
    $("#jqGrid").jqGrid({
        url: '../course/courserecorddetail/list',
        datatype: "json",
        colModel: [		
				{ label: 'recordId', name: 'recordId', key: true,hidden:true },
				{ label: '录播课ID', name: 'vid', width: 80 }, 						
				{ label: '录播课名称', name: 'recordName', width: 80 }, 						
				{ label: '时长', name: 'duration', width: 80 }, 						
				{ label: '课程PK', name: 'courseId', width: 80,hidden: true }, 	
				{ label: '课程名称', name: 'courseName', width: 80 }, 	
				{ label: '排序', name: 'orderNum', width: 80 },
				{ label: '其他平台URL', name: 'otherUrl', width: 80 }, 
				{ label: '创建用户ID', name: 'createPerson', width: 80,hidden: true }, 
				{ label: '创建用户', name: 'createPersonName', width: 80 }, 						
				{ label: '创建时间', name: 'creationTime', width: 80 }, 
				{ label: '修改用户ID', name: 'modifyPerson', width: 80,hidden: true }, 
				{ label: '最近修改用户', name: 'modifyPersonName', width: 80 }, 						
				{ label: '最近修改日期', name: 'modifiedTime', width: 80 }, 						
							
        ],
		viewrecords: true,
        height: 550,
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
    
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		q:{
			recordName:null,
		},
		courseRecordDetail: {
			recordId:null,
			vid:null,
			recordName:null,
			recordTitle:null,
			duration:null,
			context:null,
			ptime:null,
			firstImage:null,
			courseId:null,
			orderNum:null,
			courseId:null,
			courseName:null,
			otherUrl:null
		},
		layerIndex : null
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseRecordDetail = {
				recordId:"",
				vid:"",
				recordName:"",
				recordTitle:"",
				duration:"",
				context:"",
				ptime:"",
				firstImage:"",
				courseId:"",
				orderNum:"",
				courseId:"",
				courseName:"",
				otherUrl:""
			};
		},
		update: function (event) {
			var recordId = getSelectedRow();
			if(recordId == null){
				return;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(recordId)
		},
		saveOrUpdate: function (event) {
		    if(vm.title == "新增")
		    {
		       url = "../course/courserecorddetail/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../course/courserecorddetail/update";
		    }else
		    {
		       url = "";
		    }
		    console.log(vm.courseRecordDetail);
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseRecordDetail),
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
			var recordIds = getSelectedRows();
			if(recordIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../course/courserecorddetail/delete",
				    data: JSON.stringify(recordIds),
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
		getInfo: function(recordId){
            $.ajax({
				type: "POST",
			    url: "../course/courserecorddetail/info/" + recordId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseRecordDetail = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"recordName" : vm.q.recordName},
                page:page
            }).trigger("reloadGrid");
		},
		courseShow: function(){
			courseLay.show(function(index,opt){
				var courseId = opt.courseId;
				$.get("../course/courserecorddetail/course/"+courseId, function(r){
	                vm.courseRecordDetail.courseId = r.courses.courseId;
	                vm.courseRecordDetail.courseName = r.courses.courseName;
	            });
			});			
		},
		
		videoShow : function(){
			vm.selVideo();
		},
		
		selVideo : function(){//录播课弹窗,资源从保利威视获取
			/*//显示浮层
			vm.showVideoLay();
			
			//加载数据
			vm.reloadVideoJqGrid();*/
			
			polyvVideoLay.show(function(index,opt){
				vm.courseRecordDetail.vid = opt.vid;
				vm.courseRecordDetail.recordTitle = opt.title;
				vm.courseRecordDetail.duration = opt.duration;
				vm.courseRecordDetail.context = opt.context;
				vm.courseRecordDetail.ptime = opt.ptime;
				vm.courseRecordDetail.firstImage = opt.first_image;
			});	
		},
	}
});