$(function () {
    $("#jqGrid").jqGrid({
        url: '../mall/liveroom/list',
        datatype: "json",
        colModel: [			
			{ label: '直播间Id', name: 'liveRoomId', width: 50, key: true },
			{ label: '直播间名称', name: 'liveRoomName', width: 80 }, 
			{ label: '产品线', name: 'productName', width: 80 }, 
			/*{ label: '直播间频道号', name: 'liveRoomChannelId', width: 80 },
			{ label: '直播间频道密钥', name: 'liveRoomChannelSecretkey', width: 80 },
			{ label: '直播间频道密码', name: 'liveRoomChannelPassword', width: 80 }, */
			{ label: '展示互动直播id', name: 'genseeLiveId', width: 80 },
			{ label: '展示互动直播房间号', name: 'genseeLiveNum', width: 80 }, 
			{ label: '直播间描述', name: 'liveRoomRemake', width: 80 }, 
			{ label: '是否禁用', name: 'status', width: 80, formatter: function(value, options, row){
				return value === 0 ? 
					'<span class="label label-danger">禁用</span>' : 
					'<span class="label label-success">正常</span>';
			}},
			{ label: '创建人', name: 'creationName', width: 80 }, 			
			{ label: '创建时间', name: 'creationTime', width: 80 }, 			
			{ label: '修改人', name: 'modifiedName', width: 80 }, 			
			{ label: '修改时间', name: 'modifiedTime', width: 80 }		
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
        }
    });
});

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			liveRoomName: "",
			productName: ""
		},
		showList: true,
		title: null,
		mallLiveRoom: {
			productId:null,
			productName:null
		}
	},
	methods: {
		query: function () {
			vm.reload(null , 1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.mallLiveRoom = {
					status : 1,
					productId:null,
					productName:null
			};
		},
		update: function (event) {
			var liveRoomId = getSelectedRow();
			if(liveRoomId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "修改";
            
            vm.getInfo(liveRoomId)
		},
		saveOrUpdate: function (event) {
			if($.isNull(vm.mallLiveRoom.liveRoomName)){
		    	alert("请输入直播间名字！！！");
		    	return;
		    }
			if($.isNull(vm.mallLiveRoom.productId)){
				alert("请选择产品线！！！");
				return;
			}
            var reg=new RegExp("^[A-Za-z0-9]+$");
			if($.isNull(vm.mallLiveRoom.genseeLiveId)){
		    	alert("请输入展示互动直播Id！！！");
		    	return;
		    }else if(!reg.test(vm.mallLiveRoom.genseeLiveId)){
                alert("展示互动直播Id格式不正确，只能输入数字和字母！！！");
                return;
			}
			if($.isNull(vm.mallLiveRoom.genseeLiveNum)){
		    	alert("请输入展示互动直播房间号！！！");
		    	return;
		    }else if(!reg.test(vm.mallLiveRoom.genseeLiveNum)){
                alert("展示互动直播房间号格式不正确，只能输入数字和字母！！！");
                return;
			}
			if(!$.isNull(vm.mallLiveRoom.liveRoomRemake) && vm.mallLiveRoom.liveRoomRemake.length > 50){
				alert("直播间描述不得超过50个字！！！");
		    	return;
			}
			if(vm.title == "新增")
		    {
		       url = "../mall/liveroom/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../mall/liveroom/update";
		    }else
		    {
		       url = "";
		    }
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.mallLiveRoom),
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
			var liveRoomIds = getSelectedRows();
			if(liveRoomIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/liveroom/delete",
				    data: JSON.stringify(liveRoomIds),
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
		getInfo: function(liveRoomId){
			$.get("../mall/liveroom/info/"+liveRoomId, function(r){
                vm.mallLiveRoom = r.mallLiveRoom;
            });
		},
		reload: function (event , p) {
			vm.showList = true;
			var page = p||$("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
                page:page
            }).trigger("reloadGrid");
		},
		resume:function(event){
		   var liveRoomIds = getSelectedRows();
			if(liveRoomIds == null){
				return ;
			}
			
			confirm('确定要启用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/liveroom/resume",
				    data: JSON.stringify(liveRoomIds),
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
		    var liveRoomIds = getSelectedRows();
			if(liveRoomIds == null){
				return ;
			}
			
			confirm('确定要禁用选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../mall/liveroom/pause",
				    data: JSON.stringify(liveRoomIds),
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
				vm.mallLiveRoom.productId = opt.productId;
				vm.mallLiveRoom.productName = opt.productName;
			});
		},
	}
});