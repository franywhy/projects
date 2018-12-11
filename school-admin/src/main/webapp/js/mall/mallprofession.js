$(function () {
    $("#jqGrid").jqGrid({
        url: '../mallprofession/list',
        datatype: "json",
        colModel: [		
			{ label: 'professionId', name: 'professionId', key: true , hidden: true},
			{ label: '专业名称', name: 'professionName', width: 80 },
			{ label: '试听地址', name: 'auditionUrl', width: 80 }, 		
			{ label: '专业图片', name: 'pic', width: 30 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}}, 						
			{ label: 'app图片', name: 'appPic', width: 30 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}}, 
			{ label: '課程封面', name: 'coursePic', width: 30 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}}, 
			{ label: '品牌封面', name: 'brandPic', width: 30 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}}, 
			{ label: '排序', name: 'orderNum', width: 40 },
//			{ label: '创建人', name: 'creationName', width: 80 },
//			{ label: '创建时间', name: 'creationTime', width: 80 }, 						
//			{ label: '修改人', name: 'modifiedName', width: 80 },
//			{ label: '修改时间', name: 'modifiedTime', width: 80 }, 						
			{ label: '状态', name: 'status', width: 30, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}}
//			{ label: '专业简介', name: 'remark', width: 80 }
			,{ label: '专业简介', name: 'productName', width: 80 }
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
		q: {
			professionName: null,
			productId:"",
			productName:""
		},
		showList: true,
		title: null,
		mallProfession: {
			professionName:"",
			pic:"",
			brandPic:"",
			appPic:"",
			status:1,
			orderNum:"",
			remark:"",
			coursePic:"",
			auditionUrl:"",
			productId:"",
			productName:""
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.mallProfession = {
					professionName:"",
					pic:"",
					brandPic:"",
					appPic:"",
					status:1,
					orderNum:"",
					remark:"",
				    coursePic:"",
				    auditionUrl:"",
				    productId:"",
					productName:""
			};
		},
		update: function (event) {
			var professionId = getSelectedRow();
			if(professionId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(professionId)
		},
		saveOrUpdate: function (event) {
//			if($.isNull(vm.mallProfession.pic)){
//		    	alert("请选择专业图片！！！");
//		    	return;
//		    }
//			if($.isNull(vm.mallProfession.appPic)){
//		    	alert("请选择app图片！！！");
//		    	return;
//		    }
//			if($.isNull(vm.mallProfession.brandPic)){
//		    	alert("请选择品牌封面！！！");
//		    	return;
//		    }
			if($.isNull(vm.mallProfession.professionName)){
		    	alert("请输入专业名称！！！");
		    	return;
		    }
//			if($.isNull(vm.mallProfession.remark)){
//		    	alert("请输入专业简介！！！");
//		    	return;
//		    }
			if($.isNull(vm.mallProfession.orderNum)){
				alert("请输入排序!");
				return;
			}
			if(!$.isNull(vm.mallProfession.remark) && vm.mallProfession.remark.length > 100){
				alert("专业简介不得超过100个字！！！");
				return;
			}
			if($.isNull(vm.mallProfession.productId)){
				alert("请选择产品线!");
				return;
			}
			if(vm.title == "新增")
		    {
		       url = "../mallprofession/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mallprofession/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallProfession),
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
			var professionId = getSelectedRow();
			if(professionId == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "GET",
				    url: "../mallprofession/delete/"+professionId,
				    /*data: JSON.stringify(professionId),*/
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
		getInfo: function(professionId){
			$.ajax({
				type: "GET",
			    url: "../mallprofession/info",
			    data: {professionId , professionId},
			    success: function(r){
					if(r.code == 0){
						vm.mallProfession = r.data;
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
                postData: vm.q,
				page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
			   var professionIds = getSelectedRows();
				if(professionIds == null){
					return ;
				}
				
				confirm('确定要启用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../mallprofession/resume",
					    data: JSON.stringify(professionIds),
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
			resume:function(event){
				   var professionIds = getSelectedRows();
					if(professionIds == null){
						return ;
					}
					
					confirm('确定要启用选中的记录？', function(){
						$.ajax({
							type: "POST",
						    url: "../mallprofession/resume",
						    data: JSON.stringify(professionIds),
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
			    var professionIds = getSelectedRows();
				if(professionIds == null){
					return ;
				}
				
				confirm('确定要禁用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../mallprofession/pause",
					    data: JSON.stringify(professionIds),
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
			selectProduct : function(){//编辑-产品线
				productLay.show(function(index,opt){
					vm.mallProfession.productId = opt.productId;
					vm.mallProfession.productName = opt.productName;
				});
			},
	}
});