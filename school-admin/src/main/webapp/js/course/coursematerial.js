$(function () {
    $("#jqGrid").jqGrid({
        url: '../coursematerial/list',
        datatype: "json",
        colModel: [		
        	
	            { label: 'materialId', name: 'materialId', key: true ,hidden:true},
				{ label: '名称', name: 'materialName', width: 80 }, 						
				/*{ label: '课程PK', name: 'courseId', width: 80 }, */
				{ label: '课程ID', name: 'courseId', width: 80 , hidden: true}, 	
				{ label: '课程名称', name: 'courseName', width: 80 },
				{ label: '班型名称ids', name: 'classTypeIds', hidden : true},
				{label : '班型名称',name : 'classTypeNames',hidden : false, formatter : classTypeNameFormatter},
			//	{ label: '班型名称', name: 'courseType', width: 80 },
				{ label: '产品线', name: 'productName', width: 80 },
				{ label: '创建用户', name: 'createdName', width: 80 }, 						
				{ label: '创建时间', name: 'creationTime', width: 80 }, 						
				{ label: '最近修改用户', name: 'modifiedName', width: 80 }, 						
				{ label: '最近修改日期', name: 'modifiedTime', width: 80 } 						
				/*{ label: '平台PK', name: 'schoolId', width: 80 }	*/					
							
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
    $("#jqGridDetail").jqGrid({
    	datatype: "json",
    	colModel: [		
    		{ label: 'materialTypeId', name: 'materialTypeId', key: true ,hidden:true},
    		{ label: '排序', name: 'orderNum', key : true},
    		{ label: '资料库类型名', name: 'materialTypeName', width: 80 }/*, 						
    		{ label: '创建用户', name: 'createdName', width: 80 }, 						
    		{ label: '创建时间', name: 'creationTime', width: 80 }, 						
    		{ label: '最近修改用户', name: 'modifiedName', width: 80 }, 						
    		{ label: '最近修改日期', name: 'modifiedTime', width: 80 }		*/				
        ],
        height : 'auto',
        rownumbers : true, //如果为ture则会在表格左边新增一列，显示行顺序号，从1开始递增。此列名为'rn'.
        viewrecords : true,//定义是否要显示总记录数
        multiselect: true,//定义是否可以多选
        autowidth:false,//自动宽度
    });
    
    //班型权限格式化
	function classTypeNameFormatter(cellvalue, options, rowObject){
	//	alert(rowObject.classTypeIds);
		if(null != rowObject && null != rowObject.classTypeIds && rowObject.classTypeIds.length > 0){
			 return vm.getLiveClassTypeNames(rowObject.classTypeIds);
		}
		return "";
	};
});


function getMaterialId () {
    var selected = $('#materialTable').bootstrapTreeTable('getSelections');
    if (selected.length == 0) {
        alert("请选择一条记录");
        return false;
    } else {
    	return selected[0].id;
    }
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			materialName:null
		},
		showType: 1,
		title: null,
		classTypeList : [],
		classTypeMap : {},
		courseMaterial: {
			classtypeIdArray:[],
			classTypeIds:"",
			classTypeNames:"",
			materialName:"",
			courseId:"",
			courseName:""
		},
		
////////////////////////////////////////////////////////////////////////录播课参数
		//录播课树列表参数
		Record : {
		    id: "materialTable",
		    table: null,
		    layerIndex: -1,
		    
		    initColumn: function () {
			    var columns = [
	   		        {field: 'selectItem', radio: true},
	   		        {title: '资料库类型ID', field: 'id', visible: false, align: 'center', valign: 'middle', width: '30px'},
	   		        {title: '名称', field: 'name', align: 'center', valign: 'middle', sortable: true, width: '100px'},
	   		        {title: '地址', field: 'url', align: 'center', valign: 'middle', sortable: true, width: '100px'},
	   		        {title: '排序', field: 'orderNum', align: 'center', valign: 'middle', sortable: true, width: '30px'},
	   		        {title: '类型', field: 'type', visible: true, align: 'center', valign: 'middle', sortable: true, width: '30px'}
	   		        ]
			   	 return columns;
			}
		},
		materialtype: {
			title:"",
			url:"",
			obj:{
				materialId:"",
				materialTypeName:"",
				orderNum:"",
				materialTypeId:""
			}
		},
		materialDetail: {
			title:"",
			url:"",
			obj:{
				materialTypeId:"",
				detailName:"",
				contentHtml:"",
					url:"",
					orderNum:"",
					detailId:""
			}
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showType = 2;
			vm.title = "新增";
			vm.courseMaterial = {
					classtypeIdArray:[],
					classTypeIds:"",
					classTypeNames:"",
					materialName:"",
					courseId:"", 
					courseName:"",
					courseType:""
			};
			jQuery("#jqGridDetail").jqGrid("clearGridData");//新增的时候清空Grid子表
		},
		update: function (event) {
			var materialId = getSelectedRow();
			if(materialId == null){
				return ;
			}
			vm.showType = 2;
            vm.title = "修改";
            jQuery("#jqGridDetail").jqGrid("clearGridData");
            vm.getInfo(materialId)
		},
		
		reloadAll: function (event){ 
		 
			var materialId = getSelectedRow();
			if(materialId == null){
				return ;
			}
			vm.showType = 2;
            vm.title = "清空班型";
            jQuery("#jqGridDetail").jqGrid("clearGridData");
            vm.getInfo(materialId) 
		},
		
		
		
		
		
		
		saveOrUpdate: function (event) {
			var message = "";
			message = "<ul>该笔数据不能添加成功的原因是：";
			var flag = false;
			if($.isNull(vm.courseMaterial.courseName)){
				message += "<li>";
				message +="课程名不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请选择专业！！！");
		    	return;*/
		    }
			if($.isNull(vm.courseMaterial.materialName)){
				message += "<li>";
				message +="资料库名不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请选择省份！！！");
		    	return;*/
		    }
			if (flag) {
				message += "</ul>";
				alert(message);
			}
			else{
		    if(vm.title == "新增")
		    {
		       url = "../coursematerial/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../coursematerial/update";
		    } else if (vm.title == "清空班型") 
		    	   {
		    	 url = "../coursematerial/update";
		    }else
		    {
		       url = "";
		    }
		    //子表所有数据
			var details = [] ;
			var ids = $("#jqGridDetail").jqGrid('getDataIDs');
			for(var i = 0;i<ids.length;i++){
				var row = $('#jqGridDetail').jqGrid('getRowData',ids[i]);
				if(isNaN(row.id)){
    				row.id = null;
    			}
			    details.push(row);
			}
			vm.courseMaterial.typeList = details;
			//班型array->string
		    vm.courseMaterial.classTypeIds = vm.courseMaterial.classtypeIdArray.toString();
		  /*   alert("保存后"+vm.courseMaterial.classTypeIds ); */
			/*alert("所有数据"+vm.courseMaterial);*/
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseMaterial),
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
			var materialIds = getSelectedRows();
			if(materialIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../coursematerial/delete",
				    data: JSON.stringify(materialIds),
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
		/*getInfo: function(materialId){
            $.ajax({
				type: "POST",
			    url: "../coursematerial/info/" + materialId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseMaterial = r.data;
					}else{
						alert(r.msg);
					}
				}
			});
		},*/
		getInfo: function(materialId){
			$.get("../coursematerial/info/"+materialId, function(r){
				
				vm.courseMaterial = r.courseMaterial;
				  
				//班型string->array
			 
				 vm.courseMaterial.classtypeIdArray=[];
				    if(null != r.courseMaterial&&  vm.title != "清空班型"    ){
					   
					   vm.courseMaterial.classtypeIdArray = r.courseMaterial.classTypeIds.split(",");
				 
				   } 
			 
				
	    		
				
				
                var typeList = r.typeList
                if(null != typeList && typeList.length > 0){
                	$.each(typeList , function(i , v){
                		//添加行
						$("#jqGridDetail").addRowData(v.liveId, v, "last");
                	});
                }
            });
		},

		
		reload: function (event , p) {
			vm.showType = 1;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
                page:page,
                postData:vm.q,
            }).trigger("reloadGrid");
		},
		iadd: function(){
			//弹框标题
			vm.materialtype.title = "新增";
			//初始化弹窗数据
			vm.materialtype.obj = {orderNum:1};
			vm.ishow();
		},
		iupdate:function(){
			//获取选中行ID
			var selectDetail = getJqGridSelectedRow("jqGridDetail");
			if(selectDetail == null){
				return ;
			}
			//行数据
			var rowData = $("#jqGridDetail").jqGrid('getRowData',selectDetail);
			//str=>array
			rowData.numbers = rowData.numbers;
			//
			vm.materialtype.obj = rowData;
			
			//弹框标题
			vm.materialtype.title = "修改";
			
			//弹框
			vm.ishow();
		},
		ishow : function(){//弹出新增或者修改窗口
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.materialtype.title,
				area: ['550px', '550px'],
				shadeClose: false,
				content: jQuery("#materialTypeLayer"),
				btn: ['提交','取消'],
				btn1: function (index) {
					if("新增" == vm.materialtype.title){
						//行ID
						var rowId = new Date().getTime();
						//添加行
						$("#jqGridDetail").addRowData(rowId, vm.materialtype.obj, "last");  
					}else if("修改" == vm.materialtype.title){
						//修改
						$("#jqGridDetail").setRowData(getJqGridSelectedRow("jqGridDetail"),vm.materialtype.obj);
					}
					layer.close(index);
					
	            }
			});
		},
		idel:function(){
			//获取选中行ID
			var selectDetails = getJqGridSelectedRows("jqGridDetail");
			if(selectDetails == null){
				return ;
			}
			confirm('确定要删除选中的记录？', function(){
				$.each(selectDetails , function(i , val){
					$("#jqGridDetail").jqGrid("delRowData", val);  
				});
				return true;
			});
		},
//////////////////////////////////////////////////资料库明细//////////////////////////////////////////////
		materialList : function(){//资料库明细
			vm.materialId = getSelectedRow();
//			vm.recordQuery(vm.courseId);
			if(vm.materialId == null){
				return ;
			}
			
			vm.showType = 3;
			
	        var colunms = vm.Record.initColumn();
	        var table = new TreeTable(vm.Record.id, "../coursematerial/materialDetailList/"+vm.materialId, colunms);
	        table.setRootCodeValue(0);//设置根节点code的值
	        table.setExpandColumn(2);//设置在哪一列上面显示展开按钮，从0开始
	        table.setIdField("type");//设置记录返回的id值
	        table.setCodeField("id");//设置记录分级的字段
	        table.setParentCodeField("parentId");//设置记录分级的父级字段
	        table.setExpandAll(true);//设置是否默认全部展开
	        table.init();
	        vm.Record.table = table
		},
		goBackMaterialList : function(){//
			vm.reload();
		},
		refreshMaterialQuery : function(){//刷新
	        vm.Record.table.refresh();
		},
		goBackMaterialDetailList : function(){//
			vm.showType = 3;
			 vm.Record.table.refresh();
		},
		getSelectedId :function() {//获取选中的ID
		    var selected = $('#materialTable').bootstrapTreeTable('getSelections');
		    
		    if (selected.length == 0) {
		        alert("请选择一条记录");
		        return false;
		    } else {
		    	return selected[0].id;
		    }
		},
		editHtml : function(){//编辑文本编辑器
			var materialDetailIdTemp;
			var materialDetailId;
			var type;
			var parentId;
		    var selected = $('#materialTable').bootstrapTreeTable('getSelections');
		    var array;
		    if (selected.length == 0) {
		        alert("先选中明细,再去编辑文本");
		    } else {
		    	 array=selected[0].id.split(',');
		    	materialDetailIdTemp = array[1];
		        type = array[0];
		    	 parentId = array[2];
		    	 if(type==1){
		    		 alert("先选中明细，再去编辑文本");
		    	 }
		    	 else if(type==2){
		    		 materialDetailId=materialDetailIdTemp;
		    	 }
		    }
			if (materialDetailId == null || !materialDetailId) {
				return;
			}
			vm.showType = 4;
			editor.html('');
			$.get("../coursematerial/materialDetailInfo/" + materialDetailId, function(r) {
				if(r.code == 0){
					vm.materialDetail.obj = r.data;
					if(vm.materialDetail.obj.contentHtml.length>0){
						editor.html(vm.materialDetail.obj.contentHtml);
					}
				}else{
					alert(r.msg);
				}
			});
			
		},
		saveHtmlEditor: function (event) {
			//取得HTML内容
			var guideHtml = editor.html();
			// 同步数据后可以直接取得textarea的value
			editor.sync();
			guideHtml = $('#material_detail').val(); // jQuery
			// 设置HTML内容
			/*editor.html('HTML内容');*/
			vm.materialDetail.obj.contentHtml = guideHtml;
		      var  url = "../coursematerial/materialDetailEdit";
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.materialDetail.obj),
			    success: function(r){
			    	if(r.code === 0){
						alert('操作成功', function(index){
							vm.showType = 3;
							vm.Record.table.refresh();
						});
					}else{
						alert(r.msg);
					}
				}
			});
		},
		
		
		
		recordAddZ : function(){//新增资料库类型
			vm.materialtype.title="新增资料库类型";//标题
			vm.materialtype.url = "../coursematerial/materialTypeSave";//URL
			//录播课编辑
			vm.materialtype.obj={
					materialId:vm.materialId,
					materialTypeName:"",
					orderNum:""
			}
			vm.recordShow();
		},
		recordAddJ : function(){//新增资料库明细
			//获取选中的ID
			var materialTypeIdTemp;
			var materialTypeId;
			var type;
			var parentId;
		    var selected = $('#materialTable').bootstrapTreeTable('getSelections');
		    var array;
		    if (selected.length == 0) {
		        alert("请选择一条记录");
		    } else {
		    	 array=selected[0].id.split(',');
		    	 materialTypeIdTemp = array[1];
		    	 type = array[0];
		    	 parentId = array[2];
		    	 if(type==1){
		    		 materialTypeId=materialTypeIdTemp;
		    	 }
		    	 else if(type==2){
		    		 materialTypeId=parentId;
		    	 }
		    }
			if (materialTypeId == null || !materialTypeId) {
				return;
			}
			
			
			vm.materialDetail.title="新增资料库明细";
			vm.materialDetail.url = "../coursematerial/materialDtailSave";//URL
			//资料库明细
			vm.materialDetail.obj={
					materialTypeId:materialTypeId,
					detailName:"",
					contentHtml:"",
					url:"",
					orderNum:""
			}
			vm.recordDetailShow();
		},
		recordUpdate : function() {
			//获取选中的ID
			var materialTypeIdTemp;
			var materialTypeId;
			var type;
			var parentId;
		    var selected = $('#materialTable').bootstrapTreeTable('getSelections');
		    var array;
		    if (selected.length == 0) {
		        alert("请选择一条记录");
		    } else {
		    	 array=selected[0].id.split(',');
		    	 materialTypeIdTemp = array[1];
		    	 type = array[0];
		    	 parentId = array[2];
		    	 if(type==1){
		    		 materialTypeId=materialTypeIdTemp;
		    	 }
		    	 else if(type==2){
		    		 materialTypeId=parentId;
		    	 }
		    }
			if (materialTypeId == null || !materialTypeId) {
				return;
			}
			$.get("../coursematerial/materialTypeInfo/" + materialTypeId, function(r) {
				if(r.code == 0){
					vm.materialtype.obj = r.courserMaterialTypePOJO;
				}else{
					alert(r.msg);
				}
			});
			vm.materialtype.obj.materialTypeId=materialTypeId;
			vm.materialtype.title = "编辑资料库类型"; //标题
			vm.materialtype.url = "../coursematerial/materialTypeEdit";//URL
			vm.recordShow();

		},
		recordDetailUpdate : function() {
			var materialDetailIdTemp;
			var materialDetailId;
			var type;
			var parentId;
		    var selected = $('#materialTable').bootstrapTreeTable('getSelections');
		    var array;
		    if (selected.length == 0) {
		        alert("先选中明细,再去修改指定明细");
		    } else {
		    	 array=selected[0].id.split(',');
		    	materialDetailIdTemp = array[1];
		        type = array[0];
		    	 parentId = array[2];
		    	 if(type==1){
		    		 alert("先选中明细，再去修改指定明细");
		    	 }
		    	 else if(type==2){
		    		 materialDetailId=materialDetailIdTemp;
		    	 }
		    }
			if (materialDetailId == null || !materialDetailId) {
				return;
			}

			$.get("../coursematerial/materialDetailInfo/" + materialDetailId, function(r) {
				if(r.code == 0){
					vm.materialDetail.obj = r.data;
				}else{
					alert(r.msg);
				}
			});
			vm.materialDetail.obj.detailId=materialDetailId;
			vm.materialDetail.title = "编辑资料库类型"; //标题
			vm.materialDetail.url = "../coursematerial/materialDetailEdit";//URL
			vm.recordDetailShow();

		},
		recordShow : function(){//弹出编辑或新增框
			layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				title: vm.materialtype.title,//标题
				area: ['600px', '440px'],
				shadeClose: false,
				content: jQuery("#materialTypeLayer"),
				btn: ['提交','取消'],
				btn1: function (layerIndex) {
					//非空校验
					//名称
					if($.isNull(vm.materialtype.obj.materialTypeName)){
						alert("名称不能为空");
						return;
					}
					if($.isNull(vm.materialtype.obj.orderNum)){
						alert("排序不能为空");
						return;
					}
					$.ajax({
						type: "POST",
					    url: vm.materialtype.url,
					    data: JSON.stringify(vm.materialtype.obj),
					    success: function(r){
					    	if(r.code === 0){
								alert('操作成功', function(index){
									vm.Record.table.refresh();
									layer.close(layerIndex);
								});
							}else{
								alert(r.msg);
							}
						}
					});
	            }
			});
		},
		recordDetailShow : function(){//弹出明细编辑或新增框
		layer.open({
				type: 1,
				skin: 'layui-layer-molv',
				z_index: 1119999,
				title: vm.materialDetail.title,//标题
				area: ['600px', '440px'],
				shadeClose: false,
				content: jQuery("#materialDetailLayer"),
				btn: ['提交','取消'],
				btn1: function (layerIndex) {
					//非空校验
					//名称
					if($.isNull(vm.materialDetail.obj.detailName)){
						alert("名称不能为空");
						return;
					}
					if($.isNull(vm.materialDetail.obj.orderNum)){
						alert("排序不能为空");
						return;
					}
					$.ajax({
						type: "POST",
					    url: vm.materialDetail.url,
					    data: JSON.stringify(vm.materialDetail.obj),
					    success: function(r){
					    	if(r.code === 0){
								alert('操作成功', function(index){
									vm.Record.table.refresh();
									layer.close(layerIndex);
								});
							}else{
								alert(r.msg);
							}
						}
					});
	            }
			});
		},
		recordDelete : function(){//删除类型
			//获取选中的ID
			var materialTypeIdTemp;
			var materialTypeId;
			var type;
			var parentId;
		    var selected = $('#materialTable').bootstrapTreeTable('getSelections');
		    var array;
		    if (selected.length == 0) {
		        alert("请选择一条记录");
		    } else {
		    	   array=selected[0].id.split(',');
		    	 materialTypeIdTemp = array[1];
		    	 type = array[0];
		    	 parentId = array[2];
		    	 if(type==1){
		    		 materialTypeId=materialTypeIdTemp;
		    	 }
		    	 else if(type==2){
		    		 materialTypeId=parentId;
		    	 }
		    }
			if (materialTypeId == null || !materialTypeId) {
				return;
			}

			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "GET",
				    url: "../coursematerial/materialTypeDelete/" + materialTypeId,
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.Record.table.refresh();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		recordDetailDelete : function(){//删除类型
			var materialDetailIdTemp;
			var materialDetailId;
			var type;
			var parentId;
		    var selected = $('#materialTable').bootstrapTreeTable('getSelections');
		    var array;
		    if (selected.length == 0) {
		        alert("先选中明细,再去删除指定明细");
		    } else {
		    	array=selected[0].id.split(',');
		    	materialDetailIdTemp = array[1];
		        type = array[0];
		    	 parentId = array[2];
		    	 if(type==1){
		    		 alert("先选中明细，再去删除指定明细");
		    	 }
		    	 else if(type==2){
		    		 materialDetailId=materialDetailIdTemp;
		    	 }
		    }
			if (materialDetailId == null || !materialDetailId) {
				return;
			}
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "GET",
				    url: "../coursematerial/materialDetailDelete/" + materialDetailId,
				    success: function(r){
						if(r.code == 0){
							alert('操作成功', function(index){
								vm.Record.table.refresh();
							});
						}else{
							alert(r.msg);
						}
					}
				});
			});
		},
		//获取班型列表
		getclassTypeList: function(){
			$.get("../mall/classtype/select", function(data){
				vm.classTypeList = data.data;
				$.each(vm.classTypeList , function(i , v){
					vm.classTypeMap[v.value] = v.name;
				});
			});
		},
		//获取班型名称
		getLiveClassTypeNames : function(ids){
			var str = "";
			if(null == ids || ids.length == 0){
				return str;
			}
			if(!$.isArray(ids)){
				ids = ids.split(",");
			}
			$.each(ids , function(i , val){
				str += vm.classTypeMap[val] + ";";
			});
			return str.length > 0 ? str.substring(0 , str.length-1) : "";
		},
		
		
		
		//课程浮层
		courseLayerShow : function() { 
			courseLay.show(function(index, opt) {
				vm.courseMaterial.courseId = opt.courseId;
				vm.courseMaterial.courseName = opt.courseName;
			});
		},
		
	},
	created : function(){
		this.getclassTypeList();//班型权限初始化
//		$(".datetimepicker_start").datetimepicker('setDate', (new Date()) );
//		$(".datetimepicker_end").datetimepicker('setDate', (new Date()) );
	}
});