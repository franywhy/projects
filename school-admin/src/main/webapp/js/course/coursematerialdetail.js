$(function () {
    $("#jqGrid").jqGrid({
        url: '../coursematerialdetail/list',
        datatype: "json",
        colModel: [		
        	
	            { label: 'detailId', name: 'detailId', key: true , hidden: true},
				{ label: '名称', name: 'detailName', width: 80 }, 						
				{ label: '资料库类型ID', name: 'materialTypeId', width: 80 , hidden: true},
				{ label: '资料库类型名', name: 'materialTypeName', width: 80 },
				{ label: '资料库ID', name: 'materialId', width: 80 , hidden: true},
				{ label: '资料库名', name: 'materialName', width: 80 },
				{ label: 'html内容', name: 'contentHtml', width: 80 ,hidden: true}, 						
				{ label: '地址', name: 'url', width: 80 }, 						
				{ label: '阅读量', name: 'readNum', width: 80 ,hidden: true}, 						
				{ label: '排序', name: 'orderNum', width: 80 }, 						
				{ label: '产品线', name: 'productName', width: 80 }, 						
				{ label: '创建用户', name: 'createdName', width: 80 }, 						
				{ label: '创建时间', name: 'creationTime', width: 80 }, 						
				{ label: '最近修改用户', name: 'modifiedName', width: 80 }, 						
				{ label: '最近修改日期', name: 'modifiedTime', width: 80 }, 						
				{ label: '平台PK', name: 'schoolId', width: 80 ,hidden: true}						
							
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
		showList: true,
		title: null,
		courseMaterialDetail: {
			materialName:"",
			materialTypeName:"",
			materialTypeId:"",
			contentHtml:"",
			materialId:"",
		    orderNum:"",
		    url:"",
		    productId:"",
		    productName:""
		},
		q:{ //资料库明细查询条件
			materialName:"",
			materialTypeName:"",
			materialTypeId:"",
			detailName:"",
			materialId:"",
			detailId:""
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			editor.html('');
			vm.courseMaterialDetail = {
					materialName:"",
					materialTypeName:"",
					contentHtml:"",
					materialTypeId:"",
					orderNum:1,
					url:"",
					productId:"",
				    productName:""
			};
		},
		update: function (event) {
			var detailId = getSelectedRow();
			if(detailId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(detailId);
           
		},
		saveOrUpdate: function (event) {
			if($.isNull(vm.courseMaterialDetail.productId)){
				alert("请选择产品线！！！");
				return;
			}
			//取得HTML内容
			var contentHtml = editor.html();
			// 同步数据后可以直接取得textarea的value
			editor.sync();
			contentHtml = $('#editor_id_material').val(); // jQuery
			// 设置HTML内容
			/*editor.html('HTML内容');*/
			vm.courseMaterialDetail.contentHtml = contentHtml;
			var message = "";
			message = "<ul>该笔数据不能添加成功的原因是：";
			var flag = false;
			if($.isNull(vm.courseMaterialDetail.detailName)){
				message += "<li>";
				message +="资料库明细名不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请选择专业！！！");
		    	return;*/
		    }
			if($.isNull(vm.courseMaterialDetail.materialName)){
				message += "<li>";
				message +="资料库名不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请选择省份！！！");
		    	return;*/
		    }
			if($.isNull(vm.courseMaterialDetail.materialTypeName)){
				message += "<li>";
				message +="资料库类型不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请选择层次！！！");
		    	return;*/
		    }
			/*if($.isNull(vm.courseMaterialDetail.url)){
				message += "<li>";
				message +="url不能为空";
				message += "</li>";
				flag = true;
		    	alert("请输入流程指南名称！！！");
		    	return;
		    }*/
			if($.isNull(vm.courseMaterialDetail.contentHtml)){
				message += "<li>";
				message +="html内容不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请输入跳转地址！！！");
		    	return;*/
		    }
			if (flag) {
				message += "</ul>";
				alert(message);
			}
			else{
		    if(vm.title == "新增")
		    {
		       url = "../coursematerialdetail/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../coursematerialdetail/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseMaterialDetail),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.reload();
						});
					}else{
						alert(r.msg);
					}
				}
			});}
		},
		del: function (event) {
			var detailIds = getSelectedRows();
			if(detailIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../coursematerialdetail/delete",
				    data: JSON.stringify(detailIds),
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
		getInfo: function(detailId){
            $.ajax({
				type: "POST",
			    url: "/coursematerialdetail/info/" + detailId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseMaterialDetail = r.data;
						 editor.html(vm.courseMaterialDetail.contentHtml);
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (event , p) {
			var vm = this;
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
                page:page
            }).trigger("reloadGrid");
		},
		selectProduct : function(){
			productLay.show(function(index,opt){
				vm.courseMaterialDetail.productId = opt.productId;
				vm.courseMaterialDetail.productName = opt.productName;
			});
		},
		materialLayerShow : function() { //资料库浮层
			materialLay.show(function(index, opt) {
				vm.courseMaterialDetail.materialId = opt.materialId;
				vm.courseMaterialDetail.materialName = opt.materialName;
			});
		},
		materialTypeLayerShow : function() { //资料库类型浮层
			var vm = this;
			if($.isNull(vm.courseMaterialDetail.materialId)){
				alert("请先选择所属的资料库");
				return;
			}
			materialTypeLay.show(vm.courseMaterialDetail.materialId,function(index, opt) {
				/*materialTypeLay.init(getSelectedRow());*/
				vm.courseMaterialDetail.materialTypeId = opt.materialTypeId;
				vm.courseMaterialDetail.materialTypeName = opt.materialTypeName;
			});
		},
		selUrl : function(){
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: '导入Excel',
				area: ['300px', '200px'],
				shadeClose: false,
				content: jQuery("#selUrl"),
				btn: ['确定','取消'],
				btn1: function (index) {
					
				}
			});
		},
		//分组条件查询浮层
		materialLayerShowQuery : function() { //资料库浮层
			materialLay.show(function(index, opt) {
				vm.q.materialId = opt.materialId;
				vm.q.materialName = opt.materialName;
			});
		},
		materialTypeLayerShowQuery : function() {
			var vm = this;
			if($.isNull(vm.q.materialId)){
				alert("请先选择所属的资料库");
				return;
			}//资料库类型浮层
			materialTypeLay.show(vm.q.materialId,function(index, opt) {
				vm.q.materialTypeId = opt.materialTypeId;
				vm.q.materialTypeName = opt.materialTypeName;
			});
		},
		clearQuery : function(event) {//重置查询条件
			vm.q = {
					materialName:"",
					materialTypeName:"",
					materialTypeId:"",
					detailName:"",
					materialId:"",
					detailId:""
			}
		}
	}
});