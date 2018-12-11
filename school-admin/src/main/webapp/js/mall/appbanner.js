$(function () {
    $("#jqGrid").jqGrid({
        url: '../appbanner/list',
        datatype: "json",
        colModel: [		
				{ label: 'id', name: 'id', width: 80, key: true },
				{ label: '名称', name: 'name', width: 80 }, 						
				{ label: '图片', name: 'pic', width: 50 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}},
				{ label: '学历pk', name: 'levelId', hidden: true }, 
				{ label: '学历', name: 'levelName', width: 80 }, 
				{ label: '专业pk', name: 'professionId', hidden: true },
				{ label: '专业', name: 'professionName', width: 80 }, 
				{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
					return value === 0 ? 
						'<span class="label label-danger">禁用</span>' : 
						'<span class="label label-success">正常</span>';
				}}, 						
				{ label: '创建人', name: 'creationName', width: 80 },
				{ label: '创建时间', name: 'createTime', width: 80 }, 						
				{ label: '修改人', name: 'modifiedName', width: 80 }, 						
				{ label: '修改时间', name: 'modifyTime', width: 80 }						
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
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			name: null,
			levelId: null,
			levelName: null,
			professionId: null,
			professionName: null
		},
		showList: true,
		title: null,
		appBanner: {
			name:"",
			pic:"",
			levelId:"",
			levelName:"",
			professionId:"",
			professionName:""
		},
		/*selectData:{//下拉初始化列表
			levelList :[{levelId:"", levelName:"数据加载中。。。"}],//学历
			professionList :[{professionId:"", professionName:"数据加载中。。。"}],//专业
		},*/
	},
	methods: {
		selectLevel: function(){
			levelLay.show(function(index,opt){
				//学历ID
				vm.appBanner.levelId = opt.levelId;
				//学历名称
				vm.appBanner.levelName = opt.levelName;
			});
		},
		selectProfession: function(){
			professionLay.show(function(index,opt){
				//学历ID
				vm.appBanner.professionId = opt.professionId;
				//学历名称
				vm.appBanner.professionName = opt.professionName;
			});
		},
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.appBanner = {
					name:"",
					pic:"",
					levelId:"",
					levelName:"",
					professionId:"",
					professionName:""
			};
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(id)
		},
		saveOrUpdate: function (event) {
			if($.isNull(vm.appBanner.name)){
				alert("请输入名称！！！");
				return;
			}
			if($.isNull(vm.appBanner.pic)){
				alert("请输入图片！！！");
				return;
			}
			if($.isNull(vm.appBanner.levelId)){
				alert("请输入学历！！！");
				return;
			}
			if($.isNull(vm.appBanner.professionId)){
				alert("请输入专业！！！");
				return;
			}
		    if(vm.title == "新增")
		    {
		       url = "../appbanner/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../appbanner/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.appBanner),
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
			var ids = getSelectedRows();
			if(ids == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../appbanner/delete",
				    data: JSON.stringify(ids),
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
		getInfo: function(id){
			$.get("../appbanner/info/"+id, function(r){
                vm.appBanner = r.data;
            });
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
			   var ids = getSelectedRows();
				if(ids == null){
					return ;
				}
				
				confirm('确定要启用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../appbanner/resume",
					    data: JSON.stringify(ids),
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
			    var ids = getSelectedRows();
				if(ids == null){
					return ;
				}
				
				confirm('确定要禁用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../appbanner/pause",
					    data: JSON.stringify(ids),
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
		clearQuery: function(event){//重置查询条件
			vm.q = {//appBanner档案查询条件
				name:"",
				levelId:"",
				levelName:"",
				professionId:"",
				professionName:""
			}
		},
		////////////////////////查询条件浮层///////////////////////
		levelLayerShowQuery : function() { //层次浮层
			levelLay.show(function(index, opt) {
				vm.q.levelId = opt.levelId;
				vm.q.levelName = opt.levelName;
			});
		},
		professionLayerShowQuery : function() { //专业浮层
			professionLay.show(function(index, opt) {
				vm.q.professionId = opt.professionId;
				vm.q.professionName = opt.professionName;
			});
		},
		/*initLevelList: function(){//初始化学历列表
			var that = this;
			$.ajax({
				type: "GET",
				url: "../common/levelList",
				success: function(r){
					if(r.code == 0){
						that.selectData.levelList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},*/
		/*initProfessionList: function(){//初始化专业列表
			var that = this;
			$.ajax({
				type: "GET",
			    url: "../common/professionList",
			    success: function(r){
					if(r.code == 0){
						that.selectData.professionList = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},*/
	},
	/*created: function(){
		var vm = this;
		//初始化学历列表
		vm.initLevelList();
		//初始化专业列表
		vm.initProfessionList();
	}*/
});