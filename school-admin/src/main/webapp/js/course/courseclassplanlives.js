$(function () {
	$(".datetimepicker").datetimepicker({
 		format: 'yyyy-mm-dd hh:ii:00',
 		zIndex : 999999999,
 		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
 		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
	});
	$(".datetimepicker_start").datetimepicker({
		format: 'yyyy-mm-dd 00:00:00',
        weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
        todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
        autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
        minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
	});
	$(".datetimepicker_end").datetimepicker({
		format: 'yyyy-mm-dd 23:59:59',
		weekStart: 0,//一周从哪一天开始。0（星期日）到6（星期六）
		todayBtn:  1,//如果是true的话，"Today" 按钮仅仅将视图转到当天的日期，如果是"linked"，当天日期将会被选中。
		autoclose: 1,//当选择一个日期之后是否立即关闭此日期时间选择器。
		minView: 2//日期时间选择器所能够提供的最精确的时间选择视图。0:小时，1:分钟，2:秒
	});
	$(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
	$(".datetimepicker_end").val(new Date().format("yyyy-MM-dd 23:59:59"));
	$("#jqGrid").jqGrid({
        url: '../courseclassplanlives/list',
        datatype: "local",
//        datatype: "json",
        colModel: [		
	        	{ label: '主键PK', name: 'classplanLiveId', hidden : true, key: true },
				{ label: '排课计划PK', name: 'classplanId', hidden : true }, 	
				{ label: '课程ID', name: 'courseId', hidden : true, width: 80 }, 
				{ label: '直播课ID', name: 'courseLiveDetailId', hidden : true },
				{ label: '直播室ID', name: 'studioId', hidden : true },
				{ label: '授课老师ID', name: 'teacherId', hidden : true },
				{ label: '班型权限ids', name: 'liveClassTypeIds', hidden : true},
				{ label: '回放ID', name: 'backId', hidden : true, width: 80 },
				
				{ label: '直播课次名称', name: 'classplanLiveName', width: 150 },
				{ label: '排课计划名称', name: 'classplanName', width: 150 },
				{ label: '课程名称', name: 'courseName', width: 150 },
				/*{ label: '直播时间说明', name: 'classplanLiveTimeDetail', width: 150 },*/ 
				{ label: '即将开始时间', name: 'readyTime', width: 150 },	
				{ label: '开始时间', name: 'startTime', width: 150 },	
				
				{ label: '结束时间', name: 'endTime', width: 150 },
				{ label: '进入直播间结束时间', name: 'closeTime', width: 150 },
				{ label: '授课老师', name: 'teacherName', width: 80 },
				// { label: '上课时段', name: 'timeBucket', width: 80 ,formatter: function(value, options, row){
				// 	var text = '';
				// 	if(value == 0)text='<span class="label label-danger">上午</span>';
				// 	else if(value == 1)text='<span class="label label-danger">下午</span>';
				// 	else if(value == 2)text='<span class="label label-danger">晚上</span>';
				// 	return text;
				// }},
           		 { label: '考勤', name: 'attendance', width: 80 ,formatter: function(value, options1, row){
                    var text = '';
                    if(value == -1)text='<span class="label label-danger">否</span>';
                    else text='<span class="label label-danger">是 </span>';
                    return text;
                }},
				{ label: '直播室', name: 'studioName', width: 80 },
				{ label: '直播间', name: 'liveRoomName', width: 80 },
				/*{ label: '直播间频道号', name: 'liveRoomChannelId', width: 80 },
				{ label: '直播间频道密码', name: 'liveRoomChannelPassword', width: 80 },*/
				
				{ label: '直播间ID', name: 'liveroomId', hidden : true},
				// { label: '展示互动直播ID', name: 'genseeLiveId', width: 80 },
				// { label: '展示互动直播房间号', name: 'genseeLiveNum', width: 80 },
				{ label: '回放地址', name: 'backUrl', width: 80 },
				{ label: '回放类型', name: 'backType', hidden : true, width: 80 },
				
				{label : '班型权限',name : 'liveClassTypeNames',hidden : true, formatter : classTypeNameFormatter},
				{ label: '文件上传', name: 'fileUrl', hidden : true },
				// { label: '学来学往', name: '', width: 80,formatter: function (value, options, row) {
				// 	var button = '<a class="btn btn-primary" onclick="goXuelxuew(\''+row.classplanLiveId+'\',\''+row.classplanLiveName+'\')"><i class="fa fa-play"></i>调起直播间</a>';
				// 	return button;
				// }},
				{ label: '跳转直播间', name: '', width: 80,formatter: function (value, options, row) {
					var button = '<a class="btn btn-primary" onclick="goLiveRoom(\''+row.classplanLiveId+'\')"><i class="fa fa-play"></i>进入直播间</a>';
					return button;
				}},
				{ label: '回放', name: '', width: 80,formatter: function (value, options, row) {
					var button = '<a class="btn btn-primary" onclick="rePlay(\''+row.classplanLiveId+'\')"><i class="fa fa-play"></i>播放</a>';
					return button;
				}},
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
		if(null != rowObject && null != rowObject.liveClassTypeIds && rowObject.liveClassTypeIds.length > 0){
			 return vm.getLiveClassTypeNames(rowObject.liveClassTypeIds);
		}
		return "";
	};
});

/**
 * 进入学来学往
 * @param parm1
 * @returns
 */
function goXuelxuew(parm1,parm2){
	var data = {"classplanLiveId":parm1,"classplanLiveName":parm2}
	$.ajax({
		type: "POST",
	    url: "../live/getPOLYVUrl",
	    data: data,
	    contentType: "application/x-www-form-urlencoded",
	    success: function(r){
	    	if(r.code === 0){
	    		window.open(r.data , "live_window");
			}else{
				alert(r.msg);
			}
		}
	});
};

/**
 * 进入直播间
 * @param parm1
 * @returns
 */
function goLiveRoom(parm1){
	var data = {"classplanLiveId":parm1}
	$.ajax({
		type: "POST",
	    url: "../live/getLiveUrl",
	    data: data,
	    contentType: "application/x-www-form-urlencoded",
	    success: function(r){
	    	if(r.code === 0){
	    		window.open(r.data , "live_window");
			}else{
				alert(r.msg);
			}
		}
	});
};
/**
 * 回放
 * @param classplanLiveId
 * @returns
 */
function rePlay(classplanLiveId){
	var data = {"classplanLiveId":classplanLiveId}
	$.ajax({
		type: "POST",
	    url: "../live/getReplayUrl",
	    data: data,
	    contentType: "application/x-www-form-urlencoded",
	    success: function(r){
	    	if(r.code === 0){
	    		window.open(r.data , "live_window");
			}else{
				alert(r.msg);
			}
		}
	});
};

var vm = new Vue({
	el:'#rrapp',
	data:{
		q:{
			startTime:"",
			endTime:"",
			classplanLiveName:"",
			classplanName:"",
			timeBucket:"",
			attendance:""
		},
		showList: true,
		title: null,
		classTypeList : [],
		classTypeMap : {},
		courseClassplanLives: {
			classtypeIdArray:[],
			liveClassTypeIds:"",
			classplanLiveName:"",
//			classplanLiveTimeDetail:'',
			timeBucket:"",
			attendance:"",
			studioId:"",
			studioName:"",
			liveroomId:"",
			liveRoomName:"",
			backUrl:"",
			teacherId:"",
			teacherName:"",
			startTime:"",
			endTime:"",
			fileUrl:"",
			readyTime:"",
			closeTime:""
		},
		options: [  
		      { text: '上午', value: '0' },  
		      { text: '下午', value: '1' },  
		      { text: '晚上', value: '2' }  
		    ],
		options1:[
			{text:'是',value:'0'},
			{text:'否',value:'-1'}
		]
	},
	methods: {
		query: function () {
			vm.reload(null,1);
		},
		add: function(){
			vm.showList = false;
			vm.title = "新增";
			vm.courseClassplanLives = {};
		},
		update: function (event) {
			var classplanLiveId = getSelectedRow();
			console.log(classplanLiveId);
			if(classplanLiveId == null){
				return ;
			}
			vm.showList = false;
            vm.title = "查看";
            
            vm.getInfo(classplanLiveId)

		},
		saveOrUpdate: function (event) {
			
			vm.courseClassplanLives.readyTime = $("#readyTime").val();
			vm.courseClassplanLives.closeTime = $("#closeTime").val();
			vm.courseClassplanLives.startTime = $("#startTime").val();
			vm.courseClassplanLives.endTime = $("#endTime").val();
			
			//非空校验
			if($.isNull(vm.courseClassplanLives.classplanLiveName)){
				alert("直播课名称不能为空！！！");
				return;
			}
			
			if(vm.title == "新增")
		    {
		       url = "../courseclassplanlives/save";
		    }
		    else if(vm.title == "修改")
		    {
		       url = "../courseclassplanlives/update";
		    }else
		    {
		       url = "";
		    }
		    //班型array->string
		    vm.courseClassplanLives.liveClassTypeIds = vm.courseClassplanLives.classtypeIdArray.toString();
            console.log(vm.courseClassplanLives);
			$.ajax({
				type: "POST",
			    url: url,
			    data: JSON.stringify(vm.courseClassplanLives),
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
			var classplanLiveIds = getSelectedRows();
			if(classplanLiveIds == null){
				return ;
			}
			
			confirm('确定要删除选中的记录？', function(){
				$.ajax({
					type: "POST",
				    url: "../courseclassplanlives/delete",
				    data: JSON.stringify(classplanLiveIds),
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
		//跳转直播间
		jumpLiveRoom: function(){
			/*$.ajax({
				type: "POST",
			    url: "../live/getLiveUrl",
			    data: JSON.stringify(classplanLiveIds),
			    success: function(r){
					if(r.code == 0){
						alert('操作成功', function(index){
							$("#jqGrid").trigger("reloadGrid");
						});
					}else{
						alert(r.msg);
					}
				}
			});*/
		},
		getInfo: function(classplanLiveId){
            $.get("../courseclassplanlives/info/"+classplanLiveId, function(r){
            	vm.courseClassplanLives = r.data;
            	//班型string->array
	    		r.data.classtypeIdArray = r.data.liveClassTypeIds.split(",");
	    		//查询信息时获取并显示直播开始时间和直播结束时间
	    		$("#readyTime").val(vm.courseClassplanLives.readyTime);
	    		$("#closeTime").val(vm.courseClassplanLives.closeTime);
                $("#startTime").val(vm.courseClassplanLives.startTime);
                $("#endTime").val(vm.courseClassplanLives.endTime);

				console.log(vm.courseClassplanLives);

                $("#close_1").attr("disabled",true);
                $("#close_2").attr("disabled",true);
                $("#close_3").attr("disabled",true);
                $("#close_4").attr("disabled",true);
                $("#readyTime").attr("disabled",true);
                $("#closeTime").attr("disabled",true);
                $("#startTime").attr("disabled",true);
                $("#endTime").attr("disabled",true);
                $("#close_9").attr("disabled",true);
                $("#close_10").attr("disabled",true);
                $("#close_11").attr("disabled",true);
                $("#close_12").attr("disabled",true);
                $("#genseeLiveId").attr("disabled",true);
                $("#genseeLiveNum").attr("disabled",true);

            });
		},
		reload: function (e , p) {
			vm.showList = true;
			vm.q.startTime = $(".datetimepicker_start").val();
			vm.q.endTime = $(".datetimepicker_end").val();
			var page = p || $("#jqGrid").jqGrid('getGridParam','page');
			$("#jqGrid").jqGrid('setGridParam',{ 
				postData : vm.q,
				page:page,
                datatype: "json"
            }).trigger("reloadGrid");
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
		//选择授课老师
		selectTeacher: function(){
			teacherLay.show(function(index,opt){
				vm.courseClassplanLives.teacherId = opt.userId;
				vm.courseClassplanLives.teacherName = opt.nickName;
			});
		},
		//选择直播间
		selectLiveroom : function(){
			liveroomLay.show(function(index,opt){
				vm.courseClassplanLives.liveRoomId = opt.liveRoomId;
				vm.courseClassplanLives.liveRoomName = opt.liveRoomName;
			});
		},
		//选择直播室
		selectStudio : function(){
			studioLay.show(function(index,opt){
				vm.courseClassplanLives.studioId = opt.studioId;
				vm.courseClassplanLives.studioName = opt.studioName;
			});
		},
		clearQuery : function(){//清空查询条件
			var vm = this;
			//vm.q.startTime = "";
			vm.q.classplanLiveName = "";
			vm.q.classplanName = "";
//			$(".datetimepicker_start").datetimepicker('setDate', (new Date()) );
			$(".datetimepicker_start").val(new Date().format("yyyy-MM-dd 00:00:00"));
//			$(".datetimepicker_end").datetimepicker('setDate', (new Date()) );
			$(".datetimepicker_end").val(new Date().format("yyyy-MM-dd 23:59:59"));
			vm.q.timeBucket = "";
			vm.q.attendance="";
		},
	},
	created : function(){
		this.getclassTypeList();//班型权限初始化
//		$(".datetimepicker_start").datetimepicker('setDate', (new Date()) );
//		$(".datetimepicker_end").datetimepicker('setDate', (new Date()) );
	}
});