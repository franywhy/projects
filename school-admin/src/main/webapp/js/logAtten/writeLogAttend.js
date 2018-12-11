
$(function () {
	$("#jqGrid").jqGrid({
        url: '../logStudentAtten/list',
        datatype: "json",
        colModel: [		
                { label: '学员账号', name: 'mobileNo' , width: 50 },
                { label: '学员ID', name: 'userId' , width: 50 },
                { label: '学员ID', name: 'userId' , width: 50 },
        ],
        viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: true,
        //pager: "#jqGridPager",
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
	
	// ------------------------------------------------------------排课明细------------------------------------
    $("#classplanLivesGrid").jqGrid({
        url: '../layerdata/classplanLivesList',
        datatype: "local",
        colModel: [		
        	{ label: '子表ID', name: 'classplanLiveId', hidden : true, width: 50, key: true },
			{ label: '主表ID', name: 'classplanId', hidden : true, width: 80 }, 
			{ label: '课程ID', name: 'courseId', hidden : true, width: 80 }, 
			{ label: '直播课名称', name: 'classplanLiveName', width: 550 },
			{ label: '直播课ID', name: 'courseLiveDetailId', hidden : true },
        ],
		viewrecords: true,
        height : 500,
        rowNum: 100,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: false,
        pager: "#classplanLivesGridPager",
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
        	$("#classplanLivesGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			classplanId:"",
			classplanName:"",
			classplanLiveId:"",
			classplanLiveName:"",
		},
		showList: true,
		title: null,
	},
	methods: {
		reload: function (e , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				page:page,
                datatype: "json"
            }).trigger("reloadGrid");
		},
		
		classplanLayerShow : function(){//排课浮层
			classplanLay.show(function(index,opt){
				vm.q.classplanId = opt.classplanId;
				vm.q.classplanName = opt.classplanName;
			});
		},
		classplanLivesLayerShow: function (event) {
			vm.selClassplanLive(function(selectDetail , rowData){
				vm.q.classplanLiveId = rowData.classplanLiveId;
				vm.q.classplanLiveName = rowData.classplanLiveName;
				return true;
			});
		},
		selClassplanLive : function(callblack){//班级弹窗
			//显示浮层
			vm.showClassplanLivesLay(callblack);
			//加载数据
			vm.reloadClassplanLivesJqGrid();
		},
		showClassplanLivesLay : function(callblack){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '排课明细',
				area: ['600px', '600px'],
				shadeClose: false,
				content: jQuery("#classplanLiveslayer"),
				btn: ['确定','取消'],
				btn1: function (index) {
					//获取选中行ID
					var selectDetail = getJqGridSelectedRow("classplanLivesGrid");
					if(selectDetail == null){
						return ;
					}
					//行数据
					var rowData = $("#classplanLivesGrid").jqGrid('getRowData',selectDetail);
					if($.isFunction(callblack)){
						if(callblack(selectDetail , rowData)){
							//关闭浮层
							layer.close(index);
						}
					}
	            }
			});
		},
		reloadClassplanLivesJqGrid : function(){//刷新班级列表
			$("#classplanLivesGrid").jqGrid('setGridParam',{
	            page : 1,
	            url : '../layerdata/classplanLivesList?classplanId=' + vm.q.classplanId,
	            datatype : 'json'
	        }).trigger("reloadGrid")
		},
		clearQuery : function(){//清空查询条件
			var vm = this;
			vm.q = {
				classplanId:"",//排课
				classplanName:"",
				classplanLiveId:"",
				classplanLiveName:"",
			};
		},
		uploadExcelMethod : function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '导入Excel',
				area: ['300px', '200px'],
				shadeClose: false,
				content: jQuery("#uploadExcel"),
				btn: ['确定','取消'],
				btn1: function (index) {
					$.ajaxFileUpload({
						url:'../logStudentAtten/getExcelLogAttendData?classplanLiveId='+vm.q.classplanLiveId,
						secureuri:true,
						fileElementId:'file_data',
						dataType:'json',
						success:function(data){
							if(data.code == 0) {
								/*alert("文件上传成功", function(index){
									$("#jqGrid").trigger("reloadGrid");
								});*/
								alert("导入完成");
								layer.close(index);
							}else if(data.code == 1) { 
								alert(data.msg);
								layer.close(index);
							}
						}
					});
	            }
			});
		},
	},
});