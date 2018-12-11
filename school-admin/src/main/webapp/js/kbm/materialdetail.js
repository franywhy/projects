$(function () {
	
	$(".once_datetimepicker").datetimepicker({
 		format: 'yyyy-mm-dd hh:ii:ss',/*'yyyy-mm-dd hh:ii:00',*/
// 		maxView: view,
 		zIndex : 999999999,
 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
    });
	$(".everyday_datetimepicker").datetimepicker({
 		format: 'hh:ii:ss',/*'yyyy-mm-dd hh:ii:00',*/
 		maxView: 1,
 		zIndex : 999999999,
 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
    });
	 
    $("#jqGrid").jqGrid({
        url: '../materialdetail/list',
        datatype: "json",
        colModel: [		
        	
											{ label: '序号', name: 'id',hidden : true, key: true ,width: 40},
											{ label: '资料名称', name: 'name', width: 100 }, 						
											{ label: '属性', name: 'type', width: 55 ,formatter:"select", editoptions:{value:"VIDEO:视频;LIVE:直播;QUESTIONBANK:题库;TRAINING:实训;FILE:文件;FACETOFACE:面授;OTHER:其他"}}, 						
											{ label: '类型', name: 'property', width: 55 ,formatter:"select", editoptions:{value:"BEFORECLASS:课前;MIDDLECLASS:课中;AFTERCLASS:课后"}}, 						
											{ label: '创建时间', name: 'createTime', width: 100 }, 						
											{ label: '更新时间', name: 'updateTime', width: 100 }, 						
											/*{ label: '状态', name: 'status', width: 80 }, 				*/		
											{ label: '创建人', name: 'creationName', width: 80 }, 					
											{ label: '最后修改人', name: 'modifiedName', width: 80 }, 						
											{ label: '是否关联', name: 'isRelevance', width: 55 }, 				
											{ label: '操作', name:'process' , width: 60 }, 	
							
        ],
		viewrecords: true,
        height : 520,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 35, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
       
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"page", 
            rows:"limit", 
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        	
         
        	
        	var ids = jQuery("#jqGrid").jqGrid('getDataIDs');
			for ( var i = 0; i < ids.length; i++) {
				var materialId = ids[i];
			 
				view = " <a class='btn btn-primary'  onclick='viewMaterial(" + materialId + ")'><i class='fa fa-search'></i>&nbsp;查看</a>";  
				jQuery("#jqGrid").jqGrid('setRowData',
						ids[i], {
							process : view
						});
			}
        }
    });
    
    //$("#select_type").find("option[value='"+vm.types[2].v+"']").attr("selected","true");
    $("#qbCourseSelect").change(function() {

		var p1 = $(this).children('option:selected').val();// 这就是selected的值
		vm.questionBankCourse = p1;
		vm.courseSelected();
	});
    $("#qbChapterSelect").change(function() {

		var p1 = $(this).children('option:selected').val();// 这就是selected的值
		vm.questionBankChapter = p1;
		vm.chapterSelected();
	});
    
    $("#qbVerseSelect").change(function() {

		var p1 = $(this).children('option:selected').val();// 这就是selected的值
		vm.questionBankVerse = p1;
		
	});
    
    
});

function viewMaterial(id){
	vm.ishow(id);
}

function selectType(){
	 document.getElementById("select_type")[2].selected=true;
}

 //格式化字符
function myFormatter(ival,items){
	 for(var i=0;i<items.length;i++){
		 
		 if(ival==items[i].v){
				return items[i].t;
			}
		}
	 return null;
}


var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			name: "",
			property: "",
			type: "",
			isRelevance: ""
		},
		showList: true,
		title: null,
		materialDetail: {},
		showMaterialObj: {
			/*pushType: "",
			pushTime: ""*/
		},
		propertys:[
			 {t:"课前",v:"BEFORECLASS"},
             {t:"课中",v:"MIDDLECLASS"},
             {t:"课后",v:"AFTERCLASS"}
		],
		types:[
			{t:"视频",v:"VIDEO"},
            {t:"直播",v:"LIVE"},
            {t:"题库",v:"QUESTIONBANK"},
            {t:"实训",v:"TRAINING"},
            {t:"文件",v:"FILE"},
            {t:"面授",v:"FACETOFACE"},
            {t:"其他",v:"OTHER"}
		],
		questionBankCourse : "",
		questionBankChapter: "",
		questionBankVerse: ""
	},
	methods: {
		query: function () {   
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			page=1; //防止出现查询bug, 每次点击查询时页码数都是从1开始
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"name" : vm.q.name,"s_property":vm.q.property,"s_type":vm.q.type,"isRelevance":vm.q.isRelevance,"timeOrdering":$("#s_time").val()},
                page:page
            }).trigger("reloadGrid");
		},
		 
		add: function(){
			vm.showList = false;
			vm.title = "新增";
 			vm.materialDetail = {};
			document.getElementById("isRelId2").style.display="none";
			document.getElementById("typeDivUdate").style.display="none";
			document.getElementById("typeDivAdd").style.display="block";
			
			 console.log( document.getElementById("select_type")[2]);
			 console.log(vm.types[2].v);
			 console.log($("#select_type")[2]);
			//$("#select_type").find("option[value='"+vm.types[2].v+"']").attr("selected","true");
			   $("#select_type option").eq(2).attr('selected', 'true');
			    console.log(vm.materialDetail.type);
            console.log("vm.title="+vm.title);
            
            //
            vm.questionBankCourse = "";
			vm.questionBankChapter = "";
			vm.questionBankVerse = "";
            vm.questionCourseInit();
		},
		update: function (event) {
			var id = getSelectedRow();
			if(id == null){
				return ;
			}
			vm.showList = false;
            vm.title = "编辑";
            var xc=document.getElementById("isRelId2");
            document.getElementById("typeDivUdate").style.display="block";
			document.getElementById("typeDivAdd").style.display="none";
            $.ajax({
				type: "POST",
			    url: "../materialdetail/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.materialDetail = r.materialDetail;
						 if(vm.materialDetail.pushType=='1'){
							 $("#everyday_pushtime").val(vm.materialDetail.pushTime);
							 $("#once_pushtime").val("");
							}else if(vm.materialDetail.pushType=='0'){
							 $("#once_pushtime").val(vm.materialDetail.pushTime);
							 $("#everyday_pushtime").val("");
							}else{
								 $("#once_pushtime").val("");
								 $("#everyday_pushtime").val("");
							}
						 if(vm.materialDetail.isRelevance==null){
							 xc.style.display="none";
						 }
						 if(vm.materialDetail.isRelevance!=null){
							 xc.style.display="block";
						 }
						 //
						 vm.questionBankCourse = vm.materialDetail.questionBankCourse;
						 vm.questionBankChapter = vm.materialDetail.questionBankChapter;
						 vm.questionBankVerse = vm.materialDetail.questionBankVerse;
						 vm.questionCourseInit();
					}else{
						alert(r.msg);
					}
				}
			});
            
		},
		saveOrUpdate: function (event) {
			
			if($("#s_name").val()==""||$("#s_name").val()==null){
				alert("资料名称不能为空");
				return;
			}
			 
			if($("#s_type").val()==""||$("#s_type").val()==null){
				alert("资料类型不能为空");
				return;
			}
			
			/*if($("#pushtype").val()==""||$("#pushtype").val()==null){
				alert("推送类型不能为空");
				return;
			}*/
			
			/*if(vm.materialDetail.pushType=='1'){     //每日

				if($("#everyday_pushtime").val()==""||$("#everyday_pushtime").val()==null){
					alert("推送日期不能为空");
					return;
				}
				vm.materialDetail.pushTime=$("#everyday_pushtime").val();
				
				if($("#everyday_pushtime").val().trim()!=''){
					vm.materialDetail.pushTime=getNowFormatDate()+" "+$("#everyday_pushtime").val();
					
				}

			}else{
				if($("#once_pushtime").val()==""||$("#once_pushtime").val()==null){
					alert("推送日期不能为空");
					return;
				}
				vm.materialDetail.pushTime=$("#once_pushtime").val();
			}*/
			
		    if(vm.title == "新增")
		    {
		       url = "../materialdetail/save";
		    }
		    else if(vm.title == "编辑")
		    {
		       url = "../materialdetail/update";
		    }else
		    {
		       url = "";
		    }
		    
		    vm.materialDetail.s_type=vm.materialDetail.type;
		    vm.materialDetail.type=null;
		    
		    vm.materialDetail.s_property=vm.materialDetail.property;
		    vm.materialDetail.property=null;
		    console.log(vm.title == "新增");
		    console.log($("#select_type").val());
		    if(vm.title == "新增"){
		    	vm.materialDetail.s_type=$("#select_type").val();
			}
		    
		    vm.materialDetail.questionBankCourse = vm.questionBankCourse;
			vm.materialDetail.questionBankChapter = vm.questionBankChapter;
			vm.materialDetail.questionBankVerse = vm.questionBankVerse;
		    
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.materialDetail),
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
				    url: "../materialdetail/delete",
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
			    url: "../materialdetail/info/" + id,
			    success: function(r){
			    	if(r.code === 0){
						 vm.materialDetail = r.materialDetail;
						 /*if(vm.materialDetail.pushType=='1'){
							 $("#everyday_pushtime").val(vm.materialDetail.pushTime);
							}else{
							 $("#once_pushtime").val(vm.materialDetail.pushTime);
							}*/
						 //
						 //
						 vm.questionBankCourse = vm.materialDetail.questionBankCourse;
						 vm.questionBankChapter = vm.materialDetail.questionBankChapter;
						 vm.questionBankVerse = vm.materialDetail.questionBankVerse;
						 vm.questionCourseInit();
						 
					}else{
						alert(r.msg);
					}
				}
			});
		},
		reload: function (event) {
			vm.showList = true;
			var page = $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"name" : vm.q.name,"s_property":vm.q.property,"s_type":vm.q.type,"isRelevance":vm.q.isRelevance,"timeOrdering":$("#s_time").val()},
                page:page
            }).trigger("reloadGrid");
		},
		
		 
		ishow : function(id){//弹出新增或者修改窗口
			
			$.ajax({
					type: "POST",
				    url: "../materialdetail/info/" + id,
				    success: function(r){
				    	if(r.code === 0){
				    		debugger;
				    		vm.showMaterialObj  = r.materialDetail;
							 if(vm.showMaterialObj.pushType=='1'){
								 $("#everyday_pushtime").val(vm.showMaterialObj.pushTime);
								}else{
								 $("#once_pushtime").val(vm.showMaterialObj.pushTime);
								}
							 var xc=document.getElementById("isRelId")
							 if(vm.showMaterialObj.isRelevance==null){
								 xc.style.display="none";
							 }
							 if(vm.showMaterialObj.isRelevance!=null){
								 xc.style.display="block";
							 }
							 
							 $("#objId").val(vm.showMaterialObj.id);
								$("#objName").val(vm.showMaterialObj.name);
								$("#objType").val(myFormatter(vm.showMaterialObj.type,vm.types));
								$("#objProperty").val(myFormatter(vm.showMaterialObj.property,vm.propertys)); 
								$("#objVedioAddr").val(vm.showMaterialObj.vedioAddr);
								$("#objReturnAddr").val(vm.showMaterialObj.returnAddr);
								$("#objPushContent").val(vm.showMaterialObj.pushContent);
								$("#chapterName").val(vm.showMaterialObj.chapterName);
								$("#courseName").val(vm.showMaterialObj.courseName);
								$("#verseName").val(vm.showMaterialObj.verseName);
								$("#objPushContent").val(vm.showMaterialObj.pushContent);
								$("#kpName").val(vm.showMaterialObj.kpName);
								if(vm.showMaterialObj.pushType=="0"){
									$("#vonce_pushtime").val(vm.showMaterialObj.pushTime);
									$("#veveryday_pushtime").val("");
//									$("#vonce_PushType").val("0");
									$(":radio[name='vpush_type'][value='" + vm.showMaterialObj.pushType + "']").prop("checked", "checked");
								}else if(vm.showMaterialObj.pushType=="1"){
									$("#vonce_pushtime").val("");
									$("#veveryday_pushtime").val(vm.showMaterialObj.pushTime);
//									$("#veveryday_PushType").val("1");
									$(":radio[name='vpush_type'][value='" + vm.showMaterialObj.pushType + "']").prop("checked", "checked");
								}else{
									$("#veveryday_pushtime").val("");
//									$("#veveryday_PushType").val("");
									$("#vonce_pushtime").val("");
//									$("#vonce_PushType").val("");
									$("input:radio[name='vpush_type']").eq(0).attr("checked",false);
									$("input:radio[name='vpush_type']").eq(1).attr("checked",false);
								}
								
								//
								$("#qbCourseName").val(vm.showMaterialObj.qbCourseName);
								$("#qbChapterName").val(vm.showMaterialObj.qbChapterName);
								$("#qbVerseName").val(vm.showMaterialObj.qbVerseName);
								
						}else{
							alert(r.msg);
						}
					}
				});
			
			layer.open({
				type: 1,
				skin: 'layui-layer-lan',
//				skin: 'layui-layer-molv',
				title: '资料查看',
				area: ['60%', '84%'],
				shadeClose: false,
				content: jQuery("#materialInfoShowLayer"),
				btn: ['返回'],
				btn1: function (index) {
					layer.close(index);
					
	            },
				 
			});
		},
		questionCourseInit : function() {
			
			$.ajax({
				type : 'POST',
				url : '../materialdetail/qbCourseInit',
				
				success : function(r) {
					if (r.code === 0) {
						var result = r.data;
						$("#qbCourseSelect").empty();
						for (var i = 0; i < result.length; i++) {
							var course = result[i];
							$("#qbCourseSelect").append(
									"<option value='" + course.code + "'>"
											+ course.name + "</option>");
						}
						// $("#chapterSelect").find("option[text='']").remove();
						if ($.trim(String(vm.questionBankCourse)) == '') {
							$("#qbCourseSelect option").eq(0).attr('selected',
									'true');
							//vm.questionBankCourse = $('#qbCourseSelect option:first').val();
						} else {
							$("#qbCourseSelect").find(
									"option[value='"
											+ vm.questionBankCourse + "']")
									.attr("selected", "true");
							
						}
						$("#qbCourseSelect").trigger("change");
					} else {
						alert(r.msg);
					}
				}
			});

		},
		courseSelected : function() {
			var datas = {
				"courseId" : vm.questionBankCourse
			};
			$.ajax({
				type : 'POST',
				url : '../materialdetail/qbCourseSelect',
				data : JSON.stringify(datas),
				success : function(r) {
					if (r.code === 0) {
						var result = r.data;
						$("#qbChapterSelect").empty();
						for (var i = 0; i < result.length; i++) {
							var chapter = result[i];
							$("#qbChapterSelect").append(
									"<option value='" + chapter.code + "'>"
											+ chapter.name + "</option>");
						}
						// $("#chapterSelect").find("option[text='']").remove();
						if ($.trim(String(vm.questionBankChapter)) == '') {
							$("#qbChapterSelect option").eq(0).attr('selected',
									'true');
						} else {
							$("#qbChapterSelect").find(
									"option[value='"
											+ vm.questionBankChapter + "']")
									.attr("selected", "true");
							
						}
						$("#qbChapterSelect").trigger("change");

					} else {
						alert(r.msg);
					}
				}
			});

		},
		chapterSelected : function() {
			var datas = {
				"courseId" : vm.questionBankCourse,
				"chapterId" : vm.questionBankChapter
			};
			$.ajax({
				type : 'POST',
				url : '../materialdetail/qbChapterSelect',
				data : JSON.stringify(datas),
				success : function(r) {
					if (r.code === 0) {
						var result = r.data;
						$("#qbVerseSelect").empty();
						if (null != result) {
							for (var i = 0; i < result.length; i++) {
								var verse = result[i];
								$("#qbVerseSelect").append(
										"<option value='" + verse.code + "'>"
												+ verse.name + "</option>");
							}
							//$("#verseSelect").find("option[text='']").remove();
							if ($.trim(String(vm.questionBankVerse)) == '') {
								$("#qbVerseSelect option").eq(0).attr('selected',
										'true');
							} else {
								$("#qbVerseSelect").find(
										"option[value='"
												+ vm.questionBankVerse
												+ "']")
										.attr("selected", "true");

							}
							$("#qbVerseSelect").trigger("change");
						}

					} else {
						alert(r.msg);
					}
				}
			});

		},
	}
});
/**
 * 获取当前时间 yyyy-MM-dd
 * @returns
 */
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + seperator1 + month + seperator1 + strDate
            //+ " " + date.getHours() + seperator2 + date.getMinutes()
            //+ seperator2 + date.getSeconds()
    ;
    return currentdate;
}