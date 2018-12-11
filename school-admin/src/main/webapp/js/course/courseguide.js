$(function () {
    $("#jqGrid").jqGrid({
        url: '../courseguide/list',
        datatype: "json",
        colModel: [		
            { label: 'guideId', name: 'guideId', key: true,hidden:true },
			{ label: '专业ID', name: 'professionId', width: 80 , hidden: true}, 	
			{ label: '专业', name: 'professionName', width: 80 , formatter: function(value, options, row){ return formatterNameAll(value, row.professionId)}},
			{ label: '省份ID', name: 'areaId', width: 80 , hidden: true}, 
			{ label: '省份', name: 'areaName', width: 80 , formatter: function(value, options, row){ return formatterNameAll(value, row.areaId)}}, 
			{ label: '层次ID', name: 'levelId', width: 80, hidden: true }, 						
			{ label: '层次', name: 'levelName', width: 80 , formatter: function(value, options, row){ return formatterNameAll(value, row.levelId)} }, 
			{ label: '名称', name: 'guideName', width: 80 }, 						
			{ label: '跳转地址', name: 'guideUrl', width: 80 }, 						
			{ label: '图片', name: 'guidePic', width: 80,formatter : function(value, options, row){return '<img height="60px" width="60px"  src="'+value+'"/>';}}, 						
		    { label: '指南html内容', name: 'guideHtml', width: 80 , hidden: true}, 						
			{ label: '排序', name: 'orderNum', width: 80 }, 						
			{ label: '状态', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
//														{ label: '平台ID', name: 'schoolId', width: 80 }, 						
			{ label: '创建时间', name: 'createTime', width: 80 }, 						
			{ label: '修改人', name: 'modifiedName', width: 80 }, 						
			{ label: '修改时间', name: 'modifyTime', width: 80 }, 						
			{ label: '创建人', name: 'createdName', width: 80 }						
							
        ],
		viewrecords: true,
        height : 500,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        caption : "流程指南",
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
//$("#jqGrid").jqGrid('navGrid', '#jqGridPager', {edit : false,add : false,del : false});

/*$(function(){
	  $("#list1").jqGrid(
		      {
		    	  url: '../courseguide/list',
		          datatype: "json",
		        colNames : [ 'guideId'],
		        colModel : [ 
		                     {name : 'guideId',width : 75} 
		                   ],
		        rowNum : 10,
		        autowidth : true,
		        rowList : [ 10, 20, 30 ],
		        pager : jQuery('#pager1'),
		        mtype : "post",
		        sortname : 'id',
		        viewrecords : true,
		        sortorder : "desc",
		        caption : "XML 实例"
		      }).navGrid('#pager1', {
		    edit : false,
		    add : false,
		    del : false
		  }); 
		});*/
function formatterNameAll(name , id){
	if(0==id){
		name =  "全部";
	}
	return name
}

var vm = new Vue({
	el:'#rrapp',
	data:{
		showList: true,
		title: null,
		courseGuide: {
			levelName:"",
			levelId:"",
			guidePic:"",
			professionName:"",
			professionId:"",
			areaId:"",
			areaName:"",
			guideName:"",
			orderNum:"",
			guideUrl:""
			
		},
		q:{ //商品查询条件
			guideName : "",
			levelName:"",
			levelId:"",
			professionName:"",
			professionId:"",
			areaId:"",
			areaName:""
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
			/*vm.courseGuide = {professionId:"111"};*/
			vm.courseGuide = {
					levelName:"",
					levelId:"",
					professionName:"",
					professionId:"",
					areaId:"",
					guidePic:"",
					areaName:"",
					guideHtml:"",
					orderNum:1
			};
		},
		copyAdd:function(){
			var guideId = getSelectedRow();
			if(guideId == null){
				return ;
			}
			vm.showList = false;
			vm.title = "复制新增";
			vm.getInfo(guideId);
		},
		update: function (event) {
			var guideId = getSelectedRow();
			if(guideId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(guideId);
		},
		saveOrUpdate: function (event) {
			//取得HTML内容
			var guideHtml = editor.html();
			// 同步数据后可以直接取得textarea的value
			editor.sync();
			guideHtml = $('#editor_id').val(); // jQuery
			// 设置HTML内容
			/*editor.html('HTML内容');*/
			vm.courseGuide.guideHtml = guideHtml;
			var message = "";
			message = "<ul>该笔数据不能添加成功的原因是：";
			var flag = false;
			if($.isNull(vm.courseGuide.professionName)){
				message += "<li>";
				message +="专业不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请选择专业！！！");
		    	return;*/
		    }
			if($.isNull(vm.courseGuide.areaName)){
				message += "<li>";
				message +="省份不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请选择省份！！！");
		    	return;*/
		    }
			if($.isNull(vm.courseGuide.levelName)){
				message += "<li>";
				message +="层次不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请选择层次！！！");
		    	return;*/
		    }
			if($.isNull(vm.courseGuide.guideName)){
				message += "<li>";
				message +="流程指南名称不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请输入流程指南名称！！！");
		    	return;*/
		    }
			/*if($.isNull(vm.courseGuide.guideUrl)){
				message += "<li>";
				message +="跳转地址不能为空";
				message += "</li>";
				flag = true;
		    	alert("请输入跳转地址！！！");
		    	return;
		    }*/
			if($.isNull(vm.courseGuide.guidePic)){
				message += "<li>";
				message +="请选择图片";
				message += "</li>";
				flag = true;
		    	/*alert("请选择图片！！！");
		    	return;*/
		    }
			if($.isNull(vm.courseGuide.orderNum)){
				message += "<li>";
				message +="指南序号不能为空";
				message += "</li>";
				flag = true;
		    	/*alert("请输入指南序号！！！");
		    	return;*/
		    }
			if (flag) {
				message += "</ul>";
				alert(message);
			}
			else{
		    if(vm.title == "新增" || vm.title == "复制新增" )
		    {
		       url = "../courseguide/save";
		       vm.courseGuide.guideId = null;
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../courseguide/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseGuide),
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
			var guideIds = getSelectedRows();
			if(guideIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseguide/delete",
				    data: JSON.stringify(guideIds),
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
		getInfo: function(guideId){
            $.ajax({
				type: "POST",
			    url: "../courseguide/info/" + guideId,
			    success: function(r){
			    	if(r.code === 0){
			    		r.data.areaName = formatterNameAll(r.data.areaName , r.data.areaId);
			    		r.data.professionName = formatterNameAll(r.data.professionName , r.data.professionId);
			    		r.data.levelName = formatterNameAll(r.data.levelName , r.data.levelId);
						vm.courseGuide = r.data;
						editor.html(vm.courseGuide.guideHtml);
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
		levelLayerShow : function() { //层次浮层
			levelAllLay.show(function(index, opt) {
				vm.courseGuide.levelId = opt.levelId;
				vm.courseGuide.levelName = opt.levelName;
			});
		},
		professionLayerShow : function() { //专业浮层
			professionAllLay.show(function(index, opt) {
				vm.courseGuide.professionId = opt.professionId;
				vm.courseGuide.professionName = opt.professionName;
			});
		},
		areaLayerShow : function() { //省份浮层
			areaAllLay.show(function(index, opt) {
				vm.courseGuide.areaId = opt.areaId;
				vm.courseGuide.areaName = opt.areaName;
			});
		},
		//分组条件查询浮层
		levelLayerShowQuery : function() { //层次浮层
			levelAllLay.show(function(index, opt) {
				vm.q.levelId = opt.levelId;
				vm.q.levelName = opt.levelName;
			});
		},
		professionLayerShowQuery : function() { //专业浮层
			professionAllLay.show(function(index, opt) {
				vm.q.professionId = opt.professionId;
				vm.q.professionName = opt.professionName;
			});
		},
		areaLayerShowQuery : function() { //省份浮层
			areaAllLay.show(function(index, opt) {
				vm.q.areaId = opt.areaId;
				vm.q.areaName = opt.areaName;
			});
		},
		clearQuery : function(event) {//重置查询条件
			vm.q = {//
				guideId : "",
				guideName : "",
				levelId : "",
				levelName : "",
				professionId : "",
				professionName : "",
				areaId : "",
				areaName : ""
			}
		},
		resume:function(event){
			   var numbers = getSelectedRows();
				if(numbers == null){
					return ;
				}
				
				confirm('确定要启用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../courseguide/resume",
					    data: JSON.stringify(numbers),
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
			    var numbers = getSelectedRows();
				if(numbers == null){
					return ;
				}
				
				confirm('确定要禁用选中的记录？', function(){
					$.ajax({
						type: "POST",
					    url: "../courseguide//pause",
					    data: JSON.stringify(numbers),
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
			}
	}
});