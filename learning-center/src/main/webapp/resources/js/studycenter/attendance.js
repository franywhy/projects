!function(){
	function pageScript(){
		var self = this;
			self.classType = '';
			self.userPlanId = '';
			self.stopTime = '';
			self.reopenTime = '';
			self.cause = '';
			self.businessId = pageParam.businessId;
			self.attendanceId = null;
			
		this.init = function(){
			self.datetimepicker('.datetimepicker');
			self.bindEvent();
			return self;
		};
		
		//事件绑定
		this.bindEvent = function(){
			$("#applyBtn").click(function(){
				$("#attFormLayer .mask-layer").show(300);
			});
			
			$("#attFormLayer .grey-close-p").click(function(){
				$("#attFormLayer .mask-layer").hide(300);
			});
			
			$("#classType").click(function(){
				if($("#selectList").is(":hidden")){
					$("#classType input").css('background','#e6e6e6');
					$("#selectList").slideDown();
				}else{
					$("#selectList").slideUp();
					$("#classType input").css('background','#fff');
				}
			});
			
			$("#selectList > .select-list").click(function(){
				var txt = $(this).text();
				$("#classType input").val(txt);
				self.userPlanId = $(this).attr("data-id");
				$("#selectList").slideUp();
				$("#classType input").css('background','#fff');
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
		
		//时间控件初始化
		this.datetimepicker = function(target){
			$.datetimepicker.setLocale('ch');
			$(target).datetimepicker({
				value:'',
				format:'Y-m-d',	//Y-m-d H:i
				maxDate:false,
				maxTime:false,
				minDate:new Date(),
				minTime:'0',
				yearStart:2017,
				yearEnd:'2100',
				allowBlank: false,
				allowTimes:[],	//可供选择的时间点,格式：09:00
				inline:false,	//是否直接显示日历控件
				todayButton:true,
				datepicker:true,
				weeks:false,
				timepicker:false,
			});
		};
		
		//表单校验
		this.checkForm = function(){
			self.classType = $.trim($("#classType input").val());
			self.stopTime = $.trim($("#stopTime input").val());
			self.reopenTime = $.trim($("#reopenTime input").val());
			self.cause = $.trim($("#cause textarea").val());
			if(self.classType == ''){
				$("#promptText .text-se").text('请输入完整的休学申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}else if(self.stopTime == ''){
				$("#promptText .text-se").text('请输入完整的休学申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}else if(self.reopenTime == ''){
				$("#promptText .text-se").text('请输入完整的休学申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}else if(hqTool.replaceNumber(self.stopTime) > hqTool.replaceNumber(self.reopenTime)){
				$("#promptText .text-se").text('开始时间不能大于结束时间');
				$("#promptText").css('display','inline-block');
				return false;
			}else if(self.cause == ''){
				$("#promptText .text-se").text('请输入完整的休学申请内容，不能有为空项。');
				$("#promptText").css('display','inline-block');
				return false;
			}
			return true;
		};
		
		/**
		 * 休学申请
		 * 提交表单请求
		 * @param self.userPlanId 班型ID
		 * @param self.stopTime 休学时间
		 * @param self.reopenTime 复课时间
		 * @param self.cause 原因
		 */
		this.submitForm = function(){
			hqTool.showLoad(self.businessId);
			$.ajax({
				url:"/learningCenter/web/courseabnormalorder/save",  
	            type:"GET",  
				async:true,
				data:{
					"orderId" : self.userPlanId,
					"startTime" : self.stopTime,
					"expectEndTime" : self.reopenTime,
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
				url:"/learningCenter/web/courseabnormalorder/updateCancel",
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
	}
	window.pageScript = new pageScript().init();
}();