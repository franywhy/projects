$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/webbanner/list',
        datatype: "json",
        colModel: [		
        	
				{ label: '横幅id', name: 'id', hidden: true, key: true },
				{ label: '名称', name: 'name', width: 80 }, 						
				{ label: '图片', name: 'pic', width: 50 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}}, 						
				{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
					return value === 0 ? 
						'<span class="label label-danger">禁用</span>' : 
						'<span class="label label-success">正常</span>';
				}},						
				{ label: '跳转地址', name: 'url', width: 80 },
				{ label: 'banner颜色', name: 'colourText', width: 80 },
				{ label: '序号', name: 'orderNum', width: 80 }
							
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
			name:null
		},
		showList: true,
		title: null,
		webBanner: {
			name:"",
			pic:"",
			status:"",
			url:"",
			colourText:"",
			orderNum:""
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.webBanner = {
					name:"",
					pic:"",
					status:"",
					url:"",
					colourText:"",
					orderNum:""
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
			if($.isNull(vm.webBanner.name)){
		    	alert("请输入名称！！！");
		    	return;
		    }
			if($.isNull(vm.webBanner.pic)){
		    	alert("请上传图片！！！");
		    	return;
		    }
			/*if($.isNull(vm.webBanner.url)){
		    	alert("请输入跳转地址！！！");
		    	return;
		    }*/
			if($.isNull(vm.webBanner.colourText)){
		    	alert("请输入颜色！！！");
		    	return;
		    }
			if($.isNull(vm.webBanner.orderNum)){
		    	alert("请输入序号！！！");
		    	return;
		    }
			if(vm.title == "新增")
		    {
		       url = "../mall/webbanner/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/webbanner/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.webBanner),
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
				    url: "../mall/webbanner/delete",
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
            $.ajax({
				type: "POST",
			    url: "../mall/webbanner/info/"+id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.webBanner = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                postData:vm.q,
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
					    url: "../mall/webbanner/resume",
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
					    url: "../mall/webbanner/pause",
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
	}
});