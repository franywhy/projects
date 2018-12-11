!function(){
	function pageScript(){
		var self = this;
			self.classType = '';
			self.userPlanId = '';
			self.course = '';
			self.courseId = '';
			self.provinceId = '';
			self.timeId = '';
			self.cause = '';
			self.businessId = pageParam.businessId;
			self.attendanceId = null;
			
		this.init = function(){
			self.bindEvent();
			return self;
		};
		
		//绑定事件
		this.bindEvent = function(){
			$("#applyBtn").click(function(){
				$("#attFormLayer .mask-layer").show(300);
			});
			
			$("#attFormLayer .grey-close-p").click(function(){
				$("#attFormLayer .mask-layer").hide(300);
			});
			
			$("#classType").click(function(){
				self.sliderAction($(this));
			});
			
			$("#course").click(function(){
				var selectListVal = $.trim($("#classType input").val());
				if(selectListVal == ''){
					$("#promptText .text-se").text('请先选择班型');
					$("#promptText").css('display','inline-block');
					return false;
				}else{
					self.sliderAction($(this));
				}
			});
			
			$("#province").click(function(){
				self.sliderAction($(this));
			});
			
			$("#time").click(function(){
				self.sliderAction($(this));
			});
			
			$("#selectList > .select-list").click(function(){
				var txt = $(this).text();
				$(this).parent().parent().find('input').val(txt);
				self.userPlanId = $(this).attr("data-id");
				$(this).parent().slideUp();
				$(this).parent().parent().find('input').css('background','#fff');
				self.getCourseList();
			});
			
			$("#selectList3 > .select-list").click(function(){
				var txt = $(this).text();
				$(this).parent().parent().find('input').val(txt);
				self.provinceId = $(this).attr("data-id");
				$(this).parent().slideUp();
				$(this).parent().parent().find('input').css('background','#fff');
			});
			
			$("#selectList4 > .select-list").click(function(){
				var txt = $(this).text();
				$(this).parent().parent().find('input').val(txt);
				self.timeId = $(this).attr("data-id");
				$(this).parent().slideUp();
				$(this).parent().parent().find('input').css('background','#fff');
			});
			
			$("#cancelBtn,#cancelConfirmLayer .grey-close-p").click(function(){
				$("#cancelConfirmLayer .mask-layer").hide(300);
			});
			
			$("#confirmBtn").click(function(){
				self.cancelRequset();
			});
			
			$("#form").submit(function(){
				if(!self.checkForm()){
					return false;
				}
				self.submitForm();
				return false;
			});
		};
		
		/**
		 * 报考课程请求
		 * @param self.userPlanId 班型ID
		 */
		this.getCourseList = function(){
			hqTool.showLoad(self.businessId);
			$.ajax({
				url:"/learningCenter/web/courseAbnormalAbandonExam/courseListByOrderId",  
	            type:"GET",  
				async:true,
				data:{
					"orderId" : self.userPlanId
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	if(data.code == 200){
	            		$("#course input").val('');
	            		$("#selectList2").empty();
	            		if(data.data != null){
		            		for(var i=0; i<data.data.length; i++){
		            			$("#selectList2").append('<div class="select-list" data-id="'+data.data[i].courseId+'" onclick="pageScript.selectList($(this))">'+data.data[i].courseName+'</div>');
		            		}
	            		}
	            		return false;
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
            });
		};
		
		//表单校验
		this.checkForm = function(){
			self.classType = $.trim($("#classType input").val());
			self.course = $.trim($("#course input").val());;
			self.province = $.trim($("#province input").val());
			self.time = $.trim($("#time input").val());
			self.cause = $.trim($("#cause textarea").val());
			if(self.classType == ''){
				$("#promptText .text-se").text('请输入完整的弃考申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}else if(self.course == ''){
				$("#promptText .text-se").text('请输入完整的弃考申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}else if(self.province == ''){
				$("#promptText .text-se").text('请输入完整的弃考申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}else if(self.time == ''){
				$("#promptText .text-se").text('请输入完整的弃考申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}else if(self.cause == ''){
				$("#promptText .text-se").text('请输入完整的弃考申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}
			return true;
		};
		
		/**
		 * 报考申请
		 * 提交表单请求
		 * @param self.userPlanId 班型ID
		 * @param self.courseId 课程ID
		 * @param self.province 省份
		 * @param self.time 报考时间
		 * @param self.cause 原因
		 */
		this.submitForm = function(){
			hqTool.showLoad(self.businessId);
			$.ajax({
				url:"/learningCenter/web/courseAbnormalAbandonExam/save",  
	            type:"GET",
				async:true,
				data:{
					"orderId" : self.userPlanId,
					"courseId" : self.courseId,
					"bkAreaId" : self.provinceId,
					"scheduleId" : self.timeId,
					"abnormalReason" : self.cause
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	$("#promptText").hide();
	            	if(data.code == 200){
	            		window.location.reload();
	            		return false;
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
            });
		};
		
		//取消事件显示层
		this.cancel = function(id){
			self.attendanceId = id;
			$("#cancelConfirmLayer .mask-layer").show(300);
		};
		
		//取消请求
		this.cancelRequset = function(){
			if(self.attendanceId == null){
				return false;
			}
			hqTool.showLoad(self.businessId);
			$.ajax({
				url:"/learningCenter/web/courseAbnormalAbandonExam/updateCancel",
	            type:"GET",
				async:true,
				data:{
					"id" : self.attendanceId
				},
				dataType:"json",
	            success:function(data){
	            	hqTool.hideLoad();
	            	if(data.code == 200){
	            		window.location.reload();
	            		return false;
	            	}else{
	            		alert(data.message);
	            		return false;
	            	}
	            }
            });
		};
		
		//下拉框样式动画
		this.sliderAction = function(that){
			$(".select-bar .select").slideUp();
			$(".select-bar input").css('background','#fff');
			
			if(that.parent().find('.select').is(":hidden")){
				that.parent().find('input').css('background','#e6e6e6');
				that.parent().find('.select').slideDown();
			}
		};
		
		//下拉框选择数据
		this.selectList = function(that){
			var txt = that.text();
			that.parent().parent().find('input').val(txt);
			self.courseId = that.attr("data-id");
			that.parent().slideUp();
			that.parent().parent().find('input').css('background','#fff');
			$("#promptText").hide();
		}
	}
	window.pageScript = new pageScript().init();
}();