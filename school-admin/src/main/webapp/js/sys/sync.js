$(function () {
	
});

var vm = new Vue({
	el:'#rrapp',
	data:{
//		title: null,
	},
	methods: {
		syncCourse: function(){
			$.ajax({
				type: "POST",
			    url: "../sys/sync/course",
//			    data: JSON.stringify(vm.courseExamSchedule),
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

		syncCommodity: function(){
			$.ajax({
				type: "POST",
			    url: "../sys/sync/commodity",
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

		syncTeacher: function(){
			$.ajax({
				type: "POST",
				url: "../sys/sync/teacher",
//			    data: JSON.stringify(vm.courseExamSchedule),
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
		syncliveRoom: function(){
			$.ajax({
				type: "POST",
				url: "../sys/sync/liveRoom",
//			    data: JSON.stringify(vm.courseExamSchedule)
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
		synccourseClassPlan: function(){
			$.ajax({
				type: "POST",
				url: "../sys/sync/courseClassPlan",
//			    data: JSON.stringify(vm.courseExamSchedule), 
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
		saveUserplanBatch: function(){//批量生成学员规划-订单为生成学员规划的
			$.ajax({
				type: "POST",
				url: "../sys/sync/saveUserplanBatch",
//			    data: JSON.stringify(vm.courseExamSchedule), 
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
		syncCourseUserPlanClass: function(){//批量生成学员规划-订单为生成学员规划的
			$.ajax({
				type: "POST",
				url: "../sys/sync/courseUserPlanClass",
//			    data: JSON.stringify(vm.courseExamSchedule), 
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
		syncUsers: function(){//学员
			$.ajax({
				type: "POST",
				url: "../sys/sync/users",
//			    data: JSON.stringify(vm.courseExamSchedule), 
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
		updateCourseClassPlan: function(){//更新排课信息
			$.ajax({
				type: "POST",
				url: "../sys/sync/updateCourseClassPlan",
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
		syncLiveLog: function(){//同步直播
			$.ajax({
				type: "POST",
				url: "../sys/sync/syncLiveLog",
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
		syncVideoLog: function(){//同步录播
			$.ajax({
				type: "POST",
				url: "../sys/sync/syncVideoLog",
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
		UpdatecourseClassplanLives: function(){//更新排课明细回放
			$.ajax({
				type: "POST",
				url: "../sys/sync/UpdatecourseClassplanLives",
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
		syncKSLogMongodb: function(){//同步考勤数据
			$.ajax({
				type: "POST",
				url: "../sys/sync/syncKSLogMongodb",
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
		syncAgent: function(){//同步客服
			$.ajax({
				type: "POST",
				url: "../sys/sync/syncAgent",
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
		syncCustomers: function(){//同步客户
			$.ajax({
				type: "POST",
				url: "../sys/sync/syncCustomers",
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
		}
	}
});