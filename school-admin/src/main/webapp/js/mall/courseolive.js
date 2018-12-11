$(function () {
	$(".datetimepicker").datetimepicker({
 		format: 'yyyy-mm-dd hh:ii:00',
 		zIndex : 999999999,
 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
    });
	$("#jqGrid").jqGrid({
        url: '../courseolive/list',
        datatype: "json",
        colModel: [		
			{ label: 'oliveId', name: 'oliveId', hidden: true, key: true },
            { label: 'pushMsgId', name: 'pushMsgId', hidden: true },
            { label: 'pushFindMsgId', name: 'pushFindMsgId', hidden: true },
			{ label: '标题', name: 'oliveTitle', width: 80 },
            { label: '卡片标题', name: 'cardTitle', width: 80 },
			{ label: '封面', name: 'olivePic', width: 50 , align : "center" ,formatter : function(value, options, row){
				return '<img height="32px" width="32px"  src="'+value+'"/>';}
			},
			{ label: '讲师', name: 'teacherName', width: 80 }, 						
			{ label: '开始时间', name: 'oliveStartTime', width: 80 },
			{ label: '结束时间', name: 'oliveEndTime', width: 80 }, 						
			{ label: '消息状态', name: 'appStatus', width: 80, formatter: function(value, options, row){
        		var text = '';
                if(value == 0) text='<span class="label label-default">未推荐</span>';
        		else if(value == 1) text='<span class="label label-success">推荐</span>';
        		return text;
			}},
            { label: '推送状态', name: 'pushStatus', width: 80, formatter: function(value, options, row){
                var text = '';
                if(value == 0)text='<span class="label label-default">未推送</span>';
                else if(value == 1) {
                	if(new Date().getTime() > new Date(row.pushTime).getTime()) {
                        text='<span class="label label-success">已推送</span>';
					} else {
                        text='<span class="label label-primary">预推送</span>';
					}
                }
                else if(value == 2)text='<span class="label label-danger">推送失败</span>';
                return text; }
            },
            { label: '推送时间', name: 'pushTime', width: 80 },
			{ label: '直播间', name: 'liveRoomName', width: 80 },
			{ label: '回放地址', name: 'replayUrl', width: 80 }, 
			{ label: '产品线', name: 'productName', width: 80 }, 
			{ label: '创建用户', name: 'creationName', width: 80 }, 						
			{ label: '创建时间', name: 'creationTime', width: 80 }, 						
			{ label: '最近修改用户', name: 'modifiedName', width: 80 }, 						
			{ label: '最近修改日期', name: 'modifiedTime', width: 80 },
            { label: '公开课类型', name: 'authorityName', width: 80 },
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
			oliveTitle:null
		},
		showList: true,
		title: null,
		courseOlive: {
			oliveTitle:"",
			oliveIntroduction:"",
            cardTitle:"",
            cardSubtitle:"",
			teacherId:"",
			teacherName:"",
			olivePic:"",
			liveRoomId:"",
			liveRoomName:"",
			replyUrl:"",
			oliveStartTime:"",
			oliveEndTime:"",
			productId:"",
			productName:"",
            teacherName:"",
			teacherAvatar:"",
			teacherIntroduction:"",
            content:"",
			suitable:"",
            authorityId:"",
            authorityName:""
		}
	},
	methods: {
		selectTeacher: function(){
			teacherLay.show(function(index,opt){
//				console.log(opt);
				//授课老师id
				vm.courseOlive.teacherId = opt.userId;
				//授课老师名称
				vm.courseOlive.teacherName = opt.nickName;
			});
		},
		selectLiveroom: function(){
			liveroomLay.show(function(index,opt){
				console.log(opt);
				//直播间id
				vm.courseOlive.liveRoomId = opt.liveRoomId;
				//直播间名称
				vm.courseOlive.liveRoomName = opt.liveRoomName;
			});
		},
		/*selectVideo: function(){
			polyvVideoLay.show(function(index,opt){
				console.log(opt);
			});
		},*/
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseOlive = {
					oliveTitle:"",
					oliveIntroduction:"",
					cardTitle:"",
					cardSubtitle:"",
					teacherId:"",
					teacherName:"",
					olivePic:"",
					liveRoomId:"",
					liveRoomName:"",
					replayUrl:"",
					oliveStartTime:"",
					oliveEndTime:"",
					productId:"",
					productName:"",
					teacherName:"",
					teacherAvatar:"",
					teacherIntroduction:"",
                	content:"",
					suitable:"",
                	authorityId:"",
                	authorityName:""
			};
			$("#detailStartTime").val("");//新增时让开始时间置空
			$("#detailEndTime").val("");//新增时让结束时间置空
		},
		update: function (event) {
			var oliveId = getSelectedRow();//获取选中一条记录；
			if(oliveId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(oliveId)
		},
		saveOrUpdate: function (event) {
			//做新增或保存时获取开始时间的值
			vm.courseOlive.oliveStartTime = $("#detailStartTime").val();
			//做新增或保存时获取结束时间的值
			vm.courseOlive.oliveEndTime = $("#detailEndTime").val();
			if($.isNull(vm.courseOlive.oliveTitle)){
		    	alert("请输入标题！！！");
		    	return;
		    }
            if($.isNull(vm.courseOlive.oliveIntroduction)){
                alert("请输入公开课简介！！！");
                return;
            }
            if($.isNull(vm.courseOlive.cardTitle)){
                alert("请输入卡片标题！！！");
                return;
            }
            if($.isNull(vm.courseOlive.cardSubtitle)){
                alert("请输入卡片副标题！！！");
                return;
            }
			if($.isNull(vm.courseOlive.productId)){
				alert("请选择产品线！！！");
				return;
			}
		    if($.isNull(vm.courseOlive.teacherName)){
		    	alert("请输入讲师姓名！！！");
		    	return;
		    }
            if($.isNull(vm.courseOlive.teacherAvatar)){
                alert("请输入讲师头像！！！");
                return;
            }
            if($.isNull(vm.courseOlive.teacherIntroduction)){
                alert("请输入讲师简介！！！");
                return;
            }
            if($.isNull(vm.courseOlive.suitable)){
                alert("请输入适用对象！！！");
                return;
            }
		    if($.isNull(vm.courseOlive.oliveStartTime)){
		    	alert("请输入开始时间！！！");
		    	return;
		    }
		    if($.isNull(vm.courseOlive.oliveEndTime)){
		    	alert("请输入结束时间！！！");
		    	return;
		    }
		    if($.isNull(vm.courseOlive.olivePic)){
		    	alert("请输入封面！！！");
		    	return;
		    }
		    if($.isNull(vm.courseOlive.liveRoomId)){
		    	alert("请输入直播间！！！");
		    	return;
		    }
            if($.isNull(vm.courseOlive.content)){
                alert("请输入课程内容！！！");
                return;
            }
		    if(!$.isNull(vm.courseOlive.oliveTitle) && vm.courseOlive.oliveTitle.length > 100){
		    	alert("标题信息不得超过100个字！！！")
		    	return;
		    }
            if($.isNull(vm.courseOlive.authorityId)){
                alert("请选择直播类型!")
                return;
            }
			if(vm.title == "新增")
		    {
		       url = "../courseolive/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../courseolive/update";
		    }else
		    {
		       url = "";
		    }
		    
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseOlive),
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
			var oliveIds = getSelectedRows();
			if(oliveIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseolive/delete",
				    data: JSON.stringify(oliveIds),
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
		getInfo: function(oliveId){
			//查询信息时获取并显示开始时间的值
            $("#detailStartTime").val(vm.courseOlive.oliveStartTime);
        	//查询信息时获取并显示结束时间的值
        	$("#detailEndTime").val(vm.courseOlive.oliveEndTime);
            $.ajax({
				type: "POST",
			    url: "/courseolive/info/" + oliveId,
			    success: function(r){
			    	if(r.code === 0){
						 vm.courseOlive = r.data;
						 //查询信息时获取并显示开始时间的值
				         $("#detailStartTime").val(vm.courseOlive.oliveStartTime);
				         //查询信息时获取并显示结束时间的值
				         $("#detailEndTime").val(vm.courseOlive.oliveEndTime);
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
                postData:{"oliveTitle" : vm.q.oliveTitle},
				page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var oliveIds = getSelectedRows();
			if(oliveIds == null){
				return ;
			}
			
			confirm('确定要上架选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseolive/resume",
				    data: JSON.stringify(oliveIds),
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
		    var oliveIds = getSelectedRows();
			if(oliveIds == null){
				return ;
			}
			
			confirm('确定要下架选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseolive/pause",
				    data: JSON.stringify(oliveIds),
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
		selectProduct : function(){
			productLay.show(function(index,opt){
				vm.courseOlive.productId = opt.productId;
				vm.courseOlive.productName = opt.productName;
			});
		},
        pushRulesLayerShow : function(){//推送规则浮层
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            //行详情
            var row = $("#jqGrid").jqGrid('getRowData',id);
			/*消息类型 3：公开课 4：自定义卡片 9：系统消息*/
            pushRulesLay.show(3, id, row.pushTime, function(index,opt){
            });
        },
        delMessage: function () {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
			/*消息类型 3：公开课 4：自定义卡片 9：系统消息*/
            //行详情
            var row = $("#jqGrid").jqGrid('getRowData',id);
            confirm('确定要取消推送选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: "../pushrules/delMessage",
                    data: JSON.stringify({
                        "messageType":3,
                        "messageId":id,
						"pushMsgId":row.pushMsgId,
						"pushFindMsgId":row.pushFindMsgId
                    }),
                    success: function(r){
                        if(r.code == 0){
                            alert(r.msg, function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        updateMsgRecommend: function (msgRecommend) {
            var id = getSelectedRow();
            if (id == null) {
                return;
            }
            //行详情
            var row = $("#jqGrid").jqGrid('getRowData',id);
            var text = "确定要推荐选中的记录？"
            if (msgRecommend == 0){
                text = "确定要取消推荐选中的记录?"
            }
            confirm(text, function(){
                $.ajax({
                    type: "POST",
                    url: "../pushrules/updateMsgRecommend",
                    data: JSON.stringify({
                        "messageType":11,
                        "messageId":id,
                        "pushFindMsgId":row.pushFindMsgId,
						"msgRecommend":msgRecommend
                    }),
                    success: function(r){
                        if(r.code == 0){
                            alert(r.msg, function(index){
                                $("#jqGrid").trigger("reloadGrid");
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        //公开课权限
        courseOliveAuthorityShow : function(){
            courseOliveAuthorityLay.show(function(index, opt) {
                vm.courseOlive.authorityId = opt.authorityId;
                vm.courseOlive.authorityName = opt.authorityName;
            });
        }
	}
});