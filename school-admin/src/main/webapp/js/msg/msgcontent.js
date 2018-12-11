$(function () {
	$(".datetimepicker").datetimepicker({
 		format: 'yyyy-mm-dd hh:ii:00',
 		zIndex : 999999999,
 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
	});
	$("#jqGrid").jqGrid({
        url: '../msgcontent/list',
        datatype: "json",
        colModel: [		
        	{ label: '消息Id', name: 'contentId', hidden:true, key: true },
			{ label: '标题', name: 'contentTitle', width: 80 }, 						
			{ label: '封面', name: 'contentPic', width: 50 , align : "center" ,formatter : function(value, options, row){return '<img height="32px" width="32px"  src="'+value+'"/>';}}, 						
			/*{ label: '文章', name: 'contentHtml', width: 80 }, 	*/					
			{ label: '跳转地址', name: 'contentUrl', width: 80 }, 						
			
			{ label: '站内消息', name: 'commonSend', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">不推送</span>' : 
					'<span class="label label-success">推送</span>';
			}},
			{ label: '弹窗消息', name: 'topSend', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">不推送</span>' : 
					'<span class="label label-success">推送</span>';
			}},
			{ label: '推送消息', name: 'pushSend', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">不推送</span>' : 
					'<span class="label label-success">推送</span>';
			}},
			{ label: '通知对象', name: 'type', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-success">全员</span>' : 
					'<span class="label label-success">排课</span>';
			}},
			{ label: '排课', name: 'classplanName', width: 80 }, 						
			
			/*{ label: '班型', name: 'classtypeIds', width: 80 }, 	*/					
			{ label: '班型',name : 'classTypeNames',width : 100, formatter : classTypeNameFormatter},
			{ label: '发送方式', name: 'sendType', width: 80, formatter: function(value, options, row){
				var value = row.sendType;
				var text = '';
				if(value == 0)text= '<span class="label label-success">暂不发送</span>';
				else if(value == 1)text= '<span class="label label-danger">立即发送</span>';
				else if(value == 2)text= '<span class="label label-success">定时发送</span>';
				return text;
			}},
			{ label: '发送时间', name: 'timestamp', width: 80 }, 						
			{ label: '创建人', name: 'creationName', width: 80 }, 						
			{ label: '创建时间', name: 'createTime', width: 80 }, 						
			
			{ label: '修改人', name: 'modifiedName', width: 80 }, 						
			{ label: '修改时间', name: 'modifyTime', width: 80 }, 						
        ],
		viewrecords: true,
        height: 495,
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
    
    //班型权限格式化
	function classTypeNameFormatter(cellvalue, options, rowObject){
		if(null != rowObject && null != rowObject.classtypeIds && rowObject.classtypeIds.length > 0){
			 return vm.getClassTypeNames(rowObject.classtypeIds);
		}
		return "";
	};
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			contentTitle:""
		},
		showList: true,
		title: null,
		classTypeList : [],
		classTypeMap : {},
		msgContent: {
			contentPic:"",
			commonSend:1,
			topSend:0,
			pushSend:0,
			type:1,
			sendType:0,
			classplanId:"",
			classplanName:"",
			classtypeIdArray:[],
			classtypeIds:"",
			timestamp:"",
			contentHtml:""
		},
		
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.msgContent = {
					contentPic:"",
					commonSend:1,
					topSend:0,
					pushSend:0,
					type:1,
					sendType:0,
					classplanId:"",
					classplanName:"",
					classtypeIdArray:[],
					classtypeIds:"",
					timestamp:"",
					contentHtml:""
			};
			
			editor.html("");//新增时使文章内容为空
			
			$("#commitButton").attr("style", "display: block");
			$("#backButton").attr("style", "display: none");
			
			$("#sendTime").val("");//新增时让发送时间置空
		},
		update: function (event) {
			var contentId = getSelectedRow();
			if(contentId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(contentId);
		},
		saveOrUpdate: function (event) {
			//取得html内容
			var contentHtml = editor.html();
			//同步数据后直接取得textarea的value
			editor.sync();
			contentHtml = $("#editor_id").val();
			//设置html内容
			vm.msgContent.contentHtml = contentHtml;
			//做新增或保存时获取发送时间的value
			vm.msgContent.timestamp = $("#sendTime").val();
			//检验
			if($.isNull(vm.msgContent.contentTitle)){
				alert("请输入标题！！！");
				return;
			}
			if(vm.msgContent.contentTitle.length < 10 || vm.msgContent.contentTitle.length > 100){
				alert("标题少于10个字或者大于100个字！！！");
				return;
			}
			/*if($.isNull(vm.msgContent.contentPic)){
				alert("请选择封面！！！");
				return;
			}*/
			/*if($.isNull(vm.msgContent.contentHtml)){
				alert("请输入文章！！！");
				return;
			}*/
			/*if($.isNull(vm.msgContent.contentUrl)){
				alert("请输入跳转地址！！！");
				return;
			}*/
			if(vm.msgContent.type == 1){
				if($.isNull(vm.msgContent.classplanId)){
					alert("请选择排课！！！");
					return;
				}
			}
			/*if(vm.msgContent.type == 1){
				var classtypeIds = vm.msgContent.classtypeIds;
				console.log(classtypeIds);
				if($.isNull(classtypeIds)){
					alert("请选择班型！！！");
					return;
				}
			}*/
			if(vm.msgContent.sendType == 2){
				if($.isNull(vm.msgContent.timestamp)){
					alert("请输入发送时间！！！");
					return;
				}
			}
			
		    if(vm.title == "新增")
		    {
		       url = "../msgcontent/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../msgcontent/update";
		    }else
		    {
		       url = "";
		    }
		    //班型array->string
		    vm.msgContent.classtypeIds = vm.msgContent.classtypeIdArray.toString();
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.msgContent),
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
			var contentIds = getSelectedRows();
			if(contentIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../msgcontent/delete",
				    data: JSON.stringify(contentIds),
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
		getInfo: function(contentId){
            $.ajax({
				type: "POST",
			    url: "/msgcontent/info/" + contentId,
			    success: function(r){
			    	if(r.code === 0){
			    		 //查询信息时获取发送时间的值
			    		 $("#sendTime").val(vm.msgContent.timestamp);
			    		 vm.msgContent = r.data;
			    		 editor.html(vm.msgContent.contentHtml);//获取文章内容
			    		 //班型string->array
			    		 r.data.classtypeIdArray = r.data.classtypeIds.split(",");
						 if(1 == vm.msgContent.sendType || 2 == vm.msgContent.sendType){
				    			$("#commitButton").attr("style", "display: none");
				    			$("#backButton").attr("style", "display: block");
				         }else{
				        	 	$("#commitButton").attr("style", "display: block");
				    			$("#backButton").attr("style", "display: none");
				         }
					}else{
						alert(r.msg);
					}
			    	/*if(r.code === 0){
						 vm.msgContent = r.data;
					}else{
						alert(r.msg);
					}*/
				}
			});
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : {"contentTitle" : vm.q.contentTitle},
				page:page
            }).trigger("reloadGrid");
		},
		selectClassplan: function(){
			classplanLay.show(function(index,opt){
				console.log(opt);
				//排课ID
				vm.msgContent.classplanId = opt.classplanId;
				//排课名称
				vm.msgContent.classplanName = opt.classplanName;
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
		getClassTypeNames : function(ids){
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
	},
	created : function(){
		this.getclassTypeList();//班型权限初始化
	}
});